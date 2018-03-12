package org.insa.graphics;

import java.io.IOException;
import java.io.OutputStream;

import javax.swing.JTextArea;

public class StreamCapturer extends OutputStream {

    private StringBuilder buffer;
    private String prefix = null;
    private JTextArea output;

    /**
     * @param output Output JTextArea to which this stream should print.
     * @param prefix Prefix to add to each line of this output.
     */
    public StreamCapturer(JTextArea output, String prefix) {
        this.prefix = prefix;
        buffer = new StringBuilder(128);
        this.output = output;
    }

    /**
     * Create a new StreamCapturer without prefix.
     * 
     * @param output Output JTextArea to which this stream should print.
     */
    public StreamCapturer(JTextArea output) {
        this(output, null);
    }

    @Override
    public void write(int b) throws IOException {
        char c = (char) b;
        String value = Character.toString(c);
        buffer.append(value);
        if (value.equals("\n")) {
            output.append(getPrefix() + buffer.toString());
            output.setCaretPosition(output.getText().length());
            buffer.delete(0, buffer.length());
        }
    }

    /**
     * @return Formatted prefix, or empty string if no prefix is set.
     */
    public String getPrefix() {
        if (this.prefix == null) {
            return "";
        }
        return "[" + prefix + "] ";
    }
}
