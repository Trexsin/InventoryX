import javax.swing.SwingUtilities;
import view.StoreGUI;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new StoreGUI().setVisible(true));
    }
}
