package com.lanchat.networking;
import com.lanchat.gui.*;

import java.awt.BorderLayout;
import java.io.*;
import java.net.*;

import javax.swing.*;


public class FileSender extends Thread{

	
	private ControlPanel controlPanel;
	
	
	private String username;
	private String receiver;
	
	private DataOutputStream dos;
	private DataInputStream dis;
	private FileInputStream fis;
	
	private  String path;
	
	private int defaultFileReceivePort=2500;
	private String receiverIP;
	private Socket socket=null;
	
	private JFileChooser fc;
	private File file;
	
	JDesktopPane desktop;
	
	public FileSender(ControlPanel controlPanel, String IP, String Receiver){
		this.controlPanel=controlPanel;
		this.receiverIP=IP;
		this.username=controlPanel.getUsername();
		this.receiver=Receiver;
		desktop=new JDesktopPane();
		fc=new JFileChooser();
		
	}
	
	
	public void run() {
		
			try {
				socket=new Socket(receiverIP,defaultFileReceivePort);
				System.out.println("File Sender connected at: "+socket.getRemoteSocketAddress());
				
				if(socket!=null){
					
					int ret=fc.showOpenDialog(controlPanel);
					
					if(ret==JFileChooser.APPROVE_OPTION){
						
						file=fc.getSelectedFile();
						path=file.getAbsolutePath();
						
						System.out.println(path);
						
						String fileName=getFileName(path);
						
						String req="IGNORE>>>SEND_FILE>>>"+username+">>>"+fileName;
						
						dos=new DataOutputStream(socket.getOutputStream());
						dis=new DataInputStream(socket.getInputStream());
						
						dos.writeUTF(req);
						
						if(dis.readUTF().equals("AC")){
								
							fis=new FileInputStream(path);
							byte[] data=new byte[1024];
							
							int read=0;
							
							while((read = fis.read(data)) != -1){
								
								dos.write(data);
							}
							
							data = "COMPLETE".getBytes();
							dos.write(data);
							
							String ack=dis.readUTF();
							
							System.out.println(ack);
							//NEED TO CHANGE
							
							JOptionPane.showMessageDialog(null,fileName + 
									" successfully sent! ");
							
//							JFrame notification=new JFrame("File Received");
//							notification.setSize(200, 200);
//							notification.setVisible(true);
							 
							socket.close();
						}
						else{
						
							JOptionPane.showMessageDialog(controlPanel,receiver + 
									" didn't accept "+fileName);
							
							
						}		
					}
				
				
				}
					
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
	}
	
	public String getFileName(String _path) {
		if(_path.charAt(0) == '/') {	// Linux
			return _path.substring(_path.lastIndexOf('/')+1);
		} else {						// Windows
			return _path.substring(_path.lastIndexOf('\\')+1);
		}
	}

}
