package org.acme.graph.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.acme.graph.errors.NotFoundException;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.LineString;

/**
 * 
 * Un graphe matérialisé par une liste de sommets et d'arcs
 * 
 * @author MBorne
 *
 */
public class Graph {
	/**
	 * Liste des sommets
	 */
	private List<Vertex> vertices = new ArrayList<>();

	/**
	 * Liste des arcs
	 */
	private List<Edge> edges = new ArrayList<>();

	/**
	 * 
	 */
	private Map<Coordinate, Vertex> mapVertices = new HashMap<Coordinate, Vertex>();

	/**
	 * Récupération de la liste sommets
	 * 
	 * @return
	 */
	public Collection<Vertex> getVertices() {
		return vertices;
	}

	/**
	 * Recherche d'un sommet par identifiant
	 * 
	 * @param id
	 * @return
	 */
	public Vertex findVertex(String id) {
		for (Vertex vertex : vertices) {
			if (vertex.getId().equals(id)) {
				return vertex;
			}
		}
		throw new NotFoundException(String.format("Vertex '%s' not found", id));
	}

	/**
	 * Recherche d'un sommet par égalité stricte de coordonnées
	 * 
	 * @param coordinate
	 * @return
	 */
	public Vertex findVertex(Coordinate coordinate) {
		if (mapVertices.containsKey(coordinate)) return mapVertices.get(coordinate);
		throw new NotFoundException(String.format("Vertex not found at [%s,%s]", coordinate.x, coordinate.y));
	}

	/**
	 * Récupération ou création d'un sommet en assurant l'unicité
	 * 
	 * @param graph
	 * @param coordinate
	 * @return
	 */
	public Vertex getOrCreateVertex(Coordinate coordinate) {
		Vertex vertex;
		try {
			vertex = findVertex(coordinate);
		} catch (NotFoundException e) {
			/* création d'un nouveau sommet car non trouvé */
			vertex = createVertex(coordinate, Integer.toString(getVertices().size()));
		}
		return vertex;
	}

	/**
	 * Récupération de la liste des arcs
	 * 
	 * @return
	 */
	public Collection<Edge> getEdges() {
		return edges;
	}

	/**
	 * Recherche des arcs sortant d'un sommet
	 * 
	 * @param vertex
	 * @return
	 */
	public List<Edge> getInEdges(Vertex vertex) {
		
		return vertex.getInEdges();
	}

	/**
	 * Recherche des arcs sortant d'un sommet
	 * 
	 * @param vertex
	 * @return
	 */
	public List<Edge> getOutEdges(Vertex vertex) {
		
		return vertex.getOutEdges();
	}

	public Vertex createVertex(Coordinate coordinate, String id) {
		Vertex vertex = new Vertex();
		vertex.setId(id);
		vertex.setCoordinate(coordinate);
		vertices.add(vertex);
		mapVertices.put(coordinate, vertex);
		return vertex;
	}

	public Edge createEdge(Vertex source, Vertex target, String id) {
		Edge edge = new Edge(source, target);
		edge.setId(id);
		this.edges.add(edge);

		return edge;
	}

	public Edge createEdge(Vertex source, Vertex target, String id, LineString geometry) {
		Edge edge = new Edge(source, target);
		edge.setId(id);
		edge.setGeometry(geometry);
		this.edges.add(edge);

		return edge;
	}
}
