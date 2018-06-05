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

@Path("/tafservices")
public class TAFServices {
	@POST
	@Path("/update")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateTAFServices(TAF taf){
		JSONObject resultJson = new JSONObject();
		try{
			ConnectToDatabase connection = new ConnectToDatabase();
			Connection dbConnection = connection.getConnection();
			if(dbConnection != null) {
				Statement statement = dbConnection.createStatement();
				dbConnection.setAutoCommit(false);
				Calendar cal = Calendar.getInstance();
				String sql = "update " + ConstantValues.serviceTable + " set accounting_services = '" + taf.getAccountingServices() + "',"
						+ "account_and_audit_services = '" + taf.getAccountngAndAuditServices() + "',"
						+ "audit_services = '" + taf.getAuditServices() + "',"
						+ "gst_service = '" + taf.getGstServices() + "',"
						+ "account_and_gst = '" + taf.getAccountingAndGstServices() + "',"
						+ "account_audit_and_gst = '" + taf.getAccountingAuditGstService() + "',"
						+ "financial_advisory = '" + taf.getFinancialAdvisory() + "',"
						+ "tds_service = '" + taf.getTdsService() + "',"
						+ "payroll_service = '" + taf.getPayrollService() + "',"
						+ "updated_by = '" + taf.getCreatedBy() + "',"
						+ "updated_date = '" + new java.sql.Date(cal.getTimeInMillis()) + "' where user_id = '" + taf.getUserId() + "'";
				statement.executeUpdate(sql);
				dbConnection.commit();
				statement.close();
				dbConnection.close();
				resultJson.put("code", ConstantValues.successCode);
				resultJson.put("message", "Data updated successfully");
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
}
