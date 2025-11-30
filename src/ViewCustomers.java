import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class ViewCustomers extends JFrame {
    private JPanel header;
    private JTextField admintextfield;
    private JTable customerTable;
    private JButton searchButton;
    private JTextField accNumTF;
    private JPanel viewCustomers;
    private JButton backButton;

    public ViewCustomers(Admin a) throws IOException {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(screenSize.width, screenSize.height-70);
        setContentPane(viewCustomers);
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gs = ge.getDefaultScreenDevice();
        Rectangle screenBounds = gs.getDefaultConfiguration().getBounds();
        setLocation(screenBounds.x,screenBounds.y+20);
        admintextfield.setText(a.getName());
        setTitle("View Customers");
        Image icon = Toolkit.getDefaultToolkit().getImage("src/Images/SBL (1).png");
        setIconImage(icon);
        DefaultTableModel tableModel = new DefaultTableModel();
        tableModel.addColumn("ID");
        tableModel.addColumn("Name");
        tableModel.addColumn("Username");
        tableModel.addColumn("Account Number");
        tableModel.addColumn("Account Type");
        tableModel.addColumn("Contact Number");
        String[][] customers=a.getCustomers();
        if(customers!=null){
            for(int i=0;i<customers.length;i++){
                tableModel.addRow(new Object[]{});
                tableModel.addRow(new Object[]{customers[i][0],customers[i][1],customers[i][5],customers[i][7],customers[i][8],customers[i][10]});
            }
        }
        else{
            JOptionPane.showMessageDialog(null, "No Customers Found");
        }
        customerTable.setModel(tableModel);
        searchButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String customeraccNum = accNumTF.getText();
                try {
                    if(AccountHolder.checkAccountHolder(customeraccNum)) {
                        ShowCustomerrDetails showDetails=new ShowCustomerrDetails(a,customeraccNum);
                        showDetails.setVisible(true);
                        dispose();
                    }
                    else{
                        JOptionPane.showMessageDialog(null, "No Account with this Account Number Found");
                    }
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        backButton.addActionListener(new ActionListener() {
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
                new ViewCustomers(new Admin("ADM001", "Hateem Khan", "30", "Male", "admin_hateem", "Admin@123", "42101-9876543-2", "0300-1234567", "admin@example.com")).setVisible(true);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
