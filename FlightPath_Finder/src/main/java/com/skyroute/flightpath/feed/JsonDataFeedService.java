package com.skyroute.flightpath.feed;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.skyroute.flightpath.database.DBManager;
import com.skyroute.flightpath.database.DatabaseConstants;
import com.skyroute.flightpath.database.ORMHelper;
import com.skyroute.flightpath.model.FlightPathConstants;

@Component
class JsonDataFeedService extends FeedImportServices {
	
	@Autowired
	private DBManager dbManager;

	@Override
	public Map<String, Object> loadFileDataToTable(String filePath, String tableName) {
		try {
			String content = new String(Files.readAllBytes(Paths.get(filePath)));
			JSONObject json = new JSONObject(content);
			System.out.println(json.toString());
			JSONArray jsonArray = (JSONArray) json.get("data");
			JdbcTemplate jdbcTemplate=dbManager.getConnection();
			jsonArray.forEach((oneJson)->{
//				ORMHelper.insertRow(jdbcTemplate, tableName, ((JSONObject)oneJson).toMap());
				ORMHelper.updateRecord(jdbcTemplate, Map.of(DatabaseConstants.IATA_CODE,((JSONObject)oneJson).get(DatabaseConstants.IATA_CODE)), ((JSONObject)oneJson).toMap(), tableName);
			});
			return Map.of(FlightPathConstants.RESULT,FlightPathConstants.SUCCESS);
		} catch (Exception e) {
			e.printStackTrace();
			return Map.of(FlightPathConstants.RESULT,FlightPathConstants.ERROR);
		}
	}

}
