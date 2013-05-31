import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.OverlayLayout;

class GraphEditor
{
	private GraphModel model;

	private GraphFrame window;

	public GraphEditor(GraphModel model)
	{
		this.model = model;

		window = new GraphFrame(model);

		window.setSize(400, 400);

		window.setVisible(true);
	}

	public GraphModel getModel()
	{
		return model;
	}

	public GraphFrame getWindow()
	{
		return window;
	}
}