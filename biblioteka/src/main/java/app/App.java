package app;

import panels.*;

import javax.swing.*;
import javax.xml.crypto.Data;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Scanner;
import java.util.Vector;

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
        switch(permissionLevel){
            case 0 ->{
                mainPanel.login.setText("Login");
                mainPanel.login.addActionListener(e -> {
                    Login();
                    mainPanel.frame.dispose();
                });
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
                mainPanel.reserveBook.addActionListener(e -> {
                    ReserveBook();
                    mainPanel.frame.dispose();
                });
                mainPanel.login.addActionListener(e -> {
                    Login();
                    mainPanel.frame.dispose();
                });

            }
            case 2 -> {
                mainPanel.login.setText("Logout");
                mainPanel.login.addActionListener(e -> {
                    Login();
                    mainPanel.frame.dispose();
                });

                mainPanel.getCheckOutBook().addActionListener(e -> {
                    CheckOut();
                    mainPanel.frame.dispose();
                });
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

        BrowseBooksPanel2 browseBooksPanel2 = new BrowseBooksPanel2(permissionLevel);
        browseBooksPanel2.getCancelButton().addActionListener(e -> disposeSubPanel(browseBooksPanel2));


    }

    private void CheckOut() {
        CheckOutPanel checkOutPanel = new CheckOutPanel();
        checkOutPanel.getCancelButton().addActionListener(e -> disposeSubPanel(checkOutPanel));
    }
    private void ReserveBook() {
        CheckOutPanel checkOutPanel = new CheckOutPanel();
        checkOutPanel.getCancelButton().addActionListener(e -> disposeSubPanel(checkOutPanel));
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
                returnBookPanel.fillResultTable(Database.getOrders(Integer.parseInt(userID)));
            }
            else{
                returnBookPanel.setEnabled(false);
                MessagePanel notNumber = new MessagePanel("The User ID has to be a number");
                notNumber.getAcceptButton().addActionListener(f -> {
                    notNumber.dispose();
                    returnBookPanel.setEnabled(true);});
            }
        });
        returnBookPanel.getAcceptButton().addActionListener(e -> {
            Vector selected = returnBookPanel.getResultTableModel().getDataVector().
                    elementAt(returnBookPanel.getResultTable().convertRowIndexToModel(returnBookPanel.getResultTable().
                            getSelectedRow()));
            //Database.returnBook(Integer.parseInt(returnBookPanel.getInputUserID().getText()), 1, calculatePenalty());
        });

    }
    private void RegisterBook() {
        RegisterBookPanel registerBookPanel = new RegisterBookPanel();
        registerBookPanel.getCancelButton().addActionListener(e -> disposeSubPanel(registerBookPanel));

    }

    private void Login() {
        if (permissionLevel == 0) {
            LoginPanel loginPanel = new LoginPanel();
            loginPanel.getAcceptButton().addActionListener(e -> {
                switch (Database.ValidateLoginData(loginPanel.getUsername().getText(), loginPanel.getPassword().getPassword())) {
                //switch (2) {
                    case 0 -> {// show window couldn't log in
                        permissionLevel = 0;
                        loginPanel.dispose();
                        MessagePanel messagePanel = new MessagePanel("Login falied: invalid data");
                        messagePanel.getAcceptButton().addActionListener(f -> disposeSubPanel(messagePanel));
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

    private int calculatePenalty() //@TODO
    {
        return 0;
    }

    public static void main(String[] args){
        new App();
//        Database a = new Database();
    }


}
