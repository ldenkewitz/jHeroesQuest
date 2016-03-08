package de.ldenkewi.heroesquest.view;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

/**
 * The class is used as a {@link javax.swing.JFileChooser JFileChooser} with its own {@link javax.swing.filechooser.FileFilter FileFilter}. <BR>
 * @author Lars Denkewitz
 * @version from 14/04/2009
 */
public class MyFileChooserUI {
	
	private JFileChooser fc;
	private int dialogType;

	public MyFileChooserUI(String folder, int dialogType) {
		this.dialogType = dialogType;
		fc = new JFileChooser(folder);
	}

	public File getFile() {
		fc.setAcceptAllFileFilterUsed(false);
		fc.setDialogTitle("Karte auswählen");
		fc.setDialogType(dialogType);
		fc.setFileFilter(new MyFileFilter());
		fc.setVisible(true);
		int state;
		
		if(dialogType == JFileChooser.SAVE_DIALOG)
			state = fc.showSaveDialog(null);
		else
			state = fc.showOpenDialog(null);
		
		if (state == JFileChooser.APPROVE_OPTION) {
			File file = fc.getSelectedFile();
			return file;
		} else {
			return null;
		}
	}
	
	/**
	 * Class extends FileFilter and provides a filter of file extensions for {@link MyFileChooserUI}.
	*/
	private static class MyFileFilter extends FileFilter {
		private String[] extensions = { "xml" };

		/** {@inheritDoc} */
		@Override
		public boolean accept(File f) {
			for (String extension : extensions) {
				String str = f.getName().toLowerCase();
				if (str.endsWith("." + extension) || (f.isDirectory() & !str.startsWith(".")))
					return true;
			}
			return false;
		}

		/** {@inheritDoc} */
		@Override
		public String getDescription() {
			return "MAP(*.xml)";
		}

		/** {@inheritDoc} */
		@Override
		public String toString() {
			return extensions.toString();
		}
	}
}
