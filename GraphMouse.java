import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

class GraphMouse extends MouseAdapter
{
	private GraphModel model;

	private GraphSelectionModel selectionModel;

	private int mouseOffsetX;

	private int mouseOffsetY;

	public void setSelectionModel(GraphSelectionModel selectionModel)
	{
		this.selectionModel = selectionModel;
	}

	public void setModel(GraphModel model)
	{
		this.model = model;
	}

	@Override
	public void mouseClicked(MouseEvent e)
	{
		selectionModel.setEditable(e.getClickCount() == 2);
	}

	@Override
	public void mousePressed(MouseEvent e)
	{
		if (model == null)
			return;

		GraphVertex selectedVertex = model.getVertexAtPoint(e.getPoint());
		
		if (e.isMetaDown() && selectedVertex != null)
			selectionModel.addSelected(selectedVertex);
		else
			selectionModel.setSelection(selectedVertex); // May be null

		if (selectedVertex != null) {
			mouseOffsetX = e.getX() - selectionModel.getSelection().getLocation().x;
			mouseOffsetY = e.getY() - selectionModel.getSelection().getLocation().y;
		}
	}	

	@Override
	public void mouseDragged(MouseEvent e)
	{
		if (selectionModel.hasSelection())
			selectionModel.getSelection().setLocation(
				e.getX() - mouseOffsetX,
				e.getY() - mouseOffsetY);
	}
}