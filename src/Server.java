import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;

public class Server {
    static ArrayList<PrintWriter> outputs = new ArrayList<>();

    public Server(int port) throws IOException {
        ServerSocket server = new ServerSocket(port);

        while (true) {
            Socket connectionSocket;
            try {
                connectionSocket = server.accept();

                BufferedReader input = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
                PrintWriter output = new PrintWriter(connectionSocket.getOutputStream());
                outputs.add(output);
                Thread clientThread = new ClientThread(connectionSocket, input, output);
                clientThread.start();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }

    public static void main(String[] args) throws IOException, SQLException {
        new DatabaseOperations();
        new Server(3000);

    }

}
