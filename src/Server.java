import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.Timer;
import java.util.TimerTask;
import java.util.concurrent.CopyOnWriteArrayList;

public class Server {
    static CopyOnWriteArrayList<PrintWriter> outputs;
    DatabaseOperations databaseOperations = new DatabaseOperations();
    static ArrayList<ClientThread> clientThreads = new ArrayList<>();
    static Timer timer;
    static boolean skipButtonClicked = false;

    public Server(int port) throws IOException, SQLException {

        ServerSocket server = new ServerSocket(port);
        outputs = new CopyOnWriteArrayList<>();
        while (true) {
            Socket connectionSocket;
            try {
                connectionSocket = server.accept();

                BufferedReader input = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
                PrintWriter output = new PrintWriter(connectionSocket.getOutputStream());
                outputs.add(output);
                System.out.println("added output streams to copyonwritelist");
                if (outputs.size() == 2) {
                    for (int i = 0; i < outputs.size(); i++) {
                        if (i == 0) {
                            System.out.println("sent drawer message");
                            outputs.get(i).println("drawer");
                        } else outputs.get(i).println("guesser");
                    }
                }
                System.out.println("Before thread arrayList addition");
                Thread clientThread = new ClientThread(connectionSocket, input, output);
                clientThreads.add((ClientThread) clientThread);
                if (outputs.size() == 2) {
                    for (ClientThread thr : clientThreads) {
                        thr.start();
                        System.out.println("thread started");
                    }
                    System.out.println("!!!!!!!!!!!!!!");
                    break;
                }


            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        timer = new Timer(10000, new ActionListener() {
            int i=0;
            @Override
            public void actionPerformed(ActionEvent e) {
                if(skipButtonClicked) {
                    i++;
                    System.out.println("SKİP BUTTON CLİCKED İS TRUE");
                    skipButtonClicked = false;
                }
                if (i == outputs.size()) {
                    i = 0;
                }
                outputs.get(i).println("drawer");
                try {
                    String randomQ = databaseOperations.randomQuestion();
                    for (PrintWriter o : outputs) {
                        o.println(randomQ);
                    }


                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                for (int j = 0; j < outputs.size(); j++) {
                    if (j != i) {
                        outputs.get(j).println("guesser");
                    }
                }
                i++;
            }
        });
        timer.start();

        Timer labelTimer = new Timer(1000, e -> {
            for (PrintWriter wrt : outputs) {
                wrt.println("tmr");
            }
        });
        labelTimer.start();


    }

    public static void main(String[] args) throws IOException, SQLException {
        new Server(3000);

    }

}
