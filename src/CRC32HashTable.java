public class CRC32HashTable extends HashTable {

    public CRC32HashTable(int capacity) {
        super(capacity);
    }

    @Override
    protected int hashFunction(String key) {
        int hash = 0xFFFFFFFF;
        for (int i = 0; i < key.length(); i++) {
            int c = key.charAt(i);
            hash ^= c;
            for (int j = 0; j < 8; j++) {
                if ((hash & 1) != 0) {
                    hash = (hash >>> 1) ^ 0xEDB88320;
                } else {
                    hash = hash >>> 1;
                }
            }
        }
        return Math.abs(hash ^ 0xFFFFFFFF) % capacity;
    }
}
