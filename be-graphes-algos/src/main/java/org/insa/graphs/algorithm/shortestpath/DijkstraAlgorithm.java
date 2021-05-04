package org.insa.graphs.algorithm.shortestpath;
import org.insa.graphs.algorithm.AbstractSolution;
import org.insa.graphs.algorithm.utils.BinaryHeap;
import org.insa.graphs.model.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DijkstraAlgorithm extends ShortestPathAlgorithm {
	
	// pile qu'on utilise pour le dijkstra
	public BinaryHeap<Label> tas = new BinaryHeap<Label>();

    public DijkstraAlgorithm(ShortestPathData data) {
        super(data);
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
    	while(!this.tas.isEmpty()) {
    		
    		// On marque le premier élément de la pile
    		x = this.tas.findMin();
    		this.tas.deleteMin();
    		x.marque = true;
    		
    		// On s'arrête si on atteint la destination ?
			if(x.sommet_courant == data.getDestination()) {
				break;
			}
    		
    		// Pour chacun de ses successeurs 
    		for(Arc arc : x.sommet_courant.getSuccessors()) {
    			Label y = labels.get(arc.getDestination().getId());
    			
    			// Si il n'a pas déjà été marqué et que le chemin est possible avec le véhicule
    			if(!y.marque && data.isAllowed(arc)) {
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
    						this.tas.insert(y);
    					}
    				}
    			}
    		}
    	}
        return CreateSolution(labels,data);
    }
    
    public ShortestPathSolution CreateSolution(List<Label> labels, ShortestPathData data){
    	
    	Label y = labels.get(data.getDestination().getId());
    	if (!y.marque) {
    		return new ShortestPathSolution(data, AbstractSolution.Status.INFEASIBLE);
    	}
    	else {
    		List<Arc> arcs = new ArrayList<Arc>();
        	while(y.pere != null) {
        		arcs.add(y.pere);
        		y = labels.get(y.pere.getOrigin().getId());
        	}
        	Collections.reverse(arcs);
        	return new ShortestPathSolution(data, AbstractSolution.Status.FEASIBLE, new Path(data.getGraph(),arcs));
    	}
    }
    
}
