package com.skyroute.flightpath.controller;

import java.util.*;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.skyroute.flightpath.algorithm.RouteEngine;
import com.skyroute.flightpath.database.DatabaseConstants;
import com.skyroute.flightpath.helper.SessionUtil;
import com.skyroute.flightpath.model.FlightPathConstants;
import com.skyroute.flightpath.services.SkyRouteServices;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/skyroute")
@CrossOrigin(origins = "http://localhost:5173")
public class SkyRouteController {

	@Autowired
	private RouteEngine engine;

	@Autowired
	private SkyRouteServices service;

	@PostMapping("/get-shortest-path")
	public Map<String, Object> getShortestPath(@RequestBody Map<String, Object> payload) {
		Map<String, Object> result = new HashMap<>();
		HttpSession session = SessionUtil.getSession();
		String origin = (String) payload.get(DatabaseConstants.ORIGIN);
		String destination = (String) payload.get(DatabaseConstants.DESTINATION);
		try {
			List<List<Map<String, Object>>> shortestRoutes = engine.getOptimalRoute(origin, destination);
			result.put(FlightPathConstants.RESULT, FlightPathConstants.SUCCESS);
			result.put(DatabaseConstants.ORIGIN, origin);
			result.put(DatabaseConstants.DESTINATION, destination);
			result.put("routes", shortestRoutes);
			JSONObject flightData = (JSONObject) session.getAttribute("flightData");
			System.out.println(flightData);
			result.put("flightData", flightData.toMap());
		} catch (Exception e) {
			result.put(FlightPathConstants.RESULT, FlightPathConstants.ERROR);
			result.put("responseMessage", e.getMessage());
		}
		return result;
	}

	@GetMapping("/get-airports")
	public Map<String, String> getAirports() {
		return new TreeMap<>(service.fetchAirports());
	}
}
