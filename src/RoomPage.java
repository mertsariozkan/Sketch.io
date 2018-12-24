import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.SQLException;

public class RoomPage extends CustomFrame {
    public RoomPage(String username){

        super("Rooms");

        GridLayout gridLayout = new GridLayout(2,4);

        setLayout(gridLayout);
        for (int i = 0 ; i < 8; i++){
            int a = i;
            JButton button = new JButton("Room [" + (i+1) + "]");
            button.addActionListener(e -> {
                    Thread thread = new Thread(() -> {
                        try {
                            new Client("localhost", 3000 , username , a);
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        } catch (SQLException e1) {
                            e1.printStackTrace();
                        }
                    });
                    setVisible(false);
                    thread.start();
            });
            add(button);

        }

        setVisible(true);
    }
}
