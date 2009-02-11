package flapjack.gui.navpanel;

import java.awt.*;
import java.awt.event.*;
import java.net.*;
import javax.swing.*;

import flapjack.gui.*;
import flapjack.gui.dialog.*;

public class NBStartWelcomePanel extends javax.swing.JPanel
{
	public NBStartWelcomePanel()
	{
		initComponents();
		setBackground(Color.white);
		panel.setBackground(Color.white);

		setBorder(BorderFactory.createEmptyBorder(0, 0, -10, 0));

		panel.setBorder(BorderFactory.createTitledBorder(RB.getString("gui.navpanel.NBStartWelcomePanel.panel.title")));
		flapjackLabel.setText("<html>" + RB.format("gui.navpanel.NBStartWelcomePanel.panel.label", Install4j.VERSION));
		feedbackLabel.setText(RB.getString("gui.navpanel.NBStartWelcomePanel.panel.feedback"));

		feedbackLabel.setIcon(Icons.getIcon("FEEDBACK"));
		feedbackLabel.setCursor(FlapjackUtils.HAND_CURSOR);
		feedbackLabel.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent event)
			{
				try
				{
					Desktop desktop = Desktop.getDesktop();
					desktop.mail(new URI("mailto:flapjack@germinate.org.uk?subject=Flapjack%20Feedback"));
				}
				catch (Exception e) { System.out.println(e);}
			}
		});
	}

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panel = new javax.swing.JPanel();
        flapjackLabel = new javax.swing.JLabel();
        feedbackLabel = new javax.swing.JLabel();

        panel.setBorder(javax.swing.BorderFactory.createTitledBorder("Welcome to Flapjack:"));

        flapjackLabel.setText("<html>Flapjack x.xx.xx.xx - &copy; Plant Bioinformatics Group, SCRI.");

        feedbackLabel.setForeground(new java.awt.Color(68, 106, 156));
        feedbackLabel.setText("Send feedback");

        org.jdesktop.layout.GroupLayout panelLayout = new org.jdesktop.layout.GroupLayout(panel);
        panel.setLayout(panelLayout);
        panelLayout.setHorizontalGroup(
            panelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, panelLayout.createSequentialGroup()
                .addContainerGap()
                .add(flapjackLabel)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(feedbackLabel)
                .addContainerGap())
        );
        panelLayout.setVerticalGroup(
            panelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(panelLayout.createSequentialGroup()
                .addContainerGap()
                .add(panelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(feedbackLabel)
                    .add(flapjackLabel))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(panel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(panel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel feedbackLabel;
    private javax.swing.JLabel flapjackLabel;
    private javax.swing.JPanel panel;
    // End of variables declaration//GEN-END:variables
}