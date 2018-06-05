package com.knight.messages.registration;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.simple.JSONObject;

import com.knight.messages.dbconnection.ConnectToDatabase;
import com.knight.messages.other.ConstantValues;
import com.sun.javafx.collections.MappingChange.Map;

@Path("/getDataService")
public class ViewUserDataService {
	
	@POST
	@Path("/userData")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getUser(ViewUserData viewData){
		JSONObject resultJson = new JSONObject();
		try{
			ConnectToDatabase connection = new ConnectToDatabase();
			Connection dbConnection = connection.getConnection();
			if(dbConnection != null) {
				Statement statement = dbConnection.createStatement();
				String query = "select * from " + ConstantValues.registrationTable + " where userid = '" + viewData.getUserId() +"'";
				ResultSet restultSet = statement.executeQuery(query);
				List list = new ArrayList();
				if(restultSet != null) {
					ResultSetMetaData metaData = restultSet.getMetaData();
					HashMap<String,Object> columnMap = new HashMap<String, Object>();
					while (restultSet.next()) {
			                for(int columnIndex=1;columnIndex<=metaData.getColumnCount();columnIndex++)
			                {
			                    if(restultSet.getString(metaData.getColumnName(columnIndex))!=null)
			                    	resultJson.put(metaData.getColumnLabel(columnIndex), restultSet.getString(metaData.getColumnName(columnIndex)));
//			                        columnMap.put(metaData.getColumnLabel(columnIndex),     restultSet.getString(metaData.getColumnName(columnIndex)));
			                    else
//			                        columnMap.put(metaData.getColumnLabel(columnIndex), "");
			                    	resultJson.put(metaData.getColumnLabel(columnIndex), "");
			                }
//			                list.add(columnMap);
					}
//					System.out.println(columnMap.toString()+"----"+list.toString());
				}
				resultJson.put("code", ConstantValues.successCode);
				resultJson.put("message", "Data retrieved successfully");
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
}
