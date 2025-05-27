import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

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
            Boolean insert = fnvTable.insert(names.get(i));
            if(!insert){
                i = -1;
            }
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
            Boolean insert = sdbmTable.insert(names.get(i));
            if(!insert){
                i = -1;
            }
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

// Ask user for display preference
        Scanner scanner = new Scanner(System.in);
        System.out.println("Como deseja visualizar a tabela?");
        System.out.println("1 - Resumo paginado (apenas totais por faixa)");
        System.out.println("2 - Paginado detalhado (com detalhes de cada posição)");
        System.out.println("3 - Tudo de uma vez");
        System.out.print("Escolha (1, 2 ou 3): ");
        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        BucketList[] tabelaDados = table.table;
        int capacity = tabelaDados.length;

        if (choice == 1 || choice == 2) {
            // PAGINATED VIEW (Summary or Detailed)
            int pageSize = capacity / 10; // 10% of capacity
            int totalPages = (capacity + pageSize - 1) / pageSize; // Ceiling division

            System.out.println("=== TABELA DE HASH - VISUALIZAÇÃO PAGINADA ===");
            System.out.printf("Capacidade total: %d | Tamanho da página: %d | Total de páginas: %d%n%n",
                    capacity, pageSize, totalPages);

            // Always show summary of all pages
            System.out.println("=== RESUMO DAS PÁGINAS ===");
            System.out.printf("%-12s | %-10s | %-10s | %-10s%n", "Faixa", "Chaves", "Colisões", "Ocupação");
            System.out.println("-------------|------------|------------|------------");

            for (int page = 0; page < totalPages; page++) {
                int startIndex = page * pageSize;
                int endIndex = Math.min(startIndex + pageSize - 1, capacity - 1);

                // Calculate summary for this page
                int totalKeys = 0;
                int totalCollisions = 0;
                int occupiedBuckets = 0;

                for (int i = startIndex; i <= endIndex; i++) {
                    int bucketSize = tabelaDados[i].size();
                    totalKeys += bucketSize;
                    totalCollisions += table.getNumCollisions(i);
                    if (bucketSize > 0) {
                        occupiedBuckets++;
                    }
                }

                int pageSize_actual = endIndex - startIndex + 1;
                double occupationRate = (double) occupiedBuckets / pageSize_actual * 100;

                System.out.printf("%-12s | %-10d | %-10d | %-9.1f%%%n",
                        startIndex + "-" + endIndex,
                        totalKeys,
                        totalCollisions,
                        occupationRate);
            }

            // Only show detailed breakdown if choice is 2
            if (choice == 2) {
                System.out.println("\n=== DETALHES POR PÁGINA ===");

                for (int page = 0; page < totalPages; page++) {
                    int startIndex = page * pageSize;
                    int endIndex = Math.min(startIndex + pageSize - 1, capacity - 1);

                    System.out.printf("\n=== PÁGINA %d (%d-%d) ===%n", page + 1, startIndex, endIndex);
                    System.out.printf("%-8s | %-8s | %-8s%n", "Posição", "Chaves", "Colisões");
                    System.out.println("---------|----------|----------");

                    for (int i = startIndex; i <= endIndex; i++) {
                        System.out.printf("%-8d | %-8d | %-8d%n", i, tabelaDados[i].size(), table.getNumCollisions(i));
                    }

                    // Pause between pages (except for the last page)
                    if (page < totalPages - 1) {
                        System.out.print("\nPressione Enter para continuar para a próxima página...");
                        scanner.nextLine();
                    }
                }
            }
        } else {
            // SHOW ALL AT ONCE
            System.out.println("=== TABELA DE HASH - VISUALIZAÇÃO COMPLETA ===");
            System.out.printf("%-8s | %-8s | %-8s%n", "Posição", "Chaves", "Colisões");
            System.out.println("---------|----------|----------");

            for (int i = 0; i < capacity; i++) {
                System.out.printf("%-8d | %-8d | %-8d%n", i, tabelaDados[i].size(), table.getNumCollisions(i));
            }
            System.out.println();
        }

        System.out.printf("Factor: %.3f%n", table.getFactor());
        System.out.printf("Chaves Preenchidas: %d%n", table.filledBuckets);
        System.out.println("Número total de colisões: " + table.getNumCollisions());
        System.out.println("Capacidade: " + table.capacity);


    }
}
