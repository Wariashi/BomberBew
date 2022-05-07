package de.wariashi.bomberbew;

import de.wariashi.bomberbew.model.Game;

/**
 * The Clock periodically calls the "step" function of a {@link Game}.
 */
public class Clock {
	private static boolean paused = true;
	private static Game game;

	private static long delay;
	private static long lastStep = System.nanoTime();
	private static long currentTime = System.nanoTime();

	/**
	 * Static constructor, which creates the clock.
	 */
	static {
		Thread thread = new Thread() {
			@Override
			public void run() {
				while (true) {
					try {
						currentTime = System.nanoTime();
						if (currentTime - lastStep > delay) {
							if (!paused && game != null) {
								game.step();
							}
							lastStep = lastStep + delay;
						}

						Thread.sleep(1);
					} catch (InterruptedException exception) {
						Thread.currentThread().interrupt();
					}
				}
			}
		};
		thread.setName("Clock");
		thread.start();
	}

	private Clock() throws IllegalAccessException {
		throw new IllegalAccessException();
	}

	/**
	 * Sets the game whose step function should be called periodically.
	 * 
	 * @param game the game whose step function should be called periodically
	 */
	public static void setGame(Game game) {
		Clock.game = game;
	}

	/**
	 * Sets the tick speed.
	 * 
	 * @param ticks the number of ticks per second
	 */
	public static void setTicksPerSecond(int ticks) {
		if (ticks == 0) {
			delay = Long.MAX_VALUE;
			return;
		}
		double ticksPerMillisecond = ticks / 1000.0;
		double ticksPerMicrosecond = ticksPerMillisecond / 1000.0;
		double ticksPerNanosecond = ticksPerMicrosecond / 1000.0;
		delay = (long) (1 / ticksPerNanosecond);
	}

	/**
	 * Pauses the clock.
	 */
	public static void stop() {
		paused = true;
	}

	/**
	 * Starts the clock.
	 */
	public static void start() {
		paused = false;
	}
}
