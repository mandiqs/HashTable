public class FNV1aHashTable extends HashTable {

    public FNV1aHashTable(int capacity) {
        super(capacity);
    }

    @Override
    protected int hashFunction(String key) {
        int h = 0x811c9dc5;
        for (int i = 0; i < key.length(); i++) {
            h ^= key.charAt(i);
            h *= 0x01000193;
        }
        h ^= (h >>> 16);
        return h & (capacity - 1);     // máscara (capacity = potência de 2)
    }
}

