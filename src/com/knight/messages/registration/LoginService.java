package com.knight.messages.registration;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.simple.JSONObject;

import com.knight.messages.dbconnection.ConnectToDatabase;
import com.knight.messages.other.ConstantValues;
import com.knight.messages.other.SendMail;

@Path("/loginService")
public class LoginService {
	
	@POST
	@Path("/auth")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response authUser(Login login){
		JSONObject resultJson = new JSONObject();
		try{
			ConnectToDatabase connection = new ConnectToDatabase();
			Connection dbConnection = connection.getConnection();
			if(dbConnection != null) {
				Statement statement = dbConnection.createStatement();
				String sql = "select userid from " + ConstantValues.lgnTable + " where userid = '" + login.getId() + "' and userid_dwp = '" + login.getPwd() + "'";
				ResultSet restultSet = statement.executeQuery(sql);
				if(restultSet != null){
					
				}
				resultJson.put("code", ConstantValues.successCode);
				resultJson.put("message", "Authenticated successfully");
				statement.close();
				dbConnection.close();
				return Response.ok(resultJson, MediaType.APPLICATION_JSON).build();
			} else {
				resultJson.put("code", ConstantValues.errorCode);
				resultJson.put("message", "Unable to connect database");
				return Response.ok(resultJson, MediaType.APPLICATION_JSON).build();
			}			
		} catch(Exception e){
			resultJson.put("code", ConstantValues.exceptionCode);
			resultJson.put("message", e.toString());
			return Response.ok(resultJson, MediaType.APPLICATION_JSON).build();
		}
	}
	
	@GET
	@Path("/mail")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response mailSend(){
		JSONObject resultJson = new JSONObject();
		SendMail mail = new SendMail();
		resultJson.put("response", mail.sendMail("bhargav4366@gmail.com", "testing","got the mail hurrayyyy"));
		return Response.ok(resultJson, MediaType.APPLICATION_JSON).build();
	}

}
