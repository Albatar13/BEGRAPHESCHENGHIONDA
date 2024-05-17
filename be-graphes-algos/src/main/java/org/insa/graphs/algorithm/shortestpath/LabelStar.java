package org.insa.graphs.algorithm.shortestpath;
import org.insa.graphs.model.Node;

public class LabelStar extends Label{
    private float totalcost;
    public LabelStar(Node sommet_courant){
        super(sommet_courant);
    }
    
    public void settotalcost(float totalcost){
        this.totalcost=totalcost;
    }

    @Override
    public float gettotalcost(){
        return this.totalcost;
    }

    
}
