import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.KeyStroke;

class GraphCloseGraphAction extends AbstractAction
{
	private GraphEditor controller;

	public GraphCloseGraphAction(GraphEditor controller)
	{
		super("Close Graph");

		putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("meta W"));

		this.controller = controller;
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		controller.getWindow().dispose();
	}
}