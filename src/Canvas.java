import javax.swing.*;
import java.awt.*;

public class Canvas extends JPanel {
    public Canvas(int pageWidth , int pageHeight){
        setBounds(pageWidth/32 , pageHeight/16 , pageWidth*20/32 , pageHeight*12/16);
        setBackground(new Color(250 , 250,250));
    }
}
