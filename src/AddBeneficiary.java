import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;

public class AddBeneficiary extends JFrame  { // by ramsha
    private JPanel panel1;
    private JTextField accountnumTF;
    private JButton addAccountButton;
    private JButton exitButton;
    private JPanel header;
    private JTextField userTF;
    private JTextField accountnum;
    private JLabel newLabel;
    private JButton addBeneficiaryButton;
    private JTextField bname;
    private JComboBox selectbanks;
    private JPanel addBeneficiaryPanel;
//AccountHolder accountHolder;


    public AddBeneficiary(AccountHolder accountHolder) throws IOException {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(screenSize.width, screenSize.height-70);
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gs = ge.getDefaultScreenDevice();
        Rectangle screenBounds = gs.getDefaultConfiguration().getBounds();
        setLocation(screenBounds.x,screenBounds.y+20);
        setContentPane(addBeneficiaryPanel);
        setTitle("Add Beneficiary");
        Image icon = Toolkit.getDefaultToolkit().getImage("src/Images/SBL (1).png");
        setIconImage(icon);
        userTF.setText(accountHolder.getName());
        ArrayList<String > banks=accountHolder.getAllBanks();
        for (int i = 0; i < banks.size(); i++) {
            String bank = banks.get(i);
            selectbanks.addItem(bank);
        }

        addBeneficiaryButton.addActionListener(new ActionListener() {

            //add combobox nvm
            public void actionPerformed(ActionEvent e) {
                String beneficiaryAccNum = accountnum.getText().trim();
                String beneficiaryName = bname.getText().trim();
                String beneficiaryBankName = (String) selectbanks.getSelectedItem();
                if (accountnum.getText().matches(".*[a-zA-Z].*")) {
                    JOptionPane.showMessageDialog(null, "Please enter a valid a account number");
                }
                else if(beneficiaryAccNum.equalsIgnoreCase(accountHolder.getAccountNumber())) {
                    JOptionPane.showMessageDialog(null, "Cannot add yourself");
                }
                else {
                    if (beneficiaryName.isEmpty() || beneficiaryBankName == null || beneficiaryBankName.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Please enter  all beneficiary details.");
                    }
                    else {
                        try {
                            boolean success = accountHolder.addBeneficiary(beneficiaryAccNum, beneficiaryName, beneficiaryBankName);
                            if (success) {
                                JOptionPane.showMessageDialog(null, "Beneficiary added successfully");
                                TransferDashboard td = new TransferDashboard(accountHolder);
                                td.setVisible(true);
                                dispose();
                            } else {
                                JOptionPane.showMessageDialog(null, "Beneficiary does not exist or already added");
                            }
                        } catch (IOException ex) {
                            JOptionPane.showMessageDialog(null, "An error occurred while adding" + ex.getMessage());
                            ex.printStackTrace();
                        }
                    }
                }
            }
        });
    exitButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                TransferDashboard td = new TransferDashboard(accountHolder);
                td.setVisible(true);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            dispose();
        }

    });
    }
}

