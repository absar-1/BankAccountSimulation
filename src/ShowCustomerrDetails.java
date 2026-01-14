import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class ShowCustomerrDetails extends JFrame {
    private JPanel header;
    private JTextField admintextfield;
    private JTextField nameTF;
    private JTextField ageTF;
    private JTextField balanceTF;
    private JComboBox genderCombobox;
    private JTextField usernameTF;
    private JTextField passwordTF;
    private JComboBox accTypeCombobox;
    private JTextField contactTF;
    private JTextField emailTF;
    private JTextField addressTF;
    private JPanel showcustomerDetails;
    private JButton editButton;
    private JButton backButton;

    public ShowCustomerrDetails(Admin a,String customerAccNum) throws IOException {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(screenSize.width, screenSize.height - 70);
        setContentPane(showcustomerDetails);
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gs = ge.getDefaultScreenDevice();
        Rectangle screenBounds = gs.getDefaultConfiguration().getBounds();
        setLocation(screenBounds.x, screenBounds.y + 20);
        setTitle("Customer Details");
        Image icon = Toolkit.getDefaultToolkit().getImage("src/Images/SBL (1).png");
        setIconImage(icon);
        admintextfield.setText(a.getName());
        String[] accountHolderdetails=AccountHolder.getAccountdetails(customerAccNum);
        nameTF.setText(accountHolderdetails[1]);
        balanceTF.setText(accountHolderdetails[2]);
        ageTF.setText(accountHolderdetails[3]);
        if(accountHolderdetails[4].equalsIgnoreCase("Male")){
            genderCombobox.addItem("Male");
            genderCombobox.addItem("Female");
        }else{
            genderCombobox.addItem("Female");
            genderCombobox.addItem("Male");
        }
        usernameTF.setText(accountHolderdetails[5]);
        passwordTF.setText(accountHolderdetails[6]);
        if(accountHolderdetails[8].equalsIgnoreCase("Savings")){
            accTypeCombobox.addItem("Savings");
            accTypeCombobox.addItem("Current");
        }
        else{
            accTypeCombobox.addItem("Current");
            accTypeCombobox.addItem("Savings");
        }
        addressTF.setText(accountHolderdetails[9]);
        contactTF.setText(accountHolderdetails[10]);
        emailTF.setText(accountHolderdetails[12]);
        editButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
               String newCustomerDetails=accountHolderdetails[0]+","+nameTF.getText()+","+balanceTF.getText()+","+ageTF.getText()+","+genderCombobox.getSelectedItem().toString()+","+usernameTF.getText()+","+passwordTF.getText()+","+accountHolderdetails[7]+","+accTypeCombobox.getSelectedItem().toString()+","+addressTF.getText()+","+contactTF.getText()+","+accountHolderdetails[11]+","+emailTF.getText();
               try {
                   if(!Validations.checkAddress(addressTF.getText())){
                       JOptionPane.showMessageDialog(null, "Please enter a valid address");
                   }
                   else if(!Validations.checkEmail(emailTF.getText(),customerAccNum)){
                       JOptionPane.showMessageDialog(null, "Invalid email address or already in use");
                   }
                   else if(!Validations.checkContact(contactTF.getText(),customerAccNum)){
                       JOptionPane.showMessageDialog(null, "Invalid contact number or already in use");
                   }
                   else if(!Validations.checkUsername(usernameTF.getText(),customerAccNum)){
                       JOptionPane.showMessageDialog(null,"Invalid username or already taken");
                   }
                   else if(!Validations.checkPassword(passwordTF.getText(),false)){
                       JOptionPane.showMessageDialog(null, "Invalid password");
                   }
                   else if(!Validations.checkAge(ageTF.getText())){
                       JOptionPane.showMessageDialog(null, "Account Holder must be 18 or older");
                   }
                   else if(!Validations.checkName(nameTF.getText())){
                       JOptionPane.showMessageDialog(null, "Invalid name");
                   }
                   else if (a.editCustomerDetails(customerAccNum, newCustomerDetails)) {
                       JOptionPane.showMessageDialog(null, "Customer Details Edited Successfully");
                       ViewCustomers vc=new ViewCustomers(a);
                       vc.setVisible(true);
                       dispose();
                   }
                   else{
                       JOptionPane.showMessageDialog(null, "Customer Details Not Edited");
                   }
               }
               catch (IOException e1) {}

            }
        });
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    ViewCustomers vc = new ViewCustomers(a);
                    vc.setVisible(true);
                    dispose();
                } catch (IOException ex) {
                }
            }
        });

    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                new ShowCustomerrDetails(new Admin("ADM001", "Hateem Khan", "30", "Male", "admin_hateem", "Admin@123", "42101-9876543-2", "0300-1234567", "admin@example.com"),"011102220333001").setVisible(true);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
