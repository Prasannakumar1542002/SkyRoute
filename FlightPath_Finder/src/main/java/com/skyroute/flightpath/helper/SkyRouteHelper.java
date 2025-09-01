package com.skyroute.flightpath.helper;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.skyroute.flightpath.database.DBManager;

@Component
public class SkyRouteHelper {
	
	@Autowired
	private DBManager dbManager;
	
	public Map<String,Object> getAirportDetails(String city_name){
		JdbcTemplate jdbcTemplate = dbManager.getConnection();
		Map<String,Object> airportDetails=jdbcTemplate.queryForMap("SELECT * FROM airports WHERE city_name=?",city_name);
		return airportDetails;
	}
}
