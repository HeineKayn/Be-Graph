package org.insa.graphs.algorithm.shortestpath;
import org.insa.graphs.model.Node;
    
public class Label {
	
	// Sommet associé à ce label
	private Node sommet_courant;
	
	// Vrai lorsque le coût min de ce sommet est définitivement connu par l'algorithme
	public Boolean marque;
	
	// Valeur courante du plus court chemin depuis l'origine vers le sommet
	private int cout;
	
	// Correspond au sommet précédent sur le chemin qui correspond au plus court chemin courant
	public Node pere;

	public Label(Node courant) {
		this.sommet_courant = courant;
		this.marque = false ;
		this.cout = Integer.MAX_VALUE ;
		this.pere = null;
	}
	
	public int getCost() {
		return this.cout;
	}
}
