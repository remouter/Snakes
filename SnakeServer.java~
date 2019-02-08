//package com.exactprosystems.snakes;

import java.util.*;


public class SnakeServer{
	private static final int DEFAULT_X = 50;
	private static final int DEFAULT_Y = 0;
	private int velocityX, velocityY;
	private ArrayList<Point2D> body = new ArrayList<>();
	private Connection connection;
	private Random rand = new Random();
	private String movement;

	
	public String getMovement(){
		return movement;
	}
	
	public void setMovement(String movement){
		this.movement = movement;
	}
	
	public SnakeServer(int posX, int posY, Connection connection){
		this.connection = connection;
		initVelocity();
		this.body.add(new Point2D(posX, posY));
	}
	
	public Point2D getHead(){
		return body.get(0);
	}
	
	// public void sendToClient(String message){
		// connection.sendToClient(message);
	// }
	
	public void sendMessage(Message message){
		connection.sendMessage(message);
	}
	
	public int getVelX(){ return velocityX; }
	public int getVelY(){ return velocityY; }
	
	public void setPos(int posX, int posY){
		body = new ArrayList<>();
		body.add(new Point2D(posX, posY));
	}

	public void initVelocity(){
		/*this.velocityX  = (rand.nextInt(2) - 1) * 50;				
		if(Math.abs(this.velocityX) == 50) 
			this.velocityY = 0;
		else if(rand.nextInt(2) % 2 == 0) this.velocityY = -50;
		else this.velocityY = 50;*/
		this.velocityX = 0;
		this.velocityY = 50;
	}
	
	public String getSnake(){ 
		String tmp = body.get(0).toString();
		for(int i = 1; i < body.size(); i++)
			tmp += "," + body.get(i).toString();
		return tmp;
	}
	
	public void setVelocity(int x, int y){
		velocityX = x;
		velocityY = y;
	}
	
	public void move(){
		if(body.size() == 1){
			body.get(0).setX(body.get(0).getX() + velocityX);
			body.get(0).setY(body.get(0).getY() + velocityY);
		}
		else{
			int headX = body.get(0).getX() + velocityX;
			int headY = body.get(0).getY() + velocityY;
			
			body.add(0, new Point2D(headX, headY));
			body.remove(body.size() - 1);
		}
	}
	
	public boolean isBorderCollided(){
		if(body.get(0).getX() < 0 || body.get(0).getX() > 750) return true;
		if(body.get(0).getY() < 0 || body.get(0).getY() > 550) return true;
		return false;
	}
	
	public boolean isCollided(Point2D point){
		return body.get(0).equals(point);
	}
	
	public void growUp(Point2D point){
		body.add(point);
	}
	
	public boolean selfCollided(){
		Point2D head = body.get(0);
		for(int i = 1; i < body.size(); i++){
			if(head.equals(body.get(i))) return true;
		}
		return false;
	}
	
	public ArrayList<Point2D> getBody(){
		return body;
	}
	
	public boolean snakeCollision(SnakeServer other){
		Point2D head = body.get(0);
		for(Point2D p : other.getBody())
			if(head.equals(p)) return true;
		return false;
	}
	
	public String getLength(){
		return String.valueOf(body.size() - 1);
	}
}
