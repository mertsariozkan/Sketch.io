
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.TimerTask;
import java.util.Timer;

public class Client {
    private Socket socket;
    private BufferedReader input;
    private PrintWriter output;
    int oldX, oldY, currentX, currentY;
    private JPanel panel;
    private ArrayList<String> coordinates;
    private Timer timer;
    private DrawPage drawingPage;

    public Client(String ip, int port) throws IOException {

        coordinates = new ArrayList<>();
        drawingPage = new DrawPage("SKETCH.IO");
        panel = drawingPage.canvas;
        drawingPage.sendButton.addActionListener(e -> {
            if (!drawingPage.messageField.getText().isEmpty()){
                String clientMsg ="msg" + drawingPage.messageField.getText();
                output.println(clientMsg);
                output.flush();
                drawingPage.messageField.setText("");
            }

        });

        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                oldX = e.getX();
                oldY = e.getY();
                panel.getGraphics().drawLine(10, 10, 20, 20);
                System.out.println("Clicked");
            }
        });
        panel.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                currentX = e.getX();
                currentY = e.getY();
                coordinates.add(currentX + "-" + currentY);
                panel.getGraphics().drawLine(oldX, oldY, currentX, currentY);
                oldX = currentX;
                oldY = currentY;
            }
        });

        socket = new Socket(ip, port);
        input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        output = new PrintWriter(socket.getOutputStream(), true);



        timer = new Timer();
        TimerTask job = new TimerTask() {
            @Override
            public synchronized void run() {
                output.println(coordinates);
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
                else {
                    if (message != "") {

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
    public static void main(String[] args) throws IOException {
        new Client("localhost", 3000);
    }



}
