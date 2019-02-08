//package com.exactprosystems.snakes;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.Properties;
import java.io.*;

//public class LoginDialog extends JFrame{
public class LoginDialog extends JDialog{
	private static final long serialVersionUID = 1L;
	private static final int DEFAULT_WIDTH = 120;
	private static final int DEFAULT_HEIGHT = 220;
	private JTextField nameField, hostField, portField;
	private Properties prop = new Properties();
	private String name, host, port;
	private FileInputStream in;
	
	public LoginDialog(Frame owner, boolean modal){
		super(owner, modal);
		try{
			in = new FileInputStream(new File("test.test"));
			prop.load(in);
			
			name = prop.getProperty("Name", "Default");
			host = prop.getProperty("Host", "localhost");
			port = prop.getProperty("Port", "64444");
		}
		catch(FileNotFoundException e){ 
			name = "Default";
			host = "localhost";
			port = "64444";
			e.printStackTrace(); 
		}
		catch(IOException e){ e.printStackTrace(); }
		finally{
			try{
				in.close();
			}
			catch(Exception e){}
		}
		
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation((dim.width / 2  - DEFAULT_WIDTH), (dim.height / 2 - DEFAULT_HEIGHT));
		setTitle("Login");
		setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
		setLayout(new FlowLayout());			
		JLabel nameLabel = new JLabel("Player: ");
		nameField = new JTextField(10);
		nameField.setText(name);
		add(nameLabel);
		add(nameField);
		
		JLabel hostLabel = new JLabel("Host");
		hostField = new JTextField(10);
		hostField.setText(host);
		add(hostLabel);
		add(hostField);
		
		JLabel portLabel = new JLabel("Port");
		portField = new JTextField(10);
		portField.setText(port);
		add(portLabel);
		add(portField);
		
		JButton button = new JButton("Connect");
		
		getRootPane().setDefaultButton(button);
		button.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				setVisible(false);
			}
		});
		add(button);				
		
		setResizable(false);
		setVisible(true);
		pack();
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		
		addWindowListener(new WindowAdapter(){
			@Override
			public void windowClosing(WindowEvent we){
				saveProps();
			}
		});
	}
	
	public void saveProps(){
		prop.setProperty("Name", nameField.getText());
		prop.setProperty("Host", hostField.getText());
		prop.setProperty("Port", portField.getText());
		try{
			FileOutputStream out = new FileOutputStream(new File("test.test"));
			prop.store(out, "TEST");
			out.close();
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public String getName(){ return nameField.getText(); }
	public String getHost(){ return hostField.getText(); }
	public int getPort(){ return Integer.parseInt(portField.getText()); }
	//public boolean isClicked(){ return CLICKED; }
}