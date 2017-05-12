package com.lanchat.gui;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.*;

import com.lanchat.main.*;
/***
 * 
 * @author plabon
 *
 */


public class LoginFrame extends JFrame{
	
		JPanel mainPanel;
		
		public LoginFrame(){
			this.setTitle("Lan Chat");
			this.setDefaultCloseOperation(EXIT_ON_CLOSE);
			
			this.setVisible(true);
			this.setMinimumSize(Main.minFrameSize);
			//this.setResizable(false);
			this.add(new LoginPanel(this));
			
			this.pack();
		}
	
}

/***To understand the work flow go to actionPerFormed() method ... 
	Thats where all the action begins :P
***/


class LoginPanel extends JPanel implements ActionListener{
	
	//network elements
	
	private Socket clientSocket = null;
	private DataInputStream inputStream=null;
	private DataOutputStream outputStream=null;
	
	//credentials
	private String username=null;
	private String password=null;
	
	
	//Gui elemenets
	LoginFrame loginFrame;
	
	private Image background;
	
	private JLabel logo;
	private JLabel userNameLabel;
	private JLabel passwordLabel;
	private JButton signin;
	private JButton signup;
	private JTextField userNameField;
	private JPasswordField passwordField;
	
	private GridBagLayout gBLayout=new GridBagLayout();
	private GridBagConstraints gbc;
	
	public LoginPanel(LoginFrame loginFrame){
		
		this.loginFrame=loginFrame;
		
		background=new ImageIcon(this.getClass().getResource("/Background/sky6.jpg")).getImage();
		Dimension dimension=new Dimension(Main.WIDTH,Main.HEIGHT);
		this.setPreferredSize(dimension);
		this.setSize(dimension);
		this.setLayout(gBLayout);
		initComponent();
	}
	
