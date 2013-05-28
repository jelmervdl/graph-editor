import java.awt.event.ActionEvent;
import java.util.Observer;
import java.util.Observable;
import javax.swing.AbstractAction;
import javax.swing.KeyStroke;

class GraphDeleteEdgeAction extends AbstractAction implements Observer
{
	private GraphEditor controller;

	private GraphVertex fromVertex;

	public GraphDeleteEdgeAction(GraphEditor controller)
	{
		super("Delete Edge");

		putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("control alt D"));

		this.controller = controller;

		this.controller.getSelectionModel().addObserver(this);

		// Update the state
		update(null, null);
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		fromVertex = controller.getSelectionModel().getSelection();
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
			controller.getModel().removeEdge(new GraphEdge(fromVertex, toVertex));
		}
		
		fromVertex = null;
	}
}