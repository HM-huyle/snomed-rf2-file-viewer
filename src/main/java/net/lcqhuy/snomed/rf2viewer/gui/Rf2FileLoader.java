package net.lcqhuy.snomed.rf2viewer.gui;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;

import javax.swing.table.DefaultTableModel;
import java.io.File;
import java.io.IOException;

public class Rf2FileLoader {

    private String filePath;
    private File file;
    private DefaultTableModel tableModel;

    public Rf2FileLoader(String filePath, DefaultTableModel tableModel) {
        this.filePath = filePath;
        this.file = new File(filePath);
        this.tableModel = tableModel;
    }

    public void loadTable() throws IOException {
        LineIterator lineIterator = FileUtils.lineIterator(file, "utf-8");
        try {
            if(lineIterator.hasNext()) {
                String firstLine = lineIterator.nextLine();
                tableModel.setColumnIdentifiers(firstLine.split("\t"));
            }
            while (lineIterator.hasNext()) {
                tableModel.addRow(lineIterator.next().split("\t"));
            }
        } finally {
            lineIterator.close();
        }
    }

}
