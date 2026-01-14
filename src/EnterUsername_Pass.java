import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class EnterUsername_Pass extends JFrame {  // by hateem
    private JPanel header;
    private JTextField usernameTF;
    private JTextField passTF;
    private JButton exitButton;
    private JButton loginButton;
    private JPanel enterusernameandpass;

    public EnterUsername_Pass(boolean admin) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(screenSize.width, screenSize.height-70);
        setContentPane(enterusernameandpass);
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gs = ge.getDefaultScreenDevice();
        Rectangle screenBounds = gs.getDefaultConfiguration().getBounds();
        setLocation(screenBounds.x,screenBounds.y+20);
        setTitle("Login Page");
        Image icon1 = Toolkit.getDefaultToolkit().getImage("src/Images/SBL (1).png");
        setIconImage(icon1);
        Image icon = Toolkit.getDefaultToolkit().getImage("src/Images/SBL (1).png");
        setIconImage(icon);
        boolean isAdmin = admin;

        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String username = usernameTF.getText();
                String password = passTF.getText();
                if (isAdmin) {
                    try {
                        String[] adminDetails = Admin.loginAndgetAdminDetails(username, password);
                        if (adminDetails != null) {
                            Admin a = new Admin(adminDetails[0], adminDetails[1], adminDetails[2],adminDetails[3],adminDetails[4],adminDetails[5],adminDetails[6],adminDetails[7],adminDetails[8]);
                            JOptionPane.showMessageDialog(null, "Welcome " + username);
                            AdminDashboard ad=new AdminDashboard(a);
                            ad.setVisible(true);
                            dispose();
                        }
                        else {
                            JOptionPane.showMessageDialog(null, "Wrong username or password entered");
                        }
                    }
                    catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                } else {
                    try {
                        String[] accHolderDetails = AccountHolder.loginAndgetAccountDetails(username, password);
                        if (accHolderDetails != null) {
                            if (accHolderDetails[6].equalsIgnoreCase("Savings")) {
                                SavingsAccountHolder sah=new SavingsAccountHolder(accHolderDetails[0],accHolderDetails[1],Double.parseDouble(accHolderDetails[2]),accHolderDetails[3],accHolderDetails[4],accHolderDetails[5],accHolderDetails[6],accHolderDetails[7],accHolderDetails[8],accHolderDetails[9],accHolderDetails[10],accHolderDetails[11],accHolderDetails[12]);
                                JOptionPane.showMessageDialog(null, "Welcome " + username);
                                MainDashboard md=new MainDashboard(sah);
                                md.setVisible(true);
                                dispose();
                            }
                            else {
                                CurrentAccountHolder cah=new CurrentAccountHolder(accHolderDetails[0],accHolderDetails[1],Double.parseDouble(accHolderDetails[2]),accHolderDetails[3],accHolderDetails[4],accHolderDetails[5],accHolderDetails[6],accHolderDetails[7],accHolderDetails[8],accHolderDetails[9],accHolderDetails[10],accHolderDetails[11],accHolderDetails[12]);
                                JOptionPane.showMessageDialog(null, "Welcome " + username);
                                MainDashboard md=new MainDashboard(cah);
                                md.setVisible(true);
                                dispose();
                            }
                        } else {
                            JOptionPane.showMessageDialog(null, "Wrong username or password entered");
                        }
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
        });
        exitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                LoginForm lf=new LoginForm();
                lf.setVisible(true);
                dispose();
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new EnterUsername_Pass(false).setVisible(true);
        });
    }
}
