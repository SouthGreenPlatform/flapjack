package flapjack.gui.visualization;

import java.awt.*;
import java.awt.image.*;
import javax.swing.*;

import flapjack.data.*;

class QTLCanvas extends JPanel
{
	private GenotypePanel gPanel;
	private GenotypeCanvas canvas;

	private BufferFactory bufferFactory;
	BufferedImage image;

	private int h = 20;

	// Scaling factor to convert between pixels and map positions
	private float xScale;

	QTLCanvas(GenotypePanel gPanel, GenotypeCanvas canvas)
	{
		this.gPanel = gPanel;
		this.canvas = canvas;

		setLayout(new BorderLayout());
		setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
		add(new Canvas2D());
	}

	void updateView()
	{
		repaint();
	}

	void createImage()
	{
		image = null;

		if (bufferFactory != null)
		{
			bufferFactory.killMe = true;
			bufferFactory.interrupt();
		}

		bufferFactory = new BufferFactory(canvas.canvasW, h, false);
		bufferFactory.start();
	}

	BufferedImage createSavableImage()
	{
		BufferFactory tempFactory = new BufferFactory(canvas.canvasW, h, true);
		tempFactory.run();

		return tempFactory.buffer;
	}

	private void bufferAvailable(BufferedImage image)
	{
		this.image = image;
		repaint();
	}

	private class Canvas2D extends JPanel
	{
		Canvas2D()
		{
			setPreferredSize(new Dimension(0, h));
		}

		public void paintComponent(Graphics graphics)
		{
			super.paintComponent(graphics);
			Graphics2D g = (Graphics2D) graphics;

			// Calculate the required offset and width
			int xOffset = gPanel.listPanel.getPanelWidth() + 1;
			int width = (canvas.pX2-canvas.pX1);
			g.translate(xOffset, 0);


			if (image == null)
				return;

			int w = width;
			int x = canvas.pX2;
			if (canvas.canvasW < w)
				w = x = canvas.canvasW;

			// Cut out the area of the main buffer we want to draw
			BufferedImage image2 = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
			Graphics2D g2d = image2.createGraphics();
			g2d.drawImage(image, 0, 0, w, h, canvas.pX1, 0, x, h, null);
			g2d.dispose();

			// And dump it to the screen
			g.drawImage(image2, 0, 0, null);

			// Translate the origin of the canvas so that we see (and draw) the
			// area appropriate to what the main canvas is viewing
			g.translate(0-canvas.pX1, 0);
		}
	}

	private class BufferFactory extends Thread
	{
		BufferedImage buffer;

		// isTempBuffer = true when a buffer is being made for saving as an image
		private boolean isTempBuffer = false;
		private boolean killMe = false;
		private int w, h;

		BufferFactory(int w, int h, boolean isTempBuffer)
		{
			this.w = w;
			this.h = h;
			this.isTempBuffer = isTempBuffer;
		}

		public void run()
		{
			setPriority(Thread.MIN_PRIORITY);
			setName("QTLCanvas BufferFactory");

			try { Thread.sleep(500); }
			catch (InterruptedException e) {}

			// Run everything under try/catch conditions due to changes in the
			// view that may invalidate what this thread is trying to access
			try
			{
				createBuffer();
			}
			catch (ArrayIndexOutOfBoundsException e)
			{
				System.out.println("QTLCanvas: " + e.getMessage());
			}
		}

		private void createBuffer()
			throws ArrayIndexOutOfBoundsException
		{
			try
			{
				buffer = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
			}
			catch (Throwable t) { return; }

			Graphics2D g2d = buffer.createGraphics();

			drawCanvas(g2d);
			g2d.dispose();
		}

		private void drawCanvas(Graphics2D g)
			throws ArrayIndexOutOfBoundsException
		{
			if (isTempBuffer)
				g.setColor(Color.white);
			else
				g.setColor(getBackground());
			g.fillRect(0, 0, canvas.canvasW, h);

			int mkrCount = canvas.view.getMarkerCount();
			xScale = canvas.canvasW / canvas.view.getMapLength();

			// Draw a white line representing the map
			BasicStroke s = new BasicStroke(1, BasicStroke.CAP_BUTT,
				BasicStroke.JOIN_MITER, 10, new float[] { 5,2 }, 0);

			g.setColor(Color.lightGray);
			g.setStroke(s);
			g.drawLine(0, 10, canvas.canvasW, 10);
			g.setStroke(new BasicStroke(1));


			ChromosomeMap map = canvas.view.getChromosomeMap();

			for (QTL qtl: map.getQTLs())
			{
				g.setColor(Color.white);
				int minX = Math.round(xScale * qtl.getRangeMin());
				int maxX = Math.round(xScale * qtl.getRangeMax());
				g.fillRect(minX, 5, (maxX-minX+1), 10);
				g.setColor(Color.lightGray);
				g.drawRect(minX, 5, (maxX-minX+1), 10);

				int x = Math.round(xScale * qtl.getPosition());
				g.drawLine(x, 2, x, 18);
			}



			if (!killMe && !isTempBuffer)
				bufferAvailable(buffer);
		}
	}
}