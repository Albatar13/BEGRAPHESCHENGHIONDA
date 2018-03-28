package org.insa.graphics;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JOptionPane;

public class BlockingActionFactory {

    // List of running actions.
    private ArrayList<RunningAction> actions = new ArrayList<>();

    // Parent component.
    private Component parentComponent;

    public BlockingActionFactory(Component parentComponent) {
        this.parentComponent = parentComponent;
    }

    public void addAction(RunningAction action) {
        actions.add(action);
    }

    public ActionListener createBlockingAction(ActionListener listener) {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean accepted = true;

                // Check if actions...
                for (int i = 0; i < actions.size() && accepted; ++i) {
                    RunningAction action = actions.get(i);
                    // If action is running, ask user...
                    if (action.isRunning()) {
                        if (JOptionPane.showConfirmDialog(parentComponent, "Action {"
                                + action.getInformation()
                                + "} is running, do you want to stop it?") == JOptionPane.OK_OPTION) {
                            action.interrupt();
                        }
                        else {
                            accepted = false;
                        }
                    }
                }

                // If action is accepted, run it...
                if (accepted) {
                    listener.actionPerformed(e);
                }
            }
        };
    }

}
