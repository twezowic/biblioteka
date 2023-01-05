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
        setBounds(500, 200, 450, 300);
        getAcceptButton().setText("login");
        password = new InteractiveJPasswordField("password");
        username = new InteractiveJTextField("username");
        getUpperPanel().add(username);
        getUpperPanel().add(password);
        getUpperPanel().setLayout(new GridLayout(1,2, 200, 200));
        setVisible(true);
    }
}
