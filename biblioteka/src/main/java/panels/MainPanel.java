package panels;

import lombok.Getter;

import javax.swing.*;
import java.awt.*;
import java.text.MessageFormat;

@Getter
public class MainPanel
{
    public JFrame frame;
    private int permissionLevel;
    public JButton login;
    public JButton browseBooks;
    public JButton checkOutBook;
    public JButton returnBook;
    public JButton registerBook;
    public JButton reserveBook;
    private String username;
    public MainPanel(int permissionLevel, String username)
    {
        this.permissionLevel = permissionLevel;
        this.username = username;

        JSplitPane splitPane = new JSplitPane();
        frame = new JFrame();
        JPanel centralPanel = new JPanel();
        JPanel loginPanel = new JPanel();
        frame.setBounds(400, 50, 800, 800);
        JLabel loginLabel = new JLabel();

        login = new JButton("login");
        browseBooks = new JButton("Browse Books");


        login.setBounds(0,0,10,10);
        login.setSize(new Dimension(10,10));
        login.setBorder(BorderFactory.createLineBorder(Color.WHITE, 20));
        switch (permissionLevel) {
            case 0 -> {
                loginLabel.setText("Currently not logged in");
                login.setBackground(Color.YELLOW);
            }
            case 1 -> {
                loginLabel.setText("Currently logged in as: " + username);
                login.setEnabled(false);
                reserveBook = new JButton("reserve a book");
                centralPanel.add(reserveBook);
                centralPanel.setLayout(new GridLayout(2, 1, 100, 100));
            }
            case 2 -> {
                loginLabel.setText(MessageFormat.format("<html>Permission level - Employee. <br>Currently logged in as: {0}</html>", username));
                login.setEnabled(false);
                checkOutBook = new JButton("Check out a book to a user");
                returnBook = new JButton("Return a specific book to the database");
                registerBook = new JButton("register a new book");
                centralPanel.add(checkOutBook); centralPanel.add(returnBook); centralPanel.add(registerBook);
                centralPanel.setLayout(new GridLayout(4, 1, 100, 100));
            }
        }
        loginPanel.add(loginLabel); loginPanel.add(login);
        centralPanel.add(browseBooks);

        splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
        splitPane.setDividerLocation(100);
        splitPane.setTopComponent(loginPanel);
        splitPane.setBottomComponent(centralPanel);
        frame.add(splitPane);
        loginPanel.setLayout(new GridLayout(1, 2, 300, 100));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Library");
        frame.setVisible(true);
    }
}
