package de.wariashi.bomberbew.controller.wariashi;

import java.awt.Point;

import de.wariashi.bomberbew.controller.Controller;
import de.wariashi.bomberbew.controller.ControllerInput;
import de.wariashi.bomberbew.controller.ControllerOutput;

public class Wariashi implements Controller {
	private ReachabilityMap reachabilityMap;

	@Override
	public String getName() {
		return "Wariashi";
	}

	@Override
	public ControllerOutput update(ControllerInput input) {
		var target = calculateTarget(input);
		if (target == null) {
			return new ControllerOutput();
		}

		initializeReachabilityMap(input);

		var output = new ControllerOutput();
		if (reachabilityMap.isReachable(target.x, target.y)) {
			var pathfinding = new Pathfinding(input.getMapData(), target.x, target.y);

			var player = input.getPlayerData();
			var playerX = player.getTileX();
			var playerY = player.getTileY();

			var direction = pathfinding.getDirection(playerX, playerY);

			output.setDirection(direction);
		}

		return output;
	}

	private Point calculateTarget(ControllerInput input) {
		if (input == null) {
			return null;
		}

		var enemies = input.getEnemyData();
		if (enemies.isEmpty()) {
			return null;
		}

		var firstEnemy = enemies.get(0);
		return new Point(firstEnemy.getTileX(), firstEnemy.getTileY());
	}

	private void initializeReachabilityMap(ControllerInput input) {
		var map = input.getMapData();
		var player = input.getPlayerData();
		var tileX = player.getTileX();
		var tileY = player.getTileY();
		reachabilityMap = new ReachabilityMap(map, tileX, tileY);
	}
}
