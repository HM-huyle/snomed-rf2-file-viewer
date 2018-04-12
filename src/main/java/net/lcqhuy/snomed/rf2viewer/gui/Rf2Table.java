package net.lcqhuy.snomed.rf2viewer.gui;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.regex.Pattern;

public class Rf2Table extends JPanel{

    private DefaultTableModel defaultTableModel;

    public Rf2Table(LayoutManager layout) {
        super(layout);
    }

    public void initTable(String filePath) throws IOException {
        defaultTableModel = new DefaultTableModel();
        Rf2FileLoader rf2FileLoader = new Rf2FileLoader(filePath, defaultTableModel);
        rf2FileLoader.loadTable();
        
        JTable jTable = new JTable(defaultTableModel);
        JScrollPane jScrollPane = new JScrollPane(jTable);
        
        jScrollPane.setPreferredSize(new Dimension(1200,600));
        jScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        jScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);

        JLabel jLabel = new JLabel("<html>" + FilenameUtils.getFullPath(filePath) + "<font color='blue'>" +  FilenameUtils.getName(filePath) +"</font></html>");
        this.add(jLabel,BorderLayout.BEFORE_FIRST_LINE);
        this.add(jScrollPane);
        createSearchPane(jTable);

    }

    public void clearData() {
        defaultTableModel.getDataVector().removeAllElements();
        System.gc();
    }

    private void createSearchPane(JTable jTable) {
        JLabel jLabel = new JLabel();
        jLabel.setText(jTable.getRowCount() + " rows");
        JPanel searchPane = new JPanel(new BorderLayout());
        JComboBox<String> comboBox = new JComboBox<>();
        int columnsCount = jTable.getColumnCount();
        for(int i=0;i<columnsCount;i++) {
            comboBox.addItem(jTable.getColumnName(i));
        }
        TableRowSorter<TableModel> rowSorter = new TableRowSorter<>(jTable.getModel());
        jTable.setRowSorter(rowSorter);
        JTextField jTextField = new JTextField();
        jTextField.setColumns(100);
        jTextField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(StringUtils.isNotBlank(jTextField.getText())) {
                    int columnIndex = comboBox.getSelectedIndex();
                    rowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + Pattern.quote(jTextField.getText().trim()),columnIndex));
                } else {
                    rowSorter.setRowFilter(null);
                }
                jLabel.setText(jTable.getRowCount() + " rows");
            }
        });
        searchPane.add(comboBox,BorderLayout.WEST);
        searchPane.add(jTextField, BorderLayout.CENTER);
        searchPane.add(jLabel,BorderLayout.EAST);
        this.add(searchPane, BorderLayout.SOUTH);
    }

}
