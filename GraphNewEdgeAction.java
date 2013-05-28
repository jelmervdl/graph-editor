import java.awt.event.ActionEvent;
import java.util.Iterator;
import java.util.Observer;
import java.util.Observable;
import javax.swing.AbstractAction;
import javax.swing.KeyStroke;

class GraphNewEdgeAction extends AbstractAction implements Observer
{
	private GraphEditor controller;

	private GraphVertex fromVertex;

	public GraphNewEdgeAction(GraphEditor controller)
	{
		super("New Edge");

		putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("control alt N"));

		this.controller = controller;

		this.controller.getSelectionModel().addObserver(this);

		// Update the state
		update(null, null);
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		// If the user has already selected a bunch of vertices, add them all.
		if (controller.getSelectionModel().getSelected().size() > 1)
			chainVertices();
		// Otherwise, use the old UI and remember the from-point till the user
		// selects a second vertice.
		else
			fromVertex = controller.getSelectionModel().getSelection();
	}

	private void chainVertices()
	{
		Iterator<GraphVertex> it = controller.getSelectionModel().getSelected().iterator();
		GraphVertex prev = it.next();

		while (it.hasNext())
		{
			GraphVertex current = it.next();

			controller.getModel().addEdge(new GraphEdge(prev, current));

			prev = current;
		}
	}

	@Override
	public void update(Observable model, Object arg)
	{
		setEnabled(controller.getSelectionModel().hasSelection());

		if (controller.getSelectionModel().hasSelection()
			&& fromVertex != null
			&& fromVertex != controller.getSelectionModel().getSelection())
		{
			GraphVertex toVertex = controller.getSelectionModel().getSelection();
			controller.getModel().addEdge(new GraphEdge(fromVertex, toVertex));
		}

		fromVertex = null;
	}
}