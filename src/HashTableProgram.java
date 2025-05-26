import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class HashTableProgram {

    // Negrito + cor verde
    private static final String ANSI_BOLD   = "\033[1m";
    private static final String ANSI_GREEN  = "\033[32m";
    private static final String ANSI_RESET  = "\033[0m";

    public static void main(String[] args) {
        int capacity = 16;
        MyArrayList names = readNames("female_names.txt");
        if (names == null) return;

        HashTable fnvTable  = new FNV1aHashTable(capacity);
        HashTable sdbmTable = new SDBMHashTable(capacity);


        // Mede desempenho FNV-1a
        long t0 = System.nanoTime();
        for (int i = 0; i < names.size(); i++) {
            fnvTable.insert(names.get(i));
        }
        long t1 = System.nanoTime();
        for (int i = 0; i < names.size(); i++) {
            fnvTable.search(names.get(i));
        }
        long t2 = System.nanoTime();
        printReport("FNV-1a", fnvTable, t1 - t0, t2 - t1);


        // Mede desempenho SDBM
        t0 = System.nanoTime();
        for (int i = 0; i < names.size(); i++) {
            sdbmTable.insert(names.get(i));
        }
        t1 = System.nanoTime();
        for (int i = 0; i < names.size(); i++) {
            sdbmTable.search(names.get(i));
        }
        t2 = System.nanoTime();
        printReport("SDBM", sdbmTable, t1 - t0, t2 - t1);
    }

    // Lê um nome por linha do txt e retorna no array
    private static MyArrayList readNames(String filename) {
        MyArrayList list = new MyArrayList();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (!line.isEmpty()) {
                    list.add(line);
                }
            }
        } catch (IOException e) {
            System.err.println("Erro ao ler arquivo: " + e.getMessage());
            return null;
        }
        return list;
    }

    // Relatório
    private static void printReport(String title, HashTable table, long insertNs, long searchNs) {
        System.out.println();
        System.out.println(
                ANSI_BOLD + ANSI_GREEN +
                        "Relatório: " + title +
                        ANSI_RESET
        );

        System.out.println("Número total de colisões: " + table.getNumCollisions());
        System.out.printf("Tempo total de inserção: %.3f ms%n", insertNs  / 1_000_000.0);
        System.out.printf("Tempo total de busca: %.3f ms%n", searchNs  / 1_000_000.0);
        System.out.println();
        System.out.printf("%-8s | %-8s | %-8s%n", "Posição", "Chaves", "Colisões");

        BucketList[] tabelaDados = table.table;
        for (int i = 0; i < tabelaDados.length; i++) {
            System.out.printf("%-8d | %-8d | %-8d%n", i, tabelaDados[i].size(), tabelaDados[i].size() - 1);
        }
    }
}
