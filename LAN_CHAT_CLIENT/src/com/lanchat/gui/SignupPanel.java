package com.lanchat.gui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Enumeration;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import com.lanchat.main.Main;

public class SignupPanel extends JPanel implements ActionListener{
	
		private Socket clientSocket = null;
		private DataInputStream inputStream=null;
		private DataOutputStream outputStream=null;
	
	
		JFrame parentFrame;
		JFrame loginFrame;
		private Image background;
		
		GridBagLayout gBLayout;
		GridBagConstraints gbc;
		
		
	// Sign Up Information
		//Labels
		JLabel logo;
		
		JLabel nameLabel;
		JLabel userNameLabel;
		JLabel passwordLabel;
		JLabel repeatPasswordLabel;
		JLabel emailLabel;
		JLabel genderLabel;
		
		//Fields
		
		JTextField name;
		JTextField userName;
		JPasswordField password;
		JPasswordField repeatedPassword;
		JTextField email;
		JRadioButton male;
		JRadioButton female;
		JRadioButton others;
		ButtonGroup gender;
		
		JButton signup;
		
		
		
		
		SignupPanel(JFrame jFrame){
				
				this.parentFrame=jFrame;
				gBLayout=new GridBagLayout();
				this.setLayout(gBLayout);
				this.setPreferredSize(new Dimension(Main.WIDTH,Main.HEIGHT));
				initComponents();
			
		}
		
		public void paintComponent(Graphics g) {
		    g.drawImage(background, 0, 0,getWidth(),getHeight(), null);
		 }
		
		
		private void initComponents(){
			
			background=new ImageIcon(this.getClass().getResource("/Background/sky2.jpg")).getImage();
			
			//Label Definition
			logo=new JLabel("Sign Up");
			nameLabel=new JLabel("Name:");
			userNameLabel= new JLabel("Username:");
			passwordLabel=new JLabel("Password:");
			repeatPasswordLabel=new JLabel("Repeat Password:");
			emailLabel=new JLabel("Email:");
			genderLabel=new JLabel("Sex:");
			
			//Fields Definition
			
			Dimension fieldSize=new Dimension(10,40);
		
			
			name=new JTextField(30);
			name.setBorder(null);
			name.setPreferredSize(fieldSize);
			
			userName=new JTextField(30);
			userName.setBorder(null);
			userName.setPreferredSize(fieldSize);
			
			
			password=new JPasswordField(30);
			password.setBorder(null);
			password.setPreferredSize(fieldSize);
			
			
			repeatedPassword=new JPasswordField(30);
			repeatedPassword.setBorder(null);
			repeatedPassword.setPreferredSize(fieldSize);
			
			email=new JTextField(30);
			email.setBorder(null);
			email.setPreferredSize(fieldSize);
			
			male=new JRadioButton("Male");
			male.setOpaque(false);
			male.setSelected(true);
			female=new JRadioButton("Female");
			female.setOpaque(false);
			others=new JRadioButton("Others");
			others.setOpaque(false);
			
			gender=new ButtonGroup();
			
			gender.add(male);
			gender.add(female);
			gender.add(others);
			
			signup=new JButton("Sign Up");
			signup.addActionListener(this);
			signup.setActionCommand("signup");
			
			
			//Adding components using GridBagConstraints
			
			
			gbc=new GridBagConstraints();
			gbc.insets=new Insets(5,15,50,15); //for logo
			
			gbc.gridwidth=3;
			gbc.gridx=1;
			gbc.gridy=0;
			add(logo,gbc);
			
			//adding lables
			gbc.gridwidth=1;
			gbc.insets=new Insets(15,15,15,15); 
			gbc.gridx=0;
			gbc.gridy=1;
			
			gbc.anchor=GridBagConstraints.LINE_END;
			
			add(nameLabel,gbc);
			gbc.gridy++;
			
			add(userNameLabel,gbc);
			gbc.gridy++;
			
			add(passwordLabel,gbc);
			gbc.gridy++;
			
			add(repeatPasswordLabel,gbc);
			gbc.gridy++;
			
			add(emailLabel,gbc);
			gbc.gridy++;
			
			add(genderLabel,gbc);
			gbc.gridy++;
			
			//Fileds
			gbc.anchor=GridBagConstraints.LINE_START;
			gbc.gridwidth=3;
			gbc.gridx=1;
			gbc.gridy=1;
			
			add(name,gbc);
			gbc.gridy++;
			
			add(userName,gbc);
			gbc.gridy++;
			
			add(password,gbc);
			gbc.gridy++;
			
			add(repeatedPassword,gbc);
			gbc.gridy++;
			
			add(email,gbc);
			gbc.gridy++;
			
			
			gbc.gridwidth=1;
			
			add(male,gbc);
			gbc.gridx++;
			add(female,gbc);
			gbc.gridx++;
			add(others,gbc);
			
			//if(true) return;
			
			gbc.gridwidth=2;
			gbc.gridx--;
			gbc.gridy++;
			gbc.anchor=GridBagConstraints.CENTER;
			
			add(signup,gbc);
			
			
		}
		
		
		
