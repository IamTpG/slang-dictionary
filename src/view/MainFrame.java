package view;

import model.SlangDictionary;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class MainFrame extends JFrame {
    private SlangDictionary dictionary;

    public MainFrame(SlangDictionary dictionary) {
        this.dictionary = dictionary;

        setTitle("Slang Dictionary App");
        setSize(900, 600);
        setMinimumSize(new Dimension(700, 500));
        setLocationRelativeTo(null);

        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dictionary.quit();
                dispose();
                System.exit(0);
            }
        });

        setupTabs();
    }

    private void setupTabs() {
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Arial", Font.BOLD, 18));

        // 1. Tab Tra Cứu & Quản Lý
        SlangPanel slangPanel = new SlangPanel(dictionary);
        tabbedPane.addTab("1. Tra Cứu & Quản Lý", slangPanel);

        // 2. Tab Lịch Sử
        HistoryPanel historyPanel = new HistoryPanel(dictionary);
        tabbedPane.addTab("2. Lịch Sử Tra Cứu", historyPanel);

        // 3. Tab Đố Vui
        QuizPanel quizPanel = new QuizPanel(dictionary);
        tabbedPane.addTab("3. Đố Vui Slang", quizPanel);

        // 4. Tab Reset
        JPanel resetPanel = createResetPanel();
        tabbedPane.addTab("4. Reset", resetPanel);

        add(tabbedPane, BorderLayout.CENTER);
    }

    private JPanel createResetPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        JLabel label = new JLabel("Chức năng này sẽ khôi phục từ điển về trạng thái gốc (slang.txt).");
        label.setFont(new Font("Arial", Font.PLAIN, 16));
        JButton resetButton = new JButton("THỰC HIỆN RESET");
        resetButton.setFont(new Font("Arial", Font.BOLD, 18));
        resetButton.setBackground(Color.RED);
        resetButton.setForeground(Color.WHITE);

        resetButton.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Bạn có chắc chắn muốn RESET toàn bộ từ điển và lịch sử không?",
                    "Xác nhận Reset", JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                dictionary.resetDictionary();
                JOptionPane.showMessageDialog(this, "Reset thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        gbc.gridy = 0;
        panel.add(label, gbc);

        gbc.gridy = 1;
        panel.add(resetButton, gbc);

        return panel;
    }
}
