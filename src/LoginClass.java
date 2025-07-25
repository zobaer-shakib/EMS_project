import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginClass extends JFrame {
   
    private final String USERNAME = "group3";
    private final String PASSWORD = "khuljasimsim";

    private JTextField usertxt;
    private JPasswordField passwordField;

    public LoginClass() {
    
        setTitle("Sign in");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);
        getContentPane().setBackground(Color.WHITE);
        
        JLabel title = new JLabel("Log in to EMS", SwingConstants.CENTER);
        title.setBounds(50, 20, 400, 60);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        title.setForeground(Color.BLUE);
        add(title);

        JPanel loginPanel = new JPanel();
        loginPanel.setBounds(50, 100, 400, 200);
        loginPanel.setBackground(Color.WHITE);
        loginPanel.setLayout(null);
        add(loginPanel);

        JLabel userl = new JLabel("Username:");
        userl.setBounds(30, 20, 150, 30);
        userl.setForeground(Color.blue);
        userl.setFont(new Font("Arial", Font.PLAIN, 16));
        loginPanel.add(userl);

        usertxt = new JTextField();
        usertxt.setBounds(30, 50, 340, 30);
        usertxt.setFont(new Font("Arial", Font.PLAIN, 16));
        loginPanel.add(usertxt);

        
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(30, 90, 150, 30);
        passwordLabel.setForeground(Color.blue);
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        loginPanel.add(passwordLabel);

        passwordField = new JPasswordField();
        passwordField.setBounds(30, 120, 340, 30);
        passwordField.setFont(new Font("Arial", Font.PLAIN, 16));
        loginPanel.add(passwordField);

        
        JButton logbtn = new JButton("Log In");
        logbtn.setBounds(30, 160, 340, 40);
        logbtn.setFont(new Font("Arial", Font.BOLD, 16));
        
        logbtn.setBackground(new Color(66, 183, 42));
        logbtn.setForeground(Color.WHITE);
        logbtn.addActionListener(new logbtnListener());
        loginPanel.add(logbtn);

   
        setVisible(true);
    }

    private class logbtnListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String username = usertxt.getText();
            String password = new String(passwordField.getPassword());

            if (username.equals(USERNAME) && password.equals(PASSWORD)) {
                JOptionPane.showMessageDialog(null, "Login successful!");
                openEms();
            } else {
                JOptionPane.showMessageDialog(null, "Invalid username or password.", "Login Failed", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

   
    private void openEms() {
       
        dispose();
        new EmployeeManagementGUI();
    }

}
