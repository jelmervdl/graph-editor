import java.awt.event.ActionEvent;
import java.io.File;
import java.io.InputStream;
import java.io.FileInputStream;
import java.util.Observer;
import java.util.Observable;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JFrame;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.undo.UndoableEdit;


class GraphFrame extends JFrame
{
	private UndoableGraphModel model;

	private GraphEditor controller;

	private UndoAction undoAction;

	private RedoAction redoAction;

	private class UndoAction extends AbstractAction implements Observer
	{
		public UndoAction()
		{
			super("Undo");
			putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("meta Z"));
		}

		public void actionPerformed(ActionEvent event)
		{
			if (model.getUndoManager().canUndo())
				model.getUndoManager().undo();
		}

		public void update(Observable source, Object arg)
		{
			putValue(NAME, model.getUndoManager().getUndoPresentationName());
			setEnabled(model.getUndoManager().canUndo());
		}
	}

	private class RedoAction extends AbstractAction implements Observer
	{
		public RedoAction()
		{
			super("Redo");
			putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("meta shift Z"));
		}

		public void actionPerformed(ActionEvent event)
		{
			if (model.getUndoManager().canRedo())
				model.getUndoManager().redo();
		}

		public void update(Observable source, Object arg)
		{
			putValue(NAME, model.getUndoManager().getRedoPresentationName());
			setEnabled(model.getUndoManager().canRedo());
		}
	}

	private class NewGraphAction extends AbstractAction
	{
		public NewGraphAction()
		{
			super("New Graph");

			putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("meta N"));
		}

		@Override
		public void actionPerformed(ActionEvent e)
		{
			Main.getSharedInstance().newEditor();
		}
	}

	private class LoadGraphAction extends AbstractAction
	{
		public LoadGraphAction()
		{
			super("Open...");

			putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("meta O"));
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
				if (model.isEmpty())
					model.load(in);
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

	public GraphFrame(GraphEditor controller)
	{
		super("Grafeneditor");
		this.controller = controller;

		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		// File menu
		menuBar.add(buildFileMenu());
		
		// Edit menu
		menuBar.add(buildEditMenu());
	}

	public void setModel(UndoableGraphModel model)
	{
		this.model = model;
		model.addObserver(undoAction);
		model.addObserver(redoAction);
	}

	private JMenu buildFileMenu()
	{
		JMenu fileMenu = new JMenu("File");

		fileMenu.add(new NewGraphAction());
		fileMenu.add(new LoadGraphAction());
		
		fileMenu.addSeparator();
		
		fileMenu.add(new GraphSaveGraphAction(controller));
		
		fileMenu.addSeparator();
		
		fileMenu.add(new GraphCloseGraphAction(controller));

		return fileMenu;
	}

	private JMenu buildEditMenu()
	{
		JMenu editMenu = new JMenu("Edit");
		
		// Undo
		undoAction = new UndoAction();
		editMenu.add(undoAction);

		// Redo
		redoAction = new RedoAction();
		editMenu.add(redoAction);

		editMenu.addSeparator();

		editMenu.add(new GraphNewVertexAction(controller));
		editMenu.add(new GraphDeleteVertexAction(controller));
		editMenu.addSeparator();
		editMenu.add(new GraphNewEdgeAction(controller));
		editMenu.add(new GraphDeleteEdgeAction(controller));

		return editMenu;
	}
}