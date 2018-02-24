package org.insa.graphics;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;

import javax.swing.BorderFactory;
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
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.insa.algo.shortestpath.BellmanFordAlgorithm;
import org.insa.algo.shortestpath.ShortestPathAlgorithm;
import org.insa.algo.shortestpath.ShortestPathData;
import org.insa.algo.shortestpath.ShortestPathData.Mode;
import org.insa.algo.shortestpath.ShortestPathGraphicObserver;
import org.insa.algo.shortestpath.ShortestPathSolution;
import org.insa.algo.weakconnectivity.WeaklyConnectedComponentGraphicObserver;
import org.insa.algo.weakconnectivity.WeaklyConnectedComponentTextObserver;
import org.insa.algo.weakconnectivity.WeaklyConnectedComponentsAlgorithm;
import org.insa.algo.weakconnectivity.WeaklyConnectedComponentsData;
import org.insa.graph.Graph;
import org.insa.graph.Node;
import org.insa.graph.Path;
import org.insa.graph.io.BinaryGraphReader;
import org.insa.graph.io.BinaryGraphReaderV2;
import org.insa.graph.io.BinaryPathReader;
import org.insa.graph.io.GraphReader;
import org.insa.graph.io.MapMismatchException;
import org.insa.graph.io.Openfile;
import org.insa.graphics.MultiPointsClickListener.CallableWithNodes;
import org.insa.graphics.drawing.BasicDrawing;
import org.insa.graphics.drawing.BlackAndWhiteGraphPalette;
import org.insa.graphics.drawing.Drawing;
import org.insa.graphics.drawing.MapViewDrawing;

public class MainWindow extends JFrame {

    /**
     * 
     */
    private static final long serialVersionUID = -527660583705140687L;

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

    // Current loaded path.
    // private Path currentPath;

    // Drawing and click adapter.
    protected Drawing drawing;

    // Main panel.
    private JSplitPane mainPanel;

    // List of item for the top menus.
    private JMenuItem openMapItem;

    // List of items that cannot be used without a graph
    private ArrayList<JMenuItem> graphLockItems = new ArrayList<JMenuItem>();

    // Label containing the map ID of the current graph.
    private JLabel mapIdPanel;

    // Thread information
    private Timer threadTimer;
    private JPanel threadPanel;

    // Log stream and print stream
    private StreamCapturer logStream;

    @SuppressWarnings("unused")
    private PrintStream printStream;

    // Current running thread
    private ThreadWrapper currentThread;

    // Multi point listener
    private MultiPointsClickListener clickAdapter = null;

    // Factory
    private BlockingActionFactory baf;

    public MainWindow() {
        super(WINDOW_TITLE);

        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setLayout(new BorderLayout());

        // Create drawing and action listeners...
        this.drawing = new BasicDrawing();

        this.clickAdapter = new MultiPointsClickListener(this);
        this.currentThread = new ThreadWrapper(this);
        this.baf = new BlockingActionFactory(this);
        this.baf.addAction(clickAdapter);
        this.baf.addAction(currentThread);

        // Click adapter
        addDrawingClickListeners();
        setJMenuBar(createMenuBar());

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                int confirmed = JOptionPane.showConfirmDialog(null, "Are you sure you want to close the application?",
                        "Exit Confirmation", JOptionPane.YES_NO_OPTION);

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

        mainPanel.setResizeWeight(0.8);
        mainPanel.setDividerSize(5);

        mainPanel.setBackground(Color.WHITE);
        mainPanel.setLeftComponent((Component) this.drawing);
        mainPanel.setRightComponent(new JScrollPane(infoPanel));
        this.add(mainPanel, BorderLayout.CENTER);

        // Top Panel
        this.add(createStatusBar(), BorderLayout.SOUTH);

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
    }

    private void launchShortestPathThread(ShortestPathAlgorithm spAlgorithm) {
        spAlgorithm.addObserver(new ShortestPathGraphicObserver(drawing));
        // algo.addObserver(new ShortestPathTextObserver(printStream));
        launchThread(new Runnable() {
            @Override
            public void run() {
                ShortestPathSolution solution = spAlgorithm.run();
                if (solution != null && solution.isFeasible()) {
                    drawing.drawPath(solution.getPath());
                }
            }
        });
    }

