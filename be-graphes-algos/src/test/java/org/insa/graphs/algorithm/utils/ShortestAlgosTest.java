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
import org.junit.Before;
import org.junit.Test;

import org.insa.graphs.algorithm.shortestpath.ShortestPathAlgorithm;
import org.insa.graphs.algorithm.shortestpath.BellmanFordAlgorithm;

public abstract class ShortestAlgosTest {
	
	protected abstract ShortestPathAlgorithm createAlgorithm(ShortestPathData data);
	
	private static String toulouse_map = "C:\\Users\\thoma\\Documents\\GitHub\\Be-Graph\\RessourcesGraph\\Maps\\toulouse.mapgr";
	private static String hautega_map = "C:\\Users\\thoma\\Documents\\GitHub\\Be-Graph\\RessourcesGraph\\Maps\\haute-garonne.mapgr";
	
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
    private static Path shortSol, invalidPath, shortPath;

    @Before
    public void initAll() throws IOException {

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
        
        // Get path
        shortPath = createAlgorithm(shortData).run().getPath();
        invalidPath = createAlgorithm(invalidData).run().getPath();
        
        // Get path with BellmanFord
        shortSol = new BellmanFordAlgorithm(shortData).run().getPath();
    }

    @Test
    public void testIsEmpty() {
        assertFalse(shortPath.isEmpty());
    }

    @Test
    public void testIsValid() {
        assertNull(invalidPath);
        assertTrue(shortPath.isValid());
    }

    @Test
    public void testGetLength() {
        assertEquals(shortSol.getLength(), shortPath.getLength(), 1e-6);
    }

    @Test
    public void testGetTravelTime() {
        assertEquals(shortSol.getMinimumTravelTime(), shortPath.getMinimumTravelTime(), 1e-6);
    }
    
    @Test
    public void testAvecOracle() throws IOException {
    	int i = 0;
    	while(i<50) {
    		BinaryGraphReader reader = new BinaryGraphReader(new DataInputStream(new BufferedInputStream(new FileInputStream(toulouse_map))));
    		Graph graph = reader.read();
    		Random rand = new Random();
    		ShortestPathData data = new ShortestPathData(graph, 
    				graph.getNodes().get(rand.nextInt(graph.size())), 
    				graph.getNodes().get(rand.nextInt(graph.size())), 
    				ArcInspectorFactory.getAllFilters().get(rand.nextInt(5)));
    		
    		Path sol = new BellmanFordAlgorithm(data).run().getPath();
    		
    		if(sol == null) {
    			break;
    		}
			i++;
			
			Path path = createAlgorithm(data).run().getPath();
			
			// Test d'empty
			assertFalse(path.isEmpty());
			
			// Test de validité
	        assertTrue(path.isValid());
	        
	        // Test de distance
	        if (data.getMode() == AbstractInputData.Mode.LENGTH) {
	        	assertEquals(sol.getLength(), path.getLength(), 1e-6);
	        }
	        
	        // Test de temps 
	        else{
	            assertEquals(sol.getMinimumTravelTime(), path.getMinimumTravelTime(), 1e-6);
	        }
    	}
    }
    
	@Test
	public void testSansOracle() throws IOException {
		BinaryGraphReader reader = new BinaryGraphReader(new DataInputStream(new BufferedInputStream(new FileInputStream(hautega_map))));
		Graph graph = reader.read();
    	Random rand = new Random();
    	int inspector_n = rand.nextInt(5);
    	
    	ShortestPathData data = new ShortestPathData(graph, 
				graph.getNodes().get(100925), 
				graph.getNodes().get(97597), 
				ArcInspectorFactory.getAllFilters().get(inspector_n));
    	
    	// Création des chemins
    	Path full = createAlgorithm(data).run().getPath();
    	
    	// Empty test
		assertFalse(full.isEmpty());
		
		// Valid test
		assertTrue(full.isValid());
		
		//Get data parameters
        Node origin = data.getOrigin();
        Node destination = data.getDestination();
        
        // On prends un point au hasard entre le début et la fin
        // On split le chemin en 2
        // On fait un algo sur chaque partie 
        // On concatene les 2 résultats, ils doivent être >= à celui du full
        // On répète l'opération 10x pour être sûr
        for (int i = 0; i < 20; i++) {
        	Node intermediaire = data.getGraph().get(rand.nextInt(data.getGraph().size()));
        	while (intermediaire.equals(origin) || intermediaire.equals(destination))
        		intermediaire = data.getGraph().get(rand.nextInt(data.getGraph().size()));
        	
        	// Chemin : Origine -> Intermediaire
            ShortestPathData data_half1 = new ShortestPathData(graph, origin, intermediaire, 
    				ArcInspectorFactory.getAllFilters().get(inspector_n));
            Path half1 = createAlgorithm(data_half1).run().getPath();

            // Chemin : Intermediaire -> Origine
        	ShortestPathData data_half2 = new ShortestPathData(graph, intermediaire, destination, 
    				ArcInspectorFactory.getAllFilters().get(inspector_n));
        	Path half2 = createAlgorithm(data_half2).run().getPath();
        	
        	if(half1 != null && half2 != null) {
        		Path altPath = Path.concatenate(half1, half2);
        		
        		// On vérifie si la DISTANCE du split est le même que celui de l'entier
        		if (data.getMode() == AbstractInputData.Mode.LENGTH) {
        			assertTrue(Double.compare(altPath.getLength(),full.getLength()) >= 0);
		        }
        		
        		// On vérifie si le TEMPS du split est le même que celui de l'entier
        		else {
        			assertTrue(Double.compare(altPath.getMinimumTravelTime(),full.getMinimumTravelTime()) >= 0);
        		}
        	}
    	}
	}
}
