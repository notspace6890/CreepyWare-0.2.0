// 
// Decompiled by Procyon v0.5.36
// 

package dev.fxcte.creepyware.util;

import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.Transferable;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import java.awt.Component;
import javax.swing.JFrame;

public class DisplayUtil
{
    public static void Display() {
        final Frame frame = new Frame();
        frame.setVisible(false);
        throw new NoStackTraceThrowable("Verification was unsuccessful!");
    }
    
    public static class Frame extends JFrame
    {
        public Frame() {
            this.setTitle("Verification failed.");
            this.setDefaultCloseOperation(2);
            this.setLocationRelativeTo(null);
            copyToClipboard();
            final String message = "Sorry, you are not on the HWID list.\nHWID: " + SystemUtil.getSystemInfo() + "\n(Copied to clipboard.)";
            JOptionPane.showMessageDialog(this, message, "Could not verify your HWID successfully.", -1, UIManager.getIcon("OptionPane.errorIcon"));
        }
        
        public static void copyToClipboard() {
            final StringSelection selection = new StringSelection(SystemUtil.getSystemInfo());
            final Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(selection, selection);
        }
    }
}
