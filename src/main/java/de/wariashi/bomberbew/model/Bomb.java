package de.wariashi.bomberbew.model;

/**
 * Represents a bomb that can be placed by a {@link Player}.
 * 
 * @author Wariashi
 */
public class Bomb {
	private Player owner;
	private int range;
	private int timer;

	/**
	 * Creates a new bomb.
	 * 
	 * @param owner the {@link Player} that has playced the bomb
	 * @param range the range of the explosion
	 * @param timer the number of remaining ticks
	 */
	public Bomb(Player owner, int range, int timer) {
		this.owner = owner;
		this.range = range;
		this.timer = timer;
	}

	/**
	 * Returns the {@link Player} that has playced the bomb.
	 * 
	 * @return the {@link Player} that has playced the bomb
	 */
	public Player getOwner() {
		return owner;
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

	/**
	 * Updates the timer.
	 */
	public void step() {
		if (timer > 0) {
			timer--;
		}
	}
}
