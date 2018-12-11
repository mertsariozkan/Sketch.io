import javax.swing.*;
import java.awt.*;
public class CustomFrame extends JFrame {
    public CustomFrame(String pageTitle){
        super(pageTitle);
        setLayout(null);
        //GraphicsDevice deviceScreen = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()[0];
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //setUndecorated(false);
        setSize(1280,800);
        //deviceScreen.setFullScreenWindow(this);
    }
}
