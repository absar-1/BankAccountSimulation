import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;

public class ViewLoanOptions extends JFrame{
    private JPanel header;
    private JTextField userTF;
    private JTable goldTable;
    private JTable silverTable;
    private JTable platiniumTable;
    private JButton backButton;
    private JButton applySilverButton;
    private JButton applyGoldButton;
    private JButton applyPlatiniumButton;
    private JPanel loanOptionsPane;

    public ViewLoanOptions(AccountHolder accountHolder) throws IOException {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(screenSize.width, screenSize.height-70);
        setContentPane(loanOptionsPane);
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gs = ge.getDefaultScreenDevice();
        Rectangle screenBounds = gs.getDefaultConfiguration().getBounds();
        setLocation(screenBounds.x,screenBounds.y+20);
        setTitle("View Loan Options");
        Image icon = Toolkit.getDefaultToolkit().getImage("src/Images/SBL (1).png");
        setIconImage(icon);
        userTF.setText(accountHolder.getName());
        ArrayList<String> arrayList=accountHolder.viewLoanOptions();
        for (int i = 0; i < arrayList.size(); i++) {
            String[] parts=arrayList.get(i).split(",");
            if(i==0){
                DefaultTableModel silverModel=new DefaultTableModel();
                silverModel.addColumn("Loan Amount (PKR)");
                silverModel.addColumn("Minimum Balance (PKR)");
                silverModel.addColumn("Loan Duration in Months");
                silverModel.addColumn("Interest Percentage per annum");
                silverModel.addRow(parts);
                silverTable.setModel(silverModel);
            }
            if(i==1){
                DefaultTableModel goldModel=new DefaultTableModel();
                goldModel.addColumn("Loan Amount (PKR)");
                goldModel.addColumn("Minimum Balance (PKR)");
                goldModel.addColumn("Loan Duration in Months");
                goldModel.addColumn("Interest Percentage per annum");
                goldModel.addRow(parts);
                goldTable.setModel(goldModel);
            }
            if(i==2){
                DefaultTableModel platModel=new DefaultTableModel();
                platModel.addColumn("Loan Amount (PKR)");
                platModel.addColumn("Minimum Balance (PKR)");
                platModel.addColumn("Loan Duration in Months");
                platModel.addColumn("Interest Percentage per annum");
                platModel.addRow(parts);
                platiniumTable.setModel(platModel);
            }
        }
        applySilverButton.addActionListener(new ActionListener() {
                public void actionPerformed (ActionEvent e) {
                    try {
                        if (accountHolder.getBalance() < 50000) {
                            JOptionPane.showMessageDialog(null, "Minimum balance required for Silver Loan Package is 50000 Rs");
                        }
                        else if(Requests.alreadyAppliedForLoan(accountHolder.getId())){
                            JOptionPane.showMessageDialog(null,"Already applied for a Loan.");
                        }
                        else {
                            try {
                                Requests r = new Requests(accountHolder.getId(), accountHolder.getName(), LoanOptions.SILVER, silverTable.getValueAt(0, 0).toString());
                                JOptionPane.showMessageDialog(null, "Request Submitted for Silver Loan Package");
                            } catch (Exception ex) {
                                throw new RuntimeException(ex);
                            }
                        }
                    } catch (Exception ex) {}
                }
        });
        applyGoldButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try{
                if(accountHolder.getBalance()<100000){
                    JOptionPane.showMessageDialog(null,"Minimum balance required for Gold Loan Package is 100000 Rs");
                }
                else if(Requests.alreadyAppliedForLoan(accountHolder.getId())){
                    JOptionPane.showMessageDialog(null,"Already applied for a Loan.");
                }
                else {
                    try {
                        Requests r = new Requests(accountHolder.getId(), accountHolder.getName(), LoanOptions.GOLD, goldTable.getValueAt(0, 0).toString());
                        JOptionPane.showMessageDialog(null, "Request Submitted for Gold Loan Package");
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }
                }
                } catch (Exception ex){}
            }
        });
        applyPlatiniumButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try{
                if(accountHolder.getBalance()<200000){
                    JOptionPane.showMessageDialog(null,"Minimum balance required for Platinium Loan Package is 200000 Rs");
                }
                else if(Requests.alreadyAppliedForLoan(accountHolder.getId())){
                    JOptionPane.showMessageDialog(null,"Already applied for a Loan.");
                }
                else {
                    try {
                        Requests r = new Requests(accountHolder.getId(), accountHolder.getName(), LoanOptions.PLATINIUM, platiniumTable.getValueAt(0, 0).toString());
                        JOptionPane.showMessageDialog(null, "Request Submitted for Platinium Loan Package");
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }
                }
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try{
                    MainDashboard md=new MainDashboard(accountHolder);
                    md.setVisible(true);
                    dispose();
                }
                catch(Exception ex){}
            }
        });

    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                new ViewLoanOptions(new SavingsAccountHolder("ID123", "Hateem Khan", 50000.0, "21", "Male", "hateem21", "ACC456789123", "StrongP@ssw0rd", "Savings", "123 Main Street", "0312-3456789", "42101-1234567-8", "hateem@example.com")).setVisible(true);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
