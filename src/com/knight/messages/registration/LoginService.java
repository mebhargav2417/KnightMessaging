package com.knight.messages.registration;

import java.sql.Connection;
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
				 String sql = "UPDATE " + ConstantValues.registrationTable +
		                   "SET age = 30 WHERE id in (100, 101)";
				 statement.executeUpdate(sql);
				resultJson.put("code", ConstantValues.successCode);
				resultJson.put("message", "Authenticated successfully");
				statement.close();
				dbConnection.close();
				return Response.ok(resultJson, MediaType.APPLICATION_JSON).build();
			} else {
				resultJson.put(ConstantValues.errorCode, "Unable to connect database");
				return Response.ok(resultJson, MediaType.APPLICATION_JSON).build();
			}			
		} catch(Exception e){
			resultJson.put(ConstantValues.exceptionCode, e.toString());
			return Response.ok(resultJson, MediaType.APPLICATION_JSON).build();
		}
	}
	
	@GET
	@Path("/mail")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response mailSend(){
		JSONObject resultJson = new JSONObject();
		SendMail mail = new SendMail();
		resultJson.put("response", mail.sendMail("bhargav4366@gmail.com", "testing"));
		return Response.ok(resultJson, MediaType.APPLICATION_JSON).build();
	}

}
