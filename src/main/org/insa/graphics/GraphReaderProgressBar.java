package org.insa.graphics;

import java.awt.Component;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.border.EmptyBorder;

import org.insa.graph.Arc;
import org.insa.graph.Node;
import org.insa.graph.RoadInformation;
import org.insa.graph.io.GraphReaderObserver;

/**
 * One-time use GraphReaderObserver that display progress in three different
 * JProgressBar.
 * 
 * @author Mikael
 *
 */
public class GraphReaderProgressBar extends JDialog implements GraphReaderObserver {

    /**
     * 
     */
    private static final long serialVersionUID = -1;

    // Index...
    private static final int NODE = 0, DESC = 1, ARC = 2;

    // Progress bar
    private final JProgressBar[] progressBars = new JProgressBar[3];

    // Current element read, and modulo.
    private int[] counters = new int[]{ 0, 0, 0 };
    private int[] modulos = new int[3];

    public GraphReaderProgressBar(JFrame owner) {
        super(owner);
        this.setVisible(false);
        setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);

        final String[] infos = { "nodes", "road informations", "arcs" };

        JPanel pane = new JPanel();
        pane.setLayout(new BoxLayout(pane, BoxLayout.PAGE_AXIS));
        pane.setBorder(new EmptyBorder(15, 15, 15, 15));
        pane.add(Box.createVerticalGlue());

        for (int i = 0; i < 3; ++i) {
            JLabel label = new JLabel("Reading " + infos[i] + "... ");
            label.setAlignmentX(Component.LEFT_ALIGNMENT);
            progressBars[i] = new JProgressBar();
            progressBars[i].setAlignmentX(Component.LEFT_ALIGNMENT);
            pane.add(label);
            pane.add(progressBars[i]);
        }

        pane.add(Box.createVerticalGlue());
        setContentPane(pane);
        pack();
    }

    @Override
    public void notifyStartReading(String mapId) {
        setTitle("Reading graph " + mapId + "... ");
        setVisible(true);
    }

    @Override
    public void notifyEndReading() {
        setVisible(false);
        dispose();
    }

    protected void initProgressBar(int index, int max) {
        progressBars[index].setMaximum(max);
        modulos[index] = Math.max(max / 100, 1);
    }

    protected void incCounter(int index) {
        counters[index] += 1;
        if (counters[index] % modulos[index] == 0) {
            progressBars[index].setValue(counters[index]);
        }
    }

    @Override
    public void notifyStartReadingNodes(int nNodes) {
        initProgressBar(NODE, nNodes);
    }

    @Override
    public void notifyNewNodeRead(Node node) {
        incCounter(NODE);
    }

    @Override
    public void notifyStartReadingDescriptors(int nDesc) {
        initProgressBar(DESC, nDesc);
    }

    @Override
    public void notifyNewDescriptorRead(RoadInformation desc) {
        incCounter(DESC);
    }

    @Override
    public void notifyStartReadingArcs(int nArcs) {
        initProgressBar(ARC, nArcs);
    }

    @Override
    public void notifyNewArcRead(Arc arc) {
        incCounter(ARC);
    }

}
