package Library_app_OOP_Proj;

//SignupPage.java
import javax.swing.*;
import java.awt.*;

public class SignupPage extends JFrame {
	private JTextField usernameField;
	private JPasswordField passwordField;
	private JPasswordField confirmPasswordField;
	private JComboBox<String> userTypeCombo;
	private static final int FRAME_WIDTH = 800;
	private static final int FRAME_HEIGHT = 500;
	private static final int HEADER_HEIGHT = 100;

	public SignupPage() {
	   initializeFrame();
	   createComponents();
	   pack();
	   setLocationRelativeTo(null);
	}

	private void initializeFrame() {
	   setTitle("Library Room Booking System - Sign Up");
	   setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	   setPreferredSize(new Dimension(FRAME_WIDTH, FRAME_HEIGHT));
	   setResizable(false);
	}

	private void createComponents() {
	   // Header Panel
	   JPanel headerPanel = createHeaderPanel();
	   add(headerPanel, BorderLayout.NORTH);

	   // Signup Panel
	   JPanel signupPanel = createSignupPanel();
	   add(signupPanel, BorderLayout.CENTER);
	}

	private JPanel createHeaderPanel() {
	   JPanel panel = new JPanel();
	   panel.setBackground(new Color(0, 52, 52));
	   panel.setPreferredSize(new Dimension(FRAME_WIDTH, HEADER_HEIGHT));

	   JLabel titleLabel = new JLabel("CREATE NEW ACCOUNT");
	   titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 36));
	   titleLabel.setForeground(Color.WHITE);
	   panel.add(titleLabel);

	   return panel;
	}

	private JPanel createSignupPanel() {
	   JPanel mainPanel = new JPanel();
	   mainPanel.setBackground(Color.WHITE);
	   mainPanel.setLayout(new GridBagLayout());

	   JPanel formPanel = createSignupForm();
	   mainPanel.add(formPanel);

	   return mainPanel;
	}

	private JPanel createSignupForm() {
	   JPanel panel = new JPanel(new GridBagLayout());
	   panel.setBackground(Color.WHITE);
	   GridBagConstraints gbc = new GridBagConstraints();
	   gbc.fill = GridBagConstraints.HORIZONTAL;
	   gbc.insets = new Insets(5, 5, 5, 5);

	   // User Type Selection
	   JLabel typeLabel = new JLabel("Register as:");
	   userTypeCombo = new JComboBox<>(new String[]{"Student", "Lecturer", "General"});

	   // Username Field
	   JLabel usernameLabel = new JLabel("Username:");
	   usernameField = new JTextField(20);

	   // Password Fields
	   JLabel passwordLabel = new JLabel("Password:");
	   passwordField = new JPasswordField(20);

	   JLabel confirmLabel = new JLabel("Confirm Password:");
	   confirmPasswordField = new JPasswordField(20);

	   // Buttons
	   JButton registerButton = createRegisterButton();
	   JButton backButton = createBackButton();

	   // Add components
	   gbc.gridx = 0;
	   gbc.gridy = 0;
	   gbc.gridwidth = 2;
	   panel.add(typeLabel, gbc);

	   gbc.gridy = 1;
	   panel.add(userTypeCombo, gbc);

	   gbc.gridy = 2;
	   panel.add(usernameLabel, gbc);

	   gbc.gridy = 3;
	   panel.add(usernameField, gbc);

	   gbc.gridy = 4;
	   panel.add(passwordLabel, gbc);

	   gbc.gridy = 5;
	   panel.add(passwordField, gbc);

	   gbc.gridy = 6;
	   panel.add(confirmLabel, gbc);

	   gbc.gridy = 7;
	   panel.add(confirmPasswordField, gbc);

	   gbc.gridy = 8;
	   gbc.insets = new Insets(20, 5, 5, 5);
	   panel.add(registerButton, gbc);

	   gbc.gridy = 9;
	   gbc.insets = new Insets(5, 5, 5, 5);
	   panel.add(backButton, gbc);

	   return panel;
	}

	private JButton createRegisterButton() {
	   JButton button = new JButton("Register");
	   button.setBackground(new Color(0, 52, 52));
	   button.setForeground(Color.WHITE);
	   button.addActionListener(e -> handleSignup());
	   return button;
	}

	private JButton createBackButton() {
	   JButton button = new JButton("Back to Login");
	   button.setForeground(new Color(0, 52, 52));
	   button.setBorderPainted(false);
	   button.setContentAreaFilled(false);
	   button.addActionListener(e -> openLoginPage());
	   return button;
	}

	private void handleSignup() {
	   String username = usernameField.getText();
	   String password = new String(passwordField.getPassword());
	   String confirmPassword = new String(confirmPasswordField.getPassword());
	   String userType = (String) userTypeCombo.getSelectedItem();

	   // Validation
	   if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
	       showError("Please fill in all fields");
	       return;
	   }

	   if (!password.equals(confirmPassword)) {
	       showError("Passwords do not match");
	       return;
	   }

	   try {
	       createUser(username, password, userType);
	       showSuccess();
	       openLoginPage();
	   } catch (IllegalArgumentException e) {
	       showError(e.getMessage());
	   }
	}

	private void createUser(String username, String password, String userType) {
	   switch (userType) {
	       case "Student":
	           new Student(username, password);
	           break;
	       case "Lecturer":
	           new Lecturer(username, password);
	           break;
	       case "General":
	           new General(username, password);
	           break;
	   }
	}

	private void showError(String message) {
	   JOptionPane.showMessageDialog(this,
	       message,
	       "Registration Error",
	       JOptionPane.ERROR_MESSAGE);
	}

	private void showSuccess() {
	   JOptionPane.showMessageDialog(this,
	       "Registration successful! Please login.",
	       "Success",
	       JOptionPane.INFORMATION_MESSAGE);
	}

	private void openLoginPage() {
	   dispose();
	   new LoginPage().setVisible(true);
	}
}