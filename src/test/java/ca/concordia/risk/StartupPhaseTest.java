package ca.concordia.risk;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import ca.concordia.risk.game.Continent;
import ca.concordia.risk.game.Country;
import ca.concordia.risk.game.GameMap;
import ca.concordia.risk.game.phases.Phase;
import ca.concordia.risk.game.phases.StartupPhase;
import ca.concordia.risk.io.commands.AssignCountriesCommand;
import ca.concordia.risk.io.commands.GamePlayerCommand;

public class StartupPhaseTest {
	private static GameMap d_defaultMap;

	
	/**
	 * Setup the default map and initilize the GameEngine to Strtup Phase
	 */
	@BeforeEach
	void SetUpBeforeClass() {
		d_defaultMap = new GameMap();
		
		Continent l_continent = new Continent("Continent", 5);
		Country l_country_1 = new Country("Country_1", l_continent);
		Country l_country_2 = new Country("Country_2", l_continent);
		Country l_country_3 = new Country("Country_3", l_continent);
		Country l_country_4 = new Country("Country_4", l_continent);
		
		d_defaultMap.addContinent(l_continent);
		d_defaultMap.addCountry(l_country_1);
		d_defaultMap.addCountry(l_country_2);
		d_defaultMap.addCountry(l_country_3);
		d_defaultMap.addCountry(l_country_4);
		
		GameEngine.Initialize();
		GameEngine.SetMap(d_defaultMap);
		GameEngine.SwitchToNextPhase();
	}
	
	@Test
	@Order(1)
	void testGameplayerCommand() {
		System.out.println("======= testGameplayerCommand =======");
		// Try to add the same player twice
		GamePlayerCommand l_gameplayerCommand = new GamePlayerCommand();
		l_gameplayerCommand.addPlayer("Player_1");
		l_gameplayerCommand.addPlayer("Player_1");
		l_gameplayerCommand.addPlayer("Player_2");
		l_gameplayerCommand.execute();
		
		// Player with the same name cannot be added twice
		assertEquals(GameEngine.GetNumberOfPlayers(), 2);
		
		// Players were added to the GameEngine successfully
		assertEquals(GameEngine.GetPlayer("Player_1").getName(), "Player_1");
		assertEquals(GameEngine.GetPlayer("Player_2").getName(), "Player_2");
		
		// Remove player
		l_gameplayerCommand.removePlayer("Player_1");
		l_gameplayerCommand.removePlayer("Player_2");
		l_gameplayerCommand.execute();
		
		// Players removed successfully 
		assertEquals(GameEngine.GetNumberOfPlayers(), 0);
	}
	
	@Test
	void testStartGameOnePlayer() {
		System.out.println("======= testStartGameOnePlayer =======");
		// Add just one player
		GamePlayerCommand l_gameplayerCommand = new GamePlayerCommand();
		l_gameplayerCommand.addPlayer("Player_0");
		l_gameplayerCommand.execute();
		
		// Try to start the game with one player
		AssignCountriesCommand l_assignCountriesCommand = new AssignCountriesCommand();
		l_assignCountriesCommand.execute();
		
		// Not possible to start a game with just one player
		assertTrue(GameEngine.GetActivePhase() instanceof StartupPhase);
	}
	
	@Test
	@Order(3)
	void testStartGameMorePlayersThanCountries() {
		System.out.println("======= testStartGameMorePlayersThanCountries =======");
		GamePlayerCommand l_gameplayerCommand = new GamePlayerCommand();
		// Add more players than countries
		for (int i = 0; i == d_defaultMap.getCountries().size(); i++) {
			l_gameplayerCommand.addPlayer("Player_" + i);
		}
		l_gameplayerCommand.execute();
		
		// Try to start the game with one player
		AssignCountriesCommand l_assignCountriesCommand = new AssignCountriesCommand();
		l_assignCountriesCommand.execute();
		
		// Not possible to start a game with just one player
		assertTrue(GameEngine.GetActivePhase() instanceof StartupPhase);
	}
}
