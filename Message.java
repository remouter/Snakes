//package com.exactprosystems.snakes;

import java.util.*;

public class Message{
	private String actionCode;
	private ArrayList<String> parameters = new ArrayList<String>();	

	public Message(String str){
		if(str.length() == 0 || str == null) return;

		String arr[] = str.split(";");
		actionCode = arr[0];
		for(int i = 1; i < arr.length; i++)
			parameters.add(arr[i]);
	}

	public void addParameter(String str){
		parameters.add(str);
	}

	public String toString(){
		String tmp = actionCode;
		for(String s : parameters)
			tmp += ";" + s;
		return tmp;
	}

	public String getActionCode(){ return actionCode; }
	public String getParameter(int index){ return parameters.get(index); }
}