package ca.concordia.risk.utils;

import java.io.FileNotFoundException;
import java.io.IOException;
import ca.concordia.risk.game.GameMap;
import ca.concordia.risk.utils.MapLoader.FileParsingException;

/**
 * This class represents an Adapter from <code>ConquestMapLoader</code> to
 * <code>DominationMapLoader</code>.
 */
public class ConquestMapLoaderAdapter extends DominationMapLoader {
	private ConquestMapLoader d_loader;

	/**
	 * Creates a new adapter, wrapping a given <code>ConquestMapLoader</code>
	 * object.
	 * 
	 * @param p_loader <code>ConquestMapLoader</code> to wrap.
	 */
	public ConquestMapLoaderAdapter(ConquestMapLoader p_loader) {
		this.d_loader = p_loader;
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * Delegates the map loading to the adaptee loader while providing an interface
	 * of the target.
	 */
	@Override
	public GameMap LoadMap(String p_fileName) throws FileParsingException, FileNotFoundException {
		return d_loader.loadMap(p_fileName);
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * Delegates the map saving to the adaptee loader while providing an interface
	 * of the target.
	 */
	@Override
	public void SaveMap(String p_fileName, GameMap p_map) throws IOException {
		d_loader.saveMap(p_fileName, p_map);
	}
}