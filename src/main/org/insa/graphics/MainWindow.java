package org.insa.graphics;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;

import org.insa.algo.AbstractSolution;
import org.insa.algo.AlgorithmFactory;
import org.insa.algo.carpooling.CarPoolingAlgorithm;
import org.insa.algo.packageswitch.PackageSwitchAlgorithm;
import org.insa.algo.shortestpath.ShortestPathAlgorithm;
import org.insa.algo.shortestpath.ShortestPathData;
import org.insa.algo.shortestpath.ShortestPathGraphicObserver;
import org.insa.algo.shortestpath.ShortestPathSolution;
import org.insa.algo.shortestpath.ShortestPathTextObserver;
import org.insa.algo.weakconnectivity.WeaklyConnectedComponentGraphicObserver;
import org.insa.algo.weakconnectivity.WeaklyConnectedComponentTextObserver;
import org.insa.algo.weakconnectivity.WeaklyConnectedComponentsAlgorithm;
import org.insa.algo.weakconnectivity.WeaklyConnectedComponentsData;
import org.insa.graph.Graph;
import org.insa.graph.Path;
import org.insa.graph.io.BinaryGraphReader;
import org.insa.graph.io.BinaryPathReader;
import org.insa.graph.io.GraphReader;
import org.insa.graph.io.MapMismatchException;
import org.insa.graphics.AlgorithmPanel.StartActionEvent;
import org.insa.graphics.drawing.BasicGraphPalette;
import org.insa.graphics.drawing.BlackAndWhiteGraphPalette;
import org.insa.graphics.drawing.Drawing;
import org.insa.graphics.drawing.GraphPalette;
import org.insa.graphics.drawing.components.BasicDrawing;
import org.insa.graphics.drawing.components.MapViewDrawing;
import org.insa.graphics.utils.FileUtils;
import org.insa.graphics.utils.FileUtils.FolderType;

public class MainWindow extends JFrame {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * 
     */
    private static final String WINDOW_TITLE = "BE Graphes INSA";

    /**
     * 
     */
    private static final int THREAD_TIMER_DELAY = 1000; // in milliseconds

    // Current graph.
    protected Graph graph;

    // Path to the last opened graph file.
    private String graphFilePath;

    // Drawing and click adapter.
    protected Drawing drawing;
    private final MapViewDrawing mapViewDrawing;
    private final BasicDrawing basicDrawing;

    private final GraphPalette basicPalette, blackAndWhitePalette;
    private GraphPalette currentPalette;

    // Main panel.
    private final JSplitPane mainPanel;

    // Algorithm panels
    private final List<AlgorithmPanel> algoPanels = new ArrayList<>();
    private final AlgorithmPanel wccPanel, spPanel, cpPanel, psPanel;

    // Path panel
    private final PathsPanel pathPanel;

    // List of items that cannot be used without a graph
    private final ArrayList<JMenuItem> graphLockItems = new ArrayList<JMenuItem>();

    // Label containing the map ID of the current graph.
    private JLabel graphInfoPanel;

    // Thread information
    private Timer threadTimer;
    private JPanel threadPanel;

    // Log stream and print stream
    private StreamCapturer logStream;

    private PrintStream printStream;

    // Current running thread
    private ThreadWrapper currentThread;

    // Factory
    private BlockingActionFactory baf;

    // Observers
    private List<DrawingChangeListener> drawingChangeListeners = new ArrayList<>();
    private List<GraphChangeListener> graphChangeListeneres = new ArrayList<>();

