package de.wariashi.bomberbew.model;

import java.text.MessageFormat;
import java.util.ArrayList;

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
	private Player[] players;
	private Controller[] controllers;

	/**
	 * Creates a new Game.
	 * 
	 * @param map         the {@link Map map} of the game
	 * @param players     an array of the {@link Player players} of the game
	 * @param controllers an array of the {@link Controller controllers} controlling
	 *                    the players
	 * @throws IllegalArgumentException if one of the arguments is <code>null</code>
	 *                                  or if the number of players does not match
	 *                                  the number of controllers
	 */
	public Game(Map map, Player[] players, Controller[] controllers) {
		if (map == null || players == null || controllers == null || players.length != controllers.length) {
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
	 * Returns an array of all {@link Player players} of the game.
	 * 
	 * @return an array of all {@link Player players} of the game
	 */
	public Player[] getPlayers() {
		return players;
	}

	/**
	 * Moves every {@link Player} by calling their "step" function.
	 */
	public void step() {
		map.step();

		// get projections of current state
		var mapData = new MapData(map);
		var playerData = new PlayerData[4];
		for (int i = 0; i < players.length; i++) {
			if (players[i] == null) {
				playerData[i] = null;
			} else {
				playerData[i] = new PlayerData(players[i]);
			}
		}

		// update controllers
		for (int playerIndex = 0; playerIndex < players.length; playerIndex++) {
			// separate the player ...
			var player = players[playerIndex];
			if (player == null) {
				continue;
			}

			// ... from their enemies
			var enemyData = new ArrayList<PlayerData>();
			for (int enemyIndex = 0; enemyIndex < players.length; enemyIndex++) {
				if (enemyIndex != playerIndex && playerData[enemyIndex] != null) {
					enemyData.add(playerData[enemyIndex]);
				}
			}

			// create controller input
			var controllerInput = new ControllerInput(mapData, playerData[playerIndex], enemyData);

			// update
			var controller = controllers[playerIndex];
			try {
				var controllerOutput = controller.update(controllerInput);
				player.step(controllerOutput);
				player.setImage(controller.getPlayerImage());
			} catch (Exception exception) {
				Clock.stop();
				var message = exception.getMessage();
				var title = MessageFormat.format("Foul by {0}!", controller.getName());
				JOptionPane.showMessageDialog(null, message, title, JOptionPane.ERROR_MESSAGE);
				exception.printStackTrace();
			}
		}
		checkForWinner();
	}

	public void updateControllers(Player[] players, Controller[] controllers) {
		if (players == null || controllers == null || players.length != controllers.length) {
			throw new IllegalArgumentException();
		}
		this.players = players;
		this.controllers = controllers;
	}

	private void checkForWinner() {
		var playersLeft = 0;
		var winner = "";
		for (int playerIndex = 0; playerIndex < players.length; playerIndex++) {
			var player = players[playerIndex];
			if (player != null && player.isAlive()) {
				playersLeft++;
				winner = controllers[playerIndex].getName();
			}
		}
		if (playersLeft == 0) {
			Clock.stop();
			var title = "Draw!";
			var message = "All players died.";
			JOptionPane.showMessageDialog(null, message, title, JOptionPane.INFORMATION_MESSAGE);
		} else if (playersLeft == 1) {
			Clock.stop();
			var title = "Someone won!";
			var message = winner + " won.";
			JOptionPane.showMessageDialog(null, message, title, JOptionPane.INFORMATION_MESSAGE);
		}
	}
}
