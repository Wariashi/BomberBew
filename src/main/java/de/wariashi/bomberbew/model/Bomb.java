package de.wariashi.bomberbew.model;

/**
 * Represents a bomb that can be placed by a {@link Player}.
 * 
 * @author Wariashi
 */
public class Bomb {
	private int timer;

	/**
	 * Creates a new bomb.
	 * 
	 * @param timer the number of remaining ticks
	 */
	public Bomb(int timer) {
		this.timer = timer;
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
