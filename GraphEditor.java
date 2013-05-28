import javax.swing.JFrame;

class GraphEditor
{
	private UndoableGraphModel model;

	private GraphFrame window;

	private EditableGraphPanel panel;

	private GraphSelectionModel selectionModel;

	private GraphMouse selectionController;

	public GraphEditor()
	{
		selectionModel = new GraphSelectionModel();

		selectionController = new GraphMouse();
		selectionController.setSelectionModel(selectionModel);

		panel = new EditableGraphPanel();

		panel.getTextField().addActionListener(new GraphNameChangeListener(this));

		panel.setSelectionModel(selectionModel);

		// For the mousePressed notifications
		panel.addMouseListener(selectionController);

		// .. and for the mouseDragged notifications
		panel.addMouseMotionListener(selectionController);

		window = new GraphFrame(this);
		window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		window.add(panel);

		setModel(new UndoableGraphModel());

		window.setSize(400, 400);

		window.setVisible(true);
	}

	public void setModel(UndoableGraphModel model)
	{
		selectionModel.clearSelection();

		this.model = model;

		window.setModel(model);

		panel.setModel(model);

		selectionController.setModel(model);
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