import java.awt.Point;

class GraphEdge 
{
	private GraphVertex from;

	private GraphVertex to;

	public GraphEdge(GraphVertex from, GraphVertex to)
	{
		this.from = from;

		this.to = to;
	}

	public GraphVertex getFrom()
	{
		return from;
	}

	public GraphVertex getTo()
	{
		return to;
	}

	public Point getFromPoint()
	{
		return from.getCenter();
	}

	public Point getToPoint()
	{
		return to.getCenter();
	}

	public boolean connectsTo(GraphVertex vertex)
	{
		return from == vertex || to == vertex;
	}

	@Override
	public int hashCode()
	{
		return from.hashCode() * 17 + to.hashCode();
	}

	@Override
	public boolean equals(Object other)
	{
		if (this == other)
			return true;

		if (!(other instanceof GraphEdge))
			return false;

		GraphEdge edge = (GraphEdge) other;

		return from.equals(edge.from)
			&& to.equals(edge.to);
	}
}