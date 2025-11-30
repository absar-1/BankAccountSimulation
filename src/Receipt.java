import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class Receipt extends JFrame {
    private JPanel header;
    private JLabel transactionTypeLabel;
    private JLabel senderLabel;
    private JLabel amountLabel;
    private JLabel receiverLabel;
    private JLabel dateTimeLabel;
    private JButton exitButton;
    private JPanel receiptDashboard;

    public Receipt(double amount,String transactionType,String dateTime,AccountHolder accountHolder) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(screenSize.width, screenSize.height-70);
        setContentPane(receiptDashboard);
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gs = ge.getDefaultScreenDevice();
        Rectangle screenBounds = gs.getDefaultConfiguration().getBounds();
        setLocation(screenBounds.x,screenBounds.y+20);
        setTitle("Receipt");
        Image icon = Toolkit.getDefaultToolkit().getImage("src/Images/SBL (1).png");
        setIconImage(icon);
        receiverLabel.setVisible(false);
        transactionTypeLabel.setText(transactionType);
        senderLabel.setText("From: "+accountHolder.getName());
        amountLabel.setText("Amount: "+amount);
        dateTimeLabel.setText(dateTime);


        exitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    if(accountHolder.getAccountType().equalsIgnoreCase("Savings")) {
                        AccountHolder sah=SavingsAccountHolder.getAccountHolderObject(accountHolder.getId());
                        MainDashboard mainDashboard = new MainDashboard(sah);
                        mainDashboard.setVisible(true);
                        dispose();
                    }
                    else{
                        AccountHolder cah=CurrentAccountHolder.getAccountHolderObject(accountHolder.getId());
                        MainDashboard mainDashboard = new MainDashboard(cah);
                        mainDashboard.setVisible(true);
                        dispose();
                    }
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
    }
    public Receipt(String receiverName,double amount,String transactionType,String dateTime,AccountHolder accountHolder) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(screenSize.width, screenSize.height-70);
        setContentPane(receiptDashboard);
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gs = ge.getDefaultScreenDevice();
        Rectangle screenBounds = gs.getDefaultConfiguration().getBounds();
        setLocation(screenBounds.x,screenBounds.y+20);
        setTitle("Receipt");
        Image icon = Toolkit.getDefaultToolkit().getImage("src/Images/SBL (1).png");
        setIconImage(icon);
        transactionTypeLabel.setText(transactionType);
        senderLabel.setText("From: "+accountHolder.getName());
        amountLabel.setText("Amount: "+amount);
        receiverLabel.setText("To: "+receiverName);
        dateTimeLabel.setText(dateTime);
        exitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    if(accountHolder.getAccountType().equalsIgnoreCase("Savings")) {
                        AccountHolder sah=SavingsAccountHolder.getAccountHolderObject(accountHolder.getId());
                        MainDashboard mainDashboard = new MainDashboard(sah);
                        mainDashboard.setVisible(true);
                        dispose();
                    }
                    else{
                        AccountHolder cah=CurrentAccountHolder.getAccountHolderObject(accountHolder.getId());
                        MainDashboard mainDashboard = new MainDashboard(cah);
                        mainDashboard.setVisible(true);
                        dispose();
                    }
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
    }
}
