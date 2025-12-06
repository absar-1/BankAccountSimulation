import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.time.LocalDateTime;

public class EnterAmountToTransfer extends JFrame { // by hateem
    private JPanel header;
    private JTextField userTF;
    private JLabel newLabel;
    private JTextField amountTF;
    private JButton transferButton;
    private JButton exitButton;
    private JPanel enterAmountPanel;

    public EnterAmountToTransfer(String receiverAccNum,String bankName,AccountHolder accountHolder,String receiverName) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(screenSize.width, screenSize.height-70);
        setContentPane(enterAmountPanel);
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gs = ge.getDefaultScreenDevice();
        Rectangle screenBounds = gs.getDefaultConfiguration().getBounds();
        setLocation(screenBounds.x,screenBounds.y+20);
        setTitle("Transfer Money");
        Image icon = Toolkit.getDefaultToolkit().getImage("src/Images/SBL (1).png");
        setIconImage(icon);
        userTF.setText(accountHolder.getName());
        transferButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(amountTF.getText().matches(".*[a-zA-Z].*")){
                    JOptionPane.showMessageDialog(null, "Please enter a valid amount in number to transfer");
                }
                else {
                    if (Transaction.checkAmount(amountTF.getText())) {
                        double amount = Double.parseDouble(amountTF.getText());
                        try {
                            if (amount <= accountHolder.getBalance()) {
                                LocalDateTime dateNtime = LocalDateTime.now();
                                boolean success = TransactionDAO.transfer(
                                    accountHolder.getAccountNumber(),
                                    receiverAccNum,
                                    bankName,
                                    amount,
                                    receiverName
                                );

                                if (success) {
                                    JOptionPane.showMessageDialog(null, "Money Transfer Successful");
                                    Receipt r = new Receipt(receiverName, amount, "Transfer",
                                        "Date:" + dateNtime.getDayOfMonth() + "/" + dateNtime.getMonth() + "/" +
                                        dateNtime.getYear() + "," + "Time:" + dateNtime.getHour() + ":" +
                                        dateNtime.getMinute(), accountHolder);
                                    r.setVisible(true);
                                    dispose();
                                } else {
                                    JOptionPane.showMessageDialog(null, "Transfer failed. Please check receiver account or balance.");
                                }
                            } else {
                                JOptionPane.showMessageDialog(null, "Insufficient Funds");
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            JOptionPane.showMessageDialog(null, "Error during transfer: " + ex.getMessage());
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Enter Valid Amount (greater than 0 and less than your balance)");
                    }
                }
            }
        });
        exitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    TransferDashboard td = new TransferDashboard(accountHolder);
                    td.setVisible(true);
                    dispose();
                }
                catch (IOException ex) {}
            }
        });
    }
}
