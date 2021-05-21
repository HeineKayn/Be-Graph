package org.insa.graphs.algorithm.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;

import org.insa.graphs.algorithm.ArcInspectorFactory;
import org.insa.graphs.algorithm.shortestpath.ShortestPathData;
import org.insa.graphs.model.Arc;
import org.insa.graphs.model.Graph;
import org.insa.graphs.model.Node;
import org.insa.graphs.model.Path;
import org.insa.graphs.model.RoadInformation;
import org.insa.graphs.model.RoadInformation.RoadType;
import org.insa.graphs.model.io.BinaryGraphReader;
import org.junit.BeforeClass;
import org.junit.Test;

import org.insa.graphs.algorithm.shortestpath.DijkstraAlgorithm;
import org.insa.graphs.algorithm.shortestpath.BellmanFordAlgorithm;

public class ShortestAlgosTest {

    // Small graph use for tests
    private static Graph graph, graph_toulouse;
    
    private static String map_toulouse = "C:\\Users\\thoma\\Documents\\GitHub\\Be-Graph\\RessourcesGraph\\Maps\\toulouse.mapgr";

    // List of nodes
    private static Node[] nodes;

    // List of arcs in the graph, a2b is the arc from node A (0) to B (1).
    @SuppressWarnings("unused")
    private static Arc a2b, a2c, a2e, b2c, c2d_1, c2d_2, c2d_3, c2a, d2a, d2e, e2d;
    
    // Data inputed in Dijkstra
    private static ShortestPathData shortData, invalidData, 
    								toulouseD1_voiture, toulouseD1_pied,
    								toulouseD2_voiture, toulouseD2_pied;
    
    // Result of the algorithm
    private static Path invalidDij, shortDij, toulouseDij1_voiture, toulouseDij1_pied, toulouseDij2_voiture, toulouseDij2_pied,
    					invalidA, shortA, toulouseA1_voiture, toulouseA1_pied, toulouseA2_voiture, toulouseA2_pied,
    					shortSol, toulouseS1_voiture, toulouseS1_pied, toulouseS2_voiture, toulouseS2_pied;

