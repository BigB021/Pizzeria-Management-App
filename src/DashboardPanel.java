/* 
 * Author: Youssef AITBOUDDROUB
 */

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.geom.RoundRectangle2D; 
import java.awt.geom.Point2D;



public class DashboardPanel extends JPanel {

    private JLabel titleLabel;
    private JLabel usernameLabel;
    private JLabel dateTimeLabel;

    public DashboardPanel() {
        setLayout(new BorderLayout());
    
        // Top Header
        JPanel topHeaderPanel = createHeaderPanel("Dashboard");
        add(topHeaderPanel, BorderLayout.NORTH);

        // Global Statistics
        JPanel statisticsPanel = createStatisticsPanel();
        add(statisticsPanel, BorderLayout.CENTER);

        // Last Orders Table
        JPanel lastOrdersPanel = createLastOrdersPanel();
        add(lastOrdersPanel, BorderLayout.SOUTH);
    }

    // Create a header panel with title, username, and profile icon
    private JPanel createHeaderPanel(String title) {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(1, 20, 2))); // Add a separating line
        headerPanel.setPreferredSize(new Dimension(getWidth(), 80)); // Increase header height
        headerPanel.setBackground(Color.WHITE); // Set the background color for the header

        // Dashboard Title
        titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Poppins", Font.BOLD, 20)); // Adjust font size and style
        titleLabel.setForeground(new Color(0, 20, 2)); // Set the title color
        titleLabel.setHorizontalAlignment(SwingConstants.LEFT);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0)); // Add left padding
        headerPanel.add(titleLabel, BorderLayout.WEST);

        // Profile Icon
        ImageIcon profileIcon = new ImageIcon("./assets/profile.png"); // Replace with the path to your profile icon
        Image scaledLogoImage = profileIcon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH); // Resize image
        profileIcon = new ImageIcon(scaledLogoImage);

        // Create a label to display date and time
        dateTimeLabel = new JLabel(getCurrentDateTime(), SwingConstants.CENTER);
        dateTimeLabel.setForeground(Utilities.Green); // Set text color
        dateTimeLabel.setFont(new Font("Poppins", Font.PLAIN, 18)); // Set font style
        headerPanel.add(dateTimeLabel, BorderLayout.CENTER);

        // Use Timer to update the date and time every second
        Timer timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dateTimeLabel.setText(getCurrentDateTime());
            }
        });
        timer.start();

        // Admin Username
        usernameLabel = new JLabel(getAdminUsername());
        usernameLabel.setFont(new Font("Poppins", Font.BOLD, 18)); // Adjust font size
        usernameLabel.setForeground(Utilities.Green); // Set the text color
        usernameLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        usernameLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 50)); // Add right padding
        usernameLabel.setIcon(profileIcon);
        headerPanel.add(usernameLabel, BorderLayout.EAST);

        return headerPanel;
    }

    private String getCurrentDateTime() {
        // Get the current date and time in the desired format
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss");
        Date now = new Date();
        return dateFormat.format(now);
    }

    // Method to retrieve admin username (replace this with your logic)
    private String getAdminUsername() {
        // DB Implementation !!
        return LoginPanel.USERNAME; // Replace with your logic to get the admin username
    }

    // Create a panel for displaying global statistics
    private JPanel createStatisticsPanel() {
        JPanel statisticsPanel = new JPanel(new GridLayout(1, 3, 30, 0)){ // 1 row, 3 columns, horizontal gap of 30
                    @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);

                int width = getWidth();
                int height = getHeight();

                // Define the start and end points for the gradient
                Point2D startPoint = new Point2D.Float(0, 0);
                Point2D endPoint = new Point2D.Float(0, height);

                // Define the colors for the gradient with reduced passthrough (40%)
                Color startColor = new Color(253, 164, 9, 102); 
                Color endColor = new Color(209, 70, 0, 102); 


                // Create a linear gradient paint
                LinearGradientPaint gradientPaint = new LinearGradientPaint(startPoint, endPoint,
                        new float[]{0.0f, 1.0f}, new Color[]{startColor, endColor});

                // Fill the entire headerPanel with the gradient paint
                Graphics2D graphics = (Graphics2D) g.create();
                graphics.setPaint(gradientPaint);
                graphics.fillRect(0, 0, width, height);
                graphics.dispose();
            }
        };
        statisticsPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30)); // Add padding

        // Display Orders card
        JPanel ordersPanel = createCardPanel("Orders Being Prepared", "15", "./assets/order.png", new Color(102, 250, 154));
        statisticsPanel.add(ordersPanel);

        // Display Tables card
        JPanel tablesPanel = createCardPanel("Available Tables", "15/24", "./assets/table.png", new Color(52, 192, 169));
        statisticsPanel.add(tablesPanel);

        // Display Earnings card
        JPanel earningsPanel = createCardPanel("Daily Earnings", "$500.00", "./assets/money.png", new Color(209, 70, 0));
        statisticsPanel.add(earningsPanel);

        
        return statisticsPanel;
    }

    // Create a card panel with specified title, value, icon, color, and corner radius
    private JPanel createCardPanel(String title, String value, String iconPath, Color panelColor) {
        JPanel cardPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                int width = getWidth();
                int height = getHeight();
                int radius = 30; // Adjust the radius for rounded corners

                Graphics2D graphics = (Graphics2D) g.create();
                graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Define the start and end points for the gradient
                Point2D startPoint = new Point2D.Float(0, 0);
                Point2D endPoint = new Point2D.Float(0, height);

                // Define the colors for the gradient (adjust these colors as needed)
                Color startColor = panelColor.brighter();
                Color endColor = panelColor.darker();

                // Create a linear gradient paint
                GradientPaint gradientPaint = new GradientPaint(startPoint, startColor, endPoint, endColor);

                // Fill the rounded rectangle with the gradient paint
                RoundRectangle2D rect = new RoundRectangle2D.Float(0, 0, width - 1, height - 1, radius, radius);
                graphics.setPaint(gradientPaint);
                graphics.fill(rect);

                graphics.dispose();
            }
        };

        // Set the background color of the card to be fully transparent
        cardPanel.setOpaque(false);

        // Create an icon label
        JLabel iconLabel = new JLabel(new ImageIcon(iconPath));
        iconLabel.setVerticalAlignment(SwingConstants.TOP); // Align the icon to the top
        iconLabel.setBorder(BorderFactory.createEmptyBorder(30, 20, 0, 20)); // Adjust padding

        // Create a panel for text (title and value)
        JPanel textPanel = new JPanel(new GridLayout(2, 1)); // 2 rows, 1 column
        textPanel.setOpaque(false); // Make the background transparent

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Poppins", Font.BOLD, 18));
        titleLabel.setForeground(new Color(0, 20, 2)); // Set text color
        titleLabel.setHorizontalAlignment(SwingConstants.LEFT); // Align the title to the left

        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Poppins", Font.BOLD, 24));
        valueLabel.setForeground(new Color(0, 20, 2)); // Set text color
        valueLabel.setHorizontalAlignment(SwingConstants.CENTER); // Center the value
        valueLabel.setBorder(BorderFactory.createEmptyBorder(0, -80, 0, 0));// Adjusting the padding

        // Add components to the textPanel
        textPanel.add(titleLabel);
        textPanel.add(valueLabel);

        // Add components to the cardPanel
        cardPanel.add(iconLabel, BorderLayout.WEST); // Icon label will be left-aligned
        cardPanel.add(textPanel, BorderLayout.CENTER);

        return cardPanel;
    }



