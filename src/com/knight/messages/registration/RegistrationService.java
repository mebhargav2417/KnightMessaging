package com.knight.messages.registration;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Calendar;
import java.util.Date;
import java.sql.Statement;
import java.text.SimpleDateFormat;

import javax.json.JsonObject;
import javax.ws.rs.Consumes;
import javax.ws.rs.core.Response;

import org.json.simple.JSONObject;

import com.knight.messages.dbconnection.ConnectToDatabase;
import com.knight.messages.other.ConstantValues;
import com.knight.messages.other.SendMail;
import com.knight.messages.registration.Registration;


@Path("/registrationService")
public class RegistrationService {

	@POST
	@Path("/registration")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response newUser(Registration registration){
		String result = "", userId = "";
		JSONObject resultJson = new JSONObject();
		try{
			ConnectToDatabase connection = new ConnectToDatabase();
			Connection dbConnection = connection.getConnection();
			if(dbConnection != null) {
				Statement statement = dbConnection.createStatement();
				dbConnection.setAutoCommit(false);
				if(registration.getBusinessType().equalsIgnoreCase("individual")) {
					userId = ConstantValues.userIdIndividual;
				} else {
					userId = ConstantValues.usreIdOther;
				}
				String countQuery = "select count(*) from " + ConstantValues.registrationTable ;
				ResultSet restultSet = statement.executeQuery(countQuery);
				int count = 0;
				while (restultSet.next()) {
					count = restultSet.getInt(1);
				}
				System.out.println("Count: "+count);
				count++;
				String strCount = String.valueOf(count);
				if(strCount.length() == 1) {
					userId = userId + "0000" + strCount;
				} else if(strCount.length() == 2) {
					userId = userId + "000" + strCount;
				} else if(strCount.length() == 3) {
					userId = userId + "00" + strCount;
				} else if(strCount.length() == 4) {
					userId = userId + "0" + strCount;
				} else {
					userId = userId + strCount;
				}
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Date  date = new Date();
				Calendar cal = Calendar.getInstance();
				String insertQuery = "insert into " + ConstantValues.registrationTable +" values ( " + count +"," 
						+"'"+ registration.getBusinessType() +"','"+ registration.getBusinessTypeName() +"','"
						+ registration.getFirstName() +"','"+ registration.getSurName() +"','"+ registration.getBusinessName() +"','"
						+ registration.getDoorNo() +"','"+ registration.getSex() +"','"+registration.getDob()+"','"
						+ registration.getPanNo() + "','" + registration.getAadharNo() +"','"+registration.getSeniorCtznApplicability()+"','"
						+ registration.getSeniorCtznClause() +"','"+ registration.getMobileNo() +"','" +registration.getAltMobileNo() +"','"
						+ registration.getAuthoriseSignaroty1() +"','"+ registration.getMobileAsig1() +"','"
						+ registration.getAuthoriseSignatory2() +"','"+ registration.getMobileAsig2() +"','"
						+ registration.getCinOrUinNo() +"','" + registration.getGstApplicability() +"','" 
						+ registration.getGstNo() +"','"+ registration.getGstType() +"','"
						+ registration.getTurnOver() +"','" + registration.getRevMechanismApplicability() +"','"
						+ registration.getSezOrStpiApplicability() +"','"+ registration.getEf1() +"','" + registration.getEf2() +"','"
						+ registration.getEf3() +"','" + registration.getMail() +"','" 
						+ registration.getAccno1() +"','"+ registration.getAccno1Type() +"','"+ registration.getBankname1() +"','" + registration.getIfsc1() +"','"
						+ registration.getAccno2() +"','"+ registration.getAccno2Type() +"','"+ registration.getBankName2() +"','" + registration.getIfsc2() +"','"
						+ registration.getAccno3() +"','"+ registration.getAccno3Type() +"','"+ registration.getBankname3() +"','" + registration.getIfsc3() +"','"
						+ registration.getAccno4() +"','"+ registration.getAccno4Type() +"','"+ registration.getBankname4() +"','" + registration.getIfsc4() +"','"
						+ userId +"','"+ registration.getExportBusApplicability() +"','" + registration.getExportRegulationNo() +"','"
						+ registration.getCreatedBy() +"','" + new java.sql.Date(cal.getTimeInMillis()) +"','" + new java.sql.Date(cal.getTimeInMillis()) +"','"
						+ registration.getLastModifiedBy() +"','"+ registration.getTanNo() +"','" + registration.getLocality() +"','"
						+ registration.getStreet() + "','" + registration.getCity() +"','"+ registration.getState() +"','" + registration.getPincode() +"','"
						+ registration.getEnabled() +"')";
				statement.executeUpdate(insertQuery);
				String serviceQuery = "insert into " + ConstantValues.serviceTable + " values ( '" + userId + "','yes','" + registration.getMail() + "','no','no','no','no','no','no','no','no','no','"+registration.getCreatedBy()+"','"+new java.sql.Date(cal.getTimeInMillis())+"')" ;
				statement.executeUpdate(serviceQuery);	
				String pwd = userId;
				String loginQuery = "insert into " + ConstantValues.lgnTable + " values ( '" + userId + "','" + pwd + "','" + new java.sql.Date(cal.getTimeInMillis()) + "','" 
						+ new java.sql.Date(cal.getTimeInMillis()) + "','yes','" + registration.getMail() + "')";
				statement.executeUpdate(loginQuery);
				statement.close();
				dbConnection.commit();
				dbConnection.close();
				resultJson.put("code", ConstantValues.successCode);
				resultJson.put("message", "Registration success");
				resultJson.put("userId", userId);
				SendMail mail = new SendMail();
				mail.sendMail(registration.getMail(), "Registration success","Hi,\n\t You have been successfully registered with user id: " + userId);
				return Response.ok(resultJson, MediaType.APPLICATION_JSON).build();
			} else {
				resultJson.put("code", ConstantValues.errorCode);
				resultJson.put("message", "Unable to connect to database");
				return Response.ok(resultJson,MediaType.APPLICATION_JSON).build();
			}
		} catch(Exception e) {
			resultJson.put("code", ConstantValues.exceptionCode);
			resultJson.put("message", e.toString());
			return Response.ok(resultJson,MediaType.APPLICATION_JSON).build();
		}
	}
	
