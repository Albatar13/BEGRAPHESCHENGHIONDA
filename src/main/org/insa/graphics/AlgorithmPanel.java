package org.insa.graphics;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.insa.algo.AbstractAlgorithm;
import org.insa.algo.AlgorithmFactory;
import org.insa.algo.ArcInspector;
import org.insa.algo.ArcInspectorFactory;
import org.insa.graph.Node;
import org.insa.graphics.NodesInputPanel.InputChangedEvent;
import org.insa.graphics.drawing.Drawing;
import org.insa.graphics.drawing.components.MapViewDrawing;
import org.insa.graphics.utils.ColorUtils;

public class AlgorithmPanel extends JPanel implements DrawingChangeListener {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public class StartActionEvent extends ActionEvent {

        /**
         * 
         */
        private static final long serialVersionUID = 4090710269781229078L;

        protected static final String START_EVENT_COMMAND = "allInputFilled";

        protected static final int START_EVENT_ID = 0x1;

        private final List<Node> nodes;
        private final Class<? extends AbstractAlgorithm<?>> algoClass;

        private final ArcInspector arcFilter;

        private final boolean graphicVisualization;
        private final boolean textualVisualization;

        public StartActionEvent(Class<? extends AbstractAlgorithm<?>> algoClass, List<Node> nodes,
                ArcInspector arcFilter, boolean graphicVisualization,
                boolean textualVisualization) {
            super(AlgorithmPanel.this, START_EVENT_ID, START_EVENT_COMMAND);
            this.nodes = nodes;
            this.algoClass = algoClass;
            this.graphicVisualization = graphicVisualization;
            this.textualVisualization = textualVisualization;
            this.arcFilter = arcFilter;
        }

        /**
         * @return Nodes associated with this event.
         */
        public List<Node> getNodes() {
            return this.nodes;
        }

        /**
         * @return Arc filter associated with this event.
         */
        public ArcInspector getArcFilter() {
            return this.arcFilter;
        }

        /**
         * @return Algorithm class associated with this event.
         */
        public Class<? extends AbstractAlgorithm<?>> getAlgorithmClass() {
            return this.algoClass;
        }

        /**
         * @return true if graphic visualization is enabled.
         */
        public boolean isGraphicVisualizationEnabled() {
            return this.graphicVisualization;
        }

        /**
         * @return true if textual visualization is enabled.
         */
        public boolean isTextualVisualizationEnabled() {
            return this.textualVisualization;
        }

    };

    // Input panels for node.
    protected NodesInputPanel nodesInputPanel;

    // Solution
    protected SolutionPanel solutionPanel;

    // Component that can be enabled/disabled.
    private ArrayList<JComponent> components = new ArrayList<>();

    // Graphic / Text checkbox observer
    private final JCheckBox graphicObserverCheckbox, textualObserverCheckbox;

    // Drawing
    private Drawing drawing = null;

    private JButton startAlgoButton;

    // Start listeners
    List<ActionListener> startActionListeners = new ArrayList<>();

