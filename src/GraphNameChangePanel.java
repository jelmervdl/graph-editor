import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Color;
import java.awt.Graphics;
import java.util.Observable;
import java.util.Observer;
import javax.swing.Action;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.OverlayLayout;

/**
 * EditableGraphPanel is a decorator for GraphPanel which
 * adds an JTextField over the editable GraphVertex.
 */
class GraphNameChangePanel extends JPanel implements Observer
{
	private JTextField textField;

	private GraphSelectionModel selectionModel;

	public GraphNameChangePanel(GraphSelectionModel selectionModel)
	{
		this.selectionModel = selectionModel;
		selectionModel.addObserver(this);

		textField = new JTextField();
		textField.setVisible(false);
		add(textField);

		// This panel is mostly transparent
		setOpaque(false);
	}

	public JTextField getTextField()
	{
		return textField;
	}

	public void update(Observable model, Object arg)
	{
		// isEditable changed to false
		if (textField.isVisible() && !selectionModel.isEditable())
		{
			textField.setVisible(false);
		}

		// isEditable changed to true
		else if (!textField.isVisible() && selectionModel.isEditable())
		{
			textField.setVisible(true);
			textField.setText(selectionModel.getSelection().getName());
			textField.selectAll();
		}

		// We're still editable, we just need to update our position
		if (textField.isVisible())
		{
			GraphVertex vertex = selectionModel.getSelection();
			textField.setLocation(vertex.getLocation());
			textField.setSize(vertex.getSize());
		}
	}
}