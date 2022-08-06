package de.wariashi.bomberbew.model.projection;

import de.wariashi.bomberbew.controller.Controller;
import de.wariashi.bomberbew.model.Player;

/**
 * Contains immutable information about a {@link Player} which can be used to
 * forward the state of a {@link Player} to a {@link Controller} so that they
 * can not interact with it directly.
 */
public class PlayerData {
	private final boolean alive;
	private final int tileX;
	private final int tileY;
	private final int offsetX;
	private final int offsetY;

	/**
	 * Creates a new {@link PlayerData} object
	 * 
	 * @param player the player that will be represented by this {@link PlayerData}
	 * @throws IllegalArgumentException if <code>player</code> is <code>null</code>
	 */
	public PlayerData(Player player) {
		if (player == null) {
			throw new IllegalArgumentException();
		}
		alive = player.isAlive();
		tileX = player.getTileX();
		tileY = player.getTileY();
		offsetX = player.getOffsetX();
		offsetY = player.getOffsetY();
	}

	/**
	 * Returns the horizontal offset of the {@link Player} within the tile they are
	 * currently standing on. This value is always between -16 and +15 (including).
	 * 
	 * @return the horizontal offset of the {@link Player} within the tile they are
	 *         currently standing on
	 */
	public int getOffsetX() {
		return offsetX;
	}

	/**
	 * Returns the vertical offset of the {@link Player} within the tile they are
	 * currently standing on. This value is always between -16 and +15 (including).
	 * 
	 * @return the vertical offset of the {@link Player} within the tile they are
	 *         currently standing on
	 */
	public int getOffsetY() {
		return offsetY;
	}

	/**
	 * Returns the x coordinate of the tile that the {@link Player} is currently
	 * standing on.
	 * 
	 * @return the x coordinate of the tile that the {@link Player} is currently
	 *         standing on
	 */
	public int getTileX() {
		return tileX;
	}

	/**
	 * Returns the y coordinate of the tile that the {@link Player} is currently
	 * standing on.
	 * 
	 * @return the y coordinate of the tile that the {@link Player} is currently
	 *         standing on
	 */
	public int getTileY() {
		return tileY;
	}

	/**
	 * Returns whether the {@link Player} is still alive.
	 * 
	 * @return <code>true</code> if the {@link Player} is alive, <code>false</code>
	 *         otherwise
	 */
	public boolean isAlive() {
		return alive;
	}
}
