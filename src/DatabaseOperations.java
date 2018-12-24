import java.sql.*;

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

        String createClientsTable = "CREATE TABLE IF NOT EXISTS clients (\n"
                + " id integer primary key,\n"
                + " clientName text not null,\n"
                + " score integer\n"
                + ");";

        Statement statementClients = connection.createStatement();
        statementClients.execute(createClientsTable);


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

    public String randomQuestion() throws SQLException {
        String selectQuery = "SELECT COUNT(*) FROM questions";
        Statement selectStatement = connection.createStatement();
        ResultSet sizeSet = selectStatement.executeQuery(selectQuery);
        int size = sizeSet.getInt(1);
        int randomizedNumber =(int)(Math.random() * ((size - 1) + 1)) + 1 ;

        String randomQuestionQuery = "SELECT question FROM questions WHERE id = " + randomizedNumber;
        Statement randomStatement = connection.createStatement();
        ResultSet randomQ = randomStatement.executeQuery(randomQuestionQuery);

        return "que"+randomQ.getString("question");


    }
}
