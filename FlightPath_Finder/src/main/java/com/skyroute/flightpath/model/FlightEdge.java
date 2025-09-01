package com.skyroute.flightpath.model;

public final class FlightEdge {
	private boolean available;
	private AirportNode node;
	private Integer distance;
	
	public FlightEdge(boolean available, AirportNode node, Integer distance) {
		super();
		this.available = available;
		this.node = node;
		this.distance = distance;
	}

	public boolean isAvailable() {
		return available;
	}

	public AirportNode getNode() {
		return node;
	}

	public Integer getDistance() {
		return distance;
	}
	
}
