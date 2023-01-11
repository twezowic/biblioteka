package panels;


import app.Database;
import classes.Book;
import classes.Order;
import lib.BasePanel;

import javax.swing.*;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Vector;

import static javax.swing.JSplitPane.HORIZONTAL_SPLIT;

public class AddingCopy extends BasePanel {
    private JComboBox<String> chooseLibrary;
    private JComboBox<String> chooseBook;
    public AddingCopy() {
        chooseLibrary = new JComboBox<>(Database.getLibrariesNames());
        JSplitPane upperSplitPane5 = new JSplitPane();
        upperSplitPane5.setResizeWeight(0.5);
        upperSplitPane5.setOrientation(HORIZONTAL_SPLIT);
        upperSplitPane5.setRightComponent(chooseLibrary);

        upperSplitPane5.setEnabled(false);
        Field[] fields = Order.class.getDeclaredFields();
        ArrayList<Book> a=Database.getBooks("","","","");
        ArrayList<String> b=new ArrayList<String>();
        for (int i=0;i<a.size();i++)
        {
            b.add(a.get(i).getTitle());
        }
        chooseBook= new JComboBox<>(b.toArray(new String[Database.getGenres().size()]));
        upperSplitPane5.setLeftComponent(chooseBook);

        getUpperPanel().add(upperSplitPane5);

        setVisible(true);
    }
}

