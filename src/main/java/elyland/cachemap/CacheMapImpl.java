
package elyland.cachemap;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class CacheMapImpl<KeyType, ValueType> implements CacheMap<KeyType, ValueType> {

    private static final long DEFAULT_TTL = 60000;
    Map<KeyType, CacheEntry> map = new HashMap<>();
    LinkedList<KeyType> index = new LinkedList<>();
    long timeToLive;
    
    
    public CacheMapImpl() {
        timeToLive = DEFAULT_TTL;
    }
    
    public CacheMapImpl(long timeToLive) {
        this.timeToLive = timeToLive;
    }

    boolean entryExpired(CacheEntry entry) {
        return entry == null || (Clock.getTime() - entry.time) > timeToLive;
    }
    
    boolean removeEntry(Object key) {
        CacheEntry removed = map.remove(key);
        return  removed != null && index.remove(key);
    }

    @Override
    public void setTimeToLive(long timeToLive) {
        this.timeToLive = timeToLive;
    }

    @Override
    public long getTimeToLive() {
        return this.timeToLive;
    }

    @Override
    public ValueType put(KeyType key, ValueType value) {
        CacheEntry<KeyType, ValueType> entry = new CacheEntry<>(key, value, Clock.getTime());
        CacheEntry<KeyType, ValueType> oldEntry = map.put(key, entry);
        index.add(key);
        return entryExpired(oldEntry) ? null : oldEntry.value;
    }

    @Override
    public void clearExpired() {
        while (!index.isEmpty()) {
            KeyType last = index.getLast();
            CacheEntry entry = map.get(last);
            if (entryExpired(entry)) {
                map.remove(last);
                index.removeLast();
            } else {
                break;
            }
        }
    }

    @Override
    public void clear() {
        map.clear();
        index.clear();
    }

    @Override
    public boolean containsKey(Object key) {
        CacheEntry entry = map.get(key);
        return  !(entry == null || entryExpired(entry));
    }

    @Override
    public boolean containsValue(Object value) {
        for(CacheEntry entry : map.values()) {
            if (entry.value.equals(value)) {
                if (!entryExpired(entry)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public ValueType get(Object key) {
        CacheEntry<KeyType, ValueType> entry = map.get(key);
        if (entry == null) return null;
        if (entryExpired(entry)) {
            remove(key);
            return null;
        }
        return entry.value;
    }

    @Override
    public boolean isEmpty() {
        clearExpired();
        return map.isEmpty();
    }

    @Override
    public ValueType remove(Object key) {
        CacheEntry<KeyType, ValueType> entry = map.remove(key);
        if (entry != null) {
            index.remove(key);
        }
        return entry == null ? null : entry.value;
    }

    @Override
    public int size() {
        clearExpired();
        return map.size();
    }
}
