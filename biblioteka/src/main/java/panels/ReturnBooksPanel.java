package panels;

import lib.BasePanel;
import lib.InteractiveJTextField;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ReturnBooksPanel extends BasePanel {
    private JTextField searchData;
    private InteractiveJTextField inputUsername;
    public ReturnBooksPanel() {
        inputUsername = new InteractiveJTextField("Type the username of the person, whose books you want to return");
        searchData = new JTextField();
        JTable resultTable = new JTable();
        inputUsername.setPreferredSize(new Dimension(100, 100));
        searchData.setPreferredSize(new Dimension(100, 100));
        resultTable.setPreferredSize(new Dimension(500, 500));
        searchData.setEditable(false);

        getUpperPanel().add(inputUsername);
        getUpperPanel().add(searchData);
        getUpperPanel().add(resultTable);
        getUpperPanel().setLayout(new BoxLayout(getUpperPanel(),BoxLayout.Y_AXIS));
        setVisible(true);

        inputUsername.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setSearchDataText(inputUsername.getText());
            }
        });
    }
    private void setSearchDataText(String username)
    {
        searchData.setText("searching for user: " + username);
    }
}
