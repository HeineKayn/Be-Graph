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
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import org.insa.graphs.algorithm.AbstractInputData;
import org.insa.graphs.algorithm.ArcInspectorFactory;
import org.insa.graphs.algorithm.shortestpath.ShortestPathData;
import org.insa.graphs.model.Arc;
import org.insa.graphs.model.Graph;
import org.insa.graphs.model.Node;
import org.insa.graphs.model.Path;
import org.insa.graphs.model.Point;
import org.insa.graphs.model.RoadInformation;
import org.insa.graphs.model.RoadInformation.RoadType;
import org.insa.graphs.model.io.BinaryGraphReader;
import org.junit.BeforeClass;
import org.junit.Test;

import org.insa.graphs.algorithm.shortestpath.DijkstraAlgorithm;
import org.insa.graphs.algorithm.shortestpath.AStarAlgorithm;
import org.insa.graphs.algorithm.shortestpath.BellmanFordAlgorithm;

public class ShortestAlgosTest {
	
	private static String toulouse_map = "C:\\Users\\thoma\\Documents\\GitHub\\Be-Graph\\RessourcesGraph\\Maps\\toulouse.mapgr";
	private static String midipy_map = "C:\\Users\\thoma\\Documents\\GitHub\\Be-Graph\\RessourcesGraph\\Maps\\midi-pyrenees.mapgr";
	
    // Small graph use for tests
    private static Graph graph;
    
    // List of nodes
    private static Node[] nodes;

    // List of arcs in the graph, a2b is the arc from node A (0) to B (1).
    @SuppressWarnings("unused")
    private static Arc a2b, a2c, a2e, b2c, c2d_1, c2d_2, c2d_3, c2a, d2a, d2e, e2d;
    
    // Data inputed in Dijkstra
    private static ShortestPathData shortData, invalidData;
    
    // Result of the algorithm
    private static Path shortSol, invalidDij, shortDij, invalidA, shortA;

