import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class ClientThread extends Thread {
    private Socket socket;
    private BufferedReader input;
    private PrintWriter output;
    private DatabaseOperations databaseOperations;
    private String score;
    private String nickname;
    private int roomId;
    private boolean loop = true;
    private ArrayList<Integer> roomInfo;

    public ClientThread(Socket socket, BufferedReader input, PrintWriter output, int roomId) {
        this.socket = socket;
        this.input = input;
        this.output = output;
        this.roomId = roomId;
        databaseOperations = new DatabaseOperations();
    }

    @Override
    public void run() {
        try {
            while (loop) {
                String message;
                while (loop && (message = input.readLine()) != null) {
                    if (message.contains("$skipword")) {
                        // Skip current word
                        Server.correctAnswerCounter = 0;
                        createUserList(message);
                        databaseOperations.connectToDatabase();
                        String randomQ = databaseOperations.randomQuestion();
                        databaseOperations.closeConnection();
                        broadcastMessage(randomQ);
                        broadcastMessage(Server.userLists.get(roomId));
                    } else if (message.contains("$usr")) {
                        createUserList(message);
                        broadcastMessage(Server.userLists.get(roomId));
                    } else if (message.contains("$scs")) {
                        Server.correctAnswerCounter++;
                        broadcastMessage(message);
                        if (Server.correctAnswerCounter == Server.rooms.get(roomId).getClientOutputs().size() - 1) {
                            Server.correctAnswerCounter = 0;
                            databaseOperations.connectToDatabase();
                            String randomQ = databaseOperations.randomQuestion();
                            databaseOperations.closeConnection();
                            broadcastMessage(randomQ);
                        }
                    } else if (message.contains("$ovr")) {
                        broadcastMessage(message);
                    } else if (message.contains("$scc")) {
                        createUserList(message);
                        broadcastMessage(Server.userLists.get(roomId));
                    } else if (message.contains("$ovx")) {
                        createUserList(message);
                        broadcastMessage(Server.userLists.get(roomId));
                    } else if(message.contains("$gR")) {
                        // Update room info.
                        roomInfo = new ArrayList<>();
                        for(Room r : Server.rooms) {
                            roomInfo.add(r.getClientOutputs().size());
                        }
                        output.println(roomInfo);
                        output.flush();
                    } else if (message.equals("$cls")) {
                        broadcastMessage(message);
                        loop = false;
                    } else {
                        broadcastMessage(message);
                    }
                }
            }
        } catch (IOException e) {
        } finally {
            // Update userlist and room accordingly if client disconnects.
            Server.rooms.set(roomId, new Room(roomId));
            Server.userLists.set(roomId, null);
            Server.statusOfRoomAvailability.set(roomId, true);
        }
    }

    public void broadcastMessage(Object message) {
        for (PrintWriter o : Server.rooms.get(roomId).getClientOutputs()) {
            o.println(message);
            o.flush();
        }
    }

    // Create info from incoming message by parsing them to analyze nickname and score.
    public void createUserList(String message) {
        String tempMessage = null;
        if (message.contains("$usr")) {
            tempMessage = message.substring(4);
        } else if (message.contains("$skipword")) {
            tempMessage = message.substring(9);
        } else if (message.contains("$ovx")) {
            tempMessage = message.substring(4);
        } else if (message.contains("$scc")) {
            tempMessage = message.substring(4);
        }
        char[] messageContent = tempMessage.toCharArray();
        boolean regexFlag = false;
        nickname = "";
        score = "";
        for (char c : messageContent) {
            if (regexFlag) {
                score += c;
            }
            if (c == '/') {
                regexFlag = true;
            }
            if (!regexFlag) {
                nickname += c;
            }

        }
        Server.userLists.get(roomId).put(nickname, Integer.valueOf(score));
    }
}
