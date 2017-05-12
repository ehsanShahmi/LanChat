package com.lanchat.networking;

import java.net.*;
import java.util.*;
import java.io.*;

import com.lanchat.database.*;

public class Server {
		
		
		private ArrayList<ClientThread> clientThreads;
		private  ArrayList <String> totalOnline; //for non database purpuse
		
		
		private ServerSocket serverSocket;
		private int port;
		
		private Socket socket;

		private DataOutputStream outputStream;

		private DataInputStream inputStream;
		
		private DatabaseManager dbms;
	
	 	public Server(){
	 		
	 		this.port=ServerMain.defaulfPort;
	 		clientThreads=new ArrayList<ClientThread>();
	 		totalOnline=new ArrayList<String>();
	 		
	 		dbms=new DatabaseManager();
	 		
	 		try {
				serverSocket=new ServerSocket(port);
				initClients();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	 		
	 	}
	 	
	 	
	 public void initClients(){
		 
		 System.out.println("Server running at port : "+port);
		 
		 while(true){
			 
			 try {
				
				socket=serverSocket.accept();
				
				outputStream = new DataOutputStream(socket.getOutputStream());
				
				inputStream=new DataInputStream(socket.getInputStream());
				
				
				String msg=inputStream.readUTF();
				String msgs[]=msg.split(">>>");
				System.out.println(msg);
				
				
				if(msgs[0].equals("LOGIN")){
					
					String username=msgs[1];
					String password=msgs[2];
					
					
					
					///This value should be derived from database
					///Now just just checkeing if username and password is same! 
					String orginalPassword=dbms.getPassword(username);
			
					//originalPassword=getPassword(username); //from database
					
					if(dbms.isOnline(username)!=1){
						
						if(password.equals(orginalPassword)){
							outputStream.writeUTF("OK");
							//outputStream.flush();
							System.out.println("User name matched");
							
							ClientThread clientThread=new ClientThread(socket,username,this,false);
//							
							dbms.setOnline(username, 1);
//							//writers.add(writer);
							clientThreads.add(clientThread);
							clientThread.start();
//							//writer.start();
							
							
						}
						else{
							System.out.println("User name didn't matched");
							outputStream.writeUTF("NO"); //MSG for password didn't match
							socket.close();
						}
						
					}
					else{
						System.out.println("User already online");
						outputStream.writeUTF("ONLINE"); //MSG for password didn't match
						socket.close();
						
					}
				}//Login Ends
				
				if(msgs[0].equals("SIGNUP")){
					
					System.out.println("Signing up new client....");
					
					String username=msgs[1];
					String name=msgs[2];
					String password=msgs[3];
					String mail=msgs[4];
					String gender=msgs[5];
					
					/***/
					System.out.println("Username: "+username);
					System.out.println("Name: "+name);
					System.out.println("Password: "+password);
					System.out.println("Email: "+mail);
					System.out.println("Gender: "+gender);
					
					/***/
					
					if(dbms.isExist(username)){
						
						outputStream.writeUTF("EXIST");
						socket.close();
					}else{
						dbms.signUp(username, name, password, mail, gender);
						outputStream.writeUTF("SIGNED_UP");
					
				       ClientThread clientThread=new ClientThread(socket,username,this,true);
						
						dbms.setOnline(username, 1);
						
						//writers.add(writer);
						clientThreads.add(clientThread);
						clientThread.start();
					}
				
				}
				
				
			 }catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			 
		 }
		 
	 }


	
	public ArrayList<ClientThread> getClientThreads() {
		return clientThreads;
	}


	
	public void setClientThreads(ArrayList<ClientThread> clientThreads) {
		this.clientThreads = clientThreads;
	}


	/**
	 * @return the outputStream
	 */
	public DataOutputStream getOutputStream() {
		return outputStream;
	}


	/**
	 * @param outputStream the outputStream to set
	 */
	public void setOutputStream(DataOutputStream outputStream) {
		this.outputStream = outputStream;
	}


	/**
	 * @return the inputStream
	 */
	public DataInputStream getInputStream() {
		return inputStream;
	}


	/**
	 * @param inputStream the inputStream to set
	 */
	public void setInputStream(DataInputStream inputStream) {
		this.inputStream = inputStream;
	}


	/**
	 * @return the totalOnline
	 */
	public ArrayList<String> getTotalOnline() {
		return totalOnline;
	}


	/**
	 * @param totalOnline the totalOnline to set
	 */
	public void setTotalOnline(ArrayList<String> totalOnline) {
		this.totalOnline = totalOnline;
	}


	/**
	 * @return the dbms
	 */
	public DatabaseManager getDbms() {
		return dbms;
	}


	/**
	 * @param dbms the dbms to set
	 */
	public void setDbms(DatabaseManager dbms) {
		this.dbms = dbms;
	}

	
}