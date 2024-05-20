package org.insa.graphs.gui.simple;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.lang.Exception;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import org.insa.graphs.gui.drawing.Drawing;
import org.insa.graphs.gui.drawing.components.BasicDrawing;
import org.insa.graphs.model.Graph;
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

        // Visit these directory to see the list of available files on Commetud.
        final String toulouse = "/mnt/commetud/3eme Annee MIC/Graphes-et-Algorithmes/Maps/toulouse.mapgr";
        //final String midi_pyrenees= "/mnt/commetud/3eme Annee MIC/Graphes-et-Algorithmes/Maps/midi_pyrennes.mapgr";
        //final String madagascar="/mnt/commetud/3eme Annee MIC/Graphes-et-Algorithmes/Maps/madagascar.mapgr";
        //final String pathName = "/home/juchen/3A/Graphes/test2.path";
        GraphReader reader = new BinaryGraphReader(new DataInputStream(new BufferedInputStream(new FileInputStream(toulouse))));
        final Graph graph_toulouse = reader.read();
        //reader = new BinaryGraphReader(new DataInputStream(new BufferedInputStream(new FileInputStream(madagascar))));
        //final Graph graph_madagascar = reader.read();
        //reader = new BinaryGraphReader(new DataInputStream(new BufferedInputStream(new FileInputStream(midi_pyrenees))));
        //final Graph graph_midi_pyrenees = reader.read();

        
        // Create the drawing:
        final Drawing drawing = createDrawing();

        // Draw the graph on the drawing.
        drawing.drawGraph(graph_toulouse);
        
        testShortestAllRoads(15685, 3420, graph_toulouse,"Toulouse");
        testShortestCarsonly(15685 , 3420, graph_toulouse, "Toulouse");
        testFastestAllRoads(15685, 3420, graph_toulouse,"Toulouse");
        testFastestCarsOnly(15685 , 3420, graph_toulouse, "Toulouse");
        System.out.println("Exception expected: PathNotFoundException\n");
        try{testPathNotFound(11670, 36863,1, graph_toulouse, "Toulouse");
        }catch(PathNotFoundException e){
            System.out.println(e.getMessage());
        } 
        // Create a PathReader.
        //final PathReader pathReader = new BinaryPathReader(new DataInputStream(new BufferedInputStream(new FileInputStream(pathName))));

        // Read the path.
        //final Path path = pathReader.readPath(graph);
        
        //System.out.println(path.getLength());
        // Draw the path.
        //drawing.drawPath(path);

        reader.close();
        //pathReader.close();
    }



    public static void init(int orgin,int dest,int mode,Graph graph){
        data=new ShortestPathData(graph,graph.get(orgin),graph.get(dest),ArcInspectorFactory.getAllFilters().get(mode));
        dijstra_algo=new DijkstraAlgorithm(data);
        dijstra_solution=dijstra_algo.run();
        BMF_algo=new BellmanFordAlgorithm(data);
        BMF_solution=BMF_algo.run();
        Astar_algo=new AStarAlgorithm(data);
        Astar_solution=Astar_algo.run();
    }

    public static void testShortestAllRoads(int origin,int dest,Graph graph,String mapname){
        System.out.println("---- testShortestAllRoads-----------");
        init(origin, dest, 0, graph);
        System.out.println("Shortest path length from node " + origin + " to node " + dest + " in " + mapname + "\n with Dijkstra: " + dijstra_solution.getPath().getLength() 
        +"\n with BMF: " + BMF_solution.getPath().getLength() + "\n with A*: " + Astar_solution.getPath().getLength());
    }

    public static void testShortestCarsonly(int origin,int dest,Graph graph,String mapname){
        System.out.println("---- testShortestCarsOnly-----------");
        init(origin, dest, 1, graph);
        System.out.println("ShortestCarsOnly path length from node " + origin + " to node " + dest + " in " + mapname + "\n with Dijkstra: " + dijstra_solution.getPath().getLength() 
        +"\n with BMF: " + BMF_solution.getPath().getLength() + "\n with A*: " + Astar_solution.getPath().getLength());
    }

    public static void testFastestAllRoads(int origin,int dest,Graph graph,String mapname){
        System.out.println("---- testFastestAllRoads-----------");
        init(origin, dest, 2, graph);
        System.out.println("FastestAllRoads path length from node " + origin + " to node " + dest + " in " + mapname + "\n with Dijkstra: " + dijstra_solution.getPath().getLength() 
        +"\n with BMF: " + BMF_solution.getPath().getLength() + "\n with A*: " + Astar_solution.getPath().getLength());
    } 
    public static void testFastestCarsOnly(int origin,int dest,Graph graph,String mapname){
        System.out.println("---- testFastestCarsOnly-----------");
        init(origin, dest, 2, graph);
        System.out.println("FastestCarsOnly path length from node " + origin + " to node " + dest + " in " + mapname + "\n with Dijkstra: " + dijstra_solution.getPath().getLength() 
        +"\n with BMF: " + BMF_solution.getPath().getLength() + "\n with A*: " + Astar_solution.getPath().getLength());
    } 

    public static void testPathNotFound(int origin,int dest,int mode,Graph graph,String mapname) throws PathNotFoundException{
        System.out.println("---- testPathRoadsNotFound-----------");
        init(origin, dest, mode, graph);
        if(BMF_solution.getStatus()==Status.INFEASIBLE && BMF_solution.getStatus()==Status.INFEASIBLE && BMF_solution.getStatus()==Status.INFEASIBLE) 
       { throw new PathNotFoundException("PathNotFoundException");} 
    } 

   
   
}
