package org.acme.graph.model;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 */
public class Path {

    private List<Edge> edges = new ArrayList<Edge>();

    public Path() {
    }

    public double getLength(){
        double length = 0;
        for (Edge edge : edges) length += edge.getCost();
        return length;
    }
    
}
