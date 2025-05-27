public class DJB2HashTable extends HashTable {

    public DJB2HashTable(int capacity) {
        super(capacity);
    }

    @Override
    protected int hashFunction(String key) {
        long hash = 5381;
        for (int i = 0; i < key.length(); i++) {
            hash = ((hash << 5) + hash) + key.charAt(i); // hash * 33 + c
        }
        return (int)(Math.abs(hash) % capacity);
    }
}

