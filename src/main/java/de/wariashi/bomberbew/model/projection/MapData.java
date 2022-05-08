package de.wariashi.bomberbew.model.projection;

import de.wariashi.bomberbew.controller.Controller;
import de.wariashi.bomberbew.model.Map;
import de.wariashi.bomberbew.model.Material;

/**
 * {@link MapData} is an immutable copy of a {@link Map} which can be used to
 * forward the state of the map to a {@link Controller} so that they can not
 * interact with it directly.
 */
public class MapData {
	private final int width;
	private final int height;
	private final Material[][] materials;
	private final int[][] bombTimers;
	private final int[][] explosionTimers;

	/**
	 * Creates a new {@link MapData} object
	 * 
	 * @param map the map that will be represented by this {@link MapData}
	 * @throws IllegalArgumentException if <code>map</code> is <code>null</code>
	 */
	public MapData(Map map) {
		if (map == null) {
			throw new IllegalArgumentException();
		}
		width = map.getWidth();
		height = map.getHeight();
		materials = new Material[width][height];
		bombTimers = new int[width][height];
		explosionTimers = new int[width][height];

		for (var y = 0; y < height; y++) {
			for (var x = 0; x < width; x++) {
				materials[x][y] = map.getMaterial(x, y);
				bombTimers[x][y] = map.getBombTimer(x, y);
				explosionTimers[x][y] = map.getExplosionTimer(x, y);
			}
		}
	}

	/**
	 * Returns the number of ticks a {@link Material#BOMB bomb} has remaining before
	 * exploding at the given location. A value of 0 indicates that there is no
	 * {@link Material#BOMB bomb} at all. If the given coordinates are outside of
	 * the map, this method will fail silently.
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
}
