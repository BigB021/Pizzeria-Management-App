import javax.swing.*;
import javax.swing.border.Border;

import java.awt.*;

public class Utilities {
    // Colors
    protected static Color Green = new Color(0, 20, 2); // 001402
    protected static Color Orange = new Color(253, 164, 9); // FDA409
    protected static Color DarkOrange = new Color(209, 70, 0);

    // Create styled button (size, font, and hover effect)
    protected static JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Poppins", Font.BOLD, 16)); // Set font
        button.setPreferredSize(new Dimension(120, 40)); // Set button size
        button.setBackground(Utilities.Orange); // Set background color
        button.setForeground(Color.WHITE); // Set text color
        button.setBorder(BorderFactory.createLineBorder(Color.BLACK, 0)); // Set border color
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setFocusPainted(false);

        // Add hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(Utilities.DarkOrange);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(Utilities.Orange);
            }
        });

        return button;
    }

    // Create styled button (size, font, and hover effect)
    protected static JButton createStyledButton(String text,Color buttonColor,Color hoverColor, int borderThickness) {
        JButton button = new JButton(text);
        button.setFont(new Font("Poppins", Font.BOLD, 16)); // Set font
        button.setPreferredSize(new Dimension(120, 40)); // Set button size
        button.setBackground(buttonColor); // Set background color
        button.setForeground(Color.WHITE); // Set text color
        button.setBorder(BorderFactory.createLineBorder(Color.BLACK, borderThickness )); // Set border color
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setFocusPainted(false);

        // Add hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(hoverColor);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(buttonColor);
            }
        });

        return button;
    }

    //Create styled text field
    protected static JTextField createStyledTextField(int columns, Color textFieldColor, Color textColor){
        JTextField textField = new JTextField(columns);
        textField.setPreferredSize(new Dimension(textField.getPreferredSize().width, 40));// Adjust the height of the text field
        textField.setFont(new Font("Poppins", Font.PLAIN, 18)); // Increased font size

        // Style the text field with bottom border only
        Border border = BorderFactory.createCompoundBorder(
        BorderFactory.createEmptyBorder(0, 0, 0, 0), // Empty border at the top
        BorderFactory.createMatteBorder(0, 0, 1, 0, Color.BLACK) // Bottom border
        );
        textField.setBorder(border);
        textField.setBackground(textFieldColor); // Set the desired background color
        textField.setForeground(textColor);
        

        return textField;
    }

    protected static JPasswordField createStyledPasswordField(int columns, Color passwordFieldColor, Color textColor){
        JPasswordField passwordField = new JPasswordField(15);
        passwordField.setPreferredSize(new Dimension(passwordField.getPreferredSize().width, 40));// Adjust the height of the text field
        passwordField.setFont(new Font("Poppins", Font.PLAIN, 18)); // Increased font size

        // Style the text field with bottom border only
        Border border = BorderFactory.createCompoundBorder(
        BorderFactory.createEmptyBorder(0, 0, 0, 0), // Empty border at the top
        BorderFactory.createMatteBorder(0, 0, 1, 0, Color.BLACK) // Bottom border
        );
        passwordField.setBorder(border);
        passwordField.setBackground(passwordFieldColor); // Set the desired background color
        passwordField.setForeground(textColor);
        
        return passwordField;
    }
}
