package com.skyroute.flightpath.model;

import java.util.List;
import java.util.Map;

public final class AirportNetwork {

	private List<AirportNode> airportNodes;
	private Map<String,AirportNode> nodeMap;
	public List<AirportNode> getAirportNodes() {
		return airportNodes;
	}
	public void setAirportNodes(List<AirportNode> airportNodes) {
		this.airportNodes = airportNodes;
	}
	public Map<String, AirportNode> getNodeMap() {
		return nodeMap;
	}
	public void setNodeMap(Map<String, AirportNode> nodeMap) {
		this.nodeMap = nodeMap;
	}

}
