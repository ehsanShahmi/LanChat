package com.lanchat.gui;
import java.awt.Dimension;
import com.lanchat.main.*;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
/***
 * 
 * @author plabon
 *
 */


public class SignupFrame extends JFrame {
 
		private JFrame parentFrame;
		private SignupPanel signupPanel;
		
	
		
	
		public SignupFrame(JFrame parentFrame){
			
			this.parentFrame=parentFrame;
			this.setDefaultCloseOperation(EXIT_ON_CLOSE);
			//this.setSize(Main.WIDTH,Main.HEIGHT);
			this.setSize(parentFrame.getSize());
			this.setMinimumSize(Main.minFrameSize);
			this.setLocationRelativeTo(parentFrame);
			this.setVisible(true);
			initComponents();
		}
		
		
		private void initComponents(){
			signupPanel=new SignupPanel(this);
			add(signupPanel);
		}
	
}


