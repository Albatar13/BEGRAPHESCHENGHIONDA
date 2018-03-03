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

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import org.insa.graph.Graph;
import org.insa.graph.Path;
import org.insa.graph.io.BinaryPathWriter;
import org.insa.graphics.drawing.Drawing;
import org.insa.graphics.drawing.overlays.PathOverlay;

public class PathsPanel extends JPanel implements DrawingChangeListener, GraphChangeListener {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private class PathBundle {

        // Solution
        private final Path path;

        // Path Overlay (not final due to redraw)
        private PathOverlay overlay;

        /**
         * Create a new bundle with the given path and create a new overlay
         * corresponding to the path.
         * 
         * @param path Path for this bundle, must not be null.
         * 
         */
        public PathBundle(Path path) {
            this.path = path;
            this.overlay = drawing.drawPath(this.path);
        }

        /**
         * @return Path associated with this bundle.
         */
        public Path getPath() {
            return this.path;
        }

        /**
         * @return Overlay associated with this bundle (never null).
         */
        public PathOverlay getOverlay() {
            return this.overlay;
        }

        /**
         * Re-draw the current overlay (if any) on the new drawing.
         * 
         */
        public void updateOverlay() {
            PathOverlay oldOverlay = this.overlay;
            this.overlay = drawing.drawPath(path);
            this.overlay.setVisible(oldOverlay.isVisible());
            oldOverlay.delete();
        }

        /*
         * (non-Javadoc)
         * 
         * @see java.lang.Object#toString()
         */
        public String toString() {
            return "Path from #" + path.getOrigin().getId() + " to #" + path.getDestination().getId();
        }

    }

    // Solution
    private Drawing drawing;

    // Solution selector
    private final JComboBox<PathBundle> solutionSelect;

    // Map solution -> panel
    private final JTextArea informationPanel;

    // Current bundle
    private PathBundle currentBundle = null;

    public PathsPanel(Component parent) {
        super();
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

        setBorder(new EmptyBorder(15, 15, 15, 15));

        solutionSelect = new JComboBox<>();
        solutionSelect.setBackground(Color.WHITE);
        solutionSelect.setAlignmentX(Component.LEFT_ALIGNMENT);
        add(solutionSelect);

        informationPanel = new JTextArea();
        informationPanel.setWrapStyleWord(true);
        informationPanel.setLineWrap(true);
        informationPanel.setOpaque(true);
        informationPanel.setFocusable(false);
        informationPanel.setEditable(false);
        informationPanel.setBackground(UIManager.getColor("Label.background"));
        informationPanel.setFont(UIManager.getFont("Label.font"));
        informationPanel.setBorder(UIManager.getBorder("Label.border"));
        informationPanel.setAlignmentX(JLabel.LEFT_ALIGNMENT);

        add(Box.createVerticalStrut(8));
        add(informationPanel);

        JButton clearButton = new JButton("Hide");
        clearButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (currentBundle != null) {
                    if (currentBundle.getOverlay().isVisible()) {
                        currentBundle.getOverlay().setVisible(false);
                        clearButton.setText("Show");
                    }
                    else {
                        currentBundle.getOverlay().setVisible(true);
                        clearButton.setText("Hide");
                    }
                }
            }
        });

        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String filepath = System.getProperty("user.dir");
                filepath += File.separator + String.format("path_%s_%d_%d.path",
                        currentBundle.getPath().getGraph().getMapId().toLowerCase().replaceAll("[^a-z0-9_]", "_"),
                        currentBundle.getPath().getOrigin().getId(), currentBundle.getPath().getDestination().getId());
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setSelectedFile(new File(filepath));
                fileChooser.setApproveButtonText("Save");

                if (fileChooser.showOpenDialog(parent) == JFileChooser.APPROVE_OPTION) {
                    File file = fileChooser.getSelectedFile();
                    try {
                        BinaryPathWriter writer = new BinaryPathWriter(
                                new DataOutputStream(new BufferedOutputStream(new FileOutputStream(file))));
                        writer.writePath(currentBundle.getPath());
                    }
                    catch (IOException e1) {
                        JOptionPane.showMessageDialog(parent, "Unable to write path to the selected file.");
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

        add(Box.createVerticalStrut(4));
        add(buttonPanel);

        solutionSelect.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                PathBundle bundle = (PathBundle) solutionSelect.getSelectedItem();

                // Handle case when the JComboBox is empty.
                if (bundle == null) {
                    return;
                }

                if (currentBundle != null) {
                    currentBundle.getOverlay().setVisible(false);
                }

                updateInformationLabel(bundle);
                clearButton.setText("Hide");
                bundle.getOverlay().setVisible(true);
                currentBundle = bundle;
            }
        });

        // Default hidden
        this.setVisible(false);

    }

    public void addPath(Path path) {
        PathBundle bundle = new PathBundle(path);
        solutionSelect.addItem(bundle);
        solutionSelect.setSelectedItem(bundle);

        this.setVisible(true);
    }

    protected void updateInformationLabel(PathBundle bundle) {
        String info = "";
        info += String.format("Length = %.3f kilometers, duration = ", bundle.getPath().getLength() / 1000.);
        double time = bundle.getPath().getMinimumTravelTime();
        int hours = (int) (time / 3600);
        int minutes = (int) (time / 60) % 60;
        int seconds = ((int) time) % 60;
        info += String.format("%d hours, %d minutes, %d seconds.", hours, minutes, seconds);
        informationPanel.setText(info);
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        solutionSelect.setEnabled(enabled);

        if (enabled) {
            // Trigger event
            solutionSelect.actionPerformed(null);
        }
        else {
            PathBundle bundle = (PathBundle) this.solutionSelect.getSelectedItem();
            if (bundle != null) {
                bundle.getOverlay().setVisible(false);
            }
        }
    }

    @Override
    public void newGraphLoaded(Graph graph) {
        for (int i = 0; i < this.solutionSelect.getItemCount(); ++i) {
            this.solutionSelect.getItemAt(i).getOverlay().delete();
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
            this.solutionSelect.getItemAt(i).updateOverlay();
        }
    }

}
