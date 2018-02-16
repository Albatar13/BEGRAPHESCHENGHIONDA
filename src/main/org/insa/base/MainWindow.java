package org.insa.base;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
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
import javax.swing.filechooser.FileNameExtensionFilter;

import org.insa.algo.weakconnectivity.WeaklyConnectedComponentGraphicObserver;
import org.insa.algo.weakconnectivity.WeaklyConnectedComponentTextObserver;
import org.insa.algo.weakconnectivity.WeaklyConnectedComponentsAlgorithm;
import org.insa.algo.weakconnectivity.WeaklyConnectedComponentsInstance;
import org.insa.drawing.DrawingVisible;
import org.insa.drawing.graph.BlackAndWhiteGraphPalette;
import org.insa.drawing.graph.GraphDrawing;
import org.insa.drawing.graph.PathDrawing;
import org.insa.graph.Graph;
import org.insa.graph.Path;
import org.insa.graph.io.BinaryGraphReader;
import org.insa.graph.io.BinaryPathReader;
import org.insa.graph.io.MapMismatchException;
import org.insa.graph.io.Openfile;

import com.sun.glass.events.KeyEvent;

public class MainWindow extends JFrame {
	
	public class JOutputStream extends OutputStream {
	    private JTextArea textArea;

	    public JOutputStream(JTextArea textArea) {
	        this.textArea = textArea;
	    }

	    @Override
	    public void write(int b) throws IOException {
	        // redirects data to the text area
	    		textArea.setText(textArea.getText() + String.valueOf((char)b));
	        // scrolls the text area to the end of data
	        textArea.setCaretPosition(textArea.getDocument().getLength());
	        // keeps the textArea up to date
	        textArea.update(textArea.getGraphics());
	    }
	}

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
	private static final Dimension DEFAULT_DIMENSION = new Dimension(800, 600);
	
	// Current graph.
	private Graph graph;
	
	// Current loaded path.
	private Path currentPath;
	
	// List of item for the top menus.
	private JMenuItem openMapItem;
	
	// List of items that cannot be used without a graph
	private ArrayList<JMenuItem> graphItems = new ArrayList<JMenuItem>();
	
	// Label containing the map ID of the current graph.
	private JLabel mapIdPanel;
	
	// Log stream and print stream
	private JOutputStream logStream;
	private PrintStream printStream;
	
	/**
	 * 
	 */
	private DrawingVisible drawing;
	
