package com.lanchat.networking;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;

import com.lanchat.gui.*;

/**
 * @author plabon
 *
 */
public class MessageReceiver extends Thread{
	
	private ControlPanel controlPanel;
	private Socket mainSocket;
	
	private DataInputStream inputStream;
	private JTextArea displayArea;
	private ObjectInputStream objIn;
	private ArrayList<String> onlineArrayList;
	private HashMap <String,String> messages;
	private JList<String> onlineJList;
	private DefaultListModel<String> onlineJListModel;
	private String username;
	
	
	public  MessageReceiver(ControlPanel _controlPanel){
		
		this.controlPanel=_controlPanel;
		this.username=controlPanel.getUsername();
		
		displayArea=controlPanel.getDisplayArea();
		mainSocket=controlPanel.getSocket();
		this.onlineArrayList=controlPanel.getOnlineArrayList();
		this.messages=controlPanel.getMessages();
		
		onlineJList=controlPanel.getOnlineJList();
		onlineJListModel=controlPanel.getonlineJListModel();
		
		
		try {
			objIn=new ObjectInputStream(mainSocket.getInputStream());
			inputStream=new DataInputStream(mainSocket.getInputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

	@Override
	public void run() {
		
		//Receiving onlineList
		
		try {
			Object obj=objIn.readObject();
			
//			String onfl=inputStream.readLine();
//			String s[]=onfl.split(">>>");
//			
//			System.out.println(s);
//			
//			ArrayList <String> al=new ArrayList<String>(); 
//			
//			int i=0;
//			for(i=2;i<s.length;i++){
//				al.add(s[i]);
//			}
//			
//			onlineArrayList=al;
			
			
			onlineArrayList=(ArrayList<String>)obj;
			controlPanel.updateFriendList(onlineArrayList);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		while(true){
				
			
			System.out.println("Receiver Running");
			try {
				
				//String msg=inputStream.readUTF();
				@SuppressWarnings("deprecation")
				String msg=inputStream.readLine();
				System.out.println("Received:"+msg);
				
				System.out.println();
				
				String msgs[]=msg.split(">>>");
				
				if(msgs[1].equals("TEXT_MSG")){///Text message arrived
					
					String actualMessage=msgs[2];
					String from=msgs[3];
					//displayArea.setText(displayArea.getText()+actualMessage+" from "+from+"\n");
					
					
					messages.put(from, messages.get(from)+actualMessage+"\n");
					
					String activeUser=onlineJList.getSelectedValue();
					
					displayArea.setText(messages.get(activeUser));
				
				}
				
				else if(msgs[1].equals("FRIEND_JOIN")){ //new Friend Join Online
					
					//JOptionPane.showMessageDialog(null,msgs[2]+" just joined online!");
					
					onlineArrayList.add(msgs[2]);
					controlPanel.updateFriendList(onlineArrayList);
					
				}
				
				else if(msgs[1].equals("FRIEND_LEAVE")){ //new Friend Join Online
					
					//JOptionPane.showMessageDialog(null,msgs[2]+" just went offline!");
					System.out.println("Removing : "+msgs[2]);
					onlineArrayList.remove(msgs[2]);
					controlPanel.updateFriendList(onlineArrayList);
					
				}
				
				if(msgs[1].equals("FILE_SEND")){ //new Friend Join Online
					
					String IP=msgs[2];
					String receiver=msgs[3];
					
					new FileSender(controlPanel,IP,receiver).start();
					
				}
				if(msgs[1].equals("FLIST")){
					
					
					new FriendRequestFrame(username,mainSocket,controlPanel,msg);
				}
				
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		}
		
	}

}
