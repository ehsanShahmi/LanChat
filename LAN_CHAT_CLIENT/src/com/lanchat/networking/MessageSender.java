package com.lanchat.networking;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;

import javax.swing.*;

import com.lanchat.gui.*;
import com.lanchat.main.UserInfo;

/**
 * @author plabon
 *
 */
public class MessageSender extends Thread implements ActionListener{
	
	private ControlPanel controlPanel;
	
	private String username;
	
	
	//Recreating some of the control panel's element here
	private Socket mainSocket;
	
	private JButton sendButton;
	private JButton sendFileButton;
	
	private JButton addFriend;
	private JButton profile;
	private JButton settings;
	
	
	private JTextArea displayArea;
	private JTextArea messageArea;
	
	private JList<String> onlineJList;
	private DefaultListModel<String> onlineJListModel;
	private HashMap <String,String> messages;
	
	private DataOutputStream outputStream;
	
	//not comitted
	private ObjectOutputStream objOut;

	private JButton signout;
	
	
	
	public MessageSender(ControlPanel _controlPanel){
		
		this.controlPanel=_controlPanel;
		mainSocket=controlPanel.getSocket();
		
		sendButton=controlPanel.getSendButton();
		sendButton.setActionCommand("Send");
		sendButton.addActionListener(this);
		
		sendFileButton=controlPanel.getSendFileButton();
		sendFileButton.setActionCommand("File");
		sendFileButton.addActionListener(this);
		
		addFriend=controlPanel.getAddFriend();
		addFriend.addActionListener(this);
		
		profile=controlPanel.getProfile();
		profile.addActionListener(this);
		
		signout=controlPanel.getSignout();
		signout.addActionListener(this);
		
		settings=controlPanel.getSettings();
		settings.addActionListener(this);
		
		displayArea=controlPanel.getDisplayArea();
		messageArea=controlPanel.getMessageArea();
		
		onlineJList=controlPanel.getOnlineJList();
		onlineJListModel=controlPanel.getonlineJListModel();
		
		outputStream=controlPanel.getOutputStream();
		username=controlPanel.getUsername();
		
		this.messages=controlPanel.getMessages();
		
	}
	
	boolean flag=true;
	
	@Override
	public void run() {		
		
		
		messageArea.addKeyListener(new KeyListener(){

			public void keyPressed(KeyEvent e) {
				
				if(e.getKeyCode()==KeyEvent.VK_ENTER){
					e.consume();
					sendButton.doClick();
				}
				
			}

			public void keyReleased(KeyEvent e) {}
			public void keyTyped(KeyEvent e) {}
		});
		
	}
	
	

	@Override
	public void actionPerformed(ActionEvent e) {
		
		if(e.getActionCommand().equals("Send")){
			
			String message=messageArea.getText();
			
			System.out.println(username+" entered: "+message);
			
			
			if(message.equals("")) return;
			
			String friend=onlineJList.getSelectedValue();
			
			
			if(message.startsWith("change")){
				controlPanel.backgroundChnage(message);
				return;
			}
			
			
			String actualMessage=username+" : "+message;
			
			String formattedMessage="TEXT_MSG>>>"+actualMessage+">>>"+friend+">>>"+username;
			
			String displayMessage=actualMessage+"\n";
			
			synchronized(this){
			try {
				outputStream.writeUTF(formattedMessage);
				
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			//displayArea.setText(displayArea.getText()+display);
			
			messages.put(friend,messages.get(friend)+displayMessage);
			
			displayArea.setText(messages.get(friend));
			
			messageArea.setText("");
			}//Sync end
		}
		
		else if(e.getActionCommand().equals("File")){
			
			System.out.println("Clicked Send File Button");
			
			String user=this.username;
			String friend=onlineJList.getSelectedValue();
			
			String req="FILE_REQ>>>"+username+">>>"+friend;
			
			try {
				outputStream.writeUTF(req);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
		}
		
		else if(e.getActionCommand().equals("add-friend")){
			
			System.out.println("Add button clicked");
			
			String all="FRND_REQ>>>IGNORE";
			try {
				outputStream.writeUTF(all);
				
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			
		}
		
		else if(e.getActionCommand().equals("profile")){
			
			System.out.println("Profile button clicked");
		}
		
		else if(e.getActionCommand().equals("signout")){
			
			System.out.println("Sigout button clicked");
			try {
				mainSocket.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			new LoginFrame();
			controlPanel.getParentFrame().setVisible(false);
			
			controlPanel.getParentFrame().dispose();
			
		}
		
		else if(e.getActionCommand().equals("settings")){
			
			new ThemeChangeFrame(controlPanel);
		}
	}

}
