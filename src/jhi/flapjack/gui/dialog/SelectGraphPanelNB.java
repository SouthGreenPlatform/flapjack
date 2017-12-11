// Copyright 2009-2017 Information & Computational Sciences, JHI. All rights
// reserved. Use is subject to the accompanying licence terms.

package jhi.flapjack.gui.dialog;

import java.awt.*;
import javax.swing.*;

import scri.commons.gui.*;

import jhi.flapjack.data.*;
import jhi.flapjack.gui.*;
import jhi.flapjack.gui.visualization.*;

public class SelectGraphPanelNB extends javax.swing.JPanel
{
    /** Creates new form NBSelectGraphPanel */
    public SelectGraphPanelNB(GenotypePanel gPanel)
	{
        initComponents();

		setBackground((Color)UIManager.get("fjDialogBG"));

		RB.setText(lblHeader, "gui.dialog.SelectGraphPanelNB.lblHeader");
		RB.setText(lblGraph1, "gui.dialog.SelectGraphPanelNB.graph1");
		RB.setText(lblGraph2, "gui.dialog.SelectGraphPanelNB.graph2");
		RB.setText(lblGraph3, "gui.dialog.SelectGraphPanelNB.graph3");
		RB.setText(lblGraphType, "gui.dialog.SelectGraphPanelNB.graphType");

		ChromosomeMap map = gPanel.getView().getChromosomeMap();

		// Add blank entries to the second two combo boxes
		graph2.addItem("");
		graph3.addItem("");

		// Add the names to the combo box
		for (GraphData graph: map.getGraphs())
		{
			graph1.addItem(graph.getName());
			graph2.addItem(graph.getName());
			graph3.addItem(graph.getName());
		}

		// If graph data not loaded this stops a crapout based on the following
		// setSelectedIndex on graph1
		if (graph1.getModel().getSize() == 0)
			graph1.addItem("");

		// And select whichever ones are "selected"
		int[] graphs = gPanel.getViewSet().getGraphs();
		graph1.setSelectedIndex(graphs[0]);
		graph2.setSelectedIndex(graphs[1] + 1);
		graph3.setSelectedIndex(graphs[2] + 1);


		graphTypeCombo.addItem(RB.getString("gui.dialog.SelectGraphPanelNB.histogramGraphType"));
		graphTypeCombo.addItem(RB.getString("gui.dialog.SelectGraphPanelNB.lineGraphType"));
		graphTypeCombo.setSelectedIndex(Prefs.guiGraphStyle);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblHeader = new javax.swing.JLabel();
        lblGraph1 = new javax.swing.JLabel();
        graph1 = new javax.swing.JComboBox<String>();
        lblGraph2 = new javax.swing.JLabel();
        graph2 = new javax.swing.JComboBox<String>();
        lblGraph3 = new javax.swing.JLabel();
        graph3 = new javax.swing.JComboBox<String>();
        lblGraphType = new javax.swing.JLabel();
        graphTypeCombo = new javax.swing.JComboBox<String>();

        lblHeader.setText("You can select up to three graphs to be displayed on screen at once:");

        lblGraph1.setLabelFor(graph1);
        lblGraph1.setText("Graph 1:");

        lblGraph2.setLabelFor(graph2);
        lblGraph2.setText("Graph 2:");

        lblGraph3.setLabelFor(graph3);
        lblGraph3.setText("Graph 3:");

        lblGraphType.setLabelFor(graphTypeCombo);
        lblGraphType.setText("Graph type:");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblHeader)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblGraph3)
                            .addComponent(lblGraph2)
                            .addComponent(lblGraph1))
                        .addGap(20, 20, 20)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(graph1, 0, 318, Short.MAX_VALUE)
                            .addComponent(graph2, 0, 318, Short.MAX_VALUE)
                            .addComponent(graph3, 0, 318, Short.MAX_VALUE)))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lblGraphType)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(graphTypeCombo, 0, 318, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblHeader)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblGraph1)
                    .addComponent(graph1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblGraph2)
                    .addComponent(graph2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblGraph3)
                    .addComponent(graph3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblGraphType)
                    .addComponent(graphTypeCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    javax.swing.JComboBox<String> graph1;
    javax.swing.JComboBox<String> graph2;
    javax.swing.JComboBox<String> graph3;
    javax.swing.JComboBox<String> graphTypeCombo;
    javax.swing.JLabel lblGraph1;
    javax.swing.JLabel lblGraph2;
    javax.swing.JLabel lblGraph3;
    javax.swing.JLabel lblGraphType;
    private javax.swing.JLabel lblHeader;
    // End of variables declaration//GEN-END:variables

}