package com.skyroute.flightpath.model;

import java.util.Collections;
import java.util.List;

public class Result {
    public int distance;
    public List<AirportNode> path;  // first shortest path (for existing main method)
    private List<List<AirportNode>> allPaths; // all shortest paths

    public Result(int distance, List<List<AirportNode>> allPaths) {
        this.distance = distance;
        this.allPaths = allPaths;
        this.path = allPaths.isEmpty() ? Collections.emptyList() : allPaths.get(0);
    }

    public List<List<AirportNode>> getAllPaths() {
        return allPaths;
    }
}
