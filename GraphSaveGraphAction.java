import java.awt.event.ActionEvent;
import java.io.File;
import java.io.OutputStream;
import java.io.FileOutputStream;
import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

class GraphSaveGraphAction extends AbstractAction
{
	private GraphEditor controller;

	public GraphSaveGraphAction(GraphEditor controller)
	{
		super("Save As...");

		putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("meta S"));

		this.controller = controller;
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		JFileChooser chooser = new JFileChooser();
		int returnVal = chooser.showSaveDialog(controller.getWindow());

		if (returnVal != JFileChooser.APPROVE_OPTION)
			return;

		try {
			File file = chooser.getSelectedFile();
			OutputStream out = new FileOutputStream(file);

			// Write the model to the file
			controller.getModel().save(out);

			out.close();
		}
		catch (Exception error) {
			// Print complete error to terminal
			System.err.println(error);

			// And also show a nice popup to the user.
			JOptionPane.showMessageDialog(
				controller.getWindow(),
				error.getMessage(),
    			"Error while saving graph",
				JOptionPane.ERROR_MESSAGE);
		}
    }
}