	@POST
	@Path("/updationUser")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateUser(Registration registration){
		JSONObject resultJson = new JSONObject();
		try{
			ConnectToDatabase connection = new ConnectToDatabase();
			Connection dbConnection = connection.getConnection();
			if(dbConnection != null) {
				Statement statement = dbConnection.createStatement();
				dbConnection.setAutoCommit(false);
				String sql = "UPDATE " + ConstantValues.registrationTable +
		                   "SET age = 30 WHERE id in (100, 101)";
				statement.executeUpdate(sql);
				dbConnection.commit();
				statement.close();
				dbConnection.close();
				resultJson.put("code", ConstantValues.successCode);
				resultJson.put("message", "Data updated successfully");
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
	
	@POST
	@Path("/disableUser")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response disableUSer(Id id){
		JSONObject resultJson = new JSONObject();
		try{
			ConnectToDatabase connection = new ConnectToDatabase();
			Connection dbConnection = connection.getConnection();
			if(dbConnection != null) {
				Statement statement = dbConnection.createStatement();
				dbConnection.setAutoCommit(false);
				String sql = "UPDATE " + ConstantValues.registrationTable +
		                   "SET enabled = 'no' WHERE userid = " + "'" + id.getUserId()  + "'";
				statement.executeUpdate(sql);
				dbConnection.commit();
				statement.close();
				dbConnection.close();
				resultJson.put("code", ConstantValues.successCode);
				resultJson.put("message", "User deleted successfully");
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
	
	@POST
	@Path("/updateServices")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateUser(TAF tafServices){
		JSONObject resultJson = new JSONObject();
		try{
			ConnectToDatabase connection = new ConnectToDatabase();
			Connection dbConnection = connection.getConnection();
			if(dbConnection != null) {
				Statement statement = dbConnection.createStatement();
				dbConnection.setAutoCommit(false);
				Calendar cal = Calendar.getInstance();
				 String sql = "UPDATE " + ConstantValues.serviceTable +
		                   "SET accounting_services = '" +tafServices.getAccountingServices()+ "', account_and_audit_services = '" + tafServices.getAccountngAndAuditServices()+ "',"
		                   		+ " audit_services = '" +tafServices.getAuditServices()+ "', gst_service = '" +tafServices.getGstServices()+ "', account_and_gst = '" +tafServices.getAccountingAndGstServices()+ "',"
		                   				+ "account_audit_and_gst = '" +tafServices.getAccountingAuditGstService()+ "', financial_advisory = '" +tafServices.getFinancialAdvisory()+ "', tds_service = '" +tafServices.getTdsService()+ "',"
		                   						+ " payroll_service = '" +tafServices.getPayrollService()+ "',updated_by = '" +tafServices.getCreatedBy()+"', ='" +new java.sql.Date(cal.getTimeInMillis())+ "' WHERE user_id='" +tafServices.getUserId()+"'";
				statement.executeUpdate(sql);
				dbConnection.commit();
				statement.close();
				dbConnection.close();
				resultJson.put("code", ConstantValues.successCode);
				resultJson.put("message", "Services updated successfully");
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
