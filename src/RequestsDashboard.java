import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class RequestsDashboard extends JFrame {
    private JPanel header;
    private JTextField admintextfield;
    private JTable table1;
    private JPanel requestsDashboard;
    private JButton exitButton;

    public RequestsDashboard(Admin a) throws IOException {
        setTitle("Requests Dashboard");
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(screenSize.width, screenSize.height - 70);
        setContentPane(requestsDashboard);
        Image icon = Toolkit.getDefaultToolkit().getImage("src/Images/SBL (1).png");
        setIconImage(icon);
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gs = ge.getDefaultScreenDevice();
        Rectangle screenBounds = gs.getDefaultConfiguration().getBounds();
        setLocation(screenBounds.x, screenBounds.y + 20);
        admintextfield.setText(a.getName());

        DefaultTableModel tableModel = new DefaultTableModel();
        tableModel.addColumn("Request ID");
        tableModel.addColumn("Name");
        tableModel.addColumn("Age");
        tableModel.addColumn("Gender");
        tableModel.addColumn("CNIC #");
        tableModel.addColumn("Contact #");
        tableModel.addColumn("Address");
        tableModel.addColumn("Email");
        tableModel.addColumn("Account Type");
        tableModel.addColumn("Accept");
        tableModel.addColumn("Reject");

        tableModel.addRow(new Object[]{"", "", "", "", "", "", "", "", "", "", ""});

        String[][] requests = a.getRequests();
        if (requests != null) {
            for (int i = 0; i < requests.length; i++) {
                Object[] originalRow = requests[i];
                Object[] fullRow = new Object[11];
                for (int j = 0; j < 9; j++) {
                    fullRow[j] = originalRow[j];
                }
                fullRow[9] = "Accept";
                fullRow[10] = "Reject";
                tableModel.addRow(fullRow);
            }
        }

        table1.setModel(tableModel);
        table1.getColumn("Accept").setCellRenderer(new ButtonRenderer());
        table1.getColumn("Accept").setCellEditor(new ButtonEditor(new JCheckBox(), "Accept", requests));
        table1.getColumn("Reject").setCellRenderer(new ButtonRenderer());
        table1.getColumn("Reject").setCellEditor(new ButtonEditor(new JCheckBox(), "Reject", requests));

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);

        exitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                AdminDashboard ad = new AdminDashboard(a);
                ad.setVisible(true);
                dispose();
            }
        });
    }
}

class ButtonRenderer extends JButton implements TableCellRenderer {
    public ButtonRenderer() {
        setOpaque(true);
    }

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        if (row == 0 || !(value instanceof String)) return new JLabel("");
        String text = (String) value;
        setText(text);
        if ("Accept".equals(text)) {
            setBackground(new Color(21, 150, 124));
            setForeground(Color.WHITE);
        } else if ("Reject".equals(text)) {
            setBackground(new Color(150, 0, 4));
            setForeground(Color.WHITE);
        } else {
            setBackground(UIManager.getColor("Button.background"));
            setForeground(Color.BLACK);
        }
        return this;
    }
}

class ButtonEditor extends DefaultCellEditor {
    protected JButton button;
    private String label;
    private boolean isPushed;
    private JTable table;
    private String[][] requests;
    private String actionType;

    public ButtonEditor(JCheckBox checkBox, String actionType, String[][] requests) {
        super(checkBox);
        this.requests = requests;
        this.actionType = actionType;
        button = new JButton();
        button.setOpaque(true);
        if (actionType.equals("Accept")) {
            button.setBackground(new Color(21, 150, 124));
            button.setForeground(Color.WHITE);
        } else {
            button.setBackground(new Color(150, 0, 4));
            button.setForeground(Color.WHITE);
        }
        button.addActionListener(e -> {
            fireEditingStopped();
            int row = table.getSelectedRow();
            if (row == 0 || row - 1 >= requests.length) return;
            String requestId = requests[row - 1][0];
            try {
                Requests r = new Requests();
                if (actionType.equals("Accept")) {
                    r.approveAccountCreationRequest(requestId);
                    table.setValueAt("Accepted", row, 9);
                    table.setValueAt("", row, 10);
                    JOptionPane.showMessageDialog(button, "Accepted request: " + requestId);
                } else {
                    r.rejectAccountCreationRequest(requestId);
                    table.setValueAt("Rejected", row, 9);
                    table.setValueAt("", row, 10);
                    JOptionPane.showMessageDialog(button, "Rejected request: " + requestId);
                }
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
    }

    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        this.table = table;
        label = (value == null) ? "" : value.toString();
        if (row == 0 || "".equals(label)) return new JLabel("");
        button.setText(label);
        isPushed = true;
        return button;
    }

    public Object getCellEditorValue() {
        isPushed = false;
        return label;
    }

    public boolean stopCellEditing() {
        isPushed = false;
        return super.stopCellEditing();
    }

    protected void fireEditingStopped() {
        super.fireEditingStopped();
    }
}



