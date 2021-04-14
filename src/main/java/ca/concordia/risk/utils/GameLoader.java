package ca.concordia.risk.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map.Entry;

import ca.concordia.risk.GameEngine;
import ca.concordia.risk.game.Card;
import ca.concordia.risk.game.Country;
import ca.concordia.risk.game.GameMap;
import ca.concordia.risk.game.Player;
import ca.concordia.risk.game.phases.GameplayPhase;
import ca.concordia.risk.game.phases.MapEditorPhase;
import ca.concordia.risk.game.phases.Phase;
import ca.concordia.risk.game.phases.StartupPhase;
import ca.concordia.risk.game.strategies.PlayerStrategy;
import ca.concordia.risk.utils.MapLoader.FileParsingException;

/**
 * This class provides functionality to save and load the game as its being
 * played.
 */
public class GameLoader {

	private static final String d_SaveDirectoryPath = "save/";

	/**
	 * Saves the current game state into the save file with the given path.
	 * 
	 * @param p_saveFilePath path to the save file.
	 * @throws GameLoaderException thrown if an error occurs while saving.
	 */
	public static void SaveGame(String p_saveFilePath) throws GameLoaderException {
		// Ensure the active phase is Gameplay
		if (!(GameEngine.GetActivePhase() instanceof GameplayPhase)) {
			throw new GameLoaderException("invalid state - can only save game in Gameplay Phase");
		}

		// Create the file path if it does not already exists
		File l_saveFile = new File(d_SaveDirectoryPath + p_saveFilePath);
		if (!l_saveFile.exists()) {
			l_saveFile.getParentFile().mkdirs();
		}

		// Create the save data
		SaveData l_saveData = BuildSaveData();

		// Write the save data to the save file
		try (ObjectOutputStream l_objectStream = new ObjectOutputStream(new FileOutputStream(l_saveFile))) {
			l_objectStream.writeObject(l_saveData);
		} catch (IOException l_e) {
			throw new GameLoaderException("error writing to the save file " + p_saveFilePath, l_e);
		}
	}

	/**
	 * Load the game state from the save with the given path.
	 * 
	 * @param p_saveFilePath path to the save file.
	 * @throws GameLoaderException thrown if an error occurs while loading.
	 */
	public static void LoadGame(String p_saveFilePath) throws GameLoaderException {
		// Ensure the active phase is Gameplay. If it's not, switch to Gameplay phase
		Phase l_activePhase = GameEngine.GetActivePhase();
		if (l_activePhase instanceof MapEditorPhase) {
			GameEngine.SwitchToNextPhase();
			GameEngine.SwitchToNextPhase();
		} else if (l_activePhase instanceof StartupPhase) {
			GameEngine.SwitchToNextPhase();
		}

		// Ensure the save file exists
		File l_saveFile = new File(d_SaveDirectoryPath + p_saveFilePath);
		if (!l_saveFile.exists()) {
			throw new GameLoaderException("save file " + p_saveFilePath + " does not exist");
		}

		// Load and restore the save data
		try (ObjectInputStream l_objectStream = new ObjectInputStream(new FileInputStream(l_saveFile))) {
			// Load the save data from file
			Object l_saveObject = l_objectStream.readObject();
			// Ensure the loaded object correctly represents the save data
			if (l_saveObject instanceof SaveData) {
				// Restore the save data
				SaveData l_saveData = (SaveData) l_saveObject;
				RestoreSaveData(l_saveData);
			} else {
				// Report invalid save data file
				throw new GameLoaderException("invalid save file " + p_saveFilePath, null);
			}
		} catch (IOException | ClassNotFoundException l_e) {
			throw new GameLoaderException("error reading from the save file " + p_saveFilePath, l_e);
		}
	}

	/**
	 * Builds the save data structure from the current game state.
	 * 
	 * @return the built save data.
	 */
	private static SaveData BuildSaveData() {
		// Ensure the active phase is Gameplay
		if (!(GameEngine.GetActivePhase() instanceof GameplayPhase)) {
			throw new IllegalStateException("Invalid state: can only load game in Gameplay Phase");
		}

		// Create the new save state
		SaveData l_saveData = new SaveData();

		// Save the active map filename
		l_saveData.d_mapFilename = GameEngine.GetActiveMapFile();

		// Save turn number
		GameplayPhase l_phase = (GameplayPhase) GameEngine.GetActivePhase();
		l_saveData.d_turn = l_phase.getTurnNumber();

		// Save player data for each players
		Collection<Player> l_players = GameEngine.GetPlayers();
		for (Player l_player : l_players) {
			// Create new player data
			SaveData.PlayerData l_playerData = new SaveData.PlayerData();

			// Save player name and strategy
			l_playerData.d_name = l_player.getName();
			l_playerData.d_strategy = l_player.GetStrategy().getClass().getName();

			// Save player cards
			for (Card l_card : l_player.getCards()) {
				l_playerData.d_cards.add(l_card.toString());
			}

			// Save countries owned by the player as well as the number of armies deployed
			// on them
			for (Country l_country : l_player.getCountries()) {
				l_playerData.d_ownedCountryDataList.put(l_country.getName(), l_country.getArmies());
			}

			// Add player data to the list of player data
			l_saveData.d_playerDataList.add(l_playerData);
		}

		// Save country data for neutral player
		l_saveData.d_neutralPlayerData = new SaveData.PlayerData();
		for (Country l_country : GameEngine.GetNeutralPlayer().getCountries()) {
			l_saveData.d_neutralPlayerData.d_ownedCountryDataList.put(l_country.getName(), l_country.getArmies());
		}

		// Return built save data
		return l_saveData;
	}

