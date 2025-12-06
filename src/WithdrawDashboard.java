import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;

public class WithdrawDashboard extends JFrame {
    private JPanel header;
    private JTextField userTF;
    private JPanel withdrawDashboard;
    private JTextField amountTF;
    private JButton backButton;
    private JButton withdrawButton;
    public WithdrawDashboard(AccountHolder accountHolder) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(screenSize.width, screenSize.height-70);
        setContentPane(withdrawDashboard);
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gs = ge.getDefaultScreenDevice();
        Rectangle screenBounds = gs.getDefaultConfiguration().getBounds();
        setLocation(screenBounds.x,screenBounds.y+20);
        userTF.setText(accountHolder.getName());
        setTitle("Withdraw Dashboard");
        Image icon = Toolkit.getDefaultToolkit().getImage("src/Images/SBL (1).png");
        setIconImage(icon);
        withdrawButton.addActionListener(new ActionListener() {
           public void actionPerformed(ActionEvent e) {
               if (amountTF.getText().matches(".*[a-zA-Z].*")) {
                   JOptionPane.showMessageDialog(null, "Please enter a valid amount in number to withdraw");
               } else {
                   if (Transaction.checkAmount(amountTF.getText())) {
                       double amount = Double.parseDouble(amountTF.getText());
                       if (amount % 500 == 0) {
                           try {
                               if (accountHolder.getBalance() >= amount) {
                                   boolean success = TransactionDAO.withdraw(accountHolder.getAccountNumber(), amount);
                                   if (success) {
                                       LocalDateTime dateNtime = LocalDateTime.now();
                                       JOptionPane.showMessageDialog(null, "Withdraw Successful");
                                       Receipt r = new Receipt(amount, "Withdrawal", "Date:" + dateNtime.getDayOfMonth() + "/" + dateNtime.getMonth() + "/" + dateNtime.getYear() + "," + "Time:" + dateNtime.getHour() + ":" + dateNtime.getMinute(), accountHolder);
                                       r.setVisible(true);
                                       dispose();
                                   } else {
                                       JOptionPane.showMessageDialog(null, "Withdrawal failed. Please check your balance.");
                                   }
                               } else {
                                   JOptionPane.showMessageDialog(null, "Insufficient Funds");
                               }
                           } catch (Exception e1) {
                               e1.printStackTrace();
                               JOptionPane.showMessageDialog(null, "Error during withdrawal: " + e1.getMessage());
                           }
                       }
                       else{
                           JOptionPane.showMessageDialog(null, "Enter amount in multiples of 500 PKR");
                       }
                   }
                   else {
                       JOptionPane.showMessageDialog(null, "Enter money greater than 0 PKR");
                   }
               }
           }
        });
        backButton.addActionListener(new ActionListener() {
           public void actionPerformed(ActionEvent e) {
               try {
                   MainDashboard md = new MainDashboard(accountHolder);
                   md.setVisible(true);
                   dispose();
               }
               catch (Exception e1) { }
           }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
//            new WithdrawDashboard(new SavingsAccountHolder("ID123", "Hateem Khan", 50000.0, "21", "Male", "hateem21", "ACC456789123", "StrongP@ssw0rd", "Savings", "123 Main Street", "0312-3456789", "42101-1234567-8", "hateem@example.com")).setVisible(true);
        });
    }
}
