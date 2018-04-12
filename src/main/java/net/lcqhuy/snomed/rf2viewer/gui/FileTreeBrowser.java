package net.lcqhuy.snomed.rf2viewer.gui;

import org.apache.commons.io.FilenameUtils;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;

public class FileTreeBrowser extends JPanel{

    private DefaultMutableTreeNode root;

    private DefaultTreeModel treeModel;

    private JTree tree;

    private JFileChooser fileChooser;

    private String selectedFolder;

    private JScrollPane treeScrollPane;

    private JTabbedPaneCloseButton fileViewerTab;


    public FileTreeBrowser(JTabbedPaneCloseButton fileViewerTab) {
        super(new BorderLayout());
        fileChooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
        fileChooser.setDialogTitle("Select folder");
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fileChooser.setPreferredSize(new Dimension(800, 600));
        this.fileViewerTab = fileViewerTab;
    }

    public void initialize() {

        createSelectFolderPane();
    }

    private void loadTree() {
        File fileRoot = new File(selectedFolder);
        root = new DefaultMutableTreeNode(new FileNode(fileRoot));
        treeModel = new DefaultTreeModel(root);

        tree = new JTree(treeModel);
        tree.setShowsRootHandles(true);
        tree.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selRow = tree.getRowForLocation(e.getX(), e.getY());
                TreePath selPath = tree.getPathForLocation(e.getX(), e.getY());
                if(selRow != -1) {
                    if(e.getClickCount() == 2) {
                        SwingUtilities.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    tree.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                                    openFile(selPath);
                                } finally {
                                    tree.setCursor(Cursor.getDefaultCursor());
                                }

                            }
                        });

                    }
                }
            }
        });
        if(treeScrollPane != null) {
            this.remove(treeScrollPane);
        }
        treeScrollPane = new JScrollPane(tree);
        this.add(treeScrollPane,BorderLayout.CENTER);
        CreateChildNodes createChildNodes = new CreateChildNodes(fileRoot, root, this);
        new Thread(createChildNodes).start();

    }


    private void openFile(TreePath path) {
        Object o = path.getLastPathComponent();
        if(o instanceof DefaultMutableTreeNode) {
            DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode) o;
            if(treeNode.getUserObject() instanceof FileNode) {
                File file = ((FileNode) treeNode.getUserObject()).getFile();
                if(file.isFile() && FilenameUtils.getExtension(file.getAbsolutePath()).equalsIgnoreCase("txt")) {
                    Rf2Table rf2Table = new Rf2Table(new BorderLayout());
                    try {
                        rf2Table.initTable(file.getAbsolutePath());
                        fileViewerTab.addTab(file.getName().substring(0,15)+"...", null, rf2Table, FilenameUtils.getName(file.getAbsolutePath()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private void createSelectFolderPane() {
        JPanel footerPane = new JPanel();
        JButton selectDirBtn = new JButton("Open Folder");
        selectDirBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int result = fileChooser.showOpenDialog(null);
                if(result == JFileChooser.APPROVE_OPTION) {
                    if(fileChooser.getSelectedFile().isDirectory()) {
                        selectedFolder = fileChooser.getSelectedFile().getAbsolutePath();
                        loadTree();
                    }
                }
            }
        });
        footerPane.add(selectDirBtn);
        this.add(footerPane, BorderLayout.SOUTH);
        
    }

    class CreateChildNodes implements Runnable {

        private DefaultMutableTreeNode root;

        private File fileRoot;

        private JPanel jPanel;

        public CreateChildNodes(File fileRoot,
                                DefaultMutableTreeNode root, JPanel jPanel) {
            this.fileRoot = fileRoot;
            this.root = root;
            this.jPanel = jPanel;
        }

        @Override
        public void run() {
            createChildren(fileRoot, root);
            jPanel.revalidate();

        }

        private void createChildren(File fileRoot,
                                    DefaultMutableTreeNode node) {
            File[] files = fileRoot.listFiles();
            if (files == null) return;

            for (File file : files) {
                DefaultMutableTreeNode childNode =
                        new DefaultMutableTreeNode(new FileNode(file));
                node.add(childNode);
                if (file.isDirectory()) {
                    createChildren(file, childNode);
                }
            }
        }
    }

    class FileNode {

        private File file;

        public FileNode(File file) {
            this.file = file;
        }

        public File getFile() {
            return this.file;
        }

        @Override
        public String toString() {
            String name = file.getName();
            if (name.equals("")) {
                return file.getAbsolutePath();
            } else {
                return name;
            }
        }
    }
    

}
