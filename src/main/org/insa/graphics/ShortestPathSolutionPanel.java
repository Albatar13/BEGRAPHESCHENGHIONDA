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
import java.util.IdentityHashMap;
import java.util.Map;

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

    // Parent components
    Component parent;

    // Solution
    private Drawing drawing;

    // Solution selector
    JComboBox<ShortestPathSolution> solutionSelect;

    // Map solution -> panel
    Map<ShortestPathSolution, JPanel> solutionToPanel = new IdentityHashMap<>();

    public ShortestPathSolutionPanel(Component parent, Drawing drawing) {
        super();
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        setBorder(new CompoundBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.BLACK),
                new EmptyBorder(15, 15, 15, 15)));

        this.parent = parent;
        this.drawing = drawing;

        // TODO: Create select + Block for JPanel

        solutionSelect = new JComboBox<>(new ShortestPathSolution[0]);
        solutionSelect.setBackground(Color.WHITE);
        solutionSelect.setAlignmentX(Component.LEFT_ALIGNMENT);
        add(solutionSelect);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        add(bottomPanel);

        solutionSelect.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                @SuppressWarnings("unchecked")
                JComboBox<ShortestPathSolution> combo = (JComboBox<ShortestPathSolution>) e
                        .getSource();
                ShortestPathSolution solution = (ShortestPathSolution) combo.getSelectedItem();

                bottomPanel.removeAll();
                bottomPanel.add(solutionToPanel.get(solution));
            }
        });

    }

    public void addSolution(ShortestPathSolution solution) {

        // add info panel
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
        panel.add(createInformationLabel(solution));
        if (solution != null && solution.isFeasible()) {
            panel.add(Box.createVerticalStrut(8));
            panel.add(createPathPanel(solution));
        }

        solutionToPanel.put(solution, panel);
        solutionSelect.addItem(solution);
        solutionSelect.setSelectedItem(solution);
    }

    protected JLabel createInformationLabel(ShortestPathSolution solution) {
        ShortestPathData data = (ShortestPathData) solution.getInstance();
        String info = null;
        if (solution == null || !solution.isFeasible()) {
            info = String.format("Shortest path: No path found from node #%d to node #%d.",
                    data.getOrigin().getId(), data.getDestination().getId());
        }
        else {
            info = String.format("Shortest path: Found a path from node #%d to node #%d",
                    data.getOrigin().getId(), data.getDestination().getId());
            if (data.getMode() == Mode.LENGTH) {
                info = String.format("%s, %.2f kilometers.", info,
                        (solution.getPath().getLength() / 1000.0));
            }
            else {
                info = String.format("%s, %.2f minutes.", info,
                        (solution.getPath().getMinimumTravelTime() / 60.0));
            }
        }
        JLabel label = new JLabel(info);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        label.setHorizontalAlignment(JLabel.LEFT);

        return label;
    }

    protected JPanel createPathPanel(ShortestPathSolution solution) {

        ShortestPathData data = (ShortestPathData) solution.getInstance();

        final PathOverlay overlay = drawing.drawPath(solution.getPath());

        JButton clearButton = new JButton("Hide");
        clearButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
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
                filepath += File.separator
                        + String.format("path_%#x_%d_%d.path", data.getGraph().getMapId(),
                                data.getOrigin().getId(), data.getDestination().getId());
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setSelectedFile(new File(filepath));
                fileChooser.setApproveButtonText("Save");

                if (fileChooser.showOpenDialog(parent) == JFileChooser.APPROVE_OPTION) {
                    File file = fileChooser.getSelectedFile();
                    try {
                        BinaryPathWriter writer = new BinaryPathWriter(new DataOutputStream(
                                new BufferedOutputStream(new FileOutputStream(file))));
                        writer.writePath(solution.getPath());
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

        return buttonPanel;
    }

    @Override
    public void newGraphLoaded(Graph graph) {
        // TODO:
    }

    @Override
    public void onDrawingLoaded(Drawing oldDrawing, Drawing newDrawing) {
        if (newDrawing != drawing) {
            drawing = newDrawing;
        }
    }

    @Override
    public void onRedrawRequest() {
        // TODO:
    }

}
