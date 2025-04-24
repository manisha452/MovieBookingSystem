import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.regex.Pattern;
import java.sql.PreparedStatement;


public class RegisterForm extends JFrame {
    private JTextField nameField, emailField, phoneField;
    private JPasswordField passwordField, confirmPasswordField;
    private JButton signUpButton, cancelButton;
    private JCheckBox termsCheckBox;

    public RegisterForm() {
        setTitle("Create Your Movie Booking Account");
        setSize(500, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(new Color(240, 240, 240));

        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(50, 50, 100));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        JLabel titleLabel = new JLabel("SIGN UP", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel);
        add(headerPanel, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridLayout(7, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        formPanel.setBackground(new Color(240, 240, 240));

        addFormField(formPanel, "Full Name:", nameField = new JTextField());
        addFormField(formPanel, "Email Address:", emailField = new JTextField());
        addFormField(formPanel, "Phone Number:", phoneField = new JTextField());
        addFormField(formPanel, "Password:", passwordField = new JPasswordField());
        addFormField(formPanel, "Confirm Password:", confirmPasswordField = new JPasswordField());

        formPanel.add(new JLabel(""));
        termsCheckBox = new JCheckBox("I agree to Terms & Conditions and Privacy Policy");
        termsCheckBox.setBackground(new Color(240, 240, 240));
        formPanel.add(termsCheckBox);

        formPanel.add(new JLabel(""));
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        signUpButton = new JButton("SIGN UP");
        styleButton(signUpButton, new Color(76, 175, 80));
        cancelButton = new JButton("CANCEL");
        styleButton(cancelButton, new Color(244, 67, 54));
        buttonPanel.add(signUpButton);
        buttonPanel.add(cancelButton);
        formPanel.add(buttonPanel);

        add(formPanel, BorderLayout.CENTER);

        JPanel footerPanel = new JPanel();
        footerPanel.setBackground(new Color(240, 240, 240));
        JLabel loginLabel = new JLabel("Already have an account? Login here");
        loginLabel.setForeground(Color.BLUE.darker());
        loginLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        loginLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                dispose();
                new LoginForm().setVisible(true);
            }
        });
        footerPanel.add(loginLabel);
        add(footerPanel, BorderLayout.SOUTH);

        signUpButton.addActionListener(e -> registerUser());
        cancelButton.addActionListener(e -> {
            dispose();
            new WelcomeForm().setVisible(true);
        });

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void addFormField(JPanel panel, String labelText, JTextField field) {
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Arial", Font.BOLD, 12));
        panel.add(label);
        panel.add(field);
    }

    private void registerUser() {
        String name = nameField.getText().trim();
        String email = emailField.getText().trim().toLowerCase();
        String phone = phoneField.getText().trim();
        String password = new String(passwordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());

        if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || password.isEmpty()) {
            showError("All fields are required!");
            return;
        }

        if (!termsCheckBox.isSelected()) {
            showError("You must agree to Terms & Conditions!");
            return;
        }

        if (!password.equals(confirmPassword)) {
            showError("Passwords do not match!");
            return;
        }

        if (!isValidEmail(email)) {
            showError("Please enter a valid email address!");
            return;
        }

        if (!isValidPhone(phone)) {
            showError("Please enter a valid 10-digit phone number!");
            return;
        }

            Database conn=new Database();
try{

            String query = "INSERT INTO users (username, full_name, email, phone, password) VALUES (?, ?, ?, ?, ?)";

            try (PreparedStatement pst = conn.con.prepareStatement(query)) {
                pst.setString(1, email);
                pst.setString(2, name);
                pst.setString(3, email);
                pst.setString(4, phone);
                pst.setString(5, password);
                pst.executeUpdate();

                showSuccess("Registration Successful! You can now login.");
                dispose();
                new LoginForm().setVisible(true);
            }
        } catch (SQLException ex) {
            showError("Database Error: " + ex.getMessage());
        }
    }

    private boolean userExists(Connection conn, String email) throws SQLException {
        String query = "SELECT id FROM users WHERE email = ?";
        try (PreparedStatement pst = conn.prepareStatement(query)) {
            pst.setString(1, email);
            try (ResultSet rs = pst.executeQuery()) {
                return rs.next();
            }
        }
    }

    private boolean phoneExists(Connection conn, String phone) throws SQLException {
        String query = "SELECT id FROM users WHERE phone = ?";
        try (PreparedStatement pst = conn.prepareStatement(query)) {
            pst.setString(1, phone);
            try (ResultSet rs = pst.executeQuery()) {
                return rs.next();
            }
        }
    }

    private String hashPassword(String plainPassword) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = md.digest(plainPassword.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hashedBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Hashing algorithm not found", e);
        }
    }

    private boolean isValidEmail(String email) {
        return Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$").matcher(email).matches();
    }

    private boolean isValidPhone(String phone) {
        return Pattern.compile("^\\d{10}$").matcher(phone).matches();
    }

    private void styleButton(JButton button, Color bgColor) {
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void showSuccess(String message) {
        JOptionPane.showMessageDialog(this, message, "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            SwingUtilities.invokeLater(() -> new RegisterForm().setVisible(true));
        } catch (ClassNotFoundException e) {
            JOptionPane.showMessageDialog(null,
                "MySQL JDBC Driver not found!",
                "Database Error",
                JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }
}