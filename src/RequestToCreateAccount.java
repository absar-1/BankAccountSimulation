import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class RequestToCreateAccount extends JFrame {

    private JPanel header;
    private JLabel icon;
    private JLabel adminNameLabel;
    private JTextField nameTF;
    private JComboBox genderCombobox;
    private JComboBox accTypeComboBox;
    private JButton requestButton;
    private JButton exitButton;
    private JTextField ageTF;
    private JTextField cnicTF;
    private JTextField contactTF;
    private JTextField emailTF;
    private JTextField addressTF;
    private JPanel requestAcc;

    public RequestToCreateAccount() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(screenSize.width, screenSize.height - 70);
        setContentPane(requestAcc);
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gs = ge.getDefaultScreenDevice();
        Rectangle screenBounds = gs.getDefaultConfiguration().getBounds();
        setLocation(screenBounds.x, screenBounds.y + 20);
        genderCombobox.addItem("Select Gender");
        setTitle("Request to Create Account");
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
        accTypeComboBox.addItem("Select Account Type");
        AccountTypes[] accountTypes=AccountTypes.values();
        for (int i = 0; i < accountTypes.length; i++) {
            if(accountTypes[i]==AccountTypes.CURRENT){
                accTypeComboBox.addItem("Current");
            }
            else if(accountTypes[i]==AccountTypes.SAVINGS){
                accTypeComboBox.addItem("Savings");
            }
        }
        requestButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String name = nameTF.getText();
                String gender = genderCombobox.getSelectedItem().toString();
                String accType = accTypeComboBox.getSelectedItem().toString();
                String age = ageTF.getText();
                String cnic = cnicTF.getText();
                String contact = contactTF.getText();
                String email = emailTF.getText();
                String address = addressTF.getText();
                if (!gender.equalsIgnoreCase("Select Gender")) {
                    if(accType.equalsIgnoreCase("Savings") || accType.equalsIgnoreCase("Current")) {
                        try {
                            if (Validations.checkEmail(email,false)) {
                                if(Validations.checkCnic(cnic,false)) {
                                    if(Validations.checkContact(contact,false)) {
                                        if(Validations.checkAddress(address)) {
                                            if (Validations.checkAge(age)) {
                                                if(Validations.checkName(name)) {
                                                    Requests r = new Requests(name, age, gender, cnic, contact, address, email, accType);
                                                    JOptionPane.showMessageDialog(null, "Request Submitted");
                                                    LoginForm lf = new LoginForm();
                                                    lf.setVisible(true);
                                                    dispose();
                                                }
                                                else{
                                                    JOptionPane.showMessageDialog(null, "Enter a valid name");
                                                }
                                            }
                                            else{
                                                JOptionPane.showMessageDialog(null,"Account Holder must be 18 or older");
                                            }
                                        }
                                        else{
                                            JOptionPane.showMessageDialog(null, "Invalid Address");
                                        }
                                    }
                                    else {
                                        JOptionPane.showMessageDialog(null, "Invalid Contact Number or already in use");
                                    }
                                }
                                else{
                                    JOptionPane.showMessageDialog(null, "Invalid Cnic Number or already in use");
                                }
                            }
                            else{
                                JOptionPane.showMessageDialog(null, "Invalid Email or already in use");
                            }
                        }catch (IOException ex) {
                                throw new RuntimeException(ex);
                            }
                    }
                    else{
                        JOptionPane.showMessageDialog(null, "Select Account Type");
                    }
                }
                else{
                    JOptionPane.showMessageDialog(null, "Please select a gender");
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
            new RequestToCreateAccount().setVisible(true);
        });
    }
}
