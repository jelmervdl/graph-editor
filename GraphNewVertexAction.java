import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.KeyStroke;

class GraphNewVertexAction extends AbstractAction
{
	private GraphEditor controller;

	public GraphNewVertexAction(GraphEditor controller)
	{
		super("New Vertex");

		putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("control N"));

		this.controller = controller;
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		controller.getModel().addVertex(new GraphVertex());
	}
}