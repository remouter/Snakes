//package com.exactprosystems.snakes;

import javax.swing.*;
import java.net.*;
import java.io.*;
import javax.sound.sampled.AudioSystem;
import java.awt.*;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.*;
import java.awt.event.*;

// USER LOGIN SCREEN / host and port+++++++++++++
// Save Properties on EXIT and LOAD on start+++++++
// ASK for start new game+++++++++++++++++++++++

// Images add / Grass and Body and heads+++++++++
// Add music background++++++++++++++++++++++++++

// USER SLPASH SCREEN+++++++++++++++++++++++++++
// Server Start+++++++++++++++++++++++++++++++++
// Expections in Client Window++++++++++++++++++

public class Client extends JFrame implements Runnable{
	private static final long serialVersionUID = 1L;
	private static String HOST = "localhost";
	private static int PORT = 64444;
	private String name = "DefaultName";	
	private Socket socket;
	private BufferedReader in;
	private PrintWriter out;
	private ClientPanel gamePanel = new ClientPanel();
	private Clip clip;
	private JLabel score1, score2;
	
	public static void main(String[] args) throws MalformedURLException{
		new SplashWindow("res/splash.jpg");
		
		Client client = new Client();
		LoginDialog loginDialog = new LoginDialog(client, true);

		String name = loginDialog.getName();
		String HOST = loginDialog.getHost();
		int PORT = loginDialog.getPort();
		loginDialog.saveProps();
		client.initGUI(HOST, PORT, name);
	}
	
	public Client(){
		addKeyListener(new KeyAdapter(){
			@Override
			public void keyPressed(KeyEvent e){
				Message message = new Message("MOVE");
				boolean flag = false;
				switch(e.getKeyChar()){
					case 'a': flag = true; message.addParameter(name); message.addParameter("A"); break;
					case 'w': flag = true; message.addParameter(name); message.addParameter("W"); break;
					case 'd': flag = true; message.addParameter(name); message.addParameter("D"); break;
					case 's': flag = true; message.addParameter(name); message.addParameter("S"); break;
					case ' ': message = new Message("PAUSE"); flag = true; break;
					default: break;
				}
				if(flag) out.println(message);
			}
		});

		addWindowListener(new WindowAdapter(){
			@Override
			public void windowClosing(WindowEvent e){
				JOptionPane.showMessageDialog(null, "Client Close: " + name);
				close();
			}
		});
	}
	
