import app.Database;
import app.DatabaseBuilder;
import classes.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import javax.print.attribute.standard.Copies;
import java.util.ArrayList;


public class DatabaseTest {
    Database mockDatabase = new DatabaseBuilder()
            .setDBURL("jdbc:oracle:thin:@//ora4.ii.pw.edu.pl:1521/pdb1.ii.pw.edu.pl")
            .setDBUSERNAME("z95")
            .setDBPASSWORD("36aydi")
            .build();
    @BeforeEach
    void reset()
    {
        mockDatabase.initializeData();
    }
    @Test
    void checkAuthor()
    {
        Assertions.assertEquals(1,
                mockDatabase.checkAuthor("Adam Mickiewicz", false));
    }
    @Test
    void checkAuthorNotInDatabase()
    {
        Assertions.assertEquals(-1,
                mockDatabase.checkAuthor("Jan Parandowski", false));
    }
    @Test
    void validateLoginUser()
    {
        int[] data = mockDatabase.validateLoginData("user1", new char[]{'p', 'a', 's', 's', 'w', 'o', 'r', 'd', '1'});
        Assertions.assertEquals(1, data[0]);
        Assertions.assertEquals(1, data[1]);
    }
    @Test
    void validateLoginAdmin()
    {
        int[] data = mockDatabase.validateLoginData("admin1", new char[]{'a', 'd', 'm', 'i', 'n', '1'});
        Assertions.assertEquals(2, data[0]);
        Assertions.assertEquals(4, data[1]);
    }
    @Test
    void validateLoginIncorrect()
    {
        int[] data = mockDatabase.validateLoginData("admin1", new char[]{'h', 'a', 's', 'l', 'o'});
        Assertions.assertEquals(0, data[0]);
        Assertions.assertEquals(0, data[1]);
    }
    @Test
    void getBooksAll()
    {
    }
    @Test
    void getBooksTitle()
    {

    }
    @Test
    void getBooksAuthor()
    {
        mockDatabase.initializeData();
    }
    @Test
    void getBooksISBN()
    {

    }
    @Test
    void getBooksIncorrect()
    {

    }
    @Test
    void addBook()
    {
        Book newBook = new Book(0, "test", "Adam Mickiewicz", 5, "test", 2023, "test");
        Assertions.assertEquals(2, mockDatabase.getBooks("", "Adam Mickiewicz", "", "").size());
        mockDatabase.addBook(newBook);
        Assertions.assertEquals(3, mockDatabase.getBooks("", "Adam Mickiewicz", "", "").size());

    }
    @Test
    void getGenres()
    {

    }
    @Test
    void isPeneltyWithout()
    {

    }
    @Test
    void isPeneltyWith()
    {

    }
    @Test
    void getLibraryInfo()
    {
        Library lib = mockDatabase.getLibraryInfo("Biblioteka Miejska");
        Assertions.assertEquals(1, lib.getLibraryID());
        Assertions.assertEquals("Biblioteka Miejska", lib.getName());
        Assertions.assertEquals("Piotrkowska 100", lib.getStreet());
        Assertions.assertEquals("Lodz", lib.getCity());
        Assertions.assertEquals(123456789, lib.getPhone());
        Assertions.assertEquals(5, lib.getWorkTimes().getOpening().size());
    }
    @Test
    void getOrders()
    {

    }
    @Nested
    class TestOrders{
        @BeforeEach
        void initializeOrders()
        {

        }
        @Test
        void getOrdersAll()
        {
            Assertions.assertEquals(0, mockDatabase.getOrders(1,"").size());
            mockDatabase.orderBook(1,1);
            Assertions.assertEquals(1, mockDatabase.getOrders(1,"").size());
            mockDatabase.borrowBook(1);
            Assertions.assertEquals(1, mockDatabase.getOrders(1,"").size());
            mockDatabase.returnBook(1,0);
            Assertions.assertEquals(1, mockDatabase.getOrders(1,"").size());
        }
        @Test
        void getOrdersReserved()
        {
            Assertions.assertEquals(0, mockDatabase.getOrders(1,"Rezerwacja").size());
            mockDatabase.orderBook(1,1);
            Assertions.assertEquals(1, mockDatabase.getOrders(1,"Rezerwacja").size());
            mockDatabase.borrowBook(1);
            Assertions.assertEquals(0, mockDatabase.getOrders(1,"Rezerwacja").size());
            mockDatabase.returnBook(1,0);
            Assertions.assertEquals(0, mockDatabase.getOrders(1,"Rezerwacja").size());
        }
        @Test
        void getOrdersBorrowed()
        {
            Assertions.assertEquals(0, mockDatabase.getOrders(1,"Wypozyczona").size());
            mockDatabase.orderBook(1,1);
            Assertions.assertEquals(0, mockDatabase.getOrders(1,"Wypozyczona").size());
            mockDatabase.borrowBook(1);
            Assertions.assertEquals(1, mockDatabase.getOrders(1,"Wypozyczona").size());
            mockDatabase.returnBook(1,0);
            Assertions.assertEquals(0, mockDatabase.getOrders(1,"Wypozyczona").size());
        }
        @Test
        void getOrdersReturned()
        {
            Assertions.assertEquals(0, mockDatabase.getOrders(1,"Zwrocona").size());
            mockDatabase.orderBook(1,1);
            Assertions.assertEquals(0, mockDatabase.getOrders(1,"Zwrocona").size());
            mockDatabase.borrowBook(1);
            Assertions.assertEquals(0, mockDatabase.getOrders(1,"Zwrocona").size());
            mockDatabase.returnBook(1,0);
            Assertions.assertEquals(1, mockDatabase.getOrders(1,"Zwrocona").size());
        }
    }
    @Test
    void getPenalties()
    {
        Assertions.assertEquals("Zniszczenie ksiazki", mockDatabase.getPenalties().get(1).getName());
    }
    @Test
    void addAndPayPenalty()
    {
        mockDatabase.orderBook(1,1);
        mockDatabase.borrowBook(1);
        mockDatabase.returnBook(1,3);
        Assertions.assertEquals(true, mockDatabase.isPenalty(1));
        mockDatabase.payPenalty(1);
        Assertions.assertEquals(false, mockDatabase.isPenalty(1));
    }
    @Test
    void registerUser()
    {
        User newUser = new User("test", "test", "nowy", "nowy");
        mockDatabase.registerUser(newUser);
        Assertions.assertEquals(newUser, mockDatabase.getUserID("nowy")); //not sure if this will work
    }
    @Test
    void getLibrariesNames()
    {
        String[] result = mockDatabase.getLibrariesNames();
        Assertions.assertEquals("Biblioteka Miejska", result[0]);
        Assertions.assertEquals("Biblioteka Uniwersytecka", result[2]);
        Assertions.assertEquals("Biblioteka Szkolna", result[4]);
    }
    @Test
    void getAvailableCopies()
    {
        try {
            Assertions.assertEquals(5, mockDatabase.getAvailableCopies(1).size());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
    @Test
    void addCopy()
    {
        mockDatabase.addCopy(1,1);
        try {
            Assertions.assertEquals(6, mockDatabase.getAvailableCopies(1).size());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    @Test
    void getUserID()
    {
        Assertions.assertEquals(1, mockDatabase.getUserID("user1"));
    }

}
