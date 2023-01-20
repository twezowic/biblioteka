package app;

import classes.Book;
import classes.Library;
import classes.User;
import lib.Settings;
import panels.*;

import javax.swing.*;
import java.util.Scanner;


public class App {
    // permission level 0 - not logged in
    // permission level 1 - logged in as user
    // permission level 2 - logged as employee
    private int permissionLevel = 0;
    private int userID = 0;
    private String username = "";
    public App(){
        Run();
    }

    /**
     * opens the MainPanel and handles the listeners
     */
    public void Run()
    {
        MainPanel mainPanel = new MainPanel(permissionLevel, username);
        // listeners for panels that are visible for every permission level

        mainPanel.getViewLibrariesInfo().addActionListener(e -> {
            ViewLibInfo();
            mainPanel.dispose();
        });
        mainPanel.login.addActionListener(e -> {
            Login();
            mainPanel.dispose();
        });

        //listeners for panels that are visible on specific permission level
        switch(permissionLevel){
            case 0 ->{
                mainPanel.login.setText("Login");
                mainPanel.getBrowseBooks().addActionListener(e -> {
                    BrowseBooks();
                    mainPanel.dispose();
                });
                mainPanel.getRegisterButton().addActionListener(e -> {
                    Register();
                    mainPanel.dispose();
                });
            }
            case 1 ->{
                mainPanel.login.setText("Logout");
                mainPanel.getBrowseBooks().addActionListener(e -> {
                    BrowseBooks();
                    mainPanel.dispose();
                });

            }
            case 2 -> {
                mainPanel.login.setText("Logout");

                mainPanel.getReturnBook().addActionListener(e -> {
                    ReturnBook();
                    mainPanel.dispose();
                });
                mainPanel.getAddCopy().addActionListener(e->{
                    AddCopy();
                    mainPanel.dispose();
                });
                mainPanel.getRegisterBook().addActionListener(e -> {
                    RegisterBook();
                    mainPanel.dispose();
                });
                mainPanel.getBorrowBooks().addActionListener(e -> {
                    BorowBook();
                    mainPanel.dispose();
                });
            }
        }
    }

    private void AddCopy() {
        AddingCopy addingCopy=new AddingCopy();
        addingCopy.getCancelButton().addActionListener(e -> disposeSubPanel(addingCopy));
        addingCopy.getAcceptButton().addActionListener(e->{

            Settings.getInstance().database.addCopy(addingCopy.ReturnSelectedBookId(),addingCopy.ReturnSelectedLibraryId());
            handleMessagePanel(addingCopy, " Adding a book copy successful");
                }
                );
    }

    private void BrowseBooks() {
        BrowseBookPanel3 browseBookPanel3 = new BrowseBookPanel3(userID);
        browseBookPanel3.getCancelButton().addActionListener(e -> disposeSubPanel(browseBookPanel3));
        browseBookPanel3.getSearchButton().addActionListener(e -> browseBookPanel3.fillBooksInfo());
        browseBookPanel3.getAcceptButton().addActionListener(e -> browseBookPanel3.reserv(username));
        browseBookPanel3.getPayButton().addActionListener(e-> Settings.getInstance().database.payPenalty(userID));
    }

