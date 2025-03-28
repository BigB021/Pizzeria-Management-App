/* 
 * Author: Youssef AITBOUDDROUB
 */

import javax.swing.*;
import java.awt.*;
import java.util.Collections;
import java.util.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent; // Add this import statement


public class CategoriesPanel extends JPanel {
    private DefaultListModel<MenuItem> menuListModel;
    private DefaultListModel<MenuItem> orderListModel;
    private JLabel totalLabel;
    private double totalPrice;

    public CategoriesPanel() {
        setLayout(new BorderLayout());

        // Left side for menu items
        JPanel menuPanel = createMenuPanel();
        JScrollPane menuScrollPane = new JScrollPane(menuPanel);
        menuScrollPane.setPreferredSize(new Dimension(600, 400)); // Set the width
        add(menuScrollPane, BorderLayout.WEST);

        // Right side for order items
        JPanel orderPanel = createOrderPanel();
        JScrollPane orderScrollPane = new JScrollPane(orderPanel);
        add(orderScrollPane, BorderLayout.CENTER);

        // Add a button to generate the receipt
        JButton generateReceiptButton = Utilities.createStyledButton("Generate Reciept", new Color(95,150,50), Utilities.Green, 0);
        generateReceiptButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Convert Enumeration to List
                List<MenuItem> selectedItems = Collections.list(orderListModel.elements());
                double total = calculateTotal(selectedItems);
            
                // Generate PDF receipt
                ReceiptGenerator.generateReceipt(selectedItems, total);
            
                // Notify user
                JOptionPane.showMessageDialog(null, "Receipt generated successfully.");
            
                // Optionally, you can clear the order list and update the total label
                orderListModel.clear();
                updateTotalLabel();
            }
        
