package view;

import model.SlangDictionary;
import java.awt.*;
import javax.swing.*;
import java.util.Map;
import java.util.List;
import java.util.TreeSet;

public class SlangPanel extends JPanel {
    private SlangDictionary dictionary;

    private JTextField searchField;
    private JRadioButton rbSlang, rbDefinition;
    private JTextArea resultArea;
    private JButton btnSearch, btnRandom, btnAdd;

    public SlangPanel(SlangDictionary dictionary) {
        this.dictionary = dictionary;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // 1. Setup Search Area (North)
        add(createSearchPanel(), BorderLayout.NORTH);

        // 2. Setup Results Area (Center)
        resultArea = new JTextArea("Kết quả tra cứu sẽ hiển thị tại đây.");
        resultArea.setEditable(false);
        resultArea.setFont(new Font("Monospaced", Font.PLAIN, 18));
        add(new JScrollPane(resultArea), BorderLayout.CENTER);

        // 3. Setup CRUD/Random Buttons (South)
        add(createActionPanel(), BorderLayout.SOUTH);
    }

    private JPanel createSearchPanel() {
        JPanel searchPanel = new JPanel(new BorderLayout(10, 5));

        // Input Field
        searchField = new JTextField(30);
        searchField.setFont(new Font("Arial", Font.PLAIN, 18));
        searchField.addActionListener(e -> performSearch());
        searchPanel.add(searchField, BorderLayout.CENTER);

        // Radio Buttons
        JPanel radioPanel = new JPanel();
        rbSlang = new JRadioButton("Theo Slang Word", true);
        rbDefinition = new JRadioButton("Theo Definition");
        rbSlang.setFont(new Font("Arial", Font.PLAIN, 18));
        rbDefinition.setFont(new Font("Arial", Font.PLAIN, 18));
        ButtonGroup group = new ButtonGroup();
        group.add(rbSlang);
        group.add(rbDefinition);
        radioPanel.add(rbSlang);
        radioPanel.add(rbDefinition);
        searchPanel.add(radioPanel, BorderLayout.NORTH);

        // Search Button
        btnSearch = new JButton("TRA CỨU");
        btnSearch.setFont(new Font("Arial", Font.BOLD, 18));
        btnSearch.addActionListener(e -> performSearch());
        searchPanel.add(btnSearch, BorderLayout.EAST);

        return searchPanel;
    }

