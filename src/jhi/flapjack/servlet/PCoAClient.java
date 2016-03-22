// Copyright 2009-2015 Information & Computational Sciences, JHI. All rights
// reserved. Use is subject to the accompanying licence terms.

package jhi.flapjack.servlet;

import java.io.*;
import java.util.concurrent.*;

import jhi.flapjack.data.*;
import jhi.flapjack.gui.*;

import org.restlet.data.*;
import static org.restlet.data.Status.SUCCESS_NO_CONTENT;
import static org.restlet.data.Status.SUCCESS_OK;
import org.restlet.representation.*;
import org.restlet.resource.*;

public class PCoAClient
{
	private final String URL = "http://wildcat:8080/flapjack-test/pcoa/";

	private boolean okToRun = true;

	public PCoAResult generatePco(SimMatrix matrix)
		throws Exception
	{
		Reference pcoaId = postPcoa(matrix);

		if (okToRun)
		{
			String pcoaText = getPcoa(pcoaId);

			if (pcoaText != null && pcoaText.isEmpty() == false)
				return createPcoaFromResponse(pcoaText, matrix);
		}

		return null;
	}

	private Reference postPcoa(SimMatrix matrix)
	{
		ClientResource pcoaResource = new ClientResource(URL);
		pcoaResource.setFollowingRedirects(false);
		pcoaResource.addQueryParameter("flapjackUID", Prefs.flapjackID);

		SimMatrixWriterRepresentation writerRep = new SimMatrixWriterRepresentation(MediaType.TEXT_PLAIN, matrix);
		pcoaResource.post(writerRep);

		return pcoaResource.getLocationRef();
	}

	private String getPcoa(Reference uri)
		throws Exception
	{
		ClientResource cr = new ClientResource(uri);
		cr.accept(MediaType.TEXT_PLAIN);
		Representation r = cr.get();

		while (okToRun && cr.getStatus().equals(SUCCESS_NO_CONTENT))
		{
			System.out.println("Waiting for result...");

			try { Thread.sleep(500); }
			catch (InterruptedException e) {}

			cr.setReference(uri);
			r = cr.get();
		}

		if (okToRun && cr.getStatus().equals(SUCCESS_OK))
		{
			System.out.println("Grabbing result...");

			return r.getText();
		}

		return null;
	}

	private PCoAResult createPcoaFromResponse(String responseText, SimMatrix matrix)
	{
		try (BufferedReader in = new BufferedReader(new StringReader(responseText)))
		{
			PCoAResult result = new PCoAResult(matrix.getLineInfos());

			String str;

			// Read the line order
			while ((str = in.readLine()) != null && str.length() > 0)
			{
				String[] tokens = str.split("\t");
				float[] data = new float[tokens.length - 1];

				// TODO: Will R always return comma for its decimals???
				for (int i = 1; i < tokens.length; i++)
					data[i - 1] = Float.parseFloat(tokens[i]);

				result.addDataRow(data);
			}

			return result;
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		return null;
	}

	public void cancelJob()
	{
		okToRun = false;
	}
}