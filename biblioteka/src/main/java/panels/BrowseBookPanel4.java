package panels;

import classes.Order;
import lib.BasePanel;
import lib.InteractiveJTextField;
import lib.Settings;
import lombok.Getter;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Vector;
@Getter
public class BrowseBookPanel4 extends BasePanel{

        private JTextField searchData;
        private InteractiveJTextField inputUserID;
        private JTable resultTable;
        private DefaultTableModel resultTableModel;
        private Vector<Method> orderGetterMethods;
        public BrowseBookPanel4() {



            setPreferredSize(Settings.getInstance().BIG_WINDOW_PREFERRED_SIZE);
            setMinimumSize(Settings.getInstance().BIG_WINDOW_MIN_SIZE);
            setLocation(Settings.getInstance().BIG_WINDOW_LOCATION_X, Settings.getInstance().BIG_WINDOW_LOCATION_Y);

            inputUserID = new InteractiveJTextField("Type the user ID of the person, who came, only numbers allowed!");
            searchData = new JTextField();
            resultTableModel = new DefaultTableModel() {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };
            resultTable = new JTable(resultTableModel);
            resultTable.getTableHeader().setReorderingAllowed(false);
            inputUserID.setPreferredSize(new Dimension(100, 100));
            searchData.setPreferredSize(new Dimension(100, 100));
            searchData.setEditable(false);
            getAcceptButton().setEnabled(false);
            getAcceptButton().setText("Borrow");
            resultTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            fillResultTableColumnNames();

            JScrollPane scrollPane = new JScrollPane(resultTable);
            scrollPane.setPreferredSize(new Dimension(500, 500));

            getUpperPanel().add(inputUserID);
            getUpperPanel().add(searchData);
            getUpperPanel().add(scrollPane);

            getUpperPanel().setLayout(new BoxLayout(getUpperPanel(),BoxLayout.Y_AXIS));
            setVisible(true);


        }


        public void fillResultTable(ArrayList<Order> usersOrders) {

            for (Order order: usersOrders)
            {
                Vector row = new Vector();
                for (Method getter : orderGetterMethods)
                {
                    try {
                        row.add(getter.invoke(order));

                    }
                    catch (IllegalAccessException e)  {}
                    catch (InvocationTargetException e) {
                        row.add("");
                    }
                }
                resultTableModel.addRow(row);
                //resultTableModel.addRow(new Object[]{order.getStatus(), order.getDateBorrow(), order.getDateReturn(), order.getBookTitle()});
            }
        }

        public void setSearchDataText(String username)
        {
            searchData.setText("your typed user ID: " + username);
        }

        private void fillResultTableColumnNames()
        {
            Field[] fields = Order.class.getDeclaredFields();
            orderGetterMethods = new Vector();
            for(Field field: fields)
            {
                resultTableModel.addColumn(field.getName());
                try
                {
                    orderGetterMethods.add(Order.class.getDeclaredMethod("get" + field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1)));
                }
                catch(NoSuchMethodException e) {}
            }
        }
    }

