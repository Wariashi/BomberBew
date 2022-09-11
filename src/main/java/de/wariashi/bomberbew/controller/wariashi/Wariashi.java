package de.wariashi.bomberbew.controller.wariashi;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import de.wariashi.bomberbew.Textures;
import de.wariashi.bomberbew.controller.Controller;
import de.wariashi.bomberbew.controller.ControllerInput;
import de.wariashi.bomberbew.controller.ControllerOutput;
import de.wariashi.bomberbew.model.Direction;
import de.wariashi.bomberbew.model.projection.MapData;
import de.wariashi.bomberbew.model.projection.PlayerData;

public class Wariashi implements Controller {
	private List<PlayerData> enemies = new ArrayList<>();
	private MapData map;
	private int playerX;
	private int playerY;

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
		updateVariables(input);

		var output = new ControllerOutput();

		var dangerMap = new DangerMap(map);
		if (dangerMap.isDangerous(playerX, playerY)) {
			output.setDirection(getDirectionToNearestSafeTile(dangerMap));
			return output;
		}

		var target = calculateTarget();
		if (target == null) {
			return output;
		}

		var pathfinding = new Pathfinding(map, target.x, target.y);
		if (pathfinding.isReachableFrom(target.x, target.y)) {

			var direction = pathfinding.getDirectionFrom(playerX, playerY);
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

	private Point calculateTarget() {

		if (enemies.isEmpty()) {
			return null;
		}

		var firstEnemy = enemies.get(0);
		return new Point(firstEnemy.getTileX(), firstEnemy.getTileY());
	}

	private Direction getDirectionToNearestSafeTile(DangerMap dangerMap) {
		var pathfindingToPlayer = new Pathfinding(map, playerX, playerY);

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
		return pathfindingToTarget.getDirectionFrom(playerX, playerY);
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

	private void updateVariables(ControllerInput input) {
		if (input == null) {
			return;
		}

		enemies = input.getEnemyData();

		map = input.getMapData();

		var player = input.getPlayerData();
		playerX = player.getTileX();
		playerY = player.getTileY();
	}
}
