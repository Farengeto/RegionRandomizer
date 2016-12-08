import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;

public class FileSelect implements ActionListener{
	private RegionGenerator source;
	private JFrame frame;
	private JFileChooser fc;
	
	public FileSelect(RegionGenerator src,JFrame f){
		source = src;
		frame = f;
		fc = new JFileChooser();
	}
	
	public void actionPerformed(ActionEvent e) {
		int returnVal = fc.showOpenDialog(frame);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File file = fc.getSelectedFile();
			source.setFile(file);
		}
	}
}