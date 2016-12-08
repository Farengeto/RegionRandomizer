import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;

import javax.swing.JPanel;


public class RegionPanel extends JPanel{
	private RegionMain map;
	private int width;
	private int height;
	
	public RegionPanel(RegionMain main){
		map = main;
		width = main.getWidth();
		height = main.getHeight();
		this.setPreferredSize(new Dimension(width,height));
	}
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		//System.out.println(points.size());
		g.drawImage(map.getImage(), 0, 0, null);
		/*for(int i = 0; i < width; i++){
			for(int j = 0; j < height; j++){
				g.setColor(new Color(map[i][j]));
				g.fillRect(i, j, 1, 1);		
			}
		}*/
		//g.setColor(Color.white);
		g.setColor(new Color(0x3FFFFFFF));
		//drawPoints = new ArrayList<>(points);
		for(Point p : map.getPoints()){
			g.fillRect((int)p.getX(), (int)p.getY(), 1, 1);	
		}
	}
}
