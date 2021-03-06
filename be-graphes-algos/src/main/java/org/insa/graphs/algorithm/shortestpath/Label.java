package org.insa.graphs.algorithm.shortestpath;
import org.insa.graphs.model.Node;
import org.insa.graphs.model.Arc;
    
public class Label implements Comparable<Label> {
	
	// Sommet associé à ce label
	public Node sommet_courant;
	
	// Vrai lorsque le coût min de ce sommet est définitivement connu par l'algorithme
	public Boolean marque;
	
	// Valeur courante du plus court chemin depuis l'origine vers le sommet
	public double cout;
	
	// Correspond au sommet précédent sur le chemin qui correspond au plus court chemin courant
	public Arc pere;

	public Label(Node courant) {
		this.sommet_courant = courant;
		this.marque = false ;
		this.cout = Double.MAX_VALUE ;
		this.pere = null;
	}
	
	public double getCost() {
		return this.cout;
	}
	
	public double getScore() {
		return this.cout;
	}
	
	@Override
    public String toString() {
        return String.valueOf(this.getCost());
    }
	
	public int compareTo(Label other) {
        return (int)(this.getScore()-other.getScore());
    }
}
