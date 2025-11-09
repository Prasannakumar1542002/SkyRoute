package com.skyroute.flightpath.helper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.skyroute.flightpath.database.DBManager;
import com.skyroute.flightpath.database.DatabaseConstants;
import com.skyroute.flightpath.database.ORMHelper;

@Component
public class SkyRouteHelper {

	@Autowired
	private DBManager dbManager;

	private static final double EARTH_RADIUS_KM = 6371.0;
	private static final double AVERAGE_FLIGHT_SPEED_KMPH = 850.0;

	public Map<String, Object> getAirportDetails(String city_name) {
		JdbcTemplate jdbcTemplate = dbManager.getConnection();
		Map<String, Object> airportDetails = jdbcTemplate.queryForMap("SELECT * FROM airports WHERE city_name=?",
				city_name.trim());
		return airportDetails;
	}

	public List<Map<String, Object>> getAirlinesDetails(String origin, String destination) {
		JdbcTemplate jdbcTemplate = dbManager.getConnection();
		List<Map<String, Object>> airportDetails = jdbcTemplate
				.queryForList("SELECT * FROM airlines WHERE origin=? AND destination=?", origin, destination);
		return airportDetails;
	}
	
	public List<Map<String, Object>> getflightDetails() {
		JdbcTemplate jdbcTemplate = dbManager.getConnection();
		return jdbcTemplate
				.queryForList("SELECT * FROM flights");
	}

	public static List<String> splitRoute(String route) {
		// Split by "->"
		String[] cities = route.split("->");
		List<String> paths = new ArrayList<>();

		// Build consecutive pairs
		for (int i = 0; i < cities.length - 1; i++) {
			paths.add(cities[i] + "->" + cities[i + 1]);
		}

		return paths;
	}

	// DTO class to hold both distance and duration
	public static class FlightInfo {
		private final double distanceKm;
		private final String duration;

		public FlightInfo(double distanceKm, String duration) {
			this.distanceKm = distanceKm;
			this.duration = duration;
		}

		public double getDistanceKm() {
			return distanceKm;
		}

		public String getDuration() {
			return duration;
		}

		@Override
		public String toString() {
			return String.format("Distance: %.2f km, Duration: %s", distanceKm, duration);
		}
	}

	/**
	 * Calculates flight distance and estimated duration between two coordinates.
	 * 
	 * @param lat1 Latitude of source (degrees)
	 * @param lon1 Longitude of source (degrees)
	 * @param lat2 Latitude of destination (degrees)
	 * @param lon2 Longitude of destination (degrees)
	 * @return FlightInfo containing distance (km) and formatted duration ("2h 35m")
	 */
	public FlightInfo calculateFlightInfo(double lat1, double lon1, double lat2, double lon2) {
		// Convert degrees to radians
		double lat1Rad = Math.toRadians(lat1);
		double lon1Rad = Math.toRadians(lon1);
		double lat2Rad = Math.toRadians(lat2);
		double lon2Rad = Math.toRadians(lon2);

		// Haversine formula
		double dLat = lat2Rad - lat1Rad;
		double dLon = lon2Rad - lon1Rad;

		double a = Math.pow(Math.sin(dLat / 2), 2)
				+ Math.cos(lat1Rad) * Math.cos(lat2Rad) * Math.pow(Math.sin(dLon / 2), 2);

		double c = 2 * Math.asin(Math.sqrt(a));

		// Distance in km
		double distanceKm = EARTH_RADIUS_KM * c;

		// Time in hours
		double timeHours = distanceKm / AVERAGE_FLIGHT_SPEED_KMPH;
		int hours = (int) timeHours;
		int minutes = (int) Math.round((timeHours - hours) * 60);

		String duration = String.format("%dh %02dm", hours, minutes);

		return new FlightInfo(distanceKm, duration);
	}

//	public static void main(String[] args) {
//		// Example: Chennai (MAA) to Delhi (DEL)
//		double chennaiLat = 13.0827, chennaiLon = 80.2707;
//		double delhiLat = 28.6139, delhiLon = 77.2090;
//
//		FlightInfo info = calculateFlightInfo(chennaiLat, chennaiLon, delhiLat, delhiLon);
//		System.out.println(info);
//	}
}
