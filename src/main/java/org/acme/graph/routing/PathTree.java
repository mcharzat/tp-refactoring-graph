package org.acme.graph.routing;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.acme.graph.model.Edge;
import org.acme.graph.model.Path;
import org.acme.graph.model.Vertex;

public class PathTree {
    
    private Map<Vertex,PathNode> nodes = new HashMap<Vertex, PathNode>();

	/**
	 * Prépare le graphe pour le calcul du plus court chemin
	 * 
	 * @param source
	 */
	public PathTree (Vertex source) {
		nodes.put(source, new PathNode(0.0));
	}

    /**
     * 
     * @param vertex
     * @return
     */
    private PathNode getNode(Vertex vertex) {
		return nodes.get(vertex);
	}

    /**
     * 
     * @param vertex
     * @return
     */
    public PathNode getOrCreateNode(Vertex vertex) {
        if (!isReached(vertex))
        nodes.put(
            vertex, new PathNode(Double.POSITIVE_INFINITY)
        );
        return getNode(vertex);
    }

    /**
	 * Construit le chemin en remontant les relations incoming edge
	 * 
	 * @param target
	 * @return
	 */
	public Path getPath(Vertex target) {
        assert isReached(target);

		List<Edge> result = new ArrayList<>();

		Edge current = getNode(target).getReachingEdge();
		do {
			result.add(current);
			current = getNode(current.getSource()).getReachingEdge();
		} while (current != null);

		Collections.reverse(result);
		return new Path(result);
	}

    public boolean isReached(Vertex destination){
        return nodes.containsKey(destination);
    }

    public Collection<Vertex> getReachedVertices(){
        return nodes.keySet();
    }
}
