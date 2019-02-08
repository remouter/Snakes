//package com.exactprosystems.snakes;

import java.util.*;
import javax.swing.*;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.*;

public class Game implements Runnable{
	private static final int GAMESPEED = 1000;
	private int availablePlayers = 0;
	private Random rand = new Random();		
	private Point2D apple = new Point2D((int)(Math.random() * 15) * 50, (int)(Math.random() * 11) * 50);	
	private HashMap<String, SnakeServer> snakes = new HashMap<String, SnakeServer>();
	private String[] names = new String[2];
	private boolean isPaused = false;
	private ArrayList<String> availablePoints = new ArrayList<String>();
	
	private void newAvailablePoints(){
		for(int i = 0; i < 16; i++)
			for(int j = 0; j < 12; j++){
				availablePoints.add("" + i * 50 + "," + j * 50);
			}
	}	

	public void pause(){
		Message message;
		if(isPaused) message = new Message("PAUSEOFF");
		else message = new Message("PAUSEON");
		isPaused = !isPaused;
		for(SnakeServer s : snakes.values())
				s.sendMessage(message);
	}
	
	public void removePlayer(String name){
		//JOptionPane.showMessageDialog(null, "Remove player: " + Arrays.toString(names));
		if(names[0].equals(name)) names[0] = null;
		else if(names[1].equals(name)) names[1] = null;
		--availablePlayers;
	}
	
	public void restart(String name, String value){
		if(value.equals("0")){
			//newAvailablePoints();
			int x = (rand.nextInt(12) + 2) * 50;
			int y = (rand.nextInt(8) + 2) * 50;
			
			availablePoints.remove("" + x + "," + y);
			snakes.get(name).setPos(x, y);
			snakes.get(name).initVelocity();
			names[availablePlayers] = name;
			availablePlayers++;
			Message message = new Message("RESTART");
			snakes.get(name).sendMessage(message);
		}
	}
	
	public boolean isHeadCollision(){// REMOVE??????????????????????????????????????????????????
		if(availablePlayers == 1) return false;
		
		if(snakes.get(0).getHead().equals(snakes.get(1))) return true;
		return false;
	}
	
	public boolean isNameAvailable(String newName){
		if(names[0] == null || names[1] == null || availablePlayers == 0) return true;
		
		if(names[0].equals(newName)) return false;
		else if(names[1].equals(newName)) return false;
		return true;
	}
	
	public void addPlayer(Connection connection, String name){
		if(name != null && connection != null){
			int x = (rand.nextInt(12) + 2) * 50;
			int y = (rand.nextInt(8) + 2) * 50;
			availablePoints.remove("" + x + "," + y); // remove point
						
			snakes.put(name, new SnakeServer(x, y, connection));
			names[availablePlayers] = name;
			availablePlayers++;
		}
	}
	
	public void sendScore(){
		Message message = new Message("SCORE");
		if(names[0] == null){
			message.addParameter("Player1");
			message.addParameter("Not Connected");
		}
		else{
			message.addParameter(names[0]);
			message.addParameter(snakes.get(names[0]).getLength());
		}
		
		if(names[1] == null){
			message.addParameter("Player2");
			message.addParameter("Not Connected");
		}
		else{
			message.addParameter(names[1]);
			message.addParameter(snakes.get(names[1]).getLength());
		}
	
		for(SnakeServer s : snakes.values())
			s.sendMessage(message);
	}
	
	public int getPlayersCount(){ return availablePlayers; }
	
	public Game(){
		newAvailablePoints();
		new Thread(this).start();
	}
	
	public String getSnake(String name){ return snakes.get(name).getSnake(); }
	public String getSnakeMovement(String name){ return snakes.get(name).getMovement(); }
	
	
	public Collection<SnakeServer> getSnakes(){
		return snakes.values();
	}	
	
	public String getApple(){ return apple.toString(); }
	
	public void setVelocity(String name, String velocity){
		switch(velocity){
			case "A": snakes.get(name).setVelocity(-50, 0); snakes.get(name).setMovement("LEFT"); break;
			case "D": snakes.get(name).setVelocity(50, 0);  snakes.get(name).setMovement("RIGTH"); break;
			case "S": snakes.get(name).setVelocity(0, 50);  snakes.get(name).setMovement("SOUTH"); break;
			case "W": snakes.get(name).setVelocity(0, -50);  snakes.get(name).setMovement("NORTH"); break;
			default: 
			break;
		}
	}
	
	private void addApple(){
		/*do{
			int x = (int)(Math.random() * 15) * 50;
			int y = (int)(Math.random() * 11) * 50;
			apple = new Point2D(x, y);
		}
		while(snakes.get(names[0]).getBody().contains(apple));*/
		
		Random rand = new Random();
		int value = rand.nextInt(availablePoints.size());
		
		String arr[] = availablePoints.get(value).split(",");
		int x = Integer.valueOf(arr[0]);
		int y = Integer.valueOf(arr[1]);
		
		apple = new Point2D(x, y);
		availablePoints.remove("" + x + "," + y); // remove point
	}
	
