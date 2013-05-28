import java.awt.Point;
import java.awt.Rectangle;

class GraphVertex extends Rectangle
{
	private String name;

	private GraphModel model;

	public GraphVertex()
	{
		super(10, 10, 160, 40);

		setName("[Unnamed]");
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
		notifyObservers();
	}

	public void setModel(GraphModel model)
	{
		this.model = model;
	}

	public Point getCenter()
	{
		return new Point((int) getCenterX(), (int) getCenterY());
	}

	@Override
	public void setLocation(int x, int y)
	{
		super.setLocation(x, y);
		notifyObservers();
	}

	@Override
	public int hashCode()
	{
		return super.hashCode() + 17 * name.hashCode();
	}

	@Override
	public boolean equals(Object other)
	{
		return super.equals(other) && name.equals(((GraphVertex) other).name);
	}

	private void notifyObservers()
	{
		if (model != null)
			model.notifyObservers(this);
	}
}
