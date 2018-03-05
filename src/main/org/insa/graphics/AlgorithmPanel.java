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
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.EmptyBorder;

import org.insa.algo.AbstractAlgorithm;
import org.insa.algo.AbstractInputData;
import org.insa.algo.AbstractInputData.ArcFilter;
import org.insa.algo.AbstractInputData.Mode;
import org.insa.algo.AlgorithmFactory;
import org.insa.algo.ArcFilterFactory;
import org.insa.graph.Node;
import org.insa.graphics.NodesInputPanel.InputChangedEvent;

public class AlgorithmPanel extends JPanel {

    /**
     * 
     */
    private static final long serialVersionUID = 406148710808045035L;

    public class StartActionEvent extends ActionEvent {

        /**
         * 
         */
        private static final long serialVersionUID = 4090710269781229078L;

        protected static final String START_EVENT_COMMAND = "allInputFilled";

        protected static final int START_EVENT_ID = 0x1;

        private final List<Node> nodes;
        private final AbstractInputData.Mode mode;
        private final Class<? extends AbstractAlgorithm<?>> algoClass;

        private final AbstractInputData.ArcFilter arcFilter;

        private final boolean graphicVisualization;
        private final boolean textualVisualization;

        public StartActionEvent(Class<? extends AbstractAlgorithm<?>> algoClass, List<Node> nodes,
                Mode mode, ArcFilter arcFilter, boolean graphicVisualization,
                boolean textualVisualization) {
            super(AlgorithmPanel.this, START_EVENT_ID, START_EVENT_COMMAND);
            this.nodes = nodes;
            this.mode = mode;
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
         * @return Mode associated with this event.
         */
        public Mode getMode() {
            return this.mode;
        }

        /**
         * @return Arc filter associated with this event.
         */
        public ArcFilter getArcFilter() {
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

    private JButton startAlgoButton;

    // Start listeners
    List<ActionListener> startActionListeners = new ArrayList<>();

    /**
     */
    public AlgorithmPanel(Component parent, Class<? extends AbstractAlgorithm<?>> baseAlgorithm,
            String title, String[] nodeNames, boolean enableModeSelection,
            boolean enableArcFilterSelection) {
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

        JComboBox<ArcFilter> arcFilterSelect = new JComboBox<>(
                ArcFilterFactory.getAllFilters().toArray(new ArcFilter[0]));
        arcFilterSelect.setBackground(Color.WHITE);

        // Add mode selection
        JPanel modeAndObserverPanel = new JPanel();
        modeAndObserverPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        modeAndObserverPanel.setLayout(new GridBagLayout());
        JRadioButton lengthModeButton = new JRadioButton("Length");
        lengthModeButton.setSelected(true);
        JRadioButton timeModeButton = new JRadioButton("Time");
        ButtonGroup group = new ButtonGroup();
        group.add(lengthModeButton);
        group.add(timeModeButton);

        JCheckBox graphicObserver = new JCheckBox("Graphic");
        graphicObserver.setSelected(true);
        JCheckBox textObserver = new JCheckBox("Textual");

        GridBagConstraints c = new GridBagConstraints();

        c.fill = GridBagConstraints.HORIZONTAL;

        if (enableModeSelection) {
            c.gridx = 0;
            c.gridy = 0;
            c.weightx = 0;
            modeAndObserverPanel.add(new JLabel("Mode: "), c);
            c.gridx = 1;
            c.weightx = 1;
            modeAndObserverPanel.add(lengthModeButton, c);
            c.gridx = 2;
            c.weightx = 1;
            modeAndObserverPanel.add(timeModeButton, c);
        }

        c.gridy = 2;
        c.gridx = 0;
        c.weightx = 0;
        modeAndObserverPanel.add(new JLabel("Visualization: "), c);
        c.gridx = 1;
        c.weightx = 1;
        modeAndObserverPanel.add(graphicObserver, c);
        c.gridx = 2;
        c.weightx = 1;
        modeAndObserverPanel.add(textObserver, c);

        if (enableArcFilterSelection) {
            c.gridy = 1;
            c.gridx = 0;
            c.weightx = 0;
            modeAndObserverPanel.add(new JLabel("Restrictions: "), c);
            c.gridx = 1;
            c.gridwidth = 2;
            c.weightx = 1;
            modeAndObserverPanel.add(arcFilterSelect, c);
        }

        components.add(timeModeButton);
        components.add(lengthModeButton);
        components.add(arcFilterSelect);
        components.add(graphicObserver);
        components.add(textObserver);

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
                AbstractInputData.Mode mode = lengthModeButton.isSelected()
                        ? AbstractInputData.Mode.LENGTH
                        : AbstractInputData.Mode.TIME;

                for (ActionListener lis: startActionListeners) {
                    lis.actionPerformed(new StartActionEvent(
                            AlgorithmFactory.getAlgorithmClass(baseAlgorithm,
                                    (String) algoSelect.getSelectedItem()),
                            nodesInputPanel.getNodeForInputs(), mode,
                            (AbstractInputData.ArcFilter) arcFilterSelect.getSelectedItem(),
                            graphicObserver.isSelected(), textObserver.isSelected()));
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
     * @return
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
     * @return
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
     * @param nodeNames
     * @return
     */
    protected NodesInputPanel createNodesInputPanel(String[] nodeNames) {
        final Color[] nodeColors = { new Color(57, 172, 115), new Color(255, 77, 77),
                new Color(77, 77, 255), new Color(77, 255, 77) };
        NodesInputPanel panel = new NodesInputPanel();
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        for (int i = 0; i < nodeNames.length; ++i) {
            panel.addTextField(nodeNames[i] + ": ", nodeColors[i % nodeColors.length]);
        }
        panel.setEnabled(false);
        return panel;
    }

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
        enabled = enabled && allNotNull(this.nodesInputPanel.getNodeForInputs());
        startAlgoButton.setEnabled(enabled);
    }

    /**
     * Add a new start action listener to this class.
     * 
     * @param listener
     */
    public void addStartActionListener(ActionListener listener) {
        this.startActionListeners.add(listener);
    }

}
