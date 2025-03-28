import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class LoginPanel extends JPanel {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    protected static String USERNAME;
    protected static String PASSWORD;

    private PizzeriaApp pizzeriaApp; // Add this field

    public LoginPanel(PizzeriaApp pizzeriaApp) {
        // Set layout to GridBagLayout
        setLayout(new GridBagLayout());
        this.pizzeriaApp = pizzeriaApp;

        // Left Panel - Logo, Name, and Text
        JPanel leftPanel = new JPanel(new GridBagLayout());
        leftPanel.setBackground(new Color(0, 20, 2)); // Set the desired background color

        // Add logo and Pizzeria name
        ImageIcon logoImage = new ImageIcon("./assets/pizza.png"); // Check the path
        Image scaledLogoImage = logoImage.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH); // Resize image
        ImageIcon scaledIcon = new ImageIcon(scaledLogoImage);
        JLabel nameLabel = new JLabel("Pizzeria Al Fadl");
        nameLabel.setFont(new Font("Poppins", Font.PLAIN, 30)); // Increased font size
        nameLabel.setForeground(new Color(253, 164, 9)); // FDA409
        nameLabel.setIcon(scaledIcon);

        // Center components in the left panel
        GridBagConstraints gbcLeft = new GridBagConstraints();
        gbcLeft.gridx = 0;
        gbcLeft.gridy = 0;
        leftPanel.add(nameLabel, gbcLeft);

        // Add a cool catchphrase as a slogan
        JLabel sloganLabel = new JLabel("Delivering happiness one slice at a time!");
        sloganLabel.setFont(new Font("Poppins", Font.PLAIN, 16));
        sloganLabel.setForeground(Utilities.Orange);
        GridBagConstraints gbcSlogan = new GridBagConstraints();
        gbcSlogan.gridx = 0;
        gbcSlogan.gridy = 1; // Adjusted the grid y position
        gbcSlogan.insets = new Insets(-50, 235, 20, 0); // Add some spacing at the bottom
        leftPanel.add(sloganLabel, gbcSlogan);


        // Right Panel - Sign-Up Form
        JPanel rightPanel = new JPanel(new GridBagLayout());
        rightPanel.setBackground(new Color(253, 164, 9)); // Set the desired background color

        // Profile Icon
        ImageIcon profileIcon = new ImageIcon("./assets/account.png"); // Replace with the path to your profile icon
        scaledLogoImage = profileIcon.getImage().getScaledInstance(35, 35, Image.SCALE_SMOOTH); // Resize image
        profileIcon = new ImageIcon(scaledLogoImage);

        // Username Label and Field
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(new Font("Poppins", Font.BOLD, 18)); // Increased font size
        usernameLabel.setForeground(Utilities.Green);
        usernameLabel.setIcon(profileIcon);
        usernameLabel.setBorder(BorderFactory.createEmptyBorder(50, 0, 10, 0));
        GridBagConstraints gbcUsernameLabel = new GridBagConstraints();
        gbcUsernameLabel.gridx = 0;
        gbcUsernameLabel.gridy = 0;
        gbcUsernameLabel.anchor = GridBagConstraints.WEST;
        gbcUsernameLabel.insets = new Insets(0, 0, 10, 0); // Add some spacing at the bottom
        rightPanel.add(usernameLabel, gbcUsernameLabel);

        usernameField = Utilities.createStyledTextField(15,Utilities.Orange,Utilities.Green);
        GridBagConstraints gbcUsernameField = new GridBagConstraints();
        gbcUsernameField.gridx = 0;
        gbcUsernameField.gridy = 1;
        gbcUsernameField.insets = new Insets(0, 0, 10, 0); // Add some spacing at the bottom
        rightPanel.add(usernameField, gbcUsernameField);

        // Password Icon
        ImageIcon passwordIcon = new ImageIcon("./assets/padlock.png"); // Replace with the path to your profile icon
        scaledLogoImage = passwordIcon.getImage().getScaledInstance(35, 35, Image.SCALE_SMOOTH); // Resize image
        passwordIcon = new ImageIcon(scaledLogoImage);
        // Password Label and Field
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Poppins", Font.BOLD, 18)); // Increased font size
        passwordLabel.setForeground(Utilities.Green);
        passwordLabel.setIcon(passwordIcon);
        passwordLabel.setBorder(BorderFactory.createEmptyBorder(50, 0, 10, 0));
        GridBagConstraints gbcPasswordLabel = new GridBagConstraints();
        gbcPasswordLabel.gridx = 0;
        gbcPasswordLabel.gridy = 2;
        gbcPasswordLabel.anchor = GridBagConstraints.WEST;
        gbcPasswordLabel.insets = new Insets(0, 0, 10, 0); // Add some spacing at the bottom
        rightPanel.add(passwordLabel, gbcPasswordLabel);

        passwordField = Utilities.createStyledPasswordField(15,Utilities.Orange,Utilities.Green);
        GridBagConstraints gbcPasswordField = new GridBagConstraints();
        gbcPasswordField.gridx = 0;
        gbcPasswordField.gridy = 3;
        gbcPasswordField.insets = new Insets(0, 0, 20, 0); // Add some spacing at the bottom
        rightPanel.add(passwordField, gbcPasswordField);

        // Login Button
        loginButton = Utilities.createStyledButton("Login",Utilities.Green,Utilities.DarkOrange,1);
        loginButton.setFont(new Font("Poppins", Font.PLAIN, 18)); // Increased font size
        GridBagConstraints gbcLoginButton = new GridBagConstraints();
        gbcLoginButton.gridx = 0;
        gbcLoginButton.gridy = 4;
        rightPanel.add(loginButton, gbcLoginButton);

        // Add Action Listener to the login button
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Add your login logic here
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());

                // Database Implementation
                try (Connection connection = DatabaseConnection.getConnection()) {
                    String query = "SELECT ROLE FROM STAFF WHERE NAME = ? AND PASSWORD = ?";
                    try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                        preparedStatement.setString(1, username);
                        preparedStatement.setString(2, password);

                        try (ResultSet resultSet = preparedStatement.executeQuery()) {
                            if (resultSet.next()) {
                                String role = resultSet.getString("ROLE");
                                // Check if the role is manager or cashier
                                if ("MANAGER".equals(role) || "CASHIER".equals(role)) {
                                    // Authentication successful, launch the PizzeriaApp
                                    launchPizzeriaApp();
                                } else {
                                    // Authentication failed, show an error message
                                    JOptionPane.showMessageDialog(LoginPanel.this,
                                            "Invalid username, password, or role", "Login Failed", JOptionPane.ERROR_MESSAGE);
                                }
                            } else {
                                // No matching user found
                                JOptionPane.showMessageDialog(LoginPanel.this,
                                        "Invalid username or password", "Login Failed", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace(); // Handle the exception appropriately
                }

                USERNAME = username;
                PASSWORD = password;
            }
        });

        // Add the left and right panels to the main panel with adjusted constraints
        gbcLeft.gridx = 0;
        gbcLeft.gridy = 0;
        gbcLeft.gridheight = GridBagConstraints.REMAINDER; // Occupy all rows
        gbcLeft.weightx = 0.4; // 40% of width
        gbcLeft.weighty = 1.0;
        gbcLeft.fill = GridBagConstraints.BOTH; // Occupy both horizontal and vertical space
        add(leftPanel, gbcLeft);

        GridBagConstraints gbcRight = new GridBagConstraints();
        gbcRight.gridx = 1;
        gbcRight.gridy = 0;
        gbcRight.weightx = 0.6; // 60% of width
        gbcRight.fill = GridBagConstraints.BOTH; // Occupy both horizontal and vertical space
        add(rightPanel, gbcRight);

        // Adjust the appearance of your panel as needed
        setBackground(new Color(253, 164, 9));
    }

    public JTextField getUsernameField() {
        return usernameField;
    }

    public JPasswordField getPasswordField() {
        return passwordField;
    }

    public JButton getLoginButton() {
        return loginButton;
    }

    private void launchPizzeriaApp() {
        SwingUtilities.invokeLater(() -> {
            // Launch the PizzeriaApp using the stored reference
            pizzeriaApp.onLoginSuccess();
        });
    }
}
