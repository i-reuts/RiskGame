package ca.concordia.risk.game;

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
		Map map = new Map();
		Continent continent;
		Country country;
		
		try {
			ArrayList<String> continent_name = new ArrayList<>();
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
								String continent_n = reader.next();
								String continent_v = reader.next();
								reader.next();
								continent_name.add(continent_n);
								continent_value.add(continent_v);
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
			ArrayList<String> borders = new ArrayList<>();
			sc = new Scanner (new File(p_fileName));
			
			while (sc.hasNextLine()) {
				String line = sc.nextLine();
				if (line.equals("[borders]")) {
					while (sc.hasNextLine()) {
						String parse_borders = sc.nextLine();
						reader = new Scanner(parse_borders);
						while(reader.hasNext()) 
							borders.add(reader.next());
						borders.add("end");	
					}
				}
			}
			
			/*for (int i = 0; i < borders.size(); i++) {
				System.out.println(borders.get(i));
			}*/
		
			/*System.out.println("continent name size: "+ continent_name.size());
			System.out.println("continent value size: "+ continent_value.size());
			System.out.println("\nContinent Name\t" + "Continent Values\n");
			
			for (int i = 0; i < continent_name.size(); i++) {
				System.out.println(continent_name.get(i) + "\t" +continent_value.get(i));
			}
			
			System.out.println("\ncountry id size: "+ country_id.size());
			System.out.println("country name size: "+ country_name.size());
			System.out.println("country continent size: "+ country_continent.size());
			System.out.println("\nCountry id\t" + "Country Name\t" + "Country continent\n");
			
			for (int i = 0; i < country_id.size(); i++) {
				System.out.println(country_id.get(i) + "\t" + country_name.get(i) + "\t" + country_continent.get(i));
			}*/

			/*
			for(int i = 0; i < continent_name.size(); i++) {
				continent = new Continent(String.valueOf(continent_name.get(i)), Integer.parseInt(continent_value.get(i)));
				map.addContinent(continent); 
				System.out.println("continent name: " + continent_name.get(i));
				System.out.println("continent reinforcement: " + continent_value.get(i));
				
				for(int j = 0; j < country_id.size(); j++) {
					country = new Country(Integer.parseInt(country_id.get(j)), String.valueOf(country_name.get(j)));
					System.out.println("\tcountry name: " + country_name.get(j));
					System.out.println("\tcountry ID: " + country_id.get(j));
					System.out.println("\tparent continent id: " + country_continent.get(j));
					if(String.valueOf(continent.getId()).equals(country_continent.get(j))) {
						Boolean status = continent.addCountry(country);
						System.out.println("\t\tcountry added to "+ continent.getName());
						if(status == true) {
							map.addCountry(country);
							System.out.println("\t\t\tcountry added to map object");
							
						}
					}
				}
			}*/	
			
			
			
			
			
			
			
			
		} catch (Exception e) {
			System.out.println("Cannot open the Map file");
			e.printStackTrace();
		}	
	}

	public static void main(String[] args) {
		MapLoader obj = new MapLoader();
		obj.loadMap("testMapFile.map");
	}
}
