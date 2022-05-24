package de.wariashi.bomberbew.model.projection;

import de.wariashi.bomberbew.controller.Controller;
import de.wariashi.bomberbew.model.Bomb;
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
	private final BombData[][] bombs;
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
		bombs = new BombData[width][height];
		explosionTimers = new int[width][height];

		for (var y = 0; y < height; y++) {
			for (var x = 0; x < width; x++) {
				materials[x][y] = map.getMaterial(x, y);
				var bomb = map.getBomb(x, y);
				if (bomb != null) {
					bombs[x][y] = new BombData(map.getBomb(x, y));
				}
				explosionTimers[x][y] = map.getExplosionTimer(x, y);
			}
		}
	}

	/**
	 * Returns {@link BombData data} about the {@link Bomb} at the given location.
	 * If the given coordinates are outside of the map, this method will fail
	 * silently.
	 * 
	 * @param x the x coordinate
	 * @param y the y coordinate
	 * 
	 * @return the {@link BombData bomb} at the given coordinates or
	 *         <code>null</code> if there is no bomb
	 */
	public BombData getBomb(int x, int y) {
		if (x < 0 || width <= x || y < 0 || height <= y) {
			return null;
		}
		return bombs[x][y];
	}

	/**
	 * Returns the number of ticks a {@link Material#BOMB bomb} has remaining before
	 * exploding at the given location. A value of 0 indicates that there is no
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
