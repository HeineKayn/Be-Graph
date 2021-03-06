package org.insa.graphs.algorithm.utils;

import org.insa.graphs.algorithm.shortestpath.DijkstraAlgorithm;
import org.insa.graphs.algorithm.shortestpath.ShortestPathAlgorithm;
import org.insa.graphs.algorithm.shortestpath.ShortestPathData;

public class DijkstraTest extends ShortestAlgosTest {
	
	@Override
    protected ShortestPathAlgorithm createAlgorithm(ShortestPathData data) {
        return new DijkstraAlgorithm(data);
    }
}
