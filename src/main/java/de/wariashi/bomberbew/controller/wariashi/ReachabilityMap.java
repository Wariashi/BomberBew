package de.wariashi.bomberbew.controller.wariashi;

import de.wariashi.bomberbew.model.Material;
import de.wariashi.bomberbew.model.projection.MapData;

public class ReachabilityMap {
	private boolean[][] reachability;

	public ReachabilityMap(MapData map, int startX, int startY) {
		reachability = new boolean[map.getWidth()][map.getHeight()];
		calculateReachabilityForTile(map, startX, startY);
	}

	public boolean isReachable(int x, int y) {
		if (x < 0 || y < 0 || reachability.length <= x || reachability[0].length <= y) {
			return false;
		}
		return reachability[x][y];
	}

	private void calculateReachabilityForTile(MapData map, int tileX, int tileY) {
		if (map.getMaterial(tileX, tileY) == Material.EMPTY) {
			reachability[tileX][tileY] = true;
			// east
			if (!isReachable(tileX + 1, tileY)) {
				calculateReachabilityForTile(map, tileX + 1, tileY);
			}
			// north
			if (!isReachable(tileX, tileY - 1)) {
				calculateReachabilityForTile(map, tileX, tileY - 1);
			}
			// south
			if (!isReachable(tileX, tileY + 1)) {
				calculateReachabilityForTile(map, tileX, tileY + 1);
			}
			// west
			if (!isReachable(tileX - 1, tileY)) {
				calculateReachabilityForTile(map, tileX - 1, tileY);
			}
		}
	}
}
