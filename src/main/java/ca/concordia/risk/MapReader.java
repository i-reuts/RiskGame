package ca.concordia.risk;
import java.io.File;
import java.util.Scanner;

/**
 * This class reads the existing maps
 * @author Shubham Vashisth
 */
public class MapReader {
	private Scanner d_sc;
	private String d_map;
	
	public MapReader() {
		d_map = "testMapFile.map";
	}
	
	public void openMap() {
		try {
			d_sc = new Scanner(new File(d_map));
		} catch (Exception e) {
			System.out.println("Cannot open the Map file");
		}
	}
	
	public void readMap() {
		while(d_sc.hasNext()) {
			//do something
			System.out.println("in loop");
			break;
		}
	}
	
	private void closeMap() {
		d_sc.close();
	}

	public static void main(String[] args) {
		MapReader map = new MapReader();
		map.openMap();
		map.readMap();
		map.closeMap();
	}	

}
