package com.skyroute.flightpath.algorithm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.skyroute.flightpath.database.DBManager;
import com.skyroute.flightpath.database.DatabaseConstants;
import com.skyroute.flightpath.model.AirportNetwork;
import com.skyroute.flightpath.model.AirportNode;
import com.skyroute.flightpath.model.FlightEdge;

@Component
public class NodeLoader {
	
	@Autowired
	private DBManager dbManager;
	
	private static AirportNetwork airportnetwork=null;
	
	public AirportNetwork loadNodes(){
		if(airportnetwork!=null) {
			return airportnetwork;
		}
		List<AirportNode> airportNodes=new ArrayList<>();
		
		Map<String,AirportNode> airportNodesMap=new HashMap<>();
		JdbcTemplate jdbcTemplate = dbManager.getConnection();
		List<Map<String,Object>> sourceAirports=jdbcTemplate.queryForList("SELECT IATA_code,city_name FROM airports");
		sourceAirports.forEach((oneAirport)->{
			String IATA_code=(String) oneAirport.get(DatabaseConstants.IATA_CODE);
			String city_name=(String) oneAirport.get(DatabaseConstants.CITY_NAME);
			AirportNode obj=new AirportNode(city_name, new ArrayList<>(), IATA_code);
			airportNodesMap.put(IATA_code,obj);
		});
		sourceAirports.forEach((oneAirport)->{
			String IATA_code=(String) oneAirport.get(DatabaseConstants.IATA_CODE);
			List<Map<String,Object>> airlines=jdbcTemplate.queryForList("SELECT destination FROM airlines WHERE origin=?",IATA_code);
			airlines.forEach((oneAirline)->{
				AirportNode obj=airportNodesMap.get(oneAirline.get(DatabaseConstants.DESTINATION));
				FlightEdge edge=new FlightEdge(true, obj, 1);
				airportNodesMap.get(IATA_code).getEdges().add(edge);
			});
			airportNodes.add(airportNodesMap.get(IATA_code));
		});
		airportnetwork=new AirportNetwork();
		airportnetwork.setAirportNodes(airportNodes);
		airportnetwork.setNodeMap(airportNodesMap);
		return airportnetwork;
	}
}
