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

@Path("/salesManagement")
public class SalesDataManagement {
	@POST
	@Path("/insert")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response insertSales(Sales sales){
		JSONObject resultJson = new JSONObject();
		try{
			ConnectToDatabase connection = new ConnectToDatabase();
			Connection dbConnection = connection.getConnection();
			if(dbConnection != null) {
				Statement statement = dbConnection.createStatement();
				Calendar cal = Calendar.getInstance();
				 String sql = "insert into " + ConstantValues.SalesTable + " values ('" + sales.getUserId() + "','" + sales.getDate() + "','" + sales.getInvoiceNo() + "','" + sales.getReceiverName() + "','" + sales.getGstApplicability() + "','"
						 + sales.getGstNo() + "','" + sales.getInvoiceNo() + "','" + sales.getTaxableValue() + "','" + sales.getTaxPercentage() + "','" + sales.getTax() + "','" + sales.getTotalValue() + "','" + sales.getHsnSacCode() + "','"
						 + sales.getEf1() + "','" + sales.getEf2() + "','" +  new java.sql.Date(cal.getTimeInMillis()) + "','" + sales.getUpdatedBy() +"')";
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
