package de.wariashi.bomberbew.controller.wariashi;

import de.wariashi.bomberbew.model.Material;
import de.wariashi.bomberbew.model.projection.MapData;

public class DangerMap {
	private boolean[][] danger;
	private MapData map;

	public DangerMap(MapData map) {
		this.map = map;
		danger = new boolean[map.getWidth()][map.getHeight()];

		for (int y = 0; y < map.getHeight(); y++) {
			for (int x = 0; x < map.getWidth(); x++) {
				var material = map.getMaterial(x, y);
				if (material == Material.EXPLOSION) {
					danger[x][y] = true;
				} else if (material == Material.BOMB) {
					var bomb = map.getBomb(x, y);
					if (bomb != null) {
						var range = bomb.getRange();
						addBombDangers(x, y, range);
					}
				}
			}
		}
	}

	private DangerMap(DangerMap original) {
		this.map = original.map;

		var originalDanger = original.danger;
		int width = originalDanger.length;
		int height = originalDanger[0].length;
		danger = new boolean[width][height];
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				danger[x][y] = originalDanger[x][y];
			}
		}
	}

	public boolean isDangerous(int x, int y) {
		return danger[x][y];
	}

	public DangerMap withAdditionalBomb(int x, int y, int range) {
		var copy = new DangerMap(this);
		copy.addBombDangers(x, y, range);
		return copy;
	}

	private void addBombDangers(int x, int y, int range) {
		danger[x][y] = true;

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