    private void ViewLibInfo() {
        ViewLibInfoPanel viewLibInfo = new ViewLibInfoPanel();
        viewLibInfo.getChooseLibrary().addActionListener(e -> {
            viewLibInfo.getStatusInfo().setText("Status: Waiting for the Data from the database");
            viewLibInfo.getStatusInfo().paintImmediately(viewLibInfo.getStatusInfo().getVisibleRect());
            Library answer = Settings.getInstance().database.getLibraryInfo(viewLibInfo.getChooseLibrary().getSelectedItem().toString());
            viewLibInfo.fillLibraryInfo(answer);
            viewLibInfo.getStatusInfo().setText("Status: Waiting for change");
        });
        viewLibInfo.getCancelButton().addActionListener(e -> disposeSubPanel(viewLibInfo));
    }
    private void Register(){
        RegisterUserPanel registerPanel = new RegisterUserPanel();
        registerPanel.getCancelButton().addActionListener(e -> disposeSubPanel(registerPanel));
        registerPanel.getAcceptButton().addActionListener(e -> {
            User newUser = new User(registerPanel.getPersonsName().getText(), registerPanel.getSurname().getText(),
                    registerPanel.getLogin().getText(), registerPanel.getPassword().getText());
            try {
                Settings.getInstance().database.registerUser(newUser);
            }
            catch (RuntimeException exc)
            {
                handleMessagePanel(registerPanel, "Could not create a new user account!");
            }
            disposeSubPanel(registerPanel);
        });
    }
    private void BorowBook() {
        BorrowBookPanel borrowBookPanel = new BorrowBookPanel();

        borrowBookPanel.getCancelButton().addActionListener(e -> disposeSubPanel(borrowBookPanel));
        borrowBookPanel.getInputUserID().addActionListener(e -> {
            borrowBookPanel.getResultTableModel().setRowCount(0);
            String userData = borrowBookPanel.getInputUserID().getText().trim();
            Scanner sc = new Scanner(userData);
            if(sc.hasNextInt()){
                borrowBookPanel.setSearchDataText(userData);
                borrowBookPanel.getAcceptButton().setEnabled(true);
                borrowBookPanel.fillResultTable(Settings.getInstance().database.getOrders(Integer.parseInt(userData), "Rezerwacja"));
            }
            else{
                try {
                    borrowBookPanel.setSearchDataText(Integer.toString(Settings.getInstance().database.getUserID(userData)));
                    borrowBookPanel.getAcceptButton().setEnabled(true);

                    borrowBookPanel.fillResultTable(Settings.getInstance().database.getOrders(Settings.getInstance().database.getUserID(userData), "Rezerwacja"));
                }
                catch (RuntimeException exc ) {
                    handleMessagePanel(borrowBookPanel, "The Username not found");
                }
            }
        });
        borrowBookPanel.getAcceptButton().addActionListener(e -> {
            Settings.getInstance().database.borrowBook(Integer.valueOf(borrowBookPanel.getResultTable().getValueAt(borrowBookPanel.getResultTable().getSelectedRow(),0).toString()));
            handleMessagePanel(borrowBookPanel,"Successful borrowed book");
        });

    }

    private void ReturnBook() {
        ReturnBooksPanel returnBookPanel = new ReturnBooksPanel();
        returnBookPanel.getCancelButton().addActionListener(e -> disposeSubPanel(returnBookPanel));
        returnBookPanel.getInputUserID().addActionListener(e -> {
            returnBookPanel.getResultTableModel().setRowCount(0);
            String userID = returnBookPanel.getInputUserID().getText().trim();
            Scanner sc = new Scanner(userID);
            if(sc.hasNextInt()){
                returnBookPanel.setSearchDataText(userID);
                returnBookPanel.getAcceptButton().setEnabled(true);
                returnBookPanel.fillResultTable(Settings.getInstance().database.getOrders(Integer.parseInt(userID), "Wypozyczona"));
            }
            else{
                returnBookPanel.getAcceptButton().setEnabled(false);
                handleMessagePanel(returnBookPanel,"The User ID has to be a number");
            }
        });
        returnBookPanel.getAcceptButton().addActionListener(e -> {
            int selectedRow = returnBookPanel.getResultTable().getSelectedRow();
            int orderID = (Integer)returnBookPanel.getResultTableModel().getValueAt(selectedRow, returnBookPanel.getResultTable().getColumn("OrderID").getModelIndex());

            finalizeReturnBook(returnBookPanel, orderID, selectedRow);
        });

    }

