package elyland.cachemap;

public class CacheEntry<KeyType, ValueType> {
    
    public CacheEntry(KeyType key, ValueType value, Long time) {
        this.value = value;
        this.key = key;
        this.time = time;
    }
    
    ValueType value;
    KeyType key;
    Long time;
}
