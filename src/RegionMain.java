import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Observable;
import java.util.Random;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class RegionMain extends Observable{
//public class RegionMain extends JPanel {
	//private Map<Point,Integer> cities; //map of points and their respective colours
	private List<RegionPoint> points; //list of all points
	//public List<RegionPoint> drawPoints;
	private int[][] map; //2D ARGB colour map
	private int width = 436;
	private int height = 273;
	private BufferedImage capitals; //area used for capital locations
	private BufferedImage area; //are used for points
	private BufferedImage output; //area used for map generation
	private int maxPoints = 10000; //total points in the calculation
	private int areas = 50; //total second-level divisions
	private int states = 10;
	//public int drawInterval = 1000;
	//private int[][] initial = {{427,957,0xff25254a},{917,277,0xff2b4060}};
	private String nationName;
	//private String areaFile;
	//private String outputFile;
	//private String capitalFile;
	private Map<Integer,Integer> colourMap;
	
	private BufferedImage outputMap;
	private BufferedImage stateMap;
	private BufferedImage cityMap;
	private BufferedImage capitalMap;
	
	private int runningThreads = 0;
	private int nextX = 0;
	
	public RegionMain(String name,File areaFile, File outputFile, File capitalFile,int max, int l1, int l2) throws IOException,IllegalArgumentException{
		maxPoints = max;
		states = l1;
		areas = l2;
		points = new ArrayList<>();
		colourMap = new HashMap<>();
		//cities = new HashMap<>();
		nationName = name;
		//try{
			area = ImageIO.read(areaFile);
			output = ImageIO.read(outputFile);
			capitals = ImageIO.read(capitalFile);
			/*area = ImageIO.read(new File(areaFile));
			output = ImageIO.read(new File(outputFile));
			capitals = ImageIO.read(new File(capitalFile));*/
			width = area.getWidth();
			height = area.getHeight();
			map = new int[width][height];
			outputMap = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
			//makePoints(area);
			makePoints();
		//}catch(IOException e){ //terminate if image not found, temporary fix until UI implemented
		//	System.out.println("Image not found");
		//	System.exit(0);
		//}
		
		/*int scale = 5;
		int offset = 7;
		Point p = new Point(84*scale+offset,190*scale+offset);
		points.add(p);
		cities.put(p,0xff25254a);
		p = new Point(182*scale+offset,54*scale+offset);
		points.add(p);
		cities.put(p,0xff2b4060);
		p = new Point(185*scale+offset,233*scale+offset);
		points.add(p);
		cities.put(p,0xff204a4d);
		p = new Point(417*scale+offset,154*scale+offset);
		points.add(p);
		cities.put(p,0xff1c1a4e);*/
		//drawPoints = points;
		//this.setPreferredSize(new Dimension(width,height));
		//JFrame frame = new JFrame("Regions - " + nationName);
		//JScrollPane scroll = new JScrollPane(this);
		//scroll.setPreferredSize(new Dimension(1000,1000));
		//frame.add(scroll);
		//frame.add(this);
		//frame.setSize(width, height);
		//frame.pack();
		//frame.setVisible(true);
		//frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public static void main(String[] args){
		int processors = Runtime.getRuntime().availableProcessors();
		System.out.print("Generating Points");
		String name = "Farengeto";
		File f = new File("Farengeto.png");
		RegionMain r = null;
		try{
			r = new RegionMain(name,f,f,f,10000,10,50);
		}catch(IOException e){
			System.out.println("Image not found");
			System.exit(0);
		}
		//r.makeMapThreaded2(processors);
		//System.out.println("Done");
		//r.repaint();
		/*int mp = r.getMax();
		int a = r.getAreas();
		System.out.println("Creating " + mp + " points");
		while(r.numPoints() < mp){
			r.addPoint();
			//if(r.numPoints() % r.drawInterval == 0 || r.numPoints() == a){
			if(r.numPoints() == a){
				System.out.print("Generating Provinces");
				r.makeMapThreaded2(processors);
				System.out.println("Done");
				//r.repaint();
			}
		}*/
		System.out.println("Created " + r.numPoints() + " points");
		
		System.out.println(processors + " Processor Cores detected");
		System.out.println();
		//System.out.print(r.numPoints());
		long start;
		
		/*start = System.currentTimeMillis();
		System.out.println("Starting unthreaded for " + r.numPoints());
		r.makeMap();
		//System.out.println(" - done");
		r.repaint();
		long runtime = System.currentTimeMillis() - start;
		System.out.println("Unthreaded finished in " + runtime);
		System.out.println();*/
		
		/*start = System.currentTimeMillis();
		System.out.print("Starting Sectional threading for " + r.numPoints());
		System.out.println(" with " + processors + " threads");
		r.makeMapThreaded(processors);
		//System.out.println(" - done");
		r.repaint();
		long threadRuntime = System.currentTimeMillis() - start;
		System.out.println("Sectional Threaded finished in " + threadRuntime);
		System.out.println();*/
		
		/*start = System.currentTimeMillis();
		System.out.print("Starting Sectional threading for " + r.numPoints());
		System.out.println(" with " + processors/2 + " threads");
		r.makeMapThreaded(processors/2);
		//System.out.println(" - done");
		r.repaint();
		long threadRuntime2 = System.currentTimeMillis() - start;
		System.out.println("Sectional Threaded finished in " + threadRuntime2);
		System.out.println();*/
		
		start = System.currentTimeMillis();
		System.out.print("Starting Balanced threading for " + r.numPoints());
		System.out.println(" with " + processors + " threads");
		r.makeMapThreaded2(processors);
		//System.out.println(" - done");
		//r.repaint();
		long balancedRuntime = System.currentTimeMillis() - start;
		System.out.println("Balanced Threaded finished in " + balancedRuntime);
		System.out.println();
		
		/*start = System.currentTimeMillis();
		System.out.print("Starting Balanced threading for " + r.numPoints());
		System.out.println(" with " + processors/2 + " threads");
		r.makeMapThreaded2(processors/2);
		//System.out.println(" - done");
		r.repaint();
		long balancedRuntime2 = System.currentTimeMillis() - start;
		System.out.println("Balanced Threaded finished in " + balancedRuntime2);
		System.out.println();*/
		
		System.out.println("Final Results:");
		//System.out.println("Unthreaded:\t" + runtime);
		//System.out.println("Sectional Threaded (Hyper-threaded):\t" + threadRuntime);
		//System.out.println("Sectional Threaded (Real):\t" + threadRuntime2);
		System.out.println("Balanced Threaded (Hyper-threaded):\t" + balancedRuntime);
		//System.out.println("Balanced Threaded (Real):\t" + balancedRuntime2);
	}
	
	public void addPoint(){
		Random random = new Random();
		int x;
		int y;
		RegionPoint closest;
		//long t1 = System.currentTimeMillis();
		do{
			x = random.nextInt(width);
			y = random.nextInt(height);
			closest = closest(x,y);
		}while(area.getRGB(x,y) == 0xffffffff || 0 >= closest.distance(x,y)); //check that point is not already in use, and is in target area;
		//long t2 = System.currentTimeMillis();
		//System.out.println(x + "\t" + y);
		//Point p = new Point(x,y);
		//points.add(p);
		int size = points.size();
		int col = closest.getArea();
		int offset = 0;
		if(size < areas){
			offset = ((int)(size/16) * 0x00010000) + ((int)(size%16) * 0x00000100);
			//int offset = ((int)(size/16) * 0x00010000) + ((int)(size%16) * 0x00000100);
			//cities.put(p,cities.get(closest) + offset);
		}
		/*else{
			cities.put(p,cities.get(closest)); //give new point colour of closest point
		}*/
		points.add(new RegionPoint(x,y,col+offset,closest.getSection(),closest.getMask()));
		//long t3 = System.currentTimeMillis();
		//System.out.println(size + "\t" + (t3-t1) + "\t" + t1);
		//System.out.println(points.size() + "\t" + x + "\t" + y + "\t" + Integer.toHexString(col+offset)+ "\t" + Integer.toHexString(col)+ "\t" + Integer.toHexString(closest.getMask()));
	}
	
	public int numPoints(){
		return points.size();
	}
	
	public RegionPoint closest(int x,int y){
		RegionPoint closest = null; 
		double minDist = 1000000000;
		int c = area.getRGB(x,y);
		if(colourMap.get(c) != null){
			c = colourMap.get(c);
		}
		/*try{
			c = colourMap.get(c);
		}catch(NullPointerException e){
			System.out.println("NullPointException on " + Integer.toHexString(c));
			//System.exit(0);
		}*/	
		for(RegionPoint p : points){
			if(p.getMask() == c){
				double d = p.distance(x,y);
				if(d < minDist){
					minDist = d;
					closest = p;
				}
			}
		}
		return closest;
	}
	
	public RegionPoint closest(int x,int y, BufferedImage map){
		RegionPoint closest = null; 
		double minDist = 1000000000;
		int c = map.getRGB(x,y);
		try{
			c = colourMap.get(c);
		}catch(NullPointerException e){
			System.out.println(Integer.toHexString(c));
			//System.exit(0);
		}	
		for(RegionPoint p : points){
			if(p.getMask() == c){
				double d = p.distance(x,y);
				if(d < minDist){
					minDist = d;
					closest = p;
				}
			}
		}
		return closest;
	}
	
	public RegionPoint closestNoMask(int x,int y){
		RegionPoint closest = null; 
		double minDist = 1000000000;
		for(RegionPoint p : points){
				double d = p.distance(x,y);
				if(d < minDist){
					minDist = d;
					closest = p;
				}
		}
		return closest;
	}
	
	public int getMax(){
		return maxPoints;
	}
	
	public int getAreas(){
		return areas;
	}
	
	public int[][] getMap(){
		return map;
	}
	
	public BufferedImage getImage(){
		return outputMap;
	}
	
	public List<RegionPoint> getPoints(){
		List<RegionPoint> nPoints = new ArrayList<>(points);
		return nPoints;
	}
	
	public int getHeight(){
		return height;
	}
	
	public int getWidth(){
		return width;
	}
	
	public int getRGB(int x, int y){
		return output.getRGB(x,y);
	}
	
	public void setRGB(int x, int y, int c, int c2){
		map[x][y] = c;
		outputMap.setRGB(x, y, c);
		stateMap.setRGB(x, y, c2);
	}
	
	public synchronized void threadDone(){
		runningThreads--;
		setChanged();
		notifyObservers(100*(double)Math.max(nextX - runningThreads,0)/width);
		//System.out.println(100*(double)Math.max(nextX - runningThreads,0)/width);
		notifyAll();
	}
	
	public synchronized int getNext(){
		if(nextX < width){
			int x = nextX;
			nextX++;
			setChanged();
			notifyObservers(100*(double)Math.max(nextX - runningThreads,0)/width);
			//System.out.println(100*(double)Math.max(nextX - runningThreads,0)/width);
			return x;
		}
		return -1;
	}
	
	public void makeMap(){
		outputMap = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		stateMap = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		cityMap = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		capitalMap = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		//BufferedImage outputMap2 = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		//BufferedImage stateMap2 = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		
		for(int i = 0; i < width; i++){
			for(int j = 0; j < height; j++){
				int c = 0;
				if(output.getRGB(i,j) != 0xffffffff){ //only set if in area
					//current method selects closest point by absolute distance as colour
					//c = cities.get(closest(i,j));
					RegionPoint close = closest(i,j);
					try{
						c = close.getArea();
					}catch(NullPointerException e){
						System.out.println(i + " " + j + " " + Integer.toHexString(output.getRGB(i, j)));
						System.exit(0);
					}
					map[i][j] = c; 
				
					/*if((c%256 + 256) == 0x0000004a){
						stateMap.setRGB(i, j, 0xff25254a);
					}
					else if((c%256 + 256) == 0x00000060){
						stateMap.setRGB(i, j, 0xff2b4060);
					}
					else if((c%256 + 256) == 0x0000004d){
						stateMap.setRGB(i, j, 0xff204a4d);
					}
					else if((c%256 + 256) == 0x0000004e){
						stateMap.setRGB(i, j, 0xff1c1a4e);
					}
					else{
						System.out.println("No state found - " + (c%256 + 256) + " - " + c);
					}*/
					stateMap.setRGB(i, j, close.getSection());
				}
				outputMap.setRGB(i, j, c);
				/*c = 0;
				int c2 = 0;
				RegionPoint close = closestNoMask(i,j);
				try{
					c = close.getArea();
					c2 = close.getSection();
				}catch(NullPointerException e){
					//System.out.println(Integer.toHexString(area.getRGB(i, j)));
					//System.exit(0);
				}
				//map[i][j] = c; 
				stateMap2.setRGB(i, j, c2);
				outputMap2.setRGB(i, j, c);*/
				/*if(area.getRGB(i,j) != 0xffffffff){ //only set if in area
					//current method selects closest point by absolute distance as colour
					//c = cities.get(closest(i,j));
					RegionPoint close = closest(i,j,area);
					try{
						c = close.getArea();
					}catch(NullPointerException e){
						System.out.println(Integer.toHexString(area.getRGB(i, j)));
						System.exit(0);
					}
					map[i][j] = c; 
					stateMap2.setRGB(i, j, close.getSection());
				}
				outputMap2.setRGB(i, j, c);*/
				
			}
			//System.out.println(i);
		}
		for(Point p : points){
			cityMap.setRGB((int)p.getX(), (int)p.getY(), 0xffffffff);
			if(points.indexOf(p) < areas){
				capitalMap.setRGB((int)p.getX(), (int)p.getY(), 0xffffffff);
			}
		}
		try{
			File mImage = new File(nationName + "-" + points.size() +".png");
			File cImage = new File(nationName + "City-" + points.size() +".png");
			File caImage = new File(nationName + "Capital-" + points.size() +".png");
			File sImage = new File(nationName + "State-" + points.size() +".png");
			//File m2Image = new File(nationName + "2-" + points.size() +".png");
			//File s2Image = new File(nationName + "State2-" + points.size() +".png");
			ImageIO.write(outputMap, "png", mImage);
			ImageIO.write(cityMap, "png", cImage);
			ImageIO.write(capitalMap, "png", caImage);
			ImageIO.write(stateMap, "png", sImage);
			//ImageIO.write(outputMap2, "png", m2Image);
			//ImageIO.write(stateMap2, "png", s2Image);
		}catch(IOException e){}
		setChanged();
		notifyObservers(-1.0);
		//drawPoints = points;
	}
	
	public synchronized void makeMapThreaded(int processors){
		outputMap = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		stateMap = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		cityMap = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		capitalMap = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		
		runningThreads = 0;
		for(int i = 0; i < processors; i++){
			int min = runningThreads * width / processors;
			int max = ((runningThreads+1) * width / processors);
			Thread t = new RegionThreadSection(this,min,max);
			t.start();
			runningThreads++;
		}
		
		try{
			while(runningThreads > 0){
				wait();
			}
		}catch(InterruptedException e){
			System.exit(0);
		}
		
		for(Point p : points){
			cityMap.setRGB((int)p.getX(), (int)p.getY(), 0xffffffff);
			if(points.indexOf(p) < areas){
				capitalMap.setRGB((int)p.getX(), (int)p.getY(), 0xffffffff);
			}
		}
		try{
			File mImage = new File(nationName + "--" + points.size() +".png");
			File cImage = new File(nationName + "-City-" + points.size() +".png");
			File caImage = new File(nationName + "-Capital-" + points.size() +".png");
			File sImage = new File(nationName + "-State-" + points.size() +".png");
			ImageIO.write(outputMap, "png", mImage);
			ImageIO.write(cityMap, "png", cImage);
			ImageIO.write(capitalMap, "png", caImage);
			ImageIO.write(stateMap, "png", sImage);
		}catch(IOException e){}
		setChanged();
		notifyObservers(-1.0);
		//drawPoints = points;
	}
	
	public synchronized void makeMapThreaded2(int processors){
		outputMap = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		stateMap = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		cityMap = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		capitalMap = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		
		nextX = 0;
		runningThreads = 0;
		for(int i = 0; i < processors; i++){
			Thread t = new RegionThreadBalanced(this);
			t.start();
			runningThreads++;
		}
		
		try{
			while(runningThreads > 0){
				wait();
			}
		}catch(InterruptedException e){
			System.exit(0);
		}
		
		for(Point p : points){
			cityMap.setRGB((int)p.getX(), (int)p.getY(), 0xffffffff);
			if(points.indexOf(p) < areas){
				capitalMap.setRGB((int)p.getX(), (int)p.getY(), 0xffffffff);
			}
		}
		try{
			File mImage = new File(nationName + "-" + points.size() +".png");
			File cImage = new File(nationName + "-City-" + points.size() +".png");
			File caImage = new File(nationName + "-Capital-" + points.size() +".png");
			File sImage = new File(nationName + "-State-" + points.size() +".png");
			ImageIO.write(outputMap, "png", mImage);
			ImageIO.write(cityMap, "png", cImage);
			ImageIO.write(capitalMap, "png", caImage);
			ImageIO.write(stateMap, "png", sImage);
		}catch(IOException e){}
		setChanged();
		notifyObservers(-1.0);
		//drawPoints = points;
	}
	
	/*@Override
	public void paint(Graphics g) {
		super.paint(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		//System.out.println(points.size());
		for(int i = 0; i < width; i++){
			for(int j = 0; j < height; j++){
				g.setColor(new Color(map[i][j]));
				g.fillRect(i, j, 1, 1);		
			}
		}
		//g.setColor(Color.white);
		g.setColor(new Color(0x3FFFFFFF));
		drawPoints = new ArrayList<>(points);
		for(Point p : drawPoints){
			g.fillRect((int)p.getX(), (int)p.getY(), 1, 1);	
		}
	}*/
	
	/*public void makePoints(BufferedImage a) throws IOException{
		BufferedImage capitals = ImageIO.read(new File(capitalFile));
		for(int i = 0; i < capitals.getWidth(); i++){
			for(int j = 0; j < capitals.getHeight(); j++){
				if(capitals.getRGB(i,j) != 0xffffffff){
					int c = checkBlue(a.getRGB(i,j));
					points.add(new RegionPoint(i,j,c,c,c));
					System.out.println(points.size() + "\t" + i + "\t" + j + "\t" + Integer.toHexString(c));
				}
			}
		}
	}*/
	
	//Generates a list of points with the corresponding subdivisions assigned
	public void makePoints() throws IOException{
		Random random = new Random();
		int x,y,col;
		//Ensure a state is generated for each colour mask region 
		List<Integer> colours = buildColourList(capitals);
		for(int c : colours){
			do{
				x = random.nextInt(capitals.getWidth());
				y = random.nextInt(capitals.getHeight());
			}while(capitals.getRGB(x,y) != c);
			col = 0xff000000 + random.nextInt(0x00dfffff);
			points.add(new RegionPoint(x,y,col,col,capitals.getRGB(x,y)));
			System.out.println("State for " + capitals.getRGB(x,y));
		}
		/*do{
			x = random.nextInt(capitals.getWidth());
			y = random.nextInt(capitals.getHeight());
		}while(capitals.getRGB(x,y) == 0xffffffff);
		int col = 0xff000000 + random.nextInt(0x00dfffff);
		points.add(new RegionPoint(x,y,col,col,capitals.getRGB(x,y)));
		System.out.println("State for " + capitals.getRGB(x,y));*/
		
		//Generate more states until desired count reached
		for(int i = colours.size(); i < states; i++){
			RegionPoint closest;
			do{
				x = random.nextInt(capitals.getWidth());
				y = random.nextInt(capitals.getHeight());
				closest = closest(x,y,capitals);
			}while(capitals.getRGB(x,y) == 0xffffffff || (closest != null && 0 >= closest.distance(x,y)));
			col = checkBlue(0xff000000 + random.nextInt(0x00dfffff));
			points.add(new RegionPoint(x,y,col,col,capitals.getRGB(x,y)));
			System.out.println("State for " + capitals.getRGB(x,y));
			//colourMap.put(0xff000000,0xff000000);
			//colourMap.put(0xffffffff,0xffffffff);
		}
		
		//Generate remaining points
		while(points.size() < maxPoints){
			addPoint();
		}
	}
	
	private List<Integer> buildColourList(BufferedImage capitals){
		ArrayList<Integer> colours = new ArrayList<>();
		for(int x = 0; x < capitals.getWidth(); x++){
			for(int y = 0; y < capitals.getHeight(); y++){
				int c = capitals.getRGB(x,y);
				if(c != 0xffffffff && !colours.contains(c)){
					colours.add(c);
					//colourMap.put(c,c);
				}
			}
		}
		
		return colours;
	}
	
	public int checkBlue(int c){
		boolean duplicate;
		int src = c;
		do{
			duplicate = false;
			for(RegionPoint p : points){
				if(p.getArea()%256 == c%256){
					c++;
					duplicate = true;
				}
			}
		}while(duplicate);
		colourMap.put(src, c);
		return c;
	}
}
