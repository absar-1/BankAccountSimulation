import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class MainDashboard extends JFrame {
    private JPanel mainDashboard;
    private JButton logoutButton;
    private JButton transferButton;
    private JButton viewTranscations;
    private JButton WithdrawMoney;
    private JButton DepositMoney;
    private JTextField nameField;
    private JTextField balanceField;
    private JPanel header;
    private JButton showHideButton;
    private JButton getInterest;
    private JComboBox menuComboBox;
    private JButton issueChequeBookButton;
    private JButton billPaymentButton;
    private JButton issueCard;
    private JButton viewCardDetails;
    private JButton viewLoanPackages;
    private JButton responseButton;
    private JPanel cardPanel;

    public MainDashboard(AccountHolder accountHolder) throws IOException {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(screenSize.width, screenSize.height-70);
        setContentPane(mainDashboard);
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gs = ge.getDefaultScreenDevice();
        Rectangle screenBounds = gs.getDefaultConfiguration().getBounds();
        setLocation(screenBounds.x,screenBounds.y+20);
        setTitle("Main Dashboard");
        Image icon = Toolkit.getDefaultToolkit().getImage("src/Images/SBL (1).png");
        setIconImage(icon);
        menuComboBox.setForeground(new Color(248,249,250));
        menuComboBox.addItem("      Menu");
        menuComboBox.addItem("      Change Password");
//        menuComboBox.addItem("      Change Username");
//        menuComboBox.addItem("      Change Email");
//        menuComboBox.addItem("      Change Address");
        menuComboBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String selected = (String) menuComboBox.getSelectedItem();
                if ("      Change Password".equals(selected)) {
                    ChangeByAccountHolder changeByAccountHolder=new ChangeByAccountHolder(accountHolder,"Change Password");
                    changeByAccountHolder.setVisible(true);
                    dispose();
                }
//                else if ("      Change Username".equals(selected)) {
//                    ChangeByAccountHolder changeByAccountHolder=new ChangeByAccountHolder(accountHolder,"Change Username");
//                    changeByAccountHolder.setVisible(true);
//                    dispose();
//                }
//                else if ("      Change Email".equals(selected)) {
//                    ChangeByAccountHolder changeByAccountHolder=new ChangeByAccountHolder(accountHolder,"Change Email");
//                    changeByAccountHolder.setVisible(true);
//                    dispose();
//                }
//                else if ("      Change Address".equals(selected)) {
//                    ChangeByAccountHolder changeByAccountHolder=new ChangeByAccountHolder(accountHolder,"Change Address");
//                    changeByAccountHolder.setVisible(true);
//                    dispose();
//                }
            }
        });
        if(accountHolder.getAccountType().equalsIgnoreCase("Savings")){getInterest.setVisible(true);}
        else{getInterest.setVisible(false);}
        nameField.setText("         Name:                    "+accountHolder.getName());
        balanceField.setText("      Balance (PKR) :                    ******");
        ImageIcon unhide=new ImageIcon("src/Images/unhide (3) (1).png");
        ImageIcon hide=new ImageIcon("src/Images/hidden (1).png");
        showHideButton.setIcon(unhide);
        showHideButton.addActionListener(new ActionListener() {
           public void actionPerformed(ActionEvent e) {
               if(balanceField.getText().equals("      Balance (PKR) :                    ******")){
                   balanceField.setText("      Balance (PKR) :                    "+accountHolder.getBalance());
                   showHideButton.setIcon(hide);
               }else{
                   balanceField.setText("      Balance (PKR) :                    ******");
                   showHideButton.setIcon(unhide);
               }
           }
        });
        getInterest.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                confirmPage ci = new confirmPage(accountHolder,true);
                ci.setVisible(true);
                dispose();
            }
        });
        billPaymentButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                BillPayment bp = new BillPayment(accountHolder);
                bp.setVisible(true);
                dispose();
            }
        });
        issueCard.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

            }
        });
        issueChequeBookButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                confirmPage cic=new confirmPage(accountHolder,false);
                cic.setVisible(true);
                dispose();
            }
        });
        DepositMoney.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    DepositDashboard dd=new DepositDashboard(accountHolder);
                    dd.setVisible(true);
                    dispose();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        WithdrawMoney.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                WithdrawDashboard wd=new WithdrawDashboard(accountHolder);
                wd.setVisible(true);
                dispose();
            }
        });
        viewTranscations.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    TransactionsDashboard td=new TransactionsDashboard(accountHolder);
                    td.setVisible(true);
                    dispose();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        issueCard.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    IssueDebitcredit idc = new IssueDebitcredit(accountHolder);
                    idc.setVisible(true);
                    dispose();
                }
                catch (IOException ex) {}
            }
        });
        logoutButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                LoginForm lf=new LoginForm();
                lf.setVisible(true);
                dispose();
            }
        });
        transferButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    TransferDashboard td = new TransferDashboard(accountHolder);
                    td.setVisible(true);
                    dispose();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        viewCardDetails.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    ViewCardDetails vcd = new ViewCardDetails(accountHolder);
                    vcd.setVisible(true);
                    dispose();
                }
                catch (IOException ex) {}
            }
        });
        viewLoanPackages.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    ViewLoanOptions vlo = new ViewLoanOptions(accountHolder);
                    vlo.setVisible(true);
                    dispose();
                }
                catch (IOException ex) {}
            }
        });
        responseButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                FeedBack_Queries fbq=new FeedBack_Queries(accountHolder);
                fbq.setVisible(true);
                dispose();
            }
        });



    }
    public static void main(String[] args) {
    }
}

