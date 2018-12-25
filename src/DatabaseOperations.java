import java.sql.*;

public class DatabaseOperations {

    private Connection connection;

    public DatabaseOperations() throws SQLException {
        connectToDatabase();
        String createQuestionsTable = "CREATE TABLE IF NOT EXISTS questions (\n"
                + " id integer primary key,\n"
                + " question text not null\n"
                + ");";
        Statement statement = connection.createStatement();
        statement.execute(createQuestionsTable);
    }

    public void closeConnection() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean connectToDatabase() {
        try {
            String connectionUrl = "jdbc:sqlite:sketchio.db";
            connection = DriverManager.getConnection(connectionUrl);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

    }

    public String randomQuestion() throws SQLException {
        String selectQuery = "SELECT COUNT(*) FROM questions";
        Statement selectStatement = connection.createStatement();
        ResultSet sizeSet = selectStatement.executeQuery(selectQuery);
        int size = sizeSet.getInt(1);
        int randomizedNumber = (int) (Math.random() * ((size - 1) + 1)) + 1;

        String randomQuestionQuery = "SELECT question FROM questions WHERE id = " + randomizedNumber;
        Statement randomStatement = connection.createStatement();
        ResultSet randomQ = randomStatement.executeQuery(randomQuestionQuery);

        return "$que" + randomQ.getString("question");
    }

    public void addQuestionWord(String questionWord) throws SQLException {
        String insertQuestion = "INSERT INTO questions(question) VALUES ('" + questionWord + "')";
        Statement statement = connection.createStatement();
        statement.execute(insertQuestion);
    }
}
