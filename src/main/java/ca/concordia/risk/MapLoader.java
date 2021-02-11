package ca.concordia.risk;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * This class reads the existing maps
 * 
 * @author Shubham Vashisth
 */
public class MapLoader {

	public void loadMap(String p_fileName) {
		try {
			ArrayList<String> continent_names = new ArrayList<>();
			ArrayList<String> continent_value = new ArrayList<>();
			Scanner sc = new Scanner(new File(p_fileName));

			while (sc.hasNextLine()) {
				String line = sc.nextLine();
				if (line.equals("[continents]")) {
					while (sc.hasNextLine()) {
						String parseContinents = sc.nextLine();
						if (parseContinents.equals("[countries]"))
							break;
						else {
							Scanner reader = new Scanner(parseContinents);
							while (reader.hasNext()) {
								String continent = reader.next();
								String value = reader.next();
								reader.next();
								continent_names.add(continent);
								continent_value.add(value);
							}
							reader.close();
						}
					}
				}
			}
			sc.close();

			for (int i = 0; i < continent_names.size(); i++) {
				System.out.println(continent_names.get(i));
			}

			for (int i = 0; i < continent_value.size(); i++) {
				System.out.println(continent_value.get(i));
			}
		} catch (Exception e) {
			System.out.println("Cannot open the Map file");
		}
	}

	public static void main(String[] args) {
		MapLoader obj = new MapLoader();
		obj.loadMap("solar.map");
	}
}
