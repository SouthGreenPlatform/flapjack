package flapjack.gui;

import java.io.*;
import java.util.*;
import javax.swing.*;

import flapjack.data.*;
import flapjack.gui.dialog.*;
import flapjack.gui.traits.*;
import flapjack.gui.visualization.*;
import flapjack.io.*;

import scri.commons.file.*;
import scri.commons.gui.*;

public class MenuFile
{
	private WinMain winMain;
	private WinMainMenuBar menubar;
	private NavPanel navPanel;
	private GenotypePanel gPanel;

	void setComponents(WinMain winMain, WinMainMenuBar menubar, NavPanel navPanel)
	{
		this.winMain = winMain;
		this.menubar = menubar;
		this.navPanel = navPanel;

		gPanel = navPanel.getGenotypePanel();
	}

	void fileNew()
	{
		Project project = winMain.getProject();

		if (ProjectSerializer.okToContinue(project, false) == false)
			return;

		project = new Project();
		winMain.setTitle(RB.getString("gui.WinMain.title") + " - " + Install4j.VERSION);

		gPanel.resetBufferedState(false);
		winMain.setProject(project);
		navPanel.setProject(project);
	}

	void fileOpen(File file)
	{
		Project project = winMain.getProject();

		if (ProjectSerializer.okToContinue(project, false) == false)
			return;

		// If the file is invalid or the user cancels the dialog, just quit
		File toOpen = ProjectSerializer.queryOpen(file);
		if (toOpen == null)
			return;

		gPanel.resetBufferedState(false);

		// Attempt to open the project (as a trackable job)...
		SaveLoadHandler handler = new SaveLoadHandler(null, toOpen);
		ProgressDialog dialog = new ProgressDialog(handler,
			 RB.format("gui.MenuFile.loadTitle"),
			 RB.format("gui.MenuFile.loading"));

		if (dialog.isOK() && handler.isOK)
		{
			Project openedProject = handler.project;

			winMain.setProject(openedProject);
			navPanel.setProject(openedProject);
			menubar.createRecentMenu(openedProject.filename);
			new DataOpenedAnimator(gPanel);

			winMain.setTitle(openedProject.filename.getName()
				+ " - " + RB.getString("gui.WinMain.title")
				+ " - " + Install4j.VERSION);
		}
		else
			gPanel.resetBufferedState(true);
	}

	public boolean fileSave(boolean saveAs)
	{
		Project project = winMain.getProject();

		// Check that it's ok to save (or save as)
		boolean gz = Prefs.guiSaveCompressed;
		if (ProjectSerializer.querySave(project, saveAs, gz) == false)
			return false;

		// If so, do so
		SaveLoadHandler handler = new SaveLoadHandler(project, null);
		ProgressDialog dialog = new ProgressDialog(handler,
			 RB.format("gui.MenuFile.saveTitle"),
			 RB.format("gui.MenuFile.saving"));

		if (dialog.isOK() && handler.isOK)
		{
			Actions.projectSaved();
			menubar.createRecentMenu(project.filename);

			winMain.setTitle(project.filename.getName()
				+ " - " + RB.getString("gui.WinMain.title")
				+ " - " + Install4j.VERSION);

			return true;
		}

		return false;
	}

	void fileImport()
	{
		boolean secondaryOptions = navPanel.getDataSetForSelection() != null;

		// Start by offering various import options
		ImportOptionsDialog optionsDialog = new ImportOptionsDialog(secondaryOptions);
		if (optionsDialog.isOK() == false)
			return;


		switch (Prefs.guiImportMethod)
		{
			// Import from file
			case 0: importGenotypeData();
				break;

			// Import from Germinate
			case 1:
				break;

			// Importing from a Flapjack-provided sample fileset
			case 2: importSampleData();
				break;

			// Import trait data
			case 20: importTraitData();
				break;
		}
	}

	// Pops up the Import Data dialog, then uses the returned map and dat file
	// information to import data
	private void importGenotypeData()
	{
		DataImportDialog dialog = new DataImportDialog();
		if (dialog.isOK() == false)
			return;

		gPanel.resetBufferedState(false);

		importGenotypeData(dialog.getMapFile(), dialog.getGenotypeFile());
	}

	// Extracts sample data from the jar file, writes it to a temp location,
	// then imports it
	private void importSampleData()
	{
		File dir = SystemUtils.getTempUserDirectory("flapjack");
		File mapFile = new File(dir, "sample.map");
		File datFile = new File(dir, "sample.dat");

		try
		{
			FileUtils.writeFile(mapFile, getClass().getResourceAsStream("/res/samples/sample.map"));
			FileUtils.writeFile(datFile, getClass().getResourceAsStream("/res/samples/sample.dat"));
		}
		catch (Exception e)
		{
			System.out.println(e);
			TaskDialog.error(RB.format("gui.WinMain.readJarError", e.getMessage()),
				RB.getString("gui.text.close"));
			return;
		}

		importGenotypeData(mapFile, datFile);
	}

	// Given a map file and a genotype (dat) file, imports that data, showing a
	// progress bar while doing so
	private void importGenotypeData(File mapFile, File datFile)
	{
		DataImportingDialog dialog = new DataImportingDialog(mapFile, datFile);

		if (dialog.isOK())
		{
			DataSet dataSet = dialog.getDataSet();

			winMain.getProject().addDataSet(dataSet);
			navPanel.addDataSetNode(dataSet);
			new DataOpenedAnimator(gPanel);

			Actions.projectModified();
		}
	}

	public void importTraitData()
	{
		DataSet dataSet = navPanel.getDataSetForSelection();

		// Open up a file browser
		JFileChooser fc = new JFileChooser();
		fc.setDialogTitle(RB.getString("gui.MenuFile.traitBrowse"));
		fc.setCurrentDirectory(new File(Prefs.guiCurrentDir));

		if (fc.showOpenDialog(winMain) != JFileChooser.APPROVE_OPTION)
			return;

		File file = fc.getSelectedFile();
		Prefs.guiCurrentDir = fc.getCurrentDirectory().toString();

		// Import the data using the standard progress bar dialog...
		TraitImporter importer = new TraitImporter(file, dataSet);

		ProgressDialog dialog = new ProgressDialog(importer,
			 RB.format("gui.MenuFile.importTraits.dialogTitle"),
			 RB.format("gui.MenuFile.importTraits.dialogLabel"));

		// If the operation failed or was cancelled...
		if (dialog.isOK() == false)
		{
			if (dialog.getException() != null)
			{
				dialog.getException().printStackTrace();
				TaskDialog.error(
					RB.format("gui.MenuFile.importTraits.error",
					file, dialog.getException().getMessage()),
					RB.getString("gui.text.close"));
			}

			return;
		}

		// Check to see if any of the views need traits assigned to them
		for (GTViewSet viewSet: dataSet.getViewSets())
			viewSet.assignTraits();

		navPanel.getTraitsPanel(dataSet).updateModel();
		Actions.projectModified();
	}

	private static class SaveLoadHandler implements ITrackableJob
	{
		Project project;
		File file;
		boolean isOK;

		SaveLoadHandler(Project project, File file)
		{
			this.project = project;
			this.file = file;
		}

		public void runJob()
		{
			// Loading...
			if (project == null)
				if ((project = ProjectSerializer.open(file)) != null)
					isOK = true;
			// Saving...
			else
				isOK = ProjectSerializer.save(project, Prefs.guiSaveCompressed);
		}

		public boolean isIndeterminate()
			{ return false; }

		public int getMaximum()
			{ return 0; }

		public int getValue()
			{ return 0; }

		public void cancelJob() {}
	}
}