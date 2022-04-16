package de.wariashi.bomberbew;

public class Map {
	private final int width;
	private final int height;
	private final boolean[][] walls;

	public Map(int width, int height) {
		this.width = width;
		this.height = height;
		walls = new boolean[width][height];

		for (var y = 0; y < height; y++) {
			for (var x = 0; x < width; x++) {
				walls[x][y] = (x % 2 != 0) && (y % 2 != 0);
			}
		}
	}

	public int getHeight() {
		return height;
	}

	public int getWidth() {
		return width;
	}

	public boolean isWall(int x, int y) {
		if (x < 0 || width <= x || y < 0 || height <= y) {
			return true;
		}
		return walls[x][y];
	}
}
