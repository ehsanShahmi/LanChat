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
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.lanchat.main.*;
/***
 * 
 * @author plabon
 *
 */


public class ThemeChangeFrame extends JFrame{
	
		
		

		
		public ThemeChangeFrame(ControlPanel controlPanel){
			
			this.setTitle("Change Theme");
			
			this.setVisible(true);
			this.setMinimumSize(Main.minFrameSize);
			this.add(new ThemeChangePanel(controlPanel,this));
			this.setAlwaysOnTop(true);
			this.pack();
		}
	
}




class ThemeChangePanel extends JPanel implements ActionListener{
	
	
	
	
	
	ThemeChangeFrame frame;
	
	private String username;
	

	
	
	private Image background;
	
	private JLabel logo;
	private JLabel welcomeLabel;
	
	private JButton add;
	private JButton skip;
	
	

	
	private JList<String> jList;
	private JScrollPane jListScrollPane;
	private DefaultListModel<String> jListModel;
	
	private ControlPanel controlPanel;
	

	
	private GridBagLayout gBLayout=new GridBagLayout();
	private GridBagConstraints gbc;
	
	public ThemeChangePanel(ControlPanel controlPanel,ThemeChangeFrame f){
		
		frame=f;
		
		this.controlPanel=controlPanel;
	
	
		
		background=new ImageIcon(this.getClass().getResource("/Background/sky6.jpg")).getImage();
		Dimension dimension=new Dimension(640,480);
		this.setPreferredSize(dimension);
		this.setSize(dimension);
		this.setLayout(gBLayout);
		

		jListModel=new DefaultListModel<String>();
		jList=new JList<String>(jListModel);
		
		ImageIcon logoIcon =new ImageIcon(this.getClass().getResource("/Logo/logo2.png"));
		
		Image img=logoIcon.getImage();
		
		img=img.getScaledInstance(100,100,java.awt.Image.SCALE_SMOOTH);
		
		logoIcon=new ImageIcon(img);
		
		logo=new JLabel(logoIcon);
		
		welcomeLabel=new JLabel("Here are few themes you can choose from!");
		
		add=new JButton("Apply");
		add.setActionCommand("add");
		add.addActionListener(this);
		
	    skip=new JButton("Cancel");
	    
	    skip.setActionCommand("Cancel");
	    skip.addActionListener(this);
		
		initComponent();
	}
	
	
	
	
	private void initComponent(){
		
		
		
		jListModel.addElement("Sky1");
		jListModel.addElement("Sky2");
		jListModel.addElement("Sky3");
		jListModel.addElement("Sky4(Deafult)");
		jListModel.addElement("Nature1");
		jListModel.addElement("Nature2");
		jListModel.addElement("Nature3");
		jListModel.addElement("Sea1");
		jListModel.addElement("Sea2");
		jListModel.addElement("Sea3");
		
		
		
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
	    
		
		ListSelectionListener listListener=new ListSelectionListener(){

			@Override
			public void valueChanged(ListSelectionEvent e) {
				

				JList list = (JList) e.getSource();
				
				String active=(String) list.getSelectedValue();
				
				controlPanel.backgroundChnage(active);
				
			
			}
			 
		 };	
		
		 jList.addListSelectionListener(listListener);
	}
	
	
	public void paintComponent(Graphics g) {
	    g.drawImage(background, 0, 0,getWidth(),getHeight(), null);
	}




	@Override
	public void actionPerformed(ActionEvent e) {
		
		
		if(e.getActionCommand().equals("add")){
			
		
			
			
			frame.setVisible(false);
			frame.dispose();
		}
		
		else if(e.getActionCommand().equals("Cancel")){
			
			
			frame.setVisible(false);
			frame.dispose();
			
		}
		
		
	}
}