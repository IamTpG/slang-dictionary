package view;

import model.SlangDictionary;
import model.QuizQuestion;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class QuizPanel extends JPanel {
    private SlangDictionary dictionary;
    private QuizQuestion currentQuestion;

    private JLabel questionLabel;
    private JPanel answerPanel;
    private ButtonGroup answerGroup;
    private JButton btnNext, btnCheck;

    public QuizPanel(SlangDictionary dictionary) {
        this.dictionary = dictionary;
        setLayout(new BorderLayout(15, 15));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // 1. Question Area (North)
        questionLabel = new JLabel("Chọn loại Quiz để bắt đầu.", SwingConstants.CENTER);
        questionLabel.setFont(new Font("Arial", Font.BOLD, 18));
        add(questionLabel, BorderLayout.NORTH);

        // 2. Answer Area (Center)
        answerPanel = new JPanel(new GridLayout(4, 1, 10, 10));
        add(answerPanel, BorderLayout.CENTER);

        // 3. Control Area (South)
        add(createControlPanel(), BorderLayout.SOUTH);

        // Bắt đầu câu đố
        generateNewQuiz(true);
    }

    private JPanel createControlPanel() {
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));

        JButton btnQ9 = new JButton("Quiz: Slang to Def");
        btnQ9.setFont(new Font("Arial", Font.BOLD, 18));
        btnQ9.addActionListener(e -> generateNewQuiz(true));
        controlPanel.add(btnQ9);

        JButton btnQ10 = new JButton("Quiz: Def to Slang");
        btnQ10.setFont(new Font("Arial", Font.BOLD, 18));
        btnQ10.addActionListener(e -> generateNewQuiz(false));
        controlPanel.add(btnQ10);

        btnCheck = new JButton("KIỂM TRA ĐÁP ÁN");
        btnCheck.setFont(new Font("Arial", Font.BOLD, 18));
        btnCheck.addActionListener(e -> checkAnswer());
        controlPanel.add(btnCheck);

        return controlPanel;
    }

    private void generateNewQuiz(boolean isSlangToDef) {
        if (isSlangToDef) {
            currentQuestion = dictionary.generateSlangToDefQuiz();
        } else {
            currentQuestion = dictionary.generateDefToSlangQuiz();
        }

        if (currentQuestion == null) {
            questionLabel.setText("Lỗi: Không đủ dữ liệu để tạo câu hỏi (Cần ít nhất 4 từ).");
            answerPanel.removeAll();
            answerPanel.revalidate();
            answerPanel.repaint();
            return;
        }

        questionLabel.setText(currentQuestion.getQuestion());
        displayAnswers(currentQuestion.getAnswers());
        btnCheck.setEnabled(true);
    }

    private void displayAnswers(List<String> answers) {
        answerPanel.removeAll();
        answerGroup = new ButtonGroup();

        for (String answer : answers) {
            JRadioButton rb = new JRadioButton(answer);
            rb.setFont(new Font("Arial", Font.PLAIN, 18));
            answerGroup.add(rb);
            answerPanel.add(rb);
        }

        answerPanel.revalidate();
        answerPanel.repaint();
    }

    private void checkAnswer() {
        if (currentQuestion == null) return;

        String selectedAnswer = "";
        ButtonModel selectedModel = answerGroup.getSelection();

        if (selectedModel == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một đáp án.", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Tìm đáp án đã chọn
        for (java.util.Enumeration<AbstractButton> buttons = answerGroup.getElements(); buttons.hasMoreElements();) {
            AbstractButton button = buttons.nextElement();
            if (button.getModel() == selectedModel) {
                selectedAnswer = button.getText();
                break;
            }
        }

        if (currentQuestion.checkAnswer(selectedAnswer)) {
            JOptionPane.showMessageDialog(this, "CHÍNH XÁC! Đáp án đúng là: " + currentQuestion.getCorrectAnswer(), "Kết quả", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "SAI RỒI. Đáp án đúng là: " + currentQuestion.getCorrectAnswer(), "Kết quả", JOptionPane.ERROR_MESSAGE);
        }

        btnCheck.setEnabled(false);
    }
}
