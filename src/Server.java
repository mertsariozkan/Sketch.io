import javax.xml.crypto.Data;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CopyOnWriteArrayList;

public class Server {
    static CopyOnWriteArrayList<PrintWriter> outputs;
    DatabaseOperations databaseOperations = new DatabaseOperations();
    static ArrayList<ClientThread> clientThreads = new ArrayList<>();
    static TimerTask timerTask;
    static Timer timer;
    static boolean skip;
    public Server(int port) throws IOException, SQLException {

        ServerSocket server = new ServerSocket(port);
        outputs = new CopyOnWriteArrayList<>();
        while (true) {
            Socket connectionSocket;
            if (outputs.size() == 2){
                while(true){

                    if (skip){
                        timer.cancel();

                        Timer timer = new Timer();
                        TimerTask timerTask1=null;
                        try {
                            timerTask1 = new CustomTimerTask(outputs);
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        timer.schedule(timerTask1 , 0 , 10000);
                        skip = false;
                    }
                }
            }

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

        timer = new Timer();
        TimerTask timerTask = new CustomTimerTask(outputs);
        timer.schedule(timerTask, 0, 10000);

        Timer labelTimer = new Timer();
        TimerTask labelTimerTask = new TimerTask() {
            @Override
            public void run() {
                for (PrintWriter wrt : outputs) {
                    wrt.println("tmr");
                }

            }
        };
        labelTimer.schedule(labelTimerTask, 0, 1000);


    }

    public static void main(String[] args) throws IOException, SQLException {
        new Server(3000);

    }

}
