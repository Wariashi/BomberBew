package de.wariashi.bomberbew.controller.wariashi;

import de.wariashi.bomberbew.model.Material;
import de.wariashi.bomberbew.model.projection.MapData;

public class DangerMap {
	private boolean[][] danger;

	public DangerMap(MapData map) {
		danger = new boolean[map.getWidth()][map.getHeight()];

		for (int y = 0; y < map.getHeight(); y++) {
			for (int x = 0; x < map.getWidth(); x++) {
				var material = map.getMaterial(x, y);
				if (material == Material.EXPLOSION) {
					danger[x][y] = true;
				} else if (material == Material.BOMB) {
					addBombDangers(map, x, y);
				}
			}
		}
	}

	public boolean isDangerous(int x, int y) {
		return danger[x][y];
	}

	private void addBombDangers(MapData map, int x, int y) {
		var bomb = map.getBomb(x, y);
		if (bomb == null) {
			return;
		}

		danger[x][y] = true;
		var range = bomb.getRange();

		// east
		for (int i = 1; i <= range; i++) {
			if (x + i < map.getWidth()) {
				if (map.getMaterial(x + i, y).isSolid()) {
					break;
				}
				danger[x + i][y] = true;
			}
		}

		// north
		for (int i = 1; i <= range; i++) {
			if (0 <= y - 1) {
				if (map.getMaterial(x, y - i).isSolid()) {
					break;
				}
				danger[x][y - i] = true;
			}
		}

		// south
		for (int i = 1; i <= range; i++) {
			if (y + i < map.getHeight()) {
				if (map.getMaterial(x, y + i).isSolid()) {
					break;
				}
				danger[x][y + i] = true;
			}
		}

		// west
		for (int i = 1; i <= range; i++) {
			if (0 <= x - 1) {
				if (map.getMaterial(x - i, y).isSolid()) {
					break;
				}
				danger[x - i][y] = true;
			}
		}
	}
}
