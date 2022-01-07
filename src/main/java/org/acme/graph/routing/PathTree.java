package org.acme.graph.routing;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import com.googlecode.cqengine.ConcurrentIndexedCollection;
import com.googlecode.cqengine.IndexedCollection;
import com.googlecode.cqengine.index.hash.HashIndex;
import com.googlecode.cqengine.index.navigable.NavigableIndex;

import static com.googlecode.cqengine.query.QueryFactory.*;

import org.acme.graph.errors.NotFoundException;
import org.acme.graph.model.Edge;
import org.acme.graph.model.Path;
import org.acme.graph.model.Vertex;

public class PathTree {
    
    private IndexedCollection<PathNode> nodes = new ConcurrentIndexedCollection<PathNode>();

	/**
	 * Prépare le graphe pour le calcul du plus court chemin
	 * 
	 * @param source
	 */
	public PathTree (Vertex source) {
        this.nodes.addIndex(HashIndex.onAttribute(PathNode.VERTEX));
        this.nodes.addIndex(NavigableIndex.onAttribute(PathNode.COST));
        this.nodes.addIndex(HashIndex.onAttribute(PathNode.VISITED));
		this.nodes.add(new PathNode(source, 0.0));
	}

    /**
     * 
     * @param vertex
     * @return
     */
    private PathNode getNode(Vertex vertex) {
		Optional<PathNode> result = nodes.retrieve(equal(PathNode.VERTEX, vertex)).stream().findFirst();
        if (result.isPresent()) return result.get();
        throw new NotFoundException(String.format("Vertex %s not found", vertex));
	}

    /**
     * 
     * @param vertex
     * @return
     */
    public PathNode getOrCreateNode(Vertex vertex) {
        if (!isReached(vertex))
        nodes.add(new PathNode(vertex, Double.POSITIVE_INFINITY));
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
        return nodes.retrieve(equal(PathNode.VERTEX, destination)).isNotEmpty();
    }

    public void setReached(PathNode node,double reachingCost,Edge reachingEdge){
        this.nodes.remove(node);
        node.setCost(reachingCost);
        node.setReachingEdge(reachingEdge);
        this.nodes.add(node);
    }

    public void markVisited(Vertex vertex) {
        PathNode node = getNode(vertex);
        this.nodes.remove(node);
        node.setVisited(true);
        this.nodes.add(node);
    }

    public Collection<Vertex> getReachedVertices(){
        List<Vertex> reached = new ArrayList<Vertex>();
        this.nodes.forEach((PathNode node) -> {
            reached.add(node.getVertex());
        });

        return reached;
    }

    public Vertex getNearestNonVisitedVertex() {
        Optional<PathNode> result = this.nodes.retrieve(
            equal(PathNode.VISITED, false),
            queryOptions(orderBy(ascending(PathNode.COST)))).stream().findFirst();

        if (result.isPresent()) return result.get().vertex;
        throw new NotFoundException(String.format("No vertex was found"));
    }
}
