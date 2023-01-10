package panels;

import lib.Settings;
import lombok.Getter;

import javax.swing.*;
import java.awt.*;
import java.text.MessageFormat;

@Getter
public class MainPanel extends JFrame
{
    private int permissionLevel;
    public JButton login;
    public JButton browseBooks;
    public JButton viewLibrariesInfo;
    public JButton returnBook;
    public JButton registerBook;
    public JButton reserveBook;
    private String username;
    private JButton registerButton;
    public MainPanel(int permissionLevel, String username)
    {
        this.permissionLevel = permissionLevel;
        this.username = username;

        setPreferredSize(Settings.getInstance().BIG_WINDOW_PREFERRED_SIZE);
        setMinimumSize(Settings.getInstance().BIG_WINDOW_MIN_SIZE);
        setLocation(Settings.getInstance().BIG_WINDOW_LOCATION_X, Settings.getInstance().BIG_WINDOW_LOCATION_Y);

        JSplitPane splitPane = new JSplitPane();
        JPanel centralPanel = new JPanel();
        JPanel loginPanel = new JPanel();
        JLabel loginLabel = new JLabel();
        registerButton = new JButton("register");

        login = new JButton("login");
        browseBooks = new JButton("Browse Books");
        viewLibrariesInfo = new JButton("View information about a specific library");

        //login.setBorder(BorderFactory.createLineBorder(Color.WHITE, 20));
        loginPanel.add(loginLabel);
        switch (permissionLevel) {
            case 0 -> {
                loginPanel.add(Box.createRigidArea(new Dimension(5,0)));
                loginLabel.setText("Currently not logged in");
                loginPanel.add(registerButton);
                loginPanel.setLayout(new GridLayout(1,3, 50, 50));
                login.setBackground(Color.YELLOW);
                centralPanel.setLayout(new GridLayout(2, 1, 100, 100));
            }
            case 1 -> {
                loginLabel.setText("Currently logged in as: " + username);
                login.setEnabled(true);
                centralPanel.setLayout(new GridLayout(3, 1,100,100));
                loginPanel.setLayout(new GridLayout(1, 2, 300, 100));
            }
            case 2 -> {
                loginLabel.setText(MessageFormat.format("<html>Permission level - Employee. <br>Currently logged in as: {0}</html>", username));
                login.setEnabled(true);
                returnBook = new JButton("Return a specific book to the database");
                registerBook = new JButton("register a new book");
                centralPanel.add(returnBook); centralPanel.add(registerBook);
                centralPanel.setLayout(new GridLayout(4, 1, 100, 100));
                loginPanel.setLayout(new GridLayout(1, 2, 300, 100));
            }
        }
        loginPanel.add(login);
        centralPanel.add(browseBooks); centralPanel.add(viewLibrariesInfo);

        splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
        splitPane.setDividerLocation(100);
        splitPane.setTopComponent(loginPanel);
        splitPane.setBottomComponent(centralPanel);
        add(splitPane);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Library");
        setVisible(true);
    }
}
