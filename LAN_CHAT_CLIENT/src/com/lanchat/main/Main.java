package com.lanchat.main;
import java.awt.Dimension;

import com.lanchat.gui.*;

/**
 * @author plabon
 *
 */
public class Main {

	public static final int WIDTH=900;
	public static final int  HEIGHT=675;
	
	public static final Dimension minFrameSize=new Dimension(640,480);
	public static final Dimension defaultFrameSize=new Dimension(WIDTH,HEIGHT);
	
	
	public static String defaulIP="10.42.0.1";
	public static int defaulfPort=1214;
	
	
	public static void main(String args[]){
		new LoginFrame();
	}
}
