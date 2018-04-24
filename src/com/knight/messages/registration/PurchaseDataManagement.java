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

@Path("/purchasesManagement")
public class PurchaseDataManagement {
	@POST
	@Path("/insert")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response insertPurchases(Purchases purchases){
		JSONObject resultJson = new JSONObject();
		try{
			ConnectToDatabase connection = new ConnectToDatabase();
			Connection dbConnection = connection.getConnection();
			if(dbConnection != null) {
				Statement statement = dbConnection.createStatement();
				Calendar cal = Calendar.getInstance();
				 String sql = "insert into " + ConstantValues.PurchasesTable + " values ('" + purchases.getUserId() + "','" + purchases.getDate() + "','" + purchases.getInvoiceNo() + "','" + purchases.getPurchaserName() + "','" + purchases.getGstApplicability() + "','"
						 + purchases.getGstNo() + "','" + purchases.getInvoiceNo() + "','" + purchases.getTaxableValue() + "','" + purchases.getTaxPercentage() + "','" + purchases.getTax() + "','" + purchases.getTotalValue() + "','" + purchases.getHsnSacCode() + "','"
						 + purchases.getEf1() + "','" + purchases.getEf2() + "','" +  new java.sql.Date(cal.getTimeInMillis()) + "','" + purchases.getUpdatedBy() +"')";
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
