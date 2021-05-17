package org.insa.graphs.algorithm.shortestpath;
import org.insa.graphs.model.Node;
import org.insa.graphs.model.Arc;
    
public class Label implements Comparable<Label> {
	
	// Sommet associé à ce label
	public Node sommet_courant;
	
	// Vrai lorsque le coût min de ce sommet est définitivement connu par l'algorithme
	public Boolean marque;
	
	// Valeur courante du plus court chemin depuis l'origine vers le sommet
	public float cout;
	
	// Correspond au sommet précédent sur le chemin qui correspond au plus court chemin courant
	public Arc pere;

	public Label(Node courant) {
		this.sommet_courant = courant;
		this.marque = false ;
		this.cout = Float.MAX_VALUE ;
		this.pere = null;
	}
	
	public float getCost() {
		return this.cout;
	}
	
	public float getScore() {
		return this.cout;
	}
	
	public float minLabel(Label other, float val) {
		return Math.min(this.getScore(),other.getScore()+val);
    }
	
	@Override
    public String toString() {
        return String.valueOf(this.getCost());
    }
	
	public int compareTo(Label other) {
        return (int)(this.getCost()-other.getCost());
    }
}
