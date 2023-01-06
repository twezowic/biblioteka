package panels;

import classes.Book;
import lib.BasePanel;
import lib.InteractiveJTextField;
import lombok.Getter;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

@Getter
public class ReturnBooksPanel extends BasePanel {
    private JTextField searchData;
    private InteractiveJTextField inputUsername;
    private JTable resultTable;
    public ReturnBooksPanel() {
        inputUsername = new InteractiveJTextField("Type the username of the person, whose books you want to return");
        searchData = new JTextField();
        DefaultTableModel model = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable resultTable = new JTable(model);
        inputUsername.setPreferredSize(new Dimension(100, 100));
        searchData.setPreferredSize(new Dimension(100, 100));
        //resultTable.setPreferredSize(new Dimension(500, 500));
        searchData.setEditable(false);

        //DefaultTableModel model = (DefaultTableModel) resultTable.getModel();
        model.addColumn("title");
        model.addColumn("author");
        model.addColumn("pages");
        model.addColumn("ISBN");
        model.addColumn("year");
        model.addColumn("genre");

        model.addRow(new Object[]{"Column 1", "column2", "column3"});
        model.addRow(new Object[]{"aaa", "aaa", "aaa"});
        model.addRow(new Object[]{"bbb", "bbb", "bbb"});
        //
        resultTable.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
            public void valueChanged(ListSelectionEvent event) {
                if (!event.getValueIsAdjusting()) {//This line prevents double events
                    System.out.println(resultTable.getValueAt(resultTable.getSelectedRow(), 0).toString());

                }
                // do some actions here, for example
                // print first column value from selected row
                }
        });
        //
        JScrollPane scrollPane = new JScrollPane(resultTable);
        scrollPane.setPreferredSize(new Dimension(500, 500));

        getUpperPanel().add(inputUsername);
        getUpperPanel().add(searchData);
        //getUpperPanel().add(resultTable);
        getUpperPanel().add(scrollPane);

        getUpperPanel().setLayout(new BoxLayout(getUpperPanel(),BoxLayout.Y_AXIS));
        setVisible(true);


    }

    public void fillResultTable(ArrayList<Book> usersBooks) {
        for (Book book: usersBooks)
        {

        }
    }

    public void setSearchDataText(String username)
    {
        searchData.setText("searching for user: " + username);
    }
}
