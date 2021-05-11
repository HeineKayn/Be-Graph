package org.insa.graphs.algorithm.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
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
import org.insa.graphs.model.io.GraphReader;
import org.junit.BeforeClass;
import org.junit.Test;

import org.insa.graphs.algorithm.AbstractSolution;
import org.insa.graphs.algorithm.shortestpath.DijkstraAlgorithm;
import org.insa.graphs.algorithm.shortestpath.BellmanFordAlgorithm;
import org.insa.graphs.algorithm.shortestpath.Label;
import java.util.Collections;
import java.util.List;

public class DijkstraTest {

    // Small graph use for tests
    private static Graph graph, graph_toulouse;
    
    private static String map_toulouse = "C:\\Users\\thoma\\Documents\\GitHub\\Be-Graph\\RessourcesGraph\\Maps\\toulouse.mapgr";

    // List of nodes
    private static Node[] nodes;

    // List of arcs in the graph, a2b is the arc from node A (0) to B (1).
    @SuppressWarnings("unused")
    private static Arc a2b, a2c, a2e, b2c, c2d_1, c2d_2, c2d_3, c2a, d2a, d2e, e2d;
    
    // Data inputed in Dijkstra
    private static ShortestPathData shortData, invalidData, singleNodeData, emptyData, 
    								toulouseD1, toulouseD2;
    
    // Result of the algorithm
    private static Path shortPath, invalidPath, singleNodePath, emptyPath, 
    				    toulouseP1, toulouseP2;

    @BeforeClass
    public static void initAll() throws IOException {

        // 10 and 20 meters per seconds
        RoadInformation speed10 = new RoadInformation(RoadType.MOTORWAY, null, true, 36, ""),
                		speed20 = new RoadInformation(RoadType.MOTORWAY, null, true, 72, "");

        // Create nodes for small graphs
        nodes = new Node[5];
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
        // empty ?
        // singleNode ?
        shortData = new ShortestPathData(graph, nodes[0], nodes[3], ArcInspectorFactory.getAllFilters().get(0));
        invalidData = new ShortestPathData(graph, nodes[0], nodes[5], ArcInspectorFactory.getAllFilters().get(0));
        
        // ---- Construct Toulouse Data
        // Domicile -> INSA - Voiture
        toulouseD1 = new ShortestPathData(graph_toulouse, graph_toulouse.getNodes().get(11827), graph_toulouse.getNodes().get(5901), ArcInspectorFactory.getAllFilters().get(0));
        // INSA -> CNES - Pied
        toulouseD2 = new ShortestPathData(graph_toulouse, graph_toulouse.getNodes().get(11574), graph_toulouse.getNodes().get(11081), ArcInspectorFactory.getAllFilters().get(0));
    
        // Get path with dijkstra
        shortPath = new DijkstraAlgorithm(shortData).run().getPath();
        invalidPath = new DijkstraAlgorithm(invalidData).run().getPath();
        toulouseP1 = new DijkstraAlgorithm(toulouseD1).run().getPath();
        toulouseP2 = new DijkstraAlgorithm(toulouseD2).run().getPath();
    }

