import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.OverlayLayout;

class GraphEditor
{
	private UndoableGraphModel model;

	private GraphFrame window;

	private GraphSelectionModel selectionModel;

	private GraphMouse selectionController;

	public GraphEditor(UndoableGraphModel model)
	{
		this.model = model;

		selectionModel = new GraphSelectionModel();

		selectionController = new GraphMouse();
		selectionController.setModel(model);
		selectionController.setSelectionModel(selectionModel);

		JPanel layers = new JPanel();
		layers.setLayout(new OverlayLayout(layers));

		// First, we have the edit layer which shows the change name textfield
		GraphNameChangePanel editLayer = new GraphNameChangePanel(selectionModel);
		editLayer.getTextField().addActionListener(new GraphNameChangeListener(this));
		layers.add(editLayer);
		
		// Finally, we have the graph layer, which draws the actual graph
		GraphPanel graphLayer = new GraphPanel();
		graphLayer.setModel(model);
		graphLayer.setSelectionModel(selectionModel);
		
		graphLayer.addMouseListener(selectionController);
		graphLayer.addMouseMotionListener(selectionController);
		layers.add(graphLayer);

		window = new GraphFrame(this);
		window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		window.add(layers);

		window.setSize(400, 400);

		window.setVisible(true);
	}

	public GraphModel getModel()
	{
		return model;
	}

	public GraphSelectionModel getSelectionModel()
	{
		return selectionModel;
	}

	public GraphFrame getWindow()
	{
		return window;
	}
}