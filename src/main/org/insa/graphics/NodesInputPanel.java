package org.insa.graphics;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.insa.graph.Graph;
import org.insa.graph.Node;
import org.insa.graph.Point;
import org.insa.graphics.drawing.Drawing;
import org.insa.graphics.drawing.Drawing.AlphaMode;
import org.insa.graphics.drawing.DrawingClickListener;
import org.insa.graphics.drawing.overlays.MarkerOverlay;

public class NodesInputPanel extends JPanel
        implements DrawingClickListener, DrawingChangeListener, GraphChangeListener {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private static final Color DEFAULT_MARKER_COLOR = Color.BLUE;

    /**
     * Utility class that can be used to find a node from coordinates in a "fast"
     * way.
     *
     */
    private static class NodeFinder {

        // Graph associated with this node finder.
        private Graph graph;

        /**
         * @param graph
         */
        public NodeFinder(Graph graph) {
            this.graph = graph;
        }

        /**
         * @param point
         * 
         * @return the closest node to the given point, or null if no node is "close
         *         enough".
         */
        public Node findClosestNode(Point point) {
            Node minNode = null;
            double minDis = Double.POSITIVE_INFINITY;
            for (Node node: graph) {
                double dlon = point.getLongitude() - node.getPoint().getLongitude();
                double dlat = point.getLatitude() - node.getPoint().getLatitude();
                double dis = dlon * dlon + dlat * dlat; // No need to square
                if (dis < minDis) {
                    minNode = node;
                    minDis = dis;
                }
            }
            return minNode;
        }

    }

    /**
     * Event data send when a node input has changed.
     *
     */
    public class InputChangedEvent extends ActionEvent {

        /**
         * 
         */
        private static final long serialVersionUID = 3440024811352247731L;

        protected static final String ALL_INPUT_FILLED_EVENT_COMMAND = "allInputFilled";

        protected static final int ALL_INPUT_FILLED_EVENT_ID = 0x1;

        // List of nodes
        List<Node> nodes;

        public InputChangedEvent(List<Node> nodes2) {
            super(NodesInputPanel.this, ALL_INPUT_FILLED_EVENT_ID, ALL_INPUT_FILLED_EVENT_COMMAND);
            this.nodes = nodes2;
        }

        List<Node> getNodes() {
            return Collections.unmodifiableList(nodes);
        }

    };

    // Node inputs and markers.
    private final ArrayList<JTextField> nodeInputs = new ArrayList<>();
    private final Map<JTextField, MarkerOverlay> markerTrackers = new IdentityHashMap<JTextField, MarkerOverlay>();

    // Component that can be enabled/disabled.
    private ArrayList<JComponent> components = new ArrayList<>();
    private int inputToFillIndex;

    // ActionListener called when all inputs are filled.
    private ArrayList<ActionListener> inputChangeListeners = new ArrayList<>();

    // Drawing and graph
    private Drawing drawing;
    private Graph graph;
    private NodeFinder nodeFinder;

    /**
     * Create a new NodesInputPanel.
     * 
     */
    public NodesInputPanel() {
        super(new GridBagLayout());
        initInputToFill();
    }

    /**
     * Add an InputChanged listener to this panel. This listener will be notified by
     * a {@link InputChangedEvent} each time an input in this panel change (click,
     * clear, manual input).
     * 
     * @param listener Listener to add.
     * 
     * @see InputChangedEvent
     */
    public void addInputChangedListener(ActionListener listener) {
        inputChangeListeners.add(listener);
    }

    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);
        for (JTextField input: nodeInputs) {
            MarkerOverlay marker = markerTrackers.getOrDefault(input, null);
            if (marker != null) {
                marker.setVisible(visible && !input.getText().trim().isEmpty());
            }
        }
    }

    @Override
    public void setEnabled(boolean enabled) {
        for (JComponent component: components) {
            component.setEnabled(enabled);
        }
        super.setEnabled(enabled);
        if (enabled) {
            // Enable: Check if there is an input to fill, otherwize find the next one.
            if (getInputToFill() == null) {
                nextInputToFill();
            }
        }
        else {
            // Disable, next input to fill = -1.
            this.inputToFillIndex = -1;
        }
    }

    public void clear() {
        for (JTextField field: nodeInputs) {
            field.setText("");
            markerTrackers.put(field, null);
        }
        initInputToFill();
    }

    public void addTextField(String label) {
        addTextField(label, DEFAULT_MARKER_COLOR);
    }

    public void addTextField(String label, Color markerColor) {

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(3, 3, 3, 3);

        JLabel jLabel = new JLabel(label);
        jLabel.setFont(jLabel.getFont().deriveFont(~Font.BOLD));
        JTextField textField = new JTextField();
        jLabel.setLabelFor(textField);

        c.gridx = 0;
        c.gridy = nodeInputs.size();
        c.weightx = 0;
        c.gridwidth = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        add(jLabel, c);

        c.gridx = 1;
        c.gridy = nodeInputs.size();
        c.weightx = 1;
        c.gridwidth = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        add(textField, c);

        JButton clearButton = new JButton("Clear");
        c.gridx = 2;
        c.gridy = nodeInputs.size();
        c.weightx = 0;
        c.gridwidth = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        add(clearButton, c);

        JButton clickButton = new JButton("Click");
        c.gridx = 3;
        c.gridy = nodeInputs.size();
        c.weightx = 0;
        c.gridwidth = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        add(clickButton, c);

        // Did not find something easier that this... ?
        textField.getDocument().addDocumentListener(new DocumentListener() {

            @Override
            public void changedUpdate(DocumentEvent e) {
                insertUpdate(e);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                insertUpdate(e);
            }

            @Override
            public void insertUpdate(DocumentEvent e) {

                // Draw marker if possible
                Node curnode = getNodeForInput(textField);
                MarkerOverlay tracker = markerTrackers.getOrDefault(textField, null);
                if (curnode != null) {
                    if (tracker == null) {
                        tracker = drawing.drawMarker(curnode.getPoint(), markerColor, Color.BLACK,
                                AlphaMode.TRANSPARENT);
                        markerTrackers.put(textField, tracker);
                    }
                    else {
                        tracker.moveTo(curnode.getPoint());
                    }
                    tracker.setVisible(true);
                }
                else if (tracker != null) {
                    tracker.setVisible(false);
                    if (getInputToFill() == null) {
                        nextInputToFill();
                    }
                }

                // Create array of nodes
                List<Node> nodes = getNodeForInputs();

                // Trigger change event.
                for (ActionListener lis: inputChangeListeners) {
                    lis.actionPerformed(new InputChangedEvent(nodes));
                }
            }
        });

        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                textField.setText("");
                setInputToFill(textField);
            }
        });

        clickButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setInputToFill(textField);
            }
        });

        nodeInputs.add(textField);
        components.add(textField);
        components.add(clearButton);
        components.add(clickButton);
    }

    /**
     * @return Current graph associated with this input panel.
     */
    protected Graph getGraph() {
        return this.graph;
    }

    /**
     * @return The node for the given text field, or null if the content of the text
     *         field is invalid.
     */
    protected Node getNodeForInput(JTextField textfield) {
        try {
            Node node = graph.get(Integer.valueOf(textfield.getText().trim()));
            return node;
        }
        catch (IllegalArgumentException | IndexOutOfBoundsException ex) {
            return null;
        }
    }

    /**
     * @return List of nodes associated with the input. Some nodes may be null if
     *         their associated input is invalid.
     */
    public List<Node> getNodeForInputs() {
        List<Node> nodes = new ArrayList<>(nodeInputs.size());
        for (JTextField input: nodeInputs) {
            nodes.add(getNodeForInput(input));
        }
        return nodes;
    }

    /**
     * Get the next input that should be filled by a click, or null if none should
     * be filled.
     * 
     * @return
     */
    protected JTextField getInputToFill() {
        if (inputToFillIndex < 0 || inputToFillIndex >= nodeInputs.size()) {
            return null;
        }
        return nodeInputs.get(inputToFillIndex);
    }

    /**
     * Initialize the next input to fill.
     */
    protected void initInputToFill() {
        inputToFillIndex = 0;
    }

    /**
     * Set the next input to fill to the given text field.
     * 
     * @param input
     */
    protected void setInputToFill(JTextField input) {
        inputToFillIndex = nodeInputs.indexOf(input);
    }

    /**
     * Find the next input to fill, if any.
     */
    protected void nextInputToFill() {
        boolean found = false;
        if (inputToFillIndex == -1) {
            inputToFillIndex = 0;
        }
        for (int i = 0; i < nodeInputs.size() && !found; ++i) {
            int nextIndex = (i + inputToFillIndex) % nodeInputs.size();
            JTextField input = nodeInputs.get(nextIndex);
            if (input.getText().trim().isEmpty()) {
                inputToFillIndex = nextIndex;
                found = true;
            }
        }
        if (!found) {
            inputToFillIndex = -1;
        }
    }

    @Override
    public void mouseClicked(Point point) {
        JTextField input = getInputToFill();
        if (input != null) {
            Node node = nodeFinder.findClosestNode(point);
            input.setText(String.valueOf(node.getId()));
            nextInputToFill();
        }
    }

    @Override
    public void newGraphLoaded(Graph graph) {
        if (graph != this.graph) {
            this.clear();
            this.graph = graph;

            nodeFinder = new NodeFinder(graph);

            // Disable if previously disabled...
            setEnabled(this.isEnabled());
        }
    }

    @Override
    public void onDrawingLoaded(Drawing oldDrawing, Drawing newDrawing) {
        if (newDrawing != drawing) {
            this.drawing = newDrawing;
        }
    }

    @Override
    public void onRedrawRequest() {
        for (JTextField input: nodeInputs) {
            MarkerOverlay tracker = markerTrackers.getOrDefault(input, null);
            if (tracker != null) {
                MarkerOverlay newMarker = this.drawing.drawMarker(tracker.getPoint(),
                        tracker.getColor(), Color.BLACK, AlphaMode.TRANSPARENT);
                markerTrackers.put(input, newMarker);
                newMarker.setVisible(tracker.isVisible());
                tracker.delete();
            }
        }
    }
}
