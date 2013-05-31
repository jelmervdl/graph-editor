import java.awt.event.ActionEvent;
import java.io.File;
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.Observer;
import java.util.Observable;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JFrame;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.OverlayLayout;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.undo.UndoableEdit;


class GraphFrame extends JFrame
{
	private UndoableGraphModel model;

	private GraphSelectionModel selectionModel;

	private class UndoAction extends AbstractAction implements Observer
	{
		public UndoAction()
		{
			super("Undo");
			putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("meta Z"));
			model.addObserver(this);
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
			model.addObserver(this);
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
			chooser.setFileFilter(new FileNameExtensionFilter("Graph files", "graph"));

			int returnVal = chooser.showOpenDialog(GraphFrame.this);

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
				error.printStackTrace(System.err);

				// And also show a nice popup to the user.
				JOptionPane.showMessageDialog(
					GraphFrame.this,
					error.getMessage(),
	    			"Error while loading graph",
					JOptionPane.ERROR_MESSAGE);
			}
	    }
	}

	private class SaveGraphAction extends AbstractAction
	{
		public SaveGraphAction()
		{
			super("Save As...");
			putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("meta S"));
		}

		@Override
		public void actionPerformed(ActionEvent e)
		{
			JFileChooser chooser = new JFileChooser();
			chooser.setFileFilter(new FileNameExtensionFilter("Graph files", "graph"));

			int returnVal = chooser.showSaveDialog(GraphFrame.this);

			if (returnVal != JFileChooser.APPROVE_OPTION)
				return;

			try {
				File file = chooser.getSelectedFile();

				if (!file.exists()) // TODO: also test if there is no other extension given.
					file = new File(file + ".graph");

				OutputStream out = new FileOutputStream(file);

				// Write the model to the file
				model.save(out);

				out.close();
			}
			catch (Exception error) {
				// Print complete error to terminal
				System.err.println(error);

				// And also show a nice popup to the user.
				JOptionPane.showMessageDialog(
					GraphFrame.this,
					error.getMessage(),
					"Error while saving graph",
					JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	private class CloseGraphAction extends AbstractAction
	{
		public CloseGraphAction()
		{
			super("Close Graph");
			putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("meta W"));
		}

		@Override
		public void actionPerformed(ActionEvent e)
		{
			dispose();
		}
	}

	private class NewVertexAction extends AbstractAction
	{
		public NewVertexAction()
		{
			super("New Vertex");
			putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("control N"));
		}

		@Override
		public void actionPerformed(ActionEvent e)
		{
			model.addVertex(new GraphVertex());
		}
	}

	private class DeleteVertexAction extends AbstractAction implements Observer
	{
		public DeleteVertexAction()
		{
			super("Delete Vertex");

			putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("control D"));

			selectionModel.addObserver(this);

			// Update the state
			update(null, null);
		}

		@Override
		public void actionPerformed(ActionEvent e)
		{
			model.removeVertices(selectionModel.getSelected());
		}

		@Override
		public void update(Observable model, Object arg)
		{
			setEnabled(selectionModel.hasSelection());
		}
	}

	private class NewEdgeAction extends AbstractAction implements Observer
	{
		private GraphVertex fromVertex;

		public NewEdgeAction()
		{
			super("New Edge");

			putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("control alt N"));

			selectionModel.addObserver(this);

			// Update the state
			update(null, null);
		}

		@Override
		public void actionPerformed(ActionEvent e)
		{
			// If the user has already selected a bunch of vertices, add them all.
			if (selectionModel.getSelected().size() > 1)
				chainVertices();
			// Otherwise, use the old UI and remember the from-point till the user
			// selects a second vertice.
			else
				fromVertex = selectionModel.getSelection();
		}

		private void chainVertices()
		{
			Iterator<GraphVertex> it = selectionModel.getSelected().iterator();
			GraphVertex prev = it.next();

			while (it.hasNext())
			{
				GraphVertex current = it.next();

				model.addEdge(new GraphEdge(prev, current));

				prev = current;
			}
		}

		@Override
		public void update(Observable source, Object arg)
		{
			setEnabled(selectionModel.hasSelection());

			if (selectionModel.hasSelection()
				&& fromVertex != null
				&& fromVertex != selectionModel.getSelection())
			{
				GraphVertex toVertex = selectionModel.getSelection();
				model.addEdge(new GraphEdge(fromVertex, toVertex));
			}

			fromVertex = null;
		}
	}

	private class DeleteEdgeAction extends AbstractAction implements Observer
	{
		private GraphVertex fromVertex;

		public DeleteEdgeAction()
		{
			super("Delete Edge");
			putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("control alt D"));
			selectionModel.addObserver(this);

			// Update the state
			update(null, null);
		}

		@Override
		public void actionPerformed(ActionEvent e)
		{
			fromVertex = selectionModel.getSelection();
		}

		@Override
		public void update(Observable source, Object arg)
		{
			setEnabled(selectionModel.hasSelection());

			if (selectionModel.hasSelection()
				&& fromVertex != null
				&& fromVertex != selectionModel.getSelection())
			{
				GraphVertex toVertex = selectionModel.getSelection();
				model.removeEdge(new GraphEdge(fromVertex, toVertex));
			}
			
			fromVertex = null;
		}
	}

	class NameChangeListener extends AbstractAction implements Observer
	{
		public NameChangeListener()
		{
			super("Change name");
			putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("control r"));
			selectionModel.addObserver(this);
		}

		@Override
		public void actionPerformed(ActionEvent e)
		{
			selectionModel.getSelection().setName(e.getActionCommand());
			selectionModel.setEditable(false);
		}

		@Override
		public void update(Observable source, Object arg)
		{
			setEnabled(selectionModel.hasSelection());
		}
	} 

	public GraphFrame(GraphModel model)
	{
		super("Grafeneditor");

		this.model = new UndoableGraphModel(model);

		this.selectionModel = new GraphSelectionModel();

		GraphMouse selectionController = new GraphMouse();
		selectionController.setModel(model);
		selectionController.setSelectionModel(selectionModel);

		JPanel layers = new JPanel();
		layers.setLayout(new OverlayLayout(layers));

		// First, we have the edit layer which shows the change name textfield
		GraphNameChangePanel editLayer = new GraphNameChangePanel(selectionModel);
		editLayer.getTextField().addActionListener(new NameChangeListener());
		layers.add(editLayer);
		
		// Finally, we have the graph layer, which draws the actual graph
		GraphPanel graphLayer = new GraphPanel(model, selectionModel);
		graphLayer.addMouseListener(selectionController);
		graphLayer.addMouseMotionListener(selectionController);
		layers.add(graphLayer);

		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);		
		
		add(layers);

		setJMenuBar(buildMenuBar());
	}

	private JMenuBar buildMenuBar()
	{
		JMenuBar menuBar = new JMenuBar();

		// File menu
		menuBar.add(buildFileMenu());
		
		// Edit menu
		menuBar.add(buildEditMenu());

		return menuBar;
	}

	private JMenu buildFileMenu()
	{
		JMenu fileMenu = new JMenu("File");

		fileMenu.add(new NewGraphAction());
		fileMenu.add(new LoadGraphAction());

		fileMenu.addSeparator();
		
		fileMenu.add(new SaveGraphAction());
		
		fileMenu.addSeparator();
		
		fileMenu.add(new CloseGraphAction());

		return fileMenu;
	}

	private JMenu buildEditMenu()
	{
		JMenu editMenu = new JMenu("Edit");
		
		editMenu.add(new UndoAction());
		editMenu.add(new RedoAction());

		editMenu.addSeparator();
		
		editMenu.add(new NewVertexAction());
		editMenu.add(new DeleteVertexAction());
		
		editMenu.addSeparator();
		
		editMenu.add(new NewEdgeAction());
		editMenu.add(new DeleteEdgeAction());

		return editMenu;
	}
}