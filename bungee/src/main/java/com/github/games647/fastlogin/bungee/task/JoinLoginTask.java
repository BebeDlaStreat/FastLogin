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
package com.github.games647.fastlogin.bungee.task;

import com.github.games647.fastlogin.bungee.FastLoginBungee;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.HashMap;
import java.util.UUID;

public class JoinLoginTask implements Runnable {
    private FastLoginBungee plugin;
    private static final HashMap<UUID, LoginInfos> LOGIN_INFOS = new HashMap<>();

    public JoinLoginTask(FastLoginBungee plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        new HashMap<>(LOGIN_INFOS).forEach((uuid, infos) -> {
            if (!infos.isLoginMessageSend() && infos.getJoin() + 2000 < System.currentTimeMillis()) {
                infos.setLoginMessageSend(true);
                if (!plugin.getAuthenticatedPlayers().contains(uuid)) {
                    ProxiedPlayer player = ProxyServer.getInstance().getPlayer(uuid);
                    if (player == null) {
                        LOGIN_INFOS.remove(uuid);
                        return;
                    }
                    if (plugin.getCore().getStorage().isRegister(uuid)) {
                        player.sendMessage(plugin.getCore().getMessage("login"));
                    } else {
                        player.sendMessage(plugin.getCore().getMessage("register"));
                    }
                }
            } else if (infos.getJoin() + 30000 < System.currentTimeMillis()) {
                LOGIN_INFOS.remove(uuid);
                ProxiedPlayer player = ProxyServer.getInstance().getPlayer(uuid);
                if (player == null) {
                    return;
                }
                player.disconnect(plugin.getCore().getMessage("time-up"));
            }
        });
    }

    public static HashMap<UUID, LoginInfos> getLoginInfos() {
        return LOGIN_INFOS;
    }

    public static class LoginInfos {
        private long join;
        private boolean loginMessageSend;

        public LoginInfos(long join, boolean loginMessageSend) {
            this.join = join;
            this.loginMessageSend = loginMessageSend;
        }

        public long getJoin() {
            return join;
        }

        public boolean isLoginMessageSend() {
            return loginMessageSend;
        }

        public void setLoginMessageSend(boolean loginMessageSend) {
            this.loginMessageSend = loginMessageSend;
        }
    }
}
