import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.time.LocalDateTime;

public class confirmPage extends JFrame { // by hateem
    private JPanel confirmPanel;
    private JButton noButton;
    private JButton yesButton;
    private JLabel questionLabel;

    public confirmPage(AccountHolder accountHolder, boolean IsinterestConfirmation) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(screenSize.width, screenSize.height-70);
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gs = ge.getDefaultScreenDevice();
        Rectangle screenBounds = gs.getDefaultConfiguration().getBounds();
        setLocation(screenBounds.x,screenBounds.y+20);
        setContentPane(confirmPanel);
        setTitle("Confirmation");
        Image icon = Toolkit.getDefaultToolkit().getImage("src/Images/SBL (1).png");
        setIconImage(icon);
        if (!IsinterestConfirmation) {
            questionLabel.setText("The chequebook costs 500 PKR. Are you sure you want to buy the chequebook?");
            setTitle("Confirm Issue Cheque");
            yesButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    try {
                        if (accountHolder.getBalance() >= 500) {
                            boolean success = TransactionDAO.issueChequebook(accountHolder.getAccountNumber());
                            if (success) {
                                JOptionPane.showMessageDialog(null, "Chequebook Issued Successfully");
                                LocalDateTime dateNtime = LocalDateTime.now();
                                Receipt r = new Receipt("Chequebook Bought", 500, "Chequebook Purchased",
                                    "Date:" + dateNtime.getDayOfMonth() + "/" + dateNtime.getMonth() + "/" +
                                    dateNtime.getYear() + "," + "Time:" + dateNtime.getHour() + ":" +
                                    dateNtime.getMinute(), accountHolder);
                                r.setVisible(true);
                                MainDashboard md = new MainDashboard(accountHolder);
                                md.setVisible(true);
                                dispose();
                            } else {
                                JOptionPane.showMessageDialog(null, "Failed to issue chequebook. Please check your balance.");
                            }
                        } else {
                            JOptionPane.showMessageDialog(null, "You have not enough money to issue chequebook");
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(null, "Error issuing chequebook: " + ex.getMessage());
                    }
                }
            });
            noButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    try {
                        MainDashboard md = new MainDashboard(accountHolder);
                        md.setVisible(true);
                        dispose();
                    } catch (IOException ex) {
                    }
                }
            });
        }
        else {
            try {
                SavingsAccountHolder sah = SavingsAccountHolder.getAccountObject(accountHolder.getId());
                double interestAmount = sah.getInterestAmount();
                questionLabel.setText("You'll get total interest " +interestAmount+" PKR. Are you sure you want to deposit it into account ?");
                setTitle("Confirm Interest Deposit");
                yesButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        try {
                            if (sah.getBalanceWithInterest()) {
                                LocalDateTime dateNtime = LocalDateTime.now();
                                JOptionPane.showMessageDialog(null, interestAmount + " PKR Interest Successfully Deposited");
                                Receipt r = new Receipt(interestAmount, "Interest Deposit", "Date:" + dateNtime.getDayOfMonth() + "/" + dateNtime.getMonth() + "/" + dateNtime.getYear() + "," + "Time:" + dateNtime.getHour() + ":" + dateNtime.getMinute(), accountHolder);
                                r.setVisible(true);
                                dispose();
                            }
                            else{
                                JOptionPane.showMessageDialog(null, "You have 0 PKR balance so no interest");
                            }
                        } catch (IOException ex) {
                        }
                    }
                });
                noButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        try {
                            MainDashboard md = new MainDashboard(accountHolder);
                            md.setVisible(true);
                            dispose();
                        } catch (IOException ex) {
                        }
                    }
                });
            }
            catch (Exception e){}
        }
    }
    public confirmPage(Admin a,String ID,boolean isAdmin) throws IOException {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(screenSize.width, screenSize.height-70);
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gs = ge.getDefaultScreenDevice();
        Rectangle screenBounds = gs.getDefaultConfiguration().getBounds();
        setLocation(screenBounds.x,screenBounds.y+20);
        setContentPane(confirmPanel);
        Image icon = Toolkit.getDefaultToolkit().getImage("src/Images/SBL (1).png");
        setIconImage(icon);
        if(!isAdmin) {
        setTitle("Account Holder Removal Confirmation");
        String accType=AccountHolder.getAccountType(ID);
        if(accType.equalsIgnoreCase("Savings")) {
            questionLabel.setText("Are you sure you want to remove Account Holder : " +SavingsAccountHolder.getAccountObject(ID).getName());
            yesButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    try {
                        if (a.removeAccount(ID)) {
                            JOptionPane.showMessageDialog(null, "Customer Removed Successfully");
                            AdminDashboard AdminDashboard = new AdminDashboard(a);
                            AdminDashboard.setVisible(true);
                            dispose();
                        } else {
                            JOptionPane.showMessageDialog(null, "Failed to remove customer");
                            removeCustomer rc = new removeCustomer(a);
                            rc.setVisible(true);
                            dispose();
                        }
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }
                }
            });
            noButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    AdminDashboard AdminDashboard = new AdminDashboard(a);
                    AdminDashboard.setVisible(true);
                    dispose();
                }
            });
        }
        else {
            questionLabel.setText("Are you sure you want to remove Account Holder : " + CurrentAccountHolder.getAccountHolderObject(ID).getName());
            yesButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    try {
                        if (a.removeAccount(ID)) {
                            JOptionPane.showMessageDialog(null, "Customer Removed Successfully");
                            AdminDashboard AdminDashboard = new AdminDashboard(a);
                            AdminDashboard.setVisible(true);
                            dispose();
                        } else {
                            JOptionPane.showMessageDialog(null, "Failed to remove customer");
                            removeCustomer rc = new removeCustomer(a);
                            rc.setVisible(true);
                            dispose();
                        }
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }
                }
            });
            noButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    AdminDashboard AdminDashboard = new AdminDashboard(a);
                    AdminDashboard.setVisible(true);
                    dispose();
                }
            });
            }
        }
        else{
            setTitle("Admin Removal Confirmation");
            questionLabel.setText("Are you sure you want to remove Admin with ID : " + ID);
            yesButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    try {
                        if (a.removeAdmin(ID)) {
                            JOptionPane.showMessageDialog(null, "Admin Removed Successfully");
                            View_All_Admins vaa=new View_All_Admins(a);
                            vaa.setVisible(true);
                            dispose();
                        } else {
                            JOptionPane.showMessageDialog(null, "Failed to remove admin");
                            dispose();
                        }
                    }
                    catch (Exception ex) {}
                }
            });
            noButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    try {
                        View_All_Admins vaa = new View_All_Admins(a);
                        vaa.setVisible(true);
                        dispose();
                    }
                    catch (Exception ex) {}
                }
            });
        }
    }
}
