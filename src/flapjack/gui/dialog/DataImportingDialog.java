package flapjack.gui.dialog;

import java.awt.*;
import java.awt.event.*;
import java.text.*;
import java.io.*;
import java.util.*;
import javax.swing.*;

import flapjack.data.*;
import flapjack.gui.*;
import flapjack.gui.visualization.colors.*;
import flapjack.io.*;

import scri.commons.file.*;
import scri.commons.gui.*;

// TODO: This dialog allows itself to be closed during the loading operation
// which cancels the load in terms of data appearing in the interface, but the
// actual loading thread (in the background) will still run to completion.

/**
 * Dialog that appears during the importing of data. Shows a progress bar and
 * information/stats on the loading process and the data being read.
 */
public class DataImportingDialog extends JDialog implements Runnable
{
	private DataSet dataSet = new DataSet();

	// To load the map file...
	private File mapFile;
	private ChromosomeMapImporter mapImporter;

	// To load the genotype file...
	private File genoFile;
	private GenotypeDataImporter genoImporter;

	private JLabel mapsLabel, mrksLabel, lineLabel;
	private JProgressBar pBar;
	private boolean isOK = false;

	public DataImportingDialog(File mapFile, File genoFile)
	{
		super(
			Flapjack.winMain,
			RB.getString("gui.dialog.DataImportingDialog.title"),
			true
		);

		this.mapFile = mapFile;
		this.genoFile = genoFile;

		mapImporter  = new ChromosomeMapImporter(mapFile, dataSet);
		genoImporter = new GenotypeDataImporter(genoFile, dataSet,
			Prefs.ioMissingData, Prefs.ioHeteroSeparator);

		add(createControls());

		addWindowListener(new WindowAdapter()
		{
			public void windowOpened(WindowEvent e) {
				loadData();
			}

			public void windowClosing(WindowEvent e) {
				mapImporter.cancelImport();
				genoImporter.cancelImport();
			}
		});

		pack();
		setLocationRelativeTo(Flapjack.winMain);
		setResizable(false);
		setVisible(true);
	}

	private JPanel createControls()
	{
		pBar = new JProgressBar();
		pBar.setIndeterminate(true);
		pBar.setPreferredSize(new Dimension(300, pBar.getPreferredSize().height));

		mapsLabel = new JLabel();
		mrksLabel = new JLabel();
		lineLabel = new JLabel();


		JPanel panel = new JPanel(new BorderLayout(5, 10));

		JPanel labelPanel = new JPanel(new GridLayout(3, 2));
		labelPanel.add(
			new JLabel(RB.getString("gui.dialog.DataImportingDialog.maps")));
		labelPanel.add(mapsLabel);
		labelPanel.add(
			new JLabel(RB.getString("gui.dialog.DataImportingDialog.line")));
		labelPanel.add(lineLabel);
		labelPanel.add(
			new JLabel(RB.getString("gui.dialog.DataImportingDialog.mrks")));
		labelPanel.add(mrksLabel);


		panel.add(labelPanel, BorderLayout.WEST);
		panel.add(pBar, BorderLayout.SOUTH);

		panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		return panel;
	}

	public boolean isOK() {
		return isOK;
	}

	public DataSet getDataSet() {
		return dataSet;
	}

	private void loadData()
	{
		final NumberFormat nf = NumberFormat.getInstance();

		Runnable r = new Runnable() {
			public void run()
			{
				while (isVisible())
				{
					int lineCount = genoImporter.getLineCount();

					if (lineCount > 0)
					{
						pBar.setIndeterminate(false);
						pBar.setValue(genoImporter.getLineCount());
					}

					lineLabel.setText("  " + nf.format(genoImporter.getLineCount()));
					mrksLabel.setText("  " + nf.format(genoImporter.getMarkerCount()));

					try { Thread.sleep(100); }
					catch (InterruptedException e) {}
				}
			}
		};

		new Thread(r).start();
		new Thread(this).start();
	}

	public void run()
	{
		try
		{
			// Read the map
			long s = System.currentTimeMillis();
			mapImporter.importMap();
			long e = System.currentTimeMillis();

			System.out.println("Map loaded in " + (e-s) + "ms");

			mapsLabel.setText("  " + dataSet.countChromosomeMaps());

			int lineCount = FileUtils.countLines(genoImporter.getFile(), 16384);
			pBar.setMaximum(lineCount);

			// Read the genotype data
			s = System.currentTimeMillis();
			genoImporter.importGenotypeData();
			e = System.currentTimeMillis();

			System.out.println("Genotype data loaded in " + (e-s) + "ms");


			// Collapse heterozyous states
			if (Prefs.ioHeteroCollapse)
			{
				s = System.currentTimeMillis();

				pBar.setIndeterminate(true);
				CollapseHeterozygotes c = new CollapseHeterozygotes(dataSet);
				c.collapse();

				e = System.currentTimeMillis();
				System.out.println("Genotypes collapsed in " + (e-s) + "ms");
			}

			fakeQTLs(dataSet.getMapByIndex(0).getQTLs());

			dataSet.setName(getDataSetName());

			// Create (and add) a default view of the dataset
			String name = RB.getString("gui.navpanel.VisualizationNode.defaultView");
			GTViewSet viewSet = new GTViewSet(dataSet, name);
			dataSet.getViewSets().add(viewSet);

			// Try to guess a suitable colour scheme - 2 (hz) states can
			// probably use the two colour model
			if (dataSet.getStateTable().getHomozygousStateCount() == 2)
				viewSet.setColorScheme(ColorScheme.SIMPLE_TWO_COLOR);

			if (Prefs.warnDuplicateMarkers)
				displayDuplicates();

			isOK = true;
		}
		catch (IOException e)
		{
			TaskDialog.error(
				RB.format("gui.dialog.DataImportingDialog.ioException", e.getMessage()),
				RB.getString("gui.text.close"));

			e.printStackTrace();
		}
		catch (Exception e)
		{
			TaskDialog.error(
				RB.format("gui.dialog.DataImportingDialog.exception", e.getMessage()),
				RB.getString("gui.text.close"));

			e.printStackTrace();
		}

		setVisible(false);
	}

	private void displayDuplicates()
	{
		if (mapImporter.getDuplicates().size() == 0)
			return;

		Runnable r = new Runnable() {
			public void run() {
				new DuplicateMarkersDialog(mapImporter.getDuplicates());
			}
		};

		try { SwingUtilities.invokeAndWait(r); }
		catch (Exception e) {}
	}

	private String getDataSetName()
	{
		String name = genoFile.getName();

		// Strip away the extension (if there is one)
		if (name.lastIndexOf(".") != -1)
			name = name.substring(0, name.lastIndexOf("."));

		name += " " + dataSet.countLines() + "x" + dataSet.countMarkers();

		return name;
	}

	private void fakeQTLs(Vector<QTL> qtls)
	{
		qtls.add(new QTL("TestA", 5f, 3f, 7f, 39.31f));
		qtls.add(new QTL("TestB", 141f, 138f, 141.5f, 39.31f));
		qtls.add(new QTL("TestC", 28f, 24f, 30f, 19.96f));
	}
}