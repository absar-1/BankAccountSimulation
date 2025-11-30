import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class ChangeByAccountHolder extends JFrame { // by hateem
    private JTextField usertextfield;
    private JTextField newTF;
    private JTextField confirmTF;
    private JButton backButton;
    private JButton changeButton;
    private JPanel changeUsername;
    private JPanel header;
    private JLabel iconLabel;
    private JLabel headingLabel;
    private JLabel newLabel;
    private JLabel confirmLabel;

    public ChangeByAccountHolder(AccountHolder accountHolder, String changeX) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(screenSize.width, screenSize.height-70);
        setContentPane(changeUsername);
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gs = ge.getDefaultScreenDevice();
        Rectangle screenBounds = gs.getDefaultConfiguration().getBounds();
        setLocation(screenBounds.x,screenBounds.y+20);
        setTitle(changeX);
        Image icon = Toolkit.getDefaultToolkit().getImage("src/Images/SBL (1).png");
        setIconImage(icon);
        headingLabel.setText(changeX);
        ImageIcon icon1 = new ImageIcon("./images/heading.png");
//        if(changeX.equalsIgnoreCase("Change Username")){
//            icon=new ImageIcon("src/Images/user (4) (1).png");
//            newLabel.setText("New Username");
//            confirmLabel.setText("Confirm Username");
//        }
//        else
        if(changeX.equalsIgnoreCase("Change Password")){
            icon1=new ImageIcon("src/Images/padlock (1).png");
            newLabel.setText("New Password");
            confirmLabel.setText("Confirm Password");
        }
        else if(changeX.equalsIgnoreCase("Change Email")){
            icon1=new ImageIcon("src/Images/email (1).png");
            newLabel.setText("New Email");
            confirmLabel.setText("Confirm Email");
        }
        else if(changeX.equalsIgnoreCase("Change Address")) {
            icon1=new ImageIcon("src/Images/location-pin (1).png");
            newLabel.setText("New Address");
            confirmLabel.setText("Confirm Address");
        }
        iconLabel.setIcon(icon1);
        usertextfield.setText(accountHolder.getName());
        changeButton.addActionListener(new ActionListener() {
           public void actionPerformed(ActionEvent e) {
               if (changeX.equalsIgnoreCase("Change Password")) {
                   String password = newTF.getText();
                   String confirmPassword = confirmTF.getText();
                   if (password.equals(confirmPassword)) {
                       if (accountHolder.getPassword().equals(password)) {
                           JOptionPane.showMessageDialog(header, "Password is the same as current password");
                       }
                       else{
                           if(Validations.checkPassword(password,false)){
                               try {
                                   if(accountHolder.change(accountHolder.getAccountNumber(),password,6)) {
                                       JOptionPane.showMessageDialog(header, "Password has been changed");
                                       MainDashboard md=new MainDashboard(accountHolder);
                                       md.setVisible(true);
                                       dispose();
                                   }
                                   else{
                                       JOptionPane.showMessageDialog(header, "There was an error changing the password");
                                   }
                               } catch (IOException ex) {
                                   throw new RuntimeException(ex);
                               }
                           }
                           else{
                               JOptionPane.showMessageDialog(header, "The password should be atleast 4 characters long and should have digits only");
                           }
                       }
                   }
                   else{
                       JOptionPane.showMessageDialog(null, "The passwords do not match");
                   }

               }
               else if(changeX.equalsIgnoreCase("Change Email")) {
                   String email = newTF.getText();
                   String confirmEmail = confirmTF.getText();
                   if (email.equalsIgnoreCase(confirmEmail)) {
                       if (accountHolder.getEmail().equalsIgnoreCase(email)) {
                           JOptionPane.showMessageDialog(header, "Email is the same as current email");
                       }
                       else{
                           try {
                               if (Validations.checkEmail(email,accountHolder.getAccountNumber())) {
                                   try {
                                       if (accountHolder.change(accountHolder.getAccountNumber(),email,12)) {
                                           JOptionPane.showMessageDialog(header, "Email has been changed");
                                           MainDashboard md = new MainDashboard(accountHolder);
                                           md.setVisible(true);
                                           dispose();
                                       } else {
                                           JOptionPane.showMessageDialog(header, "There was an error changing the email");
                                       }
                                   } catch (IOException ex) {
                                       throw new RuntimeException(ex);
                                   }
                               } else {
                                   JOptionPane.showMessageDialog(header, "The email is either not valid or already in use");
                               }
                           } catch (Exception ex) {
                               throw new RuntimeException(ex);
                           }
                       }
                   }
                   else{
                       JOptionPane.showMessageDialog(null, "The emails do not match");
                   }
               }
               else if(changeX.equalsIgnoreCase("Change Address")) {
                   String address = newTF.getText();
                   String confirmAddress = confirmTF.getText();
                   if (address.equalsIgnoreCase(confirmAddress)) {
                       if (accountHolder.getAddress().equalsIgnoreCase(address)) {
                           JOptionPane.showMessageDialog(header, "Address is the same as current address");
                       }
                       else {
                           if (Validations.checkAddress(address)) {
                               try {
                                   if (accountHolder.change(accountHolder.getAccountNumber(),address,9)) {
                                       JOptionPane.showMessageDialog(header, "Address has been changed");
                                       MainDashboard md = new MainDashboard(accountHolder);
                                       md.setVisible(true);
                                       dispose();
                                   } else {
                                       JOptionPane.showMessageDialog(header, "There was an error changing the address");
                                   }
                               } catch (IOException ex) {
                                   throw new RuntimeException(ex);
                               }
                           } else {
                               JOptionPane.showMessageDialog(header, "Invalid Address! The address should be at least 4 character long and should have atleast 1 alphabet.");
                           }
                       }
                   }
                   else{
                       JOptionPane.showMessageDialog(null, "The addresses do not match");
                   }
               }
           }
        });
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    MainDashboard md = new MainDashboard(accountHolder);
                    md.setVisible(true);
                    dispose();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
    }
}
