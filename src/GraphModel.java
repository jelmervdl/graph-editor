import java.awt.Point;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Observable;
import java.util.Scanner;
import java.util.Observer;

class GraphModel extends Observable implements Observer
{
	private List<GraphVertex> vertices;

	private List<GraphEdge> edges;

	private boolean dirty;

	public GraphModel()
	{
		vertices = new ArrayList<GraphVertex>();

		edges = new ArrayList<GraphEdge>();

		dirty = false;
	}

	/* Vertex */

	public void addVertex(GraphVertex vertex)
	{
		vertices.add(vertex);
		vertex.addObserver(this);

		dirty = true;

		setChanged();
		notifyObservers();
	}

	public void removeVertex(GraphVertex vertex)
	{
		vertices.remove(vertex);
		vertex.deleteObserver(this);

		List<GraphEdge> edgesToRemove = new ArrayList<GraphEdge>();

		for (GraphEdge edge : edges)
			if (edge.connectsTo(vertex))
				edgesToRemove.add(edge);
		
		for (GraphEdge edge : edgesToRemove)
			removeEdge(edge);

		dirty = true;

		setChanged();
		notifyObservers();
	}

	public void removeVertices(List<GraphVertex> vertices)
	{
		for (GraphVertex vertex : vertices)
			removeVertex(vertex);
	}

	public List<GraphVertex> getVertices()
	{
		return vertices;
	}

	public GraphVertex getVertexAtPoint(Point p)
	{
		for (int i = vertices.size() - 1; i >= 0; --i)
			if (vertices.get(i).contains(p))
				return vertices.get(i);

		return null;
	}

	/* Edges */

	public void addEdge(GraphEdge edge)
	{
		edges.add(edge);
		dirty = true;

		setChanged();
		notifyObservers();
	}

	public void removeEdge(GraphEdge edge)
	{
		edges.remove(edge);
		dirty = true;

		setChanged();
		notifyObservers();
	}

	public List<GraphEdge> getEdges()
	{
		return edges;
	}

	/* General */

	public boolean isEmpty()
	{
		return vertices.isEmpty();
	}

	public boolean isDirty()
	{
		return dirty;
	}

	/* Observer */

	public void update(Observable subject, Object arg)
	{
		dirty = true;
		
		setChanged();
		notifyObservers();
	}

	/* Save & Load */

	public void save(OutputStream out)
	{
		PrintWriter pout = new PrintWriter(out);

		// First line: sizes
		pout.format("%d %d%n", vertices.size(), edges.size());

		// Following lines: every vertice (x, y, width, height, label)
		for (GraphVertex vertex : vertices)
			pout.format("%d %d %d %d %s%n",
				vertex.getLocation().x, vertex.getLocation().y,
				vertex.getSize().width, vertex.getSize().height,
				vertex.getName());

		// Following lines: edges (from index, to index)
		for (GraphEdge edge : edges)
			pout.format("%d %d%n",
				vertices.indexOf(edge.getFrom()),
				vertices.indexOf(edge.getTo()));

		pout.flush();

		dirty = false;

		setChanged();
		notifyObservers();
	}

	public void load(InputStream in)
	{
		Scanner sc = new Scanner(in);

		// Start clean
		vertices.clear();
		edges.clear();

		// Read the first line
		int n_vertices = sc.nextInt();
		int n_edges = sc.nextInt();

		for (int i = 0; i < n_vertices; ++i)
		{
			GraphVertex vertex = new GraphVertex();
			vertex.setLocation(sc.nextInt(), sc.nextInt());
			vertex.setSize(sc.nextInt(), sc.nextInt());
			vertex.setName(sc.nextLine());
			addVertex(vertex);
		}

		for (int i = 0; i < n_edges; ++i)
		{
			GraphEdge edge = new GraphEdge(
				vertices.get(sc.nextInt()),
				vertices.get(sc.nextInt()));
			addEdge(edge);
		}

		dirty = false;

		setChanged();
		notifyObservers();
	}
}