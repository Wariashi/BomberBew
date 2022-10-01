package de.wariashi.bomberbew.model.projection;

import de.wariashi.bomberbew.model.Bomb;
import de.wariashi.bomberbew.model.Player;

/**
 * Represents a {@link Bomb} that can be placed by a {@link Player}.
 * 
 * @author Wariashi
 */
public class BombData {
	private final int range;
	private final int timer;

	/**
	 * Creates a new bomb.
	 * 
	 * @param bomb the {@link Bomb} that this object represents
	 */
	public BombData(Bomb bomb) {
		range = bomb.getRange();
		timer = bomb.getTimer();
	}

	/**
	 * Returns the range of the explosion. A range of 0 indicates that only the tile
	 * containing the bomb will explode.
	 * 
	 * @return the range of the explosion
	 */
	public int getRange() {
		return range;
	}

	/**
	 * Returns the number remaining of ticks.
	 * 
	 * @return the number remaining of ticks
	 */
	public int getTimer() {
		return timer;
	}
}
