public class MyArrayList {
    private String[] data;
    private int size;


    public MyArrayList() {
        this.data = new String[16];
        this.size = 0;
    }


    public void add(String s) {
        if (size == data.length) {
            String[] novo = new String[data.length * 2];
            System.arraycopy(data, 0, novo, 0, data.length);
            data = novo;
        }
        data[size++] = s;
    }

    public String get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException(
                    "Índice " + index + " fora de alcance (size = " + size + ")");
        }
        return data[index];
    }

    public int size() {
        return size;
    }
}

