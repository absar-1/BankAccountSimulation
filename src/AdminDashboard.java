import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class AdminDashboard extends JFrame { // by hateem
    private JPanel header;
    private JTextField admintextfield;
    private JButton openAccButton;
    private JButton logoutButton;
    private JButton depositIntoCustomerAcc;
    private JButton viewCustomer;
    private JButton viewAllAdmins;
    private JPanel adminDashboard;
    private JButton accRequestsButton;
    private JButton removeAdmin;
    private JButton removeAccHolder;
    private JButton viewStatement;
    private JButton viewBanks;
    private JButton viewLoanRequests;
    private JButton viewResponses;

    public AdminDashboard(Admin a) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(screenSize.width, screenSize.height-70);
        setContentPane(adminDashboard);
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gs = ge.getDefaultScreenDevice();
        Rectangle screenBounds = gs.getDefaultConfiguration().getBounds();
        setLocation(screenBounds.x,screenBounds.y+20);
        admintextfield.setText(a.getName());
        setTitle("Admin Dashboard");
        Image icon = Toolkit.getDefaultToolkit().getImage("src/Images/SBL (1).png");
        setIconImage(icon);
        if(a.getUsername().equalsIgnoreCase("hateem123")){
            viewAllAdmins.setVisible(true);
        }
        else{
            viewAllAdmins.setVisible(false);
        }
        logoutButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                LoginForm lf = new LoginForm();
                lf.setVisible(true);
                dispose();
            }
        });
        viewBanks.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    ViewAllBanks vab = new ViewAllBanks(a);
                    vab.setVisible(true);
                    dispose();
                }
                catch (IOException e1) {}
            }
        });
        openAccButton.addActionListener(new ActionListener() {
           public void actionPerformed(ActionEvent e) {
               OpenAccount oa=new OpenAccount(a);
               oa.setVisible(true);
               dispose();
           }
        });
        depositIntoCustomerAcc.addActionListener(new ActionListener() {
           public void actionPerformed(ActionEvent e) {
               try {
                   DepositDashboard dd = new DepositDashboard(a);
                   dd.setVisible(true);
                   dispose();
               }
               catch (IOException e1) {}
           }
        });
        viewAllAdmins.addActionListener(new ActionListener() {
           public void actionPerformed(ActionEvent e) {
               try {
                   View_All_Admins vaa = new View_All_Admins(a);
                   vaa.setVisible(true);
                   dispose();
               }
               catch (IOException e1) {}
           }
        });
        accRequestsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    RequestsDashboard rd = new RequestsDashboard(a);
                    rd.setVisible(true);
                    dispose();
                } catch (IOException ex) {throw new RuntimeException(ex);}
            }
        });
        removeAccHolder.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                removeCustomer rc=new removeCustomer(a);
                rc.setVisible(true);
                dispose();
            }
        });
        viewCustomer.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                   ViewCustomers vc = new ViewCustomers(a);
                    vc.setVisible(true);
                    dispose();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        viewStatement.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ViewAccHolderStatement vs=new ViewAccHolderStatement(a);
                vs.setVisible(true);
                dispose();
            }
        });
        viewLoanRequests.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    ViewLoanRequests vl = new ViewLoanRequests(a);
                    vl.setVisible(true);
                    dispose();
                }
                catch (IOException ex) {}
            }
        });
        viewResponses.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    View_Feedback_Queries_Suggestions vfqs = new View_Feedback_Queries_Suggestions(a);
                    vfqs.setVisible(true);
                    dispose();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });


    }
}
