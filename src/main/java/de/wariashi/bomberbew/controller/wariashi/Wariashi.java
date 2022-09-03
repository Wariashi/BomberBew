package de.wariashi.bomberbew.controller.wariashi;

import java.awt.Point;
import java.awt.image.BufferedImage;

import de.wariashi.bomberbew.Textures;
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
	public BufferedImage getPlayerImage() {
		return Textures.getWariashi();
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
			if (pathfinding.getDistance(playerX, playerY) <= 1) {
				output.setDropBomb(true);
				output.setDirection(invert(direction));
			} else {
				output.setDirection(direction);
			}
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

	private Direction invert(Direction direction) {
		if (direction == null) {
			return null;
		}
		switch (direction) {
		case NORTH:
			return Direction.SOUTH;
		case NORTH_EAST:
			return Direction.SOUTH_WEST;
		case EAST:
			return Direction.WEST;
		case SOUTH_EAST:
			return Direction.NORTH_WEST;
		case SOUTH:
			return Direction.NORTH;
		case SOUTH_WEST:
			return Direction.NORTH_EAST;
		case WEST:
			return Direction.EAST;
		case NORTH_WEST:
			return Direction.SOUTH_EAST;
		default:
			return null;
		}
	}
}
