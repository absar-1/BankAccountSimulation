import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.time.LocalDateTime;

public class IssueDebitcredit extends JFrame {
    private JPanel header;
    private JTextField userTF;
    private JButton issueCreditCardButton;
    private JButton issueDebitCard;
    private JPanel issueCardPanel;
    private JButton exitButton;
    private JLabel senderLabel;
    private JButton issueDebitCardButton;
    AccountHolder accountholder;
    public IssueDebitcredit(AccountHolder accountHolder) throws IOException {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(screenSize.width, screenSize.height-70);
        setContentPane(issueCardPanel);
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gs = ge.getDefaultScreenDevice();
        Rectangle screenBounds = gs.getDefaultConfiguration().getBounds();
        setLocation(screenBounds.x,screenBounds.y+20);
        setTitle("Issue Card");
        Image icon = Toolkit.getDefaultToolkit().getImage("src/Images/SBL (1).png");
        setIconImage(icon);
        accountholder = accountHolder;
        setContentPane(issueCardPanel);
        userTF.setText(accountholder.getName());
        issueCreditCardButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    LocalDateTime dateNtime = LocalDateTime.now();
                    if (accountholder.getBalance() >= 2230) {
                        if (accountHolder.issueCard(cardType.CREDIT)) {
                            JOptionPane.showMessageDialog(null, "Credit Card Issued");
                            Receipt r=new Receipt("Credit Card Bought",2230,"Card Purchased","Date:" + dateNtime.getDayOfMonth() + "/" + dateNtime.getMonth() + "/" + dateNtime.getYear() + "," + "Time:" + dateNtime.getHour() + ":" + dateNtime.getMinute(),accountHolder);
                            r.setVisible(true);
                            dispose();
                        }
                        else{
                            JOptionPane.showMessageDialog(null, "Card Already issued");
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Insufficient Balance to issue Credit Card");
                    }
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        issueDebitCard.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if (accountholder.getBalance() >= 2230) {
                        LocalDateTime dateNtime = LocalDateTime.now();
                        if (accountHolder.issueCard(cardType.DEBIT)) {
                            JOptionPane.showMessageDialog(null, "Debit Card Issued");
                            Receipt r=new Receipt("Debit Card Bought",2230,"Card Purchased","Date:" + dateNtime.getDayOfMonth() + "/" + dateNtime.getMonth() + "/" + dateNtime.getYear() + "," + "Time:" + dateNtime.getHour() + ":" + dateNtime.getMinute(),accountHolder);
                            r.setVisible(true);
                            dispose();
                        }
                        else{
                            JOptionPane.showMessageDialog(null, "Card Already issued");
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Insufficient Balance to issue Debit Card");
                    }
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        exitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    MainDashboard md = new MainDashboard(accountHolder);
                    md.setVisible(true);
                    dispose();
                }
                catch (IOException ex) {}
            }
        });
    }
}






