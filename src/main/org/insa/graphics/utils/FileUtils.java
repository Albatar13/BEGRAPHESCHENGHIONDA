package org.insa.graphics.utils;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.EnumMap;
import java.util.Map;
import java.util.prefs.Preferences;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

public class FileUtils {

    // Preferences
    private static Preferences preferences = Preferences.userRoot().node(FileUtils.class.getName());

    public enum FolderType {
        Map, PathInput, PathOutput
    }

    private static class PreferencesEntry {
        public String key;
        public String value;

        public PreferencesEntry(String key, String value) {
            this.key = key;
            this.value = value;
        }
    }

    // Map folder type -> PreferencesEntry
    private static final Map<FolderType, PreferencesEntry> folderToEntry = new EnumMap<>(
            FolderType.class);

    // Map folder type -> File Filter
    private static final Map<FolderType, FileFilter> folderToFilter = new EnumMap<>(
            FolderType.class);

    static {
        // Populate folderToEntry
        folderToEntry.put(FolderType.Map, new PreferencesEntry("DefaultMapFolder",
                "/home/commetud/3eme Annee MIC/Graphes-et-Algorithmes/Maps"));
        folderToEntry.put(FolderType.PathInput, new PreferencesEntry("DefaultPathInputFolder",
                "/home/commetud/3eme Annee MIC/Graphes-et-Algorithmes/Paths"));
        folderToEntry.put(FolderType.PathOutput,
                new PreferencesEntry("DefaultPathOutputsFolder", "paths"));

        // Populate folderToFilter
        folderToFilter.put(FolderType.Map, new FileNameExtensionFilter("Graph files", "mapgr"));
        folderToFilter.put(FolderType.PathInput, new FileNameExtensionFilter("Path files", "path"));
        folderToFilter.put(FolderType.PathOutput,
                new FileNameExtensionFilter("Path files", "path"));
    }

    /**
     * @return A File instance pointing to the preferred folder for the given type.
     */
    public static File getPreferredFolder(FolderType folderType) {
        PreferencesEntry entry = folderToEntry.get(folderType);
        File folder = new File(preferences.get(entry.key, entry.value));
        if (!folder.exists()) {
            folder = new File(System.getProperty("user.dir"));
        }
        return folder;
    }

    public static void updatePreferredFolder(FolderType folderType, File newPreferredFolder) {
        PreferencesEntry entry = folderToEntry.get(folderType);
        preferences.put(entry.key, newPreferredFolder.getAbsolutePath());
    }

    /**
     * @return A FileFilter corresponding to input graph files.
     */
    public static FileFilter getFileFilter(FolderType folderType) {
        return folderToFilter.get(folderType);
    }

    /**
     * @param folderType
     * @return
     */
    public static JFileChooser createFileChooser(FolderType folderType, String defaultFileName) {
        JFileChooser chooser = new JFileChooser();
        chooser.setCurrentDirectory(getPreferredFolder(folderType));
        if (defaultFileName != null) {
            chooser.setSelectedFile(new File(chooser.getCurrentDirectory().getAbsolutePath()
                    + File.separator + defaultFileName));
        }
        chooser.setFileFilter(getFileFilter(folderType));
        chooser.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getActionCommand().equals(JFileChooser.APPROVE_SELECTION)) {
                    if (chooser.getSelectedFile().exists()) {
                        updatePreferredFolder(folderType,
                                chooser.getSelectedFile().getParentFile());
                    }
                }
            }
        });
        return chooser;
    }

    public static JFileChooser createFileChooser(FolderType folderType) {
        return createFileChooser(folderType, null);
    }

}
