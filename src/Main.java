import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.UIManager;

class Main extends WindowAdapter
{
	private ArrayList<GraphEditor> editors;

	static private Main instance = new Main();

	private Main()
	{
		editors = new ArrayList<GraphEditor>();
	}

	public GraphEditor newEditor()
	{
		return newEditor(new GraphModel());
	}

	public GraphEditor newEditor(GraphModel model)
	{
		GraphEditor editor = new GraphEditor(model);
		
		// Listen for window closing
		editor.getWindow().addWindowListener(this);

		editors.add(editor);

		return editor;
	}

	/* WindowAdapter */

	// Note that we use windowClosed in stead of windowClosing. This
	// requires windows to be disposed in stead of just hidden.
	public void windowClosed(WindowEvent e)
	{
		// Remove the editor from the list of editors
		for (Iterator<GraphEditor> it = editors.iterator(); it.hasNext();)
			if (it.next().getWindow() == e.getWindow())
				it.remove();

		// If all windows are closed, quit the application. This is not
		// really the OS X way of doing things, but since we can't control
		// the menu anyway, it will do.
		if (editors.isEmpty())
			System.exit(0);
	}

	static public Main getSharedInstance()
	{
		return instance;
	}

	static public void main(String[] args)
	{
		try {
			System.setProperty("apple.laf.useScreenMenuBar", "true");
	    	System.setProperty("com.apple.mrj.application.apple.menu.about.name", "GrafenEditor");
	    	UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
	    }
	    catch (Exception e) {
	    	System.err.println("Could not set native look and feel: " + e);
	    }

		Main.getSharedInstance().newEditor();
	}
}