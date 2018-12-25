import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class UnitTests {

    private DatabaseOperations databaseOperations;

    @Before
    public void setUp() {
        databaseOperations = new DatabaseOperations();
        new LoginPage();
    }

    @Test
    public void isDatabaseConnecting() {
        assertTrue(databaseOperations.connectToDatabase());
    }

    @Test
    public void testQuestion() {
        assertNotNull(databaseOperations.randomQuestion());
    }
}
