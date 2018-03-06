package se.kth.integral.omstoelk;

import java.util.LinkedHashMap;
import java.util.Map;

@SuppressWarnings("serial")
public class FifoCache<K,V> extends LinkedHashMap<K,V> {
    private static final int DEFAULT_SIZE = 1000;
    private final int limit;

    public FifoCache() {
        this(DEFAULT_SIZE);
    }

    public FifoCache(int size) {
        this.limit = size;
    }

    @Override
    protected boolean removeEldestEntry(Map.Entry<K,V> eldest) {
        return size() > limit;
    }
}