	public MainWindow() {
		super(WINDOW_TITLE);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setLayout(new BorderLayout());
		setSize(DEFAULT_DIMENSION);
		setJMenuBar(createMenuBar());
		
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				int confirmed = JOptionPane.showConfirmDialog(null, 
						"Are you sure you want to close the application?", "Exit Confirmation",
						JOptionPane.YES_NO_OPTION);

				if (confirmed == JOptionPane.YES_OPTION) {
					dispose();
					System.exit(0);
				}
			}
		});
		
		// Create graph area
		JSplitPane sp = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
				
		drawing = new DrawingVisible();
		drawing.setBackground(Color.WHITE);
		
		JTextArea infoPanel = new JTextArea();
		infoPanel.setMinimumSize(new Dimension(200, 50));
		// infoPanel.setBorder(BorderFactory.createMatteBorder(0, 1, 0, 0, Color.GRAY));
		infoPanel.setBackground(Color.WHITE);
		infoPanel.setLineWrap(true);
		infoPanel.setEditable(false);
		this.logStream = new JOutputStream(infoPanel);
		this.printStream = new PrintStream(this.logStream);
		
        sp.setResizeWeight(0.8);
        // sp.setEnabled(false);
        sp.setDividerSize(5);
        
        sp.setBackground(Color.WHITE);
		sp.add(drawing);
		sp.add(new JScrollPane(infoPanel));		
		this.add(sp, BorderLayout.CENTER);
				
		this.add(createStatusBar(), BorderLayout.SOUTH);
	}
	
	private JMenuBar createMenuBar() {

		// Open Map item...
		openMapItem = new JMenuItem("Open Map... ",
		                         KeyEvent.VK_O);
		openMapItem.setAccelerator(KeyStroke.getKeyStroke(
		        KeyEvent.VK_O, ActionEvent.ALT_MASK));
		openMapItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser();
			    FileNameExtensionFilter filter = new FileNameExtensionFilter(
			        "Map & compressed map files", "map", "map.gz");
			    chooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
			    chooser.setFileFilter(filter);
			    if (chooser.showOpenDialog(MainWindow.this) == JFileChooser.APPROVE_OPTION) {
			    		BinaryGraphReader reader;
					try {
						reader = new BinaryGraphReader(
								Openfile.open(chooser.getSelectedFile().getAbsolutePath()));
					} catch (IOException e1) {
						JOptionPane.showMessageDialog(MainWindow.this, "Cannot open the selected file.");
						return ;
					}
			    		try {
			    			graph = reader.read();
			    		}
			    		catch (Exception exception) {
						JOptionPane.showMessageDialog(MainWindow.this, "Unable to read graph from the selected file.");
						return ;
			    		}
			    		drawing.clear();
			    		new GraphDrawing(drawing).drawGraph(graph);
			    		
			    		for (JMenuItem item: graphItems) {
			    			item.setEnabled(true);
			    		}
			    		mapIdPanel.setText("Map ID: 0x" + Integer.toHexString(graph.getMapId()));
			    }
			}
		});
		
		// Open Path item...
		JMenuItem openPathItem = new JMenuItem("Open Path... ", KeyEvent.VK_P);
		openPathItem.setAccelerator(KeyStroke.getKeyStroke(
		        KeyEvent.VK_P, ActionEvent.ALT_MASK));
		openPathItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser();
			    FileNameExtensionFilter filter = new FileNameExtensionFilter(
			        "Path & compressed path files", "path", "path.gz");
			    chooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
			    chooser.setFileFilter(filter);
			    if (chooser.showOpenDialog(MainWindow.this) == JFileChooser.APPROVE_OPTION) {
			    		BinaryPathReader reader;
					try {
						reader = new BinaryPathReader(
								Openfile.open(chooser.getSelectedFile().getAbsolutePath()));
					} catch (IOException e1) {
						JOptionPane.showMessageDialog(MainWindow.this, "Cannot open the selected file.");
						return ;
					}
			    		try {
			    			currentPath = reader.readPath(graph);
			    		}
			    		catch (MapMismatchException exception) {
			    			JOptionPane.showMessageDialog(MainWindow.this, "The selected file does not contain a path for the current graph.");
			    			return;
			    		}
			    		catch (Exception exception) {
						JOptionPane.showMessageDialog(MainWindow.this, "Unable to read path from the selected file.");
						return ;
			    		}
			    		new PathDrawing(drawing).drawPath(currentPath);
			    }
			}
		});
		graphItems.add(openPathItem);
		
		// Close item
		JMenuItem closeItem = new JMenuItem("Quit", KeyEvent.VK_Q);
		closeItem.setAccelerator(KeyStroke.getKeyStroke(
				KeyEvent.VK_Q, ActionEvent.ALT_MASK));
		closeItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				MainWindow.this.dispatchEvent(new WindowEvent(MainWindow.this, WindowEvent.WINDOW_CLOSING));
			}
		});

		//Build the first menu.
		JMenu fileMenu = new JMenu("File");
		fileMenu.add(openMapItem);
		fileMenu.add(openPathItem);
		fileMenu.addSeparator();
		fileMenu.add(closeItem);
		
		// Second menu
		JMenuItem drawGraphItem = new JMenuItem("Redraw", KeyEvent.VK_R);
		drawGraphItem.setAccelerator(KeyStroke.getKeyStroke(
				KeyEvent.VK_R, ActionEvent.ALT_MASK));
		drawGraphItem.addActionListener(new ActionListener() {		
			@Override
			public void actionPerformed(ActionEvent e) {
				drawing.clear();
				drawing.setAutoRepaint(true);
				new GraphDrawing(drawing).drawGraph(graph);
				drawing.setAutoRepaint(false);
			}
		});
		graphItems.add(drawGraphItem);
		JMenuItem drawGraphBWItem = new JMenuItem("Redraw (B&W)", KeyEvent.VK_B);
		drawGraphBWItem.setAccelerator(KeyStroke.getKeyStroke(
				KeyEvent.VK_B, ActionEvent.ALT_MASK));
		drawGraphBWItem.addActionListener(new ActionListener() {		
			@Override
			public void actionPerformed(ActionEvent e) {
				drawing.clear();
				drawing.setAutoRepaint(true);
				new GraphDrawing(drawing, new BlackAndWhiteGraphPalette()).drawGraph(graph);
				drawing.setAutoRepaint(false);
			}
		});
		graphItems.add(drawGraphBWItem);
		
		JMenu graphMenu = new JMenu("Graph");
		graphMenu.add(drawGraphItem);
		graphMenu.add(drawGraphBWItem);
		
		// Algo menu
		JMenu algoMenu = new JMenu("Algorithms");
		
		JMenuItem wccItem = new JMenuItem("Weakly Connected Components");
		wccItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				WeaklyConnectedComponentsInstance instance = new WeaklyConnectedComponentsInstance(graph);
				WeaklyConnectedComponentsAlgorithm algo = new WeaklyConnectedComponentsAlgorithm(instance);
				algo.addObserver(new WeaklyConnectedComponentGraphicObserver(drawing));
				
				(new Thread(algo)).start();
			}
		});
		graphItems.add(wccItem);
		
		algoMenu.add(wccItem);
		
		// Create the menu bar.
		JMenuBar menuBar = new JMenuBar();
		
		menuBar.add(fileMenu);
		menuBar.add(graphMenu);
		menuBar.add(algoMenu);

		for (JMenuItem item: graphItems) {
			item.setEnabled(false);
		}
		
		return menuBar;
	}
	
	private JPanel createStatusBar() {
		// create the status bar panel and shove it down the bottom of the frame
		JPanel statusPanel = new JPanel();
		statusPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.GRAY));
		statusPanel.setPreferredSize(new Dimension(getWidth(), 20));
		statusPanel.setLayout(new BoxLayout(statusPanel, BoxLayout.X_AXIS));
		
		mapIdPanel = new JLabel();
		mapIdPanel.setHorizontalAlignment(SwingConstants.LEFT);
		statusPanel.add(mapIdPanel);

		return statusPanel;
	}
	
	public static void main(final String[] args) {
		MainWindow w = new MainWindow();
		w.setExtendedState(JFrame.MAXIMIZED_BOTH);
		w.setVisible(true);
	}
	

}
