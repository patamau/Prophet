package prophet.util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileFilter;
import java.io.InputStream;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class Resources {
	
	private static final Logger logger = Logger.getLogger(Resources.class);
	
	public static String PIX_PATH = "pix";
	
	private static HashMap<String, ImageIcon> cache = new HashMap<String, ImageIcon>();

	public static final ImageIcon getIcon(String name){
		ImageIcon i = cache.get(name);
		if(i==null){
			String url = PIX_PATH+'/'+name;
			try {
				i = new ImageIcon(ImageIO.read(ClassLoader.getSystemClassLoader().getResourceAsStream(url)));
				cache.put(name, i);
			} catch (Exception e) {
				logger.error("Cannot locate icon "+name+" at "+url);
			}
		}
		return i;
	}
	
	public static final BufferedImage getImage(String url){
		BufferedImage i = null;
		try {
			i = ImageIO.read(ClassLoader.getSystemClassLoader().getResourceAsStream(url));
		} catch (Exception e) {
			logger.error("cannot load image from ",url);
		}
		return i;
	}
	
	public static final InputStream getFile(String path){
		try{
			return ClassLoader.getSystemResourceAsStream(path);
		}catch(Exception e){
			System.err.println("Cannot locate "+path);
			e.printStackTrace();
		}
		return null;
	}
	
	public static final File[] getFiles(final String path, final String suffix){
		File root = new File(ClassLoader.getSystemClassLoader().getResource(path).getFile());
		if(root.exists()&&root.isDirectory()){
			return root.listFiles(new FileFilter(){
				@Override
				public boolean accept(File f) {
					return f.isFile()&&f.getName().endsWith(suffix);
				}
			});
		}else{
			throw new RuntimeException("Invalid folder "+path);
		}
	}
}
