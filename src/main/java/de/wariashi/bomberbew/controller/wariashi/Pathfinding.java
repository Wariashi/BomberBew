package de.wariashi.bomberbew.controller.wariashi;

import de.wariashi.bomberbew.model.Direction;
import de.wariashi.bomberbew.model.projection.MapData;

public class Pathfinding {
	private int[][] distance;

	private static final int UNDEFINED = Integer.MAX_VALUE;

	public Pathfinding(MapData map, int targetX, int targetY) {
		initializeDistanceMap(map);

		distance[targetX][targetY] = 0;
		updateDistance(map, targetX - 1, targetY);
		updateDistance(map, targetX + 1, targetY);
		updateDistance(map, targetX, targetY - 1);
		updateDistance(map, targetX, targetY + 1);
	}

	public Direction getDirectionFrom(int startX, int startY) {
		int currentDistance = distance[startX][startY];

		// check east
		if (startX < distance.length - 1 && distance[startX + 1][startY] != UNDEFINED
				&& distance[startX + 1][startY] < currentDistance) {
			return Direction.EAST;
		}

		// check north
		if (0 < startY && distance[startX][startY - 1] != UNDEFINED && distance[startX][startY - 1] < currentDistance) {
			return Direction.NORTH;
		}

		// check south
		if (startY < distance[startX].length - 1 && distance[startX][startY + 1] != UNDEFINED
				&& distance[startX][startY + 1] < currentDistance) {
			return Direction.SOUTH;
		}

		// check west
		if (0 < startX && distance[startX - 1][startY] != UNDEFINED && distance[startX - 1][startY] < currentDistance) {
			return Direction.WEST;
		}

		return null;
	}

	public int getDistance(int startX, int startY) {
		// check for map border
		if (startX < 0 || startY < 0 || distance.length <= startX || distance[startX].length <= startY) {
			return UNDEFINED;
		}

		return distance[startX][startY];
	}

	public boolean isReachableFrom(int startX, int startY) {
		return distance[startX][startY] != UNDEFINED;
	}

	private void initializeDistanceMap(MapData map) {
		int height = map.getHeight();
		int width = map.getWidth();
		distance = new int[width][height];
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				distance[x][y] = UNDEFINED;
			}
		}
	}

	private void updateDistance(MapData map, int tileX, int tileY) {
		// check for map border
		if (tileX < 0 || tileY < 0 || map.getWidth() <= tileX || map.getHeight() <= tileY) {
			return;
		}

		// check for obstacles
		if (map.getMaterial(tileX, tileY).isSolid()) {
			return;
		}

		boolean updateNeighbors = false;

		// check east
		if (tileX < map.getWidth() - 1 && distance[tileX + 1][tileY] != UNDEFINED
				&& distance[tileX + 1][tileY] + 1 < distance[tileX][tileY]) {
			distance[tileX][tileY] = distance[tileX + 1][tileY] + 1;
			updateNeighbors = true;
		}

		// check north
		if (0 < tileY && distance[tileX][tileY - 1] != UNDEFINED
				&& distance[tileX][tileY - 1] + 1 < distance[tileX][tileY]) {
			distance[tileX][tileY] = distance[tileX][tileY - 1] + 1;
			updateNeighbors = true;
		}

		// check south
		if (tileY < map.getHeight() - 1 && distance[tileX][tileY + 1] != UNDEFINED
				&& distance[tileX][tileY + 1] + 1 < distance[tileX][tileY]) {
			distance[tileX][tileY] = distance[tileX][tileY + 1] + 1;
			updateNeighbors = true;
		}

		// check west
		if (0 < tileX && distance[tileX - 1][tileY] != UNDEFINED
				&& distance[tileX - 1][tileY] + 1 < distance[tileX][tileY]) {
			distance[tileX][tileY] = distance[tileX - 1][tileY] + 1;
			updateNeighbors = true;
		}

		if (updateNeighbors) {
			updateDistance(map, tileX - 1, tileY);
			updateDistance(map, tileX + 1, tileY);
			updateDistance(map, tileX, tileY - 1);
			updateDistance(map, tileX, tileY + 1);
		}
	}
}
