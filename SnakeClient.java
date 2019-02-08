//package com.exactprosystems.snakes;

import java.awt.*;
import javax.imageio.ImageIO;
//import java.awt.geom.AffineTransform;;

public class SnakeClient{
	private String body = null;
	private Image picture, head, head_left, head_right, head_top, head_down;
	//private AffineTransform at = new AffineTransform();
	
	public SnakeClient(String color){
		try{
			//"res/red" + "_down.png"
			this.picture = ImageIO.read(this.getClass().getResource(color + ".png"));
			this.head = ImageIO.read(this.getClass().getResource(color + "_down.png"));
			this.head_left = ImageIO.read(this.getClass().getResource(color + "_left.png"));
			this.head_right = ImageIO.read(this.getClass().getResource(color + "_right.png"));
			this.head_top = ImageIO.read(this.getClass().getResource(color + "_top.png"));
			this.head_down = ImageIO.read(this.getClass().getResource(color + "_down.png"));
		}
		catch(Exception e){ e.printStackTrace(); }
		//at.rotate(Math.toRadians(180));
		//at.scale(-50, -50);
		//at.translate(50, 50);
		//at.rotate(90);
	
	}
	
	public void setSnake(String body, String movement){
		try{
			switch(movement){
				case "LEFT": head = head_left; break;
				case "RIGTH": head = head_right; break;
				case "NORTH": head = head_top; break;
				case "SOUTH": head = head_down; break;
				default: break;
			}			
		}
		catch(Exception e){
		
		}
		//at.rotate(Math.toRadians(180));		
		//at.rotate(0.5);		
		this.body = body;
	}	
	
	public void draw(Graphics2D g2d){
		if(body == null) return;
		String arr[] = body.split(",");
		if(arr.length == 1) return;
		
		
		g2d.drawImage(head, Integer.valueOf(arr[0]), Integer.valueOf(arr[1]), null);
		//at.rotate(Math.toRadians(90));
		//at.translate(Integer.valueOf(arr[0]), Integer.valueOf(arr[1]));
		//g2d.drawImage(head, at, null);
		//at.setToIdentity();
		
		for(int i = 2; i < arr.length; i += 2)
			//g2d.fillRect(Integer.valueOf(arr[i]), Integer.valueOf(arr[i + 1]), DEFAULT_WIDTH, DEFAULT_HEIGHT);	//Shape
			g2d.drawImage(picture, Integer.valueOf(arr[i]), Integer.valueOf(arr[i + 1]), null);	//Image
	}
}