            private double calculateTotal(List<MenuItem> selectedItems) {
                return totalPrice;
            }
        });
        
        generateReceiptButton.setFont(new Font("Poppins", Font.BOLD, 16));
        add(generateReceiptButton, BorderLayout.SOUTH);
    }

    // Inside createMenuPanel method
    private JPanel createMenuPanel() {
        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new BorderLayout());

        // Create a panel for category buttons
        JPanel categoryButtonPanel = createCategoryButtonPanel();
        menuPanel.add(categoryButtonPanel, BorderLayout.NORTH);

        // Create a DefaultListModel to store the menu items
        menuListModel = new DefaultListModel<>();

        // Initial category (Drinks)
        populateMenuItems("Drinks");

        // Create a JList to display the menu items
        JList<MenuItem> menuList = new JList<>(menuListModel);
        menuList.setCellRenderer(new MenuItemRenderer());
        menuList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // Allow only single selection
        menuList.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Add padding
        menuList.setBackground(new Color(239, 239, 239)); // Match the color scheme

        // Add a MouseListener to handle mouse clicks
        menuList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 1) { // Check for a single click
                    int index = menuList.locationToIndex(e.getPoint());
                    if (index != -1) {
                        // Get the selected item and add it to the order list
                        MenuItem selectedItem = menuListModel.getElementAt(index);
                        orderListModel.addElement(selectedItem);
                        updateTotalLabel();
                    }
                }
            }
        });

        // Add the menuList to the menuPanel
        menuPanel.add(new JScrollPane(menuList), BorderLayout.CENTER);

        return menuPanel;
    }


    private JPanel createCategoryButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEADING, 0, 0)); // Adjusted layout and removed space
        buttonPanel.setBackground(Utilities.Green); // Background color: #34C0A9

        // Category buttons
        String[] categories = {"Drinks", "Pizza", "Sandwich", "Panini", "Special Menu", "Dessert"};
        String[] iconPaths = {
                "./assets/drinks.png", "./assets/pizza1.png", "./assets/sandwich.png", "./assets/panini.png",
                "./assets/menu.png", "./assets/dessert.png"
        };

        for (int i = 0; i < categories.length; i++) {
            final int categoryIndex = i;  // Declare a final variable
            // Resize the icon to 30x30 pixels
            ImageIcon categoryIcon = new ImageIcon(new ImageIcon(iconPaths[i]).getImage().getScaledInstance(40, 40, Image.SCALE_DEFAULT));

            JButton categoryButton = new JButton(categories[i], categoryIcon);
            categoryButton.setVerticalTextPosition(SwingConstants.BOTTOM);
            categoryButton.setHorizontalTextPosition(SwingConstants.CENTER);
            categoryButton.setFocusPainted(false); // Remove focus border

            // Set custom styling
            categoryButton.setForeground(new Color(240, 240, 240)); // Font color: #F0F0F0
            categoryButton.setBackground(Utilities.Orange); // Background color: #FDA409
            categoryButton.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15)); // Add padding
            categoryButton.setBorderPainted(false); // Remove border

            // Add hover effect using MouseListener
            categoryButton.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    categoryButton.setBackground(Utilities.DarkOrange);
                    categoryButton.setForeground(Utilities.Orange);
                }

                public void mouseExited(java.awt.event.MouseEvent evt) {
                    categoryButton.setForeground(new Color(240, 240, 240)); // Restore original background on exit
                    categoryButton.setBackground(Utilities.Orange);
                }
            });

            // Set font to Poppins and increase font size
            Font buttonFont = new Font("Poppins", Font.PLAIN, 18); // Adjust font size
            categoryButton.setFont(buttonFont);

            categoryButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // Populate menu items based on the selected category
                    populateMenuItems(categories[categoryIndex]);
                }
            });
            buttonPanel.add(categoryButton);
        }
        buttonPanel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return buttonPanel;
    }

    private void populateMenuItems(String category) {
        // Clear existing menu items
        menuListModel.clear();
        // DB Implementation !!
        // Populate the menu items based on the selected category (replace this with your actual data)
        switch (category) {
            case "Drinks":
                menuListModel.addElement(new MenuItem("Drink 1", 2.5));
                menuListModel.addElement(new MenuItem("Drink 2", 3.0));
                // Add more items as needed
                break;
            case "Pizza":
                menuListModel.addElement(new MenuItem("Pizza 1", 12.0));
                menuListModel.addElement(new MenuItem("Pizza 2", 15.0));
                // Add more items as needed
                break;
            // Repeat for other categories
        }
    }

     // Inside the createOrderPanel method
     private JPanel createOrderPanel() {
        JPanel orderPanel = new JPanel();
        orderPanel.setLayout(new BoxLayout(orderPanel, BoxLayout.Y_AXIS));
        orderPanel.setBackground(new Color(240, 240, 240)); // Match the color scheme

        // Create a DefaultListModel to store the order items
        orderListModel = new DefaultListModel<>();

        // Create a JList to display the order items
        JList<MenuItem> orderList = new JList<>(orderListModel);
        orderList.setCellRenderer(new OrderItemRenderer());
        orderList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // Allow only single selection
        orderList.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Add padding
        orderList.setBackground(new Color(240, 240, 240)); // Match the color scheme

        // Add the orderList to the orderPanel
        orderPanel.add(new JScrollPane(orderList));

        // Create a panel for the remove button and total label
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.setBackground(new Color(240, 240, 240)); // Match the color scheme

        // Add the totalLabel to the bottom left of the buttonPanel
        totalLabel = new JLabel("Total: $0.00");
        totalLabel.setFont(new Font("Poppins", Font.BOLD, 16));
        totalLabel.setForeground(new Color(2, 20, 2)); // Match the color scheme
        totalLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 200));
        buttonPanel.add(totalLabel);

        // Add a JButton to remove selected item from the order
        JButton removeButton = Utilities.createStyledButton("Remove", Utilities.DarkOrange, new Color(120,20,30),0);
        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Remove the last item added to the order list
                int lastIndex = orderListModel.getSize() - 1;
                if (lastIndex >= 0) {
                    orderListModel.remove(lastIndex);
                    updateTotalLabel(); // Call updateTotalLabel after removing an item
                }
            }
        });
        removeButton.setForeground(Color.WHITE); // Set text color
        //removeButton.setOpaque(true);
        //removeButton.setBorderPainted(false);
        removeButton.setPreferredSize(new Dimension(100, 30)); // Set button size
        removeButton.setMaximumSize(new Dimension(100, 30));
        buttonPanel.add(Box.createHorizontalGlue()); // Push components to the right
        buttonPanel.add(removeButton);

        // Add the buttonPanel to the orderPanel
        orderPanel.add(buttonPanel);

        return orderPanel;
    }

    // Calculate and update the total price in the order
    private void updateTotalLabel() {
        totalPrice = 0.0; // Reset total price to zero
        for (int i = 0; i < orderListModel.getSize(); i++) {
            totalPrice += orderListModel.getElementAt(i).getPrice();
        }
        totalLabel.setText("Total: $" + String.format("%.2f", totalPrice));
    }


}

class MenuItem {
    private String label;
    private double price;

    public MenuItem(String name, double price) {
        this.label = name;
        this.price = price;
    }

    public String getName() {
        return label;
    }

    public double getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return label + " - $" + String.format("%.2f", price);
    }
}

class MenuItemRenderer extends DefaultListCellRenderer {
    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        label.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10)); // Add padding
        return label;
    }
}

class OrderItemRenderer extends DefaultListCellRenderer {
    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        label.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10)); // Add padding

        if (value instanceof MenuItem) {
            MenuItem menuItem = (MenuItem) value;
            label.setText(menuItem.getName() + " - $" + String.format("%.2f", menuItem.getPrice()));
        }

        return label;
    }
}