	/**
	 * Restores the game state using the given save data.
	 * 
	 * @param p_saveData save data to restore.
	 * @throws GameLoaderException thrown if an error occurs while restoring the
	 *                             save data.
	 */
	private static void RestoreSaveData(SaveData p_saveData) throws GameLoaderException {
		// Load the map file
		try {
			GameMap l_map = MapLoader.LoadMap(p_saveData.d_mapFilename);
			GameEngine.SetMap(l_map);
			GameEngine.SetActiveMapFile(p_saveData.d_mapFilename);

			// Restore the game turn
			GameplayPhase l_phase = (GameplayPhase) GameEngine.GetActivePhase();
			l_phase.setTurnNumber(p_saveData.d_turn);

			// Restore players and countries
			GameEngine.ClearPlayers();
			for (SaveData.PlayerData l_playerData : p_saveData.d_playerDataList) {
				// Create a player with save name
				Player l_player = new Player(l_playerData.d_name);

				// Restore player strategy
				Class<?> l_strategyClass;
				l_strategyClass = Class.forName(l_playerData.d_strategy);
				PlayerStrategy l_strategy = (PlayerStrategy) l_strategyClass.getConstructor(Player.class)
						.newInstance(l_player);
				l_player.SetStrategy(l_strategy);

				// Restore player cards
				for (String l_card : l_playerData.d_cards) {
					l_player.addCard(Card.getCardFromString(l_card));
				}

				// Add the player to the game
				GameEngine.AddPlayer(l_player);

				// Restore country ownership and armies
				for (Entry<String, Integer> l_countryData : l_playerData.d_ownedCountryDataList.entrySet()) {
					// Find the target country
					Country l_country = l_map.getCountry(l_countryData.getKey());
					// Restore armies
					l_country.addArmies(l_countryData.getValue());
					// Restore ownership
					l_player.addCountry(l_country);
					l_country.setOwner(l_player);
				}
			}

			// Restore neutral player countries
			Player l_neutralPlayer = GameEngine.GetNeutralPlayer();
			SaveData.PlayerData l_neutralPlayerData = p_saveData.d_neutralPlayerData;
			for (Entry<String, Integer> l_countryData : l_neutralPlayerData.d_ownedCountryDataList.entrySet()) {
				// Find the target country
				Country l_country = l_map.getCountry(l_countryData.getKey());
				// Restore armies
				l_country.addArmies(l_countryData.getValue());
				// Restore ownership
				l_neutralPlayer.addCountry(l_country);
				l_country.setOwner(l_neutralPlayer);
			}
		} catch (FileNotFoundException | FileParsingException l_e) {
			// Report map loading error
			throw new GameLoaderException("failed to load the map file " + p_saveData.d_mapFilename, l_e);
		} catch (ReflectiveOperationException l_e) {
			// Report reflection error when restoring the strategy class
			throw new GameLoaderException("failed restore the strategy class", l_e);
		}
	}

	/**
	 * Serializable data class that store the game save data.
	 */
	private static class SaveData implements Serializable {

		// Coding convention violated, because Serial ID has to have the exact name as
		// required by Serializable
		private static final long serialVersionUID = 5396464616800481951L;

		private int d_turn;
		private String d_mapFilename;
		private ArrayList<PlayerData> d_playerDataList = new ArrayList<PlayerData>();
		PlayerData d_neutralPlayerData;

		/**
		 * Serializable data class that stores the save data of a Player.
		 */
		private static class PlayerData implements Serializable {

			// Coding convention violated, because Serial ID has to have the exact name as
			// required by Serializable
			private static final long serialVersionUID = 7598765309358696165L;

			private String d_name;
			private String d_strategy;
			private ArrayList<String> d_cards = new ArrayList<String>();
			private HashMap<String, Integer> d_ownedCountryDataList = new HashMap<String, Integer>();
		}
	}

	/**
	 * A custom <code>Exception</code> class thrown when a game saving/loading error
	 * occurs.
	 */
	@SuppressWarnings("serial")
	public static class GameLoaderException extends Exception {

		/**
		 * This constructor calls the constructor of the Exception class and sets a
		 * custom file exception message.
		 * 
		 * @param p_message contains the custom file exception message.
		 */
		public GameLoaderException(String p_message) {
			super("Error when saving/loading a game: " + p_message);
		}

		/**
		 * This constructor calls the constructor of the Exception class and sets a
		 * custom file exception message, as well the original cause of the exception.
		 * 
		 * @param p_message contains the custom file exception message.
		 * @param p_cause   original cause of the exception.
		 */
		public GameLoaderException(String p_message, Throwable p_cause) {
			super("Error when saving/loading a game: " + p_message, p_cause);
		}
	}
}