	private void gameUpdate(){
		//SNAKES MOVE
		for(SnakeServer snake : snakes.values())
			snake.move();

		//APPLE COLLISION
		for(SnakeServer snake : snakes.values()){
			if(apple != null && snake.isCollided(apple)){
				int x = apple.getX() - snake.getVelX();
				int y = apple.getY() - snake.getVelY();
				snake.growUp(new Point2D(x, y));
				
				//New Apple
				addApple();
				/*x = (int)(Math.random() * 15) * 50;
				y = (int)(Math.random() * 11) * 50;
				apple = new Point2D(x, y);*/
				
				try{
					AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(this.getClass().getResource("res\\chompSound.wav"));
					Clip chompSound = AudioSystem.getClip();
					chompSound.open(audioInputStream);
					chompSound.start();	
				}
				catch(Exception e){}	
			}
		}

		//Snakes SelfCollided & isBorderCollided Check
		if(availablePlayers < 2){
			if(names[0] != null && (snakes.get(names[0]).selfCollided() || snakes.get(names[0]).isBorderCollided())){
				availablePlayers = 0;				
				Message message = new Message("LOSE");
				snakes.get(names[0]).sendMessage(message);
				names[0] = null; // test!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! Name mark as unused
				return;
			}
			
			if(names[1] != null && (snakes.get(names[1]).selfCollided() || snakes.get(names[1]).isBorderCollided())){
				availablePlayers = 0;				
				Message message = new Message("LOSE");
				snakes.get(names[1]).sendMessage(message);
				names[1] = null; // test!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! Name mark as unused
				return;
			}
		}
		else{
			if(names[0] != null && (snakes.get(names[0]).selfCollided() || snakes.get(names[0]).isBorderCollided())){
				availablePlayers = 0;
				Message message = new Message("LOSE");
				snakes.get(names[0]).sendMessage(message);	
				message = new Message("WIN");
				snakes.get(names[1]).sendMessage(message);
				names[0] = null;
				names[1] = null;
				return;
			}

			if(names[1] != null && (snakes.get(names[1]).selfCollided() || snakes.get(names[1]).isBorderCollided())){
				availablePlayers = 0;
				Message message = new Message("LOSE");
				snakes.get(names[1]).sendMessage(message);	
				message = new Message("WIN");
				snakes.get(names[0]).sendMessage(message);
				names[0] = null;
				names[1] = null;
				return;
			}
		}

		//SNAKES COLLISION
		if(availablePlayers == 2){
			boolean snake1Col = false, snake2Col = false;
			if(snakes.get(names[0]).snakeCollision(snakes.get(names[1]))) snake1Col = true;
			if(snakes.get(names[1]).snakeCollision(snakes.get(names[0]))) snake2Col = true;
			
			Message messageTest = new Message("UPDATE");
			messageTest.addParameter(getApple());
			for(SnakeServer snake : snakes.values())
				messageTest.addParameter(snake.getSnake());

			for(SnakeServer snake : snakes.values())
				snake.sendMessage(messageTest);

			if(snake1Col && snake2Col){
				Message message = new Message("DRAW");
				snakes.get(names[0]).sendMessage(message);
				snakes.get(names[1]).sendMessage(message);
				names[0] = null;
				names[1] = null;
				return;
			}
			if(snake1Col){
				Message message = new Message("LOSE");
				snakes.get(names[0]).sendMessage(message);	
				message = new Message("WIN");
				snakes.get(names[1]).sendMessage(message);
				names[0] = null;
				names[1] = null;
				return;
			}
			if(snake2Col){
				Message message = new Message("LOSE");
				snakes.get(names[1]).sendMessage(message);	
				message = new Message("WIN");
				snakes.get(names[0]).sendMessage(message);
				names[0] = null;
				names[1] = null;
				return;
			}
		}
	}
	
	public void run(){
		boolean keepRunning = true;
		while(keepRunning){
			try{
				Thread.sleep(GAMESPEED);
				if(availablePlayers > 0 && !isPaused){
					gameUpdate();				
					sendScore();				
					
					//Send Update
					Message message = new Message("UPDATE");
					message.addParameter(getApple());
					
					if(names[0] != null){
						message.addParameter(snakes.get(names[0]).getSnake());
						message.addParameter(snakes.get(names[0]).getMovement());
					}
					if(names[1] != null){
						message.addParameter(snakes.get(names[1]).getSnake());
						message.addParameter(snakes.get(names[1]).getMovement());
					}
					
					if(names[0] != null) snakes.get(names[0]).sendMessage(message);
					if(names[1] != null) snakes.get(names[1]).sendMessage(message);
				}
			}
			catch(InterruptedException e){ e.printStackTrace(); }
		}
	}
}
