import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.Color;
import java.awt.Graphics;
import java.util.HashSet;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;
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

	private Set<ChangeListener> listeners;

	public interface ChangeListener
	{
		public void changeAccepted(GraphVertex vertex, String name);

		public void changeCancelled(GraphVertex vertex);
	}

	private class TextFieldController extends KeyAdapter
	{
		@Override
		public void keyPressed(KeyEvent e)
		{
			switch (e.getKeyCode())
			{
				case KeyEvent.VK_ESCAPE:
					for (ChangeListener listener : listeners)
						listener.changeCancelled(selectionModel.getSelection());
					
					e.consume();
					break;

				case KeyEvent.VK_ENTER:
					for (ChangeListener listener : listeners)
						listener.changeAccepted(selectionModel.getSelection(), textField.getText());

					e.consume();
					break;
			}
		}
	}

	public GraphNameChangePanel(GraphSelectionModel selectionModel)
	{
		this.selectionModel = selectionModel;
		selectionModel.addObserver(this);

		textField = new JTextField();
		textField.setVisible(false);
		textField.addKeyListener(new TextFieldController());
		add(textField);

		listeners = new HashSet<ChangeListener>();

		// This panel is mostly transparent
		setOpaque(false);
	}

	public void addChangeListener(ChangeListener listener)
	{
		listeners.add(listener);
	}

	public void removeChangeListener(ChangeListener listener)
	{
		listeners.remove(listener);
	}

	/* Observer */

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
			// Make text field visible with current text
			textField.setVisible(true);
			textField.setText(selectionModel.getSelection().getName());

			// .. and focus it.
			textField.requestFocus();
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