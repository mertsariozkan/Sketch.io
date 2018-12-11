import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseOperations {

    private static Connection connection;

    public DatabaseOperations() throws SQLException {
        connectToDatabase();
        String createQuestionsTable = "CREATE TABLE IF NOT EXISTS questions (\n"
                + " id integer primary key,\n"
                + " question text not null\n"
                + ");";

        Statement statement = connection.createStatement();

        statement.execute(createQuestionsTable);

    }

    public void connectToDatabase(){
            try {
                String connectionUrl = "jdbc:sqlite:sketchio.db";

                connection = DriverManager.getConnection(connectionUrl);
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (Exception e){
                e.printStackTrace();
            }

    }


    public String selectRandomQuestion(){


        return "";
    }
}
