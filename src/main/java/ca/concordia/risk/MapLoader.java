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
		Scanner sc, reader;
		//Map map = new Map();
	
		try {
			ArrayList<String> continent_names = new ArrayList<>();
			ArrayList<String> continent_value = new ArrayList<>();
			ArrayList<String> country_id = new ArrayList<>();
			ArrayList<String> country_name = new ArrayList<>();
			ArrayList<String> country_continent = new ArrayList<>();
			sc = new Scanner(new File(p_fileName));

			while (sc.hasNextLine()) {
				String line = sc.nextLine();
				if (line.equals("[continents]")) {
					while (sc.hasNextLine()) {
						String parse_continents = sc.nextLine();
						if (parse_continents.equals("[countries]"))
							break;
						else {
							reader = new Scanner(parse_continents);
							while (reader.hasNext()) {
								String continent = reader.next();
								String value = reader.next();
								reader.next();
								continent_names.add(continent);
								continent_value.add(value);
							}
						}
					}
				}
			}
			
			sc = new Scanner (new File(p_fileName));

			while (sc.hasNextLine()) {
				String line = sc.nextLine();
				if (line.equals("[countries]")) {
					while (sc.hasNextLine()) {
						String parse_countries = sc.nextLine();
						if (parse_countries.equals("[borders]"))
							break;
						else {
							reader = new Scanner(parse_countries);
							while (reader.hasNext()) {
								String country_i = reader.next();
								String country_n = reader.next();
								String country_c = reader.next();
								reader.next();
								reader.next();
								country_id.add(country_i);
								country_name.add(country_n); 
								country_continent.add(country_c);
							}
						}
					}
				}
			}
			
			System.out.println("\ncontinent names, size: "+ continent_names.size() + "\n");
			for (int i = 0; i < continent_names.size(); i++) {
				System.out.println(continent_names.get(i));
			}
			System.out.println("\ncontinent value, size: "+ continent_value.size() + "\n");
			for (int i = 0; i < continent_value.size(); i++) {
				System.out.println(continent_value.get(i));
			}
			System.out.println("\ncountry id, size: "+ country_id.size() + "\n");
			for (int i = 0; i < country_id.size(); i++) {
				System.out.println(country_id.get(i));
			}
			System.out.println("\ncountry name, size: "+ country_name.size() + "\n");
			for (int i = 0; i < country_name.size(); i++) {
				System.out.println(country_name.get(i));
			}
			System.out.println("\ncountry continent, size: "+ country_continent.size() + "\n");
			for (int i = 0; i < country_continent.size(); i++) {
				System.out.println(country_continent.get(i));
			}
		} catch (Exception e) {
			System.out.println("Cannot open the Map file");
		}	
	}

	public static void main(String[] args) {
		MapLoader obj = new MapLoader();
		obj.loadMap("testMapFile.map");
	}
}
