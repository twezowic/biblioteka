package panels;

import lib.BasePanel;

import javax.swing.*;
import java.awt.*;

public class MessagePanel extends BasePanel {
    public MessagePanel(String message){
        getBottomPanel().remove(getCancelButton());
        JLabel messageField = new JLabel();
        //messageField.setVerticalAlignment(SwingConstants.CENTER);
        messageField.setBorder(BorderFactory.createEmptyBorder(50,0,0,0));
        //messageField.setEditable(false);
        messageField.setFont(new Font("SansSerif",0, 20));
        messageField.setText(message);
        getUpperPanel().add(messageField);
        setVisible(true);
    }
}
