package de.wariashi.bomberbew.controller.wariashi;

import java.awt.Point;

import de.wariashi.bomberbew.controller.Controller;
import de.wariashi.bomberbew.controller.ControllerInput;
import de.wariashi.bomberbew.controller.ControllerOutput;
import de.wariashi.bomberbew.model.Direction;

public class Wariashi implements Controller {
	private ReachabilityMap reachabilityMap;

	@Override
	public String getName() {
		return "Wariashi";
	}

	@Override
	public ControllerOutput update(ControllerInput input) {
		initializeReachabilityMap(input);

		var output = new ControllerOutput();

		var target = calculateTarget(input);
		if (target != null && reachabilityMap.isReachable(target.x, target.y)) {
			var direction = getDirectionToTarget(input, target);
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

	private Direction getDirectionToTarget(ControllerInput input, Point target) {
		if (input == null || target == null) {
			return null;
		}
		var player = input.getPlayerData();
		var distanceX = target.x - player.getTileX();
		var distanceY = target.y - player.getTileY();
		if (distanceX == 0 && distanceY == 0) {
			return null;
		} else if (distanceX == 0) {
			if (distanceY < 0) {
				return Direction.NORTH;
			} else {
				return Direction.SOUTH;
			}
		} else if (distanceY == 0) {
			if (distanceX < 0) {
				return Direction.WEST;
			} else {
				return Direction.EAST;
			}
		} else if (distanceX < 0 && distanceY < 0) {
			return Direction.NORTH_WEST;
		} else if (distanceX < 0 && distanceY > 0) {
			return Direction.SOUTH_WEST;
		} else if (distanceX > 0 && distanceY < 0) {
			return Direction.NORTH_EAST;
		} else {
			return Direction.SOUTH_EAST;
		}
	}

	private void initializeReachabilityMap(ControllerInput input) {
		var map = input.getMapData();
		var player = input.getPlayerData();
		var tileX = player.getTileX();
		var tileY = player.getTileY();
		reachabilityMap = new ReachabilityMap(map, tileX, tileY);
	}
}
