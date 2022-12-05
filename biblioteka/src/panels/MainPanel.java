package panels;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MainPanel
{
    public JFrame frame;
    private boolean loggedIn;
    public JButton login;
    public JButton browseBooks;
    public MainPanel(boolean isLoggedIn)
    {
        this.loggedIn = isLoggedIn;

        //JFrame.setDefaultLookAndFeelDecorated(true);
        frame = new JFrame();
        JPanel panel = new JPanel();
        frame.setBounds(600, 600, 800, 800);
        login = new JButton("login");
        browseBooks = new JButton("Browse Books");
        login.setBounds(0,0,10,10);
        login.setSize(new Dimension(10,10));
        //login.setPreferredSize(new Dimension(100, 100));
        //frame.setLocationRelativeTo(null);
        login.setBorder(BorderFactory.createLineBorder(Color.WHITE, 20));
        if (loggedIn) login.setEnabled(false);
        else login.setBackground(Color.YELLOW);
        panel.add(login); panel.add(browseBooks);
        frame.add(panel);
        panel.setLayout(new GridLayout(2, 1, 100, 100));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Library");
        frame.setVisible(true);
    }

}
