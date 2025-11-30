import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;

public class View_Feedback_Queries_Suggestions extends JFrame {
    private JTextField adminTF;
    private JTable responseTable;
    private JButton backButton;
    private JPanel viewFeedBackPane;

    public View_Feedback_Queries_Suggestions(Admin a) throws IOException {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(screenSize.width, screenSize.height-70);
        setContentPane(viewFeedBackPane);
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gs = ge.getDefaultScreenDevice();
        Rectangle screenBounds = gs.getDefaultConfiguration().getBounds();
        setLocation(screenBounds.x,screenBounds.y+20);
        adminTF.setText(a.getName());
        setTitle("View Feedback,Queries and Suggestions");
        Image icon = Toolkit.getDefaultToolkit().getImage("src/Images/SBL (1).png");
        setIconImage(icon);
        DefaultTableModel tableModel = new DefaultTableModel();
        tableModel.addColumn("Response ID");
        tableModel.addColumn("Customer ID");
        tableModel.addColumn("Category");
        tableModel.addColumn("Response");
        ArrayList<String> customers=a.getFeedback_Queries_Suggestions();
        if(customers!=null){
            for(int i=0;i<customers.size();i++){
                tableModel.addRow(new Object[]{});
                String[] line=customers.get(i).split(",");
                tableModel.addRow(new Object[]{line[0],line[1],line[2],line[3]});
            }
        }
        responseTable.setModel(tableModel);
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
                new View_Feedback_Queries_Suggestions(new Admin("ADM001", "Hateem Khan", "30", "Male", "admin_hateem", "Admin@123", "42101-9876543-2", "0300-1234567", "admin@example.com")).setVisible(true);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
