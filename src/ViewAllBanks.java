import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;

public class ViewAllBanks extends JFrame {
    private JPanel header;
    private JTextField admintextfield;
    private JTable customerTable;
    private JButton backButton;
    private JPanel viewBankPanel;
    private JButton addBankButton;


    public ViewAllBanks(Admin a) throws IOException {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(screenSize.width, screenSize.height - 70);
        setContentPane(viewBankPanel);
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gs = ge.getDefaultScreenDevice();
        Rectangle screenBounds = gs.getDefaultConfiguration().getBounds();
        setLocation(screenBounds.x, screenBounds.y + 20);
        admintextfield.setText(a.getName());
        setTitle("View Banks");
        Image icon = Toolkit.getDefaultToolkit().getImage("src/Images/SBL (1).png");
        setIconImage(icon);

        DefaultTableModel model = new DefaultTableModel(new Object[]{"Bank Name", ""}, 0) {
            public boolean isCellEditable(int row, int column) {
                return column == 1;
            }
        };

        ArrayList<String> banks = a.getAllBanks();
        for (String bank : banks) {
            model.addRow(new Object[]{bank, "Delete"});
        }
        customerTable.setModel(model);
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        customerTable.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        customerTable.getColumnModel().getColumn(1).setCellRenderer(new ButtonRendererr());
        customerTable.getColumnModel().getColumn(1).setCellEditor(new ButtonEditorrr(new JCheckBox(), a, model));

        TableColumn deleteColumn = customerTable.getColumnModel().getColumn(1);
        deleteColumn.setHeaderValue("");
        customerTable.getTableHeader().repaint();
        addBankButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                AddBank ab=new AddBank(a);
                ab.setVisible(true);
                dispose();
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
                new ViewAllBanks(new Admin("ADM001", "Hateem Khan", "30", "Male", "admin_hateem", "Admin@123", "42101-9876543-2", "0300-1234567", "admin@example.com")).setVisible(true);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    class ButtonRendererr extends JButton implements TableCellRenderer {
        public ButtonRendererr() {
            setOpaque(true);
            setBackground(new Color(10, 51, 87));
            setBorderPainted(false);
            setFocusPainted(false);
        }

        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            if (row == 0) {
                setIcon(null);
                setEnabled(false);
            } else {
                setIcon(new ImageIcon("src/Images/delete (1).png"));
                setEnabled(true);
            }
            setText("");
            return this;
        }
    }

    class ButtonEditorrr extends DefaultCellEditor {
        private JButton button;
        private String bankName;
        private boolean clicked;
        private Admin a;
        private DefaultTableModel model;
        private int row;

        public ButtonEditorrr(JCheckBox checkBox, Admin a, DefaultTableModel model) {
            super(checkBox);
            this.a = a;
            this.model = model;
            button = new JButton();
            button.setOpaque(true);
            button.setBackground(new Color(10, 51, 87));
            button.setBorderPainted(false);
            button.setFocusPainted(false);
            button.addActionListener(e -> fireEditingStopped());
        }

        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            this.row = row;
            bankName = (String) table.getValueAt(row, 0);
            clicked = true;
            if (row == 0) {
                button.setIcon(null);
                button.setEnabled(false);
            } else {
                button.setIcon(new ImageIcon("src/Images/delete (1).png"));
                button.setEnabled(true);
            }
            button.setText("");
            return button;
        }

        public Object getCellEditorValue() {
            if (clicked && row != 0) {
                try {
                    a.removeBank(bankName);
                    model.removeRow(row);
                    JOptionPane.showMessageDialog(null, "Bank \"" + bankName + "\" has been removed successfully.");
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "Failed to remove bank: " + e.getMessage());
                }
            }
            clicked = false;
            return "Delete";
        }

        public boolean stopCellEditing() {
            clicked = false;
            return super.stopCellEditing();
        }

        protected void fireEditingStopped() {
            super.fireEditingStopped();
        }
    }
}

