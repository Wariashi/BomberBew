package de.wariashi.bomberbew.model;

import java.util.List;

/**
 * A Game consists of a {@link Map map} and a list of {@link Player players}.
 */
public class Game {
	private Map map;
	private List<Player> players;

	/**
	 * Creates a new Game.
	 * 
	 * @param map     the {@link Map map} of the game
	 * @param players a list of the {@link Player players} of the game
	 * @throws IllegalArgumentException if <code>map</code> or <code>players</code>
	 *                                  is <code>null</code>
	 */
	public Game(Map map, List<Player> players) {
		if (map == null || players == null) {
			throw new IllegalArgumentException();
		}
		this.map = map;
		this.players = players;
	}

	/**
	 * Returns the {@link Map map} of the game.
	 * 
	 * @return the {@link Map map} of the game
	 */
	public Map getMap() {
		return map;
	}

	/**
	 * Returns the list of all {@link Player players} of the game.
	 * 
	 * @return the list of all {@link Player players} of the game
	 */
	public List<Player> getPlayers() {
		return players;
	}

	/**
	 * Moves every {@link Player} by calling their "step" function.
	 */
	public void step() {
		var iterator = players.iterator();
		while (iterator.hasNext()) {
			var player = iterator.next();
			player.step();
		}
	}
}
