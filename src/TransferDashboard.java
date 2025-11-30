import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;

public class TransferDashboard extends JFrame {
    private JPanel header;
    private JTextField userTF;
    private JTextField receiverAccNumTF;
    private JButton exitButton;
    private JPanel transferDashboard;
    private JTable beneficiaryTable;
    private JButton addBeneficiaryButton;
    private JButton backButton;
    private JComboBox bankNameCombobox;
    private JTextField amountTF;

    public TransferDashboard(AccountHolder accountHolder) throws IOException {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(screenSize.width, screenSize.height-70);
        setContentPane(transferDashboard);
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gs = ge.getDefaultScreenDevice();
        Rectangle screenBounds = gs.getDefaultConfiguration().getBounds();
        setLocation(screenBounds.x,screenBounds.y+20);
        setTitle("Transfer Money");
        Image icon = Toolkit.getDefaultToolkit().getImage("src/Images/SBL (1).png");
        setIconImage(icon);
        userTF.setText(accountHolder.getName());

        ArrayList<String> arr = accountHolder.getBeneficiaries();
        DefaultTableModel beneficiaryTableModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 3;
            }
        };
        beneficiaryTableModel.addColumn("Beneficiary Account Number");
        beneficiaryTableModel.addColumn("Beneficiary Name");
        beneficiaryTableModel.addColumn("Beneficiary Bank Name");
        beneficiaryTableModel.addColumn("");

        for (String beneficiary : arr) {
            String[] beneficiaryDetails = beneficiary.split("/");
            beneficiaryTableModel.addRow(new Object[]{beneficiaryDetails[0], beneficiaryDetails[1], beneficiaryDetails[2], "Pay Now"});
        }

        beneficiaryTable.setModel(beneficiaryTableModel);
        beneficiaryTable.getColumn("").setCellRenderer(new ButtonRenderer2());
        beneficiaryTable.getColumn("").setCellEditor(new ButtonEditorr2(new JCheckBox(), beneficiaryTable,accountHolder,this));

        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    MainDashboard md=new MainDashboard(accountHolder);
                    md.setVisible(true);
                    dispose();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        addBeneficiaryButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    AddBeneficiary ab = new AddBeneficiary(accountHolder);
                    ab.setVisible(true);
                    dispose();
                }
                catch (IOException ex) {}
            }
        });
    }

    public static void main(String[] args) {
//        SwingUtilities.invokeLater(() -> {
//            new TransferDashboard("","","").setVisible(true);
//        });
    }
}
class ButtonRenderer2 extends JButton implements TableCellRenderer {
    public ButtonRenderer2() {
        setOpaque(true);
        setBackground(Color.GREEN);
        setForeground(Color.WHITE);
    }

    public Component getTableCellRendererComponent(JTable table, Object value,
                                                   boolean isSelected, boolean hasFocus, int row, int column) {
        setText((value == null) ? "Pay Now" : value.toString());
        return this;
    }
}
class ButtonEditorr2 extends DefaultCellEditor {
    protected JButton button;
    private String label;
    private boolean clicked;
    private JTable table;

    public ButtonEditorr2(JCheckBox checkBox, JTable table,AccountHolder accountHolder,TransferDashboard transferDashboard) {
        super(checkBox);
        this.table = table;
        button = new JButton();
        button.setOpaque(true);
        button.setBackground(Color.GREEN);
        button.setForeground(Color.WHITE);

        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                fireEditingStopped();
                int selectedRow = table.getSelectedRow();
                String accNum = table.getValueAt(selectedRow, 0).toString();
                String bank = table.getValueAt(selectedRow, 2).toString();
                String receiverName = table.getValueAt(selectedRow, 1).toString();
                EnterAmountToTransfer ea=new EnterAmountToTransfer(accNum,bank,accountHolder,receiverName);
                ea.setVisible(true);
                transferDashboard.dispose();
            }
        });
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value,
                                                 boolean isSelected, int row, int column) {
        label = (value == null) ? "Pay Now" : value.toString();
        button.setText(label);
        clicked = true;
        return button;
    }

    @Override
    public Object getCellEditorValue() {
        if (clicked) {
            clicked = false;
        }
        return label;
    }

    @Override
    public boolean stopCellEditing() {
        clicked = false;
        return super.stopCellEditing();
    }

    @Override
    protected void fireEditingStopped() {
        super.fireEditingStopped();
    }
}

