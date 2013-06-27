import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Point;

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

		GraphPanel source = (GraphPanel) e.getSource();
		
		Point scaledPoint = new Point(
			(int) (e.getX() / source.getScale()),
			(int) (e.getY() / source.getScale()));

		GraphVertex selectedVertex = model.getVertexAtPoint(scaledPoint);

		if (e.isMetaDown() && selectedVertex != null)
			selectionModel.addSelected(selectedVertex);
		else
			selectionModel.setSelection(selectedVertex); // May be null

		if (selectedVertex != null) {
			mouseOffsetX = (int) scaledPoint.getX() - selectionModel.getSelection().getLocation().x;
			mouseOffsetY = (int) scaledPoint.getY() - selectionModel.getSelection().getLocation().y;
		}
	}	

	@Override
	public void mouseDragged(MouseEvent e)
	{
		GraphPanel source = (GraphPanel) e.getSource();
		
		Point scaledPoint = new Point(
			(int) (e.getX() / source.getScale()),
			(int) (e.getY() / source.getScale()));

		if (selectionModel.hasSelection())
			selectionModel.getSelection().setLocation(
				(int) scaledPoint.getX() - mouseOffsetX,
				(int) scaledPoint.getY() - mouseOffsetY);
	}
}