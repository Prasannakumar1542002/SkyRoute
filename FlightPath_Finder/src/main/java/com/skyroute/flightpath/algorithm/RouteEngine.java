package com.skyroute.flightpath.algorithm;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.*;
import java.util.stream.Collectors;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.skyroute.flightpath.database.DatabaseConstants;
import com.skyroute.flightpath.helper.SessionUtil;
import com.skyroute.flightpath.helper.SkyRouteHelper;
import com.skyroute.flightpath.helper.SkyRouteHelper.FlightInfo;
import com.skyroute.flightpath.model.AirportNetwork;
import com.skyroute.flightpath.model.AirportNode;
import com.skyroute.flightpath.model.Result;

import jakarta.servlet.http.HttpSession;

@Component
public class RouteEngine {

	@Autowired
	private NodeLoader nodeLoader;

	@Autowired
	private SkyRouteHelper skyroutehelper;

	public List<List<Map<String, Object>>> getOptimalRoute(String source, String destination) {

		HttpSession session = SessionUtil.getSession();

		// source="MAA",destination="STV"
		AirportNetwork airportNetwork = nodeLoader.loadNodes();

		List<List<Map<String, Object>>> route = new ArrayList<>();
		JSONObject fdata = new JSONObject();

		Result result = PathFinder.loadAlgorithm().findShortestPath(airportNetwork.getAirportNodes(),
				airportNetwork.getNodeMap().get(source), airportNetwork.getNodeMap().get(destination));

		if (result.distance != -1) {
			System.out.println("Shortest distance from Chennai to Delhi: " + result.distance);
			System.out.println("All shortest paths:");

			// Use a set to remove duplicates
			Set<String> uniquePaths = new LinkedHashSet<>();
			Set<String> uniqueIATA = new LinkedHashSet<>();
			for (List<AirportNode> path : result.getAllPaths()) {
				String pathStr = path.stream().map(AirportNode::getName).collect(Collectors.joining(" -> "));
				uniquePaths.add(pathStr);
				String pathIataStr = path.stream().map(AirportNode::getIATA).collect(Collectors.joining(" -> "));
				uniqueIATA.add(pathIataStr);
			}
			
			System.out.println(uniqueIATA);

			int count = 1;
			List<String> list = new ArrayList<>(uniqueIATA);
			for (String pathStr : uniquePaths) {
				List<String> locations = Arrays.asList(list.get(count - 1).split(" -> "));
				System.out.println("Path " + (count++) + ": " + pathStr + " -> END");
				List<String> travel = SkyRouteHelper.splitRoute(pathStr);
				System.out.println(locations);
				String IATASStr = locations.stream().collect(Collectors.joining());
				List<JSONObject> rt = travel.stream().map(this::getFlightData).toList();
				fdata.put(IATASStr, rt);
				route.add(Arrays.asList(pathStr.split(" -> ")).stream()
						.map((location) -> skyroutehelper.getAirportDetails(location)).toList());
			}
		}
		System.out.println("done");
		session.setAttribute("flightData", fdata);
		return route;
	}

	private JSONObject getFlightData(String route) {
		JSONObject flightDatas = new JSONObject();
		List<JSONObject> flights = new ArrayList<>();
		List<Map<String, Object>> airportDetails = Arrays.asList(route.split(" -> ")).stream()
				.map((location) -> skyroutehelper.getAirportDetails(location)).toList();
		String sourceIATA = (String) airportDetails.get(0).get(DatabaseConstants.IATA_CODE);
		String destinationIATA = (String) airportDetails.get(1).get(DatabaseConstants.IATA_CODE);
		double sourceLat = Double.parseDouble((String) airportDetails.get(0).get(DatabaseConstants.LATITUDE));
		double sourceLong = Double.parseDouble((String) airportDetails.get(0).get(DatabaseConstants.LONGITUDE));
		double destinationLat = Double.parseDouble((String) airportDetails.get(1).get(DatabaseConstants.LATITUDE));
		double destinationLong = Double.parseDouble((String) airportDetails.get(1).get(DatabaseConstants.LONGITUDE));
		String[] cities = route.split("->");
		String rt = String.format("%s (%s) → %s (%s)", cities[0].trim(), sourceIATA, cities[1].trim(), destinationIATA);
		Set<String> flightNumbers = skyroutehelper.getAirlinesDetails(sourceIATA, destinationIATA).stream()
				.map((oneAirline) -> ((String) oneAirline.get(DatabaseConstants.FLIGHT_NO)).split(" ")[0])
				.collect(Collectors.toSet());
		Map<String, Map<String, Object>> flightDTO = skyroutehelper.getflightDetails().stream()
				.collect(Collectors.toMap(m -> (String) m.get(DatabaseConstants.IATA), // key
						m -> m// value
				));
		int[] count = { 1 };
		flightNumbers.forEach((oneFlightNumber) -> {
			JSONObject flight = new JSONObject();
			Map<String, Object> flightData = flightDTO.get(oneFlightNumber);
			flight.put("id", count[0]++);
			flight.put("airline", flightData.get(DatabaseConstants.AIRLINE));
			FlightInfo flightInfo = skyroutehelper.calculateFlightInfo(sourceLat, sourceLong, destinationLat,
					destinationLong);
			BigDecimal distance = BigDecimal.valueOf(flightInfo.getDistanceKm());
			BigDecimal price = ((BigDecimal) flightData.get(DatabaseConstants.PRICE_PER_KM)).multiply(distance);
			Locale india = new Locale("en", "IN");
			NumberFormat formatter = NumberFormat.getCurrencyInstance(india);
			String formattedPrice = formatter.format(price);
			flight.put("price", formattedPrice);
			List<Map<String, Object>> segments = List
					.of(Map.of("from", rt.split("→")[0].trim(), "to", rt.split("→")[1].trim(), "distance",
							String.format("%.2f", flightInfo.getDistanceKm()).concat(" km"), "duration",
							flightInfo.getDuration()));
			flight.put("segments", segments);
			flights.add(flight);
		});
		flightDatas.put("route", rt);
		flightDatas.put("flights", flights);
		return flightDatas;
	}

}
