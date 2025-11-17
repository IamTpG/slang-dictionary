package view;

import model.SlangDictionary;
import javax.swing.*;
import java.awt.*;

public class HistoryPanel extends JPanel {
    private SlangDictionary dictionary;
    private JList<String> historyList;
    private DefaultListModel<String> listModel;

    public HistoryPanel(SlangDictionary dictionary) {
        this.dictionary = dictionary;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel title = new JLabel("Lịch Sử Tra Cứu (Mới nhất đến Cũ nhất)", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 18));
        add(title, BorderLayout.NORTH);

        listModel = new DefaultListModel<>();
        historyList = new JList<>(listModel);
        historyList.setFont(new Font("Monospaced", Font.PLAIN, 18));

        // Thêm Listener để cập nhật lịch sử mỗi khi Panel được hiển thị
        addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorAdded(javax.swing.event.AncestorEvent event) {
                updateHistoryList();
            }
            public void ancestorRemoved(javax.swing.event.AncestorEvent event) {}
            public void ancestorMoved(javax.swing.event.AncestorEvent event) {}
        });

        add(new JScrollPane(historyList), BorderLayout.CENTER);

        JButton clearButton = new JButton("Xóa Lịch Sử");
        clearButton.setFont(new Font("Arial", Font.PLAIN, 18));
        clearButton.addActionListener(e -> {
            dictionary.clearHistory();
            updateHistoryList();
            JOptionPane.showMessageDialog(this, "Lịch sử đã được xóa.");
        });
        add(clearButton, BorderLayout.SOUTH);
    }

    private void updateHistoryList() {
        listModel.clear();
        for (String word : dictionary.getHistory()) {
            listModel.addElement(word);
        }
    }
}
