import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.*;
import javax.swing.JPanel;

class GraphSelectionPanel extends JPanel implements Observer
{
	private GraphSelectionModel selectionModel;

	private Shape grabber;

	public GraphSelectionPanel(GraphSelectionModel selectionModel)
	{
		this.selectionModel = selectionModel;

		this.grabber = new Ellipse2D.Double(0, 0, 8, 8);

		setOpaque(false);
	}

	@Override
	public void update(Observable source, Object arg)
	{
		repaint();
	}

	@Override
	protected void paintComponent(Graphics g)
	{
		super.paintComponent(g);

		if (selectionModel.getSelected().size() < 2)
			return;
		
		Graphics2D g2 = (Graphics2D) g;

		// Draw using antialiassing
		g2.setRenderingHint(
			RenderingHints.KEY_ANTIALIASING, 
			RenderingHints.VALUE_ANTIALIAS_ON);

		Rectangle bounds = getSelectionBounds();
		drawOutline(g2, bounds);
		
		// Top left
		drawGrabber(g2, bounds.x, bounds.y);
		
		// Top right
		drawGrabber(g2, bounds.x + bounds.width, bounds.y);

		// Bottom left
		drawGrabber(g2, bounds.x, bounds.y + bounds.height);

		// Bottom right
		drawGrabber(g2, bounds.x + bounds.width, bounds.y + bounds.height);
	}

	private Rectangle getSelectionBounds()
	{
		Iterator<GraphVertex> it = selectionModel.getSelected().iterator();

		Rectangle bounds = it.next().getBounds();
		
		// Calculate the union of the bounds of all the selected vertices
		while (it.hasNext())
			bounds = bounds.union(it.next().getBounds());

		// Add a bit of spacing
		bounds.setLocation(bounds.x - 1, bounds.y - 1);
		bounds.setSize(bounds.width + 2, bounds.height + 2);

		return bounds;
	}

	private void drawGrabber(Graphics2D g2, int x, int y)
	{
		Rectangle bounds = grabber.getBounds();

		int dx = x - bounds.width / 2;
		int dy = y - bounds.height / 2;

		// Move to grabber position
		g2.translate(dx, dy);

		// Draw background
		g2.setColor(Color.WHITE);
		g2.fill(grabber);

		// Draw outline
		g2.setColor(Color.GRAY);
		g2.draw(grabber);

		// .. and move back
		g2.translate(-dx, -dy);
	}

	private void drawOutline(Graphics2D g2, Rectangle bounds)
	{
		final float dash[] = {5.0f};

		Stroke oldStroke = g2.getStroke();

		// Draw with a dashed line
		g2.setStroke(new BasicStroke(1.0f,
			BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER,
			5.0f, dash, 0.0f));

		g2.setColor(Color.GRAY);

		// Draw the outline of the whole selection
		g2.draw(bounds);

		// Roll back to the old stroke style
		g2.setStroke(oldStroke);
	}
}