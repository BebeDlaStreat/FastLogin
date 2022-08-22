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
package com.github.games647.fastlogin.bungee.hook;

import com.github.games647.fastlogin.bungee.FastLoginBungee;
import com.github.games647.fastlogin.bungee.utils.BungeeLoginUtils;
import com.github.games647.fastlogin.core.utils.PasswordManager;
import com.github.games647.fastlogin.core.hooks.AuthPlugin;
import net.md_5.bungee.api.connection.ProxiedPlayer;

/**
 * GitHub:
 * <a href="https://github.com/vik1395/BungeeAuth-Minecraft">...</a>
 *
 * Project page:
 *
 * Spigot:
 * <a href="https://www.spigotmc.org/resources/bungeeauth.493/">...</a>
 */
public class FastLoginHook implements AuthPlugin<ProxiedPlayer> {

    private final FastLoginBungee plugin;
    public FastLoginHook(FastLoginBungee plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean forceLogin(ProxiedPlayer player) {
        if (!plugin.getAuthenticatedPlayers().contains(player.getUniqueId())) {
            new BungeeLoginUtils(player, plugin).onLogin();
            return true;
        }
        return false;
    }

    @Override
    public boolean isRegistered(String playerName) {
        return plugin.getCore().getStorage().isRegister(playerName);
    }

    @Override
    public boolean forceRegister(ProxiedPlayer player, String password) {
        plugin.getCore().getStorage().savePassword(
                player.getUniqueId(), player.getName(), new PasswordManager().hash(password)
        );
        new BungeeLoginUtils(player, plugin).onLogin();
        return true;
    }
}
