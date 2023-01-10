package app;

import classes.Book;
import classes.Library;
import panels.*;

import javax.swing.*;
import java.util.Scanner;

import static app.Database.addBook;
import static app.Database.payPenalty;

public class App {
    // permission level 0 - not logged in
    // permission level 1 - logged in as user
    // permission level 2 - logged as employee
    private int permissionLevel = 0;
    private int userId = 0;
    private String username = "";

    public App(){
        Run();
    }
    public void Run()
    {
        MainPanel mainPanel = new MainPanel(permissionLevel, username);
        mainPanel.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // panels for every permission level

        mainPanel.getViewLibrariesInfo().addActionListener(e -> {
            ViewLibInfo();
            mainPanel.frame.dispose();
        });
        mainPanel.login.addActionListener(e -> {
            Login();
            mainPanel.frame.dispose();
        });
        //
        switch(permissionLevel){
            case 0 ->{
                mainPanel.login.setText("Login");
                mainPanel.getBrowseBooks().addActionListener(e -> {
                    BrowseBooks();
                    mainPanel.frame.dispose();
                });
            }
            case 1 ->{
                mainPanel.login.setText("Logout");
                mainPanel.getBrowseBooks().addActionListener(e -> {
                    BrowseBooks();
                    mainPanel.frame.dispose();
                });
//                mainPanel.reserveBook.addActionListener(e -> {
//                    ReserveBook();
//                    mainPanel.frame.dispose();
//                });
                mainPanel.login.addActionListener(e -> {
                    Login();
                    mainPanel.frame.dispose();
                });

            }
            case 2 -> {
                mainPanel.login.setText("Logout");

                mainPanel.getReturnBook().addActionListener(e -> {
                    ReturnBook();
                    mainPanel.frame.dispose();
                });
                mainPanel.getRegisterBook().addActionListener(e -> {
                    RegisterBook();
                    mainPanel.frame.dispose();
                });
                mainPanel.getBrowseBooks().addActionListener(e -> {
                    BrowseBooks();
                    mainPanel.frame.dispose();
                });
            }
        }


    }

    private void BrowseBooks() {
        userId=0;
        BrowseBookPanel3 browseBookPanel3 = new BrowseBookPanel3(userId);
        browseBookPanel3.getCancelButton().addActionListener(e -> disposeSubPanel(browseBookPanel3));
        browseBookPanel3.getSearchButton().addActionListener(e -> {
            browseBookPanel3.fillLibraryInfo();

        });
        browseBookPanel3.getAcceptButton().addActionListener(e -> {
            browseBookPanel3.reserv(userId);

        });
        browseBookPanel3.getPayButton().addActionListener(e->{
             payPenalty(userId);
        });

    }

