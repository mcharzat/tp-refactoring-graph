package org.acme.graph.routing;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.acme.graph.model.Edge;
import org.acme.graph.model.Graph;
import org.acme.graph.model.Path;
import org.acme.graph.model.Vertex;

public class PathTree {
    
    private Map<Vertex,PathNode> nodes = new HashMap<Vertex, PathNode>();

	public PathNode getNode(Vertex vertex) {
		return nodes.get(vertex);
	}

    /**
	 * Prépare le graphe pour le calcul du plus court chemin
	 * 
	 * @param source
	 */
	public PathTree (Graph graph, Vertex source) {
		for (Vertex vertex : graph.getVertices()) {
			nodes.put(
				vertex, new PathNode(
				source == vertex ? 0.0 : Double.POSITIVE_INFINITY)
			);
		}
	}

    /**
	 * Construit le chemin en remontant les relations incoming edge
	 * 
	 * @param target
	 * @return
	 */
	public Path getPath(Vertex target) {
		List<Edge> result = new ArrayList<>();

		Edge current = getNode(target).getReachingEdge();
		do {
			result.add(current);
			current = getNode(current.getSource()).getReachingEdge();
		} while (current != null);

		Collections.reverse(result);
		return new Path(result);
	}
}
