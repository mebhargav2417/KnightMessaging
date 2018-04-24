package com.knight.messages.dbconnection;

import java.sql.DriverManager;
import java.util.Properties;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;


public class ConnectToDatabase {
	
	Connection connection;
	Properties properties = new Properties();
	InputStream inputStream;
	
	public ConnectToDatabase(){
		try{
			
		} catch(Exception e){
			
		}
	}
	
	public Connection getConnection(){
		try{
//			inputStream = new FileInputStream("config.properties");
//			properties.load(inputStream);
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/project_knight","root", "admin");
//			System.out.println(properties.getProperty("dburl")+properties.getProperty("dbuser")+ properties.getProperty("dbpwd"));
//			connection = DriverManager.getConnection(properties.getProperty("dburl"),properties.getProperty("dbuser"), properties.getProperty("dbpwd"));
			if (connection != null) {
				System.out.println("You made it, take control your database now!");
			} else {
				System.out.println("Failed to make connection!");
			}
		} catch(Exception e){
			
		}
		return connection;
	}

}
