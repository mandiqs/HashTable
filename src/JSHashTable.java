public class JSHashTable extends HashTable {

    public JSHashTable(int capacity) {
        super(capacity);
    }

    @Override
    protected int hashFunction(String key) {
        int hash = 1315423911;
        for (int i = 0; i < key.length(); i++) {
            hash ^= ((hash << 5) + key.charAt(i) + (hash >> 2));
        }
        return Math.abs(hash) % capacity;
    }
}
