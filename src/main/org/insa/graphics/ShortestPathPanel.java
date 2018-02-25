package org.insa.graphics;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.EmptyBorder;

import org.insa.algo.shortestpath.ShortestPathAlgorithm;
import org.insa.algo.shortestpath.ShortestPathAlgorithmFactory;
import org.insa.algo.shortestpath.ShortestPathData.Mode;
import org.insa.graph.Node;
import org.insa.graphics.NodesInputPanel.InputChangedEvent;

public class ShortestPathPanel extends JPanel {

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

        private final Node origin, destination;
        private final Mode mode;
        private final Class<? extends ShortestPathAlgorithm> algoClass;

        public StartActionEvent(Class<? extends ShortestPathAlgorithm> algoClass, Node origin, Node destination,
                Mode mode) {
            super(ShortestPathPanel.this, START_EVENT_ID, START_EVENT_COMMAND);
            this.origin = origin;
            this.destination = destination;
            this.mode = mode;
            this.algoClass = algoClass;
        }

        /**
         * @return Origin node associated with this event.
         */
        public Node getOrigin() {
            return this.origin;
        }

        /**
         * @return Destination node associated with this event.
         */
        public Node getDestination() {
            return this.destination;
        }

        /**
         * @return Mode associated with this event.
         */
        public Mode getMode() {
            return this.mode;
        }

        /**
         * @return Algorithm class associated with this event.
         */
        public Class<? extends ShortestPathAlgorithm> getAlgorithmClass() {
            return this.algoClass;
        }

    };

    // Input panels for node.
    protected NodesInputPanel nodesInputPanel;

    // Component that can be enabled/disabled.
    private ArrayList<JComponent> components = new ArrayList<>();

    // Start listeners
    List<ActionListener> startActionListeners = new ArrayList<>();

    public ShortestPathPanel(MainWindow mainWindow) {
        super();
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

        setBorder(new EmptyBorder(15, 15, 15, 15));

        // Set title.
        JLabel titleLabel = new JLabel("Shortest-Path");
        titleLabel.setBackground(Color.RED);
        titleLabel.setHorizontalAlignment(JLabel.LEFT);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        Font font = titleLabel.getFont();
        font = font.deriveFont(Font.BOLD, 18);
        titleLabel.setFont(font);
        add(titleLabel);

        add(Box.createVerticalStrut(8));

        // Add algorithm selection
        JComboBox<String> algoSelect = new JComboBox<>(
                ShortestPathAlgorithmFactory.getAlgorithmNames().toArray(new String[0]));
        algoSelect.setBackground(Color.WHITE);
        algoSelect.setAlignmentX(Component.LEFT_ALIGNMENT);
        add(algoSelect);
        components.add(algoSelect);

        // Add inputs for node.
        this.nodesInputPanel = new NodesInputPanel(mainWindow);
        this.nodesInputPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        nodesInputPanel.addTextField("Origin: ", new Color(57, 172, 115));
        nodesInputPanel.addTextField("Destination: ", new Color(255, 77, 77));

        add(this.nodesInputPanel);
        components.add(this.nodesInputPanel);

        // Add mode selection
        JPanel modePanel = new JPanel();
        modePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        modePanel.setLayout(new BoxLayout(modePanel, BoxLayout.LINE_AXIS));
        JRadioButton lengthModeButton = new JRadioButton("Length");
        lengthModeButton.setSelected(true);
        JRadioButton timeModeButton = new JRadioButton("Time");
        ButtonGroup group = new ButtonGroup();
        group.add(lengthModeButton);
        group.add(timeModeButton);
        modePanel.add(Box.createHorizontalGlue());
        modePanel.add(lengthModeButton);
        modePanel.add(Box.createHorizontalGlue());
        modePanel.add(timeModeButton);
        modePanel.add(Box.createHorizontalGlue());

        add(modePanel);
        components.add(timeModeButton);
        components.add(lengthModeButton);

        // Bottom panel
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.LINE_AXIS));
        JButton startAlgoButton = new JButton("Start");
        startAlgoButton.setEnabled(false);
        startAlgoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                List<Node> nodes = nodesInputPanel.getNodeForInputs();
                Node origin = nodes.get(0), destination = nodes.get(1);
                Mode mode = lengthModeButton.isSelected() ? Mode.LENGTH : Mode.TIME;

                for (ActionListener lis: startActionListeners) {
                    lis.actionPerformed(new StartActionEvent(
                            ShortestPathAlgorithmFactory.getAlgorithmClass((String) algoSelect.getSelectedItem()),
                            origin, destination, mode));
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

        components.add(startAlgoButton);
        components.add(hideButton);

        bottomPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        add(Box.createVerticalStrut(8));
        add(bottomPanel);

        nodesInputPanel.addInputChangedListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                InputChangedEvent evt = (InputChangedEvent) e;
                boolean allNotNull = true;
                for (Node node: evt.getNodes()) {
                    if (node == null) {
                        allNotNull = false;
                    }
                }
                startAlgoButton.setEnabled(allNotNull);
            }
        });
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        nodesInputPanel.setEnabled(enabled);
        for (JComponent component: components) {
            component.setEnabled(enabled);
        }
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
