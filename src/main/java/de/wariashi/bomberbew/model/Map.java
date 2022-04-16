package de.wariashi.bomberbew.model;

public class Map {
	private int width;
	private int height;
	private Material[][] materials;

	public Map(int width, int height) {
		this.width = width;
		this.height = height;

		initializeMaterials();
		addBricks();
		addPillars();
		clearCorners();
	}

	public int getHeight() {
		return height;
	}

	public Material getMaterial(int x, int y) {
		if (x < 0 || width <= x || y < 0 || height <= y) {
			return Material.CONCRETE;
		}
		return materials[x][y];
	}

	public int getWidth() {
		return width;
	}

	private void addBricks() {
		for (var y = 0; y < height; y++) {
			for (var x = 0; x < width; x++) {
				if (Math.random() < 0.75) {
					materials[x][y] = Material.BRICK;
				}
			}
		}
	}

	private void addPillars() {
		for (var y = 0; y < height; y++) {
			for (var x = 0; x < width; x++) {
				if ((x % 2 != 0) && (y % 2 != 0)) {
					materials[x][y] = Material.CONCRETE;
				}
			}
		}
	}

	private void clearCorners() {
		// top left
		materials[0][0] = Material.EMPTY;
		materials[1][0] = Material.EMPTY;
		materials[0][1] = Material.EMPTY;

		// top right
		materials[width - 2][0] = Material.EMPTY;
		materials[width - 1][0] = Material.EMPTY;
		materials[width - 1][1] = Material.EMPTY;

		// bottom left
		materials[0][height - 2] = Material.EMPTY;
		materials[0][height - 1] = Material.EMPTY;
		materials[1][height - 1] = Material.EMPTY;

		// bottom right
		materials[width - 1][height - 2] = Material.EMPTY;
		materials[width - 2][height - 1] = Material.EMPTY;
		materials[width - 1][height - 1] = Material.EMPTY;
	}

	private void initializeMaterials() {
		materials = new Material[width][height];
		for (var y = 0; y < height; y++) {
			for (var x = 0; x < width; x++) {
				materials[x][y] = Material.EMPTY;
			}
		}
	}
}
