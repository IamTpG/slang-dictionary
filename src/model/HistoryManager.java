package model;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class HistoryManager {
    private final String PATH = "data/history.txt";
    private List<String> history = null;

    public HistoryManager() {
        history = new ArrayList<>();
        loadHistory();
    }

    public void addHistory(String word) {
        if (word == null || word.trim().isEmpty()) return;
        history.add(word);
    }

    public List<String> getHistory() {
        List<String> reversedHistory = new ArrayList<>(history);
        Collections.reverse(reversedHistory);
        return reversedHistory;
    }

    private void loadHistory() {
        history.clear();
        File file = new File(PATH);
        if (!file.exists()) return;

        System.out.println("Loading history file...");

        try (Scanner sc = new Scanner(file, StandardCharsets.UTF_8)) {
            while (sc.hasNextLine()) {
                String word = sc.nextLine().trim();
                if (!word.isEmpty()) {
                    history.add(word);
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading history: " + e.getMessage());
        }
    }

    public void saveHistory() {
        File file = new File(PATH);

        try (PrintWriter pw = new PrintWriter(
                new BufferedWriter(
                        new FileWriter(file, StandardCharsets.UTF_8, false)))) {

            for (String word : history) {
                pw.println(word);
            }

        } catch (IOException e) {
            System.err.println("Error writing history: " + e.getMessage());
        }
    }

    public void clearHistory() {
        history.clear();
        File file = new File(PATH);
        if (file.exists()) {
            file.delete();
        }
    }
}
