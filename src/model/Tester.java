package model;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

public class Tester {
    public static final Random random = new Random();
    public static final String MOCK_PATH = "data/mock.txt";
    public SlangDictionary dict = new SlangDictionary(MOCK_PATH);

    public String randStr(int n) {
        String str = "";
        int length = random.nextInt(1, n + 1);
        for (int i = 0; i < length; i++) {
            str += (char)(Math.random() * 26 + 97);
        }
        return str;
    }

    public void createMockData() {
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

    public void testFeature1() {
        List<String> words = new ArrayList<>(Arrays.asList("cbnj", "ohzsqr", "uvyub", "nyaipzwc", "hkhfxlpc", "srgc", "ny", "be", "rnowoah", "lwdosh"));
        Collections.shuffle(words);
        words.subList(5, words.size()).clear();

        System.out.println("=== Feature 1 (Start): Search word ===");

        double sumTime = 0;
        double minTime = Double.MAX_VALUE;
        double maxTime = Double.MIN_VALUE;

        for (String word : words) {
            long startTime = System.nanoTime();
            List<String> defs = dict.searchByWord(word);
            double durationMillis = (double) (System.nanoTime() - startTime) / 1_000_000.0;

            System.out.print(word + ": ");
            for (String def : defs) {
                System.out.print(def + ", ");
            }
            System.out.println();
            System.out.printf("Word: %s | Time: %.4f ms\n", word, durationMillis);

            sumTime += durationMillis;
            minTime = Math.min(minTime, durationMillis);
            maxTime = Math.max(maxTime, durationMillis);
        }

        System.out.printf("Min: %.4f ms | Max: %.4f ms | Avg: %.4f ms\n", minTime, maxTime, sumTime / (double)words.size());
        System.out.println("=== Feature 1 (End): Search word ===\n");
    }

    public void testFeature2() {
        List<String> keywords = new ArrayList<>(Arrays.asList("cqzfmqzm", "dqielrlhlhzmved", "edateisiddyjvoqsdo", "eifynrtbufkddldm", "ezdlhdyfnncsiwmlr", "fyyfnyvjhcliatgv", "hduqzwpxhwgjgbb", "hrxrkudxdixtmfw", "iae", "aycwxaepdg", "bf", "eijl", "fszelyfpfhnmbdiflkff", "ghuxpyopsdfak", "ktu", "nozrzqjpvdnogoaux", "hfgyhvwghnfklmk", "kfgfwcj", "subvuxilxjntdtcppotc", "kxoncxzagjczu", "plkuyfzqyimfgeesgszv", "sdyvwbunxn", "tzydhuddyngh", "umfygmgbzdpfhtrwro", "nprpplwp", "nz", "qfswfxvmurqqwj", "ukpzomjkdytjnyqt", "vwxymvvvearorbemzkiz"));
        Collections.shuffle(keywords);
        keywords.subList(5, keywords.size()).clear();

        System.out.println("=== Feature 2 (Start): Search keyword ===");

        double sumTime = 0;
        double minTime = Double.MAX_VALUE;
        double maxTime = Double.MIN_VALUE;

        for (String keyword : keywords) {
            long startTime = System.nanoTime();
            Map<String, TreeSet<String>> data = dict.searchByDefinition(keyword);
            double durationMillis = (double) (System.nanoTime() - startTime) / 1_000_000.0;

            System.out.print(keyword + ":\n");
            for (Map.Entry<String, TreeSet<String>> entry : data.entrySet()) {
                System.out.print(entry.getKey() + ": ");
                for (String def : entry.getValue()) {
                    System.out.print(def + ", ");
                }
                System.out.println();
            }
            System.out.println();
            System.out.printf("Keyword: %s | Time: %.4f ms\n", keyword, durationMillis);

            sumTime += durationMillis;
            minTime = Math.min(minTime, durationMillis);
            maxTime = Math.max(maxTime, durationMillis);
        }

        System.out.printf("Min: %.4f ms | Max: %.4f ms | Avg: %.4f ms\n", minTime, maxTime, sumTime / (double)keywords.size());
        System.out.println("=== Feature 2 (End): Search keyword ===\n");
    }

    public void testFeature3() {
        System.out.println("=== Feature 3 (Start): Show History ===");

        long start = System.nanoTime();
        List<String> hs = dict.getHistory();
        double time = (System.nanoTime() - start) / 1_000_000.0;

        for (String h : hs) {
            System.out.println(h);
        }

        System.out.printf("History size: %d | Time: %.4f ms\n", hs.size(), time);
        System.out.println("=== Feature 3 (End): Show History ===\n");
    }

    public void testFeature4() {
        String word = "abcTest";
        String def = "definition test";

        System.out.println("=== Feature 4 (Start): Add Slang ===");

        long start = System.nanoTime();
        int code = dict.addSlang(word, def, false);
        double time = (System.nanoTime() - start) / 1_000_000.0;

        System.out.println("Return code: " + code);
        System.out.printf("Time: %.4f ms\n", time);
        System.out.println("=== Feature 4 (End): Add Slang ===\n");
    }

    public void testFeature5() {
        String word = "abcTest";
        String oldDef = "definition test";
        String newDef = "edited definition";

        System.out.println("=== Feature 5 (Start): Edit Slang ===");

        long start = System.nanoTime();
        boolean ok = dict.editSlang(word, oldDef, newDef);
        double time = (System.nanoTime() - start) / 1_000_000.0;

        System.out.println("Edit result: " + ok);
        System.out.printf("Time: %.4f ms\n", time);
        System.out.println("=== Feature 5 (End): Edit Slang ===\n");
    }

    public void testFeature6() {
        String word = "abcTest";

        System.out.println("=== Feature 6 (Start): Delete Slang ===");

        long start = System.nanoTime();
        boolean deleted = dict.deleteSlang(word);
        double time = (System.nanoTime() - start) / 1_000_000.0;

        System.out.println("Deleted: " + deleted);
        System.out.printf("Time: %.4f ms\n", time);
        System.out.println("=== Feature 6 (End): Delete Slang ===\n");
    }

    public void testFeature7() {
        System.out.println("=== Feature 7 (Start): Reset Dictionary ===");

        long start = System.nanoTime();
        dict.resetDictionary();
        double time = (System.nanoTime() - start) / 1_000_000.0;

        System.out.println("Dictionary reset.");
        System.out.printf("Time: %.4f ms\n", time);
        System.out.println("=== Feature 7 (End): Reset Dictionary ===\n");
    }

    public void testFeature8() {
        System.out.println("=== Feature 8 (Start): Random Slang ===");

        long start = System.nanoTime();
        Map.Entry<String, TreeSet<String>> entry = dict.randomSlang();
        double time = (System.nanoTime() - start) / 1_000_000.0;

        System.out.println(entry.getKey() + " : " + entry.getValue());
        System.out.printf("Time: %.4f ms\n", time);
        System.out.println("=== Feature 8 (End): Random Slang ===\n");
    }

    public void testFeature9() {
        System.out.println("=== Feature 9 (Start): Quiz For Word ===");

        long start = System.nanoTime();
        QuizQuestion q = dict.generateSlangToDefQuiz();
        double time = (System.nanoTime() - start) / 1_000_000.0;

        System.out.println(q.getQuestion());
        for (String ans : q.getAnswers()) {
            System.out.println(" - " + ans);
        }
        System.out.println("Correct: " + q.getCorrectAnswer());
        System.out.printf("Time: %.4f ms\n", time);
        System.out.println("=== Feature 9 (End): Quiz For Word ===\n");
    }

    public void testFeature10() {
        System.out.println("=== Feature 10 (Start): Quiz For Def ===");

        long start = System.nanoTime();
        QuizQuestion q = dict.generateDefToSlangQuiz();
        double time = (System.nanoTime() - start) / 1_000_000.0;

        System.out.println(q.getQuestion());
        for (String ans : q.getAnswers()) {
            System.out.println(" - " + ans);
        }
        System.out.println("Correct: " + q.getCorrectAnswer());
        System.out.printf("Time: %.4f ms\n", time);
        System.out.println("=== Feature 10 (End): Quiz For Def ===\n");
    }
}
