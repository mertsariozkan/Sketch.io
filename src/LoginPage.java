import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.sql.SQLException;

public class LoginPage extends CustomFrame {
    public LoginPage(String pageTitle) {
        super(pageTitle);

        int pageWidth = getWidth();
        int pageHeight = getHeight();
        setLayout(null);

        // Creating main title label.
        JLabel mainTitle = new JLabel("SKETCH.IO");
        mainTitle.setFont(new Font("Comic Sans MS" , Font.BOLD , pageHeight*11/80));
        mainTitle.setBounds(pageWidth/4 , pageHeight/8 , pageWidth/2 , pageHeight/4);


        // Creating nickName field.
        JTextField nicknameText = new JTextField();
        nicknameText.setBounds(pageWidth*3/8 , pageHeight/2 , pageWidth/4 , pageHeight/16);

        // Creating login button.
        JButton loginButton = new JButton("Login");
        getRootPane().setDefaultButton(loginButton);
        loginButton.setBounds(pageWidth*7/16 , pageHeight*19/32 , pageWidth/8 , pageHeight/16);
        loginButton.addActionListener(e -> {
            String username = nicknameText.getText();
                Thread thread = new Thread(() -> {
                    try {
                        new Client("localhost", 3000, username);
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    } catch (SQLException e1) {
                        e1.printStackTrace();
                    }
                });

            setVisible(false);
            thread.start();
        });

        add(nicknameText);
        add(mainTitle);
        add(loginButton);
        setVisible(true);
    }
}
