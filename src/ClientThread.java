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

    public ClientThread(Socket socket, BufferedReader input, PrintWriter output) throws SQLException {
        this.socket = socket;
        this.input = input;
        this.output = output;
        databaseOperations = new DatabaseOperations();
    }

    @Override
    public void run() {

        while (true) {
            try {
                String message;
                while ((message = input.readLine()) != null) {
                    if (message.contains("skipword")) {
                        createUserList(message);
                        String randomQ = databaseOperations.randomQuestion();
                        for (PrintWriter o : Server.outputs) {
                            o.println(randomQ);
                            o.flush();
                        }
                        for (PrintWriter o : Server.outputs) {
                            o.println(Server.userList);
                            o.flush();
                        }

                    } else if (message.contains("usr")) {
                        createUserList(message);
                    } else if (message.contains("scs")) {
                        Server.correctAnswerCounter++;
                        if (Server.correctAnswerCounter == Server.outputs.size() - 1) {
                            Server.correctAnswerCounter = 0;
                            String randomQ = databaseOperations.randomQuestion();
                            for (PrintWriter o : Server.outputs) {
                                o.println(randomQ);
                            }
                        }
                    }

                    for (PrintWriter o : Server.outputs) {
                        o.println(message);
                        o.flush();
                    }

                }
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("A client disconnected.");
                Server.userList.remove(nickname);
                Server.outputs.remove(output);
                break;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }


    }

    public void createUserList(String message) {
        String tempMessage = null;
        if(message.contains("usr")) {
             tempMessage = message.substring(3);
        }
        else if(message.contains("skipword")) {
             tempMessage = message.substring(8);
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
        Server.userList.put(nickname, Integer.valueOf(score));
    }
}
