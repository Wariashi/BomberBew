package de.wariashi.bomberbew.model;

import java.util.List;

import de.wariashi.bomberbew.controller.KeyboardController;

/**
 * A Game consists of a {@link Map map} and a list of {@link Player players}.
 */
public class Game {
	private Map map;
	private List<Player> players;
	private List<KeyboardController> controllers;

	/**
	 * Creates a new Game.
	 * 
	 * @param map     the {@link Map map} of the game
	 * @param players a list of the {@link Player players} of the game
	 * @throws IllegalArgumentException if one of the arguments is <code>null</code>
	 *                                  or if the number of players does not match
	 *                                  the number of controllers
	 */
	public Game(Map map, List<Player> players, List<KeyboardController> controllers) {
		if (map == null || players == null || controllers == null || players.size() != controllers.size()) {
			throw new IllegalArgumentException();
		}
		this.map = map;
		this.players = players;
		this.controllers = controllers;
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
		for (int i = 0; i < players.size(); i++) {
			var player = players.get(i);
			var controller = controllers.get(i);
			var controllerOutput = controller.update();
			player.step(controllerOutput);
		}
	}
}
