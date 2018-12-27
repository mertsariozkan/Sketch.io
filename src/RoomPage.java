import org.w3c.dom.css.RGBColor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class RoomPage extends CustomFrame {
    BufferedReader input;
    PrintWriter output;
    int[] roomStates;
    ArrayList<JLabel> occupancyLabels;
    ArrayList<JButton> joinButtons;
    ArrayList<RoomPanel> roomPanels;


    public RoomPage(String username) {
        super();
        occupancyLabels = new ArrayList<>();
        joinButtons = new ArrayList<>();
        roomPanels = new ArrayList<>();

        setLayout(new BorderLayout());
        roomStates = new int[8];
        try {
            Socket socket = new Socket("localhost",3000);
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            output = new PrintWriter(socket.getOutputStream());
            getRoomInfo();
        } catch (IOException e) {
            e.printStackTrace();
        }
        GridLayout gridLayout = new GridLayout(2, 4);
        JPanel rootPanel = new JPanel(gridLayout);

        RoomPanel roomPanel;
        for (int i = 0; i < 8; i++) {
            int a = i;
            JPanel parentPanel = new JPanel(new BorderLayout());
            JButton joinButton = new JButton("Join");

            joinButton.addActionListener(e -> {
                Thread thread = new Thread(() -> {
                    new Client("localhost", 3000, username, a);
                });
                setVisible(false);
                thread.start();
            });

            joinButtons.add(joinButton);
            if (roomStates[i] == 3){
                joinButton.setEnabled(false);
                roomPanel = new RoomPanel("doorclosed.png");
                roomPanels.add(roomPanel);

            }
            else {
                roomPanel = new RoomPanel("dooropen.png");
                roomPanels.add(roomPanel);
            }


            JLabel roomLabel = new JLabel("Room [" + (i + 1) + "]");

            roomPanel.add(roomLabel);
            roomPanel.add(joinButton);
            parentPanel.add(roomPanel , BorderLayout.CENTER);
            JLabel roomCapacity = new JLabel("Occupancy: " +roomStates[i] + "/3");
            occupancyLabels.add(roomCapacity);

            parentPanel.add(roomCapacity , BorderLayout.NORTH);
            parentPanel.setBorder(BorderFactory.createLineBorder(Color.ORANGE , 2));
            rootPanel.add(parentPanel);
        }

        JButton refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(e -> {
            getRoomInfo();
            for (int i = 0; i < 8; i++) {
                occupancyLabels.get(i).setText("Occupancy: " +roomStates[i] + "/3");
                if(roomStates[i]==3) {
                    roomPanels.get(i).setImage("doorclosed.png");
                    joinButtons.get(i).setEnabled(false);
                }
                else {
                    roomPanels.get(i).setImage("dooropen.png");
                    joinButtons.get(i).setEnabled(true);
                }
            }
        });

        add(rootPanel,BorderLayout.CENTER);
        add(refreshButton, BorderLayout.SOUTH);
        setVisible(true);

    }

    public void getRoomInfo() {
        output.println("$gR");
        output.flush();
        String roomInfo = null;
        try {
            if((roomInfo=input.readLine())!=null) {
                System.out.println("CCCCC");
                roomInfo = roomInfo.substring(1,roomInfo.length()-1);
                roomInfo = roomInfo.replaceAll("\\s","");
                String[] roomStringParse = roomInfo.split(",");
                for(int i=0;i<roomStringParse.length;i++) {
                    roomStates[i] = Integer.valueOf(roomStringParse[i]);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