    /**
     * Create a new AlgorithmPanel with the given parameters.
     * 
     * @param parent Parent component for this panel. Only use for centering
     *        dialogs.
     * @param baseAlgorithm Base algorithm for this algorithm panel.
     * @param title Title of the panel.
     * @param nodeNames Names of the input nodes.
     * @param enableArcFilterSelection <code>true</code> to enable
     *        {@link ArcInspector} selection.
     * 
     * @see ArcInspectorFactory
     */
    public AlgorithmPanel(Component parent, Class<? extends AbstractAlgorithm<?>> baseAlgorithm,
            String title, String[] nodeNames, boolean enableArcFilterSelection) {
        super();
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

        setBorder(new EmptyBorder(15, 15, 15, 15));

        // Set title.
        add(createTitleLabel(title));
        add(Box.createVerticalStrut(8));

        // Add algorithm selection
        JComboBox<String> algoSelect = createAlgoritmSelectComboBox(baseAlgorithm);
        if (algoSelect.getItemCount() > 1) {
            add(algoSelect);
            components.add(algoSelect);
        }

        // Add inputs for node.
        this.nodesInputPanel = createNodesInputPanel(nodeNames);
        add(this.nodesInputPanel);
        components.add(this.nodesInputPanel);

        JComboBox<ArcInspector> arcFilterSelect = new JComboBox<>(
                ArcInspectorFactory.getAllFilters().toArray(new ArcInspector[0]));
        arcFilterSelect.setBackground(Color.WHITE);

        // Add mode selection
        JPanel modeAndObserverPanel = new JPanel();
        modeAndObserverPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        modeAndObserverPanel.setLayout(new GridBagLayout());

        graphicObserverCheckbox = new JCheckBox("Graphic");
        graphicObserverCheckbox.setSelected(true);
        textualObserverCheckbox = new JCheckBox("Textual");

        GridBagConstraints c = new GridBagConstraints();

        c.fill = GridBagConstraints.HORIZONTAL;

        c.gridy = 2;
        c.gridx = 0;
        c.weightx = 0;
        modeAndObserverPanel.add(new JLabel("Visualization: "), c);
        c.gridx = 1;
        c.weightx = 1;
        modeAndObserverPanel.add(graphicObserverCheckbox, c);
        c.gridx = 2;
        c.weightx = 1;
        modeAndObserverPanel.add(textualObserverCheckbox, c);

        if (enableArcFilterSelection) {
            c.gridy = 1;
            c.gridx = 0;
            c.weightx = 0;
            modeAndObserverPanel.add(new JLabel("Mode: "), c);
            c.gridx = 1;
            c.gridwidth = 2;
            c.weightx = 1;
            modeAndObserverPanel.add(arcFilterSelect, c);
        }

        components.add(arcFilterSelect);
        components.add(textualObserverCheckbox);

        add(modeAndObserverPanel);

        solutionPanel = new SolutionPanel(parent);
        solutionPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        solutionPanel.setVisible(false);
        add(Box.createVerticalStrut(10));
        add(solutionPanel);

        // Bottom panel
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.LINE_AXIS));

        startAlgoButton = new JButton("Start");
        startAlgoButton.setEnabled(false);
        startAlgoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (ActionListener lis: startActionListeners) {
                    lis.actionPerformed(new StartActionEvent(
                            AlgorithmFactory.getAlgorithmClass(baseAlgorithm,
                                    (String) algoSelect.getSelectedItem()),
                            nodesInputPanel.getNodeForInputs(),
                            (ArcInspector) arcFilterSelect.getSelectedItem(),
                            graphicObserverCheckbox.isSelected(),
                            textualObserverCheckbox.isSelected()));
                }
            }
        });

        JButton hideButton = new JButton("Hide");
        hideButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                nodesInputPanel.setEnabled(false);
                setVisible(false);
            }
        });

        bottomPanel.add(startAlgoButton);
        bottomPanel.add(Box.createHorizontalGlue());
        bottomPanel.add(hideButton);

        components.add(hideButton);

        bottomPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        add(Box.createVerticalStrut(8));
        add(bottomPanel);

        nodesInputPanel.addInputChangedListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                InputChangedEvent evt = (InputChangedEvent) e;
                startAlgoButton.setEnabled(allNotNull(evt.getNodes()));
            }
        });

        addComponentListener(new ComponentAdapter() {

            @Override
            public void componentShown(ComponentEvent e) {
                setEnabled(true);
                nodesInputPanel.setVisible(true);
            }

            @Override
            public void componentHidden(ComponentEvent e) {
                setEnabled(false);
                nodesInputPanel.setVisible(false);
            }

        });

        setEnabled(false);
    }

    /**
     * Create the title JLabel for this panel.
     * 
     * @param title Title for the label.
     * 
     * @return A new JLabel containing the given title with proper font.
     */
    protected JLabel createTitleLabel(String title) {
        JLabel titleLabel = new JLabel(title);
        titleLabel.setBackground(Color.RED);
        titleLabel.setHorizontalAlignment(JLabel.LEFT);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        Font font = titleLabel.getFont();
        font = font.deriveFont(Font.BOLD, 18);
        titleLabel.setFont(font);
        return titleLabel;
    }

    /**
     * Create the combo box for the algorithm selection.
     * 
     * @param baseAlgorithm Base algorithm for which the select box should be
     *        created.
     * 
     * @return A new JComboBox containing algorithms for the given base algorithm.
     * 
     * @see AlgorithmFactory
     */
    protected JComboBox<String> createAlgoritmSelectComboBox(
            Class<? extends AbstractAlgorithm<?>> baseAlgorithm) {
        JComboBox<String> algoSelect = new JComboBox<>(
                AlgorithmFactory.getAlgorithmNames(baseAlgorithm).toArray(new String[0]));
        algoSelect.setBackground(Color.WHITE);
        algoSelect.setAlignmentX(Component.LEFT_ALIGNMENT);
        return algoSelect;

    }

    /**
     * Create a node input panel with the given node input names.
     * 
     * @param nodeNames Field names for the inputs to create.
     * 
     * @return A new NodesInputPanel containing inputs for the given names.
     */
    protected NodesInputPanel createNodesInputPanel(String[] nodeNames) {
        NodesInputPanel panel = new NodesInputPanel();
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        for (int i = 0; i < nodeNames.length; ++i) {
            panel.addTextField(nodeNames[i] + ": ", ColorUtils.getColor(i));
        }
        panel.setEnabled(false);
        return panel;
    }

    /**
     * Check if the given list of nodes does not contain any <code>null</code>
     * value.
     * 
     * @param nodes List of {@link Node} to check.
     * 
     * @return <code>true</code> if the list does not contain any <code>null</code>
     *         value, <code>false</code> otherwise.
     */
    protected boolean allNotNull(List<Node> nodes) {
        boolean allNotNull = true;
        for (Node node: nodes) {
            allNotNull = allNotNull && node != null;
        }
        return allNotNull;
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        nodesInputPanel.setEnabled(enabled);
        solutionPanel.setEnabled(enabled);
        for (JComponent component: components) {
            component.setEnabled(enabled);
        }
        graphicObserverCheckbox.setEnabled(enabled && !(drawing instanceof MapViewDrawing));
        enabled = enabled && allNotNull(this.nodesInputPanel.getNodeForInputs());
        startAlgoButton.setEnabled(enabled);
    }

    /**
     * Add a new start action listener to this class.
     * 
     * @param listener Listener to add.
     */
    public void addStartActionListener(ActionListener listener) {
        this.startActionListeners.add(listener);
    }

    @Override
    public void onDrawingLoaded(Drawing oldDrawing, Drawing newDrawing) {
        if (newDrawing instanceof MapViewDrawing) {
            graphicObserverCheckbox.setSelected(false);
            graphicObserverCheckbox.setEnabled(false);
        }
        else {
            graphicObserverCheckbox.setSelected(true);
            graphicObserverCheckbox.setEnabled(true);
        }
        this.drawing = newDrawing;
    }

    @Override
    public void onRedrawRequest() {
    }

}
