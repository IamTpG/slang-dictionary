package model;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

public class SlangDictionary {
    private final String SRC_PATH = "data/slang.txt";
    private final String CUR_PATH = "data/current_slang.txt";

    private Map<String, TreeSet<String>> data = null;
    private HistoryManager historyManager = null;

    public SlangDictionary() {
        data = new HashMap<>();
        historyManager = new HistoryManager();

        if (!loadFromFile(CUR_PATH)) {
            loadFromFile(SRC_PATH);
        }
    }

    private boolean loadFromFile(String path) {
        File file = new File(path);
        if (!file.exists()) return false;

        data.clear();
        try (Scanner sc = new Scanner(file, StandardCharsets.UTF_8)) {
            // Skip header
            if (sc.hasNextLine()) {
                sc.nextLine();
            }

            while (sc.hasNextLine()) {
                String line = sc.nextLine().trim();
                String[] parts = line.split("`", 2);

                if (parts.length != 2) continue;

                String word = parts[0].trim();
                List<String> definitions = Arrays.stream(parts[1].split("\\|"))
                        .map(String::trim)
                        .toList();

                if (!word.isEmpty() && !definitions.isEmpty()) {
                    data.computeIfAbsent(word, k -> new TreeSet<>(String.CASE_INSENSITIVE_ORDER))
                            .addAll(definitions);
                }
            }

            return (data.size() > 0);
        } catch (IOException e) {
            System.err.println("Error loading file: " + path + ". " + e.getMessage());
            return false;
        }
    }

    public void saveToFile() {
        try (PrintWriter pw = new PrintWriter(new File(CUR_PATH), StandardCharsets.UTF_8)) {
            pw.println("Slang`Meaning");
            for (Map.Entry<String, TreeSet<String>> entry : data.entrySet()) {
                String definitions = String.join("| ", entry.getValue());
                pw.println(entry.getKey() + "`" + definitions);
            }
        } catch (IOException e) {
            throw new RuntimeException("Error saving dictionary to file: " + CUR_PATH, e);
        }
    }

    // Chức năng 1: Tìm kiếm theo slang word
    public TreeSet<String> searchByWord(String word) {
        if (word == null || word.isEmpty()) return null;
        historyManager.addHistory(word);
        return data.get(word);
    }

    // Chức năng 2: Tìm kiếm theo definition
    public Map<String, TreeSet<String>> searchByDefinition(String keyword) {
        Map<String, TreeSet<String>> results = new HashMap<>();
        if (keyword == null || keyword.isEmpty()) return results;
        String lowerCaseKeyword = keyword.toLowerCase();

        for (Map.Entry<String, TreeSet<String>> entry : data.entrySet()) {
            TreeSet<String> matchingDefs = entry.getValue().stream()
                    .filter(def -> def.toLowerCase().contains(lowerCaseKeyword))
                    .collect(Collectors.toCollection(() -> new TreeSet<>(String.CASE_INSENSITIVE_ORDER)));

            if (!matchingDefs.isEmpty()) {
                results.put(entry.getKey(), matchingDefs);
            }
        }
        return results;
    }

    // Chức năng 3: Hiển thị history
    public List<String> getHistory() {
        return historyManager.getHistory();
    }

    // Chức năng 4: Add 1 slang word mới
    public int addSlang(String word, String definition, boolean overwrite) {
        if (word == null || word.trim().isEmpty() || definition == null || definition.trim().isEmpty()) {
            return -2;
        }

        word = word.trim();
        definition = definition.trim();

        if (data.containsKey(word)) {
            if (overwrite) {
                TreeSet<String> newDefinitions = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
                newDefinitions.add(definition);
                data.put(word, newDefinitions);
                saveToFile();
                return 0;
            } else {
                boolean definitionAdded = data.get(word).add(definition);
                if (definitionAdded) {
                    saveToFile();
                    return 2;
                }
                return -1;
            }
        } else {
            TreeSet<String> definitions = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
            definitions.add(definition);
            data.put(word, definitions);
            saveToFile();
            return 1;
        }
    }

    // Chức năng 5: Edit 1 slang word
    public boolean editSlang(String word, String oldDef, String newDef) {
        if (!data.containsKey(word)) {
            return false;
        }

        word = word.trim();
        oldDef = oldDef.trim();
        newDef = newDef.trim();

        TreeSet<String> definitions = data.get(word);

        if (!definitions.contains(oldDef)) {
            return false;
        }

        definitions.remove(oldDef);
        if (newDef.isEmpty()) {
            if (definitions.isEmpty()) {
                data.remove(word);
                System.out.println("Word '" + word + "' deleted because it had no definitions left.");
            }
        } else {
            definitions.add(newDef);
        }

        saveToFile();
        return true;
    }

    // Chức năng 6: Delete 1 slang word
    public boolean deleteSlang(String word) {
        if (data.containsKey(word)) {
            data.remove(word);
            saveToFile();
            return true;
        }
        return false;
    }

    // Chức năng 7: Reset danh sách slang words gốc.
    public void resetDictionary() {
        loadFromFile(SRC_PATH);
        new File(CUR_PATH).delete();
        historyManager.clearHistory();
    }

    // Chức năng 8: Random 1 slang word (On this day slang word)
    // Chức năng 9 & 10: Tạo Quiz (Tạo câu hỏi từ Slang/Definition ngẫu nhiên)
}
