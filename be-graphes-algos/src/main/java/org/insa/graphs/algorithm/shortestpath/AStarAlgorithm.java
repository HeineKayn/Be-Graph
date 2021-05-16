package org.insa.graphs.algorithm.shortestpath;
import java.util.ArrayList;
import java.util.List;
import org.insa.graphs.model.Node;

public class AStarAlgorithm extends DijkstraAlgorithm {

    public AStarAlgorithm(ShortestPathData data) {
        super(data);
    }

    public List<LabelStar> Init_Labels(ShortestPathData data){
    	List<LabelStar> labels = new ArrayList<LabelStar>();
    	for(int i=0; i < data.getGraph().getNodes().size(); i++) {
    		Node node = data.getGraph().getNodes().get(i);
    		LabelStar newLabel = new LabelStar(node);		
    		newLabel.estim = (float)newLabel.sommet_courant.getPoint().distanceTo(data.getDestination().getPoint());
    		
    		labels.add(node.getId(), newLabel);
    	}
    	return labels;
    }
}
