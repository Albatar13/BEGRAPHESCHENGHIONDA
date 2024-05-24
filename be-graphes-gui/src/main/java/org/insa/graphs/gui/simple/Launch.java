package org.insa.graphs.gui.simple;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.lang.Exception;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import org.insa.graphs.gui.drawing.Drawing;
import org.insa.graphs.gui.drawing.components.BasicDrawing;
import org.insa.graphs.model.Graph;
import org.insa.graphs.model.Node;
import org.insa.graphs.model.Path;
import org.insa.graphs.model.io.BinaryGraphReader;
import org.insa.graphs.model.io.BinaryPathReader;
import org.insa.graphs.model.io.GraphReader;
import org.insa.graphs.model.io.PathReader;
import org.insa.graphs.algorithm.ArcInspector;
import org.insa.graphs.algorithm.ArcInspectorFactory;
import org.insa.graphs.algorithm.AbstractSolution.Status;
import org.insa.graphs.algorithm.shortestpath.*;
import org.insa.graphs.algorithm.utils.PathNotFoundException;

public class Launch {

    private static ShortestPathData data;
    private static DijkstraAlgorithm dijstra_algo;
    private static ShortestPathSolution dijstra_solution;
    private static BellmanFordAlgorithm BMF_algo;
    private static ShortestPathSolution BMF_solution;
    private static AStarAlgorithm Astar_algo;
    private static ShortestPathSolution Astar_solution;
    private static long BMF_temps;
    private static long dijstra_temps;
    private static long Astar_temps;
    private static long startime;
    private static long endtime;
    private static int origine;
    private static int destination;
    private static Random random =new Random();
    private static float BMF_res=0;
    private static float dijkstra_res=0;
    private static float Astar_res=0;
    private final static int nb_test1=10;
    private final static int nb_test2=5;
    
