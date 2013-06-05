import java.awt.Point;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.CompoundEdit;
import javax.swing.undo.UndoableEdit;
import javax.swing.undo.UndoManager;

/**
 * GraphModel decorator which adds undo-capability to
 * the GraphModel using an UndoManager.
 */
abstract class GraphModelDecorator extends GraphModel
{
	private GraphModel model;

	public GraphModelDecorator(GraphModel delegate)
	{
		model = delegate;
	}

	/* Vertex */

	@Override
	public void addVertex(GraphVertex vertex)
	{
		model.addVertex(vertex);
	}

	@Override
	public void removeVertex(GraphVertex vertex)
	{
		model.removeVertex(vertex);
	}

	@Override
	public void removeVertices(List<GraphVertex> vertices)
	{
		model.removeVertices(vertices);
	}

	@Override
	public List<GraphVertex> getVertices()
	{
		return model.getVertices();
	}

	@Override
	public GraphVertex getVertexAtPoint(Point p)
	{
		return model.getVertexAtPoint(p);
	}

	/* Edges */

	@Override
	public void addEdge(GraphEdge edge)
	{
		model.addEdge(edge);
	}

	@Override
	public void removeEdge(GraphEdge edge)
	{
		model.removeEdge(edge);
	}

	@Override
	public List<GraphEdge> getEdges()
	{
		return model.getEdges();
	}

	/* General */

	@Override
	public boolean isEmpty()
	{
		return model.isEmpty();
	}

	@Override
	public boolean isDirty()
	{
		return model.isDirty();
	}

	/* Observer */

	@Override
	public void update(Observable subject, Object arg)
	{
		model.update(subject, arg);
	}

	@Override
	public void addObserver(Observer observer)
	{
		model.addObserver(observer);
	}

	@Override
	public void deleteObserver(Observer observer)
	{
		model.deleteObserver(observer);
	}

	@Override
	public void notifyObservers()
	{
		model.notifyObservers();
	}

	/* Save & Load */

	@Override
	public void save(OutputStream out)
	{
		model.save(out);
	}

	@Override
	public void load(InputStream in)
	{
		model.load(in);
	}


}