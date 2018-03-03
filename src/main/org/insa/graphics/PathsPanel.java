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
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
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

    private class PathPanel extends JPanel {

        /**
         * 
         */
        private static final long serialVersionUID = 1L;

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
        public PathPanel(Path path) {
            super();
            setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
            setBorder(BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.GRAY),
                    new EmptyBorder(5, 0, 5, 0)));
            this.path = path;
            this.overlay = drawing.drawPath(this.path);

            JCheckBox checkbox = new JCheckBox();
            checkbox.setSelected(true);
            checkbox.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    overlay.setVisible(checkbox.isSelected());
                }
            });

            JLabel infoPanel = new JLabel();
            String info = "";
            info += String.format("Length = %.3f kilometers, duration = ", path.getLength() / 1000.);
            double time = path.getMinimumTravelTime();
            int hours = (int) (time / 3600);
            int minutes = (int) (time / 60) % 60;
            int seconds = ((int) time) % 60;
            info += String.format("%d hours, %d minutes, %d seconds.", hours, minutes, seconds);
            infoPanel.setText("<html>" + toString() + "<br/>" + info + "</html>");

            JButton saveButton = new JButton("Save");
            saveButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String filepath = System.getProperty("user.dir");
                    filepath += File.separator + String.format("path_%s_%d_%d.path",
                            path.getGraph().getMapId().toLowerCase().replaceAll("[^a-z0-9_]", "_"),
                            path.getOrigin().getId(), path.getDestination().getId());
                    JFileChooser fileChooser = new JFileChooser();
                    fileChooser.setSelectedFile(new File(filepath));
                    fileChooser.setApproveButtonText("Save");

                    if (fileChooser.showOpenDialog(getTopLevelAncestor()) == JFileChooser.APPROVE_OPTION) {
                        File file = fileChooser.getSelectedFile();
                        try {
                            BinaryPathWriter writer = new BinaryPathWriter(
                                    new DataOutputStream(new BufferedOutputStream(new FileOutputStream(file))));
                            writer.writePath(path);
                        }
                        catch (IOException e1) {
                            JOptionPane.showMessageDialog(getTopLevelAncestor(),
                                    "Unable to write path to the selected file.");
                            e1.printStackTrace();
                        }
                    }
                }
            });

            JButton deleteButton = new JButton("Delete");
            deleteButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    overlay.delete();
                    PathsPanel.this.removePath(PathPanel.this);
                }
            });

            add(checkbox);
            add(Box.createHorizontalStrut(5));
            add(infoPanel);
            add(Box.createHorizontalGlue());
            add(saveButton);
            add(deleteButton);

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

    public PathsPanel(Component parent) {
        super();
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        setBorder(new EmptyBorder(15, 15, 15, 15));

        // Default hidden
        this.setVisible(false);

    }

    public void addPath(Path path) {
        this.add(new PathPanel(path));
        this.setVisible(true);
        this.revalidate();
    }

    protected void removePath(PathPanel panel) {
        PathsPanel.this.remove(panel);
        PathsPanel.this.validate();
        PathsPanel.this.repaint();
        if (this.getComponentCount() == 0) {
            this.setVisible(false);
        }
    }

    @Override
    public void newGraphLoaded(Graph graph) {
        this.removeAll();
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
        for (Component c: this.getComponents()) {
            if (c instanceof PathPanel) {
                ((PathPanel) c).updateOverlay();
            }
        }
    }

}
