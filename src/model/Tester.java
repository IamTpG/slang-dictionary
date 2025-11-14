package model;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

public class Tester {
    public static Random random = new Random();
    public static final String MOCK_PATH = "data/mock.txt";

    public static String randStr(int n) {
        String str = "";
        int length = random.nextInt(1, n + 1);
        for (int i = 0; i < length; i++) {
            str += (char)(Math.random() * 26 + 97);
        }
        return str;
    }

    public static void createMockData() {
        try (PrintWriter pw = new PrintWriter(new File(MOCK_PATH))) {
            pw.println("Slang`Meaning");
            for (int i = 0; i < 100000; i++) {
                int r = random.nextInt(1, 11);
                if (r <= 6) pw.println(randStr(10) + "`" + randStr(20));
                if (r == 7 || r == 8) pw.println(randStr(10) + "`" + randStr(20) + "| "  + randStr(20));
                if (r == 9 || r == 10) pw.println(randStr(10) + "`" + randStr(20) + "| " +  randStr(20) + "| " +  randStr(20));
            }
        } catch (IOException e) {
            System.err.println("Could not open file.");
            return;
        }
    }

    public static void testFeature1() {
        SlangDictionary dictionary = new SlangDictionary(MOCK_PATH);
        String words[] = {"cbnj", "ohzsqr", "uvyub", "nyaipzwc", "hkhfxlpc", "srgc", "ny", "be", "rnowoah", "lwdosh"};

        for (String word : words) {
            long startTime = System.nanoTime();
            TreeSet<String> defs = dictionary.searchByWord(word);
            double durationMillis = (double) (System.nanoTime() - startTime) / 1_000_000.0;

            System.out.print(word + ": ");
            for (String def : defs) {
                System.out.print(def + ", ");
            }
            System.out.println();
            System.out.printf("Word: %s | Time: %.4f ms\n\n", word, durationMillis);
        }
    }

    public static void testFeature2() {
        SlangDictionary dictionary = new SlangDictionary(MOCK_PATH);
        List<String> keywords = Arrays.asList("cqzfmqzm", "dqielrlhlhzmved", "edateisiddyjvoqsdo", "eifynrtbufkddldm", "ezdlhdyfnncsiwmlr", "fyyfnyvjhcliatgv", "hduqzwpxhwgjgbb", "hrxrkudxdixtmfw", "iae", "aycwxaepdg", "bf", "eijl", "fszelyfpfhnmbdiflkff", "ghuxpyopsdfak", "ktu", "nozrzqjpvdnogoaux", "hfgyhvwghnfklmk", "kfgfwcj", "subvuxilxjntdtcppotc", "kxoncxzagjczu", "plkuyfzqyimfgeesgszv", "sdyvwbunxn", "tzydhuddyngh", "umfygmgbzdpfhtrwro", "nprpplwp", "nz", "qfswfxvmurqqwj", "ukpzomjkdytjnyqt", "vwxymvvvearorbemzkiz");
        Collections.shuffle(keywords);

        double sumTime = 0;
        double minTime = Double.MAX_VALUE;
        double maxTime = Double.MIN_VALUE;

        for (String keyword : keywords) {
            long startTime = System.nanoTime();
            Map<String, TreeSet<String>> dict = dictionary.searchByDefinition(keyword);
            double durationMillis = (double) (System.nanoTime() - startTime) / 1_000_000.0;

            System.out.print(keyword + ":\n");
            for (Map.Entry<String, TreeSet<String>> entry : dict.entrySet()) {
                System.out.print(entry.getKey() + ": ");
                for (String def : entry.getValue()) {
                    System.out.print(def + ", ");
                }
                System.out.println();
            }
            System.out.println();
            System.out.printf("Keyword: %s | Time: %.4f ms\n\n", keyword, durationMillis);

            sumTime += durationMillis;
            minTime = Math.min(minTime, durationMillis);
            maxTime = Math.max(maxTime, durationMillis);
        }

        System.out.printf("Min: %.4f ms | Max: %.4f ms | Avg: %.4f ms", minTime, maxTime, sumTime / (double)keywords.size());
    }
}
