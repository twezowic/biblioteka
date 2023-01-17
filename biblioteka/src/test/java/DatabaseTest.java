import app.Database;
import app.DatabaseBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


public class DatabaseTest {
    Database mockDatabase = new DatabaseBuilder().setDBURL("jdbc:oracle:thin:@//ora4.ii.pw.edu.pl:1521/pdb1.ii.pw.edu.pl").setDBUSERNAME("z95").setDBPASSWORD("36aydi").build();

    @org.junit.jupiter.api.Test
    void justAnExample() {

        Assertions.assertEquals(2, 1+1, "co jest");
    }
    @Test
    void aha()
    {
        mockDatabase.initializeData();
        int ah = 5;
        Assertions.assertEquals(2, 1+1, "co jest");
    }
}