    public MainWindow() {
        super(WINDOW_TITLE);

        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setLayout(new BorderLayout());

        setMinimumSize(new Dimension(800, 600));

        // Create drawing and action listeners...
        this.basicDrawing = new BasicDrawing();
        this.mapViewDrawing = new MapViewDrawing();
        this.drawing = basicDrawing;

        // Createa palettes
        this.basicPalette = new BasicGraphPalette();
        this.blackAndWhitePalette = new BlackAndWhiteGraphPalette();
        this.currentPalette = this.basicPalette;

        wccPanel = new AlgorithmPanel(this, WeaklyConnectedComponentsAlgorithm.class,
                "Weakly-Connected Components", new String[] {}, false);
        wccPanel.addStartActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                StartActionEvent evt = (StartActionEvent) e;
                WeaklyConnectedComponentsData data = new WeaklyConnectedComponentsData(graph);

                WeaklyConnectedComponentsAlgorithm wccAlgorithm = null;
                try {
                    wccAlgorithm = (WeaklyConnectedComponentsAlgorithm) AlgorithmFactory
                            .createAlgorithm(evt.getAlgorithmClass(), data);
                }
                catch (Exception e1) {
                    JOptionPane.showMessageDialog(MainWindow.this,
                            "An error occurred while creating the specified algorithm.",
                            "Internal error: Algorithm instantiation failure",
                            JOptionPane.ERROR_MESSAGE);
                    e1.printStackTrace();
                    return;
                }

                wccPanel.setEnabled(false);

                if (evt.isGraphicVisualizationEnabled()) {
                    wccAlgorithm.addObserver(new WeaklyConnectedComponentGraphicObserver(drawing));
                }
                if (evt.isTextualVisualizationEnabled()) {
                    wccAlgorithm.addObserver(new WeaklyConnectedComponentTextObserver(printStream));
                }

                // We love Java...
                final WeaklyConnectedComponentsAlgorithm copyAlgorithm = wccAlgorithm;
                launchThread(new Runnable() {
                    @Override
                    public void run() {
                        AbstractSolution solution = copyAlgorithm.run();
                        wccPanel.solutionPanel.addSolution(solution, false);
                        wccPanel.solutionPanel.setVisible(true);
                        wccPanel.setEnabled(true);
                    }
                });
            }
        });

        spPanel = new AlgorithmPanel(this, ShortestPathAlgorithm.class, "Shortest-Path",
                new String[] { "Origin", "Destination" }, true);
        spPanel.addStartActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                StartActionEvent evt = (StartActionEvent) e;
                ShortestPathData data = new ShortestPathData(graph, evt.getNodes().get(0),
                        evt.getNodes().get(1), evt.getArcFilter());

                ShortestPathAlgorithm spAlgorithm = null;
                try {
                    spAlgorithm = (ShortestPathAlgorithm) AlgorithmFactory
                            .createAlgorithm(evt.getAlgorithmClass(), data);
                }
                catch (Exception e1) {
                    JOptionPane.showMessageDialog(MainWindow.this,
                            "An error occurred while creating the specified algorithm.",
                            "Internal error: Algorithm instantiation failure",
                            JOptionPane.ERROR_MESSAGE);
                    e1.printStackTrace();
                    return;
                }

                spPanel.setEnabled(false);

                if (evt.isGraphicVisualizationEnabled()) {
                    spAlgorithm.addObserver(new ShortestPathGraphicObserver(drawing));
                }
                if (evt.isTextualVisualizationEnabled()) {
                    spAlgorithm.addObserver(new ShortestPathTextObserver(printStream));
                }

                final ShortestPathAlgorithm copyAlgorithm = spAlgorithm;
                launchThread(new Runnable() {
                    @Override
                    public void run() {
                        // Run the algorithm.
                        ShortestPathSolution solution = copyAlgorithm.run();
                        // Add the solution to the solution panel (but do not display
                        // overlay).
                        spPanel.solutionPanel.addSolution(solution, false);
                        // If the solution is feasible, add the path to the path panel.
                        if (solution.isFeasible()) {
                            pathPanel.addPath(solution.getPath());
                        }
                        // Show the solution panel and enable the shortest-path panel.
                        spPanel.solutionPanel.setVisible(true);
                        spPanel.setEnabled(true);
                    }
                });
            }
        });

        cpPanel = new AlgorithmPanel(
                this, CarPoolingAlgorithm.class, "Car-Pooling", new String[] { "Origin Car",
                        "Origin Pedestrian", "Destination Car", "Destination Pedestrian" },
                true);

        psPanel = new AlgorithmPanel(this, PackageSwitchAlgorithm.class, "Car-Pooling",
                new String[] { "Oribin A", "Origin B", "Destination A", "Destination B" }, true);

        // add algorithm panels
        algoPanels.add(wccPanel);
        algoPanels.add(spPanel);
        algoPanels.add(cpPanel);
        algoPanels.add(psPanel);

        this.pathPanel = new PathsPanel(this);

        // Add click listeners to both drawing.

        for (AlgorithmPanel panel: algoPanels) {
            this.basicDrawing.addDrawingClickListener(panel.nodesInputPanel);
            this.mapViewDrawing.addDrawingClickListener(panel.nodesInputPanel);
            this.graphChangeListeneres.add(panel.nodesInputPanel);
            this.graphChangeListeneres.add(panel.solutionPanel);
            this.drawingChangeListeners.add(panel.nodesInputPanel);
            this.drawingChangeListeners.add(panel.solutionPanel);
            this.drawingChangeListeners.add(panel);
        }

        this.graphChangeListeneres.add(pathPanel);
        this.drawingChangeListeners.add(pathPanel);

        // Create action factory.
        this.currentThread = new ThreadWrapper(this);
        this.baf = new BlockingActionFactory(this);
        this.baf.addAction(currentThread);

        // Click adapter
        ActionListener openMapActionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser chooser = FileUtils.createFileChooser(FolderType.Map);
                if (chooser.showOpenDialog(MainWindow.this) == JFileChooser.APPROVE_OPTION) {
                    graphFilePath = chooser.getSelectedFile().getAbsolutePath();
                    DataInputStream stream;
                    try {
                        stream = new DataInputStream(new BufferedInputStream(
                                new FileInputStream(chooser.getSelectedFile())));
                    }
                    catch (IOException e1) {
                        JOptionPane.showMessageDialog(MainWindow.this,
                                "Cannot open the selected file.");
                        return;
                    }
                    loadGraph(new BinaryGraphReader(stream));
                }
            }
        };

        setJMenuBar(createMenuBar(openMapActionListener));

        // Initial panel to show "Open Map... "
        JPanel openPanel = new JPanel();
        openPanel.setLayout(new BoxLayout(openPanel, BoxLayout.PAGE_AXIS));
        JButton openButton = new JButton("Open Map... ");
        openButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        openButton.addActionListener(openMapActionListener);
        openButton.setFocusPainted(false);
        openPanel.add(Box.createVerticalGlue());
        openPanel.add(openButton);
        openPanel.add(Box.createVerticalGlue());

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                int confirmed = JOptionPane.showConfirmDialog(MainWindow.this,
                        "Are you sure you want to close the application?", "Exit Confirmation",
                        JOptionPane.YES_NO_OPTION);

                if (confirmed == JOptionPane.YES_OPTION) {
                    dispose();
                    System.exit(0);
                }
            }
        });

        // Create graph area
        mainPanel = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);

        JTextArea infoPanel = new JTextArea();
        infoPanel.setMinimumSize(new Dimension(200, 50));
        infoPanel.setBackground(Color.WHITE);
        infoPanel.setLineWrap(true);
        infoPanel.setEditable(false);
        this.logStream = new StreamCapturer(infoPanel);
        this.printStream = new PrintStream(this.logStream);

        JPanel rightComponent = new JPanel();
        rightComponent.setLayout(new GridBagLayout());

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        rightComponent.add(pathPanel, c);

        c.gridy = 1;
        for (AlgorithmPanel panel: algoPanels) {
            panel.setVisible(false);
            rightComponent.add(panel, c);
        }

        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 2;
        c.weightx = 1;
        c.weighty = 1;
        c.fill = GridBagConstraints.BOTH;
        c.gridheight = GridBagConstraints.REMAINDER;
        rightComponent.add(new JScrollPane(infoPanel), c);

        mainPanel.setResizeWeight(0.8);
        mainPanel.setDividerSize(5);

        mainPanel.setBackground(Color.WHITE);
        mainPanel.setLeftComponent(openPanel);
        mainPanel.setRightComponent(rightComponent);
        this.add(mainPanel, BorderLayout.CENTER);

        // Top Panel
        this.add(createStatusBar(), BorderLayout.SOUTH);

        // Notify everythin
        notifyDrawingLoaded(null, drawing);
    }

    /**
     * @param runnable
     * @param canInterrupt
     */
    private void launchThread(Runnable runnable, boolean canInterrupt) {
        if (canInterrupt) {
            currentThread.setThread(new Thread(new Runnable() {
                @Override
                public void run() {
                    threadTimer.restart();
                    threadPanel.setVisible(true);
                    runnable.run();
                    clearCurrentThread();
                }
            }));
        }
        else {
            currentThread.setThread(new Thread(runnable));
        }
        currentThread.startThread();
    }

    private void launchThread(Runnable runnable) {
        launchThread(runnable, true);
    }

    protected void clearCurrentThread() {
        threadTimer.stop();
        threadPanel.setVisible(false);
        currentThread.setThread(null);
        if (spPanel.isVisible()) {
            spPanel.setEnabled(true);
        }
    }

    /**
     * Notify all listeners that a new graph has been loaded.
     */
    private void notifyNewGraphLoaded() {
        for (GraphChangeListener listener: graphChangeListeneres) {
            listener.newGraphLoaded(graph);
        }
    }

    /**
     * Notify all listeners that a new drawing has been set up.
     * 
     * @param oldDrawing
     * @param newDrawing
     */
    private void notifyDrawingLoaded(Drawing oldDrawing, Drawing newDrawing) {
        for (DrawingChangeListener listener: drawingChangeListeners) {
            listener.onDrawingLoaded(oldDrawing, newDrawing);
        }
    }

    /**
     * Notify all listeners that a redraw request is emitted.
     */
    private void notifyRedrawRequest() {
        for (DrawingChangeListener listener: drawingChangeListeners) {
            listener.onRedrawRequest();
        }
    }

    /**
     * Draw the stored graph on the drawing.
     */
    private void drawGraph(Class<? extends Drawing> newClass, GraphPalette palette) {

        // Save old divider location
        int oldLocation = mainPanel.getDividerLocation();

        // Set drawing if not set
        if (!(mainPanel.getLeftComponent() instanceof Drawing)) {
            mainPanel.setLeftComponent((Component) this.drawing);
            mainPanel.setDividerLocation(oldLocation);
            // Need to re-validate or the drawing will not have the
            // correct size prior to drawing, which can cause issue.
            this.revalidate();
        }

        boolean isNewGraph = newClass == null;
        boolean isMapView = (isNewGraph && drawing == mapViewDrawing)
                || (!isNewGraph && newClass.equals(MapViewDrawing.class));

        // We need to draw MapView, we have to check if the file exists.
        File mfile = null;
        if (isMapView) {
            String mfpath = graphFilePath.substring(0, graphFilePath.lastIndexOf(".map"))
                    + ".mapfg";
            mfile = new File(mfpath);
            if (!mfile.exists()) {
                if (JOptionPane.showConfirmDialog(this,
                        "The associated mapsforge (.mapfg) file has not been found, do you want to specify it manually?",
                        "File not found",
                        JOptionPane.YES_NO_CANCEL_OPTION) == JOptionPane.YES_OPTION) {
                    JFileChooser chooser = new JFileChooser(mfile.getParentFile());
                    if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                        mfile = chooser.getSelectedFile();
                    }
                    else {
                        mfile = null;
                    }
                }
                else {
                    mfile = null;
                }
            }
        }

        Runnable runnable = null;

        if (isMapView && mfile != null) {
            final File mfileFinal = mfile;
            // It is a mapview drawing and the file was found, so:
            // 1. We create the drawing if necessary.
            if (drawing != mapViewDrawing) {
                drawing.clear();
                drawing = mapViewDrawing;
                mainPanel.setLeftComponent(mapViewDrawing);
                mainPanel.setDividerLocation(oldLocation);
                notifyDrawingLoaded(basicDrawing, mapViewDrawing);
                drawing.clear();
                isNewGraph = true;
                mainPanel.revalidate();
            }
            if (isNewGraph) {
                drawing.clear();
                runnable = new Runnable() {
                    public void run() {
                        ((MapViewDrawing) drawing).drawGraph(mfileFinal);
                        notifyRedrawRequest();
                    }
                };
            }

        }
        else if (!isMapView || (isMapView && mfile == null && isNewGraph)) {
            if (drawing == mapViewDrawing) {
                mapViewDrawing.clear();
                drawing = basicDrawing;
                mainPanel.setLeftComponent(basicDrawing);
                mainPanel.setDividerLocation(oldLocation);
                notifyDrawingLoaded(mapViewDrawing, basicDrawing);
                isNewGraph = true;
            }
            if (isNewGraph || palette != this.currentPalette) {
                this.currentPalette = palette;
                drawing.clear();
                runnable = new Runnable() {
                    public void run() {
                        drawing.drawGraph(graph, palette);
                        notifyRedrawRequest();
                    }
                };
            }
        }

        if (runnable != null) {
            launchThread(runnable, false);
        }
        else {
            drawing.clearOverlays();
            notifyRedrawRequest();
        }

    }

    /**
     * @param newClass
     */
    private void drawGraph(Class<? extends Drawing> newClass) {
        drawGraph(newClass, new BasicGraphPalette());
    }

    /**
     * 
     */
    private void drawGraph() {
        drawGraph(null, this.currentPalette);
    }

    private void loadGraph(GraphReader reader) {
        launchThread(new Runnable() {
            @Override
            public void run() {
                GraphReaderProgressBar progressBar = new GraphReaderProgressBar(MainWindow.this);
                progressBar.setLocationRelativeTo(mainPanel.getLeftComponent());
                reader.addObserver(progressBar);
                try {
                    graph = reader.read();
                    System.out.flush();
                }
                catch (Exception exception) {
                    progressBar.setVisible(false);
                    progressBar = null;
                    JOptionPane.showMessageDialog(MainWindow.this,
                            "Unable to read graph from the selected file.");
                    exception.printStackTrace(System.out);
                    return;
                }

                // In case of....
                progressBar.setVisible(false);
                progressBar = null;

                String info = graph.getMapId();
                if (graph.getMapName() != null && !graph.getMapName().isEmpty()) {
                    // The \u200e character is the left-to-right mark, we need to avoid issue with
                    // name that are right-to-left (e.g. arabic names).
                    info += " - " + graph.getMapName() + "\u200e";
                }
                info += ", " + graph.size() + " nodes, " + graph.getGraphInformation().getArcCount()
                        + " arcs.";
                graphInfoPanel.setText(info);

                drawGraph();

                notifyNewGraphLoaded();

                for (JMenuItem item: graphLockItems) {
                    item.setEnabled(true);
                }
            }
        }, false);
    }

    /**
     * Show and enable the given AlgorithmPanel (and hide all others).
     * 
     * @param algorithmPanel
     */
    private void enableAlgorithmPanel(AlgorithmPanel algorithmPanel) {
        int dividerLocation = mainPanel.getDividerLocation();
        for (AlgorithmPanel panel: algoPanels) {
            panel.setVisible(panel == algorithmPanel);
        }
        mainPanel.setDividerLocation(dividerLocation);
    }

    private JMenuBar createMenuBar(ActionListener openMapActionListener) {

        // Open Map item...
        JMenuItem openMapItem = new JMenuItem("Open Map... ", KeyEvent.VK_O);
        openMapItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.ALT_MASK));
        openMapItem.addActionListener(baf.createBlockingAction(openMapActionListener));

        // Open Path item...
        JMenuItem openPathItem = new JMenuItem("Open Path... ", KeyEvent.VK_P);
        openPathItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, ActionEvent.ALT_MASK));
        openPathItem.addActionListener(baf.createBlockingAction(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser chooser = FileUtils.createFileChooser(FolderType.PathInput);
                if (chooser.showOpenDialog(MainWindow.this) == JFileChooser.APPROVE_OPTION) {
                    BinaryPathReader reader;
                    try {
                        reader = new BinaryPathReader(new DataInputStream(new BufferedInputStream(
                                new FileInputStream(chooser.getSelectedFile()))));
                    }
                    catch (IOException e1) {
                        JOptionPane.showMessageDialog(MainWindow.this,
                                "Cannot open the selected file.");
                        return;
                    }
                    try {
                        Path path = reader.readPath(graph);
                        pathPanel.addPath(path);
                    }
                    catch (MapMismatchException exception) {
                        JOptionPane.showMessageDialog(MainWindow.this,
                                "The selected file does not contain a path for the current graph.");
                        return;
                    }
                    catch (Exception exception) {
                        JOptionPane.showMessageDialog(MainWindow.this,
                                "Unable to read path from the selected file.");
                        return;
                    }
                }
            }
        }));
        graphLockItems.add(openPathItem);

        // Close item
        JMenuItem closeItem = new JMenuItem("Quit", KeyEvent.VK_Q);
        closeItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, ActionEvent.ALT_MASK));
        closeItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MainWindow.this.dispatchEvent(
                        new WindowEvent(MainWindow.this, WindowEvent.WINDOW_CLOSING));
            }
        });

        // Build the first menu.
        JMenu fileMenu = new JMenu("File");
        fileMenu.add(openMapItem);
        fileMenu.add(openPathItem);
        fileMenu.addSeparator();
        fileMenu.add(closeItem);

        // Second menu
        JMenuItem drawGraphItem = new JMenuItem("Redraw", KeyEvent.VK_R);
        drawGraphItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, ActionEvent.ALT_MASK));
        drawGraphItem.addActionListener(baf.createBlockingAction(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                drawGraph(BasicDrawing.class, basicPalette);
            }
        }));
        graphLockItems.add(drawGraphItem);
        JMenuItem drawGraphBWItem = new JMenuItem("Redraw (B&W)", KeyEvent.VK_B);
        drawGraphBWItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_B, ActionEvent.ALT_MASK));
        drawGraphBWItem.addActionListener(baf.createBlockingAction(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                drawGraph(BasicDrawing.class, blackAndWhitePalette);
            }
        }));
        graphLockItems.add(drawGraphBWItem);
        JMenuItem drawGraphMapsforgeItem = new JMenuItem("Redraw (Map)", KeyEvent.VK_M);
        drawGraphMapsforgeItem
                .setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_M, ActionEvent.ALT_MASK));
        drawGraphMapsforgeItem.addActionListener(baf.createBlockingAction(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                drawGraph(MapViewDrawing.class);
            }
        }));
        graphLockItems.add(drawGraphMapsforgeItem);

        JMenu graphMenu = new JMenu("Graph");
        graphMenu.add(drawGraphItem);
        graphMenu.add(drawGraphBWItem);
        graphMenu.addSeparator();
        graphMenu.add(drawGraphMapsforgeItem);

        // Algo menu
        JMenu algoMenu = new JMenu("Algorithms");

        // Weakly connected components
        JMenuItem wccItem = new JMenuItem("Weakly Connected Components");
        wccItem.addActionListener(baf.createBlockingAction(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                enableAlgorithmPanel(wccPanel);
            }
        }));

        // Shortest path
        JMenuItem spItem = new JMenuItem("Shortest-Path");
        spItem.addActionListener(baf.createBlockingAction(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                enableAlgorithmPanel(spPanel);
            }
        }));

        // Car pooling
        JMenuItem cpItem = new JMenuItem("Car Pooling");
        cpItem.addActionListener(baf.createBlockingAction(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                enableAlgorithmPanel(cpPanel);
            }
        }));

        // Car pooling
        JMenuItem psItem = new JMenuItem("Package Switch");
        psItem.addActionListener(baf.createBlockingAction(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                enableAlgorithmPanel(psPanel);
            }
        }));

        graphLockItems.add(wccItem);
        graphLockItems.add(spItem);
        graphLockItems.add(cpItem);
        graphLockItems.add(psItem);

        algoMenu.add(wccItem);
        algoMenu.addSeparator();
        algoMenu.add(spItem);
        algoMenu.add(cpItem);
        algoMenu.add(psItem);

        // Create the menu bar.
        JMenuBar menuBar = new JMenuBar();

        menuBar.add(fileMenu);
        menuBar.add(graphMenu);
        menuBar.add(algoMenu);

        for (JMenuItem item: graphLockItems) {
            item.setEnabled(false);
        }

        return menuBar;
    }

    private JPanel createStatusBar() {
        // create the status bar panel and shove it down the bottom of the frame
        JPanel statusPanel = new JPanel();
        statusPanel.setBorder(
                new CompoundBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.GRAY),
                        new EmptyBorder(0, 15, 0, 15)));
        statusPanel.setPreferredSize(new Dimension(getWidth(), 38));
        statusPanel.setLayout(new BorderLayout());

        graphInfoPanel = new JLabel();
        graphInfoPanel.setHorizontalAlignment(SwingConstants.LEFT);
        statusPanel.add(graphInfoPanel, BorderLayout.WEST);

        JLabel threadInfo = new JLabel("Thread running... ");
        JLabel threadTimerLabel = new JLabel("00:00:00");
        JButton threadButton = new JButton("Stop");
        threadButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (currentThread.isRunning()) {
                    int confirmed = JOptionPane.showConfirmDialog(MainWindow.this,
                            "Are you sure you want to kill the running thread?",
                            "Kill Confirmation", JOptionPane.YES_NO_OPTION);
                    if (confirmed == JOptionPane.YES_OPTION) {
                        currentThread.interrupt();
                    }
                }
            }
        });

        threadTimer = new Timer(THREAD_TIMER_DELAY, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                long seconds = currentThread.getDuration().getSeconds();
                threadTimerLabel.setText(String.format("%02d:%02d:%02d", seconds / 3600,
                        seconds / 60 % 60, seconds % 60));
            }
        });
        threadTimer.setInitialDelay(0);

        threadPanel = new JPanel();
        threadPanel.add(threadInfo);
        threadPanel.add(threadTimerLabel);
        threadPanel.add(threadButton);
        threadPanel.setVisible(false);
        statusPanel.add(threadPanel, BorderLayout.EAST);

        return statusPanel;
    }

    public static void main(final String[] args) {

        // Try to set system look and feel.
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch (Exception e) {
        }

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                MainWindow w = new MainWindow();
                w.setExtendedState(JFrame.MAXIMIZED_BOTH);
                w.setVisible(true);
            }
        });
    }

}
