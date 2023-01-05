package panels;

import lib.BasePanel;

import javax.swing.*;
import java.awt.*;

public class MessagePanel extends BasePanel {
    public MessagePanel(String message){
        setBounds(500, 200, 450, 300);
        getBottomPanel().remove(getCancelButton());
        JTextField messageField = new JTextField();
        messageField.setEditable(false);
        //messageField.setLocation(50, 200);
        //messageField.setHorizontalAlignment(JTextField.CENTER);
        messageField.setFont(new Font("SansSerif",0, 30));
        messageField.setText(message);
        getUpperPanel().add(messageField);
        setVisible(true);
    }
}
