import jdk.swing.interop.SwingInterOpUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.TreeMap;

public class Server {

    static int correctAnswerCounter = 0;
    static ArrayList<Room> rooms;
    static ArrayList<TreeMap> userLists;
    static ArrayList<Boolean> statusOfRoomAvailability;
    private BufferedReader input;
    private PrintWriter output;
    private Socket connectionSocket = null;
    private ServerThread sThread = null;
    private ArrayList<Integer> roomInfo;

    private Server(int port) {
        rooms = new ArrayList<>();
        userLists = new ArrayList<>();
        statusOfRoomAvailability = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            rooms.add(new Room(i));
            userLists.add(null);
            statusOfRoomAvailability.add(true);
        }
        ServerSocket server = null;
        try {
            server = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }

        loop:
        while (true) {
            try {
                connectionSocket = server.accept();
                input = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
                output = new PrintWriter(connectionSocket.getOutputStream());
                //RoomThread roomThread = new RoomThread(connectionSocket,input,output);
                //roomThread.start();
                String id = input.readLine();
                while (!id.contains("$rid")) {
                    if(id.contains("$gR")) {
                        roomInfo = new ArrayList<>();
                        for(Room r : rooms) {
                            roomInfo.add(r.getClientOutputs().size());
                        }
                        output.println(roomInfo);
                        output.flush();
                        continue loop;
                    }
                }
                    id = id.substring(4);
                    rooms.get(Integer.parseInt(id)).getClientOutputs().add(output);
                    rooms.get(Integer.parseInt(id)).getClientInputs().add(input);
                    for (int i = 0; i < rooms.size(); i++) {
                        if (rooms.get(i).getClientOutputs().size() >= 3 && statusOfRoomAvailability.get(i)) {
                            sThread = new ServerThread(rooms.get(i), connectionSocket, input, output);
                            sThread.start();
                            statusOfRoomAvailability.set(i, false);
                        }
                    }
            } catch (IOException e) {
                sThread.stop();
                sThread = null;
                try {
                    connectionSocket.close();
                } catch (IOException e1) {
                }
            }
        }

    }


    public static void main(String[] args) {
        new Server(3000);

    }

}
