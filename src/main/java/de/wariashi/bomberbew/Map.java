package de.wariashi.bomberbew;

public class Map {
	private static final int WIDTH = 13;
	private static final int HEIGHT = 9;
	private boolean[][] walls = new boolean[WIDTH][HEIGHT];

	public Map() {
		for (var y = 0; y < HEIGHT; y++) {
			for (var x = 0; x < WIDTH; x++) {
				walls[x][y] = (x % 2 != 0) && (y % 2 != 0);
			}
		}
	}

	public int getHeight() {
		return HEIGHT;
	}

	public int getWidth() {
		return WIDTH;
	}

	public boolean isWall(int x, int y) {
		if (x < 0 || WIDTH <= x || y < 0 || HEIGHT <= y) {
			return true;
		}
		return walls[x][y];
	}
}
