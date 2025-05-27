public abstract class HashTable {
    protected int capacity;
    protected double factor;
    protected BucketList[] table;
    protected int filledBuckets = 0;

    public HashTable(int capacity) {
        this.capacity = capacity;
        this.factor = 0.98;
        this.table = new BucketList[capacity];
        for (int i = 0; i < capacity; i++) {
            table[i] = new BucketList();
        }
    }


    protected abstract int hashFunction(String key);

    // Insere a chave, contando colisões se o bucket já tiver itens
    public boolean insert(String key) {
        int idx = hashFunction(key);
        if (table[idx].size() == 0){
            filledBuckets += 1;
        }
        table[idx].add(key);

        if ((double) filledBuckets /capacity > factor){

            System.out.println("Number of Filled Keys :" + filledBuckets);
            System.out.println("Previous capacity:" + capacity);
            capacity = capacity * 2;
            filledBuckets = 0;
            System.out.println("Capacity Increased to :"+ capacity + "\n");
            this.table = new BucketList[capacity];
            for (int i = 0; i < capacity; i++) {
                table[i] = new BucketList();
            }
            return false;
        }
        return true;
    }

    public void checkFactor() {
        int filledKeys = 0;
        for(int i = 0; i < capacity; i++){
            if (table[i].size() == 0){
                filledKeys += 1;
            }
        }
        if((double) filledKeys /capacity > factor){
            System.out.println("Number of Filled Keys :" + filledKeys);
            System.out.println("Previous capacity:" + capacity);
            capacity = capacity * 2;
            System.out.println("Capacity Increased to :"+ capacity + "\n");
            this.table = new BucketList[capacity];
            for (int i = 0; i < capacity; i++) {
                table[i] = new BucketList();
            }
        }
    }

    public double getFactor(){
        return (double) filledBuckets /capacity;
    }

    // Busca a chave no bucket
    public boolean search(String key) {
        int idx = hashFunction(key);
        return table[idx].contains(key);
    }

    // Retorna total de colisões
    public int getNumCollisions() {
        int size = 0;
        for (int i = 0; i < capacity; i++) {
            if(table[i].size() > 0) {
                size += table[i].size() - 1;
            }
        }
        return size;
    }

    public int getNumCollisions(int i) {
        int size = 0;
            if(table[i].size() > 0) {
                size += table[i].size() - 1;
            }
        return size;
    }

    // Retorna distribuição de chaves por bucket
    public int[] getDistribution() {
        int[] dist = new int[capacity];
        for (int i = 0; i < capacity; i++) {
            dist[i] = table[i].size();
        }
        return dist;
    }

}
