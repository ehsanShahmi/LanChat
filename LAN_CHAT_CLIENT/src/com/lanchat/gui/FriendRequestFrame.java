package com.lanchat.gui;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import javax.swing.*;

import com.lanchat.main.*;
/***
 * 
 * @author plabon
 *
 */


public class FriendRequestFrame extends JFrame{
	
		
		private String username;
		private Socket socket;
		
		
		public FriendRequestFrame(String username, Socket socket,ControlPanel controlPanel,String msg){
			
			this.setTitle("Add Friends");
			
			this.username=username;
			this.socket=socket;
			
			this.setVisible(true);
			this.setMinimumSize(Main.minFrameSize);
			this.add(new FriendRequestPanel(controlPanel,username,socket,msg,this));
			this.setAlwaysOnTop(true);
			this.pack();
		}
	
}




class FriendRequestPanel extends JPanel implements ActionListener{
	
	
	
	
	private DataInputStream inputStream=null;
	private DataOutputStream outputStream=null;
	FriendRequestFrame frame;
	
	private String username;
	
	private Socket socket;
	
	private ObjectInputStream objIn;
	
	
	
	private Image background;
	
	private JLabel logo;
	private JLabel welcomeLabel;
	
	private JButton add;
	private JButton skip;
	
	
	String ac[];
	
	private JList<String> jList;
	private JScrollPane jListScrollPane;
	private DefaultListModel<String> jListModel;
	
	private ControlPanel controlPanel;
	
	String msg;
	
	private GridBagLayout gBLayout=new GridBagLayout();
	private GridBagConstraints gbc;
	
	public FriendRequestPanel(ControlPanel controlPanel, String username, 
			Socket socket,String msg,FriendRequestFrame f){
		
		frame=f;
		this.msg=msg;
		this.controlPanel=controlPanel;
		this.username=username;
		this.socket=socket;
		
		background=new ImageIcon(this.getClass().getResource("/Background/sky6.jpg")).getImage();
		Dimension dimension=new Dimension(640,480);
		this.setPreferredSize(dimension);
		this.setSize(dimension);
		this.setLayout(gBLayout);
		

		jListModel=new DefaultListModel<String>();
		jList=new JList<String>(jListModel);
		
		try {
			inputStream= new DataInputStream(socket.getInputStream());
			outputStream= new DataOutputStream(socket.getOutputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		ImageIcon logoIcon =new ImageIcon(this.getClass().getResource("/Logo/logo2.png"));
		
		Image img=logoIcon.getImage();
		
		img=img.getScaledInstance(100,100,java.awt.Image.SCALE_SMOOTH);
		
		logoIcon=new ImageIcon(img);
		
		logo=new JLabel(logoIcon);
		
		welcomeLabel=new JLabel("List of all user!");
		
		add=new JButton("Add");
		add.setActionCommand("add");
		add.addActionListener(this);
		
	    skip=new JButton("Cancel");
	    
	    skip.setActionCommand("Cancel");
	    skip.addActionListener(this);
		
		initComponent();
	}
	
	
	
	
	private void initComponent(){
		
		
		
		
			
			
			ac=msg.split(">>>");
		  
			
			System.out.println(ac);
	
	
		for(int i=2;i<ac.length;i++){
			
			jListModel.addElement(ac[i]);
		}
		
		System.out.println("Check1");
		
		jListScrollPane=new JScrollPane(jList);
		jListScrollPane.setSize(new Dimension(640,480));
		jListScrollPane.setMaximumSize(new Dimension(640,480));
		jListScrollPane.setMinimumSize(new Dimension(640,480));
		
		jList.setBackground(new Color(0,0,0,0));
		jList.setOpaque(false);
		jListScrollPane.setOpaque(false);
		jListScrollPane.getViewport().setOpaque(false);
		//jListScrollPane.setBorder(null);
		
		System.out.println("Check2");
		
		gbc=new GridBagConstraints();
		
		gbc.insets=new Insets(15,15,15,15);
		gbc.anchor=GridBagConstraints.CENTER;
		gbc.gridwidth=2;
		gbc.gridx=0;
		gbc.gridy=0;
		
		add(logo,gbc);
		
		gbc.gridy++;
		
		add(welcomeLabel,gbc);
	
		gbc.gridy++;
		
		add(jListScrollPane,gbc);
		
		gbc.gridwidth=1;
		
		gbc.gridy++;
	    
		add(add,gbc);
		
		gbc.gridx++;
		
		add(skip,gbc);
	    
		
	}
	
	
	public void paintComponent(Graphics g) {
	    g.drawImage(background, 0, 0,getWidth(),getHeight(), null);
	}




	@Override
	public void actionPerformed(ActionEvent e) {
		
		
		if(e.getActionCommand().equals("add")){
			
		
			 
			ArrayList <String> selected=new ArrayList<String>();
		
	     	 int[] selectedIx = jList.getSelectedIndices();

	 	    for (int i = 0; i < selectedIx.length; i++) {
			      String sel = jList.getModel().getElementAt(selectedIx[i]);
			      selected.add(sel);
			    }
			 
			  String s="FR_LIST>>>";
			  int i=0;
			  for(i=0;i<selected.size()-1;i++){
				  s+=selected.get(i)+">>>";
				  
			  }
			  
			  s+=selected.get(i);
			  
			System.out.println(s);
			  
			try {
				outputStream.writeUTF(s);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			frame.setVisible(false);
			frame.dispose();
		}
		
		else if(e.getActionCommand().equals("Cancel")){
			
			frame.setVisible(false);
			frame.dispose();
			
		}
		
		
	}
}