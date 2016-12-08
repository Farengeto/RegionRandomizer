
public class RegionThreadBalanced extends Thread{
	private RegionMain main;
	
	public RegionThreadBalanced(RegionMain r){
		main = r;
	}
	
	public void run(){
		int height = main.getHeight();
		int x = main.getNext();
		
		while(x != -1){
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
			x = main.getNext();
		}
		main.threadDone();
	}
}
