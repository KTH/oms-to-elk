package se.kth.integral.omstoelk;

import java.util.LinkedHashMap;
import java.util.Map;

@SuppressWarnings("serial")
public class FifoCache<K,V> extends LinkedHashMap<K,V> {
    public static final int SIZE = 1000;

    @Override
    protected boolean removeEldestEntry(Map.Entry<K,V> eldest) {
        return size() > SIZE;
    }
}
