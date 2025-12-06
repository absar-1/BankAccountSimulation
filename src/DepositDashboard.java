import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.time.LocalDateTime;

public class DepositDashboard extends JFrame {  // by hateem
    private JPanel header;
    private JTextField amountTF;
    private JButton depositButton;
    private JButton exitButton;
    private JTextField userTF;
    private JPanel depositDashboard;
    private JLabel heading;
    private JTextField accNumTF;
    private JLabel accNumHead;

    public DepositDashboard(AccountHolder accountHolder) throws IOException {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(screenSize.width, screenSize.height - 70);
        setContentPane(depositDashboard);
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gs = ge.getDefaultScreenDevice();
        Rectangle screenBounds = gs.getDefaultConfiguration().getBounds();
        setLocation(screenBounds.x, screenBounds.y + 20);
        setTitle("Deposit Dashboard");
        Image icon = Toolkit.getDefaultToolkit().getImage("src/Images/SBL (1).png");
        setIconImage(icon);
        userTF.setText(accountHolder.getName());
        accNumTF.setVisible(false);
        accNumHead.setVisible(false);
        depositButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (amountTF.getText().matches(".*[a-zA-Z].*")) {
                    JOptionPane.showMessageDialog(null, "Please enter a valid amount in number to transfer");
                } else {
                    if (Transaction.checkAmount(amountTF.getText())) {
                        double amount = Double.parseDouble(amountTF.getText());
                        try {
                            LocalDateTime dateNtime = LocalDateTime.now();
                            boolean success = TransactionDAO.deposit(accountHolder.getAccountNumber(), amount, false);
                            if (success) {
                                JOptionPane.showMessageDialog(null, amount + " PKR Successfully Deposited");
                                Receipt r = new Receipt(amount, "Deposit", "Date:" + dateNtime.getDayOfMonth() + "/" + dateNtime.getMonth() + "/" + dateNtime.getYear() + "," + "Time:" + dateNtime.getHour() + ":" + dateNtime.getMinute(), accountHolder);
                                r.setVisible(true);
                                dispose();
                            } else {
                                JOptionPane.showMessageDialog(null, "Deposit failed. Please try again.");
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            JOptionPane.showMessageDialog(null, "Error during deposit: " + ex.getMessage());
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Enter Amount greater than 0 PKR");
                    }
                }
            }
        });

        exitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    MainDashboard md = new MainDashboard(accountHolder);
                    md.setVisible(true);
                    dispose();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                dispose();
            }
        });
    }
    public DepositDashboard(Admin admin) throws IOException {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(screenSize.width, screenSize.height - 70);
        setContentPane(depositDashboard);
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gs = ge.getDefaultScreenDevice();
        Rectangle screenBounds = gs.getDefaultConfiguration().getBounds();
        setLocation(screenBounds.x, screenBounds.y + 20);
        userTF.setText(admin.getName());
        heading.setText("DEPOSIT MONEY INTO CUSTOMER'S ACCOUNT");
        heading.setFont(new Font("Fira Code Medium", Font.BOLD, 26));
        depositButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (amountTF.getText().matches(".*[a-zA-Z].*")) {
                    JOptionPane.showMessageDialog(null, "Please enter a valid amount in number to transfer");
                } else {
                    if (Transaction.checkAmount(amountTF.getText())) {
                        double amount = Double.parseDouble(amountTF.getText());
                        String accNum=accNumTF.getText();
                        try {
                            if(AccountHolder.checkAccountHolder(accNum)) {
                                LocalDateTime dateNtime = LocalDateTime.now();
                                boolean success = TransactionDAO.deposit(accNum, amount, true);
                                if (success) {
                                    JOptionPane.showMessageDialog(null, amount + " PKR Successfully Deposited into Customer Account");
                                    AdminDashboard ad = new AdminDashboard(admin);
                                    ad.setVisible(true);
                                    dispose();
                                } else {
                                    JOptionPane.showMessageDialog(null, "Deposit failed. Please try again.");
                                }
                            }
                            else{
                                JOptionPane.showMessageDialog(null, "Account Holder not found");
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            JOptionPane.showMessageDialog(null, "Error during deposit: " + ex.getMessage());
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Enter Amount greater than 0 PKR");
                    }
                }
            }
        });

        exitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                AdminDashboard ad = new AdminDashboard(admin);
                ad.setVisible(true);
                dispose();
            }
        });
    }


    public static void main(String[] args) {
//        SwingUtilities.invokeLater(() -> {
//            try {
//                new DepositDashboard("001","Hateem","Savings").setVisible(true);
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//        });
    }
}
