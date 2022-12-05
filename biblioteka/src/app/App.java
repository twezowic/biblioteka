package app;
import panels.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class App {
    boolean loggedIn = false;
    int permissionLevel = 0;
    int userId = 0;
    public App(){
        Run();
    }
    public void Run()
    {
        MainPanel mainPanel = new MainPanel(loggedIn);
        if(!loggedIn) mainPanel.login.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Login();
                mainPanel.frame.dispose();
            }
        });
    }

    private void Login() {
        LoginPanel loginPanel = new LoginPanel();
        loginPanel.loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(Database.ValidateLoginData(loginPanel.nick.getText(), loginPanel.password.getPassword()))
                {
                    loginPanel.frame.dispose();
                    loggedIn = true;
                    Run();
                }
            }
        });
        loginPanel.cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loginPanel.frame.dispose();
                Run();
            }
        });
    }
    public static void main(String[] args) {
        new App();
    }


}
class Database{ //temporary
    public static boolean ValidateLoginData(String nick, char[] password) {
        return true;
    }
}