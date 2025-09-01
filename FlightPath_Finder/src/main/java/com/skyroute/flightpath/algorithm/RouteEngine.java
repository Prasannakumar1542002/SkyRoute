package com.skyroute.flightpath.algorithm;

import java.util.*;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.skyroute.flightpath.helper.SkyRouteHelper;
import com.skyroute.flightpath.model.AirportNetwork;
import com.skyroute.flightpath.model.AirportNode;
import com.skyroute.flightpath.model.Result;

@Component
public class RouteEngine {

	@Autowired
	private NodeLoader nodeLoader;
	
	@Autowired
	private SkyRouteHelper skyroutehelper;

	public List<List<Map<String,Object>>> getOptimalRoute(String source, String destination) {

		//source="MAA",destination="STV"
		AirportNetwork airportNetwork = nodeLoader.loadNodes();
		
		List<List<Map<String,Object>>> route=new ArrayList<>();

		Result result = PathFinder.loadAlgorithm().findShortestPath(airportNetwork.getAirportNodes(),
				airportNetwork.getNodeMap().get(source), airportNetwork.getNodeMap().get(destination));

		if (result.distance != -1) {
			System.out.println("Shortest distance from Chennai to Delhi: " + result.distance);
			System.out.println("All shortest paths:");

			// Use a set to remove duplicates
			Set<String> uniquePaths = new LinkedHashSet<>();
			for (List<AirportNode> path : result.getAllPaths()) {
				String pathStr = path.stream().map(AirportNode::getName).collect(Collectors.joining(" -> "));
				uniquePaths.add(pathStr);
			}

			int count = 1;
			
			for (String pathStr : uniquePaths) {
				System.out.println("Path " + (count++) + ": " + pathStr + " -> END");
				route.add(Arrays.asList(pathStr.split(" -> ")).stream().map((location)->skyroutehelper.getAirportDetails(location)).toList());
			}
		}
		return route;
	}

}
