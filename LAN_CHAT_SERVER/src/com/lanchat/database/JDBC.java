package com.lanchat.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class JDBC {


	private String DB_URL="jdbc:mysql://localhost/ChatManagerBackup";
	private String user="root";
	private String password="root";
	private String driver="com.mysql.jdbc.Driver";
	
	
	Connection getDbConnection() throws ClassNotFoundException, SQLException{
		
		Connection conn=null;
		
		Class.forName(driver);
		System.out.println("Connecting to database...");
		
		conn=DriverManager.getConnection(DB_URL, user, password);
		
		return conn;

	}
	
	
	
	
	
}