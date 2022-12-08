package panels;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import lombok.Getter;

import java.awt.*;
import java.awt.event.*;

@Getter
public class BrowseBooksPanel2 extends JFrame{
    private JPanel panel1;
    private JTextField autorinput;
    private JTextArea autor1;
    private JTextField titlein;
    private JButton cancelButton;
    private JButton reservButton;
    private JComboBox gatunek_selected;
    private JTextArea gagtunek;
    private JTable table1;
    private JTextArea pole;
    private JTextArea titlevidget;

    private JSpinner spinner1;
    private JTextArea libin;
    private JTextField libreryin;
    private String gatunekstr="any";
private String autorstr="";
private String titlestr="";
private String Libreryid="0";
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
        titlein.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                titlestr= titlein.getText();
                setpole();
            }
        });




        spinner1.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                Libreryid=spinner1.getValue().toString();
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
            if (titlestr.compareTo("")!=0)
            {
                text= text+" searching title: "+titlestr;
            }
            if (Libreryid.compareTo("0")!=0)
            {
                text= text+" librery id : "+Libreryid;
            }
            pole.setText(text);


    }


   /* private void createUIComponents() {
        DefaultTableModel model =new DefaultTableModel();
        JTable table1 =new JTable(model);
        panel1.add(table1);

    }*/
}