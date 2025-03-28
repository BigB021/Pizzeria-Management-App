import javax.swing.*;
import java.awt.*;

public class ProductsPanel extends JPanel {

    public ProductsPanel() {
        setLayout(new BorderLayout());
        setBackground(new Color(255, 255, 255)); // Set the background color as needed

        JLabel titleLabel = new JLabel("Products");
        titleLabel.setFont(new Font("Poppins", Font.BOLD, 24)); // Adjust font size and style
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // Add other categories components here

        add(titleLabel, BorderLayout.NORTH);
        // Add other components to the categories panel as needed
    }
}