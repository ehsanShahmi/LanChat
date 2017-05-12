package com.lanchat.gui;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.lanchat.networking.*;
import com.lanchat.main.*;

/**
 * @author plabon
 *
 */
public class ControlPanel extends JPanel{
	
	//USER
	private String username;
	
	
	private int WIDTH=900;
	private int HEIGHT=675;
	
	private Image background;
	
	//Panels
	private MainFrame parentFrame;
	
	private JPanel topPanel; 
	private JPanel bottomPanel;
	private JPanel leftPanel;
	private JPanel rightPanel;
	private JPanel centerPanel;
	
	
	//Contents
	
	private JTextArea displayArea;
	private JTextArea messageArea;
	
	private JScrollPane displayScrollPane;
	private JScrollPane messageScrollPane;
	
	
	
	//buttons
	
	//bottom panel
	private JButton sendButton;
	private JButton sendFileButton;
	
	
	private JButton addFriend;
	private JButton settings;
	private JButton profile;
	private JButton signout;
	
	
	
	//Left panel
	private JLabel online;
	
	private JList<String> onlineJList;
	private JScrollPane onlineJListScrollPane;
	private DefaultListModel<String> onlineJListModel;
	
	private JTextField searchFriend;
	private JLabel search;
	
	
	
	//Networking
	 private Socket socket=null;
	 private DataInputStream inputStream;
	 private DataOutputStream outputStream;
	 private ObjectInputStream objIn;
	 private ObjectOutputStream objOut;
	 
	 private ArrayList <String> onlineArrayList=null;
	
	 
	 private HashMap <String,String> messages=null; //initialized in initFields()	  
	 
	 private Image sky1;
	 private Image sky2;
	 private Image sky3;
	 private Image sky4;
	 private Image sky5;
	 
	 private Image nature1;
	 private Image nature2;
	 private Image nature3;
	 
	 private Image sea1;
	 private Image sea2;
	 private Image sea3;
	
	
	
	
	public ControlPanel(MainFrame parentFrame, String username){
		
		this.parentFrame=parentFrame;
		this.socket=parentFrame.getSocket();
		this.username=username;
		
		this.setLayout(new BorderLayout());
		this.setSize(new Dimension(WIDTH,HEIGHT));
		this.setMinimumSize(Main.minFrameSize);
		sky4=new ImageIcon(this.getClass().getResource("/Background/sky4.jpg")).getImage();
		background=sky4;
		
		
		initFields();
		
		initConnections();
		
		initComponents();
		
		listenerControl();
		
	}
	





	private void listenerControl() {
		
		 ListSelectionListener listListener=new ListSelectionListener(){

			@Override
			public void valueChanged(ListSelectionEvent e) {
				
				JList list = (JList) e.getSource();
				
				String activeUser=(String) list.getSelectedValue();
				
				displayArea.setText(messages.get(activeUser));
				
			}
			 
		 };
		 
		 onlineJList.addListSelectionListener(listListener);
		
	}













	//This method initialize important panels and fields that
	//might be used before they actually added
	
