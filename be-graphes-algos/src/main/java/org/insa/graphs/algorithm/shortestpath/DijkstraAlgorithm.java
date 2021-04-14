package org.insa.graphs.algorithm.shortestpath;
import org.insa.graphs.algorithm.AbstractSolution;
import org.insa.graphs.algorithm.utils.BinaryHeap;
import org.insa.graphs.model.*;

import java.util.ArrayList;
import java.util.List;

public class DijkstraAlgorithm extends ShortestPathAlgorithm {
	
	// pile qu'on utilise pour le dijkstra
	public BinaryHeap<Label> tas = new BinaryHeap();

    public DijkstraAlgorithm(ShortestPathData data) {
        super(data);
    }

    @Override
    protected ShortestPathSolution doRun() {
        final ShortestPathData data = getInputData();

        // Initialisation du tableau de label
        List<Label> labels = Init_Labels(data.getGraph().getNodes());
        
        // Initialisation du premier point
        Label x = labels.get(data.getOrigin().getId());
        x.cout = 0;
    	this.tas.insert(x);
        
    	// Itérations
    	boolean atteint = false;
    	while(!this.tas.isEmpty() && !atteint) {
    		
    		// On marque le premier élément de la pile
    		x = this.tas.findMin();
    		this.tas.deleteMin();
    		x.marque = true;
    		
    		// Pour chacun de ses successeurs 
    		for(Arc arc : x.sommet_courant.getSuccessors()) {
    			Label y = labels.get(arc.getDestination().getId());
    			
    			// Si il n'a pas déjà été marqué
    			if(!y.marque) {
    				float min = Math.min(y.cout, x.cout + arc.getLength());
    				
    				// Si son cout à été mis à jour
    				if (y.cout != min) {
    					y.cout = min;
    					y.pere = arc;
    					
    					// On update
    					try {
    						this.tas.remove(y);
    						this.tas.insert(y);
    					}
    					catch(Exception e){
    					}
    				}
    			}
    			// On s'arrête si on atteint la destination ?
    			if(x.sommet_courant == data.getDestination()) {
    				atteint = true;
    			}
    		}
    	}
        return CreateSolution(labels,data);
    }
    
    public ShortestPathSolution CreateSolution(List<Label> labels, ShortestPathData data){
    	List<Arc> arcs = new ArrayList<Arc>();
    	for(Label label : labels) {
    		arcs.add(label.pere);
    	}
    	return new ShortestPathSolution(data, AbstractSolution.Status.FEASIBLE, new Path(data.getGraph(),arcs));
    }
    
    public List<Label> Init_Labels(List<Node> nodes){
    	List<Label> labels = new ArrayList<Label>();
    	for(int i=0; i < nodes.size(); i++) {
    		Node node = nodes.get(i);
    		Label newLabel = new Label(node);
    		labels.add(node.getId(), newLabel);
    	}
    	return labels;
    }
}
