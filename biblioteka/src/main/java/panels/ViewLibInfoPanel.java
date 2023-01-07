package panels;

import classes.Library;
import lib.BasePanel;
import lib.InteractiveJTextField;
import lombok.Getter;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

@Getter
public class ViewLibInfoPanel extends BasePanel {

    private InteractiveJTextField libraryName;
    private JTable libraryInfo;
    private DefaultTableModel LibraryInfoTableModel;
    public ViewLibInfoPanel(){
        libraryName = new InteractiveJTextField("Type the name of the library you wish to search for");
        LibraryInfoTableModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        libraryInfo = new JTable(LibraryInfoTableModel);
        libraryName.setPreferredSize(new Dimension(100, 100));
        libraryInfo.setPreferredSize(new Dimension(500, 500));

        getAcceptButton().setText("View Library Data");
        getUpperPanel().setLayout(new BoxLayout(getUpperPanel(),BoxLayout.Y_AXIS));
        getUpperPanel().add(libraryName);
        getUpperPanel().add(libraryInfo);
        setVisible(true);
        getLibraryInfoTableModel().setColumnCount(2);
    }

    public void fillLibraryInfo(Library library)
    {
        getLibraryInfoTableModel().addRow(new Object[] {"Library name: ", "tak"});


        getLibraryInfoTableModel().addRow(new Object[] {"Library name: ", library.getName()});
        getLibraryInfoTableModel().addRow(new Object[] {"Street: ", library.getStreet()});
        getLibraryInfoTableModel().addRow(new Object[] {"City: ", library.getCity()});
        getLibraryInfoTableModel().addRow(new Object[] {"Phone: ", library.getPhone()});
        getLibraryInfoTableModel().addRow(new Object[] {"Work Times:"});

        for (int i = 0; i < library.getWorkTimes().getOpening().size(); i++) {
            getLibraryInfoTableModel().addRow(new Object[] {library.getWorkTimes().getOpening().get(i),
                    library.getWorkTimes().getClosing().get(i)});
        }
    }
}
