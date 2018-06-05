package com.knight.messages.registration;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Calendar;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.simple.JSONObject;

import com.knight.messages.dbconnection.ConnectToDatabase;
import com.knight.messages.other.ConstantValues;
import com.knight.messages.other.SendMail;
import com.sun.org.apache.xml.internal.security.utils.Base64;

@Path("/forTafPeople")
public class ForTafRegLogin {
	@POST
	@Path("/registration")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response newReg(RegistrationTAF registration){
		JSONObject resultJson = new JSONObject();
		try{
			ConnectToDatabase connection = new ConnectToDatabase();
			Connection dbConnection = connection.getConnection();
			if(dbConnection != null) {
				Statement statement = dbConnection.createStatement();
				dbConnection.setAutoCommit(false);
				//Calendar cal = Calendar.getInstance(); new java.sql.Date(cal.getTimeInMillis())
				String d = new String(Base64.decode(registration.getDwp().getBytes()));
//				System.out.println(d);
				MessageDigest digest = MessageDigest.getInstance("SHA-256");
//				byte[] hash = digest.digest(d.getBytes(StandardCharsets.UTF_8));
//				System.out.println(hash.toString());
				digest.update(d.getBytes());
		        
		        byte byteData[] = digest.digest();
		 
		        //convert the byte to hex format method 1
		        StringBuffer sb = new StringBuffer();
		        for (int i = 0; i < byteData.length; i++) {
		         sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
		        }
		     
		        System.out.println("Hex format : " + sb.toString());
				java.sql.Timestamp date = new java.sql.Timestamp(new java.util.Date().getTime());
				String sql = "insert into " + ConstantValues.TAFRegForEmployee + " values ('" + registration.getMail() + "','" + registration.getUserName() + "','"
						+ sb.toString() + "','" + date + "','" + registration.getRole() + "')";  
				statement.executeUpdate(sql);
				dbConnection.commit();
				statement.close();
				dbConnection.close();
				resultJson.put("code", ConstantValues.successCode);
				resultJson.put("message", "Registration success");
				resultJson.put("username", registration.getUserName());
				SendMail mail = new SendMail();
				mail.sendMail(registration.getMail(), "Registration success","Hi,\n\t You have been successfully registered with user name: " + registration.getUserName() + " and Mail: " + registration.getMail());
				return Response.ok(resultJson, MediaType.APPLICATION_JSON).build();
			} else {
				resultJson.put("code", ConstantValues.errorCode);
				resultJson.put("message", "Unable to connect to database");
				return Response.ok(resultJson,MediaType.APPLICATION_JSON).build();
			}
		} catch(Exception e){
			resultJson.put("code", ConstantValues.exceptionCode);
			resultJson.put("message", e.toString());
			return Response.ok(resultJson,MediaType.APPLICATION_JSON).build();		
		}
	}
	
	@POST
	@Path("/login")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response login(LoginTAF login){
		JSONObject resultJson = new JSONObject();
		try{
			ConnectToDatabase connection = new ConnectToDatabase();
			Connection dbConnection = connection.getConnection();
			if(dbConnection != null) {
				Statement statement = dbConnection.createStatement();
				dbConnection.setAutoCommit(false);
				String d = new String(Base64.decode(login.getDwp().getBytes()));
//				System.out.println(d);
				MessageDigest digest = MessageDigest.getInstance("SHA-256");
//				byte[] hash = digest.digest(d.getBytes(StandardCharsets.UTF_8));
//				System.out.println(hash.toString());
				digest.update(d.getBytes());		        
		        byte byteData[] = digest.digest();		 
		        //convert the byte to hex format method 1
		        StringBuffer sb = new StringBuffer();
		        for (int i = 0; i < byteData.length; i++) {
		         sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
		        }
		     
		        System.out.println("Hex format : " + sb.toString());
				java.sql.Timestamp date = new java.sql.Timestamp(new java.util.Date().getTime());
				String sql = "select role, username from " + ConstantValues.TAFRegForEmployee + " where dwp = '" + sb.toString() + "' and mail = '" + login.getMail() + "' and username = '" + login.getUserName() +"'";  
				System.out.println(sql);
				ResultSet rs = statement.executeQuery(sql);
				String role = "", username = "";
				while(rs.next()){
					role = rs.getString("role");
					username = rs.getString("username");
				}
				rs.close();
				if(username.length() != 0) {
					resultJson.put("code", ConstantValues.successCode);
					resultJson.put("message", "Authentication success");
					resultJson.put("username", username);
					resultJson.put("role", role);
				} else {
					resultJson.put("code", ConstantValues.errorCode);
					resultJson.put("message", "Authentication failed, no user found with entered credentials");
				}
				statement.close();
				dbConnection.close();
				return Response.ok(resultJson, MediaType.APPLICATION_JSON).build();
			} else {
				resultJson.put("code", ConstantValues.errorCode);
				resultJson.put("message", "Unable to connect to database");
				return Response.ok(resultJson,MediaType.APPLICATION_JSON).build();
			}
		} catch(Exception e){
			resultJson.put("code", ConstantValues.exceptionCode);
			resultJson.put("message", e.toString());
			return Response.ok(resultJson,MediaType.APPLICATION_JSON).build();		}
		
	}
}