		public JRadioButton getSelection(ButtonGroup group) {
		    for (Enumeration e = group.getElements(); e.hasMoreElements();) {
		      JRadioButton b = (JRadioButton) e.nextElement();
		      if (b.getModel() == group.getSelection()) {
		        return b;
		      }
		    }
		    return null;
	  }


		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			
			if(e.getActionCommand().equals("signup")){
				
				String uname=userName.getText().trim();
				String fullname=name.getText().trim();
				String pass=password.getText().trim();
				String repass=repeatedPassword.getText().trim();
				
				if(!pass.equals(repass)){
					
					JOptionPane.showMessageDialog(null, "Password didn't match!","Password",JOptionPane.ERROR_MESSAGE);
					password.setText("");
					repeatedPassword.setText("");
					return;
				}
				
				
				String mail=email.getText().trim();
				
				
				String sex=getSelection(gender).getText().trim();
				
				System.out.println(uname+fullname+sex+pass);
				
				//checking if fields are empty
				
				if(fullname.equals("")){
					
					return;
				}
				else if(uname.equals("")){
					
					return;
				}
				else if(pass.equals("")){
					
					return;
				}
				else if(repass.equals("")){
					
					return;
				}
				else if(mail.equals("")){
					
					return;
				}
				else if(sex.equals("")){
					
					return;
				}
				//parentFrame.setVisible(false);
				
			
				try {
					clientSocket=new Socket(Main.defaulIP,Main.defaulfPort);
					
					
					String signupReq="SIGNUP>>>"+uname+">>>"+fullname+">>>"+pass+
							">>>"+mail+">>>"+sex;
					outputStream=new DataOutputStream(clientSocket.getOutputStream());
					inputStream=new DataInputStream(clientSocket.getInputStream());
					
					outputStream.writeUTF(signupReq);
					
					System.out.println("Send : "+signupReq);
					
					String response=inputStream.readUTF();
					
					if(response.equals("EXIST")){
						
						JOptionPane.showMessageDialog(null,"Username already exists!","Duplicate User!",JOptionPane.ERROR_MESSAGE);
						userName.setText("");
					}
					else if(response.equals("SIGNED_UP")){
						
						new WelcomeFrame(fullname,uname,clientSocket);
						parentFrame.setVisible(false);
						parentFrame.dispose();
					}
					
					
				} catch (UnknownHostException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					
				 JOptionPane.showMessageDialog(loginFrame,"Couldn't get I/O for the connection to the server: "
					          + Main.defaulIP,"I/O Error!",JOptionPane.ERROR_MESSAGE);
					
				}
				
				
				
			}
			
			
		}
	
	
}