//package com.exactprosystems.snakes;

import java.awt.*;
import java.awt.image.*;
import javax.swing.*;
import java.net.*;
import javax.imageio.ImageIO;

public class ClientPanel extends JPanel{
	private static final long serialVersionUID = 1L;
	private static final int DEFAULT_WIDTH = 800;
	private static final int DEFAULT_HEIGHT = 600;
	private BufferedImage buffer;
	private Graphics2D g2d;
	private boolean showGrid = true;
	private SnakeClient[] snakes;
	private Image grassPic;
	private AppleClient apple = new AppleClient();
	private boolean isPaused = false;
	
	public void setPaused(boolean paused){
		isPaused = paused;

		repaint();
	}
	
	public void setSnakes(String body0, String movement0, String body1, String movement1){
		if(snakes[0] != null) snakes[0].setSnake(body0, movement0);
		if(snakes[1] != null) snakes[1].setSnake(body1, movement1);
		repaint();
	}
	
	public void setApple(String pos){
		apple.setPos(pos);
		repaint();
	}
	
	public ClientPanel(){
		setPreferredSize(new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT));
		buffer = new BufferedImage(DEFAULT_WIDTH, DEFAULT_HEIGHT, BufferedImage.TYPE_INT_RGB);
		//buffer = new BufferedImage(DEFAULT_WIDTH, DEFAULT_HEIGHT, BufferedImage.TYPE_BYTE_GRAY);
		g2d = buffer.createGraphics();
		snakes = new SnakeClient[2];
		
		//grassPic = Toolkit.getDefaultToolkit().getImage("res\\grass.jpg");
		//snakes[0] = new SnakeClient(Toolkit.getDefaultToolkit().getImage("res\\snake1.png"));
		//snakes[1] = new SnakeClient(Toolkit.getDefaultToolkit().getImage("res\\snake2.png"));
		try{		
			grassPic = ImageIO.read(this.getClass().getResource("res/grass.jpg"));
			snakes[0] = new SnakeClient("res/red");
			snakes[1] = new SnakeClient("res/blue");
			
			/*snakes[0] = new SnakeClient(ImageIO.read(this.getClass().getResource("res/red.png")), 
				ImageIO.read(this.getClass().getResource("res/red_down.png")));
			snakes[1] = new SnakeClient(ImageIO.read(this.getClass().getResource("res/blue.png")), 
				ImageIO.read(this.getClass().getResource("res/blue_down.png")));*/
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void paint(Graphics g){
		//Draw Grass
		//g2d.setColor(Color.GREEN);
		//g2d.fillRect(0, 0, DEFAULT_WIDTH - 1, DEFAULT_HEIGHT - 1);
		//drawImage(Image img, AffineTransform xform, ImageObserver obs)

		if(isPaused){
			buffer = new BufferedImage(DEFAULT_WIDTH, DEFAULT_HEIGHT, BufferedImage.TYPE_BYTE_GRAY);
			g2d = buffer.createGraphics();
		}
		else{
			buffer = new BufferedImage(DEFAULT_WIDTH, DEFAULT_HEIGHT, BufferedImage.TYPE_INT_RGB);
			g2d = buffer.createGraphics();
		}
		
		g2d.drawImage(grassPic, 0, 0, null);
		apple.draw(g2d);
		snakes[0].draw(g2d);
		snakes[1].draw(g2d);
	
		if(showGrid) drawGrid();
		
		if(isPaused){
			g2d.setColor(Color.WHITE);
			g2d.setFont(new Font(Font.SERIF, Font.BOLD, 72));
			g2d.drawString("PAUSE", 100, 100);
		}		
		g.drawImage(buffer, 0, 0, this);
	}
	
	private void drawGrid(){
		g2d.setColor(Color.BLACK);
		for(int x = 50; x < DEFAULT_WIDTH; x += 50)
			g2d.drawLine(x, 0, x, 600);
		for(int y = 50; y < DEFAULT_HEIGHT; y += 50)
			g2d.drawLine(0, y, 800, y);
	}
}
