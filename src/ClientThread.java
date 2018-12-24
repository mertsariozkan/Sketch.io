import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.TreeMap;

public class ClientThread extends Thread {
    private Socket socket;
    private BufferedReader input;
    private PrintWriter output;
    DatabaseOperations databaseOperations;
    String score;
    String nickname;
    int roomId;
    boolean loop = true;

    public ClientThread(Socket socket, BufferedReader input, PrintWriter output, int roomId) throws SQLException {
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
                        if (message.contains("skipword")) {
                            createUserList(message);
                            String randomQ = databaseOperations.randomQuestion();
                            for (PrintWriter o : Server.rooms.get(roomId).getClientOutputs()) {
                                o.println(randomQ);
                                o.flush();
                            }
                            for (PrintWriter o : Server.rooms.get(roomId).getClientOutputs()) {
                                o.println(Server.userLists.get(roomId));
                                o.flush();
                            }

                        } else if (message.contains("usr")) {
                            createUserList(message);
                        } else if (message.contains("scs")) {
                            Server.correctAnswerCounter++;
                            if (Server.correctAnswerCounter == Server.rooms.get(roomId).getClientOutputs().size() - 1) {
                                Server.correctAnswerCounter = 0;
                                String randomQ = databaseOperations.randomQuestion();
                                for (PrintWriter o : Server.rooms.get(roomId).getClientOutputs()) {
                                    o.println(randomQ);
                                }
                            }
                        } else if (message.contains("scc")) {
                            createUserList(message);
                            for (PrintWriter p : Server.rooms.get(roomId).getClientOutputs()) {
                                p.println(Server.userLists.get(roomId));
                                p.flush();
                            }
                        } else if (message.contains("ovr")) {
                            for (PrintWriter p : Server.rooms.get(roomId).getClientOutputs()) {
                                p.println(message);
                                System.out.println("Over message");
                                p.flush();
                            }
                        } else if (message.contains("ovx")) {
                            createUserList(message);
                            for (PrintWriter p : Server.rooms.get(roomId).getClientOutputs()) {
                                p.println(Server.userLists.get(roomId));
                                p.flush();
                            }
                        } else if(message.equals("cls")) {
                            for (PrintWriter p : Server.rooms.get(roomId).getClientOutputs()) {
                                p.println(message);
                                p.flush();
                            }
                            System.out.println("cls geldi");
                            loop = false;
                        } else {
                            for (PrintWriter o : Server.rooms.get(roomId).getClientOutputs()) {
                                o.println(message);
                                o.flush();
                            }
                        }
                }
            }
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        } finally {
            Server.rooms.set(roomId, new Room(roomId));
            Server.userLists.set(roomId, null);
            Server.statusOfRoomAvailability.set(roomId,true);
        }
    }


    public void createUserList(String message) {
        String tempMessage = null;
        if (message.contains("usr")) {
            tempMessage = message.substring(3);
        } else if (message.contains("skipword")) {
            tempMessage = message.substring(8);
        } else if (message.contains("ovx")) {
            tempMessage = message.substring(3);
        } else if (message.contains("scc")) {
            tempMessage = message.substring(3);
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
