package de.wariashi.bomberbew.model;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import de.wariashi.bomberbew.Clock;
import de.wariashi.bomberbew.controller.Controller;
import de.wariashi.bomberbew.controller.ControllerInput;
import de.wariashi.bomberbew.model.projection.MapData;
import de.wariashi.bomberbew.model.projection.PlayerData;

/**
 * A Game consists of a {@link Map map} and a list of {@link Player players}.
 */
public class Game {
	private Map map;
	private List<Player> players;
	private List<Controller> controllers;

	/**
	 * Creates a new Game.
	 * 
	 * @param map     the {@link Map map} of the game
	 * @param players a list of the {@link Player players} of the game
	 * @throws IllegalArgumentException if one of the arguments is <code>null</code>
	 *                                  or if the number of players does not match
	 *                                  the number of controllers
	 */
	public Game(Map map, List<Player> players, List<Controller> controllers) {
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
		// get projections of current state
		var mapData = new MapData(map);
		var playerData = new ArrayList<PlayerData>();
		for (int i = 0; i < players.size(); i++) {
			playerData.add(new PlayerData(players.get(i)));
		}

		// update controllers
		for (int playerIndex = 0; playerIndex < players.size(); playerIndex++) {
			// separate the player ...
			var player = players.get(playerIndex);

			// ... from their enemies
			var enemyData = new ArrayList<PlayerData>();
			for (int enemyIndex = 0; enemyIndex < players.size(); enemyIndex++) {
				if (enemyIndex != playerIndex) {
					enemyData.add(playerData.get(enemyIndex));
				}
			}

			// create controller input
			var controllerInput = new ControllerInput(mapData, playerData.get(playerIndex), enemyData);

			// update
			var controller = controllers.get(playerIndex);
			try {
				var controllerOutput = controller.update(controllerInput);
				player.step(controllerOutput);
			} catch (Exception exception) {
				Clock.stop();
				var message = exception.getMessage();
				var title = MessageFormat.format("Foul by {0}!", controller.getName());
				JOptionPane.showMessageDialog(null, message, title, JOptionPane.ERROR_MESSAGE);
				exception.printStackTrace();
			}
		}
	}
}
