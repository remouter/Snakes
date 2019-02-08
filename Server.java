//package com.exactprosystems.snakes;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.net.*;
import java.time.*;
import java.time.format.*;
import java.util.*;

public class Server extends JFrame implements Runnable{
	private static final long serialVersionUID = 1L;
	private static final int PORT = 64444;
	private ServerSocket serverSocket;
	private JTextArea textArea;
	private JButton button = new JButton("Start");
	private boolean serverRunning = false;
	private Game game;
	private boolean keepRunning;
	//private static int availablePlayers = 0;
	//private Set<String> names = new TreeSet<String>();
	private Thread thread;
	private ArrayList<Connection> connections = new ArrayList<Connection>();
	
	public Server(){
		initGUI();
		
		thread = new Thread(this);
		thread.start();
		//new Thread(this).start();
		log("Server created");
	}
	
	private void initGUI(){
		JLabel label = new JLabel("Server");
		label.setForeground(Color.WHITE);
		label.setBackground(Color.BLACK);
		label.setOpaque(true);
		label.setFont(new Font(Font.SERIF, Font.BOLD, 32));
		JPanel topPanel = new JPanel();
		topPanel.setBackground(Color.BLACK);
		topPanel.add(label);
		add(topPanel, BorderLayout.NORTH);
		
		textArea = new JTextArea(20, 25);
		textArea.setEditable(false);
		JScrollPane scrollPane = new JScrollPane(textArea);
		add(scrollPane, BorderLayout.CENTER);
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.add(button);
		add(buttonPanel, BorderLayout.SOUTH);
		
		button.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				serverRunning = !serverRunning;
				if(serverRunning){
					log("Server started");
					button.setText("Stop");
					game = new Game();
				}
				else{
					log("Server stopped");
					button.setText("Start");
					thread = null;
					game = null;
					for(Connection c : connections){
						Message message = new Message("SERVERSTOP");
						c.sendMessage(message);
						//c.sendMessage("SERVERSTOP");
					}
				}				
			}
		});
		
		pack();
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
	}
	
	public static void main(String[] args){
		SwingUtilities.invokeLater(new Runnable(){
			public void run(){
				new Server();
			}
		});
	}
	
	public void run(){
		keepRunning = true;
		try{
			serverSocket = new ServerSocket(PORT);
			while(thread != null){
				//if(serverRunning){
					Socket socket = serverSocket.accept();
					Connection connection = new Connection(this, socket, game);
					connections.add(connection);
					log("Client connected");
					System.out.println("Client connected");
				//}
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		finally{}
	}
	
	public void log(String str){
		LocalDateTime time = LocalDateTime.now();
		DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy/MM/dd hh:mm:ss.SSS");
		String tmp = time.format(format);
		textArea.append(tmp + ": " + str + "\n");
	}
}