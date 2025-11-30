import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class removeCustomer extends JFrame {
    private JPanel remCustomer;
    private JPanel header;
    private JTextField admintextfield;
    private JTextField idTF;
    private JButton removeCustomerButton;
    private JButton exitButton;
    private JButton button;

    public removeCustomer(Admin a) {
        admintextfield.setText(a.getName());
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(screenSize.width, screenSize.height - 70);
        setContentPane(remCustomer);
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gs = ge.getDefaultScreenDevice();
        Rectangle screenBounds = gs.getDefaultConfiguration().getBounds();
        setLocation(screenBounds.x, screenBounds.y + 20);
        setTitle("Remove Customer");
        Image icon = Toolkit.getDefaultToolkit().getImage("src/Images/SBL (1).png");
        setIconImage(icon);
        removeCustomerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String id = idTF.getText().trim();
                System.out.println(id);
                try {
                    System.out.println("enter try");
                    System.out.println("Opening confirm page");
                    confirmPage cP = new confirmPage(a, id,false);
                    System.out.println("confirm page made");
                    cP.setVisible(true);
                    dispose();
                }
                catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        });
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AdminDashboard adminDashboard=new AdminDashboard(a);
                adminDashboard.setVisible(true);
                dispose();
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
//            new removeCustomer().setVisible(true);
        });
    }
}
