package de.wariashi.bomberbew.controller;

import java.util.Collections;
import java.util.List;

import de.wariashi.bomberbew.model.Game;
import de.wariashi.bomberbew.model.Map;
import de.wariashi.bomberbew.model.Player;
import de.wariashi.bomberbew.model.projection.MapData;
import de.wariashi.bomberbew.model.projection.PlayerData;

/**
 * The {@link ControllerInput} contains some information about the state of the
 * {@link Game} so that a {@link Controller} can decide what to do next.
 */
public class ControllerInput {
	private MapData mapData;
	private PlayerData playerData;
	private List<PlayerData> enemyData;

	/**
	 * Creates a new {@link ControllerInput} which contains information about the
	 * {@link Map} and the {@link Player}s.
	 * 
	 * @param mapData    a {@link MapData} object which contains information about
	 *                   the {@link Map}
	 * @param playerData a {@link PlayerData} object which contains information
	 *                   about the {@link Player} that the {@link Controller} is
	 *                   controlling
	 * @param enemyData  a {@link List} of {@link PlayerData} objects which contain
	 *                   information about the {@link Player}s that the
	 *                   {@link Controller} is <b>not</b> controlling
	 * @throws IllegalArgumentException if <code>mapData</code>,
	 *                                  <code>playerData</code> or
	 *                                  <code>enemyData</code> is <code>null</code>
	 *                                  or if <code>enemyData</code> contains the
	 *                                  value <code>null</code>
	 */
	public ControllerInput(MapData mapData, PlayerData playerData, List<PlayerData> enemyData) {
		if (mapData == null || playerData == null || enemyData == null) {
			throw new IllegalArgumentException();
		}
		for (PlayerData enemy : enemyData) {
			if (enemy == null) {
				throw new IllegalArgumentException();
			}
		}
		this.mapData = mapData;
		this.playerData = playerData;
		this.enemyData = Collections.unmodifiableList(enemyData);
	}

	/**
	 * Returns information about the {@link Player}s that the {@link Controller} is
	 * <b>not</b> controlling. This value can never be <code>null</code>. Also this
	 * {@link List} does not contain the value <code>null</code>.
	 * 
	 * @return information about the {@link Player}s that the {@link Controller} is
	 *         <b>not</b> controlling
	 */
	public List<PlayerData> getEnemyData() {
		return enemyData;
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

	/**
	 * Returns information about the {@link Player} that the {@link Controller} is
	 * controlling. This value can never be <code>null</code>.
	 * 
	 * @return information about the {@link Player} that the {@link Controller} is
	 *         controlling
	 */
	public PlayerData getPlayerData() {
		return playerData;
	}
}
