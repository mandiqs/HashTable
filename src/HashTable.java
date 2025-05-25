public abstract class HashTable {
    protected int capacity;
    protected int numCollisions;
    protected BucketList[] table;
    protected int[] collisionsPerBucket;

    public HashTable(int capacity) {
        this.capacity = capacity;
        this.numCollisions = 0;
        this.table = new BucketList[capacity];
        this.collisionsPerBucket = new int[capacity];
        for (int i = 0; i < capacity; i++) {
            table[i] = new BucketList();
            collisionsPerBucket[i] = 0;
        }
    }


    protected abstract int hashFunction(String key);

    // Insere a chave, contando colisões se o bucket já tiver itens
    public void insert(String key) {
        int idx = hashFunction(key);
        if (table[idx].size() > 0) {
            numCollisions++;
            collisionsPerBucket[idx]++;
        }
        table[idx].add(key);
    }

    // Busca a chave no bucket
    public boolean search(String key) {
        int idx = hashFunction(key);
        return table[idx].contains(key);
    }

    // Retorna total de colisões
    public int getNumCollisions() {
        return numCollisions;
    }

    // Retorna distribuição de chaves por bucket
    public int[] getDistribution() {
        int[] dist = new int[capacity];
        for (int i = 0; i < capacity; i++) {
            dist[i] = table[i].size();
        }
        return dist;
    }

    // Retorna colisões de cada bucket
    public int[] getCollisionsPerBucket() {
        return collisionsPerBucket;
    }
}
