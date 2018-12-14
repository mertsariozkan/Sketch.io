import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TimerTask;
import java.util.Timer;
import java.util.concurrent.CopyOnWriteArrayList;

public class Client {
    private Socket socket;
    private BufferedReader input;
    private PrintWriter output;
    int oldX, oldY, currentX, currentY;
    private JPanel panel;
    private CopyOnWriteArrayList<String> coordinates;
    private Timer timer;
    private DrawPage drawingPage;
    private String nickname;
    DatabaseOperations databaseOperations;
    HashMap<String , Integer> clients;
    boolean isDrawer;
    String questionWord;
    public Client(String ip, int port, String nickname) throws IOException, SQLException {
        this.nickname = nickname;

        coordinates = new CopyOnWriteArrayList<>();
        drawingPage = new DrawPage("SKETCH.IO");
        databaseOperations = new DatabaseOperations();
        clients = databaseOperations.getClients();
        for (String client : clients.keySet()){
            drawingPage.tableModel.addRow(new Object[]{client,clients.get(client)});
        }

        panel = drawingPage.canvas;

        drawingPage.skipTurn.addActionListener(e -> {
            output.println("skipmyturn");
            output.flush();

        });

        drawingPage.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                try {

                    databaseOperations.deleteClient(nickname);
                    input.close();
                    output.close();
                    socket.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
        });

        drawingPage.sendButton.addActionListener(e -> {
            if (!drawingPage.messageField.getText().isEmpty()){
                if(drawingPage.messageField.getText().stripTrailing().equalsIgnoreCase(questionWord)) {
                    System.out.println("CORRECCT");
                    output.println("msg" + nickname +" guessed the correct answer!");
                    output.flush();
                    drawingPage.messageField.setText("");
                }
                else {
                    String clientMsg = "msg" + nickname + ": " + drawingPage.messageField.getText();
                    output.println(clientMsg);
                    output.flush();
                    drawingPage.messageField.setText("");
                }
            }

        });

        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if(isDrawer) {
                    oldX = e.getX();
                    oldY = e.getY();
                    panel.getGraphics().drawLine(10, 10, 20, 20);
                    System.out.println("Clicked");
                }
                }
        });
        panel.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if(isDrawer) {
                    currentX = e.getX();
                    currentY = e.getY();
                    coordinates.add(currentX + "-" + currentY);
                    panel.getGraphics().drawLine(oldX, oldY, currentX, currentY);
                    oldX = currentX;
                    oldY = currentY;
                }
            }
        });

        socket = new Socket(ip, port);
        input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        output = new PrintWriter(socket.getOutputStream(), true);

        output.println("usr" + nickname);
        output.flush();



        timer = new Timer();
        TimerTask job = new TimerTask() {
            @Override
            public void run() {
                output.println("drw" +  coordinates);
                coordinates.clear();
            }
        };
        timer.schedule(job, 0, 50);

        String message;
        try {
            while ((message = input.readLine()) != null) {
                if (message.contains("msg")){
                    message = message.substring(3);
                    drawingPage.chatArea.append(message+"\n");
                }
                else if (message.contains("usr")){
                    message = message.substring(3);
                    drawingPage.tableModel.addRow(new Object[]{message , 0});
                }
                else if (message.contains("drawer")){
                    System.out.println("this client is drawer");
                    isDrawer = true;
                    drawingPage.questionLabel.setVisible(true);
                    drawingPage.messageField.setFocusable(false);
                }
                else if(message.contains("guesser")){
                    System.out.println("this client is guesser");
                    isDrawer = false;
                    drawingPage.questionLabel.setVisible(false);
                    drawingPage.messageField.setFocusable(true);
                    drawingPage.sendButton.setFocusable(false);
                }
                else if(message.contains("que")) {
                    questionWord = message.substring(3);
                    drawingPage.questionLabel.setText(message.substring(3));
                    System.out.println(questionWord);
                }
                else if(message.contains("tmr")){
                    if (drawingPage.timeCounter.getText().equals("0")  )  drawingPage.timeCounter.setText("10");

                    drawingPage.timeCounter.setText(String.valueOf(Integer.parseInt(drawingPage.timeCounter.getText()) - 1));
                }
                else if(message.contains("stp")){
                    drawingPage.timeCounter.setText("10");
                }
                else if(message.contains("drw")){

                        message = message.substring(3);
                        message = message.replaceAll("\\s+", "");
                        message = message.substring(1, message.length() - 1);
                        String[] coordinates = message.split(",");

                        for (String c : coordinates) {
                            if (!c.equals("")) {
                                String xy[] = c.split("-");
                                if (!xy[0].equals("") && !xy[1].equals("")) {
                                    currentX = Integer.parseInt(xy[0]);
                                    currentY = Integer.parseInt(xy[1]);

                                    panel.getGraphics().drawLine(oldX, oldY, currentX, currentY);

                                    oldX = currentX;
                                    oldY = currentY;
                                }
                            }

                        }

                }
            }
        } catch (
                IOException e1) {
            e1.printStackTrace();
        }
        finally {
            System.out.println("in finally of client");
            input.close();
            output.close();
            socket.close();
        }


    }



}
