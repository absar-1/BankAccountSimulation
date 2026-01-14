import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class ViewLoanRequests extends JFrame {
    private JPanel header;
    private JTextField userTF;
    private JTable table1;
    private JButton backButton;
    private JPanel loanRequestsPane;

    public ViewLoanRequests(Admin admin) throws IOException {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(screenSize.width, screenSize.height-70);
        setContentPane(loanRequestsPane);
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gs = ge.getDefaultScreenDevice();
        Rectangle screenBounds = gs.getDefaultConfiguration().getBounds();
        setLocation(screenBounds.x,screenBounds.y+20);
        setTitle("View Loan Requests");
        Image icon = Toolkit.getDefaultToolkit().getImage("src/Images/SBL (1).png");
        setIconImage(icon);
        userTF.setText(admin.getName());

        DefaultTableModel tableModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 5 || column == 6;
            }

            @Override
            public String getColumnName(int column) {
                if (column == 5 || column == 6) return "";
                return super.getColumnName(column);
            }
        };

        tableModel.addColumn("Request ID");
        tableModel.addColumn("Account Holder ID");
        tableModel.addColumn("Account Holder Name");
        tableModel.addColumn("Package Name");
        tableModel.addColumn("Loan Amount (PKR)");
        tableModel.addColumn("");
        tableModel.addColumn("");

        String[][] reqs = LoanRequestDAO.getAllRequests();
        for (String[] parts : reqs) {
            tableModel.addRow(new Object[]{
                    parts[0], parts[1], parts[2], parts[3], parts[4], "Accept", "Reject"
            });
        }

        table1.setModel(tableModel);
        table1.setRowHeight(40);

        table1.getColumnModel().getColumn(5).setCellRenderer(new IconButtonRenderer("src/Images/checked (1).png", new Color(0, 180, 0)));
        table1.getColumnModel().getColumn(5).setCellEditor(new IconButtonEditor(new JCheckBox(), "Accept", "src/Images/checked (1).png", new Color(0, 180, 0)));

        table1.getColumnModel().getColumn(6).setCellRenderer(new IconButtonRenderer("src/Images/deleted (1).png", new Color(200, 0, 0)));
        table1.getColumnModel().getColumn(6).setCellEditor(new IconButtonEditor(new JCheckBox(), "Reject", "src/Images/deleted (1).png", new Color(200, 0, 0)));
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                AdminDashboard aD=new AdminDashboard(admin);
                aD.setVisible(true);
                dispose();
            }
        });
    }
}

class IconButtonRenderer extends JButton implements TableCellRenderer {
    public IconButtonRenderer(String iconPath, Color bgColor) {
        setOpaque(true);
        setIcon(new ImageIcon(iconPath));
        setBackground(bgColor);
        setBorderPainted(false);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        return this;
    }
}
class IconButtonEditor extends DefaultCellEditor {
    private JButton button;
    private boolean clicked;
    private String label;
    private JTable currentTable;
    private int selectedRow;

    public IconButtonEditor(JCheckBox checkBox, String label, String iconPath, Color bgColor) {
        super(checkBox);
        this.label = label;
        button = new JButton(new ImageIcon(iconPath));
        button.setOpaque(true);
        button.setBackground(bgColor);
        button.setBorderPainted(false);
        button.addActionListener(e -> fireEditingStopped());
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        clicked = true;
        selectedRow = row;
        currentTable = table;
        return button;
    }

    @Override
    public Object getCellEditorValue() {
        if (clicked) {
            String reqID = currentTable.getValueAt(selectedRow, 0).toString();
            String customerID = currentTable.getValueAt(selectedRow, 1).toString();
            String loanAmount = currentTable.getValueAt(selectedRow, 4).toString();
            Requests r=new Requests();

            if (label.equals("Accept")) {
                try {
                    r.approveLoanRequest(reqID, customerID, loanAmount);
                    JOptionPane.showMessageDialog(null,"Loan Request ( "+reqID+ ") Accepted");
                }
                catch (Exception e) {}
            } else if (label.equals("Reject")) {
                try {
                    r.rejectLoanRequests(reqID);
                    JOptionPane.showMessageDialog(null,"Loan Request ( "+reqID+ ") Rejected");
                }
                catch (RuntimeException e) {
                    throw new RuntimeException(e);
                }
                catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        clicked = false;
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




