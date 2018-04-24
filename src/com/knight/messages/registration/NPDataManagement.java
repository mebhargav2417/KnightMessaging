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

@Path("/netprofitManagement")
public class NPDataManagement {
	@POST
	@Path("/insert")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response insertNetProfit(NP netProfit){
		JSONObject resultJson = new JSONObject();
		try{
			ConnectToDatabase connection = new ConnectToDatabase();
			Connection dbConnection = connection.getConnection();
			if(dbConnection != null) {
				Statement statement = dbConnection.createStatement();
				Calendar cal = Calendar.getInstance();
				 String sql = "insert into " + ConstantValues.NetProfitTable + " values ('" + netProfit.getUserId() + "','" + netProfit.getSalesValue() + "','" + netProfit.getIncomeValue() + "','" + netProfit.getPurchaseValue() + "','" + netProfit.getExpenseValue() + "','"
						 + netProfit.getDirectExpense() + "','" + netProfit.getIndirectExpense() + "','" + netProfit.getNetProfit() + "','" +  new java.sql.Date(cal.getTimeInMillis()) + "','" + netProfit.getUpdatededBy() +"')";
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
