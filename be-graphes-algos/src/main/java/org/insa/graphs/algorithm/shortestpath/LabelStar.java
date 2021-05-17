package org.insa.graphs.algorithm.shortestpath;
import org.insa.graphs.model.Node;

public class LabelStar extends Label {
	
	// Valeur courante du plus court chemin depuis l'origine vers le sommet
	public float estim;

	public LabelStar(Node courant) {
		super(courant);
		this.estim = Float.MAX_VALUE;
	}
	
	@Override
	public float getScore() {
		return this.cout + this.estim;
	}
	
//	public float minLabel(LabelStar other, float val) {
//		System.out.println("A*");
//		return Math.min(this.getScore(), other.getCost() + other.estim + val);
//    }
}
