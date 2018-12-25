import javax.swing.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.SQLException;
import java.util.*;
import java.util.Timer;
import java.util.concurrent.CopyOnWriteArrayList;

public class Client implements ActionListener {

    private Socket socket;
    private BufferedReader input;
    private PrintWriter output;
    private int oldX = -1, oldY = -1, currentX, currentY;
    private JPanel panel;
    private CopyOnWriteArrayList<String> coordinates;
    private Timer timer;
    private DrawPage drawingPage;
    private String nickname;

    private TreeMap<String, Integer> clients;
    private boolean isDrawer;
    private String questionWord;
    private int score;

    public Client(String ip, int port, String nickname, int id) throws IOException, SQLException {
        this.nickname = nickname;
        this.score = 0;
        coordinates = new CopyOnWriteArrayList<>();
        drawingPage = new DrawPage();
        clients = new TreeMap<>();

        panel = drawingPage.getCanvas();
        drawingPage.getMessageField().addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) { }

            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode()==KeyEvent.VK_ENTER) {
                    drawingPage.getSendButton().doClick();
                }
            }
            @Override
            public void keyReleased(KeyEvent e) {
                if(e.getKeyCode()==KeyEvent.VK_ENTER) {
                    drawingPage.getMessageField().setText("");
                }
            }
        });
        drawingPage.getPassWordButton().addActionListener(e -> {
            score -= 2;
            output.println("$skipword" + nickname + "/" + Integer.toString(score));
            output.flush();
        });

        drawingPage.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                output.println("$cls");
                output.flush();
            }
        });

        drawingPage.getSendButton().addActionListener(this);

        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (isDrawer) {
                    oldX = e.getX();
                    oldY = e.getY();
                }
            }
        });
        panel.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (isDrawer) {
                    currentX = e.getX();
                    currentY = e.getY();
                    coordinates.add(oldX + "-" + oldY + "-" + currentX + "-" + currentY);
                    panel.getGraphics().drawLine(oldX, oldY, currentX, currentY);
                    oldX = currentX;
                    oldY = currentY;
                }
            }
        });


        socket = new Socket(ip, port);
        input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        output = new PrintWriter(socket.getOutputStream(), true);

        output.println("$rid" + id);
        output.flush();

        timer = new Timer();
        TimerTask job = new TimerTask() {
            @Override
            public void run() {
                output.println("$drw" + coordinates);
                coordinates.clear();
            }
        };
        timer.schedule(job, 0, 10);

        String message;
        try {
            while ((message = input.readLine()) != null) {
                if (score >= 5) {
                    score = 0;
                    output.println("$ovr" + nickname);
                    output.flush();
                }
                if (message.contains("$msg")) {
                    message = message.substring(4);
                    drawingPage.getChatArea().append(message + "\n");
                } else if (message.contains("$scs")) {
                    message = message.substring(4);
                    if (isDrawer) {
                        score += 2;
                        output.println("$scc" + nickname + "/" + Integer.toString(score));
                        output.flush();
                    }
                    drawingPage.getChatArea().append(message + "\n");
                } else if (message.contains("$GAME")) {
                    output.println("$usr" + nickname + "/" + Integer.toString(score));
                    output.flush();
                } else if (message.contains("{")) {
                    System.out.println(message);
                    message = message.substring(1, message.length() - 1);
                    System.out.println(message);
                    String[] users = message.split(",");
                    System.out.println(message);
                    for (String user : users) {
                        user = user.stripLeading();
                        System.out.println(user);
                        String[] singleUser = user.split("=");
                        if (singleUser.length >= 2) {
                            System.out.println(singleUser[0] + "   " + singleUser[1]);
                            clients.put(singleUser[0], Integer.valueOf(singleUser[1]));
                        }
                    }
                    for (int i = drawingPage.getTableModel().getRowCount() - 1; i > -1; i--) {
                        drawingPage.getTableModel().removeRow(i);
                    }

                    Map sortedMap = sortByValues(clients);
                    Set set = sortedMap.entrySet();

                    for (Object aSet : set) {
                        Map.Entry me = (Map.Entry) aSet;
                        drawingPage.getTableModel().addRow(new Object[]{me.getKey(), me.getValue()});
                    }
                } else if (message.contains("$drawer")) {
                    System.out.println("this client is drawer");
                    clearCanvas();
                    isDrawer = true;
                    drawingPage.getPassWordButton().setVisible(true);
                    drawingPage.getQuestionLabel().setVisible(true);
                    drawingPage.getMessageField().setFocusable(false);
                } else if (message.contains("$guesser")) {
                    drawingPage.getPassWordButton().setVisible(false);
                    System.out.println("this client is guesser");
                    clearCanvas();
                    isDrawer = false;
                    drawingPage.getQuestionLabel().setVisible(false);
                    drawingPage.getMessageField().setFocusable(true);
                    drawingPage.getSendButton().setFocusable(false);
                } else if (message.contains("$que")) {
                    clearCanvas();
                    questionWord = message.substring(4);
                    drawingPage.getQuestionLabel().setText(message.substring(4));
                    System.out.println(questionWord);
                } else if (message.contains("$tmr")) {
                    if (drawingPage.getTimeCounter().getText().equals("0")) drawingPage.getTimeCounter().setText("10");

                    drawingPage.getTimeCounter().setText(String.valueOf(Integer.parseInt(drawingPage.getTimeCounter().getText()) - 1));
                } else if (message.contains("$drw")) {
                    if (!isDrawer) {
                        message = message.substring(4);
                        message = message.replaceAll("\\s+", "");
                        message = message.substring(1, message.length() - 1);
                        String[] coordinates = message.split(",");

                        for (String c : coordinates) {
                            if (!c.equals("")) {
                                String xy[] = c.split("-");
                                if (!xy[2].equals("") && !xy[3].equals("") && !xy[0].equals("") && !xy[1].equals("") ) {
                                    oldX = Integer.parseInt(xy[0]);
                                    oldY = Integer.parseInt(xy[1]);
                                    currentX = Integer.parseInt(xy[2]);
                                    currentY = Integer.parseInt(xy[3]);
                                    panel.getGraphics().drawLine(oldX, oldY, currentX, currentY);
                                }
                            }

                        }
                    }
                } else if (message.contains("$ovr")) {
                    message = message.substring(4);
                    score = 0;
                    output.println("$ovx" + nickname + "/" + Integer.toString(score));
                    drawingPage.getChatArea().setText("Game over. Winner is: " + message + "\n");

                } else if (message.equals("$cls")) {
                    drawingPage.setVisible(false);
                    new RoomPage(nickname);
                    break;
                }
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        }

    }

    public void clearCanvas() {
        panel.removeAll();
        panel.updateUI();
    }

    public static <K, V extends Comparable<V>> Map<K, V>
    sortByValues(final Map<K, V> map) {
        Comparator<K> valueComparator =
                (k2, k1) -> {
                    int compare =
                            map.get(k1).compareTo(map.get(k2));
                    if (compare == 0)
                        return 1;
                    else
                        return compare;
                };

        Map<K, V> sortedByValues =
                new TreeMap<>(valueComparator);
        sortedByValues.putAll(map);
        return sortedByValues;
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == drawingPage.getSendButton()) {
            if (!drawingPage.getMessageField().getText().isEmpty()) {
                if (drawingPage.getMessageField().getText().stripTrailing().equalsIgnoreCase(questionWord)) {
                    output.println("$scs" + nickname + " guessed the correct answer!");
                    output.flush();
                    score += 5;
                    output.println("$scc" + nickname + "/" + Integer.toString(score));
                    output.flush();
                    drawingPage.getMessageField().setText("");
                } else {
                    String clientMsg = "$msg" + nickname + ": " + drawingPage.getMessageField().getText();
                    output.println(clientMsg);
                    output.flush();
                    drawingPage.getMessageField().setText("");
                }
            }
        }
    }
}
