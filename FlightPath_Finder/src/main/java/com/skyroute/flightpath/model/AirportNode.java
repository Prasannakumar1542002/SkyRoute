package com.skyroute.flightpath.model;

import java.util.List;

public final class AirportNode {
	private String locationName;
	private List<FlightEdge> edges;
	private String IATA;
	
	public AirportNode(String locationName,List<FlightEdge> edges,String IATA) {
		// TODO Auto-generated constructor stub
		this.locationName=locationName;
		this.edges=edges;
		this.IATA=IATA;
	}

	public String getName() {
		return locationName;
	}

	public List<FlightEdge > getEdges() {
		return edges;
	}

	public String getIATA() {
		return IATA;
	}
}
