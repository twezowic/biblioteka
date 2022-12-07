package app;
import lib.BasePanel;
import panels.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
                mainPanel.login.addActionListener(e -> {
                    Login();
                    mainPanel.frame.dispose();
                });
            }
            case 1 ->{
                mainPanel.getBrowseBooks().addActionListener(e -> {
                    BrowseBooks();
                    mainPanel.frame.dispose();
                });
            }
            case 2 ->{
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
        BrowseBooksPanel browseBooksPanel = new BrowseBooksPanel();
        browseBooksPanel.getCancelButton().addActionListener(e -> {
        disposeSubPanel(browseBooksPanel);
    });
    }

    private void CheckOut() {
        CheckOutPanel checkOutPanel = new CheckOutPanel();
        checkOutPanel.getCancelButton().addActionListener(e -> {
            disposeSubPanel(checkOutPanel);
        });
    }
    private void ReturnBook() {
        ReturnBooksPanel returnBookPanel = new ReturnBooksPanel();
        returnBookPanel.getCancelButton().addActionListener(e -> {
        disposeSubPanel(returnBookPanel);
    });

    }
    private void RegisterBook() {
        RegisterBookPanel registerBookPanel = new RegisterBookPanel();
        registerBookPanel.getCancelButton().addActionListener(e -> {
        disposeSubPanel(registerBookPanel);
    });

    }

    private void Login() {
        LoginPanel loginPanel = new LoginPanel();
        loginPanel.getAcceptButton().addActionListener(e -> {
            switch(Database.ValidateLoginData(loginPanel.getUsername().getText(), loginPanel.getPassword().getPassword())){
                case 0->{// show window couldn't log in
                    disposeSubPanel(loginPanel);
                }
                case 1->{
                    username = loginPanel.getUsername().getText();
                    permissionLevel = 1;
                    disposeSubPanel(loginPanel);
                }
                case 2->{
                    username = loginPanel.getUsername().getText();
                    permissionLevel = 2;
                    disposeSubPanel(loginPanel);
                }
            }
        });
        loginPanel.getCancelButton().addActionListener(e -> {
            disposeSubPanel(loginPanel);
        });
    }
    private void disposeSubPanel(JFrame frameToDispose)
    {
        frameToDispose.dispose();
        Run();
    }

    public static void main(String[] args) {
        new App();
    }


}
