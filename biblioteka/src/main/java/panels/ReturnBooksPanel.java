package panels;

import classes.Order;
import classes.Book;
import lib.BasePanel;
import lib.InteractiveJTextField;
import lombok.Getter;
import lombok.SneakyThrows;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Vector;

@Getter
public class ReturnBooksPanel extends BasePanel {
    private JTextField searchData;
    private InteractiveJTextField inputUserID;
    private JTable resultTable;
    private DefaultTableModel resultTableModel;
    private Vector<Integer> orderIdVec;
    public ReturnBooksPanel() {
        inputUserID = new InteractiveJTextField("Type the user ID of the person, whose books you want to return, only numbers allowed!");
        searchData = new JTextField();
        orderIdVec = new Vector<>();
        resultTableModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        resultTable = new JTable(resultTableModel);
        inputUserID.setPreferredSize(new Dimension(100, 100));
        searchData.setPreferredSize(new Dimension(100, 100));
        //resultTable.setPreferredSize(new Dimension(500, 500));
        searchData.setEditable(false);
        getAcceptButton().setEnabled(false);
        getAcceptButton().setText("Set the selected book as returned to the library");
        resultTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        fillResultTableColumnNames();

        //
        JScrollPane scrollPane = new JScrollPane(resultTable);
        scrollPane.setPreferredSize(new Dimension(500, 500));

        getUpperPanel().add(inputUserID);
        getUpperPanel().add(searchData);
        //getUpperPanel().add(resultTable);
        getUpperPanel().add(scrollPane);

        getUpperPanel().setLayout(new BoxLayout(getUpperPanel(),BoxLayout.Y_AXIS));
        setVisible(true);


    }


    public void fillResultTable(ArrayList<Order> usersOrders) {
        //Class c = Order.class;
        //Field[] fields = c.getDeclaredFields();
        for (Order order: usersOrders)
        {
            //fields[0].get(order);
            orderIdVec.add(order.getOrderID());
            resultTableModel.addRow(new Object[]{order.getStatus(), order.getDateBorrow(), order.getDateReturn(), order.getBookTitle()});
        }
    }

    public void setSearchDataText(String username)
    {
        searchData.setText("your typed user ID: " + username);
    }

    private void fillResultTableColumnNames()
    {
        Field[] fields = Order.class.getDeclaredFields();
        for(Field field: fields)
        {
            resultTableModel.addColumn(field.getName());
        }
    }
}
