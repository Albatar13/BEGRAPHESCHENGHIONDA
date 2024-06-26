package org.insa.graphs.algorithm.shortestpath;
import java.util.ArrayList;
import java.util.Collections;

import java.util.List;

import org.insa.graphs.algorithm.AbstractInputData;
import org.insa.graphs.algorithm.AbstractSolution.Status;
import org.insa.graphs.algorithm.utils.BinaryHeap;
import org.insa.graphs.model.*;
import org.insa.graphs.algorithm.utils.ElementNotFoundException;


public class DijkstraAlgorithm extends ShortestPathAlgorithm {

    public DijkstraAlgorithm(ShortestPathData data) {
        super(data);
    }

    public float calcul_cout_estime(Node courant,Node destination){
        return(0);
    }

    public Label makeLabel(Node courant){
        return new Label(courant);
    }

    @Override
    protected ShortestPathSolution doRun() {
        final ShortestPathData data = getInputData();
        ShortestPathSolution solution = null;

		//Declaration de la taille du graphe, les labels des nodes et le tas qu'on va utiliser 
        boolean fini =false;
        int tailleGraphe = data.getGraph().size();
        Label Tab[]= new Label[tailleGraphe];
        BinaryHeap<Label> Tas = new BinaryHeap<Label>();    
        
        /*on definit le depart */
        Label Deb = makeLabel(data.getOrigin());
        Tab[Deb.getSommet_courant().getId()] = Deb;
        Deb.settotalcost(0);
        Deb.setCout_realise(0);
        Tas.insert(Deb); 
        Deb.InTas();

        notifyOriginProcessed(data.getOrigin());

        while(!Tas.isEmpty() && !fini){
            Label courant= Tas.deleteMin();
            notifyNodeMarked(courant.getSommet_courant());
            courant.setMarque(true);
            if (courant.getSommet_courant()==data.getDestination()){
                fini=true;
            } 
            
            /*on parcourt les successors de ce node pour les actualiser*/
            List<Arc> arcliste= courant.getSommet_courant().getSuccessors();
            for (int i=0;i<arcliste.size();i++){
                if(data.isAllowed(arcliste.get(i))){
                Node successor = (arcliste.get(i).getDestination());
                Label successorLabel= Tab[successor.getId()];
                
                    /*si ce node n'est pas encore dans le tableau, on le rajout */
                    if (successorLabel==null){
                        notifyNodeReached(arcliste.get(i).getDestination());
                        successorLabel= makeLabel(successor);
                        Tab[successor.getId()]= successorLabel; 
                    }  
                    
                    /*si le successor n'est pas marque */
                    if(!successorLabel.isMarque()){
                        /*le cas ou le cout a ete mis a jour */
                        double a;
                        if(data.getMode()== AbstractInputData.Mode.LENGTH){
                            a =(double)arcliste.get(i).getLength();
                        }else{
                            a =arcliste.get(i).getMinimumTravelTime();
                        }

                        if(successorLabel.getCout_realise()>(float)a+courant.getCout_realise()
                        ||successorLabel.getCout_realise()==Float.POSITIVE_INFINITY){
                            if(successorLabel.isInTas()){
                                try{Tas.remove(successorLabel);}catch(ElementNotFoundException e){}
                                
                            }else{
                                successorLabel.InTas();
                            }
                            successorLabel.setCout_realise((float)a+courant.getCout_realise());
                            if(data.getMode()==AbstractInputData.Mode.LENGTH){
                                successorLabel.settotalcost((float)a+courant.getCout_realise()+calcul_cout_estime(successorLabel.getSommet_courant(), data.getDestination()));
                            }else{
                                successorLabel.settotalcost((float)a+courant.getCout_realise()+(float)(calcul_cout_estime(successorLabel.getSommet_courant(), data.getDestination())* 3600.0 / (100 * 1000.0)));
                            }
                            successorLabel.setPere(arcliste.get(i));
                            Tas.insert(successorLabel);  
                        } 
                    }
                }
            } 

        } 
        
        /*la destination n'est pas atteint */
        if(!fini){
            solution = new ShortestPathSolution(data, Status.INFEASIBLE);
        /*la destination est atteint */
        }else{
            notifyDestinationReached(data.getDestination());
            ArrayList<Arc> arcs= new ArrayList<>();
            Arc arc=Tab[data.getDestination().getId()].getPere();
            while(arc!=null){
                arcs.add(arc);
                arc=Tab[arc.getOrigin().getId()].getPere(); 
            } 
            Collections.reverse(arcs);
            solution = new ShortestPathSolution(data, Status.OPTIMAL, new Path(data.getGraph(), arcs));
        } 

        return solution;
    }

}
