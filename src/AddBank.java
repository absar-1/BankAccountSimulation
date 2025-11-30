import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AddBank extends JFrame {  // by hateem
    private JPanel header;
    private JTextField admintextfield;
    private JTextField bankNameTF;
    private JButton addButton;
    private JButton backButton;
    private JPanel addBankPanel;

    public AddBank(Admin a) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(screenSize.width, screenSize.height-70);
        setContentPane(addBankPanel);
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gs = ge.getDefaultScreenDevice();
        Rectangle screenBounds = gs.getDefaultConfiguration().getBounds();
        setLocation(screenBounds.x,screenBounds.y+20);
        admintextfield.setText(a.getName());
        setTitle("Add Bank");
        Image icon = Toolkit.getDefaultToolkit().getImage("src/Images/SBL (1).png");
        setIconImage(icon);
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String bankName=bankNameTF.getText();
                try {
                    if (bankName == null || bankName.isEmpty() || bankName.isBlank() || !bankName.matches("^[a-zA-Z ]+$")) {
                        JOptionPane.showMessageDialog(null, "Please enter a valid bank name");
                    }
                    else if (a.addBank(bankName)) {
                        JOptionPane.showMessageDialog(null, bankName+" added");
                        ViewAllBanks vab = new ViewAllBanks(a);
                        vab.setVisible(true);
                        dispose();
                    }
                    else {
                        JOptionPane.showMessageDialog(null, "Bank already added");
                    }
                }
                catch (Exception ex) {}
            }
        });
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    ViewAllBanks vab = new ViewAllBanks(a);
                    vab.setVisible(true);
                    dispose();
                }
                catch (Exception ex) {}
            }
        });
    }
}
