import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class OpenIndividualAccount extends JFrame {
    private JPanel openAcc;
    private JComboBox acctypCombobox;
    private JTextField nameTF;
    private JButton createButton;
    private JButton exitButton;
    private JTextField ageTF;
    private JComboBox genderCombobox;
    private JTextField cnicTF;
    private JTextField contactTF;
    private JTextField addressTF;
    private JTextField emailTF;
    private JPanel header;
    private JTextField admintextfield;
    private JLabel adminNameLabel;

    public OpenIndividualAccount(Admin a) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(screenSize.width, screenSize.height-60);
        setContentPane(openAcc);
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gs = ge.getDefaultScreenDevice();
        Rectangle screenBounds = gs.getDefaultConfiguration().getBounds();
        setLocation(screenBounds.x,screenBounds.y+10);
        admintextfield.setText(a.getName());
        genderCombobox.addItem("Select Gender");
        setTitle("Open Account");
        Image icon = Toolkit.getDefaultToolkit().getImage("src/Images/SBL (1).png");
        setIconImage(icon);
        Gender[] genders=Gender.values();
        for (int i = 0; i < genders.length; i++) {
            if(genders[i]==Gender.MALE){
                genderCombobox.addItem("Male");
            }
            else if(genders[i]==Gender.FEMALE){
                genderCombobox.addItem("Female");
            }
        }
        acctypCombobox.addItem("Select Account Type");
        AccountTypes[] accountTypes=AccountTypes.values();
        for (int i = 0; i < accountTypes.length; i++) {
            if(accountTypes[i]==AccountTypes.CURRENT){
                acctypCombobox.addItem("Current");
            }
            else if(accountTypes[i]==AccountTypes.SAVINGS){
                acctypCombobox.addItem("Savings");
            }
        }


        createButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String name = nameTF.getText();
                String age = ageTF.getText();
                String gender = genderCombobox.getSelectedItem().toString();
                String cnic = cnicTF.getText();
                String contact = contactTF.getText();
                String address = addressTF.getText();
                String email = emailTF.getText();
                String acctyp = acctypCombobox.getSelectedItem().toString();
                try {
                            if (Validations.checkEmail(email,false)) {
                                if (!gender.equalsIgnoreCase("Select Gender")) {
                                    if (acctyp.equals("Select Account Type")) {
                                        JOptionPane.showMessageDialog(null, "Select valid account type");
                                    }
                                    else if (!Validations.checkContact(contact,false)) {
                                        JOptionPane.showMessageDialog(null, "Contact Number Invalid (must contain 11 digits) or already in use");
                                    }
                                    else if(!Validations.checkCnic(cnic,false)) {
                                        JOptionPane.showMessageDialog(null, "Cnic Number Invalid(must contain 13 digits) or already in use");
                                    }
                                    else if(!Validations.checkAddress(address)){
                                        JOptionPane.showMessageDialog(null, "Invalid Address");
                                    }
                                    else if(!Validations.checkAge(age)){
                                        JOptionPane.showMessageDialog(null, "Account Holder must be 18 or older");
                                    }
                                    else if(!Validations.checkName(name)){
                                        JOptionPane.showMessageDialog(null, "Invalid Name");
                                    }
                                    else if (acctyp.equalsIgnoreCase("Savings")) {
                                        try {
                                            AccountHolder acc = new SavingsAccountHolder(name, age, gender, acctyp, address, contact, cnic, email);
                                            JOptionPane.showMessageDialog(null, "Savings Account Created");

                                        } catch (IOException ex) {
                                            throw new RuntimeException(ex);
                                        }
                                    } else {
                                        try {
                                            AccountHolder acc = new CurrentAccountHolder(name, age, gender, acctyp, address, contact, cnic, email);
                                            JOptionPane.showMessageDialog(null, "Current Account Created");
                                            AdminDashboard ad = new AdminDashboard(a);
                                            ad.setVisible(true);
                                            dispose();
                                        } catch (IOException ex) {
                                            throw new RuntimeException(ex);
                                        }
                                    }
                                } else {
                                    JOptionPane.showMessageDialog(null, "Select a valid gender");
                                }
                            }
                            else{
                                JOptionPane.showMessageDialog(null, "Invalid email or already in use");
                            }
                }
                catch (IOException ex) {}
            }
        });
        exitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                AdminDashboard ad=new AdminDashboard(a);
                ad.setVisible(true);
                dispose();
            }
        });

    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                new OpenIndividualAccount(new Admin("ADM001", "Hateem Khan", "30", "Male", "admin_hateem", "Admin@123", "42101-9876543-2", "0300-1234567", "admin@example.com")).setVisible(true);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
