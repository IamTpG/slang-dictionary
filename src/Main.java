import model.SlangDictionary;
import view.MainFrame;

public class Main {
    public static void main(String[] args) {
        SlangDictionary dictionary = new SlangDictionary("");
        new MainFrame(dictionary).setVisible(true);
    }
}
