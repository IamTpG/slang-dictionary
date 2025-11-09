package model;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class SlangDictionary {
    private Map<String, TreeSet<String>> data = new HashMap<>();
    private final String SRC_PATH = "data/slang.txt";
    private final String CUR_PATH = "data/current_slang.txt";

    public SlangDictionary() {
        if (!loadFromFile(CUR_PATH)) {
            loadFromFile(SRC_PATH);
        }
    }

    private boolean loadFromFile(String path) {
        data.clear();
        try (Scanner sc = new Scanner(new File(path), StandardCharsets.UTF_8)) {
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
            return data.size() > 0;
        } catch (IOException e) {
            System.err.println("Error loading file: " + path + ". " + e.getMessage());
            return false;
        }
    }

    public void saveToFile() {
        try (PrintWriter pw = new PrintWriter(new File(CUR_PATH), StandardCharsets.UTF_8)) {
            for (Map.Entry<String, TreeSet<String>> entry : data.entrySet()) {
                String definitions = String.join("| ", entry.getValue());
                pw.println(entry.getKey() + "`" + definitions);
            }
        } catch (IOException e) {
            throw new RuntimeException("Error saving dictionary to file: " + CUR_PATH, e);
        }
    }

    // Chức năng 1: Tìm kiếm theo slang word
    // Chức năng 2: Tìm kiếm theo definition
    // Chức năng 3: Hiển thị history
    // Chức năng 4: Add 1 slang words mới
    // Chức năng 5: Edit 1 slang word (Thay thế định nghĩa cũ bằng định nghĩa mới)
    // Chức năng 6: Delete 1 slang word
    // Chức năng 8: Random 1 slang word (On this day slang word) 
    // Chức năng 9 & 10: Tạo Quiz (Tạo câu hỏi từ Slang/Definition ngẫu nhiên)
}
