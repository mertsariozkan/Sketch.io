import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.TreeMap;

public class ServerThread extends Thread {
    private Room room;
    private Socket connectionSocket;
    private BufferedReader input;
    private PrintWriter output;
    ArrayList<ClientThread> clientThreads;
    static Timer timer;
    TreeMap<String, Integer> userList;
    static DatabaseOperations databaseOperations;

    public ServerThread(Room room, Socket connectionSocket, BufferedReader input, PrintWriter output) throws SQLException {
        this.room = room;
        this.connectionSocket = connectionSocket;
        this.input = input;
        this.output = output;
        databaseOperations = new DatabaseOperations();
        userList = new TreeMap<>();
        Server.userLists.add(room.getId() , userList);
        clientThreads = new ArrayList<>();
    }

    @Override
    public void run() {
        for (int i = 0; i < room.getClientOutputs().size(); i++) {
            if (i == 0) {
                System.out.println("sent drawer message");
                room.getClientOutputs().get(i).println("drawer");
            } else room.getClientOutputs().get(i).println("guesser");

            System.out.println("Before thread arrayList addition");
            Thread clientThread = null;
            try {
                clientThread = new ClientThread(connectionSocket, room.getClientInputs().get(i), room.getClientOutputs().get(i), room.getId());
            } catch (SQLException e) {
                e.printStackTrace();
            }
            clientThreads.add((ClientThread) clientThread);
        }

        for (ClientThread thr : clientThreads) {
            System.out.println("aaaaa");
            thr.start();
            System.out.println("thread started");

        }
        for (PrintWriter p : room.getClientOutputs()) {
            p.println("GAME");
            p.flush();
        }
        timer = new Timer(10000, new ActionListener() {
            int i = 0;

            @Override
            public void actionPerformed(ActionEvent e) {
                for (PrintWriter o : room.getClientOutputs()) {
                    o.println(userList);
                    o.flush();
                }
                if (i >= room.getClientOutputs().size()) {
                    i = 0;
                }
                room.getClientOutputs().get(i).println("drawer");
                try {
                    String randomQ = databaseOperations.randomQuestion();
                    for (PrintWriter o : room.getClientOutputs()) {
                        o.println(randomQ);
                    }


                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                for (int j = 0; j < room.getClientOutputs().size(); j++) {
                    if (j != i) {
                        room.getClientOutputs().get(j).println("guesser");
                    }
                }
                i++;
            }
        });
        timer.setInitialDelay(0);
        timer.start();
        Timer labelTimer = new Timer(1000, e -> {
            for (PrintWriter wrt : room.getClientOutputs()) {
                wrt.println("tmr");
            }
        });
        labelTimer.start();
    }


}
