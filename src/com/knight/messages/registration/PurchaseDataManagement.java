package com.knight.messages.registration;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.simple.JSONArray;
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
				dbConnection.setAutoCommit(false);
				Calendar cal = Calendar.getInstance();
				 String sql = "insert into " + ConstantValues.PurchasesTable + " values ('" + purchases.getUserId() + "','" + purchases.getDate() + "','" + purchases.getInvoiceNo() + "','" + purchases.getPurchaserName() + "','" + purchases.getGstApplicability() + "','"
						 + purchases.getGstNo() + "','" + purchases.getInvoiceNo() + "','" + purchases.getTaxableValue() + "','" + purchases.getTaxPercentage() + "','" + purchases.getTax() + "','" + purchases.getTotalValue() + "','" + purchases.getHsnSacCode() + "','"
						 + purchases.getEf1() + "','" + purchases.getEf2() + "','" +  new java.sql.Date(cal.getTimeInMillis()) + "','" + purchases.getUpdatedBy() +"')";
				statement.executeUpdate(sql);
				dbConnection.commit();
				statement.close();
				dbConnection.close();
				resultJson.put("code", ConstantValues.successCode);
				resultJson.put("message", "Data inserted successfully");
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
	
	@POST
	@Path("/view")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response viewPurchases(ViewRequestInput viewRequestInput){
		JSONObject result = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		try{
			ConnectToDatabase connection = new ConnectToDatabase();
			Connection dbConnection = connection.getConnection();
			if(dbConnection != null) {
				Statement statement = dbConnection.createStatement();
				Calendar cal = Calendar.getInstance();
				if(viewRequestInput.getDateInterval().isEmpty() || viewRequestInput.getDateInterval().equalsIgnoreCase("no") || viewRequestInput.getDateInterval().equalsIgnoreCase("false")) {
					String query = "select * from " + ConstantValues.PurchasesTable + " where userId = '" + viewRequestInput.getUserId() +"' LIMIT 5";
					ResultSet restultSet = statement.executeQuery(query);
					List list = new ArrayList();
					if(restultSet != null) {
						ResultSetMetaData metaData = restultSet.getMetaData();
						HashMap<String,Object> columnMap = new HashMap<String, Object>();
						if(!restultSet.isBeforeFirst()) {
							result.put("code", ConstantValues.emptyDataCode);
							result.put("message", "No Data available");
							JSONObject res = new JSONObject();
							res.put("response", result);
							jsonArray.add(res);
						} else {
							while (restultSet.next()) {
								JSONObject resultJson = new JSONObject();
								for(int columnIndex=1;columnIndex<=metaData.getColumnCount();columnIndex++)
								{
									if(restultSet.getString(metaData.getColumnName(columnIndex))!=null)
										resultJson.put(metaData.getColumnLabel(columnIndex), restultSet.getString(metaData.getColumnName(columnIndex)));
									else
										resultJson.put(metaData.getColumnLabel(columnIndex), "");
								}
								jsonArray.add(resultJson);
							}
							result.put("code", ConstantValues.successCode);
							result.put("message", "Data retrieved successfully");
							JSONObject res = new JSONObject();
							res.put("response", result);
							jsonArray.add(res);
						}						
					} else {
						result.put("code", ConstantValues.emptyDataCode);
						result.put("message", "No Data available");
						JSONObject res = new JSONObject();
						res.put("response", result);
						jsonArray.add(res);
					}
					restultSet.close();
				} else {
					String query = "select * from " + ConstantValues.PurchasesTable + " where userId = '" + viewRequestInput.getUserId() +"' AND date_invoice between '" + viewRequestInput.getFromDate()
					+ "' and '" + viewRequestInput.getToDate() + "'";
					ResultSet restultSet = statement.executeQuery(query);
					List list = new ArrayList();
					if(restultSet != null) {
						ResultSetMetaData metaData = restultSet.getMetaData();
						HashMap<String,Object> columnMap = new HashMap<String, Object>();	
						if(!restultSet.isBeforeFirst()){
							result.put("code", ConstantValues.emptyDataCode);
							result.put("message", "No Data available");
							JSONObject res = new JSONObject();
							res.put("response", result);
							jsonArray.add(res);
						} else {
							while (restultSet.next()) {
								JSONObject resultJson = new JSONObject();
								for(int columnIndex=1;columnIndex<=metaData.getColumnCount();columnIndex++)
								{
									if(restultSet.getString(metaData.getColumnName(columnIndex))!=null)
										resultJson.put(metaData.getColumnLabel(columnIndex), restultSet.getString(metaData.getColumnName(columnIndex)));
									else
										resultJson.put(metaData.getColumnLabel(columnIndex), "");
								}
								jsonArray.add(resultJson);
							}
							result.put("code", ConstantValues.successCode);
							result.put("message", "Data retrieved successfully");
							JSONObject res = new JSONObject();
							res.put("response", result);
							jsonArray.add(res);
						}
						
					} else {
						result.put("code", ConstantValues.emptyDataCode);
						result.put("message", "No Data available");
						JSONObject res = new JSONObject();
						res.put("response", result);
						jsonArray.add(res);
					}	
					restultSet.close();
				}				
				statement.close();
				dbConnection.close();
				return Response.ok(jsonArray, MediaType.APPLICATION_JSON).build();
			} else {
				result.put("code", ConstantValues.errorCode);
				result.put("message", "Unable to connect database");
				JSONObject res = new JSONObject();
				res.put("response", result);
				jsonArray.add(res);
				return Response.ok(result, MediaType.APPLICATION_JSON).build();
			}			
		} catch(Exception e){
			result.put("code", ConstantValues.exceptionCode);
			result.put("message", e.toString());
			JSONObject res = new JSONObject();
			res.put("response", result);
			jsonArray.add(res);
			return Response.ok(result, MediaType.APPLICATION_JSON).build();
		}
	}
}
