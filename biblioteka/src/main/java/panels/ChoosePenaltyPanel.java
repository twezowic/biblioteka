package panels;

import classes.Penalty;
import lib.BasePanel;
import lombok.Getter;

import javax.swing.*;
import javax.swing.plaf.basic.BasicComboBoxRenderer;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.stream.Collectors;
@Getter

public class ChoosePenaltyPanel extends BasePanel {

    JComboBox penaltyBox;
    Vector<String> descriptionVec;
    JButton addPenaltyButton;
    Vector<Integer> penaltyIDVec;
    public ChoosePenaltyPanel(ArrayList<Penalty> penalties)
    {
        //JTextField explanationText = new JTextField("Choose if a user should receive a penalty while returning this book");
        //explanationText.setEditable(false);
        addPenaltyButton = new JButton("Add the penalty to the user");
        //explanationText.setPreferredSize(new Dimension(100, 100));
        getAcceptButton().setText("Return the book without any penalty");
        getBottomPanel().add(addPenaltyButton);
        //getBottomPanel().setLayout(new GridLayout(1,3,100, 100));
        Vector<String> namesVector = new Vector<String>();
        penaltyIDVec = new Vector<>();
        descriptionVec = new Vector<>();

        for(Penalty penalty: penalties)
        {
            penaltyIDVec.add(penalty.getPenaltyID());
            namesVector.add(penalty.getName());
            descriptionVec.add(penalty.getDescription());
        }
        penaltyBox = new JComboBox(namesVector);
        penaltyBox.setToolTipText("choose if a user should receive a penalty");
        penaltyBox.setRenderer(new ToolTipComboBoxRenderer());
        //penaltyBox.setPreferredSize(new Dimension(500, 500));
        //getUpperPanel().setLayout(new GridLayout(2,1,100,100));
        //getUpperPanel().setLayout(new BoxLayout(getUpperPanel(),BoxLayout.Y_AXIS));
        //getUpperPanel().add(explanationText);
        getUpperPanel().add(penaltyBox);
        setVisible(true);

    }

    class ToolTipComboBoxRenderer extends BasicComboBoxRenderer {
        @Override
        public Component getListCellRendererComponent(JList list, Object value,
                                                      int index, boolean isSelected, boolean cellHasFocus) {

            if (isSelected) {
                setBackground(list.getSelectionBackground());
                setForeground(list.getSelectionForeground());
                if (-1 < index) {

                    list.setToolTipText(descriptionVec.elementAt(index));
                }
            } else {
                setBackground(list.getBackground());
                setForeground(list.getForeground());
            }
            setFont(list.getFont());
            setText((value == null) ? "" : value.toString());
            return this;
        }
    }
}
