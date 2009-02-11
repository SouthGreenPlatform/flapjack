package flapjack.gui.dialog.prefs;

import javax.swing.*;

import flapjack.gui.*;

class NBGeneralPanel extends JPanel implements IPrefsTab
{
	private DefaultComboBoxModel displayModel;
	private DefaultComboBoxModel updateModel;

    public NBGeneralPanel()
    {
        initComponents();

		// Interface settings
		generalPanel.setBorder(BorderFactory.createTitledBorder(RB.getString("gui.dialog.prefs.NBGeneralPanel.generalPanelTitle")));
        displayLabel.setText(RB.getString("gui.dialog.prefs.NBGeneralPanel.displayLabel"));
		RB.setMnemonic(displayLabel, "gui.dialog.prefs.NBGeneralPanel.displayLabel");
        displayHint.setText(RB.getString("gui.dialog.prefs.NBGeneralPanel.displayHint"));

        displayModel = new DefaultComboBoxModel();
        displayModel.addElement(RB.getString("gui.dialog.prefs.NBGeneralPanel.auto"));
        displayModel.addElement(RB.getString("gui.dialog.prefs.NBGeneralPanel.en_GB"));
        displayModel.addElement(RB.getString("gui.dialog.prefs.NBGeneralPanel.en_US"));
        displayModel.addElement(RB.getString("gui.dialog.prefs.NBGeneralPanel.de"));
        displayCombo.setModel(displayModel);


        // Update settings
        updateLabel.setText(RB.getString("gui.dialog.prefs.NBGeneralPanel.updateLabel"));
		RB.setMnemonic(updateLabel, "gui.dialog.prefs.NBGeneralPanel.updateLabel");

        updateModel = new DefaultComboBoxModel();
        updateModel.addElement(RB.getString("gui.dialog.prefs.NBGeneralPanel.updateNever"));
        updateModel.addElement(RB.getString("gui.dialog.prefs.NBGeneralPanel.updateStartup"));
        updateModel.addElement(RB.getString("gui.dialog.prefs.NBGeneralPanel.updateDaily"));
        updateModel.addElement(RB.getString("gui.dialog.prefs.NBGeneralPanel.updateWeekly"));
        updateModel.addElement(RB.getString("gui.dialog.prefs.NBGeneralPanel.updateMonthly"));
        updateCombo.setModel(updateModel);

		projectPanel.setBorder(BorderFactory.createTitledBorder(RB.getString("gui.dialog.prefs.NBGeneralPanel.projectPanelTitle")));
		checkCompress.setText(RB.getString("gui.dialog.prefs.NBGeneralPanel.checkCompress"));
		RB.setMnemonic(checkCompress, "gui.dialog.prefs.NBGeneralPanel.checkCompress");

        initSettings();
    }

    private int getLocaleIndex()
	{
		if (Prefs.localeText.equals("en_GB"))
			return 1;
		else if (Prefs.localeText.equals("en_US"))
			return 2;
		else if (Prefs.localeText.equals("de"))
			return 3;
		else
			return 0;
	}

	private void initSettings()
    {
    	displayCombo.setSelectedIndex(getLocaleIndex());
    	updateCombo.setSelectedIndex(Prefs.guiUpdateSchedule);
    	checkCompress.setSelected(Prefs.guiSaveCompressed);
    }

	public void applySettings()
	{
		switch (displayCombo.getSelectedIndex())
		{
			case 1:  Prefs.localeText = "en_GB"; break;
			case 2:  Prefs.localeText = "en_US"; break;
			case 3:  Prefs.localeText = "de";    break;
			default: Prefs.localeText = "auto";
		}

		Prefs.guiUpdateSchedule = updateCombo.getSelectedIndex();
		Prefs.guiSaveCompressed = checkCompress.isSelected();
	}

	public void setDefaults()
	{
	}

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        generalPanel = new javax.swing.JPanel();
        displayCombo = new javax.swing.JComboBox();
        updateCombo = new javax.swing.JComboBox();
        displayLabel = new javax.swing.JLabel();
        updateLabel = new javax.swing.JLabel();
        displayHint = new javax.swing.JLabel();
        projectPanel = new javax.swing.JPanel();
        checkCompress = new javax.swing.JCheckBox();

        generalPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("General options:"));

        displayLabel.setLabelFor(displayCombo);
        displayLabel.setText("Interface display language:");

        updateLabel.setLabelFor(updateCombo);
        updateLabel.setText("Check for newer Flapjack versions:");

        displayHint.setText("(Restart Flapjack to apply)");

        org.jdesktop.layout.GroupLayout generalPanelLayout = new org.jdesktop.layout.GroupLayout(generalPanel);
        generalPanel.setLayout(generalPanelLayout);
        generalPanelLayout.setHorizontalGroup(
            generalPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(generalPanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(generalPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(displayLabel)
                    .add(updateLabel))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(generalPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(updateCombo, 0, 158, Short.MAX_VALUE)
                    .add(displayHint)
                    .add(displayCombo, 0, 158, Short.MAX_VALUE))
                .addContainerGap())
        );
        generalPanelLayout.setVerticalGroup(
            generalPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(generalPanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(generalPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(displayLabel)
                    .add(displayCombo, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(displayHint)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(generalPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(updateLabel)
                    .add(updateCombo, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        projectPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Project options:"));

        checkCompress.setText("Compress Flapjack projects files to save disk space");

        org.jdesktop.layout.GroupLayout projectPanelLayout = new org.jdesktop.layout.GroupLayout(projectPanel);
        projectPanel.setLayout(projectPanelLayout);
        projectPanelLayout.setHorizontalGroup(
            projectPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(projectPanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(checkCompress)
                .addContainerGap(77, Short.MAX_VALUE))
        );
        projectPanelLayout.setVerticalGroup(
            projectPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(projectPanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(checkCompress)
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(generalPanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(projectPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(generalPanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(projectPanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox checkCompress;
    private javax.swing.JComboBox displayCombo;
    private javax.swing.JLabel displayHint;
    private javax.swing.JLabel displayLabel;
    private javax.swing.JPanel generalPanel;
    private javax.swing.JPanel projectPanel;
    private javax.swing.JComboBox updateCombo;
    private javax.swing.JLabel updateLabel;
    // End of variables declaration//GEN-END:variables
}