package panels;
import lib.*;
import lombok.Getter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
@Getter
public class LoginPanel extends BasePanel{
    private InteractiveJPasswordField password;
    private InteractiveJTextField username;
    public LoginPanel()
    {
        JPanel upperPanel = new JPanel();
        setBounds(100, 100, 450, 300);
        getAcceptButton().setText("login");
        password = new InteractiveJPasswordField("password");
        username = new InteractiveJTextField("username");
        upperPanel.add(username);
        upperPanel.add(password);
        getDefaultSplitPane().setTopComponent(upperPanel);
        upperPanel.setLayout(new GridLayout(1,2, 200, 200));
        setVisible(true);
    }
}
