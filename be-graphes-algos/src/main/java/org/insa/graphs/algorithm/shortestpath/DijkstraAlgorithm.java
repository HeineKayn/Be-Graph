package org.insa.graphs.algorithm.shortestpath;
import org.insa.graphs.algorithm.AbstractInputData;
import org.insa.graphs.algorithm.AbstractSolution;
import org.insa.graphs.algorithm.utils.BinaryHeap;
import org.insa.graphs.model.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DijkstraAlgorithm extends ShortestPathAlgorithm {
	
	// pile qu'on utilise pour le dijkstra
	public BinaryHeap<Label> tas = new BinaryHeap<Label>();
	
	// liste des labels
	public List<Label> labels = new ArrayList<Label>();

    public DijkstraAlgorithm(ShortestPathData data) {
        super(data);
    }
    
    public void Init_Labels(ShortestPathData data){
    	for(int i=0; i < data.getGraph().getNodes().size(); i++) {
    		Node node = data.getGraph().getNodes().get(i);
    		Label newLabel = new Label(node);
    		this.labels.add(node.getId(), newLabel);
    	}
        // Initialisation du premier point
        Label x = labels.get(data.getOrigin().getId());
        this.notifyOriginProcessed(x.sommet_courant);
        x.cout = 0;
    	this.tas.insert(x);
    }

    @Override
    protected ShortestPathSolution doRun() {
        final ShortestPathData data = getInputData();

        // Initialisation du tableau de label
        this.Init_Labels(data);
        
        int nb_succ = 0;
        
    	// Itérations
    	while(!this.tas.isEmpty()) {
    		
    		// On marque le premier élément de la pile
    		Label x = this.tas.findMin();
    		this.tas.deleteMin();
    		x.marque = true;
    		this.notifyNodeMarked(x.sommet_courant);
    		
    		// On s'arrête si on atteint la destination
			if(x.sommet_courant == data.getDestination()) {
				this.notifyDestinationReached(x.sommet_courant);
				break;
			}
    		
    		// Pour chacun de ses successeurs 
    		for(Arc arc : x.sommet_courant.getSuccessors()) {
    			
    			// ici on a un problème -> Comment adapter à ce que ça soit un LabelStar
    			Label y = labels.get(arc.getDestination().getId());
    			nb_succ ++;
    			
    			// Si il n'a pas déjà été marqué et que le chemin est possible avec le véhicule
    			if(!y.marque && data.isAllowed(arc)) {
    				double min = Math.min(y.getCost(), x.getCost() + data.getCost(arc)); 
    				
    				// Si son cout à été mis à jour
    				if (y.cout > min) {
    					
    					// On update
    					if (y.cout < Double.MAX_VALUE) {
    						this.tas.remove(y);		
    					}
    					
    					y.cout = min;
    					y.pere = arc;
    					this.tas.insert(y);
    					//this.notifyNodeReached(y.sommet_courant);
    				}
    			}
    		}
//    		System.out.print("Cout label marqué : " + x.getCost() + "\n");
//    		System.out.print("Nombre de successeurs du sommet : " + nb_succ + "\n");
//			System.out.print("Le tas est toujours valide ?  " + this.tas.isValid() + "\n");
			nb_succ = 0;
    	}
    	ShortestPathSolution sol = CreateSolution(labels,data);
    	//System.out.print("Le chemin solution est valide ?  " + sol.getPath().isValid() + "\n");
        return sol;
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
