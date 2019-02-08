//package com.exactprosystems.snakes;

import java.awt.*;
import java.net.*;
import javax.imageio.ImageIO;


public class AppleClient{
	private String pos;
	private URL url;
	//private static Image picture = Toolkit.getDefaultToolkit().getImage("res\\apple.png");
	private static Image picture;
	
	public AppleClient(){
		try{
			picture = ImageIO.read(this.getClass().getResource("/res/apple.png"));
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void setPos(String pos){ this.pos = pos; }
	
	public void draw(Graphics2D g2d){		
		if(pos == null) return;
		String arr[] = pos.split(",");
		
		//SHAPES
		/*g2d.setColor(Color.RED);		
		g2d.fillRect(Integer.valueOf(arr[0]), Integer.valueOf(arr[1]), DEFAULT_WIDTH, DEFAULT_HEIGHT);
		*/	
		g2d.drawImage(picture, Integer.valueOf(arr[0]), Integer.valueOf(arr[1]), null);
	}
}