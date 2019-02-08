//package com.exactprosystems.snakes;

import java.net.*;
import java.io.*;
import javax.swing.*;

public class Connection implements Runnable{
	private Server server;
	private Game game;
	private BufferedReader in;
	private PrintWriter out;
	private Socket socket;
	
	public Connection(Server server, Socket socket, Game game){
		this.server = server;
		this.socket = socket;
		this.game = game;
		new Thread(this).start();
	}
	
	public void run(){
		try{
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new PrintWriter(socket.getOutputStream(), true);
			if(game == null){
				Message message = new Message("SERVERUNAVAILABLE");
				sendMessage(message);
				System.exit(0);
			}
			
			//sendToClient("Submit1");
			
			boolean keepRunning = true;
			while(keepRunning){
				String str = in.readLine();
				if(str != null) server.log(str);

				Message msg = new Message(str);
				String command = msg.getActionCode();
				
				switch(command){						
					case "EXIT":
						//server.log(msg.getParameter(0) + " Exit");
						game.removePlayer(msg.getParameter(0));
						break;
					case "NEWGAME":
						//JOptionPane.showMessageDialog(null, "Connection - NEWGAME");
						game.restart(msg.getParameter(0), msg.getParameter(1));			
						break;
					case "LOGIN":
						//sendToClient("Name Check");
						String name = msg.getParameter(0);
						//boolean flag = server.nameCheck(name);
						boolean flag = game.isNameAvailable(name);
						//JOptionPane.showMessageDialog(null, "Connection-isNameAvailable: " + flag);
						if(flag){
							game.addPlayer(this, name);
							Message message = new Message("ACCEPT");
							message.addParameter(name);
							message.addParameter(game.getApple());														
							message.addParameter(String.valueOf(game.getPlayersCount()));
							if(game.getPlayersCount() == 1){
								message.addParameter(game.getSnake(name));
								message.addParameter(game.getSnakeMovement(name));							
							}
							else{
								for(SnakeServer snake : game.getSnakes()){
									message.addParameter(snake.toString());
									message.addParameter(snake.getMovement());
								}
							}
							sendMessage(message);
							//JOptionPane.showMessageDialog(null, "Connection-ACCEPT: " + message);
						}
						else{
							Message message = new Message("REJECT");
							message.addParameter(name);
							sendMessage(message);
						}
						break;
					case "MOVE":
						game.setVelocity(msg.getParameter(0), msg.getParameter(1));
						break;
					case "PAUSE":
						game.pause();
						break;
					default: 
						JOptionPane.showMessageDialog(null, "Connection-default: " + msg);
						break;
				}
			}
		}
		catch(Exception e){ e.printStackTrace(); }
	}
	
	public void sendToClient(String str){
		out.println(str);
		server.log(str);
	}

	public void sendMessage(Message message){
		out.println(message);
	}
}