	public void paintComponent(Graphics g) {
	    g.drawImage(background, 0, 0,getWidth(),getHeight(), null);
	 }
	
	
	private void initComponent(){
		
		//components
		
		ImageIcon logoIcon =new ImageIcon(this.getClass().getResource("/Logo/logo2.png"));
		
		Image img=logoIcon.getImage();
		
		img=img.getScaledInstance(100,100,java.awt.Image.SCALE_SMOOTH);
		
		logoIcon=new ImageIcon(img);
		
		logo=new JLabel(logoIcon);
		
		
		userNameLabel=new JLabel("Username: ");
		passwordLabel=new JLabel("Password:");
		
		
		
		userNameField=new JTextField(20);
		userNameField.setPreferredSize(new Dimension(1,40));
		//userName.setBackground(Color.LIGHT_GRAY);
		userNameField.setBorder(null);
		userNameField.setForeground(Color.BLACK);
		
		passwordField=new JPasswordField(20);
		passwordField.setPreferredSize(new Dimension(1,40));
		//password.setBackground(Color.LIGHT_GRAY);
		passwordField.setBorder(null);
		
		
		signin=new JButton("Sign In");
		signin.setBackground(Color.WHITE);
		signin.addActionListener(this);
		signin.setActionCommand("signin");
		
		signup= new JButton("Sign Up");
		signup.setBackground(Color.WHITE);
		signup.addActionListener(this);
		signup.setActionCommand("go to sign up");
	
		
		//Layout
		
	
		gbc=new GridBagConstraints();
		gbc.insets=new Insets(5,15,50,15);
		gbc.gridx=1;
		gbc.gridy=0;

		add(logo,gbc);
		
		gbc.insets=new Insets(20,15,15,15);
		gbc.anchor=GridBagConstraints.LINE_END;
		
		gbc.gridx=0;
		gbc.gridy=1;
		
		add(userNameLabel,gbc);
		
		gbc.gridx=0;
		gbc.gridy=2;
		
		add(passwordLabel,gbc);
		
		
		gbc.anchor=GridBagConstraints.LINE_START;
		
		gbc.gridx=1;
		gbc.gridy=1;
		add(userNameField,gbc);
		
		gbc.gridx=1;
		gbc.gridy=2;
		add(passwordField,gbc);
		
		//buttons
		
		gbc.anchor=GridBagConstraints.CENTER;
		gbc.gridx=1;
		gbc.gridy=3;
		add(signin,gbc);
		
		
		gbc.gridy=4;
		gbc.insets=new Insets(150,15,5,5);
		gbc.anchor=GridBagConstraints.LINE_END;
		add(new JLabel("Need an account?"),gbc);
		
		
		gbc.gridx=2;
		gbc.gridy=4;
		add(signup,gbc);
		
	}

	
	//This function perform actions after login or signup button pressed
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
		if(e.getActionCommand().equals("signin")){
			//JOptionPane.showMessageDialog(loginFrame, "Singin Button Response");
			
			
			username=userNameField.getText();
			password=passwordField.getText();
			
			
			if(username.equals("")){
				//show dialogue that password empty
				JOptionPane.showMessageDialog(loginFrame, "Username is empty!","Username Empty!",JOptionPane.ERROR_MESSAGE);
				//System.out.println("Password Empty");
				return;
				
			}
			
			else if(password.equals("")){
				//show dialogue that password empty
				JOptionPane.showMessageDialog(loginFrame, "Password is empty!","Password Empty!",JOptionPane.ERROR_MESSAGE);
				//System.out.println("Username Empty ");
				return;
			}
			
			///wish to take input from user :P
			
			int port=Main.defaulfPort;
			String serverAddr=Main.defaulIP;
			
			//creating a socket for the first time
			
			
			try {
				clientSocket=new Socket(serverAddr,port);
				inputStream=new DataInputStream(clientSocket.getInputStream());
				outputStream=new DataOutputStream(clientSocket.getOutputStream());
			} catch (UnknownHostException e1) {
					//Show dialog and return
				System.err.println("Server "+serverAddr+" not found");
				JOptionPane.showMessageDialog(loginFrame, "Server "+serverAddr+" not found",
						"Server not found!",JOptionPane.ERROR_MESSAGE);
				return;
				
			} catch (IOException e1) {
				
				JOptionPane.showMessageDialog(loginFrame,"Couldn't get I/O for the connection to the server: "
				          + serverAddr,"I/O Error!",JOptionPane.ERROR_MESSAGE);
				
				System.err.println("Couldn't get I/O for the connection to the server "
				          + serverAddr);
			}
			
			//Socket Creating finished // Lets do some I/O
			
			//if(inputStream!=null) System.out.println("We got our input stream");
			
			String loginRequest="LOGIN>>>"+username+">>>"+password;
			
			
			//sending login request
			try {
				outputStream.writeUTF(loginRequest);
				//outputStream.writeUTF("Something else");
			} catch (IOException e1) {}
			
			
			
			try {
				//String response=inputStream.readLine();
				
				String response=inputStream.readUTF();
				
				System.out.println(response);
				//Enter to main frame if everything is ok
		
				if(response.equals("OK")){
					
					System.out.println("Password Matched for"+username);
				
					new MainFrame(clientSocket,username);
				
					loginFrame.setVisible(false);
					loginFrame.dispose();
					
				}
				else if(response.equals("ONLINE")){
					
					JOptionPane.showMessageDialog(
							loginFrame, "User already online!",
							"User Online!",JOptionPane.ERROR_MESSAGE);
							passwordField.setText("");
							userNameField.setText("");
					
					return;
				
				}
				else if(response.equals("NO")) {
					
					JOptionPane.showMessageDialog(
							loginFrame, "Username and password didn't match!",
							"Wrong Password!",JOptionPane.ERROR_MESSAGE);
							passwordField.setText("");
					
					return;
				}
				
			} catch (IOException e1) {}
				
		
		}
		
		
		else if(e.getActionCommand().equals("go to sign up")){
			new SignupFrame(loginFrame);
			loginFrame.setVisible(false);
			loginFrame.dispose();
			System.out.println("Sign up button pressed");
		}
		
		
		
	}

	/**
	 * @return the clientSocket
	 */
	public Socket getClientSocket() {
		return clientSocket;
	}

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @return the loginFrame
	 */
	public LoginFrame getLoginFrame() {
		return loginFrame;
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
	
}