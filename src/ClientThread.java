import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;

public class ClientThread extends Thread {
    private Socket socket;
    private BufferedReader input;
    private PrintWriter output;
    DatabaseOperations databaseOperations;
    public ClientThread(Socket socket, BufferedReader input, PrintWriter output) throws SQLException {
        this.socket = socket;
        this.input = input;
        this.output = output;
        databaseOperations =  new DatabaseOperations();
    }

    @Override
    public void run() {

        while (true) {
            try {
                String message;
                while ((message = input.readLine()) != null) {
                    if (message.contains("usr")){
                        System.out.println(message);
                        databaseOperations.insertClient(message.substring(3), 0) ;
                    }
                    for (PrintWriter o : Server.outputs) {
                        o.println(message);
                        o.flush();
                    }

                }
            } catch (IOException e) {
                System.out.println("A client disconnected.");
                Server.outputs.remove(output);
                break;
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }



    }
}