// Create a panel for displaying the last orders table with scrolling
private JPanel createLastOrdersPanel() {
    JPanel lastOrdersPanel = new JPanel(new BorderLayout());
    lastOrdersPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30)); // Add padding

    // Create table model and add sample data
    String[] columnNames = {"CODE", "ITEM", "P.U", "QTE", "TOTAL"};
    Object[][] data = {
            {"001", "Item 1", "$10.00", 2, "$20.00"},
            {"002", "Item 2", "$15.00", 3, "$45.00"},
            {"003", "Item 3", "$20.00", 1, "$20.00"},
            {"004", "Item 4", "$5.00", 4, "$20.00"},
            {"001", "Item 1", "$10.00", 2, "$20.00"},
            {"002", "Item 2", "$15.00", 3, "$45.00"},
            {"003", "Item 3", "$20.00", 1, "$20.00"},
            {"004", "Item 4", "$5.00", 4, "$20.00"},
            
            // DB Implementation !!
    };
    DefaultTableModel tableModel = new DefaultTableModel(data, columnNames) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false; // Make all cells non-editable
        }
    };
    JTable lastOrdersTable = new JTable(tableModel);

    // Disable column reordering
    lastOrdersTable.getTableHeader().setReorderingAllowed(false);

    // Set font size of the table
    lastOrdersTable.setFont(new Font("Poppins", Font.PLAIN, 16));
    lastOrdersTable.getTableHeader().setFont(new Font("Poppins", Font.BOLD, 18));

    // Remove selection effect
    lastOrdersTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

    // Set row height for the table cells
    lastOrdersTable.setRowHeight(40); // Adjust the value as needed

    // Create custom cell renderer to set padding
    DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer() {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            // Set padding for each cell
            int topPadding = 10;
            int bottomPadding = 10;
            int leftPadding = 10;
            int rightPadding = 10;

            setBorder(BorderFactory.createEmptyBorder(topPadding, leftPadding, bottomPadding, rightPadding));

            return cell;
        }
    };

    // Apply the custom cell renderer to each column
    for (int i = 0; i < lastOrdersTable.getColumnCount(); i++) {
        lastOrdersTable.getColumnModel().getColumn(i).setCellRenderer(cellRenderer);
    }

    // Create a scroll pane and set its preferred size
    JScrollPane scrollPane = new JScrollPane(lastOrdersTable);
    scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
    scrollPane.setPreferredSize(new Dimension(lastOrdersPanel.getWidth(), 400)); // Set the preferred height

    // Add the scroll pane to the lastOrdersPanel
    lastOrdersPanel.add(scrollPane, BorderLayout.CENTER);

    // Add a title to the table
    JLabel titleLabel = new JLabel("Recent Orders");
    titleLabel.setFont(new Font("Poppins", Font.BOLD, 20));
    titleLabel.setForeground(new Color(0, 20, 2));
    titleLabel.setHorizontalAlignment(SwingConstants.LEFT); // Center the title
    titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 20, 20, 0)); 
    lastOrdersPanel.add(titleLabel, BorderLayout.NORTH);

    // Set background color of the table
    lastOrdersTable.setBackground(new Color(253, 164, 9, 51)); // 20% passthrough
    lastOrdersTable.getTableHeader().setBackground(new Color(253, 164, 9, 127)); // 50% passthrough

    return lastOrdersPanel;
}

}
