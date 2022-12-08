package panels;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import lombok.Getter;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
@Getter
public class BrowseBooksPanel2 extends JFrame{
    private JPanel panel1;
    private JTextField autorinput;
    private JTextArea autor1;

    private JButton cancelButton;
    private JButton reservButton;
    private JComboBox gatunek_selected;
    private JTextArea gagtunek;
    private JTable table1;
    private JTextArea pole;
private String gatunekstr="any";
private String autorstr="";
    public BrowseBooksPanel2(int PermisionLevel){

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

        gatunek_selected.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gatunekstr=gatunek_selected.getSelectedItem().toString();
                setpole();
            }
        });

        autorinput.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                autorstr=autorinput.getText();
                setpole();
            }
        });
    }

    private  void setpole() {

            String text="your search :  ";
            if (gatunekstr.compareTo("any")!=0)
            {
                text= text+" searching genres: "+gatunekstr;
            }
            if (autorstr.compareTo("")!=0)
        {
            text= text+" searching autor: "+autorstr;
        }
            pole.setText(text);


    }


   /* private void createUIComponents() {
        DefaultTableModel model =new DefaultTableModel();
        JTable table1 =new JTable(model);
        panel1.add(table1);

    }*/
}