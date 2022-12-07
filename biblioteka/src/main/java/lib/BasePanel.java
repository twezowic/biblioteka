package lib;

import lombok.Getter;

import javax.swing.*;
import java.awt.*;
@Getter
public class BasePanel extends JFrame {
    private JButton cancelButton;
    private JButton acceptButton;
    private JPanel bottomPanel;
    private JPanel upperPanel;

    private JSplitPane defaultSplitPane;

    public BasePanel(){
        cancelButton = new JButton("Cancel");
        acceptButton = new JButton("Accept");
        bottomPanel = new JPanel();
        upperPanel = new JPanel();
        defaultSplitPane = new JSplitPane();
        bottomPanel.add(cancelButton);
        bottomPanel.add(acceptButton);
        bottomPanel.setLayout(new GridLayout(1,2, 50, 50));
        acceptButton.setBounds(100, 100, 400, 400);
        cancelButton.setBounds(200, 200, 400, 400);
        defaultSplitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
        defaultSplitPane.setDividerSize(100);
        defaultSplitPane.setBottomComponent(bottomPanel);
        defaultSplitPane.setTopComponent(upperPanel);
        add(defaultSplitPane);
        setSize(new Dimension(600,600));
        setBounds(400, 50, 800, 800);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }
}
