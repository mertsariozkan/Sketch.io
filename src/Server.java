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
    static ArrayList<Boolean> statusOfRoomAvailability;
    private BufferedReader input = null;
    private PrintWriter output = null;
    private Socket connectionSocket = null;
    private ServerThread sThread = null;

    private Server(int port) throws IOException, SQLException {
        rooms = new ArrayList<>();
        userLists = new ArrayList<>();
        statusOfRoomAvailability = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            rooms.add(new Room(i));
            userLists.add(null);
            statusOfRoomAvailability.add(true);
        }
        ServerSocket server = new ServerSocket(port);

        while (true) {
            try {
                connectionSocket = server.accept();

                input = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
                output = new PrintWriter(connectionSocket.getOutputStream());
                String id = input.readLine();
                while (!id.contains("$rid")) ;
                id = id.substring(4);
                rooms.get(Integer.parseInt(id)).getClientOutputs().add(output);
                rooms.get(Integer.parseInt(id)).getClientInputs().add(input);
                for (int i = 0; i < rooms.size(); i++) {
                    if (rooms.get(i).getClientOutputs().size() >= 2 && statusOfRoomAvailability.get(i)) {
                        sThread = new ServerThread(rooms.get(i), connectionSocket, input, output);
                        sThread.start();
                        statusOfRoomAvailability.set(i, false);
                    }
                }
                } catch(IOException e){
                    e.printStackTrace();
                    sThread.stop();
                    sThread = null;
                    connectionSocket.close();
                }
            }

        }


        public static void main (String[]args) throws IOException, SQLException {
            new Server(3000);

        }

    }
