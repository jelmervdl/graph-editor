import java.io.File;
import java.util.Observable;

class FileModel extends Observable
{
	private File file;

	public void setFile(File file)
	{
		this.file = file;

		setChanged();
		notifyObservers();
	}

	public File getFile()
	{
		return file;
	}

	public boolean hasFile()
	{
		return file != null;
	}
}