package org.insa.graphs.algorithm.shortestpath;

import org.insa.graphs.model.Arc;
import org.insa.graphs.model.Node;

public class Label {
    private boolean marque;
    public void setMarque(boolean marque) {
        this.marque = marque;
    }

    public void setCout_realise(int cout_realise) {
        this.cout_realise = cout_realise;
    }

    public void setSommet_courant(double sommet_courant) {
        this.sommet_courant = sommet_courant;
    }

    public void setPere(Arc pere) {
        this.pere = pere;
    }

    public static void setN(int n) {
        N = n;
    }

    public boolean isMarque() {
        return marque;
    }

    public int getCout_realise() {
        return cout_realise;
    }

    public double getSommet_courant() {
        return sommet_courant;
    }

    public Arc getPere() {
        return pere;
    }

    public static int getN() {
        return N;
    }

    private int cout_realise;
    private double sommet_courant;
    private Arc pere;
    static int N=0;
    
    public Label(boolean marque,int cout_realise){
        this.marque=marque;
        this.cout_realise=cout_realise;
    }

    

   
}
