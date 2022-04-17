package de.wariashi.bomberbew.model;

/**
 * The map is a two-dimensional array of tiles which consist of different
 * {@link Material materials}.
 */
public class Map {
	private int width;
	private int height;
	private Material[][] materials;

	/**
	 * Creates a new {@link Map}.
	 * 
	 * @param width        the width of the map in tiles
	 * @param height       the height of the map in tiles
	 * @param brickDensity a value between 0.0 and 1.0 which describes the
	 *                     percentage of the map that should be filled with
	 *                     {@link Material#BRICK bricks}
	 * @throws NegativeArraySizeException if the width or height is negative
	 */
	public Map(int width, int height, double brickDensity) {
		this.width = width;
		this.height = height;

		initializeMaterials();
		addBricks(brickDensity);
		addPillars();
		clearCorners();
	}

	/**
	 * Returns the height of the map in tiles.
	 * 
	 * @return the height of the map
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * Returns the {@link Material} of the tile at the given coordinate. If the
	 * requested coordinates are outside of the map, {@link Material#CONCRETE} will
	 * be returned.
	 * 
	 * @param x the x coordinate of the tile
	 * @param y the y coordinate of the tile
	 * @return the {@link Material} of the tile
	 */
	public Material getMaterial(int x, int y) {
		if (x < 0 || width <= x || y < 0 || height <= y) {
			return Material.CONCRETE;
		}
		return materials[x][y];
	}

	/**
	 * Returns the width of the map in tiles.
	 * 
	 * @return the width of the map
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * Adds {@link Material#BRICK bricks} at random positions to the map.
	 * 
	 * @param brickDensity a value between 0.0 and 1.0 which describes the
	 *                     percentage of the map that should be filled with
	 *                     {@link Material#BRICK bricks}
	 */
	private void addBricks(double brickDensity) {
		for (var y = 0; y < height; y++) {
			for (var x = 0; x < width; x++) {
				if (Math.random() < brickDensity) {
					materials[x][y] = Material.BRICK;
				}
			}
		}
	}

	/**
	 * Adds {@link Material#CONCRETE concrete} pillars to the map.
	 */
	private void addPillars() {
		for (var y = 0; y < height; y++) {
			for (var x = 0; x < width; x++) {
				if ((x % 2 != 0) && (y % 2 != 0)) {
					materials[x][y] = Material.CONCRETE;
				}
			}
		}
	}

	/**
	 * Removes bricks and concrete from the corners of the map so that can
	 * {@link Player players} spawn there.
	 */
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

	/**
	 * Initializes the map with {@link Material#EMPTY empty} space.
	 * 
	 * @throws NegativeArraySizeException if the width or height is negative
	 */
	private void initializeMaterials() {
		materials = new Material[width][height];
		for (var y = 0; y < height; y++) {
			for (var x = 0; x < width; x++) {
				materials[x][y] = Material.EMPTY;
			}
		}
	}
}
