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
package com.github.games647.fastlogin.bukkit.hook;

import com.github.games647.fastlogin.bukkit.FastLoginBukkit;
import com.github.games647.fastlogin.bukkit.utils.SpigotLoginUtils;
import com.github.games647.fastlogin.core.hooks.AuthPlugin;
import com.github.games647.fastlogin.core.utils.PasswordManager;
import org.bukkit.entity.Player;

/**
 * GitHub: <a href="https://github.com/Xephi/AuthMeReloaded/">...</a>
 * <p>
 * Project page:
 * <p>
 * Bukkit: <a href="https://dev.bukkit.org/bukkit-plugins/authme-reloaded/">...</a>
 * <p>
 * Spigot: <a href="https://www.spigotmc.org/resources/authme-reloaded.6269/">...</a>
 */
public class FastLoginHook implements AuthPlugin<Player> {

    private final FastLoginBukkit plugin;

    public FastLoginHook(FastLoginBukkit plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean forceLogin(Player player) {
        if (!plugin.getAuthenticatedPlayers().contains(player.getUniqueId())) {
            plugin.getAuthenticatedPlayers().add(player.getUniqueId());
            new SpigotLoginUtils(player).onLogin();
            return true;
        }
        return false;
    }

    @Override
    public boolean isRegistered(String playerName) {
        return plugin.getCore().getStorage().isRegister(playerName);
    }

    @Override
    //this automatically login the player too
    public boolean forceRegister(Player player, String password) {
        if (!plugin.getAuthenticatedPlayers().contains(player.getUniqueId())) {

            plugin.getCore().getStorage().savePassword(
                    player.getUniqueId(), player.getName(), new PasswordManager().hash(password)
            );
            plugin.getAuthenticatedPlayers().add(player.getUniqueId());
            new SpigotLoginUtils(player).onLogin();
            return true;
        }
        return false;
    }
}
