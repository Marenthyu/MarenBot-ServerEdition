package gui.visualdisplay;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.filechooser.FileFilter;


@SuppressWarnings("serial")
public class VisualDisplayHUB extends JFrame {

	public VisualDisplayHUB() {
		JFileChooser fc = new JFileChooser();
		
		fc.setFileFilter(new FileFilter(){

			@Override
			public boolean accept(File f) {
				  if (f.isDirectory()) {
				        return true;
				    }

				if (getExtension(f).equals("png")) {
					return true;
				}
				return false;
			}

			@Override
			public String getDescription() {
				// TODO Auto-generated method stub
				return ".png";
			}
			
		});
		
		System.out.println(fc.showOpenDialog(this));
		System.out.println(fc.getSelectedFile().getAbsolutePath());
		
		BufferedImage icon = null;
		try {
			icon = ImageIO.read(fc.getSelectedFile());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		new VisualDisplayUnit(new ImageIcon(icon));
		
		
	}
	
	public static String getExtension(File f) {
        String ext = null;
        String s = f.getName();
        int i = s.lastIndexOf('.');

        if (i > 0 &&  i < s.length() - 1) {
            ext = s.substring(i+1).toLowerCase();
        }
        return ext;
    }
	
}
