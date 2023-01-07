package panels;

import classes.Penalty;
import lib.BasePanel;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ChoosePenaltyPanel extends BasePanel {

    JComboBox penaltyBox;
    public ChoosePenaltyPanel(ArrayList<Penalty> penalties)
    {
        List a = penalties.stream().map(Penalty::getName).collect(Collectors.toList());
        //penaltyBox = new JComboBox();
        //penaltyBox.setToolTipText();
        String aga = "asdf";
    }
}
