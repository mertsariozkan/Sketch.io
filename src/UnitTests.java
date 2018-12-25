import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class UnitTests {

    private DatabaseOperations databaseOperations;

    @Before
    public void setUp() {
        try {
            databaseOperations = new DatabaseOperations();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        new LoginPage();
    }

    @Test
    public void isDatabaseConnecting() {
        assertTrue(databaseOperations.connectToDatabase());
    }

    @Test
    public void testQuestion() {
        try {
            assertNotNull(databaseOperations.randomQuestion());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
