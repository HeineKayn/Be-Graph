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
    			newLabel.estim = newLabel.sommet_courant.getPoint().distanceTo(data.getDestination().getPoint());
			}
			else {
				double speed = data.getMaximumSpeed()*3.6;
				if(speed < 0) {
					speed = data.getGraph().getGraphInformation().getMaximumSpeed()*3.6;
				}
				newLabel.estim = newLabel.sommet_courant.getPoint().distanceTo(data.getDestination().getPoint()) / speed;
			}
    		this.labels.add(node.getId(), newLabel);
    	}
    	// Initialisation du premier point
        LabelStar x = (LabelStar)this.labels.get(data.getOrigin().getId());
        this.notifyOriginProcessed(x.sommet_courant);
        x.cout = 0;
    	this.tas.insert(x);
    }
}
