import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class DrawPage extends CustomFrame {
    private JPanel canvas;
    private JTextArea chatArea;
    private JButton sendButton;
    private JTextArea messageField;
    private DefaultTableModel tableModel;
    private JLabel timeCounter;
    private JLabel questionLabel;
    private JButton passWordButton;

    public DrawPage() {
        super();
        getRootPane().setDefaultButton(sendButton);
        int pageHeight = getHeight();
        int pageWidth = getWidth();

        canvas = new Canvas(pageWidth, pageHeight);

        JLabel innerTitle = new JLabel("S       K       E       T       C       H       .       I       O");
        innerTitle.setFont(new Font("Comic Sans MS", Font.PLAIN, pageWidth / 34));
        innerTitle.setBounds(pageWidth * 11 / 64, pageHeight / 200, pageWidth * 26 / 32, pageHeight / 20);

        chatArea = new JTextArea();
        chatArea.setBounds(pageWidth * 11 / 16, pageHeight / 16, pageWidth * 2 / 7, pageHeight * 13 / 32);
        chatArea.setFocusable(false);
        chatArea.setBorder(BorderFactory.createMatteBorder(1, 1, 0, 1, Color.lightGray));

        JPanel messageAreaPanel = new JPanel(new BorderLayout());
        messageAreaPanel.setBounds(pageWidth * 11 / 16, pageHeight * 15 / 32, pageWidth * 2 / 7, pageHeight / 8);
        messageAreaPanel.setBorder(BorderFactory.createMatteBorder(3, 1, 1, 1, Color.lightGray));

        sendButton = new JButton("Send");

        messageField = new JTextArea();


        String[] columnNames = {"Player Name", "Score", "Turn"};
        tableModel = new DefaultTableModel(columnNames, 0);
        JTable clientTable = new JTable(tableModel);
        clientTable.setEnabled(false);

        timeCounter = new JLabel("30");
        timeCounter.setFont(new Font("Comic Sans MS", Font.PLAIN, pageWidth / 24));
        timeCounter.setBounds(pageWidth / 28, pageHeight * 27 / 32, pageWidth / 7, pageHeight / 16);

        JPanel tableHolder = new JPanel();
        tableHolder.setBounds(pageWidth * 11 / 16, pageHeight * 10 / 16, pageWidth * 2 / 7, pageHeight * 3 / 16);
        tableHolder.setLayout(new BorderLayout());
        tableHolder.add(clientTable.getTableHeader(), BorderLayout.PAGE_START);
        tableHolder.add(clientTable, BorderLayout.CENTER);

        passWordButton = new JButton("PASS !");
        passWordButton.setBounds(pageWidth * 16 / 31, pageHeight * 27 / 32, pageWidth * 1 / 7, pageHeight / 16);
        messageAreaPanel.add(sendButton, BorderLayout.EAST);
        messageAreaPanel.add(messageField, BorderLayout.CENTER);

        questionLabel = new JLabel("");
        questionLabel.setFont(new Font("Comic Sans MS", Font.PLAIN, pageWidth / 36));
        questionLabel.setBounds(pageWidth * 3 / 16, pageHeight * 27 / 32, pageWidth * 2 / 7, pageHeight / 16);

        add(questionLabel);
        add(timeCounter);
        add(tableHolder);
        add(passWordButton);
        add(innerTitle);
        add(messageAreaPanel);
        add(canvas);
        add(chatArea);

        setVisible(true);
    }

    public JPanel getCanvas() {
        return canvas;
    }

    public JTextArea getChatArea() {
        return chatArea;
    }

    public JButton getSendButton() {
        return sendButton;
    }

    public JTextArea getMessageField() {
        return messageField;
    }

    public DefaultTableModel getTableModel() {
        return tableModel;
    }

    public JLabel getTimeCounter() {
        return timeCounter;
    }

    public JLabel getQuestionLabel() {
        return questionLabel;
    }

    public JButton getPassWordButton() {
        return passWordButton;
    }

}
