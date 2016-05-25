/*
 * // Copyright 2009-2016 Information & Computational Sciences, JHI. All rights
 * // reserved. Use is subject to the accompanying licence terms.
 */

package jhi.flapjack.io.cmd;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.function.Function;
import java.util.stream.*;

import ch.systemsx.cisd.hdf5.*;

/**
 * Created by gs40939 on 17/08/2015.
 */
public class Hdf5ToGenotypeConverter
{
	private File hdf5File;
	private String outputFilePath = null;
	private List<String> lines = null;
	private List<String> markers = null;
	private boolean missingDataFilter = false;
	private boolean heterozygousFilter = false;

	private Map<String, String> linesReplaced;

	private HashMap<String, Integer> markerInds;

	private IHDF5Reader reader;

	private List<String> hdf5Lines;
	private List<String> hdf5Markers;

	public Hdf5ToGenotypeConverter(File hdf5File, List<String> lines, List<String> markers, boolean missingDataFilter, boolean heterozygousFilter)
	{
		// Setup input and output files
		this.hdf5File = hdf5File;
		this.lines = lines;
		this.markers = markers;

		// TODO: work out how we can implement these filters in a time efficient way
		this.missingDataFilter = missingDataFilter;
		this.heterozygousFilter = heterozygousFilter;
	}

	public static void main(String[] args)
	{
		File hdf5File = null;
		String outputFilePath = null;
		List<String> lines = null;
		List<String> markers = null;
		boolean missingDataFilter = false;
		boolean heterozygousFilter = false;

		try
		{
			for (int i = 0; i < args.length; i++)
			{
				if (args[i].startsWith("-hdf5="))
					hdf5File = new File(args[i].substring(6));
				if (args[i].startsWith("-genotypes="))
					outputFilePath = args[i].substring(11);
				if (args[i].startsWith("-lines="))
					lines = Files.readAllLines(new File(args[i].substring(7)).toPath());
				if (args[i].startsWith("-markers="))
					markers = Files.readAllLines(new File(args[i].substring(9)).toPath());
				if (args[i].startsWith("-missing_filter="))
					missingDataFilter = Boolean.valueOf(args[i].substring(15));
				if (args[i].startsWith("-heterozygous_filter="))
					heterozygousFilter = Boolean.valueOf(args[i].substring(21));
			}

			if (hdf5File == null || outputFilePath == null || lines == null || markers == null)
			{
				printHelp();
				return;
			}

			Hdf5ToGenotypeConverter extractor = new Hdf5ToGenotypeConverter(hdf5File, lines, markers, missingDataFilter, heterozygousFilter);
			extractor.readInput();
			extractor.extractData(outputFilePath, "");
		}
		catch (IOException e)
		{
			printHelp();

			e.printStackTrace();
		}
	}

	public void readInput()
	{
		reader = HDF5Factory.openForReading(hdf5File);

		long s = System.currentTimeMillis();
		hdf5Lines = reader.getGroupMembers("Lines");

		if(lines == null)
			lines = hdf5Lines;

		// Create a mapping of line names with slashes in to the replaced version so we can output the originals at the end
		linesReplaced = lines.stream().filter(line -> line.contains("/")).collect(Collectors.toMap(line -> line.replaceAll("/", "_"), Function.identity()));
		// Replace slashes as HDF5 treats a slash as the start of a sub-folder
		lines = lines.stream().map(line -> line.replaceAll("/", "_")).collect(Collectors.toCollection(ArrayList::new));

		lines.retainAll(hdf5Lines);
		System.out.println();
		System.out.println("Read and filtered lines: " + (System.currentTimeMillis() - s) + " (ms)");

		s = System.currentTimeMillis();
		// Load markers from HDF5 and find the indices of our loaded markers
		String[] hdf5MarkersArray = reader.readStringArray("Markers");
		hdf5Markers = Arrays.asList(hdf5MarkersArray);

		if(markers == null)
			markers = hdf5Markers;

		markerInds = new HashMap<>();
		for (int i=0; i < hdf5MarkersArray.length; i++)
			markerInds.put(hdf5MarkersArray[i], i);
		System.out.println("Read and filtered markers: " + (System.currentTimeMillis() - s) + " (ms)");

		reader.close();
	}

	public void extractData(String outputFile, String headerLines)
	{
		System.out.println();
		long s = System.currentTimeMillis();
		List<Integer> markerIndices = markers.parallelStream().map(marker -> markerInds.get(marker)).collect(Collectors.toList());
		System.out.println("Read and mapped markers: " + (System.currentTimeMillis() - s) + " (ms)");

		reader = HDF5Factory.openForReading(hdf5File);

		s = System.currentTimeMillis();
		String[] stateTable = reader.readStringArray("StateTable");
		System.out.println("Read statetable: " + (System.currentTimeMillis() - s) + " (ms)");

		// Write our output file line by line
		try (PrintWriter writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFile), "UTF8"))))
		{
			// Write header for drag and drop
			writer.println("# fjFile = GENOTYPE");

			// Output any extra header lines that have been provided such as db link urls
			if (!headerLines.isEmpty())
				writer.print(headerLines);

			// Write the header line of a Flapjack file
			writer.println(markers.parallelStream().collect(Collectors.joining("\t", "Accession/Marker\t", "")));

			s = System.currentTimeMillis();
			lines.stream().forEachOrdered(lineName ->
			{
				// Read in a line (all of its alleles from file)
				byte[] genotypes = reader.int8().readArray("Lines/" + lineName);
				if (linesReplaced.get(lineName) != null)
					lineName = linesReplaced.get(lineName);
				String outputGenotypes = createGenotypeFlatFileString(lineName, genotypes, markerIndices, stateTable);
				writer.println(outputGenotypes);
			});
			System.out.println("Output lines to genotype file: " + (System.currentTimeMillis() - s) + " (ms)");
		}
		catch (IOException e) { e.printStackTrace(); }

		reader.close();

		System.out.println();
		System.out.println("HDF5 file converted to Flapjack genotype format");
	}

	private String createGenotypeFlatFileString(String lineName, byte[] genotypes, List<Integer> markerIndices, String[] stateTable)
	{
		// Collect the alleles which match the line and markers we're looking for
		return markerIndices.stream()
			.map(index -> genotypes[index])
			.map(allele -> stateTable[allele])
			.collect(Collectors.joining("\t", lineName + "\t", ""));
	}

	public List<String> getKeptMarkers()
	{
		// Filter the markers from the hdf5 file so that we have a list of only those markers that were in both the hdf5
		// file and the list of desired markers / input list
		List<String> keptMarkers = hdf5Markers;
		keptMarkers.retainAll(markers);

		return keptMarkers;
	}

	private static void printHelp()
	{
		System.out.println("Usage: hdf52geno <options>\n"
			+ " where valid options are:\n"
			+ "   -hdf5=<hdf5_file>                               (required input file)\n"
			+ "   -lines=<file_list_of_lines_one_per_line>        (required input file)\n"
			+ "   -markers=<file_list_of_markers_one_per_line>    (required input file)\n"
			+ "   -missing_filter=<true/false>                    (required input string)\n"
			+ "   -heterozygous_filter=<true/false>               (required input string)\n"
			+ "   -genotypes=<genotypes_file>                     (required output file)\n");
	}
}