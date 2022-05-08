package de.wariashi.bomberbew.model;

import de.wariashi.bomberbew.Clock;

/**
 * The map is a two-dimensional array of tiles which consist of different
 * {@link Material materials}.
 */
public class Map {
	private int width;
	private int height;
	private Material[][] materials;
	private int[][] bombTimers;
	private int[][] explosionTimers;

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

		bombTimers = new int[width][height];
		explosionTimers = new int[width][height];

		// initialize map
		initializeMaterials();
		addBricks(brickDensity);
		addPillars();
		clearCorners();
	}

	/**
	 * Drops a {@ink Material#BOMB bomb} at the given location. If the given
	 * coordinates are outside of the map, this method will fail silently.
	 * 
	 * @param x the x coordinate
	 * @param y the y coordinate
	 */
	public void dropBomb(int x, int y) {
		if (x < 0 || width <= x || y < 0 || height <= y) {
			return;
		}
		materials[x][y] = Material.BOMB;
		bombTimers[x][y] = getIgnitionDuration();
	}

	/**
	 * Returns the number of ticks a {@ink Material#BOMB bomb} has remaining before
	 * exploding at the given location. A value of 0 indicates that there is no
	 * {@ink Material#BOMB bomb} at all. If the given coordinates are outside of the
	 * map, this method will fail silently.
	 * 
	 * @param x the x coordinate
	 * @param y the y coordinate
	 */
	public int getBombTimer(int x, int y) {
		if (x < 0 || width <= x || y < 0 || height <= y) {
			return 0;
		}
		return bombTimers[x][y];
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
	 * Returns the number of ticks that a {@ink Material#BOMB bomb} will stay in its
	 * place before exploding.
	 * 
	 * @return the number of ticks that a {@ink Material#BOMB bomb} will stay in its
	 *         place before exploding
	 */
	public int getIgnitionDuration() {
		return Clock.getTicksPerSecond() * 3;
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
	 * Updates all {@ink Material#BOMB bomb} and {@ink Material#EXPLOSION explosion}
	 * timers.
	 */
	public void step() {
		for (var y = 0; y < height; y++) {
			for (var x = 0; x < width; x++) {
				if (explosionTimers[x][y] == 1) {
					materials[x][y] = Material.EMPTY;
				}
				if (explosionTimers[x][y] > 0) {
					explosionTimers[x][y]--;
				}
				if (bombTimers[x][y] == 1) {
					detonate(x, y);
				}
				if (bombTimers[x][y] > 0) {
					bombTimers[x][y]--;
				}
			}
		}
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
	 * Replaces a {@link Material#BOMB bomb} and the adjacent tiles with an
	 * {@link Material#EXPLOSION explosion}. If another {@link Material#BOMB bomb}
	 * is hit by the {@link Material#EXPLOSION explosion}, it explodes as well.
	 * 
	 * @param x the x coordinate of the detonation
	 * @param y the y coordinate of the detonation
	 */
	private void detonate(int x, int y) {
		bombTimers[x][y] = 0;
		materials[x][y] = Material.EXPLOSION;
		explosionTimers[x][y] = 20;

		detonateEast(x, y);
		detonateNorth(x, y);
		detonateSouth(x, y);
		detonateWest(x, y);
	}

	/**
	 * Replaces a {@link Material#BOMB bomb} and the adjacent tiles to the
	 * {@link Direction#EAST east} with an {@link Material#EXPLOSION explosion}. If
	 * another {@link Material#BOMB bomb} is hit by the {@link Material#EXPLOSION
	 * explosion}, it explodes as well.
	 * 
	 * @param x the x coordinate of the detonation
	 * @param y the y coordinate of the detonation
	 */
	private void detonateEast(int x, int y) {
		for (var i = 0; i <= 2; i++) {
			var material = getMaterial(x + i, y);
			boolean stop = false;
			if (material == Material.BRICK || material == Material.CONCRETE) {
				stop = true;
			}
			if (material == Material.BOMB) {
				detonate(x + i, y);
			}
			if (material != Material.CONCRETE) {
				materials[x + i][y] = Material.EXPLOSION;
				explosionTimers[x + i][y] = 20;
			}
			if (stop) {
				break;
			}
		}
	}

	/**
	 * Replaces a {@link Material#BOMB bomb} and the adjacent tiles to the
	 * {@link Direction#NORTH north} with an {@link Material#EXPLOSION explosion}.
	 * If another {@link Material#BOMB bomb} is hit by the {@link Material#EXPLOSION
	 * explosion}, it explodes as well.
	 * 
	 * @param x the x coordinate of the detonation
	 * @param y the y coordinate of the detonation
	 */
	private void detonateNorth(int x, int y) {
		for (var i = 0; i <= 2; i++) {
			var material = getMaterial(x, y - i);
			boolean stop = false;
			if (material == Material.BRICK || material == Material.CONCRETE) {
				stop = true;
			}
			if (material == Material.BOMB) {
				detonate(x, y - i);
			}
			if (material != Material.CONCRETE) {
				materials[x][y - i] = Material.EXPLOSION;
				explosionTimers[x][y - i] = 20;
			}
			if (stop) {
				break;
			}
		}
	}

	/**
	 * Replaces a {@link Material#BOMB bomb} and the adjacent tiles to the
	 * {@link Direction#SOUTH south} with an {@link Material#EXPLOSION explosion}.
	 * If another {@link Material#BOMB bomb} is hit by the {@link Material#EXPLOSION
	 * explosion}, it explodes as well.
	 * 
	 * @param x the x coordinate of the detonation
	 * @param y the y coordinate of the detonation
	 */
	private void detonateSouth(int x, int y) {
		for (var i = 0; i <= 2; i++) {
			var material = getMaterial(x, y + i);
			boolean stop = false;
			if (material == Material.BRICK || material == Material.CONCRETE) {
				stop = true;
			}
			if (material == Material.BOMB) {
				detonate(x, y + i);
			}
			if (material != Material.CONCRETE) {
				materials[x][y + i] = Material.EXPLOSION;
				explosionTimers[x][y + i] = 20;
			}
			if (stop) {
				break;
			}
		}
	}

	/**
	 * Replaces a {@link Material#BOMB bomb} and the adjacent tiles to the
	 * {@link Direction#WEST west} with an {@link Material#EXPLOSION explosion}. If
	 * another {@link Material#BOMB bomb} is hit by the {@link Material#EXPLOSION
	 * explosion}, it explodes as well.
	 * 
	 * @param x the x coordinate of the detonation
	 * @param y the y coordinate of the detonation
	 */
	private void detonateWest(int x, int y) {
		for (var i = 0; i <= 2; i++) {
			var material = getMaterial(x - i, y);
			boolean stop = false;
			if (material == Material.BRICK || material == Material.CONCRETE) {
				stop = true;
			}
			if (material == Material.BOMB) {
				detonate(x - i, y);
			}
			if (material != Material.CONCRETE) {
				materials[x - i][y] = Material.EXPLOSION;
				explosionTimers[x - i][y] = 20;
			}
			if (stop) {
				break;
			}
		}
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
