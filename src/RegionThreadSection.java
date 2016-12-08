
public class RegionThreadSection extends Thread{
	private RegionMain main;
	private int max;
	private int min;
	
	public RegionThreadSection(RegionMain r, int min, int max){
		main = r;
		this.max = max;
		this.min = min;
		//System.out.println("Thread created for range " + min + " to " + max);
	}
	
	public void run(){
		int height = main.getHeight();
		for(int x = min; x < max; x++){
			for(int y = 0; y < height; y++){
				int c = 0;
				int sc = 0;
				int a = main.getRGB(x,y);
				//System.out.print(Integer.toHexString(a));
				if(a != 0xffffffff){ //only set if in area
					//System.out.println(" - Things are still working");
					//current method selects closest point by absolute distance as colour
					RegionPoint close = main.closest(x,y);
					try{
						c = close.getArea();
					}catch(NullPointerException e){
						System.out.println("Colour not found at " + x + " " + y + " " + Integer.toHexString(a));
						System.exit(0);
					}
					sc = close.getSection();
				}
				main.setRGB(x, y, c, sc);
			}
			//System.out.println(i);
		}
		main.threadDone();
	}
}
