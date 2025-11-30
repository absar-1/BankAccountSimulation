import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginForm extends JFrame {
    private JPanel header;
    private JButton loginAsAdminButton;
    private JButton loginAsCustomerButton;
    private JPanel loginForm;
    private JButton requestButton;

    public LoginForm() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(screenSize.width, screenSize.height-70);
        setContentPane(loginForm);
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gs = ge.getDefaultScreenDevice();
        Rectangle screenBounds = gs.getDefaultConfiguration().getBounds();
        setLocation(screenBounds.x,screenBounds.y+20);
        setTitle("Welcome Page");
        Image icon = Toolkit.getDefaultToolkit().getImage("src/Images/SBL (1).png");
        setIconImage(icon);
        loginAsAdminButton.addActionListener(new ActionListener() {
           public void actionPerformed(ActionEvent e) {
                EnterUsername_Pass enterUsernamePass=new EnterUsername_Pass(true);
                enterUsernamePass.setVisible(true);
                dispose();
           }
        });
        loginAsCustomerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                EnterUsername_Pass enterUsernamePass=new EnterUsername_Pass(false);
                enterUsernamePass.setVisible(true);
                dispose();
            }
        });
        requestButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                RequestToCreateAccount requestToCreateAccount=new RequestToCreateAccount();
                requestToCreateAccount.setVisible(true);
                dispose();
            }
        });
    }
    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            new LoginForm().setVisible(true);
        });
    }
}
