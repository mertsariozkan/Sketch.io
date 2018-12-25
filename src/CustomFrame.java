import javax.swing.*;
import java.awt.*;
public class CustomFrame extends JFrame {
    public CustomFrame(){
        super("Sketch.io");
        setLayout(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1280,800);
        setResizable(false);
    }
}
