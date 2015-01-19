import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Panel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Path2D;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.awt.image.renderable.ParameterBlock;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;
import javax.media.jai.InterpolationNearest;
import javax.media.jai.JAI;
import javax.media.jai.iterator.RandomIter;
import javax.media.jai.iterator.RandomIterFactory;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.filechooser.FileFilter;

import com.sun.media.jai.widget.DisplayJAI;


public class Imagecomparision extends JFrame {

	public Color[][] rgb;
	public Color[][] rgb1;
	static final String basepath = "C://Users//gsukeshreddy//Desktop//Images";
	static final float basepixels = 500;
	static int width = 0;
	static int height = 25;
	static int count = 0;
	static int count1 = 0;
	static int count2 = 0;
	int m, n;
	int Xval1 = 0;
	int Yval1 = 0;
	Path2D.Float roi = new Path2D.Float();
	JFrame frame = new JFrame();
	File f;
	File file;
	Container cp;
	double avgblocks;
	RenderedImage ref1;
	int min;
	double avgblocksmatched;
	long starttime;

	//Reading the input and target image for comparison
	Imagecomparision(File f) throws IOException {

		// ************First Image related Starts****************
		RenderedImage ref2 = rescaling(ImageIO.read(f));
		ImageIO.write(ref2, "jpg",new File("C:\\Users\\gsukeshreddy\\Desktop\\Images\\out2.jpg"));	
		File newfile = new File("C:\\Users\\gsukeshreddy\\Desktop\\Images\\out2.jpg");
		BufferedImage buffer = ImageIO.read(newfile);
		JLabel label = new JLabel(new ImageIcon(buffer));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(label).addMouseListener((new MouseAdapter() {
			public void mousePressed(MouseEvent me) {
				System.out.println("Button clicked on the image");
				starttime = System.currentTimeMillis();
				System.out.println("StartTime: " + starttime);
				System.out.println(me);
				Xval1 = me.getXOnScreen();
				Yval1 = me.getYOnScreen();
				try {
					getXY();
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}));
		frame.pack();
		frame.setSize(516,540);
		frame.setVisible(true);
		
		cp = frame.getContentPane();
		cp.setLayout(new BorderLayout());
		DisplayJAI jai = new DisplayJAI(ref2);
		cp.add(new JScrollPane(jai), BorderLayout.WEST);
		rgb = RGB(ref2);
		// ***********First Image related calculations Ends***************

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		frame.setSize(500, 500);

		// ************second Image related starts******************
		
		JFileChooser choose1 = new JFileChooser(basepath);
		choose1.setFileFilter(new JPEGImageFile());
		int res = choose1.showOpenDialog(null);
		if (res == JFileChooser.APPROVE_OPTION) {
			file = choose1.getSelectedFile();
			System.out.println("name:" + file);
			
		//***********second Image related ends**********************
		} else {
			JOptionPane.showMessageDialog(null,
					"You must select one image to be the reference.",
					"Aborting...", JOptionPane.WARNING_MESSAGE);
		}
	}

	//Finding the average no.of blocks matched in Image B for every block in Image A
	public void getXY() throws IOException {
		try{
		BufferedImage buffer = ImageIO.read(file);
		int Xval = Xval1 / 25;
		int Yval = Yval1 / 25;
		int red1 = 0x00ff0000;
		int white = 0xffffff; 
		int r3 = rgb[Xval][Yval].getRed();
		int g3 = rgb[Xval][Yval].getGreen();
		int b3 = rgb[Xval][Yval].getBlue();
		
		ref1 = rescaling(ImageIO.read(file));
		ImageIO.write(ref1, "jpg",new File("C:\\Users\\gsukeshreddy\\Desktop\\Images\\out.jpg"));
		
		File newfile = new File("C:\\Users\\gsukeshreddy\\Desktop\\Images\\out.jpg");
		BufferedImage buffer1 = ImageIO.read(newfile);
		
		double distance = calcDistance(ref1);
		String dist1 = String.valueOf(distance);
	
		for (int x = 0; x < 20; x++) {
			for (int y = 0; y < 20; y++) {
				int r2 = rgb1[x][y].getRed();
				int g2 = rgb1[x][y].getGreen();
				int b2 = rgb1[x][y].getBlue();

				double tempDist = Math.sqrt((r3 - r2) * (r3 - r2) + (g3 - g2)
						* (g3 - g2) + (b3 - b2) * (b3 - b2));
				int dist = (int) tempDist;
				if (dist < 60) {
					int X = x*25;
					int Y = y*25;
					for(int wid= X; wid<X+25; wid++ )
					{
						for(int hei=Y;hei<Y+25;hei++)
						{
							buffer1.setRGB(wid,hei,red1);
						}
					}			
				}
			}
		}
		ImageIO.write(buffer1, "jpg",new File("C:\\Users\\gsukeshreddy\\Desktop\\Images\\out1.jpg"));
		compareeachblock();
		Long endtime = System.currentTimeMillis();
		System.out.println("Total time taken: " + (endtime-starttime));
		String min1 = String.valueOf(min);
		String avgblocks1 = String.valueOf(avgblocks);
		cp.add(new JScrollPane(new DisplayJAI(buffer1)), BorderLayout.CENTER);
		cp.add(new JLabel("AVg Blocks Matched: " + avgblocksmatched + " and  % matched: " + avgblocks1+"%"), BorderLayout.EAST);
		System.out.println("Distance: " + distance);
		}
		catch(Exception e)
		{
			
		}
	}

	//calculating the Distance between blocks
	private double calcDistance(RenderedImage ri) {
		rgb1 = RGB(ri);
		double dist = 0;
		for (int x = 0; x < 20; x++)
			for (int y = 0; y < 20; y++) {
				int r1 = rgb[x][y].getRed();
				int g1 = rgb[x][y].getGreen();
				int b1 = rgb[x][y].getBlue();
				int r2 = rgb1[x][y].getRed();
				int g2 = rgb1[x][y].getGreen();
				int b2 = rgb1[x][y].getBlue();
				double tempDist = Math.sqrt((r1 - r2) * (r1 - r2) + (g1 - g2)
						* (g1 - g2) + (b1 - b2) * (b1 - b2));
				dist += tempDist;
			}
		return dist;
	}

	//comparing each block
	public void compareeachblock() {
		int percentcount = 0;
		Double max = Double.MAX_VALUE;
		int distance = 0;
		double dist = 0;
		min = 0;
		for (int i = 0; i < 20; i++) {
			for (int j = 0; j < 20; j++) {
				int r1 = rgb[i][j].getRed();
				int g1 = rgb[i][j].getGreen();
				int b1 = rgb[i][j].getBlue();
				max = Double.MAX_VALUE;
				percentcount = 0;
				for (int k = 0; k < 20; k++) {
					for (int l = 0; l < 20; l++) {
						int r2 = rgb1[k][l].getRed();
						int g2 = rgb1[k][l].getGreen();
						int b2 = rgb1[k][l].getBlue();

						double tempDist = Math
								.sqrt((r1 - r2) * (r1 - r2) + (g1 - g2)
										* (g1 - g2) + (b1 - b2) * (b1 - b2));
						distance = (int)tempDist;
						if (distance < 50) {
							percentcount++;
						}
						max = Math.min(max, distance);
					}
				}
				min += percentcount++;
				dist +=max;
			}
		}
		
		System.out.println("Total blocks matched: " + min);
		avgblocksmatched = (double)min/400.0;
		System.out.println("Avg blocks matched in image 2 for each block in image 1: " + avgblocksmatched);
		avgblocks = (double)(avgblocksmatched*100.0)/400.0;
		System.out.println("% of blocks matched in image 2 for each block in image 1: " + avgblocks + "%");
		System.out.println("Min Distance Acheived: " + dist);
	}

	//Getting RGB values of each block
	public Color[][] RGB(RenderedImage ref) {
		width = 0;
		height = 25;
		count = 0;
		count1 = 0;
		count2 = 0;
		Color[][] color = new Color[20][20];
		for (int i = 0; i < 20; i++) {
			for (int j = 0; j < 20; j++) {
				color[i][j] = Average(ref);
			}
		}
		return color;
	}

	//Finding Average of pixels
	public Color Average(RenderedImage ref) {
		RandomIter iterator = RandomIterFactory.create(ref, null);
		width += 25;
		double[] pix = new double[3];
		double[] total = new double[3];
		int totalpixels = 0;
		if (width != 525) {
			for (int i = count1; i < height; i++) {
				count = width - 25;
				for (int j = count; j < width; j++) {
					iterator.getPixel(i, j, pix);
					total[0] += pix[0];
					total[1] += pix[1];
					total[2] += pix[2];
					totalpixels++;
					count++;
				}
				count1++;
			}
			count1 = count2;
			total[0] /= totalpixels;
			total[1] /= totalpixels;
			total[2] /= totalpixels;
			return new Color((int) total[0], (int) total[1], (int) total[2]);
		}
		if (width == 525 && height != 500) {
			count2 += 25;
			count1 += 25;
			count = 0;
			width = 0;
			height += 25;
			Color col = Average(ref);
			return col;
		}
		return null;
	}

	//Re-Scaling the image to 500*500 pixels
	public RenderedImage rescaling(RenderedImage render) {
		float wd = render.getWidth();
		float ht = render.getHeight();
		float scaledWidth = basepixels / wd;
		float scaledHeight = basepixels / ht;
		ParameterBlock block = new ParameterBlock();
		block.addSource(render);
		block.add(scaledWidth);
		block.add(scaledHeight);
		block.add(0.0f);
		block.add(0.0f);
		block.add(new InterpolationNearest());
		// Creates a new, scaled image and uses it on the DisplayJAI component
		return JAI.create("scale", block);
	}

	public static void main(String[] args) throws IOException {
		JFileChooser choose = new JFileChooser(basepath);
		choose.setFileFilter(new JPEGImageFile());
		int res = choose.showOpenDialog(null);
		if (res == JFileChooser.APPROVE_OPTION) {
			File f = choose.getSelectedFile();
			System.out.println("name:" + f);
			new Imagecomparision(f);
		} else {
			JOptionPane.showMessageDialog(null,
					"select image",
					"Aborting..", JOptionPane.WARNING_MESSAGE);
		}
	}
}
