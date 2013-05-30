import java.awt.Point;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Shape;
import java.util.Observable;

class GraphVertex extends Observable
{
	private String name;

	private Point location;

	private Dimension size;

	public GraphVertex()
	{
		location = new Point(10, 10);

		size = new Dimension(160, 40);

		setName("[Unnamed]");
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
		
		setChanged();
		notifyObservers();
	}

	public Point getCenter()
	{
		return new Point(location.x + size.width / 2, location.y + size.height / 2);
	}

	public void setLocation(int x, int y)
	{
		location = new Point(x, y);
		
		setChanged();
		notifyObservers();
	}

	public Point getLocation()
	{
		return location;
	}

	public void setSize(int w, int h)
	{
		size = new Dimension(w, h);
	}

	public Dimension getSize()
	{
		return size;
	}

	protected Shape getShape()
	{
		return new Rectangle(getLocation(), getSize());
	}

	public boolean contains(Point p)
	{
		return getShape().contains(p);
	}

	public Rectangle getBounds()
	{
		return getShape().getBounds();
	}

	/*
	@Override
	public int hashCode()
	{
		return location.hashCode()
			 + 17 * size.hashCode()
			 + 34 * name.hashCode();
	}

	@Override
	public boolean equals(Object other)
	{
		return location.equals(other.location)
			&& size.equals(other.size)
			&& name.equals(((GraphVertex) other).name);
	}
	*/
}
