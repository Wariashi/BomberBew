package de.wariashi.bomberbew.controller.wariashi;

import java.awt.Point;
import java.awt.image.BufferedImage;

import de.wariashi.bomberbew.Textures;
import de.wariashi.bomberbew.controller.Controller;
import de.wariashi.bomberbew.controller.ControllerInput;
import de.wariashi.bomberbew.controller.ControllerOutput;
import de.wariashi.bomberbew.model.Direction;
import de.wariashi.bomberbew.model.projection.MapData;

public class Wariashi implements Controller {
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
		var output = new ControllerOutput();

		var dangerMap = new DangerMap(input.getMapData());

		var player = input.getPlayerData();
		var playerX = player.getTileX();
		var playerY = player.getTileY();
		if (dangerMap.isDangerous(playerX, playerY)) {
			output.setDirection(getDirectionToNearestSafeTile(input.getMapData(), playerX, playerY, dangerMap));
			return output;
		}

		var target = calculateTarget(input);
		if (target == null) {
			return output;
		}

		var pathfinding = new Pathfinding(input.getMapData(), target.x, target.y);
		if (pathfinding.isReachableFrom(target.x, target.y)) {

			var direction = pathfinding.getDirection(playerX, playerY);
			var nextTile = getNeighbor(playerX, playerY, direction);
			if (dangerMap.isDangerous(nextTile.x, nextTile.y)) {
				return output;
			}

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

	private Direction getDirectionToNearestSafeTile(MapData map, int currentX, int currentY, DangerMap dangerMap) {
		var pathfindingToPlayer = new Pathfinding(map, currentX, currentY);

		Point target = null;
		var distance = Integer.MAX_VALUE;
		for (int y = 0; y < map.getHeight(); y++) {
			for (int x = 0; x < map.getWidth(); x++) {
				if (!dangerMap.isDangerous(x, y) && pathfindingToPlayer.isReachableFrom(x, y)
						&& pathfindingToPlayer.getDistance(x, y) < distance) {
					target = new Point(x, y);
					distance = pathfindingToPlayer.getDistance(x, y);
				}
			}
		}

		if (target == null) {
			return null;
		}

		var pathfindingToTarget = new Pathfinding(map, target.x, target.y);
		return pathfindingToTarget.getDirection(currentX, currentY);
	}

	private Point getNeighbor(int x, int y, Direction direction) {
		if (direction == null) {
			return new Point(x, y);
		}
		switch (direction) {
		case NORTH:
			return new Point(x, y - 1);
		case NORTH_EAST:
			return new Point(x + 1, y - 1);
		case EAST:
			return new Point(x + 1, y);
		case SOUTH_EAST:
			return new Point(x + 1, y + 1);
		case SOUTH:
			return new Point(x, y + 1);
		case SOUTH_WEST:
			return new Point(x - 1, y + 1);
		case WEST:
			return new Point(x - 1, y);
		case NORTH_WEST:
			return new Point(x - 1, y - 1);
		default:
			return new Point(x, y);
		}
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
