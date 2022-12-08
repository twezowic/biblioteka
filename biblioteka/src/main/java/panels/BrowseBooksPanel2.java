package panels;

import javax.swing.*;

import lombok.Getter;

import javax.swing.table.TableColumn;
import java.awt.*;

@Getter
public class BrowseBooksPanel2 extends JFrame{
    private JPanel panel1;
    private JTextField autorinput;
    private JTextArea autor1;

private JTable table1;
private JButton cancelButton;
    private JButton reservButton;
    private JComboBox gatunek_selected;
    private JTextArea gagtunek;

    public BrowseBooksPanel2(int PermisionLevel){

        TableColumn first = new TableColumn();
        first.setHeaderValue("tytuł");
        table1.addColumn(first);
        TableColumn second = new TableColumn();
        second.setHeaderValue("autor");
        table1.addColumn(second);
        TableColumn thirt = new TableColumn();
        thirt.setHeaderValue("gatunek");
        table1.addColumn(thirt);
        TableColumn fourth = new TableColumn();
        fourth.setHeaderValue("filia");
        table1.addColumn(fourth);
        add(panel1);
        if (PermisionLevel==1) {
            reservButton.setVisible(true);
        }
        else
        {
            reservButton.setVisible(false);
        }
        setVisible(true);
        setSize(new Dimension(600,600));
        setBounds(400, 50, 800, 800);
    }
}
