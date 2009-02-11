package flapjack.gui.dialog.analysis;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;

import flapjack.data.*;
import flapjack.gui.*;

class NBSortLinesPanel extends javax.swing.JPanel
{
	private GTView view;

	public NBSortLinesPanel(GTViewSet viewSet)
	{
		initComponents();

		if (true)
			RB.setText(lineLabel, "gui.dialog.analysis.NBSortLinesPanel.lineLabel.similarity");

		linePanel.setBorder(BorderFactory.createTitledBorder(RB.getString("gui.dialog.analysis.NBSortLinesPanel.linePanel.title")));
		tablePanel.setBorder(BorderFactory.createTitledBorder(RB.getString("gui.dialog.analysis.NBSortLinesPanel.tablePanel.title")));
		RB.setText(tableLabel, "gui.dialog.analysis.NBSortLinesPanel.tableLabel");
		RB.setText(selectAllLabel, "gui.dialog.analysis.NBSortLinesPanel.selectAllLabel");
		RB.setText(selectNoneLabel, "gui.dialog.analysis.NBSortLinesPanel.selectNoneLabel");

		view = viewSet.getSelectedView();

		DefaultComboBoxModel lineModel = new DefaultComboBoxModel();
		for (int i = 0; i < view.getLineCount(); i++)
			lineModel.addElement(view.getLine(i));

		selectedLine.setModel(lineModel);

		if (view.mouseOverLine >= 0 && view.mouseOverLine < view.getLineCount())
			selectedLine.setSelectedIndex(view.mouseOverLine);

		createLinkLabels();
		createTable(viewSet);
	}

	private void createLinkLabels()
	{
		selectAllLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
		selectNoneLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));

		selectAllLabel.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent event) {
				selectAll();
			}
		});

		selectNoneLabel.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent event) {
				selectNone();
			}
		});
	}

	private void createTable(GTViewSet viewSet)
	{
		String[] columnNames = {
			RB.getString("gui.dialog.analysis.NBSortLinesPanel.column1"),
			RB.getString("gui.dialog.analysis.NBSortLinesPanel.column2"),
			RB.getString("gui.dialog.analysis.NBSortLinesPanel.column3")
		};

		// Fill the data array with the string values from the list
		Object[][] data = new Object[viewSet.getChromosomeCount()][3];

		for (int i = 0; i < viewSet.getChromosomeCount(); i++)
		{
			GTView view = viewSet.getView(i);

			if (viewSet.getViewIndex() == i)
				data[i][0] = new Boolean(true);
			else
				data[i][0] = new Boolean(false);

			data[i][1] = view.getChromosomeMap().getName();
			data[i][2] = view.getSelectedMarkerCount() + " / "
				+ view.getMarkerCount();

		}

		table.setModel(new DefaultTableModel(data, columnNames)
		{
			public Class getColumnClass(int c) {
				return getValueAt(0, c).getClass();
			}

			public boolean isCellEditable(int row, int col) {
				return col == 0;
			}
		});

		table.getColumnModel().getColumn(0).setPreferredWidth(30);
		table.setDefaultRenderer(String.class, new StringRenderer());

		// Ensure the single selection is actually visible when first displayed
		Rectangle r = table.getCellRect(viewSet.getViewIndex(), 0, true);
		table.scrollRectToVisible(r);
	}

	boolean isOK()
	{
		view.mouseOverLine = selectedLine.getSelectedIndex();

		return true;
	}

	private void selectAll()
	{
		for (int i = 0; i < table.getRowCount(); i++)
			table.setValueAt(true, i, 0);
	}

	private void selectNone()
	{
		for (int i = 0; i < table.getRowCount(); i++)
			table.setValueAt(false, i, 0);
	}

	// Generates a boolean array with a true/false selected state for each of
	// the possible chromosomes that could be used in the sort
	boolean[] getSelectedChromosomes()
	{
		boolean[] array = new boolean[table.getRowCount()];

		for (int i = 0; i < array.length; i++)
			array[i] = (Boolean) table.getValueAt(i, 0);

		return array;
	}

	// Renders strings in the table so that they are centered
	static class StringRenderer extends JLabel implements TableCellRenderer
	{
		private static Color bgColor = (Color)UIManager.get("Table.background");

		public StringRenderer()
		{
			super("", JLabel.CENTER);

			setBackground(bgColor);
			setOpaque(true);
		}

		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
		{
			setText(value.toString());
			return this;
		}
	}


	/** This method is called from within the constructor to
	 * initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is
	 * always regenerated by the Form Editor.
	 */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        group1 = new javax.swing.ButtonGroup();
        group2 = new javax.swing.ButtonGroup();
        tablePanel = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        table = new javax.swing.JTable();
        tableLabel = new javax.swing.JLabel();
        selectAllLabel = new javax.swing.JLabel();
        label2 = new javax.swing.JLabel();
        selectNoneLabel = new javax.swing.JLabel();
        linePanel = new javax.swing.JPanel();
        selectedLine = new javax.swing.JComboBox();
        lineLabel = new javax.swing.JLabel();

        tablePanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Sort over chromosomes:"));

        table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        table.setRowSelectionAllowed(false);
        table.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(table);

        tableLabel.setLabelFor(table);
        tableLabel.setText("Only markers from the following selected chromosomes will be used:");

        selectAllLabel.setForeground(new java.awt.Color(0, 0, 255));
        selectAllLabel.setText("Select all");

        label2.setText("|");

        selectNoneLabel.setForeground(java.awt.Color.blue);
        selectNoneLabel.setText("Select none");

        org.jdesktop.layout.GroupLayout tablePanelLayout = new org.jdesktop.layout.GroupLayout(tablePanel);
        tablePanel.setLayout(tablePanelLayout);
        tablePanelLayout.setHorizontalGroup(
            tablePanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(tablePanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(tablePanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 325, Short.MAX_VALUE)
                    .add(tableLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 325, Short.MAX_VALUE)
                    .add(tablePanelLayout.createSequentialGroup()
                        .add(selectAllLabel)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(label2)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(selectNoneLabel)))
                .addContainerGap())
        );
        tablePanelLayout.setVerticalGroup(
            tablePanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(tablePanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(tableLabel)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jScrollPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 153, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(tablePanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(selectAllLabel)
                    .add(label2)
                    .add(selectNoneLabel))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        linePanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Comparison line:"));

        lineLabel.setLabelFor(selectedLine);
        lineLabel.setText("Sort {0} to this line:");

        org.jdesktop.layout.GroupLayout linePanelLayout = new org.jdesktop.layout.GroupLayout(linePanel);
        linePanel.setLayout(linePanelLayout);
        linePanelLayout.setHorizontalGroup(
            linePanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(linePanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(lineLabel)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(selectedLine, 0, 226, Short.MAX_VALUE)
                .addContainerGap())
        );
        linePanelLayout.setVerticalGroup(
            linePanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(linePanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(linePanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(lineLabel)
                    .add(selectedLine, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, tablePanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, linePanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(linePanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(tablePanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup group1;
    private javax.swing.ButtonGroup group2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel label2;
    private javax.swing.JLabel lineLabel;
    private javax.swing.JPanel linePanel;
    private javax.swing.JLabel selectAllLabel;
    private javax.swing.JLabel selectNoneLabel;
    private javax.swing.JComboBox selectedLine;
    private javax.swing.JTable table;
    private javax.swing.JLabel tableLabel;
    private javax.swing.JPanel tablePanel;
    // End of variables declaration//GEN-END:variables
}