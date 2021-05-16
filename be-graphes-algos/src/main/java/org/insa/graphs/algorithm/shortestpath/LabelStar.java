package org.insa.graphs.algorithm.shortestpath;
import org.insa.graphs.model.Node;

public class LabelStar extends Label {
	
	// Valeur courante du plus court chemin depuis l'origine vers le sommet
	public float estim;

	public LabelStar(Node courant) {
		super(courant);
		this.estim = Float.MAX_VALUE;
	}
	
	public int compareTo(LabelStar other) {
        return Float.compare(this.getCost()+this.estim, other.getCost()+other.estim);
    }
}
