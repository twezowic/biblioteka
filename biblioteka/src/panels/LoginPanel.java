package panels;
import javax.swing.*;
import java.awt.*;

public class LoginPanel {
    public JButton loginButton;
    public JButton cancelButton;
    public JFrame frame;
    public JPasswordField password;
    public JTextField nick;
    public LoginPanel()
    {
        frame = new JFrame("Login");
        JPanel panel = new JPanel();
        frame.setBounds(100, 100, 450, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginButton = new JButton("Login");
        cancelButton = new JButton("Cancel");
        password = new JPasswordField("password");
        nick = new JTextField("nick");
        panel.add(nick);
        panel.add(password);
        panel.add(cancelButton);
        panel.add(loginButton);
        panel.setLayout(new GridLayout(2,2, 50, 50));
        loginButton.setBounds(100, 100, 400, 400);
        cancelButton.setBounds(200, 200, 400, 400);

        frame.add(panel);
        frame.setVisible(true);
    }
}
