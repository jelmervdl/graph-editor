import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.KeyStroke;

class GraphNameChangeListener extends AbstractAction
{
	private GraphEditor controller;

	public GraphNameChangeListener(GraphEditor controller)
	{
		super("Change name");

		putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("control r"));

		this.controller = controller;
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		GraphSelectionModel selection = controller.getSelectionModel();

		GraphVertex vertex = selection.getSelection();
		vertex.setName(e.getActionCommand());

		selection.setEditable(false);
	}
}