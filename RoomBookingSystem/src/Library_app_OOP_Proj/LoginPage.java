// LoginAndSignupPage.java
package Library_app_OOP_Proj;

//LoginPage.java
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class LoginPage extends JFrame {
	private JTextField usernameField;
	 private JPasswordField passwordField;
	 private static final int FRAME_WIDTH = 800;
	 private static final int FRAME_HEIGHT = 500;
	 private static final int HEADER_HEIGHT = 100;

	 public LoginPage() {
	     initializeFrame();
	     createComponents();
	     pack();
	     setLocationRelativeTo(null);
	 }

	 private void initializeFrame() {
	     setTitle("Library Room Booking System");
	     setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	     setPreferredSize(new Dimension(FRAME_WIDTH, FRAME_HEIGHT));
	     setResizable(false);
	 }

	 private void createComponents() {
	     // Header Panel
	     JPanel headerPanel = createHeaderPanel();
	     add(headerPanel, BorderLayout.NORTH);

	     // Login Panel
	     JPanel loginPanel = createLoginPanel();
	     add(loginPanel, BorderLayout.CENTER);
	 }

	 private JPanel createHeaderPanel() {
	     JPanel panel = new JPanel();
	     panel.setBackground(new Color(0, 52, 52));
	     panel.setPreferredSize(new Dimension(FRAME_WIDTH, HEADER_HEIGHT));

	     JLabel titleLabel = new JLabel("LIBRARY ROOM BOOKING");
	     titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 36));
	     titleLabel.setForeground(Color.WHITE);
	     panel.add(titleLabel);

	     return panel;
	 }

	 private JPanel createLoginPanel() {
	     JPanel panel = new JPanel(new GridBagLayout());
	     panel.setBackground(Color.WHITE);
	     GridBagConstraints gbc = new GridBagConstraints();
	     gbc.fill = GridBagConstraints.HORIZONTAL;
	     gbc.insets = new Insets(5, 5, 5, 5);

	     // Username Field
	     JLabel usernameLabel = new JLabel("Username:");
	     usernameField = new JTextField(20);
	     usernameField.setPreferredSize(new Dimension(200, 30));

	     // Password Field
	     JLabel passwordLabel = new JLabel("Password:");
	     passwordField = new JPasswordField(20);
	     passwordField.setPreferredSize(new Dimension(200, 30));

	     // Login Button
	     JButton loginButton = new JButton("Login");
	     loginButton.setBackground(new Color(0, 52, 52));
	     loginButton.setForeground(Color.WHITE);
	     loginButton.setPreferredSize(new Dimension(200, 35));
	     loginButton.addActionListener(e -> handleLogin());

	     // Sign Up Button
	     JButton signupButton = new JButton("Create New Account");
	     signupButton.setForeground(new Color(0, 52, 52));
	     signupButton.setBorderPainted(false);
	     signupButton.setContentAreaFilled(false);
	     signupButton.addActionListener(e -> openSignupPage());

	     // Add key listeners for Enter key
	     KeyListener enterKeyListener = new KeyAdapter() {
	         @Override
	         public void keyPressed(KeyEvent e) {
	             if (e.getKeyCode() == KeyEvent.VK_ENTER) {
	                 handleLogin();
	             }
	         }
	     };
	     usernameField.addKeyListener(enterKeyListener);
	     passwordField.addKeyListener(enterKeyListener);

	     // Add components to panel
	     gbc.gridx = 0;
	     gbc.gridy = 0;
	     gbc.gridwidth = 2;
	     panel.add(usernameLabel, gbc);

	     gbc.gridy = 1;
	     panel.add(usernameField, gbc);

	     gbc.gridy = 2;
	     panel.add(passwordLabel, gbc);

	     gbc.gridy = 3;
	     panel.add(passwordField, gbc);

	     gbc.gridy = 4;
	     gbc.insets = new Insets(20, 5, 5, 5);
	     panel.add(loginButton, gbc);

	     gbc.gridy = 5;
	     gbc.insets = new Insets(5, 5, 5, 5);
	     panel.add(signupButton, gbc);

	     return panel;
	 }

	 private void handleLogin() {
	     String username = usernameField.getText();
	     String password = new String(passwordField.getPassword());

	     if (username.isEmpty() || password.isEmpty()) {
	         showError("Please enter both username and password");
	         return;
	     }

	     // Check admin login
	     for (Admin admin : Admin.getAdminList()) {
	         if (admin.getUsername().equals(username) &&  admin.getPassword().equals(password)) {
	             openAdminPage(admin);
	             return;
	         }
	     }

	     // Check user login
	     for (User user : User.getUserList()) {
	         if (user.getUsername().equals(username) &&  user.getPassword().equals(password)) {
	        	 openBookingPage(user);
	             return;
	         }
	     }

	     showError("Invalid username or password");
	 }

	 private void openBookingPage(User user) {
	     dispose();
	     new BookingPage(user).setVisible(true);
	 }

	 private void openAdminPage(Admin admin) {
	     dispose();
	     new ManagePage(admin).setVisible(true);
	 }

	 private void openSignupPage() {
	     dispose();
	     new SignupPage().setVisible(true);
	 }

	 private void showError(String message) {
	     JOptionPane.showMessageDialog(this, message, "Login Error", JOptionPane.ERROR_MESSAGE);
	 }
}
