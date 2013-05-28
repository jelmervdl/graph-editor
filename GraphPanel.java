import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.geom.Rectangle2D;
import java.util.Observable;
import java.util.Observer;
import javax.swing.JPanel;

class GraphPanel extends JPanel implements Observer
{
	protected GraphModel model;

	protected GraphSelectionModel selectionModel;

	public void setModel(GraphModel model)
	{
		// Stop listening to the previous model
		if (this.model != null)
			this.model.deleteObserver(this);
		
		// Start Listening to the new model
		this.model = model;
		this.model.addObserver(this);
	}

	public void setSelectionModel(GraphSelectionModel model)
	{
		if (this.selectionModel != null)
			this.selectionModel.deleteObserver(this);

		this.selectionModel = model;
		this.selectionModel.addObserver(this);
	}

	/* Observer */

	@Override
	public void update(Observable model, Object arg) 
	{
		repaint();
	}

	/* JPanel */

	@Override
	protected void paintComponent(Graphics g)
	{
		super.paintComponent(g);

		for (GraphEdge edge : model.getEdges())
			drawEdge(g, edge);

		for (GraphVertex vertex : model.getVertices())
			drawVertex(g, vertex);
	}

	private void drawEdge(Graphics g, GraphEdge edge)
	{
		Point from = edge.getFromPoint(),
			  to = edge.getToPoint();

		g.drawLine(
			map(from.getX()), map(from.getY()), 
			map(to.getX()), map(to.getY()));
	}

	private void drawVertex(Graphics g, GraphVertex vertex)
	{
		drawVertexShape(g, vertex);

		drawVertexText(g, vertex);
	}

	private void drawVertexShape(Graphics g, GraphVertex vertex)
	{
		g.setColor(selectionModel.isSelected(vertex)
			? Color.GRAY
			: Color.WHITE);

		g.fillRoundRect(
			map(vertex.getX()), map(vertex.getY()), 
			map(vertex.getWidth()), map(vertex.getHeight()),
			5, 5);

		g.setColor(Color.BLACK);

		g.drawRoundRect(
			map(vertex.getX()), map(vertex.getY()), 
			map(vertex.getWidth()), map(vertex.getHeight()),
			5, 5);
	}

	private void drawVertexText(Graphics g, GraphVertex vertex)
	{
		FontMetrics f = g.getFontMetrics();
		Rectangle2D box = f.getStringBounds(vertex.getName(), g);

		g.drawString(vertex.getName(),
			map(vertex.getX()) + (map(vertex.getWidth()) - (int) box.getWidth()) / 2,
			map(vertex.getY()) + (map(vertex.getHeight()) - (int) box.getHeight()) / 2 + f.getAscent());
	}

	private int map(double p)
	{
		return (int) p;
	}
}