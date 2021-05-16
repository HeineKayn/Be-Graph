package org.insa.graphs.algorithm.shortestpath;

import org.insa.graphs.algorithm.AbstractInputData;
import org.insa.graphs.model.Node;

public class AStarAlgorithm extends DijkstraAlgorithm {

    public AStarAlgorithm(ShortestPathData data) {
        super(data);
    }

    public void Init_Labels(ShortestPathData data){
    	for(int i=0; i < data.getGraph().getNodes().size(); i++) {
    		Node node = data.getGraph().getNodes().get(i);
    		LabelStar newLabel = new LabelStar(node);	
    		
    		if (data.getMode() == AbstractInputData.Mode.LENGTH) {
    			newLabel.estim = (float)newLabel.sommet_courant.getPoint().distanceTo(data.getDestination().getPoint());
			}
			else {
				newLabel.estim = (float)newLabel.sommet_courant.getPoint().distanceTo(data.getDestination().getPoint()) / data.getGraph().getGraphInformation().getMaximumSpeed();
			}
    		this.labels.add(node.getId(), newLabel);
    	}
    }
}
