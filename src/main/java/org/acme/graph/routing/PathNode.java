package org.acme.graph.routing;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.googlecode.cqengine.attribute.SimpleAttribute;
import com.googlecode.cqengine.query.option.QueryOptions;

import org.acme.graph.model.Edge;
import org.acme.graph.model.Vertex;

/**
 * 
 */
public class PathNode {

    /**
	 * dijkstra - coût pour atteindre le sommet
	 */
	private double cost;

    /**
	 * dijkstra - arc entrant avec le meilleur coût
	 */
	private Edge reachingEdge = null;
	/**
	 * dijkstra - indique si le sommet est visité
	 */
	private boolean visited = false;

    /**
     * 
     */
    Vertex vertex;

    public PathNode(Vertex vertex, double cost) {
        this.vertex = vertex;
        this.cost = cost;
    }

    public Vertex getVertex() {
        return vertex;
    }

    public static final SimpleAttribute<PathNode, Vertex> VERTEX = new SimpleAttribute<PathNode, Vertex>("nodeVertex") {
		public Vertex getValue(PathNode node, QueryOptions queryOptions) {
			return node.vertex;
		}
	};
    
    @JsonIgnore
	public double getCost() {
		return cost;
	}

	public void setCost(double cost) {
		this.cost = cost;
	}

    public static final SimpleAttribute<PathNode, Double> COST = new SimpleAttribute<PathNode, Double>("nodeCost") {
		public Double getValue(PathNode node, QueryOptions queryOptions) {
			return node.cost;
		}
	};

    @JsonIgnore
	public Edge getReachingEdge() {
		return reachingEdge;
	}

	public void setReachingEdge(Edge reachingEdge) {
		this.reachingEdge = reachingEdge;
	}

	public boolean isVisited() {
		return visited;
	}

	public void setVisited(boolean visited) {
		this.visited = visited;
	}

    public static final SimpleAttribute<PathNode, Boolean> VISITED = new SimpleAttribute<PathNode, Boolean>("nodeVisited") {
		public Boolean getValue(PathNode node, QueryOptions queryOptions) {
			return node.visited;
		}
	};
}
