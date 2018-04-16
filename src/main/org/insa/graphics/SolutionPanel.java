package org.insa.graphics;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;

import org.insa.algo.AbstractInputData;
import org.insa.algo.AbstractSolution;
import org.insa.graph.Graph;
import org.insa.graph.Path;
import org.insa.graphics.drawing.Drawing;
import org.insa.graphics.drawing.overlays.PathOverlay;

public class SolutionPanel extends JPanel implements DrawingChangeListener, GraphChangeListener {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private class SolutionBundle {

        // Solution
        private final AbstractSolution solution;

        // Path Overlay (not final due to redraw)
        private List<PathOverlay> overlays = new ArrayList<>();

        /**
         * Create a new bundle with the given solution and create a new overlay
         * corresponding to the solution (if the solution is feasible).
         * 
         * @param solution Solution for this bundle, must not be null.
         * 
         */
        public SolutionBundle(AbstractSolution solution, boolean createOverlays) {
            this.solution = solution;
            if (createOverlays) {
                this.overlays = createOverlaysFromSolution();
            }
        }

        /**
         * @return Solution associated with this bundle.
         */
        public AbstractSolution getSolution() {
            return this.solution;
        }

        /**
         * @return Data assocaited with this bundle.
         */
        public AbstractInputData getData() {
            return this.solution.getInputData();
        }

        /**
         * @return Overlays associated with this bundle, or null.
         */
        public List<PathOverlay> getOverlays() {
            return this.overlays;
        }

        /**
         * @return true if this bundle has overlays.
         */
        public boolean hasOverlays() {
            return !this.overlays.isEmpty();
        }

        /**
         * Re-draw the current overlay (if any) on the new drawing.
         * 
         */
        public void updateOverlays() {
            if (this.overlays.isEmpty()) {
                return; // This bundle has no overlay.
            }
            List<PathOverlay> oldOverlays = this.overlays;
            this.overlays = createOverlaysFromSolution();
            for (int i = 0; i < oldOverlays.size(); ++i) {
                oldOverlays.get(i).delete();
            }
        }

        private List<PathOverlay> createOverlaysFromSolution() {
            List<PathOverlay> overlays = new ArrayList<>();
            if (solution.isFeasible()) {
                Method[] methods = this.solution.getClass().getDeclaredMethods();
                for (Method method: methods) {
                    if (method.getReturnType().equals(Path.class)
                            && method.getParameterCount() == 0) {
                        try {
                            Path path = (Path) method.invoke(this.solution);
                            overlays.add(drawing.drawPath(path));
                        }
                        catch (IllegalAccessException | IllegalArgumentException
                                | InvocationTargetException e) {
                            // This has been check before, so should never happen...
                            e.printStackTrace();
                        }
                    }
                }
            }
            return overlays;
        }

        /*
         * (non-Javadoc)
         * @see java.lang.Object#toString()
         */
        public String toString() {
            return getData().toString();
        }

    }

    // Solution
    private Drawing drawing;

    // Solution selector
    private final JComboBox<SolutionBundle> solutionSelect;

    private final JLabel informationPanel;

    // Current bundle
    private SolutionBundle currentBundle = null;

    public SolutionPanel(Component parent) {
        super();
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        setBorder(new CompoundBorder(BorderFactory.createMatteBorder(1, 0, 1, 0, Color.LIGHT_GRAY),
                new EmptyBorder(10, 0, 10, 0)));

        solutionSelect = new JComboBox<>();
        solutionSelect.setBackground(Color.WHITE);
        solutionSelect.setAlignmentX(Component.LEFT_ALIGNMENT);
        add(solutionSelect);

        informationPanel = new JLabel();
        informationPanel.setOpaque(true);
        informationPanel.setFocusable(false);
        // informationPanel.setAlignmentX(JLabel.LEFT_ALIGNMENT);
        informationPanel.setHorizontalAlignment(JLabel.LEFT);

        add(Box.createVerticalStrut(8));
        add(informationPanel);

        JButton clearButton = new JButton("Hide");
        clearButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                for (PathOverlay overlay: currentBundle.getOverlays()) {
                    if (overlay.isVisible()) {
                        overlay.setVisible(false);
                        clearButton.setText("Show");
                    }
                    else {
                        overlay.setVisible(true);
                        clearButton.setText("Hide");
                    }
                }
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.LINE_AXIS));
        buttonPanel.add(Box.createHorizontalGlue());
        buttonPanel.add(clearButton);
        buttonPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        add(Box.createVerticalStrut(4));
        add(buttonPanel);

        solutionSelect.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                if (currentBundle != null) {
                    for (PathOverlay overlay: currentBundle.getOverlays()) {
                        overlay.setVisible(false);
                    }
                }

                SolutionBundle bundle = (SolutionBundle) solutionSelect.getSelectedItem();

                if (bundle != null) {

                    updateInformationLabel(bundle);
                    buttonPanel
                            .setVisible(bundle.getSolution().isFeasible() && bundle.hasOverlays());
                    clearButton.setText(bundle.getSolution().isFeasible() ? "Hide" : "Show");

                    for (PathOverlay overlay: bundle.getOverlays()) {
                        overlay.setVisible(true);
                    }
                }

                currentBundle = bundle;
            }
        });

    }

    public void addSolution(AbstractSolution solution) {
        addSolution(solution, true);
    }

    /**
     * Add the given solution to the panel.
     * 
     * @param solution the solution to add to the panel
     * @param createOverlays Whether or not overlay should be created for this
     *        solution.
     */
    public void addSolution(AbstractSolution solution, boolean createOverlays) {
        SolutionBundle bundle = new SolutionBundle(solution, createOverlays);
        solutionSelect.addItem(bundle);
        solutionSelect.setSelectedItem(bundle);
    }

    protected void updateInformationLabel(SolutionBundle bundle) {
        informationPanel.setText(bundle.getSolution().toString());
        revalidate();
        repaint();
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        solutionSelect.setEnabled(enabled);

        if (enabled) {
            // Trigger event
            solutionSelect.setSelectedItem(currentBundle);
        }
        else {
            SolutionBundle bundle = (SolutionBundle) this.solutionSelect.getSelectedItem();
            if (bundle != null) {
                for (PathOverlay overlay: bundle.getOverlays()) {
                    overlay.setVisible(false);
                }
            }
        }
    }

    @Override
    public void newGraphLoaded(Graph graph) {
        for (int i = 0; i < this.solutionSelect.getItemCount(); ++i) {
            for (PathOverlay overlay: this.solutionSelect.getItemAt(i).getOverlays()) {
                overlay.delete();
            }
        }
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
            this.solutionSelect.getItemAt(i).updateOverlays();
        }
    }

}
