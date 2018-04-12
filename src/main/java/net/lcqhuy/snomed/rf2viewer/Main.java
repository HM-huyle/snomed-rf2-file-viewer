package net.lcqhuy.snomed.rf2viewer;

import net.lcqhuy.snomed.rf2viewer.gui.FileTreeBrowser;
import net.lcqhuy.snomed.rf2viewer.gui.JTabbedPaneCloseButton;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class Main {


    public static void main(String[] args) throws IOException {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                JTabbedPaneCloseButton jTabbedPaneCloseButton = new JTabbedPaneCloseButton();
                jTabbedPaneCloseButton.setPreferredSize(new Dimension(1000,800));
                FileTreeBrowser fileTreeBrowser = new FileTreeBrowser(jTabbedPaneCloseButton);
                fileTreeBrowser.setPreferredSize(new Dimension(400,800));
                fileTreeBrowser.initialize();
                JSplitPane jSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,fileTreeBrowser, jTabbedPaneCloseButton);
                JFrame jFrame = new JFrame("RF2 File Viewer");
                jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                jFrame.add(jSplitPane);
                jFrame.setPreferredSize(new Dimension(1400,800));
                jFrame.pack();
                jFrame.setLocationRelativeTo(null);
                jFrame.setVisible(true);
            }
        });

    }


}
