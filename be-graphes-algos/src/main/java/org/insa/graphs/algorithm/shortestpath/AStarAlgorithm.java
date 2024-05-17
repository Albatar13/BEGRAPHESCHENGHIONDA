package org.insa.graphs.algorithm.shortestpath;

import org.insa.graphs.model.Node;
import org.insa.graphs.model.Point;


public class AStarAlgorithm extends DijkstraAlgorithm {

    public AStarAlgorithm(ShortestPathData data) {
        super(data);
    }

    public float calcul_cout_estime(Node courant,Node destination){
        return (float)(Point.distance(courant.getPoint(),destination.getPoint()));
    }

    public Label makeLabel(Node courant){
        return new LabelStar(courant);
    }


}