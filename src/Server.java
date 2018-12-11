import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

public class Server {
    static CopyOnWriteArrayList<PrintWriter> outputs;

    public Server(int port) throws IOException {
        ServerSocket server = new ServerSocket(port);
        outputs = new CopyOnWriteArrayList<>();
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

    public static void main(String[] args) throws IOException {
        new Server(3000);

    }

}
