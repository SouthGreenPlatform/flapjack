// Copyright 2007-2009 Plant Bioinformatics Group, SCRI. All rights reserved.
// Use is subject to the accompanying licence terms.

package flapjack.data;

import java.text.*;
import java.util.*;

import scri.commons.gui.*;

public class XMLRoot
{
	private static float nextID = 0;
	private static DecimalFormat df = new DecimalFormat("0");

	protected float ID;

	public XMLRoot()
	{
		ID = nextID++;
	}

	public String getGuid()
	{
		return df.format(ID);
	}

	public static void reset()
	{
		nextID = 0;
	}
}