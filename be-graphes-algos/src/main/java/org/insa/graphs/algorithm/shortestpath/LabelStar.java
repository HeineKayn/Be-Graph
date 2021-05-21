package org.insa.graphs.algorithm.shortestpath;
import org.insa.graphs.model.Node;

public class LabelStar extends Label {
	
	// Valeur courante du plus court chemin depuis l'origine vers le sommet
	public double estim;

	public LabelStar(Node courant) {
		super(courant);
		this.estim = Float.MAX_VALUE;
	}
	
	@Override
	public double getScore() {
		return this.cout + this.estim;
	}
}
