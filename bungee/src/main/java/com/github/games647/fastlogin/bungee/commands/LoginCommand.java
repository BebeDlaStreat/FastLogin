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
package com.github.games647.fastlogin.bungee.commands;

import com.github.games647.fastlogin.bungee.FastLoginBungee;
import com.github.games647.fastlogin.bungee.utils.BungeeLoginUtils;
import com.github.games647.fastlogin.core.utils.PasswordManager;
import com.github.games647.fastlogin.core.shared.FastLoginCore;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class LoginCommand extends Command {
    private final FastLoginBungee plugin;
    private final FastLoginCore<ProxiedPlayer, CommandSender, FastLoginBungee> core;

    public LoginCommand(FastLoginBungee plugin, FastLoginCore<ProxiedPlayer, CommandSender, FastLoginBungee> core) {
        super("login");
        this.plugin = plugin;
        this.core = core;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof ProxiedPlayer)) {
            sender.sendMessage(core.getMessage("no-console"));
            return;
        }
        ProxiedPlayer player = (ProxiedPlayer) sender;
        if (plugin.getAuthenticatedPlayers().contains(player.getUniqueId())) {
            player.sendMessage(core.getMessage("already-login"));
            return;
        }
        if (!core.getStorage().isRegister(player.getUniqueId())) {
            player.sendMessage(core.getMessage("not-register"));
            return;
        }
        if (args.length < 1) {
            player.sendMessage(core.getMessage("login-usage"));
        }
        String password = args[0];
        String hashedPassword = core.getStorage().getPassword(player.getUniqueId());
        PasswordManager passwordManager = new PasswordManager();
        if (passwordManager.isPassword(password, hashedPassword)) {
            new BungeeLoginUtils(player, plugin).onLogin();
            player.sendMessage(core.getMessage("login-success"));
        } else {
            player.disconnect(core.getMessage("wrong-password"));
        }

    }
}
