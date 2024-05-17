package org.insa.graphs.algorithm.shortestpath;
import org.insa.graphs.model.Node;

public class LabelStar extends Label{
    protected float coutdest;
    public LabelStar(Node sommet_courant){
        super(sommet_courant);
    }

    public void setcoutdest(float cout){
        this.coutdest=cout;
    }
    
    public float getcoutdest(){
        return this.coutdest;
    }
    
    @Override
    public float gettotalcost(){
        return this.getcoutdest() + this.getCout_realise();
    }
    
}
