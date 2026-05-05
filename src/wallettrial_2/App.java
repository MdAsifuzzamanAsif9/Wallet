package wallettrial_2;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class App {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            // Falling back to the default look and feel keeps startup resilient.
        }

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new WalletUI().setVisible(true);
            }
        });
    }
}
