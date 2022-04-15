package de.wariashi.bomberbew;

public class Map {
	private static final int WIDTH = 15;
	private static final int HEIGHT = 11;
	private boolean[][] walls = new boolean[WIDTH][HEIGHT];

	public Map() {
		for (var y = 0; y < HEIGHT; y++) {
			for (var x = 0; x < WIDTH; x++) {
				var border = x == 0 || y == 0 || x == WIDTH - 1 || y == HEIGHT - 1;
				var pillar = (x % 2 == 0) && (y % 2 == 0);
				walls[x][y] = border || pillar;
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
		return walls[x][y];
	}
}
