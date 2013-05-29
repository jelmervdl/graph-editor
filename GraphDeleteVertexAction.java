import java.awt.event.ActionEvent;
import java.util.Observer;
import java.util.Observable;
import javax.swing.AbstractAction;
import javax.swing.KeyStroke;

class GraphDeleteVertexAction extends AbstractAction implements Observer
{
	private GraphEditor controller;

	public GraphDeleteVertexAction(GraphEditor controller)
	{
		super("Delete Vertex");

		putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("control D"));

		this.controller = controller;

		this.controller.getSelectionModel().addObserver(this);

		// Update the state
		update(null, null);
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		controller.getModel().removeVertices(controller.getSelectionModel().getSelected());
	}

	@Override
	public void update(Observable model, Object arg)
	{
		setEnabled(controller.getSelectionModel().hasSelection());
	}
}