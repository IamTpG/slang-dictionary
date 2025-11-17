package model;

import java.util.List;

public class QuizQuestion {
    private String question;
    private List<String> answers;
    private String correctAnswer;

    public QuizQuestion(String question, List<String> answers, String correctAnswer) {
        this.question = question;
        this.answers = answers;
        this.correctAnswer = correctAnswer;
    }

    public String getQuestion() { return question; }
    public List<String> getAnswers() { return answers; }
    public String getCorrectAnswer() { return correctAnswer; }

    public boolean checkAnswer(String selectedAnswer) {
        return correctAnswer.equalsIgnoreCase(selectedAnswer);
    }
}