    @BeforeClass
    public static void initAll() throws IOException {

        // 10 and 20 meters per seconds
        RoadInformation speed10 = new RoadInformation(RoadType.MOTORWAY, null, true, 36, ""),
                		speed20 = new RoadInformation(RoadType.MOTORWAY, null, true, 72, "");

        // Create nodes for small graphs
        nodes = new Node[6];
        for (int i = 0; i < nodes.length; ++i) {
            nodes[i] = new Node(i, null);
        }

        // Add arcs...
        a2b = Node.linkNodes(nodes[0], nodes[1], 10, speed10, null);
        a2c = Node.linkNodes(nodes[0], nodes[2], 15, speed10, null);
        a2e = Node.linkNodes(nodes[0], nodes[4], 15, speed20, null);
        b2c = Node.linkNodes(nodes[1], nodes[2], 10, speed10, null);
        c2d_1 = Node.linkNodes(nodes[2], nodes[3], 20, speed10, null);
        c2d_2 = Node.linkNodes(nodes[2], nodes[3], 10, speed10, null);
        c2d_3 = Node.linkNodes(nodes[2], nodes[3], 15, speed20, null);
        d2a = Node.linkNodes(nodes[3], nodes[0], 15, speed10, null);
        d2e = Node.linkNodes(nodes[3], nodes[4], 22.8f, speed20, null);
        e2d = Node.linkNodes(nodes[4], nodes[0], 10, speed10, null);
        
        // Construct the Graphs
        BinaryGraphReader toulouseReader = new BinaryGraphReader(new DataInputStream(new BufferedInputStream(new FileInputStream(map_toulouse))));
        graph = new Graph("ID", "", Arrays.asList(nodes), null);
        graph_toulouse = toulouseReader.read();
        
        // ---- Construct Data
        shortData = new ShortestPathData(graph, nodes[0], nodes[3], ArcInspectorFactory.getAllFilters().get(0));
        invalidData = new ShortestPathData(graph, nodes[0], nodes[5], ArcInspectorFactory.getAllFilters().get(0));
        
        // ---- Construct Toulouse Data
        // Domicile -> INSA - Voiture
        toulouseD1_voiture = new ShortestPathData(graph_toulouse, graph_toulouse.getNodes().get(11827), graph_toulouse.getNodes().get(5901), ArcInspectorFactory.getAllFilters().get(0));
        toulouseD1_pied = new ShortestPathData(graph_toulouse, graph_toulouse.getNodes().get(11827), graph_toulouse.getNodes().get(5901), ArcInspectorFactory.getAllFilters().get(2));
        
        // INSA -> CNES - Pied
        toulouseD2_voiture = new ShortestPathData(graph_toulouse, graph_toulouse.getNodes().get(11574), graph_toulouse.getNodes().get(11081), ArcInspectorFactory.getAllFilters().get(0));
        toulouseD2_pied = new ShortestPathData(graph_toulouse, graph_toulouse.getNodes().get(11574), graph_toulouse.getNodes().get(11081), ArcInspectorFactory.getAllFilters().get(2));
        
        // Get path with Dijkstra
        shortDij = new DijkstraAlgorithm(shortData).run().getPath();
        invalidDij = new DijkstraAlgorithm(invalidData).run().getPath();
        toulouseDij1_pied = new DijkstraAlgorithm(toulouseD1_pied).run().getPath();
        toulouseDij1_voiture = new DijkstraAlgorithm(toulouseD1_voiture).run().getPath();
        toulouseDij2_pied = new DijkstraAlgorithm(toulouseD2_pied).run().getPath();
        toulouseDij2_voiture = new DijkstraAlgorithm(toulouseD2_voiture).run().getPath();
        
        // Get path with A*
        shortA = new DijkstraAlgorithm(shortData).run().getPath();
        invalidA = new DijkstraAlgorithm(invalidData).run().getPath();
        toulouseA1_pied = new DijkstraAlgorithm(toulouseD1_pied).run().getPath();
        toulouseA1_voiture = new DijkstraAlgorithm(toulouseD1_voiture).run().getPath();
        toulouseA2_pied = new DijkstraAlgorithm(toulouseD2_pied).run().getPath();
        toulouseA2_voiture = new DijkstraAlgorithm(toulouseD2_voiture).run().getPath();
        
        // Get path with BellmanFord
        shortSol = new BellmanFordAlgorithm(shortData).run().getPath();
        toulouseS1_pied = new BellmanFordAlgorithm(toulouseD1_pied).run().getPath();
        toulouseS1_voiture = new BellmanFordAlgorithm(toulouseD1_voiture).run().getPath();
        toulouseS2_pied = new BellmanFordAlgorithm(toulouseD2_pied).run().getPath();
        toulouseS2_voiture = new BellmanFordAlgorithm(toulouseD2_voiture).run().getPath();
    }

    @Test
    public void testIsEmpty() {
        assertFalse(shortDij.isEmpty());
        assertFalse(toulouseDij1_voiture.isEmpty());
        assertFalse(toulouseDij1_pied.isEmpty());
        assertFalse(toulouseDij2_voiture.isEmpty());
        assertFalse(toulouseDij2_pied.isEmpty());
    }

    // cout plutot
    @Test
    public void testSize() {
        
        // Dijkstra
        assertEquals(shortSol.size(), shortDij.size());
        assertEquals(toulouseS1_pied.size(), toulouseDij1_pied.size());
        assertEquals(toulouseS1_voiture.size(), toulouseDij1_voiture.size());
        assertEquals(toulouseS2_pied.size(), toulouseDij2_pied.size());
        assertEquals(toulouseS2_voiture.size(), toulouseDij2_voiture.size());

        // A*
        assertEquals(shortSol.size(), shortA.size());
        assertEquals(toulouseS1_pied.size(), toulouseA1_pied.size());
        assertEquals(toulouseS1_voiture.size(), toulouseA1_voiture.size());
        assertEquals(toulouseS2_pied.size(), toulouseA2_pied.size());
        assertEquals(toulouseS2_voiture.size(), toulouseA2_voiture.size());
    }

    @Test
    public void testIsValid() {
        
        // Dijkstra
        assertNull(invalidDij);
        assertTrue(shortDij.isValid());
        assertTrue(toulouseDij1_voiture.isValid());
        assertTrue(toulouseDij1_pied.isValid());
        assertTrue(toulouseDij2_voiture.isValid());
        assertTrue(toulouseDij2_pied.isValid());
        
        // A*
        assertNull(invalidA);
        assertTrue(shortA.isValid());
        assertTrue(toulouseA1_voiture.isValid());
        assertTrue(toulouseA1_pied.isValid());
        assertTrue(toulouseA2_voiture.isValid());
        assertTrue(toulouseA2_pied.isValid());
        
    }

