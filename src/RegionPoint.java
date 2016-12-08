import java.awt.Point;

public class RegionPoint extends Point{
	private int area; //second level division
	private int section; //first level division
	private int mask; //section of map
	
	public RegionPoint(int x, int y, int a, int src, int map){
		super(x,y);
		area = a;
		section = src;
		mask = map;
	}

	public int getArea() {
		return area;
	}

	public void setArea(int area) {
		this.area = area;
	}

	public int getSection() {
		return section;
	}

	public void setSection(int section) {
		this.section = section;
	}

	public int getMask() {
		return mask;
	}

	public void setMask(int mask) {
		this.mask = mask;
	}
}