    @Test
    public void testConstructor() {
        //assertEquals(graph, emptyPath.getGraph());
        //assertEquals(graph, singleNodePath.getGraph());
        assertEquals(graph, shortPath.getGraph());
        assertEquals(graph, invalidPath.getGraph());
        assertEquals(graph, toulouseP1.getGraph());
        assertEquals(graph, toulouseP2.getGraph());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testImmutability() {
        emptyPath.getArcs().add(a2b);
    }

    @Test
    public void testIsEmpty() {
        assertTrue(emptyPath.isEmpty());
        assertFalse(singleNodePath.isEmpty());
        assertFalse(shortPath.isEmpty());
        assertFalse(longPath.isEmpty());
        assertFalse(loopPath.isEmpty());
        assertFalse(longLoopPath.isEmpty());
        assertFalse(invalidPath.isEmpty());
    }

    @Test
    public void testSize() {
        assertEquals(0, emptyPath.size());
        assertEquals(1, singleNodePath.size());
        assertEquals(4, shortPath.size());
        assertEquals(5, longPath.size());
        assertEquals(5, loopPath.size());
        assertEquals(10, longLoopPath.size());
    }

    @Test
    public void testIsValid() {
        assertTrue(emptyPath.isValid());
        assertTrue(singleNodePath.isValid());
        assertTrue(shortPath.isValid());
        assertTrue(longPath.isValid());
        assertTrue(loopPath.isValid());
        assertTrue(longLoopPath.isValid());

        assertFalse(invalidPath.isValid());
    }

    @Test
    public void testGetLength() {
        assertEquals(0, emptyPath.getLength(), 1e-6);
        assertEquals(0, singleNodePath.getLength(), 1e-6);
        assertEquals(40, shortPath.getLength(), 1e-6);
        assertEquals(62.8, longPath.getLength(), 1e-6);
        assertEquals(55, loopPath.getLength(), 1e-6);
        assertEquals(120, longLoopPath.getLength(), 1e-6);
    }

    @Test
    public void testGetTravelTime() {
        // Note: 18 km/h = 5m/s
        assertEquals(0, emptyPath.getTravelTime(18), 1e-6);
        assertEquals(0, singleNodePath.getTravelTime(18), 1e-6);
        assertEquals(8, shortPath.getTravelTime(18), 1e-6);
        assertEquals(12.56, longPath.getTravelTime(18), 1e-6);
        assertEquals(11, loopPath.getTravelTime(18), 1e-6);
        assertEquals(24, longLoopPath.getTravelTime(18), 1e-6);

        // Note: 28.8 km/h = 8m/s
        assertEquals(0, emptyPath.getTravelTime(28.8), 1e-6);
        assertEquals(0, singleNodePath.getTravelTime(28.8), 1e-6);
        assertEquals(5, shortPath.getTravelTime(28.8), 1e-6);
        assertEquals(7.85, longPath.getTravelTime(28.8), 1e-6);
        assertEquals(6.875, loopPath.getTravelTime(28.8), 1e-6);
        assertEquals(15, longLoopPath.getTravelTime(28.8), 1e-6);
    }

    @Test
    public void testGetMinimumTravelTime() {
        assertEquals(0, emptyPath.getMinimumTravelTime(), 1e-4);
        assertEquals(0, singleNodePath.getLength(), 1e-4);
        assertEquals(4, shortPath.getMinimumTravelTime(), 1e-4);
        assertEquals(5.14, longPath.getMinimumTravelTime(), 1e-4);
        assertEquals(5.5, loopPath.getMinimumTravelTime(), 1e-4);
        assertEquals(11.25, longLoopPath.getMinimumTravelTime(), 1e-4);
    }

    @Test
    public void testCreateFastestPathFromNodes() {
        Path path;
        Arc[] expected;

        // Simple construction
        path = Path.createFastestPathFromNodes(graph,
                Arrays.asList(new Node[] { nodes[0], nodes[1], nodes[2] }));
        expected = new Arc[] { a2b, b2c };
        assertEquals(expected.length, path.getArcs().size());
        for (int i = 0; i < expected.length; ++i) {
            assertEquals(expected[i], path.getArcs().get(i));
        }

        // Not so simple construction
        path = Path.createFastestPathFromNodes(graph,
                Arrays.asList(new Node[] { nodes[0], nodes[1], nodes[2], nodes[3] }));
        expected = new Arc[] { a2b, b2c, c2d_3 };
        assertEquals(expected.length, path.getArcs().size());
        for (int i = 0; i < expected.length; ++i) {
            assertEquals(expected[i], path.getArcs().get(i));
        }

        // Trap construction!
        path = Path.createFastestPathFromNodes(graph, Arrays.asList(new Node[] { nodes[1] }));
        assertEquals(nodes[1], path.getOrigin());
        assertEquals(0, path.getArcs().size());

        // Trap construction - The return!
        path = Path.createFastestPathFromNodes(graph, Arrays.asList(new Node[0]));
        assertEquals(null, path.getOrigin());
        assertEquals(0, path.getArcs().size());
        assertTrue(path.isEmpty());
    }

    @Test
    public void testCreateShortestPathFromNodes() {
        Path path;
        Arc[] expected;

        // Simple construction
        path = Path.createShortestPathFromNodes(graph,
                Arrays.asList(new Node[] { nodes[0], nodes[1], nodes[2] }));
        expected = new Arc[] { a2b, b2c };
        assertEquals(expected.length, path.getArcs().size());
        for (int i = 0; i < expected.length; ++i) {
            assertEquals(expected[i], path.getArcs().get(i));
        }

        // Not so simple construction
        path = Path.createShortestPathFromNodes(graph,
                Arrays.asList(new Node[] { nodes[0], nodes[1], nodes[2], nodes[3] }));
        expected = new Arc[] { a2b, b2c, c2d_2 };
        assertEquals(expected.length, path.getArcs().size());
        for (int i = 0; i < expected.length; ++i) {
            assertEquals(expected[i], path.getArcs().get(i));
        }

        // Trap construction!
        path = Path.createShortestPathFromNodes(graph, Arrays.asList(new Node[] { nodes[1] }));
        assertEquals(nodes[1], path.getOrigin());
        assertEquals(0, path.getArcs().size());

        // Trap construction - The return!
        path = Path.createShortestPathFromNodes(graph, Arrays.asList(new Node[0]));
        assertEquals(null, path.getOrigin());
        assertEquals(0, path.getArcs().size());
        assertTrue(path.isEmpty());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateFastestPathFromNodesException() {
        Path.createFastestPathFromNodes(graph, Arrays.asList(new Node[] { nodes[1], nodes[0] }));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateShortestPathFromNodesException() {
        Path.createShortestPathFromNodes(graph, Arrays.asList(new Node[] { nodes[1], nodes[0] }));
    }

}
