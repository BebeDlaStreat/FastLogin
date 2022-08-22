/*
 * SPDX-License-Identifier: MIT
 *
 * The MIT License (MIT)
 *
 * Copyright (c) 2015-2022 games647 and contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.github.games647.fastlogin.bukkit.listener;

import com.github.games647.fastlogin.bukkit.FastLoginBukkit;
import com.github.games647.fastlogin.bukkit.utils.SpigotLoginUtils;
import com.github.games647.fastlogin.core.message.AuthenticateMessage;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.jetbrains.annotations.NotNull;

public class BungeeAuthListener implements PluginMessageListener {

    private final FastLoginBukkit plugin;

    public BungeeAuthListener(FastLoginBukkit plugin) {
        this.plugin = plugin;
    }

    @Override
    public void onPluginMessageReceived(@NotNull String channel, Player player, byte[] message) {
        ByteArrayDataInput dataInput = ByteStreams.newDataInput(message);

        AuthenticateMessage authMessage = new AuthenticateMessage();
        authMessage.readFrom(dataInput);

        plugin.getLog().debug("Received plugin message {}", authMessage);

        Player targetPlayer = player;
        if (!authMessage.getPlayerUUID().equals(player.getUniqueId())) {
            targetPlayer = Bukkit.getPlayer(authMessage.getPlayerUUID());
        }

        if (targetPlayer == null) {
            plugin.getLog().warn("Auth action player {} not found", authMessage.getPlayerUUID());
            return;
        }

        // fail if target player is blocked because already authenticated or wrong bungeecord id
        if (plugin.getAuthenticatedPlayers().contains(authMessage.getPlayerUUID())) {
            plugin.getLog().warn("Received message {} from a already auth player {}", authMessage, targetPlayer);
        } else {
            plugin.getAuthenticatedPlayers().add(authMessage.getPlayerUUID());
            new SpigotLoginUtils(targetPlayer).onLogin();
        }
    }
}
