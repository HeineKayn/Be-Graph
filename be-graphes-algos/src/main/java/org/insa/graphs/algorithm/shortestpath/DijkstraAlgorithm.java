package org.insa.graphs.algorithm.shortestpath;
import org.insa.graphs.model.Node;

import java.util.ArrayList;
import java.util.List;

public class DijkstraAlgorithm extends ShortestPathAlgorithm {

    public DijkstraAlgorithm(ShortestPathData data) {
        super(data);
    }

    @Override
    protected ShortestPathSolution doRun() {
        final ShortestPathData data = getInputData();
        ShortestPathSolution solution = null;

        // Initialisation du tableau
        List<Label> labels = Init_Labels();
        
        
        return solution;
    }
    
    public List<Label> Init_Labels(){
    	List<Label> labels = new ArrayList<Label>();
    	List<Node> nodes = getInputData().getGraph().getNodes();
    	for(int i=0; i < nodes.size(); i++) {
    		Node node = nodes.get(i);
    		Label newLabel = new Label(node);
    		labels.add(node.getId(), newLabel);
    	}
    	return labels;
    }
}
