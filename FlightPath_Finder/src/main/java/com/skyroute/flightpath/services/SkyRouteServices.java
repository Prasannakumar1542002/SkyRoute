package com.skyroute.flightpath.services;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.skyroute.flightpath.database.DBManager;
import com.skyroute.flightpath.database.DatabaseConstants;
import com.skyroute.flightpath.database.ORMHelper;
import com.skyroute.flightpath.database.TableDTO;

@Service
public class SkyRouteServices {

	@Autowired
	private DBManager dbManager;

	public Map<String, String> fetchAirports() {
		JdbcTemplate jdbcTemplate = dbManager.getConnection();
		List<TableDTO> airportRS = ORMHelper.getAllRecords(DatabaseConstants.AIRPORTS_TBL, jdbcTemplate);
		return airportRS.stream()
				.collect(Collectors.toMap(airport -> (String) airport.getData().get(DatabaseConstants.CITY_NAME),
						airport -> (String) airport.getData().get(DatabaseConstants.IATA_CODE)));
	}
}
