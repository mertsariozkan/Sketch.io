import com.sun.source.tree.Tree;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.TreeMap;

public class Server {

    static int correctAnswerCounter = 0;
    static ArrayList<Room> rooms;
    static ArrayList<TreeMap> userLists;
    ArrayList<Boolean> statusOfRoomAvailability;
    BufferedReader input = null;
    PrintWriter output = null;
    Socket connectionSocket=null;

    public Server(int port) throws IOException, SQLException {
        rooms = new ArrayList<>();
        userLists = new ArrayList<>();
        statusOfRoomAvailability = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            rooms.add(new Room(i));
            userLists.add(null);
            statusOfRoomAvailability.add(true);
        }
        ServerSocket server = new ServerSocket(port);
        try {
        while (true) {
                connectionSocket = server.accept();

                input = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
                output = new PrintWriter(connectionSocket.getOutputStream());
                String id = input.readLine();
                while(!id.contains("rid"));
                id = id.substring(3);
                rooms.get(Integer.parseInt(id)).getClientOutputs().add(output);
                rooms.get(Integer.parseInt(id)).getClientInputs().add(input);
                System.out.println(rooms.get(Integer.parseInt(id)).getClientOutputs());
                System.out.println(id);
                System.out.println("added output streams to copyonwritelist");
                for (int i=0;i<rooms.size();i++) {
                    if (rooms.get(i).getClientOutputs().size() >= 2 && statusOfRoomAvailability.get(i)) {
                        new ServerThread(rooms.get(i), connectionSocket, input, output).start();
                        statusOfRoomAvailability.set(i,false);
                    }
                }
                System.out.println("Before thread arrayList addition");
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            connectionSocket.close();
            input.close();
            output.close();
        }
    }


    public static void main(String[] args) throws IOException, SQLException {
        new Server(3000);

    }

}
