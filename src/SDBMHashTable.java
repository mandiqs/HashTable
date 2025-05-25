public class SDBMHashTable extends HashTable {

    public SDBMHashTable(int capacity) {
        super(capacity);
    }

    @Override
    protected int hashFunction(String key) {
        long hash = 0;
        for (int i = 0; i < key.length(); i++) {
            char c = key.charAt(i);
            hash = c + (hash << 6) + (hash << 16) - hash;
        }
        return (int)(Math.abs(hash) % capacity);
    }
}

