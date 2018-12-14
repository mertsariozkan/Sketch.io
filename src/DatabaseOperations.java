import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

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

    public void insertClient(String clientName , int score) throws SQLException {
        String insertQuery = "INSERT INTO clients(clientName,score) VALUES(?,?)";

        PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);

        preparedStatement.setString(1 , clientName);
        preparedStatement.setInt(2  , score);
        preparedStatement.executeUpdate();
    }
    public HashMap<String , Integer> getClients() throws SQLException {

        HashMap<String, Integer> clientsMap = new HashMap<>();

        String selectQuery = "SELECT * FROM clients";
        Statement statement = connection.createStatement();
        ResultSet set = statement.executeQuery(selectQuery);

        while (set.next()){
            clientsMap.put(set.getString("clientName") , set.getInt("score"));
        }
        return clientsMap;
    }


    public void deleteClient(String nickName) throws SQLException {
        String deleteQuery = "DELETE FROM clients WHERE name = ?";

        PreparedStatement preparedStatementDelete = connection.prepareStatement(deleteQuery);
        preparedStatementDelete.setString(1 , nickName);
        preparedStatementDelete.executeUpdate();
    }

    public void addQuestion(String question) throws SQLException {
        String insertQuestionQuery = "INSERT INTO questions(question) VALUES(?)";
        PreparedStatement preparedStatementInsert = connection.prepareStatement(insertQuestionQuery);
        preparedStatementInsert.setString(1 , question);
        preparedStatementInsert.executeUpdate();
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
