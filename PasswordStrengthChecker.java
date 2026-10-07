import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;

public class PasswordStrengthChecker extends JFrame {

    private final JPasswordField passwordField;
    private final JLabel strengthLabel;
    private final JProgressBar strengthBar;
    private final JToggleButton btnToggleVisibility;

    // Requirement checklist indicators
    private final JLabel lblLengthCheck;
    private final JLabel lblUpperCheck;
    private final JLabel lblNumberCheck;
    private final JLabel lblSpecialCheck;

    public PasswordStrengthChecker() {
        setTitle("Password Strength Checker");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(420, 380);
        setMinimumSize(new Dimension(380, 350));
        setLocationRelativeTo(null);

        // Main Panel with padding
        JPanel mainPanel = new JPanel(new BorderLayout(0, 15));
        mainPanel.setBorder(new EmptyBorder(15, 15, 15, 15));

        // --- 1. INPUT PANEL (Field + Show/Hide Toggle) ---
        JPanel inputPanel = new JPanel(new BorderLayout(5, 5));
        inputPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Enter Password", TitledBorder.LEFT, TitledBorder.TOP));

        passwordField = new JPasswordField();
        passwordField.setFont(new Font("Monospaced", Font.PLAIN, 15));

        btnToggleVisibility = new JToggleButton("Show");
        btnToggleVisibility.setFocusPainted(false);
        btnToggleVisibility.addActionListener(e -> togglePasswordVisibility());

        inputPanel.add(passwordField, BorderLayout.CENTER);
        inputPanel.add(btnToggleVisibility, BorderLayout.EAST);

        // --- 2. STRENGTH DISPLAY PANEL (Label + Bar) ---
        JPanel strengthPanel = new JPanel();
        strengthPanel.setLayout(new BoxLayout(strengthPanel, BoxLayout.Y_AXIS));

        strengthLabel = new JLabel("Status: Waiting for input...");
        strengthLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        strengthLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        strengthBar = new JProgressBar(0, 100);
        strengthBar.setValue(0);
        strengthBar.setStringPainted(false);
        strengthBar.setPreferredSize(new Dimension(300, 12));
        strengthBar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 12));
        strengthBar.setAlignmentX(Component.CENTER_ALIGNMENT);

        strengthPanel.add(strengthLabel);
        strengthPanel.add(Box.createVerticalStrut(8));
        strengthPanel.add(strengthBar);

        // --- 3. CHECKLIST PANEL (Requirements) ---
        JPanel checklistPanel = new JPanel(new GridLayout(4, 1, 5, 5));
        checklistPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Password Requirements", TitledBorder.LEFT, TitledBorder.TOP));

        lblLengthCheck = createCheckLabel("At least 6 characters");
        lblUpperCheck = createCheckLabel("Contains Uppercase Letter (A-Z)");
        lblNumberCheck = createCheckLabel("Contains Number (0-9)");
        lblSpecialCheck = createCheckLabel("Contains Special Character (!@#$)");

        checklistPanel.add(lblLengthCheck);
        checklistPanel.add(lblUpperCheck);
        checklistPanel.add(lblNumberCheck);
        checklistPanel.add(lblSpecialCheck);

        // Assemble Main Panel
        JPanel topContainer = new JPanel(new BorderLayout(0, 10));
        topContainer.add(inputPanel, BorderLayout.NORTH);
        topContainer.add(strengthPanel, BorderLayout.SOUTH);

        mainPanel.add(topContainer, BorderLayout.NORTH);
        mainPanel.add(checklistPanel, BorderLayout.CENTER);

        add(mainPanel);

        // Real-time listener
        passwordField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) { evaluateStrength(); }

            @Override
            public void removeUpdate(DocumentEvent e) { evaluateStrength(); }

            @Override
            public void changedUpdate(DocumentEvent e) { evaluateStrength(); }
        });
    }

    private JLabel createCheckLabel(String text) {
        JLabel label = new JLabel("✕  " + text);
        label.setFont(new Font("SansSerif", Font.PLAIN, 12));
        label.setForeground(Color.GRAY);
        return label;
    }

    private void togglePasswordVisibility() {
        if (btnToggleVisibility.isSelected()) {
            passwordField.setEchoChar((char) 0); // Show text
            btnToggleVisibility.setText("Hide");
        } else {
            passwordField.setEchoChar('•'); // Mask text
            btnToggleVisibility.setText("Show");
        }
    }

    private void evaluateStrength() {
        char[] passwordChars = passwordField.getPassword();
        String password = new String(passwordChars);
        int length = password.length();

        // Check criteria
        boolean hasLength = length >= 6;
        boolean hasUpper = password.matches(".*[A-Z].*");
        boolean hasNumber = password.matches(".*[0-9].*");
        boolean hasSpecial = !password.matches("[a-zA-Z0-9]*");

        // Update checklist UI
        updateIndicator(lblLengthCheck, hasLength, "At least 6 characters");
        updateIndicator(lblUpperCheck, hasUpper, "Contains Uppercase Letter (A-Z)");
        updateIndicator(lblNumberCheck, hasNumber, "Contains Number (0-9)");
        updateIndicator(lblSpecialCheck, hasSpecial, "Contains Special Character (!@#$)");

        // Calculate strength rules
        if (length == 0) {
            strengthLabel.setText("Status: Waiting for input...");
            strengthLabel.setForeground(Color.BLACK);
            strengthBar.setValue(0);
            strengthBar.setForeground(Color.LIGHT_GRAY);
        } else if (length < 6) {
            strengthLabel.setText("Status: Weak (Under 6 characters)");
            strengthLabel.setForeground(Color.RED);
            strengthBar.setValue(25);
            strengthBar.setForeground(Color.RED);
        } else if (length >= 10 && hasSpecial) {
            strengthLabel.setText("Status: Strong");
            strengthLabel.setForeground(new Color(0, 150, 0)); // Dark Green
            strengthBar.setValue(100);
            strengthBar.setForeground(new Color(0, 180, 0));
        } else {
            strengthLabel.setText("Status: Medium");
            strengthLabel.setForeground(new Color(204, 102, 0)); // Orange
            strengthBar.setValue(65);
            strengthBar.setForeground(new Color(230, 130, 0));
        }

        // Wipe password from heap memory
        java.util.Arrays.fill(passwordChars, '0');
    }

    private void updateIndicator(JLabel label, boolean passed, String text) {
        if (passed) {
            label.setText("✓  " + text);
            label.setForeground(new Color(0, 130, 0));
        } else {
            label.setText("✕  " + text);
            label.setForeground(Color.GRAY);
        }
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | 
                 IllegalAccessException | UnsupportedLookAndFeelException ex) {
            ex.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            new PasswordStrengthChecker().setVisible(true);
        });
    }
}