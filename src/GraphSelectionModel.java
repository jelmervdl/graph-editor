import java.util.LinkedList;
import java.util.List;
import java.util.Observable;

class GraphSelectionModel extends Observable
{
	private LinkedList<GraphVertex> selection = new LinkedList<GraphVertex>();

	private boolean editable = false;

	public void setSelection(GraphVertex vertex)
	{
		if (hasSelection() && !isSelected(vertex))
			setChanged();
		
		selection.clear();

		if (vertex != null)
			selection.add(vertex);

		notifyObservers();
	}

	public void addSelected(GraphVertex vertex)
	{
		if (hasSelection() && !isSelected(vertex))
			setChanged();
		
		selection.add(vertex);

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
		if (hasSelection())
			setChanged();

		selection.clear();

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