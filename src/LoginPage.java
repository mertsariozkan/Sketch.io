import javax.swing.*;
import java.awt.*;

public class LoginPage extends CustomFrame {
    public LoginPage() {
        super();
        int pageWidth = getWidth();
        int pageHeight = getHeight();
        setLayout(null);

        JLabel mainTitle = new JLabel("SKETCH.IO");
        mainTitle.setFont(new Font("Comic Sans MS", Font.BOLD, pageHeight * 11 / 80));
        mainTitle.setBounds(pageWidth / 4, pageHeight / 8, pageWidth / 2, pageHeight / 4);

        JButton addWordButton = new JButton("New Question Word");
        addWordButton.setBounds(pageWidth * 5 / 7, pageHeight * 5 / 6, pageWidth / 4, pageHeight / 16);
        addWordButton.addActionListener(e -> {
            String question = JOptionPane.showInputDialog(this, "Add new question word");
            if (!question.equals("")) {
                question = question.toUpperCase();
                DatabaseOperations databaseOperations = new DatabaseOperations();
                databaseOperations.addQuestionWord(question);
                databaseOperations.closeConnection();
            }
        });

        JTextField nicknameText = new JTextField();
        nicknameText.setBounds(pageWidth * 3 / 8, pageHeight / 2, pageWidth / 4, pageHeight / 16);

        JLabel warning = new JLabel();
        warning.setBounds(pageWidth * 3 / 7, pageHeight * 18 / 32, pageWidth / 4, pageHeight / 16);

        JButton loginButton = new JButton("Login");
        getRootPane().setDefaultButton(loginButton);
        loginButton.setBounds(pageWidth * 7 / 16, pageHeight * 20 / 32, pageWidth / 8, pageHeight / 16);
        loginButton.addActionListener(e -> {
            String username = nicknameText.getText();
            if (!username.equals("")) {
                setVisible(false);
                new RoomPage(username);
            } else {
                warning.setText("Please enter a nickname!");
            }
        });

        add(nicknameText);
        add(warning);
        add(mainTitle);
        add(loginButton);
        add(addWordButton);
        setVisible(true);
    }
}
