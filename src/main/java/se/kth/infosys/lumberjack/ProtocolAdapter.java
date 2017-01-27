package se.kth.infosys.lumberjack;

import java.util.List;

import se.kth.infosys.lumberjack.util.AdapterException;

public interface ProtocolAdapter {
    public int sendEvents(List<Event> eventList) throws AdapterException;
    public void close() throws AdapterException;
    public String getServer();
    public int getPort();
}