    @BeforeClass
    public static void initAll() throws IOException {

        // 10 and 20 meters per seconds
        RoadInformation speed10 = new RoadInformation(RoadType.MOTORWAY, null, true, 36, ""),
                		speed20 = new RoadInformation(RoadType.MOTORWAY, null, true, 72, "");

        // Create nodes for small graphs
        nodes = new Node[6];
        for (int i = 0; i < nodes.length; ++i) {
        	nodes[i] = new Node(i, 
        			new Point(ThreadLocalRandom.current().nextFloat()*10,ThreadLocalRandom.current().nextFloat()*10));
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
        graph = new Graph("ID", "", Arrays.asList(nodes), null);
        
        // ---- Construct Data
        shortData = new ShortestPathData(graph, nodes[0], nodes[3], ArcInspectorFactory.getAllFilters().get(0));
        invalidData = new ShortestPathData(graph, nodes[0], nodes[5], ArcInspectorFactory.getAllFilters().get(0));
        
        // Get path with Dijkstra
        shortDij = new DijkstraAlgorithm(shortData).run().getPath();
        invalidDij = new DijkstraAlgorithm(invalidData).run().getPath();
        
        // Get path with A*
        shortA = new DijkstraAlgorithm(shortData).run().getPath();
        invalidA = new DijkstraAlgorithm(invalidData).run().getPath();
        
        // Get path with BellmanFord
        shortSol = new BellmanFordAlgorithm(shortData).run().getPath();
    }

    @Test
    public void testIsEmpty() {
        assertFalse(shortDij.isEmpty());
    }

    @Test
    public void testIsValid() {
        // Dijkstra
        assertNull(invalidDij);
        assertTrue(shortDij.isValid());
        
        // A*
        assertNull(invalidA);
        assertTrue(shortA.isValid()); 
    }

    @Test
    public void testGetLength() {
        assertEquals(shortSol.getLength(), shortDij.getLength(), 1e-6);
        assertEquals(shortSol.getLength(), shortA.getLength(), 1e-6);
    }

    @Test
    public void testGetTravelTime() {
    	
        // Note: 18 km/h = 5m/s
        assertEquals(shortSol.getTravelTime(18), shortDij.getTravelTime(18), 1e-6);
        assertEquals(shortSol.getTravelTime(18), shortA.getTravelTime(18), 1e-6);

        // Note: 28.8 km/h = 8m/s
        assertEquals(shortSol.getTravelTime(28.8), shortDij.getTravelTime(28.8), 1e-6);
        assertEquals(shortSol.getTravelTime(28.8), shortA.getTravelTime(28.8), 1e-6);
    }
    @Test
    public void testRandomToulouse() throws IOException {
    	int i = 0;
    	while(i<50) {
    		BinaryGraphReader reader = new BinaryGraphReader(new DataInputStream(new BufferedInputStream(new FileInputStream(toulouse_map))));
    		Graph graph = reader.read();
    		Random rand = new Random();
    		ShortestPathData shortdata = new ShortestPathData(graph, 
    				graph.getNodes().get(rand.nextInt(graph.size())), 
    				graph.getNodes().get(rand.nextInt(graph.size())), 
    				ArcInspectorFactory.getAllFilters().get(rand.nextInt(5)));
    		
    		Path sol = new BellmanFordAlgorithm(shortData).run().getPath();
    		if(sol.isValid()) {
    			i++;
    			
    			Path dij = new DijkstraAlgorithm(shortData).run().getPath();
    			Path astar = new AStarAlgorithm(shortData).run().getPath();
    			
    			// Test d'empty
    			assertFalse(dij.isEmpty());
    			assertFalse(astar.isEmpty());
    			
    			// Test de validité
    	        assertTrue(dij.isValid());
    	        assertTrue(astar.isValid()); 
    	        
    	        // Test de distance
    	        if (shortdata.getMode() == AbstractInputData.Mode.LENGTH) {
    	        	assertEquals(sol.getLength(), dij.getLength(), 1e-6);
        	        assertEquals(sol.getLength(), astar.getLength(), 1e-6);
    	        }
    	        
    	        // Test de temps 
    	        else{
    	        	// Note: 18 km/h = 5m/s
    	            assertEquals(sol.getTravelTime(18), dij.getTravelTime(18), 1e-6);
    	            assertEquals(sol.getTravelTime(18), astar.getTravelTime(18), 1e-6);

    	            // Note: 28.8 km/h = 8m/s
    	            assertEquals(sol.getTravelTime(28.8), dij.getTravelTime(28.8), 1e-6);
    	            assertEquals(sol.getTravelTime(28.8), astar.getTravelTime(28.8), 1e-6);
    	        }
    		}
    	}
    }
	@Test
	public void testRandomMidiPy() throws IOException {
		int i = 0;
		while(i<5) {
			BinaryGraphReader reader = new BinaryGraphReader(new DataInputStream(new BufferedInputStream(new FileInputStream(midipy_map))));
			Graph graph = reader.read();
			Random rand = new Random();
			ShortestPathData shortdata = new ShortestPathData(graph, 
					graph.getNodes().get(rand.nextInt(graph.size())), 
					graph.getNodes().get(rand.nextInt(graph.size())), 
					ArcInspectorFactory.getAllFilters().get(rand.nextInt(5)));
			
			Path dij = new DijkstraAlgorithm(shortData).run().getPath();
			if(dij.isValid()) {
				i++;
				
				Path astar = new AStarAlgorithm(shortData).run().getPath();
				
				// Test d'empty
				assertFalse(dij.isEmpty());
				assertFalse(astar.isEmpty());
				
				// Test de validité
		        assertTrue(dij.isValid());
		        assertTrue(astar.isValid()); 
		        
		        // Test de distance
		        if (shortdata.getMode() == AbstractInputData.Mode.LENGTH) {
		        	assertEquals(astar.getLength(), dij.getLength(), 1e-6);
		        }
		        
		        // Test de temps 
		        else{
		        	// Note: 18 km/h = 5m/s
		            assertEquals(astar.getTravelTime(18), dij.getTravelTime(18), 1e-6);
	
		            // Note: 28.8 km/h = 8m/s
		            assertEquals(astar.getTravelTime(28.8), dij.getTravelTime(28.8), 1e-6);
		        }
			}
		}
	}
}
