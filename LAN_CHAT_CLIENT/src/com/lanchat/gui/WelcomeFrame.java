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


public class WelcomeFrame extends JFrame{
	
		
		private String username;
		private String name;
		private Socket socket;
		
		
		public WelcomeFrame(String name, String username, Socket socket){
			
			this.setTitle("Welcome to Lan Chat");
			this.setDefaultCloseOperation(EXIT_ON_CLOSE);
			
			this.name=name;
			this.username=username;
			this.socket=socket;
			
			this.setVisible(true);
			this.setMinimumSize(Main.minFrameSize);
			this.add(new WelcomePanel(this,name,username,socket));
			this.pack();
		}
	
}




class WelcomePanel extends JPanel implements ActionListener{
	
	
	
	
	private DataInputStream inputStream=null;
	private DataOutputStream outputStream=null;
	
	
	private String username;
	private String name;
	private Socket socket;
	
	private ObjectInputStream objIn;
	
	private WelcomeFrame welcomeFrame;
	
	private Image background;
	
	private JLabel logo;
	private JLabel welcomeLabel;
	
	private JButton add;
	private JButton skip;
	
	
	ArrayList<String> a;
	
	private JList<String> jList;
	private JScrollPane jListScrollPane;
	private DefaultListModel<String> jListModel;
	
	
	
	
	
	private GridBagLayout gBLayout=new GridBagLayout();
	private GridBagConstraints gbc;
	
	public WelcomePanel(WelcomeFrame welcomeFrame,String name, String username, Socket socket){
		
		this.welcomeFrame=welcomeFrame;
		
		this.name=name;
		this.username=username;
		this.socket=socket;
		
		background=new ImageIcon(this.getClass().getResource("/Background/sky6.jpg")).getImage();
		Dimension dimension=new Dimension(Main.WIDTH,Main.HEIGHT);
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
		
		welcomeLabel=new JLabel("Welcome "+username+" , " +
				"here are few people you can choose to add from!");
		
		add=new JButton("Add");
		add.setActionCommand("add");
		add.addActionListener(this);
		
	    skip=new JButton("Skip");
	    
	    skip.setActionCommand("skip");
	    skip.addActionListener(this);
		
		initComponent();
	}
	
	
	
	
	private void initComponent(){
		
		
		
		try {
			
		
			objIn=new ObjectInputStream(socket.getInputStream());
			
			Object O=objIn.readObject();
			
		    a= (ArrayList <String>) O;
			
			//System.out.println(a);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		a.remove(username);
		
		for(String f: a){
			jListModel.addElement(f);
		}
		
		jListScrollPane=new JScrollPane(jList);
		jListScrollPane.setSize(new Dimension(640,480));
		jListScrollPane.setMaximumSize(new Dimension(640,480));
		jListScrollPane.setMinimumSize(new Dimension(640,480));
		
		jList.setBackground(new Color(0,0,0,0));
		jList.setOpaque(false);
		jListScrollPane.setOpaque(false);
		jListScrollPane.getViewport().setOpaque(false);
		//jListScrollPane.setBorder(null);
		
		
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
				
				new MainFrame(socket,username);
				welcomeFrame.setVisible(false);
				welcomeFrame.dispose();
				
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		
		else if(e.getActionCommand().equals("skip")){
			
			
			
		}
		
		
	}
}