    private JPanel createActionPanel() {
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));
        Font largeBoldFont = new Font("Arial", Font.BOLD, 18);

        // Random Button
        btnRandom = new JButton("Random Slang");
        btnRandom.setFont(largeBoldFont);
        btnRandom.addActionListener(e -> showRandomSlang());
        actionPanel.add(btnRandom);

        // Add Button
        btnAdd = new JButton("Thêm Slang");
        btnAdd.setFont(largeBoldFont);
        btnAdd.addActionListener(e -> showAddDialog());
        actionPanel.add(btnAdd);

        // Edit Button
        JButton btnEdit = new JButton("Sửa Định nghĩa");
        btnEdit.setFont(largeBoldFont);
        btnEdit.addActionListener(e -> showEditDialog());
        actionPanel.add(btnEdit);

        // Delete Button
        JButton btnDelete = new JButton("Xóa Slang");
        btnDelete.setFont(largeBoldFont);
        btnDelete.addActionListener(e -> showDeleteDialog());
        actionPanel.add(btnDelete);

        return actionPanel;
    }

    private void performSearch() {
        String keyword = searchField.getText().trim();
        if (keyword.isEmpty()) {
            resultArea.setText("Vui lòng nhập từ khóa tìm kiếm.");
            return;
        }

        if (rbSlang.isSelected()) {
            // Chức năng 1: Tìm theo Slang Word
            List<String> definitions = dictionary.searchByWord(keyword);
            if (definitions != null) {
                resultArea.setText("SLANG WORD: " + keyword + "\n\nDEFINITIONS:\n- " + String.join("\n- ", definitions));
            } else {
                resultArea.setText("Không tìm thấy Slang Word: " + keyword);
            }
        } else {
            // Chức năng 2: Tìm theo Definition
            Map<String, TreeSet<String>> results = dictionary.searchByDefinition(keyword);
            if (!results.isEmpty()) {
                StringBuilder sb = new StringBuilder("Tìm thấy " + results.size() + " Slang Word chứa keyword '" + keyword + "':\n\n");
                results.forEach((word, defs) -> {
                    sb.append("SLANG: ").append(word).append("\n");
                    sb.append("Definitions:\n- ").append(String.join("\n- ", defs)).append("\n\n");
                });
                resultArea.setText(sb.toString());
            } else {
                resultArea.setText("Không tìm thấy Slang Word nào chứa definition: " + keyword);
            }
        }
    }

    private void showRandomSlang() {
        Map.Entry<String, TreeSet<String>> entry = dictionary.randomSlang();
        if (entry != null) {
            String word = entry.getKey();
            TreeSet<String> defs = entry.getValue();

            resultArea.setText("--- SLANG WORD HÔM NAY ---\n\n");
            resultArea.append("SLANG: " + word + "\n\n");
            resultArea.append("DEFINITIONS:\n- " + String.join("\n- ", defs));
        } else {
            resultArea.setText("Từ điển rỗng.");
        }
    }

    private void showAddDialog() {
        String word = JOptionPane.showInputDialog(this, "Nhập Slang Word mới:");
        if (word == null || word.trim().isEmpty()) return;

        String def = JOptionPane.showInputDialog(this, "Nhập Definition cho '" + word + "':");
        if (def == null || def.trim().isEmpty()) return;

        if (dictionary.searchByWord(word) != null) {
            int choice = JOptionPane.showOptionDialog(this,
                    "Slang Word '" + word + "' đã tồn tại. Bạn muốn làm gì?",
                    "Slang Word trùng lặp",
                    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null,
                    new String[]{"Ghi Đè Toàn Bộ Định Nghĩa Cũ (Overwrite)", "Thêm Định Nghĩa (Duplicate)"}, "Ghi Đè Toàn Bộ Định Nghĩa Cũ (Overwrite)");

            if (choice == JOptionPane.YES_OPTION) { // Ghi đè (overwrite = true)
                dictionary.addSlang(word, def, true);
                JOptionPane.showMessageDialog(this, "Đã ghi đè định nghĩa cho '" + word + "'.");
            } else if (choice == JOptionPane.NO_OPTION) { // Thêm định nghĩa (overwrite = false)
                int status = dictionary.addSlang(word, def, false);
                if (status == 2) {
                    JOptionPane.showMessageDialog(this, "Đã thêm định nghĩa mới vào '" + word + "'.");
                } else if (status == -1) {
                    JOptionPane.showMessageDialog(this, "Lỗi: Định nghĩa đã tồn tại.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            dictionary.addSlang(word, def, true);
            JOptionPane.showMessageDialog(this, "Đã thêm mới Slang Word: '" + word + "'.");
        }
    }

    private void showEditDialog() {
        String word = JOptionPane.showInputDialog(this, "Nhập Slang Word cần sửa định nghĩa:");
        if (word == null || word.trim().isEmpty()) return;

        List<String> defs = dictionary.searchByWord(word);

        if (defs == null || defs.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Slang Word không tồn tại hoặc không có định nghĩa nào.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // 1. Hiển thị hộp thoại chọn (JComboBox) để người dùng chọn định nghĩa cũ (oldDef)
        String[] defArray = defs.toArray(new String[0]);

        String oldDef = (String) JOptionPane.showInputDialog(
                this,
                "Chọn định nghĩa bạn muốn sửa:",
                "Chọn Định Nghĩa Cũ (Q5)",
                JOptionPane.QUESTION_MESSAGE,
                null, // Icon mặc định
                defArray,
                defArray[0] // Giá trị mặc định
        );

        if (oldDef == null) {
            return;
        }

        // 2. Nhập định nghĩa mới (newDef)
        String newDef = JOptionPane.showInputDialog(
                this,
                "Sửa định nghĩa cũ:\n'" + oldDef + "'\n\nThành định nghĩa mới (Để trống để xóa):",
                "Nhập Định Nghĩa Mới (Q5)",
                JOptionPane.QUESTION_MESSAGE
        );

        if (newDef != null) {
            if (dictionary.editSlang(word, oldDef, newDef)) {
                if (newDef.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Đã xóa thành công định nghĩa: '" + oldDef + "' của từ '" + word + "'.");
                } else {
                    JOptionPane.showMessageDialog(this, "Đã cập nhật định nghĩa cho '" + word + "'.", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Lỗi khi sửa định nghĩa. Vui lòng kiểm tra lại Slang Word.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void showDeleteDialog() {
        String wordToDelete = JOptionPane.showInputDialog(this, "Nhập Slang Word cần XÓA:");
        if (wordToDelete == null || wordToDelete.trim().isEmpty()) return;

        int confirm = JOptionPane.showConfirmDialog(this,
                "Bạn có chắc chắn muốn xóa Slang Word '" + wordToDelete + "' không?",
                "Xác nhận Xóa", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            if (dictionary.deleteSlang(wordToDelete)) {
                JOptionPane.showMessageDialog(this, "Đã xóa thành công Slang Word: '" + wordToDelete + "'.");
            } else {
                JOptionPane.showMessageDialog(this, "Lỗi: Slang Word không tồn tại.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
