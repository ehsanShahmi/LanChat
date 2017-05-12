package com.lanchat.gui;

import com.lanchat.networking.*;

import javax.swing.*;

import java.awt.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import com.lanchat.main.*;
/**
 * 
 * @author plabon
 *
 */

public class MainFrame extends JFrame{
	

	int WIDTH=900;
	int HEIGHT=675;
	
	private Socket socket;
	
	String username;
	
	
	public MainFrame(Socket socket,String username){
		//this.loginPanel=loginPanel;
		this.setTitle("Lan Chat");
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setVisible(true);
		this.setSize(new Dimension(WIDTH,HEIGHT));
		this.setMinimumSize(Main.minFrameSize);
		
		this.username=username;
		this.socket=socket;
		
		//this.loginFrame=loginPanel.getLoginFrame();	
		
		this.add(new ControlPanel(this,username));
						
	}



	public Socket getSocket() {
		return socket;
	}



	/**
	 * @return the loginFrame
	


	/**
	 * @return the inputStream
	 */
//	public DataInputStream getInputStream() {
//		return inputStream;
//	}
//
//
//
//	/**
//	 * @param inputStream the inputStream to set
//	 */
//	public void setInputStream(DataInputStream inputStream) {
//		this.inputStream = inputStream;
//	}



	
}
