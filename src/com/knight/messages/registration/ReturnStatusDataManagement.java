package com.knight.messages.registration;

import java.sql.Connection;
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
import com.sun.corba.se.impl.orbutil.RepositoryIdUtility;

@Path("/returnStatusManagement")
public class ReturnStatusDataManagement {
	@POST
	@Path("/insert")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response insertNetProfit(ReturnStatus returnStatus){
		JSONObject resultJson = new JSONObject();
		try{
			ConnectToDatabase connection = new ConnectToDatabase();
			Connection dbConnection = connection.getConnection();
			if(dbConnection != null) {
				Statement statement = dbConnection.createStatement();
				Calendar cal = Calendar.getInstance();
				 String sql = "insert into " + ConstantValues.ReturnStatusTable + " values ('" + returnStatus.getUserId() + "','" + returnStatus.getTypeOfService() + "','" + returnStatus.getAmount() + "','" + returnStatus.getDate() + "','" 
						 + returnStatus.getAcknowledgementNo() + "','" +  new java.sql.Date(cal.getTimeInMillis()) + "','" + returnStatus.getUpdatedBy() +"')";
				 statement.executeUpdate(sql);
				resultJson.put("code", ConstantValues.successCode);
				resultJson.put("message", "Data inserted successfully");
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
}
