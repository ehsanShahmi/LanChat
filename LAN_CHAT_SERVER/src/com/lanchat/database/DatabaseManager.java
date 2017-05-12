package com.lanchat.database;

import com.lanchat.networking.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class DatabaseManager {
	
	

	Connection conn = null;
	Statement statement;
	ResultSet result;
	JDBC jdbc=new JDBC();
	static int id = 256;
	public DatabaseManager()
	{
		try {
			 conn=jdbc.getDbConnection();
		}catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		System.out.println("Database connection created");
	}
	
	
	public String getPassword(String str)		
	{
		String sql="select * from UserInfo";
		
		System.out.println("In the get password method for user : "+str);
		
		try {
			statement = (Statement) conn.createStatement();
			result=statement.executeQuery(sql);
			while(result.next())
			{
				String username = result.getString(1);
				String password = result.getString(3);
				//System.out.println(username);
				//System.out.println(str);
				
				if(username.equals(str))
				{
					System.out.println("matched");
					result.close();
					return password;
					//break;
				}
			}
			
			
			
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
		
		
		
		return null;
		
		
	}
	
	
	public int isOnline(String str)
	{
		
		String sql="select * from Online";
		//System.out.println("input parameter " + str);
		int status = 10;
		try {
			statement = (Statement) conn.createStatement();
			result=statement.executeQuery(sql);
			while(result.next())
			{
				String username = result.getString(1);
				String res = result.getString(2);
				
				//System.out.println(username);
				//System.out.println(res);
				status = Integer.parseInt(res);
				//System.out.println(status);
				
				if(username.equals(str))
				{
					System.out.println("status found");
					
					//System.out.println(status);
					result.close();
					//return status;
					break;
				}
			}
			
		
			
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return status;
		
	}
	
	
	
		public void setOnline(String str,int status){
		 	  
			String query = "update Online set status = ? where username = ?;";
		     
		      try {
		    	PreparedStatement preparedStmt = conn.prepareStatement(query);
				preparedStmt.setInt(1, status);
				preparedStmt.setString(2, str);
			    preparedStmt.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		     
		}
		
		
		public boolean isExist(String username) 
		{
			boolean flag = false;
			ArrayList<String> exist = new ArrayList<String>();
			exist = getAllClients();
			
			if(exist.contains(username))
			{
				return true;
			}
			
			return false;
			
			
			
		}

		
		public ArrayList<String> getAllClients()
		{
			ArrayList<String> list = new ArrayList<String>();
			ResultSet rs = null;
			
			String query = "select * from UserInfo";
			PreparedStatement preparedStmt;
			
			try {
				preparedStmt = conn.prepareStatement(query);
				rs = preparedStmt.executeQuery();
			    while(rs.next())
			    {
			    	String str = rs.getString(1);
			    	list.add(str);
			    }
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		    return list;
		    		
	 }

		public void initOnline(String username,int status) 
		{
			PreparedStatement ps;
			try {
				ps = (PreparedStatement) conn.prepareStatement("insert into Online(username, status) values(?,?)");
				ps.setString(1,username);
		        ps.setInt(2,status);
		        ps.executeUpdate();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
		public void signUp(String username, String name,String password,String email,String gender)
		{
			try{
				PreparedStatement ps = (PreparedStatement) conn.prepareStatement("insert into UserInfo(username, name, password,email,gender) values(?,?,?,?,?)");
				ps.setString(1,username);
				ps.setString(2,name);
				ps.setString(3,password);
				ps.setString(4,email);
				ps.setString(5, gender);       
				ps.executeUpdate();
				initOnline(username,1);
			}catch(SQLException e){
				
				e.printStackTrace();
			}
	   }
		
		
		public ArrayList getFriends(String user)
		{
			ArrayList<String> al = new ArrayList<String>();
			ResultSet rs = null;
			String query = "SELECT * FROM FriendList where username=? ";
			try{
			PreparedStatement preparedStmt = conn.prepareStatement(query);
		    preparedStmt.setString(1, user);
		    rs = preparedStmt.executeQuery();
		    while(rs.next())
		    {
		    	String str = rs.getString(2);
		    	al.add(str);

		    }
			
			}catch(SQLException e){
				
				e.printStackTrace();
			}
		    
		    return al;
		  
			
		}
		
		
		
		public ArrayList<String> getOnlineList(String user){
			ArrayList<String> al = new ArrayList<String>();
			ArrayList<String> online = new ArrayList<String>();
			
			al = getFriends(user);	
			for(int i = 0;i<al.size();i++)
			{
				int status =  isOnline(al.get(i));
				if(status == 1)
				{
					online.add(al.get(i));
				}	
			}
			
				return online;
		}
		
		
		public void addFriend(String user1, String user2) 
		{
			try{
			PreparedStatement ps = (PreparedStatement) conn.prepareStatement("insert into FriendList(username,friend_username) values(?,?)");
			ps.setString(1,user1);
	        ps.setString(2,user2);
	        ps.executeUpdate();
	       
	        ps.setString(1,user2);
	        ps.setString(2,user1);
	        ps.executeUpdate();
	        
			}catch(SQLException e){
				e.printStackTrace();
			}
			
	  }
		
	  
	 
		public void close() {
		    try {
		      if (result != null) {
		        result.close();
		      }

		      if (statement != null) {
		        statement.close();
		      }

		      if (conn != null) {
		        conn.close();
		      }
		    } catch (Exception e) {

		    }
	 }
	

}