	private void initFields() {
		
		
		//Five Panels
		
		topPanel=new JPanel();
		leftPanel=new JPanel();
		bottomPanel=new JPanel();
		rightPanel=new JPanel();
		centerPanel=new JPanel();
		
		
		//Initialize Fields
		sendButton=new JButton("Send"); //bottom panel
		sendFileButton=new JButton("+File"); //bottom panel
		
		ImageIcon icon=new ImageIcon(this.getClass().getResource("/ButtonIcons/addfriend.png"));
		
		Image image=icon.getImage();
		
		image=image.getScaledInstance(70,70,java.awt.Image.SCALE_SMOOTH);
		
		ImageIcon af=new ImageIcon(image);
	
		addFriend=new JButton(af);
		addFriend.setBorder(null);
		addFriend.setContentAreaFilled(false);
		addFriend.setActionCommand("add-friend");
		addFriend.setToolTipText("Add Friend");
		
		
		icon=new ImageIcon(this.getClass().getResource("/ButtonIcons/settings.png"));
		image=icon.getImage();
		image=image.getScaledInstance(70,70,java.awt.Image.SCALE_SMOOTH);
		ImageIcon si=new ImageIcon(image);
		
		settings=new JButton(si);
		settings.setBorder(null);
		settings.setContentAreaFilled(false);
		settings.setActionCommand("settings");
		settings.setToolTipText("Change Theme");
		
		
		icon=new ImageIcon(this.getClass().getResource("/ButtonIcons/profile.png"));
		image=icon.getImage();
		image=image.getScaledInstance(70,70,java.awt.Image.SCALE_SMOOTH);
		ImageIcon pf=new ImageIcon(image);
		
		profile=new JButton(pf);
		profile.setBorder(null);
		profile.setContentAreaFilled(false);
		profile.setActionCommand("profile");
		profile.setToolTipText("Profile");
		
		
		icon=new ImageIcon(this.getClass().getResource("/ButtonIcons/logout2.png"));
		image=icon.getImage();
		image=image.getScaledInstance(70,70,java.awt.Image.SCALE_SMOOTH);
		ImageIcon lo=new ImageIcon(image);
		
		signout=new JButton(lo);
		signout.setBorder(null);
		signout.setContentAreaFilled(false);
		signout.setActionCommand("signout");
		signout.setToolTipText("Log Out");
		
		
		messageArea=new JTextArea(); //bottom panel
	
		displayArea= new JTextArea(); // center panel
		
		
		//List model that containts element of online friends
		
		onlineJListModel=new DefaultListModel<String>();
		onlineJList=new JList<String>(onlineJListModel);
		
		
		try {
			inputStream= new DataInputStream(socket.getInputStream());
			outputStream= new DataOutputStream(socket.getOutputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		
		
		//HashMap that contains messages of every user and 
		//mapped with user name
		messages=new HashMap<String,String>();
		
		
	}







	private void initConnections() {
		
		
			
			//objIn=new ObjectInputStream(socket.getInputStream());
			//objOut=new ObjectOutputStream(socket.getOutputStream());
			
			//Object obj=objIn.readObject();
			
			//onlineArrayList=(ArrayList<String>) obj;
			
			initializedFriendList();
			
			//updateFriendList(onlineArrayList);

			new MessageSender(this).start();
			
			new MessageReceiver(this).start();
		
			new FileReceiver(this).start();
		
		
	}







	private void initComponents() {
	
			
		decorateTop();
		add(topPanel,BorderLayout.NORTH);
		
		
		decorateLeft();
		add(leftPanel,BorderLayout.WEST);
		
		
		decorateBottom();
		add(bottomPanel,BorderLayout.SOUTH);
		
		decorateRight();
		add(rightPanel,BorderLayout.EAST);
		
		decorateCenter();
		add(centerPanel,BorderLayout.CENTER);	
				
	}
	
	

	private void decorateCenter() {
		
		centerPanel.setPreferredSize(new Dimension(500,445));
		centerPanel.setOpaque(false);
		centerPanel.setBorder(BorderFactory.createLineBorder(Color.WHITE));
		centerPanel.setLayout(new BorderLayout());
		
		//Adding message Display Area
		
     	displayArea.setEditable(false);
		displayArea.setLineWrap(true);
		displayArea.setOpaque(false);
		
		
	  
		displayScrollPane = new JScrollPane (displayArea);
		displayScrollPane.setBackground(Color.WHITE);
		displayScrollPane.getViewport().setBackground(Color.WHITE);
	
		centerPanel.add(displayScrollPane,BorderLayout.CENTER);
	
		
	}

	private void decorateLeft() {
		
		//leftPanel.setBackground(Color.CYAN);
		leftPanel.setPreferredSize(new Dimension(200,445));
		leftPanel.setBorder(BorderFactory.createLineBorder(Color.WHITE));
		leftPanel.setOpaque(false);
		leftPanel.setLayout(new GridBagLayout());
		
		
		//contents
		online=new JLabel("Online");
		online.setForeground(Color.WHITE);
		
		searchFriend=new JTextField(16);
		searchFriend.setPreferredSize(new Dimension(40,30));
		searchFriend.setBorder(null);
		searchFriend.setToolTipText("Press enter to go");
		
		search= new JLabel("Search");
	
		
		GridBagConstraints gbc=new GridBagConstraints();
		
		gbc.insets=new Insets(5,5,5,5);
		gbc.anchor=GridBagConstraints.CENTER;
		gbc.gridheight=1;
		gbc.gridwidth=1;
		gbc.gridx=0;
		gbc.gridy=0;
		leftPanel.add(search,gbc);
		gbc.gridy=1;
		leftPanel.add(searchFriend,gbc);
		gbc.gridy=2;
		leftPanel.add(online,gbc);
		
		
		//Online Friend List
		
		//initialized in initFields()
		onlineJList.setSelectedIndex(0);
		onlineJList.setBackground(new Color(0,0,0,0));
		onlineJList.setOpaque(false);
		
		
		
//		//Optional
//		if(onlineJList.getSelectedValue().equals("<None Online>")){
//			sendButton.setEnabled(false);
//			sendFileButton.setEnabled(false);
//			
//		}else{
//			sendButton.setEnabled(true);
//			sendFileButton.setEnabled(true);
//			
//		}
		
		
		onlineJListScrollPane=new JScrollPane(onlineJList);
		//onlineListScrollPane.setMinimumSize(new Dimension(leftPanel.getWidth(),leftPanel.getHeight()/2));
		onlineJListScrollPane.setOpaque(false);
		onlineJListScrollPane.getViewport().setOpaque(false);
		onlineJListScrollPane.setBorder(null);
		
		gbc.gridy++;
		
		leftPanel.add(onlineJListScrollPane,gbc);

		
	}

	private void decorateRight() {
		
		//rightPanel.setBackground(Color.YELLOW);
		rightPanel.setPreferredSize(new Dimension(200,445));
		rightPanel.setOpaque(false);
		rightPanel.setBorder(BorderFactory.createLineBorder(Color.WHITE));
		rightPanel.setLayout(new GridBagLayout());
		
		GridBagConstraints c=new GridBagConstraints();
		
		c.insets=new Insets(10,10,10,10);
		
		c.gridx=0;
		c.gridy=0;
		
		rightPanel.add(addFriend,c);
		
		c.gridx=1;
		c.gridy=0;
		
		rightPanel.add(settings,c);
		
		c.gridx=0;
		c.gridy=1;
		rightPanel.add(profile,c);
		
		c.gridx=1;
		c.gridy=1;
		rightPanel.add(signout,c);
		
		
		
	}

	private void decorateBottom() {
		//bottomPanel.setBackground(Color.GRAY);
		bottomPanel.setPreferredSize(new Dimension(900,150));
		bottomPanel.setOpaque(false);
		bottomPanel.setBorder(BorderFactory.createLineBorder(Color.WHITE));
		
		//Layout
		bottomPanel.setLayout(new GridBagLayout());
		GridBagConstraints c=new GridBagConstraints();
		
		
		//contents
		
		messageArea.setEditable(true);
		messageArea.setLineWrap(true);
		
		messageScrollPane = new JScrollPane (messageArea);
		messageScrollPane.setPreferredSize(new Dimension(400,80));
		
		
		//Buttons initialized in initFields()
		sendFileButton.setBackground(Color.WHITE);
		sendButton.setBackground(Color.WHITE);
		
		
		
		c.insets=new Insets(15,15,15,15);
		c.gridwidth=4;
		c.gridheight=2;
		c.gridx=1;
		c.gridy=0;
		bottomPanel.add(messageScrollPane,c);
		
		c.gridwidth=1;
		c.gridheight=1;
		c.gridx=5;
		c.gridy=0;
		
		bottomPanel.add(sendButton,c);
		
		c.gridwidth=1;
		c.gridheight=1;
		c.gridx=5;
		c.gridy=1;
		
		bottomPanel.add(sendFileButton,c);
		
		
		
		
	}

	
	
	private void decorateTop() {
		topPanel.setPreferredSize(new Dimension(getWidth(),80));
		//topPanel.setBackground(Color.GRAY);
		topPanel.setOpaque(false);
		topPanel.setBorder(BorderFactory.createLineBorder(Color.WHITE));
	}

	
	/***UPDATE FRIENDLIST ***/
	
	public void initializedFriendList(){

		if(onlineArrayList==null||onlineArrayList.isEmpty()){
			
			System.out.println("Empty");
			
			onlineArrayList=new ArrayList<String>();
			onlineArrayList.add("<None Online>");
			
		} //HashMap entry for the first time
		
		onlineJListModel.removeAllElements();
		
		for(String friend: onlineArrayList){
				
				onlineJListModel.addElement(friend);
				
				
				if(!messages.containsKey(friend)){
					messages.put(friend, "");
				}
				else{
					messages.put(friend, messages.get(friend)+"");
					
				}
		}
			
		
	}
	
	
	
	public void updateFriendList(ArrayList<String> updatedArrayList){
		
		//Step1: Updating ArrayList
		this.onlineArrayList=updatedArrayList;
		
		if(onlineArrayList==null||onlineArrayList.isEmpty()){
			
//			onlineArrayList=new ArrayList<String>();
//			onlineArrayList.add("<None Online>");
			
			initializedFriendList();
			return;	
		}
		else if(onlineArrayList.size()==1){
			onlineArrayList.remove(username);
			initializedFriendList();
			return;
		}else{
			onlineArrayList.remove(username);
		}
		//Step2: Storing selected value
		String selectedFriend=onlineJList.getSelectedValue();
		int selectedIndex=onlineJList.getSelectedIndex();
		
		//Step3: Removing all elements from ListModel
		onlineJListModel.removeAllElements();
		
		//Step4: Adding again to list model and updating hash table
		for(String friend:onlineArrayList){
				
			onlineJListModel.addElement(friend);
			
			if(!messages.containsKey(friend)){
				messages.put(friend, "");
			}
			else{
				messages.put(friend, messages.get(friend)+"");
				
			}
		}
		
		//Step5: Restoring selected values
		if(onlineJListModel.contains(selectedFriend)){
			onlineJList.setSelectedValue(selectedFriend, true);
		
		}else{
			if(onlineJListModel.getSize()>selectedIndex){
				onlineJList.setSelectedIndex(selectedIndex);
			}else{
				
				onlineJList.setSelectedIndex(onlineJListModel.getSize()-1);
			}
			
		}
		
		
	}
	
	//Temp

  public void backgroundChnage(String bgName){
	  
	  if(bgName.equals("change nature")){
		  background=new ImageIcon(this.getClass().getResource("/Background/nature1.jpg")).getImage();
	  }
	  
	  else if(bgName.equals("change sea")){
		  background=new ImageIcon(this.getClass().getResource("/Background/sea1.jpg")).getImage();
	  }
	  
	  else if(bgName.equals("change default")){
		  background=new ImageIcon(this.getClass().getResource("/Background/sky4.jpg")).getImage();
	  }
	  
	  
	  if(bgName.equals("Sky1")){
		  
		  background=new ImageIcon(this.getClass().getResource("/Background/sky1.png")).getImage();
	  }
	  
	  else if(bgName.equals("Sky2")){
		  
		  background=new ImageIcon(this.getClass().getResource("/Background/sky2.jpg")).getImage();
	  }
	  
	  else if(bgName.equals("Sky3")){
		  
		  background=new ImageIcon(this.getClass().getResource("/Background/sky3.jpg")).getImage();
	  }
	  
	  else if(bgName.equals("Sky4(Default)")){
		  
		  background=new ImageIcon(this.getClass().getResource("/Background/sky4.jpg")).getImage();
	  }
	  
	  else if(bgName.equals("Sea1")){
		  
		  background=new ImageIcon(this.getClass().getResource("/Background/sea1.jpg")).getImage();
	  }
	  
	  else if(bgName.equals("Sea2")){
		  
		  background=new ImageIcon(this.getClass().getResource("/Background/sea2.jpg")).getImage();
	  }
	  
	  else if(bgName.equals("Sea3")){
		  
		  background=new ImageIcon(this.getClass().getResource("/Background/sea3.jpg")).getImage();
	  }
	  
	  else if(bgName.equals("Nature1")){
		  
		  background=new ImageIcon(this.getClass().getResource("/Background/nature1.jpg")).getImage();
	  }
	  
	  else if(bgName.equals("Nature2")){
		  
		  background=new ImageIcon(this.getClass().getResource("/Background/nature2.jpg")).getImage();
	  }
	  
	  else if(bgName.equals("Nature3")){
		  
		  background=new ImageIcon(this.getClass().getResource("/Background/nature3.jpg")).getImage();
	  }
	  repaint();
  }
	
	
	public void paintComponent(Graphics g) {
	    g.drawImage(background, 0, 0,getWidth(),getHeight(), null);
	}






	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}






	/**
	 * @return the displayArea
	 */
	public JTextArea getDisplayArea() {
		return displayArea;
	}






	/**
	 * @param displayArea the displayArea to set
	 */
	public void setDisplayArea(JTextArea displayArea) {
		this.displayArea = displayArea;
	}






	/**
	 * @return the messageArea
	 */
	public JTextArea getMessageArea() {
		return messageArea;
	}






	/**
	 * @param messageArea the messageArea to set
	 */
	public void setMessageArea(JTextArea messageArea) {
		this.messageArea = messageArea;
	}






	/**
	 * @return the displayScrollPane
	 */
	public JScrollPane getDisplayScrollPane() {
		return displayScrollPane;
	}






	/**
	 * @param displayScrollPane the displayScrollPane to set
	 */
	public void setDisplayScrollPane(JScrollPane displayScrollPane) {
		this.displayScrollPane = displayScrollPane;
	}






	/**
	 * @return the messageScrollPane
	 */
	public JScrollPane getMessageScrollPane() {
		return messageScrollPane;
	}




	/**
	 * @param messageScrollPane the messageScrollPane to set
	 */
	public void setMessageScrollPane(JScrollPane messageScrollPane) {
		this.messageScrollPane = messageScrollPane;
	}



	/**
	 * @return the onlineArrayList
	 */
	public ArrayList<String> getOnlineArrayList() {
		return onlineArrayList;
	}






	/**
	 * @param onlineArrayList the onlineFriends to set
	 */
	public void setOnlineArrayList(ArrayList<String> onlineArrayList) {
		this.onlineArrayList = onlineArrayList;
	}

	
	/**
	 * @return the onlineJList
	 */
	public JList<String> getOnlineJList() {
		return onlineJList;
	}






	/**
	 * @param onlineJList the onlineJList to set
	 */
	public void setOnlineJList(JList<String> onlineJList) {
		this.onlineJList = onlineJList;
	}






	/**
		@return the onlineJListModel
	 */
	public DefaultListModel<String> getonlineJListModel() {
		return onlineJListModel;
	}






	/**
	 * @param onlineJListModel the onlineListModel to set
	 */
	public void setonlineJListModel(DefaultListModel<String> onlineJListModel) {
		this.onlineJListModel = onlineJListModel;
	}






	/**
	 * @return the onlineJListScrollPane
	 */
	public JScrollPane getonlineJListScrollPane() {
		return onlineJListScrollPane;
	}






	/**
	 * @param onlineJListScrollPane the onlineJListScrollPane to set
	 */
	public void setOnlineListScrollPane(JScrollPane onlineJListScrollPane) {
		this.onlineJListScrollPane = onlineJListScrollPane;
	}






	/**
	 * @return the sendButton
	 */
	public JButton getSendButton() {
		return sendButton;
	}






	/**
	 * @return the sendFileButton
	 */
	public JButton getSendFileButton() {
		return sendFileButton;
	}






	/**
	 * @return the socket
	 */
	public Socket getSocket() {
		return socket;
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
	 * @return the messages
	 */
	public HashMap<String, String> getMessages() {
		return messages;
	}






	/**
	 * @param messages the messages to set
	 */
	public void setMessages(HashMap<String, String> messages) {
		this.messages = messages;
	}






	/**
	 * @return the addFriend
	 */
	public JButton getAddFriend() {
		return addFriend;
	}






	/**
	 * @param addFriend the addFriend to set
	 */
	public void setAddFriend(JButton addFriend) {
		this.addFriend = addFriend;
	}






	/**
	 * @return the settings
	 */
	public JButton getSettings() {
		return settings;
	}






	/**
	 * @param settings the settings to set
	 */
	public void setSettings(JButton settings) {
		this.settings = settings;
	}






	/**
	 * @return the profile
	 */
	public JButton getProfile() {
		return profile;
	}






	/**
	 * @param profile the profile to set
	 */
	public void setProfile(JButton profile) {
		this.profile = profile;
	}






	/**
	 * @return the signout
	 */
	public JButton getSignout() {
		return signout;
	}






	/**
	 * @param signout the signout to set
	 */
	public void setSignout(JButton signout) {
		this.signout = signout;
	}






	/**
	 * @return the parentFrame
	 */
	public MainFrame getParentFrame() {
		return parentFrame;
	}






	/**
	 * @param parentFrame the parentFrame to set
	 */
	public void setParentFrame(MainFrame parentFrame) {
		this.parentFrame = parentFrame;
	}
	
}