    /**
     * @param returnBookPanel panel who called this function
     * @param orderID
     * @param selectedRow row to remove from JTable
     *                    Handles the choosePenaltyPanel, calls the database method to return the book and removes
     *                    one row from Jtable
     */
    private void finalizeReturnBook(ReturnBooksPanel returnBookPanel, int orderID, int selectedRow)
    {
        returnBookPanel.setEnabled(false);
        ChoosePenaltyPanel choosePenaltyPanel = new ChoosePenaltyPanel(Settings.getInstance().database.getPenalties());
        choosePenaltyPanel.getCancelButton().addActionListener(e -> {
            choosePenaltyPanel.dispose();
            returnBookPanel.setEnabled(true);
        });
        choosePenaltyPanel.getAcceptButton().addActionListener(e -> {// will not add any penalties to the user
            returnBookPanel.getResultTableModel().removeRow(returnBookPanel.getResultTable().getSelectedRow());
            Settings.getInstance().database.returnBook(orderID, 0);
            if(returnBookPanel.getResultTableModel().getRowCount() == 0)
            {
                returnBookPanel.getAcceptButton().setEnabled(false);
            }
            choosePenaltyPanel.dispose();
            returnBookPanel.setEnabled(true);
        });
        choosePenaltyPanel.getAddPenaltyButton().addActionListener(e -> { // will add penalty to the user
            returnBookPanel.getResultTableModel().removeRow(returnBookPanel.getResultTable().getSelectedRow());
            int chosenPenaltyID = choosePenaltyPanel.getPenaltyIDVec().elementAt(
                    choosePenaltyPanel.getPenaltyBox().getSelectedIndex());
            Settings.getInstance().database.returnBook(orderID, chosenPenaltyID);
            returnBookPanel.getResultTableModel().removeRow(selectedRow);
            if(returnBookPanel.getResultTableModel().getRowCount() == 0)
            {
                returnBookPanel.getAcceptButton().setEnabled(false);
            }
            choosePenaltyPanel.dispose();
            returnBookPanel.setEnabled(true);
        });
    }
    private void RegisterBook() {
        RegisterBookPanel registerBookPanel = new RegisterBookPanel();
        registerBookPanel.getCancelButton().addActionListener(e -> disposeSubPanel(registerBookPanel));
        registerBookPanel.getAcceptButton().addActionListener(e -> {

            Book book =new Book(0,
                    registerBookPanel.getBookTitleInput().getText(),
                    registerBookPanel.getBookAuthorInput().getText(),
                    Integer.valueOf(registerBookPanel.getBookPagesInput().getText()),
                    registerBookPanel.getBookISBNInput().getText(),
                    Integer.valueOf(registerBookPanel.getBookYearInput().getText()),
                    registerBookPanel.getBookGenreInput().getText());
            Settings.getInstance().database.addBook(book);
            disposeSubPanel(registerBookPanel);

        });
    }

    /**
     *  gets information from database if login was successful.
     */
    private void Login() {
        if (permissionLevel == 0) {
            LoginPanel loginPanel = new LoginPanel();
            loginPanel.getAcceptButton().addActionListener(e -> {
                int [] data = Settings.getInstance().database.validateLoginData(loginPanel.getUsername().getText(), loginPanel.getPassword().getPassword());
                switch (data[0])
                {
                    case 0 -> {// show window couldn't log in
                        permissionLevel = 0;
                        handleMessagePanel(loginPanel, "Login failed: invalid data");
                    }
                    case 1 -> {
                        username = loginPanel.getUsername().getText();
                        userID = data[1];
                        permissionLevel = 1;
                        disposeSubPanel(loginPanel);

                    }
                    case 2 -> {
                        username = loginPanel.getUsername().getText();
                        permissionLevel = 2;
                        disposeSubPanel(loginPanel);
                    }
                }
            });
            loginPanel.getCancelButton().addActionListener(e -> disposeSubPanel(loginPanel));
        }
        else {
            permissionLevel=0;
            userID=0;
            Run();
        }
    }

    /**
     * @param frameToDispose can only be used if you want to open MainPanel after closing your current one.
     */
    private void disposeSubPanel(JFrame frameToDispose)
    {
        frameToDispose.dispose();
        Run();
    }


    /**
     * @param args can take one of the following arguments:<p>
     *             res - Creates or replace old database with tables and data<p>
     *             con - Takes 3 additional arguments with information about url, username and password to change database to which app connects to
     */
    public static void main(String[] args){
        if(args.length > 0)
        {
            if (args[0].equals("res"))
            {
                Settings.getInstance().database = new DatabaseBuilder().build();
                Settings.getInstance().database.initializeData();
            }
            else if (args[0].equals("con"))
            {
                Settings.getInstance().database = new DatabaseBuilder().setDBURL(args[1]).setDBUSERNAME(args[2]).setDBPASSWORD(args[3]).build();
                Settings.getInstance().database.initializeData();
            }
        }

        else {
            Settings.getInstance().database = new DatabaseBuilder().build();
        }
        try {

            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        }
        catch (Exception e){
            // will use the default "Metal" Look and Feel instead
        }
        new App();
    }
    private void handleMessagePanel(JFrame callingPanel, String textToShow)
    {
        callingPanel.setEnabled(false);
        MessagePanel notNumber = new MessagePanel(textToShow);
        notNumber.getAcceptButton().addActionListener(f -> {
            notNumber.dispose();
            callingPanel.setEnabled(true);});
    }

}