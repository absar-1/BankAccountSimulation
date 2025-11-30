import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.time.LocalDateTime;

public class BillPayment extends JFrame { // by hateem
    private JPanel header;
    private JTextField usertextfield;
    private JPanel billPayment;
    private JComboBox billsComboBox;
    private JTextField amountTF;
    private JButton payButton;
    private JButton backButton;

    public BillPayment(AccountHolder accountHolder) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(screenSize.width, screenSize.height-70);
        setContentPane(billPayment);
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gs = ge.getDefaultScreenDevice();
        Rectangle screenBounds = gs.getDefaultConfiguration().getBounds();
        setLocation(screenBounds.x,screenBounds.y+20);
        setTitle("Bill Payment");
        Image icon = Toolkit.getDefaultToolkit().getImage("src/Images/SBL (1).png");
        setIconImage(icon);
        usertextfield.setText(accountHolder.getName());
        usertextfield.setText(accountHolder.getName());
        billsComboBox.addItem("Select Bill");
        BillType[] bills=BillType.values();
        for (int i = 0; i < bills.length; i++) {
            if(bills[i].equals(BillType.SUI_GAS)){
                billsComboBox.addItem("SUI Gas");
            } else if (bills[i].equals(BillType.WATER_BILL)) {
                billsComboBox.addItem("Water Board");
            } else if (bills[i].equals(BillType.K_ELECTRIC)) {
                billsComboBox.addItem("K Electric");
            } else if (bills[i].equals(BillType.MOBILE_TOP_UP)) {
                billsComboBox.addItem("Mobile Top Up");
            } else if (bills[i].equals(BillType.FEE)) {
                billsComboBox.addItem("Fee");
            }
            else if(bills[i].equals(BillType.CHALLAN_PAYMENT)){
                billsComboBox.addItem("Challan Payment");
            }
            else if(bills[i].equals(BillType.WAPDA)){
                billsComboBox.addItem("WAPDA");
            }
        }
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    MainDashboard md = new MainDashboard(accountHolder);
                    md.setVisible(true);
                    dispose();
                }
                catch (IOException e1) {}
            }
        });
        payButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String selectedBill = billsComboBox.getSelectedItem().toString();
                if (selectedBill.equalsIgnoreCase("Select Bill")) {
                    JOptionPane.showMessageDialog(null, "You must select a valid bill");
                } else {
                    try {
                        if(amountTF.getText().matches(".*[a-zA-Z].*")){
                            JOptionPane.showMessageDialog(null, "Please enter a valid amount in number to transfer");
                        }
                        else if (Transaction.checkAmount(amountTF.getText())) {
                            double amount = Double.parseDouble(amountTF.getText());
                            if (amount <= accountHolder.getBalance()) {
                                Transaction t = new Transaction(accountHolder);
                                if (t.billPayment(amount, selectedBill)) {
                                    JOptionPane.showMessageDialog(null, selectedBill + " paid successfully");
                                    LocalDateTime dateNtime=LocalDateTime.now();
                                    Receipt r=new Receipt(selectedBill,amount,"Bill Payment","Date:" + dateNtime.getDayOfMonth() + "/" + dateNtime.getMonth() + "/" + dateNtime.getYear() + "," + "Time:" + dateNtime.getHour() + ":" + dateNtime.getMinute(),accountHolder);
                                    r.setVisible(true);
                                    dispose();
                                } else {
                                    JOptionPane.showMessageDialog(null, selectedBill + " payment failed");
                                }
                            } else {
                                JOptionPane.showMessageDialog(null, "Not enough balance");
                            }
                        } else {
                            JOptionPane.showMessageDialog(null, "Enter amount greater than 0 PKR");
                        }
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
        });
        }
    }

