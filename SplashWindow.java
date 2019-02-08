import java.awt.*;
import javax.swing.*;
import java.net.*;

public class SplashWindow{
	private static final int DELAY = 2000;
	private static final int IMAGE_W = 300;
	private static final int IMAGE_H = 200;
	
	public static void main(String[] args){
		new SplashWindow(null);
	}
	
	public SplashWindow(String str){
		JWindow window = new JWindow();
		URL url = this.getClass().getResource(str);
		ImageIcon imageIcon = new ImageIcon(url);		
		window.add(new JLabel("", imageIcon, SwingConstants.CENTER));
		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		window.setBounds((screenSize.width - IMAGE_W) / 2, (screenSize.height - IMAGE_H) / 2, IMAGE_W, IMAGE_H);
		window.setVisible(true);
		try{
			Thread.sleep(DELAY);
		}
		catch(InterruptedException e){e.printStackTrace();}
		window.setVisible(false);
		window.dispose();
	}
}
