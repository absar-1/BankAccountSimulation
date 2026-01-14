import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class ViewAccHolderStatement extends JFrame {
    private JButton searchButton;
    private JPanel viewCustomers;
    private JTextField accNumTF;
    private JButton backButton;
    private JTextField adminTF;

    public ViewAccHolderStatement(Admin a) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(screenSize.width, screenSize.height-60);
        setContentPane(viewCustomers);
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gs = ge.getDefaultScreenDevice();
        Rectangle screenBounds = gs.getDefaultConfiguration().getBounds();
        setLocation(screenBounds.x,screenBounds.y+10);
        adminTF.setText(a.getName());
        setTitle("Account Holder Statement");
        Image icon = Toolkit.getDefaultToolkit().getImage("src/Images/SBL (1).png");
        setIconImage(icon);
        searchButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String customeraccNum = accNumTF.getText();
                try {
                    if(AccountHolder.checkAccount(customeraccNum)) {
                        String[] accHolderDetails=AccountHolder.getAccountdetails(customeraccNum);
                        if(accHolderDetails[8].equalsIgnoreCase("Savings")){
                            TransactionsDashboard td=new TransactionsDashboard(SavingsAccountHolder.getAccountObject(accHolderDetails[0]),a);
                            td.setVisible(true);
                            dispose();
                        }
                        else{
                            TransactionsDashboard td=new TransactionsDashboard(CurrentAccountHolder.getAccountHolderObject(accHolderDetails[0]),a);
                            td.setVisible(true);
                            dispose();
                        }
                    }
                    else{
                        JOptionPane.showMessageDialog(null, "No Account with this Account Number Found");
                    }
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                AdminDashboard admin=new AdminDashboard(a);
                admin.setVisible(true);
                dispose();
            }
        });

    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                new ViewAccHolderStatement(new Admin("ADM001", "Hateem Khan", "30", "Male", "admin_hateem", "Admin@123", "42101-9876543-2", "0300-1234567", "admin@example.com")).setVisible(true);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
