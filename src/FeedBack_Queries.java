import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FeedBack_Queries extends JFrame { // by hateem
    private JPanel header;
    private JLabel iconLabel;
    private JTextField usertextfield;
    private JLabel headingLabel;
    private JComboBox categoryCombobox;
    private JTextField textTF;
    private JButton sendButton;
    private JButton exitButton;
    private JPanel feedbackPane;

    public FeedBack_Queries(AccountHolder accountHolder) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(screenSize.width, screenSize.height-70);
        setContentPane(feedbackPane);
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gs = ge.getDefaultScreenDevice();
        Rectangle screenBounds = gs.getDefaultConfiguration().getBounds();
        setLocation(screenBounds.x,screenBounds.y+20);
        setTitle("FeedBack,Queries and Suggestions");
        Image icon = Toolkit.getDefaultToolkit().getImage("src/Images/SBL (1).png");
        setIconImage(icon);
        usertextfield.setText(accountHolder.getName());
        categoryCombobox.addItem("Select Category");
        categoryCombobox.addItem("Feedback");
        categoryCombobox.addItem("Query");
        categoryCombobox.addItem("Suggestion");
        sendButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String text=textTF.getText();
                String category=categoryCombobox.getSelectedItem().toString();
                if(category.equals("Select Category")){
                    JOptionPane.showMessageDialog(null, "Please select a category");
                }
                else if(text.isEmpty() || text.isBlank()){
                    JOptionPane.showMessageDialog(null, "Please enter a valid text");
                }
                else{
                    try {
                        if (accountHolder.submitFeedback_Queries_Suggestions(category, text)) {
                            JOptionPane.showMessageDialog(null, category+" Submitted");
                        }
                        else {
                            JOptionPane.showMessageDialog(null, category+" Failed to submit");
                        }
                    }
                    catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
        exitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    MainDashboard md = new MainDashboard(accountHolder);
                    md.setVisible(true);
                    dispose();
                }
                catch (Exception ex) {}
            }
        });
    }
}