	private void initGUI(String host, int port, String name){
		HOST = host;
		PORT = port;
		this.name = name;

		setTitle("Client");
		JLabel label = new JLabel("Client");
		label.setFont(new Font(Font.SERIF, Font.BOLD, 32));
		label.setOpaque(true);
		label.setForeground(Color.WHITE);
		label.setBackground(Color.BLACK);
		JPanel panel = new JPanel();
		panel.setBackground(Color.BLACK);
		panel.add(label);
		add(panel, BorderLayout.NORTH);	
		add(gamePanel, BorderLayout.CENTER);
		
		JPanel scorePanel = new JPanel();
		scorePanel.setLayout(new GridLayout(1, 2));
		score1 = new JLabel("Player1", SwingConstants.LEFT);
		score1.setFont(new Font(Font.SERIF, Font.BOLD, 16));
		score1.setOpaque(true);
		score1.setForeground(Color.WHITE);
		score1.setBackground(Color.RED);
		score2 = new JLabel("Player2", SwingConstants.RIGHT);
		score2.setFont(new Font(Font.SERIF, Font.BOLD, 16));
		score2.setOpaque(true);
		score2.setForeground(Color.WHITE);
		score2.setBackground(Color.BLUE);
		
		scorePanel.add(score1);
		scorePanel.add(score2);
		add(scorePanel, BorderLayout.SOUTH);
		
		setResizable(false);
		pack();
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setFocusable(true);	
		
		try {
			AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(this.getClass().getResource("res//8-bit Detective.wav"));
			clip = AudioSystem.getClip();
			clip.open(audioInputStream);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		new Thread(this).start();
	}
	
	public void setScore(String n1, String s1, String n2, String s2){
		score1.setText(n1 + ": " + s1);
		score2.setText(n2 + ": " + s2);
	}
	
	public void run(){
		try{
			socket = new Socket(HOST, PORT);
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new PrintWriter(socket.getOutputStream(), true);		
			
			Message msg = new Message("LOGIN");
			msg.addParameter(name);
			out.println(msg);			
			
			clip.loop(10);		
			
			boolean keepRunning = true;
			while(keepRunning){
				String input = in.readLine();				
				if(input == null) continue;

				Message message = new Message(input);
				String command = message.getActionCode(); 

				switch(command){
					case "PAUSEON":
						//JOptionPane.showMessageDialog(this, "PAUSE");
						//GRAYSCALE IMAGE
						gamePanel.setPaused(true);
						clip.stop();
						break;
					case "PAUSEOFF":
						clip.start();
						gamePanel.setPaused(false);
						break;
					case "SCORE":
						setScore(message.getParameter(0), message.getParameter(1), message.getParameter(2), message.getParameter(3));
						break;
					case "SERVERSTOP":
						JOptionPane.showMessageDialog(this, "SERVER STOPPED");
						close();
						break;				
					case "SERVERUNAVAILABLE":
						JOptionPane.showMessageDialog(this, "SERVER UNAVAILABLE");
						close();
						break;
					case "EXIT":
						JOptionPane.showMessageDialog(this, "See ya next time");
						System.exit(0);
						break;
					case "REJECT":
						name = JOptionPane.showInputDialog(this, "Name " + name + " is not Available. Choose another one:");
						msg = new Message("LOGIN");
						msg.addParameter(name);
						out.println(msg);
						break;
					case "ACCEPT":
						setTitle("Player: " + message.getParameter(0));
						this.name = message.getParameter(0);
						gamePanel.setApple(message.getParameter(1));
						
						if(message.getParameter(2).equals("1"))
							gamePanel.setSnakes(message.getParameter(3), message.getParameter(4), null, null);
						else
							gamePanel.setSnakes(message.getParameter(3), message.getParameter(4), message.getParameter(5), message.getParameter(6));
						break;
					case "UPDATE":
						gamePanel.setApple(message.getParameter(0));
						try{
							gamePanel.setSnakes(message.getParameter(1), message.getParameter(2), message.getParameter(3), message.getParameter(4));
						}
						catch(Exception e){
							gamePanel.setSnakes(message.getParameter(1), message.getParameter(2), null, null);
						}
						break;
					case "LOSE":
						try{
							clip.stop();
							AudioInputStream aStrean = AudioSystem.getAudioInputStream(this.getClass().getResource("res//winSound.wav"));
							Clip sound = AudioSystem.getClip();
							sound.open(aStrean);
							sound.start();
							new SplashWindow("res/lose.jpg");
							askForNewGame();
						}
						catch(Exception e){}
						break;
					case "WIN":
						try{
							clip.stop();
							AudioInputStream aStrean = AudioSystem.getAudioInputStream(this.getClass().getResource("res//winSound.wav"));
							Clip sound = AudioSystem.getClip();
							sound.open(aStrean);
							sound.start();
							new SplashWindow("res/win.jpg");
							askForNewGame();
						}
						catch(Exception e){}
						break;
					case "DRAW":
						try{
							clip.stop();
							AudioInputStream aStrean = AudioSystem.getAudioInputStream(this.getClass().getResource("res//winSound.wav"));
							Clip sound = AudioSystem.getClip();
							sound.open(aStrean);
							sound.start();
							new SplashWindow("res/lose.jpg");
							askForNewGame();
						}
						catch(Exception e){}
						break;
					case "RESTART":
						clip.loop(5);
						break;
					default: 
						JOptionPane.showMessageDialog(this, "Client-Default: " + message);
						break;
				}
			}
		}
		catch(SocketException e){
			e.printStackTrace();		
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	private void askForNewGame(){
		int choose = JOptionPane.showConfirmDialog(this, "Play Again?", "Play Again?", JOptionPane.YES_NO_OPTION);
		System.out.println(choose); //YES = 0 | NO = 1
		if(choose == 1) close();
		
		Message message = new Message("NEWGAME");
		message.addParameter(name);
		message.addParameter(String.valueOf(choose));
		out.println(message);
	}
	
	private void close(){
		Message msg = new Message("EXIT");
		msg.addParameter(name);
		out.println(msg);
		System.exit(0);
	}
}
