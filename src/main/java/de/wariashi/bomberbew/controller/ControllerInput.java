package de.wariashi.bomberbew.controller;

import de.wariashi.bomberbew.model.Game;
import de.wariashi.bomberbew.model.Map;
import de.wariashi.bomberbew.model.projection.MapData;

/**
 * The {@link ControllerInput} contains some information about the state of the
 * {@link Game} so that a {@link Controller} can decide what to do next.
 */
public class ControllerInput {
	private MapData mapData;

	/**
	 * Creates a new {@link ControllerInput} which contains information about the
	 * map.
	 * 
	 * @param mapData a {@link MapData} object which contains information about the
	 *                {@link Map}
	 * @throws IllegalArgumentException if <code>mapData</code> is <code>null</code>
	 */
	public ControllerInput(MapData mapData) {
		if (mapData == null) {
			throw new IllegalArgumentException();
		}
		this.mapData = mapData;
	}

	/**
	 * Returns information about the {@link Map}. This value can never be
	 * <code>null</code>.
	 * 
	 * @return information about the {@link Map}
	 */
	public MapData getMapData() {
		return mapData;
	}
}
