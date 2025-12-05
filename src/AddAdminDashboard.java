import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class AddAdminDashboard extends JFrame { // by ramsha
    private JPanel header;
    private JTextField admintextfield;
    private JTextField nameTF;
    private JButton saveDetailsButton;
    private JTextField emailTF;
    private JTextField ID;
    private JLabel newLabel;
    private JButton addAdminButton;
    private JButton exitButton;
    private JTextField ageTF;
    private JTextField cnicTF;
    private JTextField contactTF;
    private JTextField passTF;
    private JComboBox genderCombobox;
    private JPanel AddadminPanel;

    public Admin admin;
    public AddAdminDashboard(Admin admin) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(screenSize.width, screenSize.height-60);
        setContentPane(AddadminPanel);
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gs = ge.getDefaultScreenDevice();
        Rectangle screenBounds = gs.getDefaultConfiguration().getBounds();
        setLocation(screenBounds.x,screenBounds.y+10);
        admintextfield.setText(admin.getName());
        genderCombobox.addItem("Select Gender");
        Image icon = Toolkit.getDefaultToolkit().getImage("src/Images/SBL (1).png");
        setIconImage(icon);
        setTitle("Add Admin Dashboard");
        Gender[] genders=Gender.values();
        for (int i = 0; i < genders.length; i++) {
            if(genders[i]==Gender.MALE){
                genderCombobox.addItem("Male");
            }
            else if(genders[i]==Gender.FEMALE){
                genderCombobox.addItem("Female");
            }
        }
        admintextfield.setText(admin.getName());
        addAdminButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(genderCombobox.getSelectedItem()=="Select Gender"){
                    JOptionPane.showMessageDialog(null, "Please Select Gender");
                }
                else {
                    String name = nameTF.getText();
                    String emailaddress = emailTF.getText();
                    String gender = genderCombobox.getSelectedItem().toString();
                    String contact = contactTF.getText();
                    String cnic=cnicTF.getText();
                    String age = ageTF.getText();
                    String pass=passTF.getText();
                    try {
                        if (!Validations.checkPassword(pass, true)) {
                            JOptionPane.showMessageDialog(null, "Invalid Password");
                        }
                        else if(!Validations.checkEmail(emailaddress,true)){
                            JOptionPane.showMessageDialog(null, "Invalid Email or already taken");
                        }
                        else if(!Validations.checkContact(contact,true)){
                            JOptionPane.showMessageDialog(null, "Invalid Contact or already taken");
                        }
                        else if(!Validations.checkCnic(cnic,true)){
                            JOptionPane.showMessageDialog(null, "Invalid CNIC or already taken");
                        }
                        else if(!Validations.checkAge(age)){
                            JOptionPane.showMessageDialog(null, "Invalid Age");
                        }
                        else if(!Validations.checkName(name)){
                            JOptionPane.showMessageDialog(null, "Invalid Name");
                        }
                        else {
                            Admin admin=new Admin(name,cnic,contact,age,gender,pass,emailaddress);
                            JOptionPane.showMessageDialog(null, "Admin added");
                        }
                    }
                    catch (Exception e1) {}

                }
            }
        });
        exitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    View_All_Admins vaa = new View_All_Admins(admin);
                    vaa.setVisible(true);
                    dispose();
                }
                catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        });




            }
    }

