import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;

public class ViewCardDetails extends JFrame {
    private JTable table1;
    private JButton exitButton;
    private JPanel header;
    private JTextField userTF;
    private JPanel cardDetails;
    private JButton backButton;


    public ViewCardDetails(AccountHolder accountHolder) throws IOException {
        setTitle("View Card Details - " + accountHolder.getName());
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(screenSize.width, screenSize.height - 70);
        setContentPane(cardDetails);
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gs = ge.getDefaultScreenDevice();
        Rectangle screenBounds = gs.getDefaultConfiguration().getBounds();
        setLocation(screenBounds.x, screenBounds.y + 20);
        Image icon = Toolkit.getDefaultToolkit().getImage("src/Images/SBL (1).png");
        setIconImage(icon);

        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Card Holder Name");
        model.addColumn("Card Number");
        model.addColumn("CVV");
        model.addColumn("Expiry Date");
        model.addColumn("Card Type");
        userTF.setText(accountHolder.getName());
        setContentPane(cardDetails);
        if(accountHolder.getCardDetails() == null) {
            JOptionPane.showMessageDialog(this, "No Card Details Found");
        }
        else {
            ArrayList<String> cardData = accountHolder.getCardDetails();
            model.addRow(cardData.toArray());
            table1.setModel(model);
            JPanel panel = new JPanel(new BorderLayout());
        }
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    MainDashboard md = new MainDashboard(accountHolder);
                    md.setVisible(true);
                    dispose();
                }
                catch (IOException e1) {}
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                new ViewCardDetails(new SavingsAccountHolder("ID123", "Hateem Khan", 50000.0, "21", "Male", "hateem21", "ACC456789123", "StrongP@ssw0rd", "Savings", "123 Main Street", "0312-3456789", "42101-1234567-8", "hateem@example.com")).setVisible(true);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
