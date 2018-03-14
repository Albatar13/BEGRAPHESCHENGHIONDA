package org.insa.graphics;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.insa.graph.Graph;
import org.insa.graph.Path;
import org.insa.graph.io.BinaryPathWriter;
import org.insa.graphics.drawing.Drawing;
import org.insa.graphics.drawing.overlays.PathOverlay;
import org.insa.graphics.utils.ColorUtils;
import org.insa.graphics.utils.FileUtils;
import org.insa.graphics.utils.FileUtils.FolderType;

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

        /**
         * Simple icon that represents a unicolor rectangle.
         *
         */
        protected class ColorIcon implements Icon {

            private Color color;
            private int width, height;

            public ColorIcon(Color color, int width, int height) {
                this.width = width;
                this.height = height;
                this.color = color;
            }

            public void setColor(Color color) {
                this.color = color;
            }

            @Override
            public void paintIcon(Component c, Graphics g, int x, int y) {
                g.setColor(this.color);
                g.fillRect(x, y, getIconWidth(), getIconHeight());
            }

            @Override
            public int getIconWidth() {
                return this.width;
            }

            @Override
            public int getIconHeight() {
                return this.height;
            }
        }

        // Solution
        private final Path path;

        // Path Overlay (not final due to redraw)
        private PathOverlay overlay;

        // Color button
        private final JButton colorButton;

        /**
         * Create a new bundle with the given path and create a new overlay
         * corresponding to the path.
         * 
         * @param path Path for this bundle, must not be null.
         * 
         * @throws IOException If a resource was not found.
         * 
         */
        public PathPanel(Path path, Color color) throws IOException {
            super();
            setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
            setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY),
                    new EmptyBorder(5, 0, 5, 0)));
            this.path = path;
            this.overlay = drawing.drawPath(this.path, color);

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

            // Display length
            float length = path.getLength();
            if (length < 2000) {
                info += String.format("Length = %.1f meters", length);
            }
            else {
                info += String.format("Length = %.3f kilometers", length / 1000.);
            }

            // Display time
            info += ", Duration=";
            double time = path.getMinimumTravelTime();
            int hours = (int) (time / 3600);
            int minutes = (int) (time / 60) % 60;
            int seconds = ((int) time) % 60;
            if (hours > 0) {
                info += String.format("%d hours, ", hours);
            }
            if (minutes > 0) {
                info += String.format("%d minutes, ", minutes);
            }
            info += String.format("%d seconds.", seconds);
            infoPanel.setText("<html>" + toString() + "<br/>" + info + "</html>");
            infoPanel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    checkbox.setSelected(!checkbox.isSelected());
                    overlay.setVisible(checkbox.isSelected());
                }
            });

            Dimension size = new Dimension(24, 24);

            ColorIcon icon = new ColorIcon(overlay.getColor(), 14, 14);
            colorButton = new JButton(icon);
            colorButton.setFocusable(false);
            colorButton.setFocusPainted(false);
            colorButton.setMinimumSize(size);
            colorButton.setPreferredSize(size);
            colorButton.setMaximumSize(size);

            colorButton.setToolTipText("Pick a color");

            colorButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    final Color originalColor = overlay.getColor();
                    JColorChooser chooser = new JColorChooser(overlay.getColor());
                    chooser.getSelectionModel().addChangeListener(new ChangeListener() {
                        @Override
                        public void stateChanged(ChangeEvent e) {
                            icon.setColor(chooser.getSelectionModel().getSelectedColor());
                            colorButton.repaint();
                            overlay.setColor(chooser.getSelectionModel().getSelectedColor());
                            overlay.redraw();
                        }
                    });

                    JColorChooser.createDialog(getTopLevelAncestor(), "Pick a new color", true,
                            chooser, new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    icon.setColor(chooser.getSelectionModel().getSelectedColor());
                                    colorButton.repaint();
                                    overlay.setColor(
                                            chooser.getSelectionModel().getSelectedColor());
                                    overlay.redraw();
                                }
                            }, new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    icon.setColor(originalColor);
                                    colorButton.repaint();
                                    overlay.setColor(originalColor);
                                    overlay.redraw();
                                }
                            }).setVisible(true);
                    ;

                }
            });

            Image saveImg = ImageIO.read(getClass().getResourceAsStream("/save-icon.png"))
                    .getScaledInstance(14, 14, java.awt.Image.SCALE_SMOOTH);
            JButton saveButton = new JButton(new ImageIcon(saveImg));
            saveButton.setFocusPainted(false);
            saveButton.setFocusable(false);
            saveButton.setMinimumSize(size);
            saveButton.setPreferredSize(size);
            saveButton.setMaximumSize(size);
            saveButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String filepath = String.format("path_%s_%d_%d.path",
                            path.getGraph().getMapId().toLowerCase().replaceAll("[^a-z0-9_]", ""),
                            path.getOrigin().getId(), path.getDestination().getId());
                    JFileChooser chooser = FileUtils.createFileChooser(FolderType.PathOutput,
                            filepath);

                    if (chooser
                            .showSaveDialog(getTopLevelAncestor()) == JFileChooser.APPROVE_OPTION) {
                        File file = chooser.getSelectedFile();
                        try {
                            BinaryPathWriter writer = new BinaryPathWriter(new DataOutputStream(
                                    new BufferedOutputStream(new FileOutputStream(file))));
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

            Image newimg = ImageIO.read(getClass().getResourceAsStream("/delete-icon.png"))
                    .getScaledInstance(14, 14, java.awt.Image.SCALE_SMOOTH);
            JButton deleteButton = new JButton(new ImageIcon(newimg));
            deleteButton.setFocusPainted(false);
            deleteButton.setFocusable(false);
            deleteButton.setMinimumSize(size);
            deleteButton.setPreferredSize(size);
            deleteButton.setMaximumSize(size);
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
            add(colorButton);
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
            this.overlay.setColor(oldOverlay.getColor());
            ((ColorIcon) this.colorButton.getIcon()).setColor(this.overlay.getColor());
            this.colorButton.repaint();
            this.overlay.setVisible(oldOverlay.isVisible());
            oldOverlay.delete();
        }

        /*
         * (non-Javadoc)
         * 
         * @see java.lang.Object#toString()
         */
        public String toString() {
            return "Path from #" + path.getOrigin().getId() + " to #"
                    + path.getDestination().getId();
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
        try {
            this.add(new PathPanel(path, ColorUtils.getColor(this.getComponentCount())));
            this.setVisible(true);
            this.revalidate();
            this.repaint();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void removePath(PathPanel panel) {
        PathsPanel.this.remove(panel);
        PathsPanel.this.revalidate();
        PathsPanel.this.repaint();
        if (this.getComponentCount() == 0) {
            this.setVisible(false);
        }
    }

    @Override
    public void newGraphLoaded(Graph graph) {
        for (Component c: this.getComponents()) {
            if (c instanceof PathPanel) {
                ((PathPanel) c).overlay.delete();
            }
        }
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