    /**
     * Create a new Drawing inside a JFrame an return it.
     * 
     * @return The created drawing.
     * 
     * @throws Exception if something wrong happens when creating the graph.
     */
    public static Drawing createDrawing() throws Exception {
        BasicDrawing basicDrawing = new BasicDrawing();
        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                JFrame frame = new JFrame("BE Graphes - Launch");
                frame.setLayout(new BorderLayout());
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setVisible(true);
                frame.setSize(new Dimension(800, 600));
                frame.setContentPane(basicDrawing);
                frame.validate();
            }
        });
        return basicDrawing;
    }

    public static void main(String[] args) throws Exception {
        System.out.println(" Premier scénario : Toulouse ");
        // Visit these directory to see the list of available files on Commetud.
        final String toulouse = "/mnt/commetud/3eme Annee MIC/Graphes-et-Algorithmes/Maps/toulouse.mapgr";
        
        //final String madagascar="/mnt/commetud/3eme Annee MIC/Graphes-et-Algorithmes/Maps/madagascar.mapgr";
        //final String pathName = "/home/juchen/3A/Graphes/test2.path";
        GraphReader reader = new BinaryGraphReader(new DataInputStream(new BufferedInputStream(new FileInputStream(toulouse))));
        final Graph graph_toulouse = reader.read();
        //reader = new BinaryGraphReader(new DataInputStream(new BufferedInputStream(new FileInputStream(madagascar))));
        //final Graph graph_madagascar = reader.read();
   

        
        // Create the drawing:
        final Drawing drawing = createDrawing();

        // Draw the graph on the drawing.
        //drawing.drawGraph(graph_toulouse);
        for(int i=0;i<nb_test1;i++){
            origine= random.nextInt(graph_toulouse.size());
            destination=random.nextInt(graph_toulouse.size());
            System.out.println("Test numéro " + i + " origine: " + origine + " destination: " + destination +"\n");
            try {
                testShortestAllRoads(origine,destination, graph_toulouse,"Toulouse",1);
                
            } catch (PathNotFoundException e) {
                System.out.println(e.getMessage()+"\n");
            }
            try {
                testShortestCarsonly(origine,destination, graph_toulouse, "Toulouse",1);
                
            } catch (PathNotFoundException e) {
                System.out.println(e.getMessage()+"\n");
            }
            try {
                testFastestAllRoads(origine,destination, graph_toulouse,"Toulouse",1);
                
            } catch (PathNotFoundException e) {
                System.out.println(e.getMessage()+"\n");
            }
            try {
                testFastestCarsOnly(origine,destination, graph_toulouse, "Toulouse",1);
                
            } catch (PathNotFoundException e) {
                System.out.println(e.getMessage()+"\n");
            }
        }
        System.out.println("Le temps moyen BMF : "+BMF_res);
        System.out.println("Le temps moyen Dijkstra : "+dijkstra_res);
        System.out.println("Le temps moyen Astar : "+Astar_res);
        
        BMF_res=0;
        dijkstra_res=0;
        Astar_res=0;
        System.out.println(" Deuxieme scénario : Midi Pyrenees ");
        final String midi_pyrenees= "/mnt/commetud/3eme Annee MIC/Graphes-et-Algorithmes/Maps/midi-pyrenees.mapgr";

        reader = new BinaryGraphReader(new DataInputStream(new BufferedInputStream(new FileInputStream(midi_pyrenees))));
        final Graph graph_midi_pyrenees = reader.read();

        //drawing.drawGraph(graph_midi_pyrenees);
         for(int i=0;i<5;i++){
            origine= random.nextInt(graph_midi_pyrenees.size());
            destination=random.nextInt(graph_midi_pyrenees.size());
            System.out.println("Test numéro " + i + " origine: " + origine + " destination: " + destination +"\n");
            try {
                testShortestAllRoads(origine,destination, graph_midi_pyrenees,"Midi Pyrenees",2);
            } catch (PathNotFoundException e) {
                System.out.println(e.getMessage()+"\n");
            }
            try {
                testShortestCarsonly(origine,destination,graph_midi_pyrenees, "Midi Pyrenees",2);
            } catch (PathNotFoundException e) {
                System.out.println(e.getMessage()+"\n");
            }
            try {
                testFastestAllRoads(origine,destination,graph_midi_pyrenees,"Midi Pyrenees",2);
            } catch (PathNotFoundException e) {
                System.out.println(e.getMessage()+"\n");
            }
            try {
                testFastestCarsOnly(origine,destination, graph_midi_pyrenees, "Midi Pyrenees",2);
            } catch (PathNotFoundException e) {
                System.out.println(e.getMessage()+"\n");
            }
        }
        System.out.println("Le temps moyen Dijkstra : "+dijkstra_res);
        System.out.println("Le temps moyen Astar : "+Astar_res);


        // Create a PathReader.
        //final PathReader pathReader = new BinaryPathReader(new DataInputStream(new BufferedInputStream(new FileInputStream(pathName))));

        // Read the path.
        //final Path path = pathReader.readPath(graph);
        
        //System.out.println(path.getLength());
        // Draw the path.
        //drawing.drawPath(path);

        //reader.close();
        //reader.close();
        //pathReader.close();
    }



    public static void init1(int orgin,int dest,int mode,Graph graph){
        data=new ShortestPathData(graph,graph.get(orgin),graph.get(dest),ArcInspectorFactory.getAllFilters().get(mode));
        dijstra_algo=new DijkstraAlgorithm(data);
        
        startime=System.nanoTime();
        dijstra_solution=dijstra_algo.run();
        endtime=System.nanoTime();
        dijstra_temps=(endtime-startime) ;
        dijkstra_res+=dijstra_temps/nb_test1;

        BMF_algo=new BellmanFordAlgorithm(data);
        startime=System.nanoTime();
        BMF_solution=BMF_algo.run();
        endtime=System.nanoTime();
        BMF_temps=endtime-startime;
        BMF_res+=BMF_temps/nb_test1;

        Astar_algo=new AStarAlgorithm(data);
        startime=System.nanoTime();
        Astar_solution=Astar_algo.run();
        endtime=System.nanoTime();
        Astar_temps=endtime-startime;
        Astar_res+=Astar_temps/nb_test1;
    }

    
    public static void init2(int orgin,int dest,int mode,Graph graph){
        data=new ShortestPathData(graph,graph.get(orgin),graph.get(dest),ArcInspectorFactory.getAllFilters().get(mode));
        dijstra_algo=new DijkstraAlgorithm(data);
        startime=System.nanoTime();
        dijstra_solution=dijstra_algo.run();
        endtime=System.nanoTime();
        dijstra_temps=(endtime-startime) ;
        dijkstra_res+=dijstra_temps/nb_test2;

        Astar_algo=new AStarAlgorithm(data);
        startime=System.nanoTime();
        Astar_solution=Astar_algo.run();
        endtime=System.nanoTime();
        Astar_temps=endtime-startime;
        Astar_res+=Astar_temps/nb_test2;
    }
    
    public static void testShortestAllRoads(int origin,int dest,Graph graph,String mapname,int scenario) throws PathNotFoundException{
        System.out.println("---- testShortestAllRoads-----------");
        switch (scenario) {
            case 1:
            init1(origin, dest, 0, graph);
            if(BMF_solution.getStatus()==Status.INFEASIBLE && dijstra_solution.getStatus()==Status.INFEASIBLE && Astar_solution.getStatus()==Status.INFEASIBLE) 
        { throw new PathNotFoundException("PathNotFoundException");} 
            if(dijstra_solution.getPath().getLength()!=Astar_solution.getPath().getLength() || BMF_solution.getPath().getLength()!=Astar_solution.getPath().getLength()||BMF_solution.getPath().getLength()!=dijstra_solution.getPath().getLength()){
            throw new PathNotFoundException("Les trois solutions ne sont pas pareilles");}
            System.out.println("Shortest path length from node " + origin + " to node " + dest + " in " + mapname + "\n with Dijkstra: " + dijstra_solution.getPath().getLength() 
            +" temps d'éxecution: " + dijstra_temps + "\n with BMF: " + BMF_solution.getPath().getLength() + " temps d'éxecution: " + BMF_temps+ "\n with A*: " + Astar_solution.getPath().getLength() + " temps d'éxecution: " +Astar_temps+"\n");    
            System.out.println("OK");
            break;
            case 2:
            init2(origin, dest, 0, graph);
            if(dijstra_solution.getStatus()==Status.INFEASIBLE && Astar_solution.getStatus()==Status.INFEASIBLE) 
        { throw new PathNotFoundException("PathNotFoundException");}
            if(dijstra_solution.getPath().getLength()!=Astar_solution.getPath().getLength()){
                System.out.println(dijstra_solution.getPath().getLength());
                System.out.println(Astar_solution.getPath().getLength());
                throw new PathNotFoundException("Les deux solutions ne sont pas pareilles");}
            System.out.println("Shortest path length from node " + origin + " to node " + dest + " in " + mapname + "\n with Dijkstra: " + dijstra_solution.getPath().getLength() 
            +" temps d'éxecution: " + dijstra_temps + "\n with A*: " + Astar_solution.getPath().getLength() + " temps d'éxecution: " +Astar_temps+"\n");
            int IDnodeintermediaire;
            Path path_dijstra=dijstra_solution.getPath();
            int size_path_dijstra=path_dijstra.size();
            IDnodeintermediaire=(path_dijstra.getArcs().get((int)(size_path_dijstra/2)).getDestination().getId());
            init2(origin,IDnodeintermediaire,0,graph);
            Path path_dijstra1=dijstra_solution.getPath();
            init2(IDnodeintermediaire,dest,0,graph);
            Path path_dijstra2=dijstra_solution.getPath();
            float diff=new Float(0.5);
            if(Math.abs(path_dijstra1.getLength()+path_dijstra2.getLength()-path_dijstra.getLength()) > diff){throw new PathNotFoundException("La somme de sous-chemin != cout de solution(Dijkstra)");}
            System.out.println("OK");
            break;    
            default:
                break;
        }
    }

    public static void testShortestCarsonly(int origin,int dest,Graph graph,String mapname,int scenario) throws PathNotFoundException{
        System.out.println("---- testShortestCarsOnly-----------");
        switch (scenario) {
            case 1:
            init1(origin, dest, 1, graph);
            if(BMF_solution.getStatus()==Status.INFEASIBLE && dijstra_solution.getStatus()==Status.INFEASIBLE && Astar_solution.getStatus()==Status.INFEASIBLE) 
            { throw new PathNotFoundException("PathNotFoundException");}
            if(dijstra_solution.getPath().getLength()!=Astar_solution.getPath().getLength() || BMF_solution.getPath().getLength()!=Astar_solution.getPath().getLength()||BMF_solution.getPath().getLength()!=dijstra_solution.getPath().getLength()){
                throw new PathNotFoundException("Les trois solutions ne sont pas pareilles");}
            System.out.println("ShortestCarsOnly path length from node " + origin + " to node " + dest + " in " + mapname + "\n with Dijkstra: " + dijstra_solution.getPath().getLength() 
            +" temps d'éxecution: " + dijstra_temps + "\n with BMF: " + BMF_solution.getPath().getLength() + " temps d'éxecution: " + BMF_temps+ "\n with A*: " + Astar_solution.getPath().getLength() + " temps d'éxecution: " +Astar_temps+"\n");
            System.out.println("OK");
            break;
            case 2:
            init2(origin, dest, 1, graph);
            if(dijstra_solution.getStatus()==Status.INFEASIBLE && Astar_solution.getStatus()==Status.INFEASIBLE) 
            { throw new PathNotFoundException("PathNotFoundException");}
            if(dijstra_solution.getPath().getLength()!=Astar_solution.getPath().getLength()){throw new PathNotFoundException("Les deux solutions ne sont pas pareilles");}
            System.out.println("ShortestCarsonly path length from node " + origin + " to node " + dest + " in " + mapname + "\n with Dijkstra: " + dijstra_solution.getPath().getLength() 
            +" temps d'éxecution: " + dijstra_temps + "\n with A*: " + Astar_solution.getPath().getLength() + " temps d'éxecution: " +Astar_temps+"\n");
            int IDnodeintermediaire;
            Path path_dijstra=dijstra_solution.getPath();
            int size_path_dijstra=path_dijstra.size();
            IDnodeintermediaire=(path_dijstra.getArcs().get((int)(size_path_dijstra/2)).getDestination().getId());
            init2(origin,IDnodeintermediaire,1,graph);
            Path path_dijstra1=dijstra_solution.getPath();
            init2(IDnodeintermediaire,dest,1,graph);
            Path path_dijstra2=dijstra_solution.getPath();
            float diff=new Float(0.5);
            if(Math.abs(path_dijstra1.getLength()+path_dijstra2.getLength()-path_dijstra.getLength()) > diff){throw new PathNotFoundException("La somme de sous-chemin != cout de solution(Dijkstra)");}  
            System.out.println("OK");
            break;  
            default:
                break;
        }
    }

    public static void testFastestAllRoads(int origin,int dest,Graph graph,String mapname,int scenario) throws PathNotFoundException{
        System.out.println("---- testFastestAllRoads-----------");
        switch (scenario) {
            case 1:
            init1(origin, dest, 2, graph);
            if(BMF_solution.getStatus()==Status.INFEASIBLE && dijstra_solution.getStatus()==Status.INFEASIBLE && Astar_solution.getStatus()==Status.INFEASIBLE) 
            { throw new PathNotFoundException("PathNotFoundException");}
            if(dijstra_solution.getPath().getLength()!=Astar_solution.getPath().getLength() || BMF_solution.getPath().getLength()!=Astar_solution.getPath().getLength()||BMF_solution.getPath().getLength()!=dijstra_solution.getPath().getLength()){
                throw new PathNotFoundException("Les trois solutions ne sont pas pareilles");}
            System.out.println("FastestAllRoads path length from node " + origin + " to node " + dest + " in " + mapname + "\n with Dijkstra: " + dijstra_solution.getPath().getMinimumTravelTime() 
            +" secondes temps d'éxecution: " + dijstra_temps + "\n with BMF: " + BMF_solution.getPath().getMinimumTravelTime() + " secondes temps d'éxecution: " + BMF_temps+ "\n with A*: " + Astar_solution.getPath().getMinimumTravelTime() + " secondes temps d'éxecution: " +Astar_temps+"\n");
            System.out.println("OK");
            break;
            case 2:
            init2(origin, dest, 2, graph);
            if(dijstra_solution.getStatus()==Status.INFEASIBLE && Astar_solution.getStatus()==Status.INFEASIBLE) 
            { throw new PathNotFoundException("PathNotFoundException");}
            if(dijstra_solution.getPath().getLength()!=Astar_solution.getPath().getLength()){throw new PathNotFoundException("Les deux solutions ne sont pas pareilles");}
            System.out.println("FastestAllRoads path length from node " + origin + " to node " + dest + " in " + mapname + "\n with Dijkstra: " + dijstra_solution.getPath().getMinimumTravelTime() 
            +" secondes temps d'éxecution: " + dijstra_temps + "\n with A*: " + Astar_solution.getPath().getMinimumTravelTime() + " secondes temps d'éxecution: " +Astar_temps+"\n");
            int IDnodeintermediaire;
            Path path_dijstra=dijstra_solution.getPath();
            int size_path_dijstra=path_dijstra.size();
            IDnodeintermediaire=path_dijstra.getArcs().get((int)(size_path_dijstra/2)).getDestination().getId();
            init2(origin,IDnodeintermediaire,2,graph);
            Path path_dijstra1=dijstra_solution.getPath();
            init2(IDnodeintermediaire,dest,2,graph);
            Path path_dijstra2=dijstra_solution.getPath();
            float diff=new Float(0.5);
            if(Math.abs(path_dijstra1.getLength()+path_dijstra2.getLength()-path_dijstra.getLength()) > diff){throw new PathNotFoundException("La somme de sous-chemin != cout de solution(Dijkstra)");}
            System.out.println("OK");
            break;
            default:
                break;
        }
    } 
    public static void testFastestCarsOnly(int origin,int dest,Graph graph,String mapname,int scenario) throws PathNotFoundException{
        System.out.println("---- testFastestCarsOnly-----------");
        switch (scenario) {
            case 1:
                init1(origin, dest, 3, graph);
                if(BMF_solution.getStatus()==Status.INFEASIBLE && dijstra_solution.getStatus()==Status.INFEASIBLE && Astar_solution.getStatus()==Status.INFEASIBLE) 
                { throw new PathNotFoundException("PathNotFoundException");}
                if(dijstra_solution.getPath().getLength()!=Astar_solution.getPath().getLength() || BMF_solution.getPath().getLength()!=Astar_solution.getPath().getLength()||BMF_solution.getPath().getLength()!=dijstra_solution.getPath().getLength()){
                    throw new PathNotFoundException("Les trois solutions ne sont pas pareilles");}
                System.out.println("FastestCarOnly path length from node " + origin + " to node " + dest + " in " + mapname + "\n with Dijkstra: " + dijstra_solution.getPath().getMinimumTravelTime() 
                +" secondes semps d'éxecution: "+dijstra_temps+"\n with BMF: " + BMF_solution.getPath().getMinimumTravelTime() + " secondes temps d'éxecution: "+BMF_temps+"\n with A*: " + Astar_solution.getPath().getMinimumTravelTime() + " secondes temps d'éxecution: "+Astar_temps+"\n");
                System.out.println("OK");
                break;
            case 2:
                init2(origin, dest, 3, graph);
                if(dijstra_solution.getStatus()==Status.INFEASIBLE && Astar_solution.getStatus()==Status.INFEASIBLE) 
                { throw new PathNotFoundException("PathNotFoundException");}
                if(dijstra_solution.getPath().getLength()!=Astar_solution.getPath().getLength()){throw new PathNotFoundException("Les deux solutions ne sont pas pareilles");}
                System.out.println("FastestCarOnly path length from node " + origin + " to node " + dest + " in " + mapname + "\n with Dijkstra: " + dijstra_solution.getPath().getMinimumTravelTime() 
                + " secondes temps d'éxecution: "+dijstra_temps+"\n with A*: " + Astar_solution.getPath().getMinimumTravelTime() + " secondes temps d'éxecution: "+Astar_temps+"\n");
                int IDnodeintermediaire;
                Path path_dijstra=dijstra_solution.getPath();
                int size_path_dijstra=path_dijstra.size();
                IDnodeintermediaire=(path_dijstra.getArcs().get((int)(size_path_dijstra/2)).getDestination().getId());
                init2(origin,IDnodeintermediaire,3,graph);
                Path path_dijstra1=dijstra_solution.getPath();
                init2(IDnodeintermediaire,dest,3,graph);
                Path path_dijstra2=dijstra_solution.getPath();
                float diff=new Float(0.5);
                if(Math.abs(path_dijstra1.getLength()+path_dijstra2.getLength()-path_dijstra.getLength()) > diff){throw new PathNotFoundException("La somme de sous-chemin != cout de solution(Dijkstra)");}
                System.out.println("OK");
                break; 
            default:
                break;
        }
    } 


}
