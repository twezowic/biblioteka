import app.Database;
import app.DatabaseBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;


public class DatabaseTest {
    Database mockDatabase = new DatabaseBuilder().setDBURL("jdbc:oracle:thin:@//ora4.ii.pw.edu.pl:1521/pdb1.ii.pw.edu.pl").setDBUSERNAME("z95").setDBPASSWORD("36aydi").build();
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

        }
        @Test
        void getOrdersReserved()
        {

        }
        @Test
        void getOrdersBorrowed()
        {

        }
        @Test
        void getOrdersReturned()
        {

        }
    }
    @Test
    void getPenalties()
    {

    }
    @Test
    void payPenalty()
    {}
    @Test
    void registerUser()
    {}
    @Test
    void getLibrariesNames()
    {}
    @Test
    void getAvailableCopies()
    {

    }
    @Test
    void addCopy()
    {

    }
    @Test
    void getUserID()
    {}

}
