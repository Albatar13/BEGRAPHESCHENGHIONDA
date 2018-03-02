package org.insa.graphics;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;

import org.insa.algo.shortestpath.ShortestPathData;
import org.insa.algo.shortestpath.ShortestPathData.Mode;
import org.insa.algo.shortestpath.ShortestPathSolution;
import org.insa.graph.Graph;
import org.insa.graph.io.BinaryPathWriter;
import org.insa.graphics.drawing.Drawing;
import org.insa.graphics.drawing.overlays.PathOverlay;

public class ShortestPathSolutionPanel extends JPanel
        implements DrawingChangeListener, GraphChangeListener {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private class ShortestPathBundle {

        // Solution
        private final ShortestPathSolution solution;

        // Path Overlay (not final due to redraw)
        private PathOverlay overlay = null;

        /**
         * Create a new bundle with the given solution and create a new overlay
         * corresponding to the solution (if the solution is feasible).
         * 
         * @param solution Solution for this bundle, must not be null.
         * 
         */
        public ShortestPathBundle(ShortestPathSolution solution) {
            this.solution = solution;
            if (this.solution.isFeasible()) {
                this.overlay = drawing.drawPath(this.solution.getPath());
            }
        }

        /**
         * @return Solution associated with this bundle.
         */
        public ShortestPathSolution getSolution() {
            return this.solution;
        }

        /**
         * @return Data assocaited with this bundle.
         */
        public ShortestPathData getData() {
            return this.solution.getInputData();
        }

        /**
         * @return Overlay associated with this bundle, or null.
         */
        public PathOverlay getOverlay() {
            return this.overlay;
        }

        /**
         * Re-draw the current overlay (if any) on the new drawing.
         * 
         */
        public void updateOverlay(Drawing newDrawing) {
            if (this.overlay != null) {
                PathOverlay oldOverlay = this.overlay;
                this.overlay = newDrawing.drawPath(this.solution.getPath());
                this.overlay.setVisible(oldOverlay.isVisible());
                oldOverlay.delete();
            }
        }

        /*
         * (non-Javadoc)
         * @see java.lang.Object#toString()
         */
        public String toString() {
            return "Shortest-path from #" + this.getData().getOrigin().getId() + " to #"
                    + this.getData().getDestination().getId() + " ["
                    + this.getData().getMode().toString().toLowerCase() + "]";
        }
    }

    // Solution
    private Drawing drawing;

    // Solution selector
    private final JComboBox<ShortestPathBundle> solutionSelect;

    // Map solution -> panel
    private final JLabel informationPanel;

    // Current bundle
    private ShortestPathBundle currentBundle = null;

    public ShortestPathSolutionPanel(Component parent, Drawing drawing) {
        super();
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        setBorder(new CompoundBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.BLACK),
                new EmptyBorder(15, 15, 15, 15)));

        this.drawing = drawing;

        solutionSelect = new JComboBox<>();
        solutionSelect.setBackground(Color.WHITE);
        solutionSelect.setAlignmentX(Component.LEFT_ALIGNMENT);
        add(solutionSelect);

        informationPanel = new JLabel();
        informationPanel.setAlignmentX(JLabel.LEFT_ALIGNMENT);
        informationPanel.setHorizontalAlignment(JLabel.LEFT);
        add(informationPanel);

        JButton clearButton = new JButton("Hide");
        clearButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                PathOverlay overlay = currentBundle.getOverlay();
                if (overlay == null) {
                    return;
                }
                if (overlay.isVisible()) {
                    overlay.setVisible(false);
                    clearButton.setText("Show");
                }
                else {
                    overlay.setVisible(true);
                    clearButton.setText("Hide");
                }
            }
        });

        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String filepath = System.getProperty("user.dir");
                filepath += File.separator + String.format("path_%#x_%d_%d.path",
                        currentBundle.getData().getGraph().getMapId(),
                        currentBundle.getData().getOrigin().getId(),
                        currentBundle.getData().getDestination().getId());
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setSelectedFile(new File(filepath));
                fileChooser.setApproveButtonText("Save");

                if (fileChooser.showOpenDialog(parent) == JFileChooser.APPROVE_OPTION) {
                    File file = fileChooser.getSelectedFile();
                    try {
                        BinaryPathWriter writer = new BinaryPathWriter(new DataOutputStream(
                                new BufferedOutputStream(new FileOutputStream(file))));
                        writer.writePath(currentBundle.getSolution().getPath());
                    }
                    catch (IOException e1) {
                        JOptionPane.showMessageDialog(parent,
                                "Unable to write path to the selected file.");
                        e1.printStackTrace();
                    }
                }
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.LINE_AXIS));
        buttonPanel.add(Box.createHorizontalGlue());
        buttonPanel.add(clearButton);
        buttonPanel.add(saveButton);
        buttonPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        add(buttonPanel);

        solutionSelect.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                ShortestPathBundle bundle = (ShortestPathBundle) solutionSelect.getSelectedItem();

                // Handle case when the JComboBox is empty.
                if (bundle == null) {
                    return;
                }

                if (currentBundle != null && currentBundle.getOverlay() != null) {
                    currentBundle.getOverlay().setVisible(false);
                }

                updateInformationLabel(bundle);
                buttonPanel.setVisible(bundle.getSolution().isFeasible());
                clearButton.setText(bundle.getSolution().isFeasible() ? "Hide" : "Show");

                if (bundle.getOverlay() != null) {
                    bundle.getOverlay().setVisible(true);
                }

                currentBundle = bundle;
            }
        });

    }

    public void addSolution(ShortestPathSolution solution) {
        ShortestPathBundle bundle = new ShortestPathBundle(solution);
        solutionSelect.addItem(bundle);
        solutionSelect.setSelectedItem(bundle);
    }

    protected void updateInformationLabel(ShortestPathBundle bundle) {
        ShortestPathData data = bundle.getData();
        String info = null;
        if (!bundle.getSolution().isFeasible()) {
            info = String.format("Shortest path: No path found from node #%d to node #%d.",
                    data.getOrigin().getId(), data.getDestination().getId());
        }
        else {
            info = String.format("Shortest path: Found a path from node #%d to node #%d",
                    data.getOrigin().getId(), data.getDestination().getId());
            if (data.getMode() == Mode.LENGTH) {
                info = String.format("%s, %.2f kilometers.", info,
                        (bundle.getSolution().getPath().getLength() / 1000.0));
            }
            else {
                info = String.format("%s, %.2f minutes.", info,
                        (bundle.getSolution().getPath().getMinimumTravelTime() / 60.0));
            }
        }
        informationPanel.setText(info);
    }

    @Override
    public void newGraphLoaded(Graph graph) {
        this.solutionSelect.removeAllItems();
        this.currentBundle = null;
        this.setVisible(false);
    }

    @Override
    public void onDrawingLoaded(Drawing oldDrawing, Drawing newDrawing) {
        if (newDrawing != drawing) {
            drawing = newDrawing;
        }
    }

    @Override
    public void onRedrawRequest() {
        for (int i = 0; i < this.solutionSelect.getItemCount(); ++i) {
            this.solutionSelect.getItemAt(i).updateOverlay(drawing);
        }
    }

}
