import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class TransactionsDashboard extends JFrame {
    private JPanel header;
    private JTextField userTF;
    private JTable table1;
    private JPanel transactionDashboard;
    private JButton exitButton;

    public TransactionsDashboard(AccountHolder accountHolder) throws IOException {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(screenSize.width, screenSize.height - 70);
        setContentPane(transactionDashboard);
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gs = ge.getDefaultScreenDevice();
        Rectangle screenBounds = gs.getDefaultConfiguration().getBounds();
        setLocation(screenBounds.x, screenBounds.y + 20);
        DefaultTableModel tableModel = new DefaultTableModel();
        tableModel.addColumn("ID");
        tableModel.addColumn("Description");
        tableModel.addColumn("Amount");
        tableModel.addColumn("Transaction Type");
        tableModel.addColumn("Date");
        tableModel.addColumn("Time");
        table1.setModel(tableModel);
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        setTitle("Transactions");
        Image icon = Toolkit.getDefaultToolkit().getImage("src/Images/SBL (1).png");
        setIconImage(icon);
        userTF.setText(accountHolder.getName());

        Transaction t;
        if(accountHolder.getAccountType().equalsIgnoreCase("Savings")){
            AccountHolder sah=SavingsAccountHolder.getAccountHolderObject(accountHolder.getId());
            t=new Transaction(sah);
        }
        else{
            AccountHolder cah=CurrentAccountHolder.getAccountHolderObject(accountHolder.getId());
            t=new Transaction(cah);
        }
//        String[][] details=t.getTransactionDetails();
//        for(int i=0;i<details.length;i++){
//            tableModel.addRow(new Object[]{"","","","","",""});
//            String[] oneTransactionDetail=new String[details[i].length-1];
//            for(int j=0;j<oneTransactionDetail.length;j++){
//                oneTransactionDetail[j]=details[i][j+1];
//            }
//            tableModel.addRow(oneTransactionDetail);
//        }
        String[][] details;
        try {
            details = TransactionDAO.getTransactionsByAccountId(accountHolder.getId());
        } catch (Exception e) {
            e.printStackTrace();
            details = new String[0][];
        }
        for (int i = 0; i < details.length; i++) {
            // the UI expects rows starting from fields after account id, so create same shape
            tableModel.addRow(new Object[]{"", "", "", "", "", ""});
            String[] oneTransactionDetail = new String[details[i].length - 1];
            for (int j = 0; j < oneTransactionDetail.length; j++) {
                oneTransactionDetail[j] = details[i][j + 1];
            }
            tableModel.addRow(oneTransactionDetail);
        }


        exitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    MainDashboard md = new MainDashboard(accountHolder);
                    md.setVisible(true);
                    dispose();
                }
                catch (IOException ex) {}
            }
        });
    }
    public TransactionsDashboard(AccountHolder accountHolder,Admin admin) throws IOException {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(screenSize.width, screenSize.height - 70);
        setContentPane(transactionDashboard);
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gs = ge.getDefaultScreenDevice();
        Rectangle screenBounds = gs.getDefaultConfiguration().getBounds();
        setLocation(screenBounds.x, screenBounds.y + 20);
        setTitle(accountHolder.getName()+"'s Transactions");
        Image icon = Toolkit.getDefaultToolkit().getImage("src/Images/SBL (1).png");
        setIconImage(icon);
        DefaultTableModel tableModel = new DefaultTableModel();
        tableModel.addColumn("ID");
        tableModel.addColumn("Description");
        tableModel.addColumn("Amount");
        tableModel.addColumn("Transaction Type");
        tableModel.addColumn("Date");
        tableModel.addColumn("Time");
        table1.setModel(tableModel);
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        userTF.setText(admin.getName());

        Transaction t;
        if(accountHolder.getAccountType().equalsIgnoreCase("Savings")){
            AccountHolder sah=SavingsAccountHolder.getAccountHolderObject(accountHolder.getId());
            t=new Transaction(sah);
        }
        else{
            AccountHolder cah=CurrentAccountHolder.getAccountHolderObject(accountHolder.getId());
            t=new Transaction(cah);
        }
//        String[][] details=t.getTransactionDetails();
//        for(int i=0;i<details.length;i++){
//            tableModel.addRow(new Object[]{"","","","","",""});
//            String[] oneTransactionDetail=new String[details[i].length-1];
//            for(int j=0;j<oneTransactionDetail.length;j++){
//                oneTransactionDetail[j]=details[i][j+1];
//            }
//            tableModel.addRow(oneTransactionDetail);
//        }

        String[][] details;
        try {
            details = TransactionDAO.getTransactionsByAccountId(accountHolder.getId());
        } catch (Exception e) {
            e.printStackTrace();
            details = new String[0][];
        }
        for (int i = 0; i < details.length; i++) {
            // the UI expects rows starting from fields after account id, so create same shape
            tableModel.addRow(new Object[]{"", "", "", "", "", ""});
            String[] oneTransactionDetail = new String[details[i].length - 1];
            for (int j = 0; j < oneTransactionDetail.length; j++) {
                oneTransactionDetail[j] = details[i][j + 1];
            }
            tableModel.addRow(oneTransactionDetail);
        }


        exitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                AdminDashboard ad = new AdminDashboard(admin);
                ad.setVisible(true);
                dispose();
            }
        });
    }

    public static void main(String[] args) {

    }
}

