import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.KeyStroke;

class GraphNewGraphAction extends AbstractAction
{
	private GraphEditor controller;

	public GraphNewGraphAction(GraphEditor controller)
	{
		super("New Graph");

		putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("meta N"));

		this.controller = controller;
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		Main.getSharedInstance().newEditor();
	}
}