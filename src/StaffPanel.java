/* 
 * Author: Youssef AITBOUDDROUB
 */

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class StaffPanel extends JPanel {
    private DefaultTableModel tableModel;
    private JTable staffTable;
    private boolean shouldClearSelection = true; // New variable to control selection clearing

    private StaffDAO staffDAO;

    public StaffPanel() {
        // Initialize components and layout
        setLayout(new BorderLayout());
        setBackground(Utilities.Green); // Set background color

        staffDAO = new StaffDAO();
        // Create staff table model and table
        tableModel = new DefaultTableModel();
        tableModel.setColumnIdentifiers(new String[]{"ID", "Name", "Telephone", "Email", "Role", "Password"}); // Added "Password"

        staffTable = new JTable(tableModel) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make all cells non-editable
            }
        };
        staffTable.setDefaultRenderer(Object.class, new StaffListRenderer());
        staffTable.getTableHeader().setDefaultRenderer(new StaffListHeaderRenderer());
        staffTable.setRowHeight(30); // Set fixed row height for better appearance
        staffTable.getTableHeader().setReorderingAllowed(false); // Disable column reordering
        staffTable.setForeground(Color.WHITE);
        staffTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Set up editing staff procedure
        staffTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    int selectedRow = staffTable.getSelectedRow();
                    if (selectedRow != -1) {
                        int selectedID = (int) tableModel.getValueAt(selectedRow, 0);
                        Staff selectedStaff = findStaffById(selectedID);

                        if (selectedStaff != null) {
                            EditStaffDialog editStaffDialog = new EditStaffDialog(selectedStaff, tableModel, staffTable, staffDAO);
                            editStaffDialog.setLocationRelativeTo(StaffPanel.this);
                            editStaffDialog.setVisible(true);

                            Staff editedStaff = editStaffDialog.getEditedStaff();
                            if (editedStaff != null) {
                                tableModel.setValueAt(editedStaff.getId(), selectedRow, 0);
                                tableModel.setValueAt(editedStaff.getName(), selectedRow, 1);
                                tableModel.setValueAt(editedStaff.getTelephone(), selectedRow, 2);
                                tableModel.setValueAt(editedStaff.getEmail(), selectedRow, 3);
                                tableModel.setValueAt(editedStaff.getRole().name(), selectedRow, 4);
                                tableModel.setValueAt(editedStaff.getPassword(), selectedRow, 5); // Set password

                                // Notify the JTable that the data has changed using fireTableRowsUpdated
                                tableModel.fireTableRowsUpdated(selectedRow, selectedRow);

                                // Do not clear the selection if the flag is set to false
                                if (shouldClearSelection) {
                                    staffTable.getSelectionModel().clearSelection();
                                }
                            }
                        }
                    }
                }
            }

            private Staff findStaffById(int id) {
            // Replace this with your actual data structure
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                int staffId = (int) tableModel.getValueAt(i, 0);
                if (staffId == id) {
                    String name = (String) tableModel.getValueAt(i, 1);
                    String telephone = (String) tableModel.getValueAt(i, 2);
                    String email = (String) tableModel.getValueAt(i, 3);
                    String role = (String) tableModel.getValueAt(i, 4);
                    return new Staff(staffId, name, telephone, email, Staff.StaffRole.valueOf(role));
                }
            }
            return null; // Return null if staff with the given ID is not found
        }

        });

        

        JScrollPane staffScrollPane = new JScrollPane(staffTable);
        staffScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        staffScrollPane.setPreferredSize(new Dimension(staffTable.getWidth(), 400)); // Set the preferred height

        // Set font and height for header
        JTableHeader header = staffTable.getTableHeader();
        header.getTable().setFont(new Font("Poppins", Font.BOLD, 18)); // Set font size for header
        header.setPreferredSize(new Dimension(header.getWidth(), 40)); // Set height for header
        header.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, new Color(1, 20, 2)));

        // Add buttons for adding, editing, and deleting
        JButton addButton = Utilities.createStyledButton("Add");
        JButton deleteButton = Utilities.createStyledButton("Delete");

        // Add ActionListener for delete button
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Handle deleting staff member
                int selectedIndex = staffTable.getSelectedRow();
                if (selectedIndex != -1) {
                    //get staff id from selected row
                    int staffId = (int) tableModel.getValueAt(selectedIndex,0);
                    //delete from database
                    staffDAO.deleteStaff(staffId);
                    tableModel.removeRow(selectedIndex);
                    // Notify the JTable that the data has changed
                    fireUpdateEvent(staffTable);
                }
            }
        });

        // Add "Add" button
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Open a new window for adding a new staff member
                AddStaffDialog addStaffDialog = new AddStaffDialog(tableModel, staffTable,staffDAO);
                addStaffDialog.setLocationRelativeTo(StaffPanel.this); // Center the dialog on the panel
                addStaffDialog.setVisible(true);

                // Retrieve the new staff member from the dialog and add it to the tableModel
                Staff newStaff = addStaffDialog.getNewStaff();
                if (newStaff != null) {
                    tableModel.addRow(new Object[]{newStaff.getId(), newStaff.getName(),
                            newStaff.getTelephone(), newStaff.getEmail(), newStaff.getRole().name(), newStaff.getPassword()}); // Set password
                }
            }
        });

        // Set up layout using BorderLayout
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonsPanel.setBackground(Utilities.Green); // Set background color
        buttonsPanel.add(deleteButton);
        buttonsPanel.add(addButton);

        add(buttonsPanel, BorderLayout.NORTH);
        add(staffScrollPane, BorderLayout.CENTER);
        loadAllStaffData();
    }

    

    // Custom renderer for staff list
    private class StaffListRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            label.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.GRAY)); // Add bottom border

            if (row == 0) { // Header (assuming header is at row 0)
                label.setBackground(new Color(253, 164, 9, 127));
                label.setForeground(Color.WHITE);
                label.setFont(new Font("Poppins", Font.BOLD, 16));
            } else {
                if (value instanceof Staff) {
                    Staff staff = (Staff) value;
                    label.setBackground(isSelected ? new Color(253, 164, 9, 127) : Color.WHITE);
                    label.setFont(new Font("Poppins", Font.PLAIN, 14));

                    // Display password only for Manager and Cashier
                    String passwordInfo = staff.getRole() == Staff.StaffRole.MANAGER || staff.getRole() == Staff.StaffRole.CASHIER
                            ? staff.getPassword()
                            : "********"; // Mask password for other roles

                    label.setText(String.format("%-8s| %-20s| %-10s| %-30s| %s| %s",
                            staff.getId(), staff.getName(), staff.getTelephone(),
                            staff.getEmail(), staff.getRole().name(), passwordInfo)); // Set password
                } else {
                    // Handle other data types if necessary
                }
            }
            return label;
        }
    }

    // Custom header renderer for staff list
    private class StaffListHeaderRenderer extends DefaultTableCellRenderer {
        public StaffListHeaderRenderer() {
            setOpaque(true);
            setHorizontalAlignment(CENTER);
            setForeground(Color.WHITE);
            setBackground(new Color(253, 164, 9, 127));
            setFont(new Font("Poppins", Font.BOLD, 16));
            setBorder(BorderFactory.createMatteBorder(1, 0, 1, 0, Color.BLACK));
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            label.setText(value == null ? "" : value.toString());
            return label;
        }
    }

    // Custom method to notify the JTable that the data has changed
    public void fireUpdateEvent(JTable table) {
        TableModel model = table.getModel();
        int index = table.getSelectedRow();
    
        if (model instanceof DefaultTableModel) {
            ((DefaultTableModel) model).fireTableDataChanged();
        }
    
        // Clear the selection if the flag is set to true
        if (shouldClearSelection) {
            table.getSelectionModel().setSelectionInterval(index, index);
        }
    }


    // Custom dialog for adding a new staff member
    class AddStaffDialog extends JDialog {
        private JTextField nameField;
        private JTextField telephoneField;
        private JTextField emailField;
        private JPasswordField passwordField; // Added password field
        private JComboBox<String> roleComboBox;
        private Staff newStaff;
        private StaffDAO staffDAO;

        public AddStaffDialog(DefaultTableModel tableModel, JTable staffTable, StaffDAO staffDAO) {
            this.staffDAO = staffDAO;
            setTitle("Add New Staff");
            setSize(450, 300);
            setLayout(new GridLayout(6, 2));

            nameField = new JTextField();
            telephoneField = new JTextField();
            emailField = new JTextField();
            passwordField = new JPasswordField(); // Initialize password field
            roleComboBox = new JComboBox<>(new String[]{"Manager", "Waiter", "Cook", "Cashier"});

            add(new JLabel("   Name:"));
            add(nameField);
            add(new JLabel("   Telephone:"));
            add(telephoneField);
            add(new JLabel("   Email:"));
            add(emailField);
            add(new JLabel("   Password:")); // Added password label
            add(passwordField); // Added password field
            add(new JLabel("   Role:"));
            add(roleComboBox);

            JButton addButton = Utilities.createStyledButton("Add Staff");
            addButton.setBorder(BorderFactory.createMatteBorder(1, 0, 1, 1, new Color(1, 20, 2)));
            addButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // Create a new Staff object with the entered information
                    newStaff = new Staff();
                    //get next staff ID
                    int nextStaffId = staffDAO.getNextStaffId();
                    newStaff.setId(nextStaffId);

                    newStaff.setName(nameField.getText());
                    newStaff.setTelephone(telephoneField.getText());
                    newStaff.setEmail(emailField.getText());

                    // Set the role using the selected item from the combo box
                    String selectedRole = (String) roleComboBox.getSelectedItem();

                    // Check and set the role based on the selected item
                    Staff.StaffRole role = Staff.StaffRole.valueOf(selectedRole.toUpperCase());

                    // Set the role for the new staff
                    newStaff.setRole(role);

                    // Set the password only for Manager and Cashier
                    String password = (newStaff.getRole() == Staff.StaffRole.MANAGER || newStaff.getRole() == Staff.StaffRole.CASHIER)
                    ? new String(passwordField.getPassword())
                    : null;
                    // Create an array of objects representing the data for the new staff
                    Object[] rowData = {newStaff.getId(), newStaff.getName(), newStaff.getTelephone(),
                            newStaff.getEmail(), newStaff.getRole().name(), password}; // Set password

                    // Add the new staff to the tableModel
                    tableModel.addRow(rowData);

                    // Notify the JTable that the data has changed using fireTableRowsInserted
                    int index = tableModel.getRowCount() - 1;
                    tableModel.fireTableRowsInserted(index, index);

                    // insert on database
                    staffDAO.insertStaff(newStaff, password);
                    // Close the dialog
                    dispose();
                }
            });

            JButton cancelButton = Utilities.createStyledButton("Cancel");
            cancelButton.setBorder(BorderFactory.createMatteBorder(1, 0, 1, 0, new Color(1, 20, 2)));
            cancelButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // Close the dialog without creating a new staff member
                    newStaff = null;
                    dispose();
                }
            });

            add(addButton);
            add(cancelButton);
        }

        // Method to get the new staff member entered in the dialog
        public Staff getNewStaff() {
            return newStaff;
        }
    }

    // Custom dialog for editing a new staff member
    class EditStaffDialog extends JDialog {
        private JTextField nameField;
        private JTextField telephoneField;
        private JTextField emailField;
        private JPasswordField passwordField; // Added password field
        private JComboBox<String> roleComboBox;

        private StaffDAO staffDAO;
        private Staff originalStaff;
        private Staff editedStaff;

        public EditStaffDialog(Staff selectedStaff, DefaultTableModel tableModel, JTable staffTable, StaffDAO staffDAO) {
            this.staffDAO = staffDAO;
            this.originalStaff = selectedStaff;

            setTitle("Edit Staff");
            setSize(450, 300);
            setLayout(new GridLayout(6, 2)); // Increased row count for password field

            nameField = new JTextField(originalStaff.getName());
            telephoneField = new JTextField(originalStaff.getTelephone());
            emailField = new JTextField(originalStaff.getEmail());
            passwordField = new JPasswordField(); // Initialize password field
            roleComboBox = new JComboBox<>(new String[]{"Manager", "Waiter", "Cook", "Cashier"});
            roleComboBox.setSelectedItem(originalStaff.getRole().name());

            add(new JLabel("   Name:"));
            add(nameField);
            add(new JLabel("   Telephone:"));
            add(telephoneField);
            add(new JLabel("   Email:"));
            add(emailField);
            add(new JLabel("   Password:")); // Added password label
            add(passwordField); // Added password field
            add(new JLabel("   Role:"));
            add(roleComboBox);

            JButton saveButton = Utilities.createStyledButton("Save Changes");
            saveButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // Create a new Staff object with the edited information
                    editedStaff = new Staff();
                    editedStaff.setId(originalStaff.getId());
                    editedStaff.setName(nameField.getText());
                    editedStaff.setTelephone(telephoneField.getText());
                    editedStaff.setEmail(emailField.getText());
                    editedStaff.setRole(Staff.StaffRole.valueOf(((String) roleComboBox.getSelectedItem()).toUpperCase()));
                                    
                    // Set the password only for Manager and Cashier
                    editedStaff.setPassword((editedStaff.getRole() == Staff.StaffRole.MANAGER || editedStaff.getRole() == Staff.StaffRole.CASHIER)
                            ? new String(passwordField.getPassword())
                            : null);
                    // Update the staff in the database
                    staffDAO.updateStaff(editedStaff);
                                    
                    // Find the index of the selected staff in the tableModel
                    int selectedIndex = staffTable.getSelectedRow();
                                    
                    if (selectedIndex != -1) {
                        // Update the tableModel with the edited staff at the correct index
                        tableModel.setValueAt(editedStaff.getId(), selectedIndex, 0);
                        tableModel.setValueAt(editedStaff.getName(), selectedIndex, 1);
                        tableModel.setValueAt(editedStaff.getTelephone(), selectedIndex, 2);
                        tableModel.setValueAt(editedStaff.getEmail(), selectedIndex, 3);
                        tableModel.setValueAt(editedStaff.getRole().name(), selectedIndex, 4); // Set role name
                        tableModel.setValueAt(editedStaff.getPassword(), selectedIndex, 5); // Set password
                    
                        // Notify the JTable that the data has changed using fireTableRowsUpdated
                        tableModel.fireTableRowsUpdated(selectedIndex, selectedIndex);
                    }

                    dispose();
                }
            });

            JButton cancelButton = Utilities.createStyledButton("Cancel");
            cancelButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    editedStaff = null;
                    dispose();
                }
            });

            add(saveButton);
            add(cancelButton);
        }

        public Staff getEditedStaff() {
            return editedStaff;
        }
    }
    private void loadAllStaffData() {
        // Retrieve all staff members from the database
        StaffDAO staffDAO = new StaffDAO();
        List<Staff> allStaff = staffDAO.getAllStaff();

        // Clear the existing rows in the tableModel
        tableModel.setRowCount(0);

        // Populate the tableModel with data for each staff member
        for (Staff staff : allStaff) {
            tableModel.addRow(new Object[]{staff.getId(), staff.getName(),
                    staff.getTelephone(), staff.getEmail(), staff.getRole().name(), staff.getPassword()});
        }

        // Notify the JTable that the data has changed
        tableModel.fireTableDataChanged();
    }

    // ... other methods ...
}

