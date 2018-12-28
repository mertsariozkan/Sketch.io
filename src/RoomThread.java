import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class RoomThread extends Thread {
    private Socket connectionSocket;
    private BufferedReader input;
    private PrintWriter output;
    private ArrayList<Integer> roomInfo;

    public RoomThread(Socket connectionSocket, BufferedReader input, PrintWriter output) {
        this.connectionSocket = connectionSocket;
        this.input = input;
        this.output = output;
    }

    @Override
    public void run() {
        try {
            String refreshRequest;
            while((refreshRequest=input.readLine())!=null) {
                if (refreshRequest.contains("$gR")) {
                    roomInfo = new ArrayList<>();
                    for (Room r : Server.rooms) {
                        roomInfo.add(r.getClientOutputs().size());
                    }
                    output.println(roomInfo);
                    output.flush();
                }
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
    }
}
