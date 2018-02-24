package org.insa.graphics;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public abstract class BlockingActionListener implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
        this.actionAccepted(e);
    }

    public abstract void actionAccepted(ActionEvent e);
}