    private void addDrawingClickListeners() {
        drawing.addDrawingClickListener(this.clickAdapter);
    }

    private void updateDrawing(Class<? extends Drawing> newClass) {
        int oldLocation = mainPanel.getDividerLocation();
        drawing.clear();
        if (drawing == null || !newClass.isInstance(drawing)) {
            try {
                drawing = newClass.newInstance();
            }
            catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
            addDrawingClickListeners();
            mainPanel.setLeftComponent((Component) drawing);
            mainPanel.setDividerLocation(oldLocation);
        }
    }

    private JMenuBar createMenuBar() {

        // Open Map item...
        openMapItem = new JMenuItem("Open Map... ", KeyEvent.VK_O);
        openMapItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.ALT_MASK));
        openMapItem.addActionListener(baf.createBlockingAction(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser chooser = new JFileChooser();
                FileNameExtensionFilter filter = new FileNameExtensionFilter("Map & compressed map files", "map",
                        "map2", "mapgr", "map.gz");
                chooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
                chooser.setFileFilter(filter);
                if (chooser.showOpenDialog(MainWindow.this) == JFileChooser.APPROVE_OPTION) {
                    launchThread(new Runnable() {
                        @Override
                        public void run() {
                            String path = chooser.getSelectedFile().getAbsolutePath();
                            DataInputStream stream;
                            try {
                                stream = Openfile.open(path);
                            }
                            catch (IOException e1) {
                                JOptionPane.showMessageDialog(MainWindow.this, "Cannot open the selected file.");
                                return;
                            }
                            GraphReader reader;
                            if (path.endsWith(".map2") || path.endsWith("mapgr")) {
                                reader = new BinaryGraphReaderV2(stream);
                            }
                            else {
                                reader = new BinaryGraphReader(stream);
                            }
                            try {
                                graph = reader.read();
                            }
                            catch (Exception exception) {
                                JOptionPane.showMessageDialog(MainWindow.this,
                                        "Unable to read graph from the selected file.");
                                exception.printStackTrace(System.out);
                                return;
                            }
                            drawing.clear();
                            drawing.drawGraph(graph);

                            for (JMenuItem item: graphLockItems) {
                                item.setEnabled(true);
                            }
                            mapIdPanel.setText("Map ID: 0x" + Integer.toHexString(graph.getMapId()));
                        }
                    }, false);
                }
            }
        }));

        // Open Path item...
        JMenuItem openPathItem = new JMenuItem("Open Path... ", KeyEvent.VK_P);
        openPathItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, ActionEvent.ALT_MASK));
        openPathItem.addActionListener(baf.createBlockingAction(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser chooser = new JFileChooser();
                FileNameExtensionFilter filter = new FileNameExtensionFilter("Path & compressed path files", "path",
                        "path.gz");
                chooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
                chooser.setFileFilter(filter);
                if (chooser.showOpenDialog(MainWindow.this) == JFileChooser.APPROVE_OPTION) {
                    BinaryPathReader reader;
                    try {
                        reader = new BinaryPathReader(Openfile.open(chooser.getSelectedFile().getAbsolutePath()));
                    }
                    catch (IOException e1) {
                        JOptionPane.showMessageDialog(MainWindow.this, "Cannot open the selected file.");
                        return;
                    }
                    try {
                        Path path = reader.readPath(graph);
                        drawing.drawPath(path);
                    }
                    catch (MapMismatchException exception) {
                        JOptionPane.showMessageDialog(MainWindow.this,
                                "The selected file does not contain a path for the current graph.");
                        return;
                    }
                    catch (Exception exception) {
                        JOptionPane.showMessageDialog(MainWindow.this, "Unable to read path from the selected file.");
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
                MainWindow.this.dispatchEvent(new WindowEvent(MainWindow.this, WindowEvent.WINDOW_CLOSING));
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
                launchThread(new Runnable() {
                    @Override
                    public void run() {
                        updateDrawing(BasicDrawing.class);
                        drawing.drawGraph(graph);
                    }
                });
            }
        }));
        graphLockItems.add(drawGraphItem);
        JMenuItem drawGraphBWItem = new JMenuItem("Redraw (B&W)", KeyEvent.VK_B);
        drawGraphBWItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_B, ActionEvent.ALT_MASK));
        drawGraphBWItem.addActionListener(baf.createBlockingAction(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                launchThread(new Runnable() {
                    @Override
                    public void run() {
                        updateDrawing(BasicDrawing.class);
                        drawing.drawGraph(graph, new BlackAndWhiteGraphPalette());
                    }
                });
            }
        }));
        graphLockItems.add(drawGraphBWItem);
        JMenuItem drawGraphMapsforgeItem = new JMenuItem("Redraw (Map)", KeyEvent.VK_M);
        drawGraphMapsforgeItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_M, ActionEvent.ALT_MASK));
        drawGraphMapsforgeItem.addActionListener(baf.createBlockingAction(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                launchThread(new Runnable() {
                    @Override
                    public void run() {
                        updateDrawing(MapViewDrawing.class);
                        drawing.drawGraph(graph);
                    }
                });
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
                WeaklyConnectedComponentsData instance = new WeaklyConnectedComponentsData(graph);
                WeaklyConnectedComponentsAlgorithm algo = new WeaklyConnectedComponentsAlgorithm(instance);
                algo.addObserver(new WeaklyConnectedComponentGraphicObserver(drawing));
                algo.addObserver(new WeaklyConnectedComponentTextObserver(printStream));
                launchThread(new Runnable() {
                    @Override
                    public void run() {
                        algo.run();
                    }
                });
            }
        }));

        // Shortest path
        JMenuItem bellmanItem = new JMenuItem("Shortest Path (Bellman-Ford)");
        bellmanItem.addActionListener(baf.createBlockingAction(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int idx = JOptionPane.showOptionDialog(MainWindow.this, "Which mode do you want?", "Mode selection",
                        JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, Mode.values(), Mode.LENGTH);

                if (idx != -1) {
                    Mode mode = Mode.values()[idx];
                    clickAdapter.enable(2, new CallableWithNodes() {
                        @Override
                        public void call(ArrayList<Node> nodes) {
                            launchShortestPathThread(new BellmanFordAlgorithm(
                                    new ShortestPathData(graph, nodes.get(0), nodes.get(1), mode)));
                        }
                    });
                }
            }
        }));
        graphLockItems.add(wccItem);
        graphLockItems.add(bellmanItem);

        algoMenu.add(wccItem);
        algoMenu.addSeparator();
        algoMenu.add(bellmanItem);
        // algoMenu.add(djikstraItem);
        // algoMenu.add(aStarItem);

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
        statusPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.GRAY));
        statusPanel.setPreferredSize(new Dimension(getWidth(), 38));
        statusPanel.setLayout(new BorderLayout());

        mapIdPanel = new JLabel();
        mapIdPanel.setHorizontalAlignment(SwingConstants.LEFT);
        statusPanel.add(mapIdPanel, BorderLayout.WEST);

        JLabel threadInfo = new JLabel("Thread running... ");
        JLabel threadTimerLabel = new JLabel("00:00:00");
        JButton threadButton = new JButton("Stop");
        threadButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (currentThread.isRunning()) {
                    int confirmed = JOptionPane.showConfirmDialog(null,
                            "Are you sure you want to kill the running thread?", "Kill Confirmation",
                            JOptionPane.YES_NO_OPTION);
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
                threadTimerLabel
                        .setText(String.format("%02d:%02d:%02d", seconds / 3600, seconds / 60 % 60, seconds % 60));
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

        MainWindow w = new MainWindow();
        w.setExtendedState(JFrame.MAXIMIZED_BOTH);
        w.setVisible(true);
    }

}