    @Test
    public void testGetLength() {
    	
    	// Dijkstra
        assertEquals(shortSol.getLength(), shortDij.getLength(), 1e-6);
        assertEquals(toulouseS1_pied.getLength(), toulouseDij1_pied.getLength(), 1e-6);
        assertEquals(toulouseS1_voiture.getLength(), toulouseDij1_voiture.getLength(), 1e-6);
        assertEquals(toulouseS2_pied.getLength(), toulouseDij2_pied.getLength(), 1e-6);
        assertEquals(toulouseS2_voiture.getLength(), toulouseDij2_voiture.getLength(), 1e-6);

        // A*
        assertEquals(shortSol.getLength(), shortA.getLength(), 1e-6);
        assertEquals(toulouseS1_pied.getLength(), toulouseA1_pied.getLength(), 1e-6);
        assertEquals(toulouseS1_voiture.getLength(), toulouseA1_voiture.getLength(), 1e-6);
        assertEquals(toulouseS2_pied.getLength(), toulouseA2_pied.getLength(), 1e-6);
        assertEquals(toulouseS2_voiture.getLength(), toulouseA2_voiture.getLength(), 1e-6);
    }

    @Test
    public void testGetTravelTime() {
    	
        // Note: 18 km/h = 5m/s
    	// Dijkstra
        assertEquals(shortSol.getTravelTime(18), shortDij.getTravelTime(18), 1e-6);
        assertEquals(toulouseS1_pied.getTravelTime(18), toulouseDij1_pied.getTravelTime(18), 1e-6);
        assertEquals(toulouseS1_voiture.getTravelTime(18), toulouseDij1_voiture.getTravelTime(18), 1e-6);
        assertEquals(toulouseS2_pied.getTravelTime(18), toulouseDij2_pied.getTravelTime(18), 1e-6);
        assertEquals(toulouseS2_voiture.getTravelTime(18), toulouseDij2_voiture.getTravelTime(18), 1e-6);

        // A*
        assertEquals(shortSol.getTravelTime(18), shortA.getTravelTime(18), 1e-6);
        assertEquals(toulouseS1_pied.getTravelTime(18), toulouseA1_pied.getTravelTime(18), 1e-6);
        assertEquals(toulouseS1_voiture.getTravelTime(18), toulouseA1_voiture.getTravelTime(18), 1e-6);
        assertEquals(toulouseS2_pied.getTravelTime(18), toulouseA2_pied.getTravelTime(18), 1e-6);
        assertEquals(toulouseS2_voiture.getTravelTime(18), toulouseA2_voiture.getTravelTime(18), 1e-6);

        // Note: 28.8 km/h = 8m/s
        // Dijkstra
        assertEquals(shortSol.getTravelTime(28.8), shortDij.getTravelTime(28.8), 1e-6);
        assertEquals(toulouseS1_pied.getTravelTime(28.8), toulouseDij1_pied.getTravelTime(28.8), 1e-6);
        assertEquals(toulouseS1_voiture.getTravelTime(28.8), toulouseDij1_voiture.getTravelTime(28.8), 1e-6);
        assertEquals(toulouseS2_pied.getTravelTime(28.8), toulouseDij2_pied.getTravelTime(28.8), 1e-6);
        assertEquals(toulouseS2_voiture.getTravelTime(28.8), toulouseDij2_voiture.getTravelTime(28.8), 1e-6);

        // A*
        assertEquals(shortSol.getTravelTime(28.8), shortA.getTravelTime(28.8), 1e-6);
        assertEquals(toulouseS1_pied.getTravelTime(28.8), toulouseA1_pied.getTravelTime(28.8), 1e-6);
        assertEquals(toulouseS1_voiture.getTravelTime(28.8), toulouseA1_voiture.getTravelTime(28.8), 1e-6);
        assertEquals(toulouseS2_pied.getTravelTime(28.8), toulouseA2_pied.getTravelTime(28.8), 1e-6);
        assertEquals(toulouseS2_voiture.getTravelTime(28.8), toulouseA2_voiture.getTravelTime(28.8), 1e-6);
    }
}
