package org.insa.graphs.algorithm.shortestpath;

import org.insa.graphs.model.Arc;
import org.insa.graphs.model.Node;

public class Label implements Comparable<Label> {
    private boolean marque;
    private float cout_realise;
    private Node sommet_courant;
    private Arc pere;
    private boolean isInTas;
    private float totalcost;
    
    public Label(Node sommet_courant){
        this.marque=false;
        this.cout_realise=Float.POSITIVE_INFINITY;
        this.sommet_courant=sommet_courant;
        this.pere=null;
        this.totalcost=Float.POSITIVE_INFINITY;     
    }

    public void setMarque(boolean marque) {
        this.marque = marque;
    }

    public void setCout_realise(float cout_realise) {
        this.cout_realise = cout_realise;
    }

    public void settotalcost(float totalcost){
        this.cout_realise=totalcost;
    }


    public void setSommet_courant(Node sommet_courant) {
        this.sommet_courant = sommet_courant;
    }

    public void setPere(Arc pere) {
        this.pere = pere;
    }

    public void InTas(){
        this.isInTas=true;
    } 
    public boolean isMarque() {
        return marque;
    }

    public float getCout_realise() {
        return cout_realise;
    }

    public Node getSommet_courant() {
        return sommet_courant;
    }

    public Arc getPere() {
        return pere;
    }

    public boolean isInTas(){
        return isInTas;
    } 
    public float gettotalcost(){
        return cout_realise;
    }


    public int compareTo(Label autre) {
		int resultat;
		if (this.gettotalcost() < autre.gettotalcost()) {
			resultat = -1;
		}
		else if (this.gettotalcost() == autre.gettotalcost()) {
			resultat = 0;
		}
		else {
			resultat = 1;
		}
		return resultat;
	}
   
}
