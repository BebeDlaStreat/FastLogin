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
package com.github.games647.fastlogin.core.message;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;

import java.util.UUID;

public class AuthenticateMessage implements ChannelMessage {

    public static final String AUTH_CHANNEL = "auth";

    private UUID playerUUID;

    public AuthenticateMessage(UUID playerUUID) {
        this.playerUUID = playerUUID;
    }

    public AuthenticateMessage() {
        //reading from
    }

    public UUID getPlayerUUID() {
        return playerUUID;
    }

    @Override
    public String getChannelName() {
        return AUTH_CHANNEL;
    }

    @Override
    public void readFrom(ByteArrayDataInput input) {
        playerUUID = UUID.fromString(input.readUTF());
    }

    @Override
    public void writeTo(ByteArrayDataOutput output) {
        output.writeUTF(playerUUID.toString());
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "{playerUUID='" + playerUUID + "'}";
    }
}
