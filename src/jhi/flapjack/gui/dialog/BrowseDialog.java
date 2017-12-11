// Copyright 2009-2017 Information & Computational Sciences, JHI. All rights
// reserved. Use is subject to the accompanying licence terms.

package jhi.flapjack.gui.dialog;

import java.awt.event.*;
import java.io.*;
import javax.swing.*;

import jhi.flapjack.gui.*;

import scri.commons.gui.*;

public class BrowseDialog extends JDialog implements ActionListener
{
	private boolean isOK = false;

	private String fileHistory;
	private String rbLabel;
	private String rbButton;
	private String help;

	/**
	 * @param fileHistory the history string to populate the combo box with
	 * @param rbTitle resource bundle string for the dialog's title
	 * @param rbLabel resource bundle string for the dialog's label
	 * @param rbButton resource bundle string for the dialog's main button
	 * @param help help topic to visit if the Help button is clicked (can be
	 * null)
	 */
	public BrowseDialog(String fileHistory, String rbTitle, String rbLabel, String rbButton, String help)
	{
		super(Flapjack.winMain, RB.getString(rbTitle), true);

		this.fileHistory = fileHistory;
		this.rbLabel = rbLabel;
		this.rbButton = rbButton;
		this.help = help;

		initComponents();
		initComponents2();

		FlapjackUtils.initDialog(this, bImport, bCancel, true, getContentPane(), panel);
	}

	private void initComponents2()
	{
		RB.setText(bImport, rbButton);
		bImport.addActionListener(this);
		RB.setText(bCancel, "gui.text.cancel");
		bCancel.addActionListener(this);
		RB.setText(bHelp, "gui.text.help");
		bHelp.addActionListener(this);

		FlapjackUtils.setHelp(bHelp, help);

		panel.setBorder(BorderFactory.createTitledBorder(RB.getString("gui.dialog.NBBrowsePanel.panel.title")));
		RB.setText(label, rbLabel);
		RB.setText(bBrowse, "gui.text.browse");

		bBrowse.addActionListener(this);
		browseComboBox.setHistory(fileHistory);
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() == bImport && browseNotEmpty())
		{
			isOK = true;
			setVisible(false);
		}

		else if (e.getSource() == bCancel)
			setVisible(false);

		else if (e.getSource() == bBrowse)
		{
			JFileChooser fc = new JFileChooser();
			fc.setDialogTitle(RB.getString("gui.dialog.NBDataImportPanel.fcTitle"));
			fc.setCurrentDirectory(new File(Prefs.guiCurrentDir));

			if (browseComboBox.getText().length() > 0)
				fc.setCurrentDirectory(new File(browseComboBox.getText()));

			if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION)
			{
				File file = fc.getSelectedFile();
				Prefs.guiCurrentDir = fc.getCurrentDirectory().toString();

				browseComboBox.updateComboBox(file.toString());
			}
		}
	}

	public boolean isOK() {
		return isOK;
	}

	public File getFile()
		{ return new File(browseComboBox.getText()); }

	/**
	 * @return browseComboBox.history
	 */
	public String getHistory()
	{
		return browseComboBox.getHistory();
	}

	boolean browseNotEmpty()
	{
		if (browseComboBox.getText().length() == 0)
		{
			TaskDialog.warning(
				RB.getString("gui.dialog.NBBrowsePanel.warning"),
				RB.getString("gui.text.ok"));
			return false;
		}

		return true;
	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        panel = new javax.swing.JPanel();
        label = new javax.swing.JLabel();
        bBrowse = new javax.swing.JButton();
        browseComboBox = new scri.commons.gui.matisse.HistoryComboBox();
        dialogPanel1 = new scri.commons.gui.matisse.DialogPanel();
        bImport = new javax.swing.JButton();
        bCancel = new javax.swing.JButton();
        bHelp = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        panel.setBorder(javax.swing.BorderFactory.createTitledBorder("Browse for file:"));

        label.setText("File to import:");

        bBrowse.setText("Browse...");

        javax.swing.GroupLayout panelLayout = new javax.swing.GroupLayout(panel);
        panel.setLayout(panelLayout);
        panelLayout.setHorizontalGroup(
            panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(label)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(browseComboBox, javax.swing.GroupLayout.DEFAULT_SIZE, 313, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(bBrowse)
                .addContainerGap())
        );
        panelLayout.setVerticalGroup(
            panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(label)
                    .addComponent(bBrowse)
                    .addComponent(browseComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        bImport.setText("Import");
        dialogPanel1.add(bImport);

        bCancel.setText("Cancel");
        dialogPanel1.add(bCancel);

        bHelp.setText("Help");
        dialogPanel1.add(bHelp);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addComponent(dialogPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(dialogPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton bBrowse;
    private javax.swing.JButton bCancel;
    private javax.swing.JButton bHelp;
    private javax.swing.JButton bImport;
    scri.commons.gui.matisse.HistoryComboBox browseComboBox;
    private scri.commons.gui.matisse.DialogPanel dialogPanel1;
    private javax.swing.JLabel label;
    private javax.swing.JPanel panel;
    // End of variables declaration//GEN-END:variables
}