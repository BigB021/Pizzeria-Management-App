import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PizzeriaApp extends JFrame {

    private JPanel sideMenuPanel;
    private JButton dashboardButton;
    private JButton categoriesButton;
    private JButton productsButton;
    private JButton staffButton;
    private JButton kitchenButton;
    private JPanel mainContentPanel;

    private LoginPanel loginPanel;

    public PizzeriaApp() {
        super("Pizzeria Management App");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1400, 900);
        //setResizable(false);

        // Initialize the LoginPanel
        loginPanel = new LoginPanel(this);

        // Create the side menu panel
        sideMenuPanel = new JPanel();
        sideMenuPanel.setLayout(new GridLayout(6, 1));
        sideMenuPanel.setPreferredSize(new Dimension(250, getHeight()));
        sideMenuPanel.setBorder(BorderFactory.createEmptyBorder(-1, 0, -1, 0));

        // Create buttons for the side menu
        dashboardButton = createMenuButton("Dashboard", "./assets/dashboard.png", "dashboard");
        categoriesButton = createMenuButton("Categories", "./assets/categories.png", "categories");
        productsButton = createMenuButton("Products", "./assets/products.png", "products");
        staffButton = createMenuButton("Staff", "./assets/staff.png", "staff");
        kitchenButton = createMenuButton("Kitchen", "./assets/kitchen.png", "kitchen");
        
        // Add logo and Pizzeria name
        ImageIcon logoImage = new ImageIcon("./assets/pizza.png"); // Check the path
        Image scaledLogoImage = logoImage.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH); // Resize image
        ImageIcon scaledIcon = new ImageIcon(scaledLogoImage);
        JLabel nameLabel = new JLabel("Pizzeria Al Fadl");
        nameLabel.setBorder(BorderFactory.createEmptyBorder(30, 0, 0, 0));;
        nameLabel.setFont(new Font("Poppins", Font.PLAIN, 20));
        nameLabel.setForeground(Utilities.Orange); 
        nameLabel.setIcon(scaledIcon);

        // Create a panel to hold the logo and name
        JPanel logoPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        logoPanel.setBackground(Utilities.Green); // Set the desired background color
        logoPanel.add(nameLabel);

        // Add buttons to the side menu panel
        sideMenuPanel.add(logoPanel);
        sideMenuPanel.add(dashboardButton);
        sideMenuPanel.add(categoriesButton);
        sideMenuPanel.add(productsButton);
        sideMenuPanel.add(staffButton);
        sideMenuPanel.add(kitchenButton);

        // Set layout for the main content area
        mainContentPanel = new JPanel(new CardLayout());
        mainContentPanel.add(loginPanel, "login");

        // Add the mainContentPanel to the frame
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(mainContentPanel, BorderLayout.CENTER);

        setLocationRelativeTo(null); // Center the frame
        setVisible(true);
    }

    // Create styled menu button
    private JButton createMenuButton(String text, String iconPath, String pageName) {
        JButton button = new JButton(text);
        button.setForeground(Utilities.Orange); 
        button.setBackground(Utilities.Green); 
        button.setBorderPainted(false); // Remove border
        button.setFocusPainted(false); // Remove focus border
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Add action listener to navigate to the corresponding panel
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showPage(pageName);
            }
        });

        // Set icon
        if (iconPath != null) {
            ImageIcon icon = new ImageIcon(iconPath);
            Image scaledIcon = icon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH); // Adjust size
            button.setIcon(new ImageIcon(scaledIcon));
            button.setVerticalTextPosition(SwingConstants.BOTTOM); // Align text below the icon
            button.setHorizontalTextPosition(SwingConstants.CENTER); // Center the text
            button.setIconTextGap(10); // Increase the gap between icon and text
        }

        // Set font to Poppins and increase font size
        Font buttonFont = new Font("Poppins", Font.PLAIN, 16); // Adjust font size
        button.setFont(buttonFont);

        // Add hover effect
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                // On hover color without alpha
                button.setForeground(Utilities.Green);
                button.setBackground(Utilities.Orange);
                button.setVerticalTextPosition(SwingConstants.CENTER);
            }

            public void mouseExited(MouseEvent evt) {
                // Restore background color and text color on exit
                button.setBackground(Utilities.Green);
                button.setForeground(Utilities.Orange);
                button.setVerticalTextPosition(SwingConstants.BOTTOM);
            }

            public void mouseClicked(MouseEvent evt) {
                // Show the corresponding panel when the button is clicked
                showPage(pageName);
            }
        });

        return button;
    }

    private void showPage(String pageName) {
        CardLayout cardLayout = (CardLayout) mainContentPanel.getLayout();
        cardLayout.show(mainContentPanel, pageName);
    }

    // This method will be called from LoginPanel when login is successful
    public void onLoginSuccess() {
        // Remove the login panel
        mainContentPanel.remove(loginPanel);

        // Add other components to the main content area as needed
        mainContentPanel.add(new DashboardPanel(), "dashboard");
        mainContentPanel.add(new CategoriesPanel(), "categories");
        mainContentPanel.add(new ProductsPanel(), "products");
        mainContentPanel.add(new StaffPanel(), "staff");
        mainContentPanel.add(new KitchenPanel(), "kitchen");
        // Add components to the main frame
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(sideMenuPanel, BorderLayout.WEST);
        getContentPane().add(mainContentPanel, BorderLayout.CENTER);

        setLocationRelativeTo(null); // Center the frame
        setVisible(true);

        
        mainContentPanel.validate();
        mainContentPanel.repaint();
    }

    // Main 
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new PizzeriaApp());
    }
}
