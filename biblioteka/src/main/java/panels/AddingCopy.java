package panels;


import app.Database;
import classes.Book;

import lib.BasePanel;

import javax.swing.*;


import java.util.ArrayList;


import static javax.swing.JSplitPane.HORIZONTAL_SPLIT;

public class AddingCopy extends BasePanel {
    private JComboBox<String> chooseLibrary;
    private JComboBox<String> chooseBook;
    private ArrayList<Book> a;
    /**
     * create panel
     */
    public AddingCopy() {

        chooseLibrary = new JComboBox<>(Database.getLibrariesNames());
        JSplitPane upperSplitPane5 = new JSplitPane();
        upperSplitPane5.setResizeWeight(0.5);
        upperSplitPane5.setOrientation(HORIZONTAL_SPLIT);
        upperSplitPane5.setRightComponent(chooseLibrary);

        upperSplitPane5.setEnabled(false);

        a=Database.getBooks("","","","");
        ArrayList<String> b=new ArrayList<String>();
        for (int i=0;i<a.size();i++)
        {
            b.add(a.get(i).getTitle());
        }
        chooseBook= new JComboBox<>(b.toArray(new String[b.size()]));
        upperSplitPane5.setLeftComponent(chooseBook);

        getUpperPanel().add(upperSplitPane5);

        setVisible(true);
    }
    /*
    * @return Selected book id
    * */

    public int ReturnSelectedBookId()
    {
        for (int i=0;i<a.size();i++)
        {
            if (chooseBook.getSelectedItem().equals(a.get(i).getTitle()))
            {
                return a.get(i).getBookID();
            }
        }
        return -1;
    }
    /*
     * @return Selected library id
     * */
    public int ReturnSelectedLibraryId() {
        return  Database.getLibraryInfo(chooseLibrary.getSelectedItem().toString()).getLibraryID();
           }
}
