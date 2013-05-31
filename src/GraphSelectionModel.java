import java.util.LinkedList;
import java.util.List;
import java.util.Observable;

class GraphSelectionModel extends Observable
{
	private LinkedList<GraphVertex> selection = new LinkedList<GraphVertex>();

	private boolean editable = false;

	public void setSelection(GraphVertex vertex)
	{
		selection.clear();

		if (vertex != null)
			selection.add(vertex);

		setChanged();
		notifyObservers();
	}

	public void addSelected(GraphVertex vertex)
	{
		if (isSelected(vertex))
			return;
		
		selection.add(vertex);
		setChanged();
		notifyObservers();
	}

	public GraphVertex getSelection()
	{
		return selection.getLast();
	}

	public List<GraphVertex> getSelected()
	{
		return selection;
	}

	public boolean hasSelection()
	{
		return !selection.isEmpty();
	}

	public boolean isSelected(GraphVertex vertex)
	{
		return selection.contains(vertex);
	}

	public void clearSelection()
	{
		if (selection.isEmpty())
			return;
		
		selection.clear();
		setChanged();
		notifyObservers();
	}

	public void setEditable(boolean editable)
	{
		if (this.editable != editable)
			setChanged();

		this.editable = editable;

		notifyObservers();
	}

	public boolean isEditable()
	{
		return hasSelection() && editable;
	}
}