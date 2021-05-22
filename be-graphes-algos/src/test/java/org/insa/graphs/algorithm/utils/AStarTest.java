package org.insa.graphs.algorithm.utils;

import static org.junit.Assert.assertTrue;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Random;

import org.insa.graphs.algorithm.AbstractInputData;
import org.insa.graphs.algorithm.ArcInspectorFactory;
import org.insa.graphs.algorithm.shortestpath.AStarAlgorithm;
import org.insa.graphs.algorithm.shortestpath.BellmanFordAlgorithm;
import org.insa.graphs.algorithm.shortestpath.ShortestPathAlgorithm;
import org.insa.graphs.algorithm.shortestpath.ShortestPathData;
import org.insa.graphs.model.Graph;
import org.insa.graphs.model.Node;
import org.insa.graphs.model.Path;
import org.insa.graphs.model.io.BinaryGraphReader;
import org.junit.Test;

public class AStarTest extends ShortestAlgosTest {
	
	private static String toulouse_map = "C:\\Users\\thoma\\Documents\\GitHub\\Be-Graph\\RessourcesGraph\\Maps\\toulouse.mapgr";
	
	@Override
    protected ShortestPathAlgorithm createAlgorithm(ShortestPathData data) {
        return new AStarAlgorithm(data);
    }
	
	@Test
    public void testAstar() throws IOException {
		int i = 0;
		while(i<50) {
			BinaryGraphReader reader = new BinaryGraphReader(new DataInputStream(new BufferedInputStream(new FileInputStream(toulouse_map))));
			Graph graph = reader.read();
			Random rand = new Random();
			
			Node origin = graph.getNodes().get(rand.nextInt(graph.size()));
			Node destination = graph.getNodes().get(rand.nextInt(graph.size()));
					
			ShortestPathData data = new ShortestPathData(graph, origin, destination, 
					ArcInspectorFactory.getAllFilters().get(rand.nextInt(5)));
			
			Path sol = new BellmanFordAlgorithm(data).run().getPath();
			
			if(sol == null) {
				break;
			}
			i++;
			
			ShortestPathAlgorithm algo = createAlgorithm(data);
			Path path = algo.run().getPath();
			
			// On regarde si la distance trouvée est <= à la distance origine -> arrivé à vol d'oiseau
			if (data.getMode() == AbstractInputData.Mode.LENGTH) {
				assertTrue(origin.getPoint().distanceTo(destination.getPoint()) - path.getLength() >= 0);
			}
			
			else {
				double speed = data.getMaximumSpeed()*3.6;
				if(speed < 0) {
					speed = data.getGraph().getGraphInformation().getMaximumSpeed()*3.6;
				}
				assertTrue(origin.getPoint().distanceTo(destination.getPoint())/speed - path.getMinimumTravelTime() >= 0);
			}
		}
    }
}
