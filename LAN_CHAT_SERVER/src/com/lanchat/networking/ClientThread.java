package com.lanchat.networking;

import java.net.*;
import java.io.*;
import java.util.*;

import com.lanchat.database.DatabaseManager;
import com.lanchat.database.UserInfo;


public class ClientThread extends Thread {

	
	private Socket socket;
	private String user;
	
	DataInputStream inputStream=null;
	DataOutputStream outputStream=null;
	
	public PrintStream os = null;
	
	private boolean signingUp=false;
	
	
	private ObjectInputStream objIn;
	
	private ObjectOutputStream objOut;
	
	private Server server;
	
	private ArrayList <ClientThread> clientThreads;
	private ArrayList <String> onlineOnServer;
	
	private ArrayList <String> onlineList=null;;
	private ArrayList <String> friendList;
	
	private DatabaseManager dbms;
	
	public ClientThread(Socket socket,String username, Server server,boolean signingUp){
		
		
		
		this.server=server;
		this.socket=socket;
		System.out.println("Connected to: "+socket.getRemoteSocketAddress());

		dbms=new DatabaseManager();
		
		this.clientThreads=server.getClientThreads();
		this.onlineOnServer=server.getTotalOnline();
		this.user=username;
		this.signingUp=signingUp;
		
		this.onlineList=new ArrayList<String>();
		this.friendList=new ArrayList<String>();
		
		onlineList=dbms.getOnlineList(user);
		friendList=dbms.getFriends(user);
		
		
		
		try {
			inputStream=new DataInputStream(socket.getInputStream());
			os=new PrintStream(socket.getOutputStream());
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	
		
		try {
			objOut=new ObjectOutputStream(socket.getOutputStream());
			//objIn=new ObjectInputStream(socket.getInputStream());
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	public void run() {
		
		
		if(signingUp){
			
			System.out.println("Signing up new client: "+user);
			
			
			
			ArrayList <String> a=new ArrayList<String>();
			
			a=dbms.getAllClients();
			
			try {
				objOut.writeObject(a);
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			
			String flist=null;
			try {
				flist = inputStream.readUTF();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			System.out.println(flist);
			
			String splits[]=flist.split(">>>");
			
			if(splits[0].equals("FR_LIST")){
				
				for(int i=1;i< splits.length;i++){
					
					dbms.addFriend(user, splits[i]);
					System.out.println("Adding "+user+" "+splits[i]);
					onlineList=dbms.getOnlineList(user);
					friendList=dbms.getFriends(user);
					
				}
			}
			else{
				
			}
			
		 }
		
		
		try {
			
			//Sending FriendList For the First Time //Asuming that arrayList already updated
			onlineOnServer.add(user);
			
			objOut.writeObject(onlineList);
			//try to use something else than objout
			
			//String onfl=ArrayListToString(onlineList);
			
			//os.println(onfl);
			
			
			System.out.println("Currently this friends of "+user+" is online :"+onlineList);
			
			//objOut.writeObject(onlineOnServer);//non database model
			
			//Now inform friends about you;
			
			//Step1: Update database about your online status
			//Step2: notify friend
			String friendJoin="IGNORE>>>FRIEND_JOIN>>>"+this.getUser();
			//System.out.println("After joining "+this.user+onlineOnServer);
			
			synchronized(this){
				
				
			for(ClientThread client: clientThreads){ //Sending everyone...//Should send friends only
				
				if(client!=this && friendList.contains(client.getUser())){
					client.os.println(friendJoin);
					//client.getOnlineList().add(this.getUser());
					//System.out.println("After joining "+this.user+onlineList);
				}
			}
		}
			
			
			while(true){
				
				synchronized(this){
				
				String msg=inputStream.readUTF();
				
				String msgs[]=msg.split(">>>");
				
			
				
				if(msgs[0].equals("TEXT_MSG")){
					
					String formattedMessage="IGNORE>>>TEXT_MSG>>>"+msgs[1]+">>>"+msgs[3];
					
					for(ClientThread client: clientThreads){
						
						if(client.getUser().equals(msgs[2])){
							client.os.println(formattedMessage);
						}
						
					}
				
				}
				
				else if(msgs[0].equals("FILE_REQ")){
					
					String name=msgs[2];
					InetAddress IP;
					String Addr;
					
					for(ClientThread client: clientThreads){
						
						if(client.getUser().equals(name)){
							IP=client.getSocket().getInetAddress();
							Addr=IP.getHostAddress();
							
							String fileSend="IGNORE>>>FILE_SEND>>>"+Addr+">>>"+name;
							
							os.println(fileSend);
						}
					}
					
					
				}else if(msgs[0].equals("FRND_REQ")){
					//Sending database
					System.out.println("Sending all cilents");
					
					ArrayList<String> ac=new ArrayList<String>();
					
					ac=dbms.getAllClients();
					
					String req=ArrayListToString(ac);
					
					os.println(req);
					
					
					//adding to database
					String res=inputStream.readUTF();
					

					String splits[]=res.split(">>>");
					
					if(splits[0].equals("FR_LIST")){
						
						System.out.println("new frnd req");
						
						for(int i=1;i< splits.length;i++){
							
							dbms.addFriend(user, splits[i]);
							System.out.println("Adding "+user+" "+splits[i]);
							
							if(dbms.isOnline(splits[i])==1){
								String friend="IGNORE>>>FRIEND_JOIN>>>"+splits[i];
								os.println(friend);
							}
							
						}	
							onlineList=dbms.getOnlineList(user);
							
							friendList=dbms.getFriends(user);
							
						synchronized(this){
								
								
						for(ClientThread client: clientThreads){ //Sending everyone...//Should send friends only
									
									if(client!=this && friendList.contains(client.getUser())){
										client.os.println(friendJoin);
										System.out.println("Writing to clients");
									}
								}
							}
						
					}
					//updating self
					
					
					
					//updtaing others
					
				
				}
				
				
			}
		}
				
			
				
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			//You are leaving
			//Now inform friends about you;
			
			//Step1: Update database about your online status
			//Step2: notify friend
			
			server.getDbms().setOnline(user, 0);
			onlineOnServer.remove(this.getUser());
			dbms.setOnline(user, 0);
			String friendLeave="IGNORE>>>FRIEND_LEAVE>>>"+this.getUser();
			synchronized(this){
			for(ClientThread client: clientThreads){ //Sending everyone...//Should send friends only
				
				if(client!=this && friendList.contains(client.getUser())){
					client.os.println(friendLeave);
					//client.getOnlineList().remove(this.getUser());
					System.out.println("After leaving "+this.user+onlineOnServer);
				}
			}
			
			}
		}	
		
		
		//Login Complete//Now a while loop for eternity
		
	}
	
	
	public String ArrayListToString(ArrayList<String> al){
		String f="IGNORE>>>FLIST>>>";
		int i=0;
		
		for(i=0;i<al.size()-1;i++){
			
			f+=al.get(i)+">>>";
			
		}
		
		f+=al.get(i);
		
		return f;
	}
	
	//Sends online friend list
	
	public void sendOnlineList() throws IOException{
		//Database works
		//reinitailized onlineList
		objOut.writeObject(onlineList);
		
	}

	
	/**
	 * @return the user
	 */
	public String getUser() {
		return user;
	}

	/**
	 * @param user the user to set
	 */
	public void setUser(String user) {
		this.user = user;
	}

	/**
	 * @return the socket
	 */
	public Socket getSocket() {
		return socket;
	}

	/**
	 * @param socket the socket to set
	 */
	public void setSocket(Socket socket) {
		this.socket = socket;
	}

	/**
	 * @return the onlineList
	 */
	public ArrayList<String> getOnlineList() {
		return onlineList;
	}

	/**
	 * @param onlineList the onlineList to set
	 */
	public void setOnlineList(ArrayList<String> onlineList) {
		this.onlineList = onlineList;
	}

	
	
}