    private void ViewLibInfo() {
        ViewLibInfoPanel viewLibInfo = new ViewLibInfoPanel();
        viewLibInfo.getCancelButton().addActionListener(e -> disposeSubPanel(viewLibInfo));
        viewLibInfo.getAcceptButton().addActionListener(e -> {
            viewLibInfo.getStatusInfo().setText("Waiting for the Data from the database");
            viewLibInfo.getStatusInfo().paintImmediately(viewLibInfo.getStatusInfo().getVisibleRect());

            Library answer = Database.getLibraryInfo(viewLibInfo.getLibraryName().getText());
            if(answer == null){
                handleMessagePanel(viewLibInfo, "No library with such name in the database!");
            }
            else{
                viewLibInfo.fillLibraryInfo(answer);
            }
            viewLibInfo.getStatusInfo().setText("Status: Waiting for Input");

        });
    }
    private void ReserveBook() { // @TODO probably will be removed with browseBooksPanel inheriting it's purpose
        //ViewLibInfoPanel ViewLibInfoPanel = new ViewLibInfoPanel();
        //ViewLibInfoPanel.getCancelButton().addActionListener(e -> disposeSubPanel(ViewLibInfoPanel));
    }
    private void ReturnBook() {
        ReturnBooksPanel returnBookPanel = new ReturnBooksPanel();
        returnBookPanel.getCancelButton().addActionListener(e -> disposeSubPanel(returnBookPanel));
        returnBookPanel.getInputUserID().addActionListener(e -> {
            String userID = returnBookPanel.getInputUserID().getText().trim();
            Scanner sc = new Scanner(userID);
            if(sc.hasNextInt()){
                returnBookPanel.setSearchDataText(userID);
                returnBookPanel.getAcceptButton().setEnabled(true);
                returnBookPanel.fillResultTable(Database.getOrders(Integer.parseInt(userID), true));
            }
            else{
                handleMessagePanel(returnBookPanel,"The User ID has to be a number");
            }
        });
        returnBookPanel.getAcceptButton().addActionListener(e -> {
            int orderID = returnBookPanel.getOrderIdVec().elementAt(returnBookPanel.getResultTable().getSelectedRow());
            returnBookPanel.getOrderIdVec().remove(returnBookPanel.getResultTable().getSelectedRow());

            finalizeReturnBook(returnBookPanel, orderID);
            //Database.returnBook(Integer.parseInt(returnBookPanel.getInputUserID().getText()), 1, finalizeReturnBook());
        });

    }
    private void finalizeReturnBook(ReturnBooksPanel returnBookPanel, int orderID) //@TODO
    {
        returnBookPanel.setEnabled(false);
        ChoosePenaltyPanel choosePenaltyPanel = new ChoosePenaltyPanel(Database.getPenalties());
        choosePenaltyPanel.getCancelButton().addActionListener(e -> {
            choosePenaltyPanel.dispose();
            returnBookPanel.setEnabled(true);
        });
        choosePenaltyPanel.getAcceptButton().addActionListener(e -> {
            returnBookPanel.getResultTableModel().removeRow(returnBookPanel.getResultTable().getSelectedRow());
            Database.returnBook(orderID, 0);
            choosePenaltyPanel.dispose();
            returnBookPanel.setEnabled(true);
        });
        choosePenaltyPanel.getAddPenaltyButton().addActionListener(e -> {
            returnBookPanel.getResultTableModel().removeRow(returnBookPanel.getResultTable().getSelectedRow());
            int chosenPenaltyID = choosePenaltyPanel.getPenaltyIDVec().elementAt(
                    choosePenaltyPanel.getPenaltyBox().getSelectedIndex());
            Database.returnBook(orderID, chosenPenaltyID);
            choosePenaltyPanel.dispose();
            returnBookPanel.setEnabled(true);
        });
    }
    private void RegisterBook() {
        RegisterBookPanel registerBookPanel = new RegisterBookPanel();
        registerBookPanel.getCancelButton().addActionListener(e -> disposeSubPanel(registerBookPanel));
        registerBookPanel.getAcceptButton().addActionListener(e -> {

            Book book =new Book(Integer.valueOf(registerBookPanel.getBookIdInput().getText()),
                    registerBookPanel.getBookTitleInput().getText(),
                    registerBookPanel.getBookAutorInput().getText(),
                    Integer.valueOf(registerBookPanel.getBookPagesInput().getText()),
                    registerBookPanel.getBookISBNInput().getText(),
                    Integer.valueOf(registerBookPanel.getBookYearInput().getText()),
                    registerBookPanel.getBookGenreInput().getText());
            //addBook(book);
            disposeSubPanel(registerBookPanel);

        });
    }

    private void Login() {
        if (permissionLevel == 0) {
            LoginPanel loginPanel = new LoginPanel();
            loginPanel.getAcceptButton().addActionListener(e -> {
                //switch (Database.ValidateLoginData(loginPanel.getUsername().getText(), loginPanel.getPassword().getPassword())) {
                switch (2) {
                    case 0 -> {// show window couldn't log in
                        permissionLevel = 0;
                        handleMessagePanel(loginPanel, "Login falied: invalid data");
                    }
                    case 1 -> {
                        username = loginPanel.getUsername().getText();
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
            Run();

        }

    }
    private int disposeSubPanel(JFrame frameToDispose)
    {
        frameToDispose.dispose();
        Run();
        return 1;
    }



    public static void main(String[] args){
        new App();
//        Database a = new Database();
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
