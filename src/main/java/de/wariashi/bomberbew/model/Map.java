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
	private Bomb[][] bombs;
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

		bombs = new Bomb[width][height];
		explosionTimers = new int[width][height];

		// initialize map
		initializeMaterials();
		addBricks(brickDensity);
		addPillars();
		clearCorners();
	}

	/**
	 * Drops a {@link Bomb} at the given location. If the given coordinates are
	 * outside of the map, this method will fail silently.
	 * 
	 * @param x the x coordinate
	 * @param y the y coordinate
	 */
	public void dropBomb(int x, int y) {
		if (x < 0 || width <= x || y < 0 || height <= y) {
			return;
		}
		materials[x][y] = Material.BOMB;
		bombs[x][y] = new Bomb(2, getIgnitionDuration());
	}

	/**
	 * Returns the {@link Bomb} at the given location. If the given coordinates are
	 * outside of the map, this method will fail silently.
	 * 
	 * @param x the x coordinate
	 * @param y the y coordinate
	 * 
	 * @return the {@link Bomb} at the given coordinates or <code>null</code> if
	 *         there is no bomb
	 */
	public Bomb getBomb(int x, int y) {
		if (x < 0 || width <= x || y < 0 || height <= y) {
			return null;
		}
		return bombs[x][y];
	}

	/**
	 * Returns the number of ticks a {@link Bomb} has remaining before exploding at
	 * the given location. A value of 0 indicates that there is no
	 * {@link Material#BOMB bomb} at all. If the given coordinates are outside of
	 * the map, this method will fail silently.
	 * 
	 * @deprecated use {@link #getBomb(int, int)}
	 * @param x the x coordinate
	 * @param y the y coordinate
	 */
	@Deprecated(forRemoval = true)
	public int getBombTimer(int x, int y) {
		var bomb = getBomb(x, y);
		if (bomb == null) {
			return 0;
		}
		return bomb.getTimer();
	}

	/**
	 * Returns the number of ticks an {@link Material#EXPLOSION explosion} has
	 * remaining before it is removed from the {@link Map map}. A value of 0
	 * indicates that there is no {@link Material#EXPLOSION explosion} at all. If
	 * the given coordinates are outside of the map, this method will fail silently.
	 * 
	 * @param x the x coordinate
	 * @param y the y coordinate
	 */
	public int getExplosionTimer(int x, int y) {
		if (x < 0 || width <= x || y < 0 || height <= y) {
			return 0;
		}
		return explosionTimers[x][y];
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
				var bomb = getBomb(x, y);
				if (bomb != null) {
					var range = bomb.getRange();
					var timer = bomb.getTimer();
					if (timer == 1) {
						detonate(x, y, range);
					}
					if (timer > 0) {
						bomb.step();
					}
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
	 * Replaces a {@link Bomb} and the adjacent tiles with an
	 * {@link Material#EXPLOSION explosion}. If another {@link Bomb} is hit by the
	 * {@link Material#EXPLOSION explosion}, it explodes as well.
	 * 
	 * @param x     the x coordinate of the explosion
	 * @param y     the y coordinate of the explosion
	 * @param range the range of the explosion
	 */
	private void detonate(int x, int y, int range) {
		bombs[x][y] = null;
		materials[x][y] = Material.EXPLOSION;
		explosionTimers[x][y] = 20;

		detonateEast(x, y, range);
		detonateNorth(x, y, range);
		detonateSouth(x, y, range);
		detonateWest(x, y, range);
	}

	/**
	 * Replaces a {@link Material#BOMB bomb} and the adjacent tiles to the
	 * {@link Direction#EAST east} with an {@link Material#EXPLOSION explosion}. If
	 * another {@link Material#BOMB bomb} is hit by the {@link Material#EXPLOSION
	 * explosion}, it explodes as well.
	 * 
	 * @param x     the x coordinate of the explosion
	 * @param y     the y coordinate of the explosion
	 * @param range the range of the explosion
	 */
	private void detonateEast(int x, int y, int range) {
		for (var i = 0; i <= range; i++) {
			var material = getMaterial(x + i, y);
			boolean stop = false;
			if (material == Material.BRICK || material == Material.CONCRETE) {
				stop = true;
			}
			if (material == Material.BOMB) {
				detonate(x + i, y, range);
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
	 * @param x     the x coordinate of the explosion
	 * @param y     the y coordinate of the explosion
	 * @param range the range of the explosion
	 */
	private void detonateNorth(int x, int y, int range) {
		for (var i = 0; i <= range; i++) {
			var material = getMaterial(x, y - i);
			boolean stop = false;
			if (material == Material.BRICK || material == Material.CONCRETE) {
				stop = true;
			}
			if (material == Material.BOMB) {
				detonate(x, y - i, range);
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
	 * @param x     the x coordinate of the explosion
	 * @param y     the y coordinate of the explosion
	 * @param range the range of the explosion
	 */
	private void detonateSouth(int x, int y, int range) {
		for (var i = 0; i <= range; i++) {
			var material = getMaterial(x, y + i);
			boolean stop = false;
			if (material == Material.BRICK || material == Material.CONCRETE) {
				stop = true;
			}
			if (material == Material.BOMB) {
				detonate(x, y + i, range);
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
	 * @param x     the x coordinate of the explosion
	 * @param y     the y coordinate of the explosion
	 * @param range the range of the explosion
	 */
	private void detonateWest(int x, int y, int range) {
		for (var i = 0; i <= range; i++) {
			var material = getMaterial(x - i, y);
			boolean stop = false;
			if (material == Material.BRICK || material == Material.CONCRETE) {
				stop = true;
			}
			if (material == Material.BOMB) {
				detonate(x - i, y, range);
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
