import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Graphics;
import java.util.Observable;
import javax.swing.Action;
import javax.swing.JTextField;

/**
 * EditableGraphPanel is a decorator for GraphPanel which
 * adds an JTextField over the editable GraphVertex.
 */
class EditableGraphPanel extends GraphPanel
{
	private JTextField textField;

	public EditableGraphPanel()
	{
		textField = new JTextField();
		textField.setVisible(false);
		add(textField);
	}

	public JTextField getTextField()
	{
		return textField;
	}

	/* GraphPanel */

	@Override
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

		super.update(model, arg);
	}
}