import java.awt.Point;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Observable;
import java.util.Scanner;
import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.CompoundEdit;
import javax.swing.undo.UndoableEdit;
import javax.swing.undo.UndoManager;

class UndoableGraphModel extends GraphModel
{
	private UndoableEdit undoManager;

	private class AddVertexEdit extends AbstractUndoableEdit
	{
		private GraphVertex vertex;

		public AddVertexEdit(GraphVertex vertex)
		{
			this.vertex = vertex;
		}

		public void undo() throws CannotUndoException
		{
			super.undo();
			UndoableGraphModel.super.removeVertex(vertex);
		}
		
		public void redo() throws CannotRedoException
		{
			super.redo();
			UndoableGraphModel.super.addVertex(vertex);
		}
		
		public String getPresentationName()
		{
			return "Add Vertex";
		}
	}

	private class RemoveVertexEdit extends AbstractUndoableEdit
	{
		private GraphVertex vertex;

		public RemoveVertexEdit(GraphVertex vertex)
		{
			this.vertex = vertex;
		}

		public void undo() throws CannotUndoException
		{
			super.undo();
			UndoableGraphModel.super.addVertex(vertex);
		}
		
		public void redo() throws CannotRedoException
		{
			super.redo();
			UndoableGraphModel.super.removeVertex(vertex);
		}
		
		public String getPresentationName()
		{
			return "Remove Vertex";
		}
	}

	private class AddEdgeEdit extends AbstractUndoableEdit
	{
		private GraphEdge edge;

		public AddEdgeEdit(GraphEdge edge)
		{
			this.edge = edge;
		}

		public void undo() throws CannotUndoException
		{
			super.undo();
			UndoableGraphModel.super.removeEdge(edge);
		}
		
		public void redo() throws CannotRedoException
		{
			super.redo();
			UndoableGraphModel.super.addEdge(edge);
		}
		
		public String getPresentationName()
		{
			return "Add Edge";
		}
	}

	private class RemoveEdgeEdit extends AbstractUndoableEdit
	{
		private GraphEdge edge;

		public RemoveEdgeEdit(GraphEdge edge)
		{
			this.edge = edge;
		}

		public void undo() throws CannotUndoException
		{
			super.undo();
			UndoableGraphModel.super.addEdge(edge);
		}
		
		public void redo() throws CannotRedoException
		{
			super.redo();
			UndoableGraphModel.super.removeEdge(edge);
		}
		
		public String getPresentationName()
		{
			return "Remove Edge";
		}
	}

	public UndoableGraphModel()
	{
		undoManager = new UndoManager();
	}

	/* Getters and Setters */

	public UndoableEdit getUndoManager()
	{
		return (UndoableEdit) undoManager;
	}

	/* Vertex */

	@Override
	public void addVertex(GraphVertex vertex)
	{
		undoManager.addEdit(new AddVertexEdit(vertex));
		super.addVertex(vertex);
	}

	@Override
	public void removeVertex(GraphVertex vertex)
	{
		UndoableEdit parent = undoManager;
		
		CompoundEdit edit = new CompoundEdit();

		// Make the edit the undo manager
		undoManager = edit;

		// Remove the vertex
		// This will also call this.removeEdge a few times and their
		// edits will be added to the compound edit.
		super.removeVertex(vertex);

		// Finally, add the comput edit.
		edit.addEdit(new RemoveVertexEdit(vertex));
		
		
		// And add the edit as an undoable edit to the old undo manager.
		edit.end();
		parent.addEdit(edit);

		// Restore original undo manager
		undoManager = parent;
	}

	@Override
	public void removeVertices(List<GraphVertex> vertices)
	{
		UndoableEdit parent = undoManager;

		CompoundEdit edit = new CompoundEdit();

		undoManager = edit;

		super.removeVertices(vertices);

		parent.addEdit(edit);
		
		undoManager = parent;
	}

	/* Edges */

	@Override
	public void addEdge(GraphEdge edge)
	{
		undoManager.addEdit(new AddEdgeEdit(edge));
		super.addEdge(edge);
	}

	@Override
	public void removeEdge(GraphEdge edge)
	{
		undoManager.addEdit(new RemoveEdgeEdit(edge));
		super.removeEdge(edge);
	}
}