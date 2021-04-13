package ca.concordia.risk.utils;

import java.io.FileNotFoundException;
import java.io.IOException;
import ca.concordia.risk.game.GameMap;

/**
 * This class implements Conquest MapLoader Adapter
 * 
 * @author Sindu
 */

public class ConquestMapLoaderAdapter extends DominationMapLoader {
    private ConquestMapLoader loader;

    public ConquestMapLoaderAdapter(ConquestMapLoader loader) {
        this.loader = loader;
    }

    @Override
    public GameMap LoadMap(String p_fileName) throws FileParsingException, FileNotFoundException {        
		try {
            return loader.LoadMap(p_fileName);			
		} catch (Exception e) {
			throw new FileParsingException(e.getMessage().substring(18));
		}
    }

    @Override
    public void SaveMap(String p_fileName, GameMap p_map) throws IOException {
        loader.SaveMap(p_fileName, p_map);
    }
}