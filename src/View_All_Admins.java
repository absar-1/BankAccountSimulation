import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

public class View_All_Admins extends JFrame {
    private JPanel header;
    private JTextField userTF;
    private JTable table1;
    private JButton addAdminButton;
    private JButton backButton;
    private JPanel viewAllAdminsPane;

    public View_All_Admins(Admin a) throws IOException {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(screenSize.width, screenSize.height - 70);
        setContentPane(viewAllAdminsPane);
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gs = ge.getDefaultScreenDevice();
        Rectangle screenBounds = gs.getDefaultConfiguration().getBounds();
        setLocation(screenBounds.x, screenBounds.y + 20);
        userTF.setText(a.getName());
        setTitle("View All Admins");
        Image icon = Toolkit.getDefaultToolkit().getImage("src/Images/SBL (1).png");
        setIconImage(icon);

        DefaultTableModel tableModel = new DefaultTableModel();
        tableModel.addColumn("ID");
        tableModel.addColumn("Name");
        tableModel.addColumn("Gender");
        tableModel.addColumn("Username");
        tableModel.addColumn("CNIC #");
        tableModel.addColumn("Contact Number");
        tableModel.addColumn("Email");
        tableModel.addColumn("");

        tableModel.addRow(new Object[]{"HBL-0001", "Hateem", "Male", "hateem123", "4556978789087", "03356787899", "khnhateem6@gmail.com", ""});

        ArrayList<String> admins = a.getAllAdmins();
        if (admins != null) {
            for (String adminStr : admins) {
                String[] details = adminStr.split(",");
                if (details.length >= 8 && !details[0].equals("HBL-0001")) {
                    tableModel.addRow(new Object[]{
                            details[0], details[1], details[3], details[4],
                            details[5], details[6], details[7], "Delete"
                    });
                }
            }
        } else {
            JOptionPane.showMessageDialog(null, "No Admins Found");
        }

        table1.setModel(tableModel);
        table1.getColumnModel().getColumn(7).setCellRenderer(new ButtonRenderer_ViewAllAdmins());
        table1.getColumnModel().getColumn(7).setCellEditor(new ButtonEditor_ViewAllAdmins(new JCheckBox(), a));

        backButton.addActionListener(e -> {
            AdminDashboard ad = new AdminDashboard(a);
            ad.setVisible(true);
            dispose();
        });
        addAdminButton.addActionListener(e -> {
            AddAdminDashboard ad = new AddAdminDashboard(a);
            ad.setVisible(true);
            dispose();
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                new View_All_Admins(new Admin("ADM001", "Hateem Khan", "30", "Male", "admin_hateem", "Admin@123", "42101-9876543-2", "0300-1234567", "admin@example.com")).setVisible(true);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    class ButtonRenderer_ViewAllAdmins extends JButton implements TableCellRenderer {
        private final Icon icon = new ImageIcon("src/Images/delete (1).png");

        public ButtonRenderer_ViewAllAdmins() {
            setOpaque(true);
            setBackground(new Color(10, 51, 87));
            setBorderPainted(false);
            setFocusPainted(false);
        }

        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus,
                                                       int row, int column) {
            String adminID = table.getValueAt(row, 0).toString();
            if (adminID.equals("HBL-0001")) {
                setEnabled(false);
                setText("");
                setIcon(null);
                setBackground(new Color(10, 51, 87));
            } else {
                setEnabled(true);
                setText("");
                setIcon(icon);
                setToolTipText("Delete this admin");
                setBackground(new Color(10, 51, 87));
            }
            return this;
        }
    }

    class ButtonEditor_ViewAllAdmins extends DefaultCellEditor {
        private JButton button;
        private String adminID;
        private Admin currentAdmin;
        private final Icon icon = new ImageIcon("src/Images/delete (1).png");
        private final Color normalBg = new Color(10, 51, 87);
        private final Color hoverBg = new Color(20, 70, 120);

        public ButtonEditor_ViewAllAdmins(JCheckBox checkBox, Admin currentAdmin) {
            super(checkBox);
            this.currentAdmin = currentAdmin;
            button = new JButton();
            button.setOpaque(true);
            button.setBackground(normalBg);
            button.setBorderPainted(false);
            button.setFocusPainted(false);
            button.setIcon(icon);

            button.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    button.setBackground(hoverBg);
                }

                public void mouseExited(java.awt.event.MouseEvent evt) {
                    button.setBackground(normalBg);
                }
            });

            button.addActionListener(e -> {
                fireEditingStopped();
                if (!adminID.equals("HBL-0001")) {
                    try {
                        new confirmPage(currentAdmin, adminID, true).setVisible(true);
                        dispose();
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            });
        }

        public Component getTableCellEditorComponent(JTable table, Object value,
                                                     boolean isSelected, int row, int column) {
            adminID = table.getValueAt(row, 0).toString();
            button.setText("");
            button.setIcon(icon);
            button.setBackground(normalBg);
            return button;
        }

        public Object getCellEditorValue() {
            return "Delete";
        }

        public boolean stopCellEditing() {
            return super.stopCellEditing();
        }
    }
}
