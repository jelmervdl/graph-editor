import java.awt.event.ActionEvent;
import java.io.File;
import java.io.InputStream;
import java.io.FileInputStream;
import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

class GraphLoadGraphAction extends AbstractAction
{
	private GraphEditor controller;

	public GraphLoadGraphAction(GraphEditor controller)
	{
		super("Open...");

		putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("meta O"));

		this.controller = controller;
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		JFileChooser chooser = new JFileChooser();
		int returnVal = chooser.showOpenDialog(controller.getWindow());

		if (returnVal != JFileChooser.APPROVE_OPTION)
			return;

		try {
			File file = chooser.getSelectedFile();
			InputStream in = new FileInputStream(file);

			// If the current model is empty, load it in this one
			if (controller.getModel().isEmpty())
				controller.getModel().load(in);
			// Otherwise, open a new editor and load it in that one.
			else
				Main.getSharedInstance().newEditor().getModel().load(in);

			in.close();
		}
		catch (Exception error) {
			// Print complete error to terminal
			System.err.println(error);

			// And also show a nice popup to the user.
			JOptionPane.showMessageDialog(
				controller.getWindow(),
				error.getMessage(),
    			"Error while loading graph",
				JOptionPane.ERROR_MESSAGE);
		}
    }
}