import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.util.Observable;
import java.util.Observer;
import javax.swing.JPanel;

class GraphPanel extends JPanel implements Observer
{
	protected GraphModel model;

	protected GraphSelectionModel selectionModel;

	private double scale;

	public GraphPanel(GraphModel model, GraphSelectionModel selectionModel)
	{
		scale = 1.0;

		this.model = model;
		model.addObserver(this);

		this.selectionModel = selectionModel;
		selectionModel.addObserver(this);

		setOpaque(false);	
	}

	public void setScale(double scale)
	{
		this.scale = scale;
		repaint();
	}

	public double getScale()
	{
		return scale;
	}

	private int scale(double value)
	{
		return (int) Math.ceil(value * scale);
	}

	/* Observer */

	@Override
	public void update(Observable model, Object arg) 
	{
		// Preferred size might have changed
		revalidate();

		// .. and we want to repaint
		repaint();
	}

	/* JPanel */

	@Override
	public Dimension getPreferredSize()
	{
		Rectangle2D bounds = new Rectangle();

		for (GraphVertex vertex : model.getVertices())
			bounds = bounds.createUnion(vertex.getBounds());

		return new Dimension(
			scale(bounds.getX() + bounds.getWidth()),
			scale(bounds.getY() + bounds.getHeight()));
	}

	@Override
	protected void paintComponent(Graphics g)
	{
		super.paintComponent(g);

		Graphics2D g2 = (Graphics2D) g;

		// Hint to use antialiasing when drawing lines
		g2.setRenderingHint(
			RenderingHints.KEY_ANTIALIASING, 
			RenderingHints.VALUE_ANTIALIAS_ON);

		// Set scale transformation
		AffineTransform t = new AffineTransform(g2.getTransform());
		t.scale(scale, scale);
		g2.setTransform(t);

		for (GraphEdge edge : model.getEdges())
			drawEdge(g, edge);

		for (GraphVertex vertex : model.getVertices())
			drawVertex(g, vertex);
	}

	private void drawEdge(Graphics g, GraphEdge edge)
	{
		Point from = edge.getFromPoint(),
			  to = edge.getToPoint();

		g.drawLine(from.x, from.y, to.x, to.y);
	}

	private void drawVertex(Graphics g, GraphVertex vertex)
	{
		drawVertexShape(g, vertex);

		drawVertexText(g, vertex);
	}

	private void drawVertexShape(Graphics g, GraphVertex vertex)
	{
		g.setColor(selectionModel.isSelected(vertex)
			? vertex.getColor().darker()
			: vertex.getColor());

		g.fillRoundRect(
			vertex.getLocation().x, vertex.getLocation().y, 
			vertex.getSize().width, vertex.getSize().height,
			5, 5);

		g.setColor(Color.BLACK);

		g.drawRoundRect(
			vertex.getLocation().x, vertex.getLocation().y, 
			vertex.getSize().width, vertex.getSize().height,
			5, 5);
	}

	private void drawVertexText(Graphics g, GraphVertex vertex)
	{
		FontMetrics f = g.getFontMetrics();
		Rectangle2D box = f.getStringBounds(vertex.getName(), g);

		g.drawString(vertex.getName(),
			vertex.getLocation().x + (int) ((vertex.getSize().width - box.getWidth()) / 2),
			vertex.getLocation().y + (int) ((vertex.getSize().height - box.getHeight()) / 2 + f.getAscent()));
	}
}