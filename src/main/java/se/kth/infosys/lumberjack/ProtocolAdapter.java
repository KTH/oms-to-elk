package se.kth.infosys.lumberjack;

import java.net.Socket;

/*
 * Copyright (c) 2017 Kungliga Tekniska högskolan
 * Copyright (c) 2015 Didier Fetter
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

import java.util.List;

import se.kth.infosys.lumberjack.util.AdapterException;

public interface ProtocolAdapter {
    public int sendEvents(List<Event> eventList) throws AdapterException;
    public void close() throws AdapterException;
    public String getServer();
    public int getPort();
    public boolean isConnected();
    public Socket getSocket();
}