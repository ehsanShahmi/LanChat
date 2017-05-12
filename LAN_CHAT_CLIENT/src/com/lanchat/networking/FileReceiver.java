package com.lanchat.networking;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import com.lanchat.gui.ControlPanel;

public class FileReceiver extends Thread {
	
	private ControlPanel controlPanel;
	
	private Socket socket;
	private ServerSocket serverSocket = null;
	private DataInputStream is;
	private DataOutputStream os;
	private FileOutputStream fos;
	
	private JFileChooser fc;
	
	
	public FileReceiver(ControlPanel controlPanel){
		
		this.controlPanel=controlPanel;
		fc=new JFileChooser();
		
		try {
			serverSocket=new ServerSocket(2500);
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		
	}
	
	
	public void run() {
			
			System.out.println("File Receviver Running");
			
			while(true){
				
				try {
					socket=serverSocket.accept();
					
					System.out.println("After Accepting");
					
					System.out.println(socket.getRemoteSocketAddress() +"wants to send file");
					
					is=new DataInputStream(socket.getInputStream());
					os= new DataOutputStream(socket.getOutputStream());
					
					String req=is.readUTF();
					
					System.out.println(req);
					
					String info[]=req.split(">>>");
					
					if(info[1].equals("SEND_FILE")){
						
						int response=JOptionPane.showConfirmDialog(null,info[2]+
								" Sending " +info[3]+ " Do you want to receive?");
						
						if(response==JOptionPane.YES_OPTION){
							
							os.writeUTF("AC");
							
							System.out.println("Creating File...");
							
							fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
							int ret=fc.showSaveDialog(controlPanel);
							
							File file;
							String directory="";
							
							if(ret==JFileChooser.APPROVE_OPTION){
								 file=fc.getSelectedFile();
								 directory=file.getAbsolutePath()+"/";
							 }
							
							fos=new FileOutputStream(directory+info[3]);
							
							 byte[] data=new byte[1024];
							 
							 while(true){
								 
								 is.read(data);
								 String end = new String(data, 0, 8);
								 System.out.println(end);
								 
								 if(end.equals("COMPLETE")){
									 break;
								 }
								 
								 fos.write(data); 
							 }
							 
							 os.writeUTF("FIle Received");
							 
							 JOptionPane.showMessageDialog(controlPanel, info[3]+"successfully received!");
							 socket.close();		 
						}else{
							
							os.writeUTF("REJECT");
							socket.close();	
							
						}
					}
					else{
						//ignore
					}
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
	}

}
