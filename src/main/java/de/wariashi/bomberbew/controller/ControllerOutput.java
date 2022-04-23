package de.wariashi.bomberbew.controller;

import de.wariashi.bomberbew.model.Direction;
import de.wariashi.bomberbew.model.Game;
import de.wariashi.bomberbew.model.Player;

/**
 * The {@link ControllerOutput} can be used by a {@link Controller} to tell the
 * {@link Game} what the {@link Player} should do next.
 */
public class ControllerOutput {
	private Direction direction = null;

	/**
	 * Returns the {@link Direction} where the {@link Player} should go next. This
	 * can also be <code>null</code> to indicate that the {@link Player} should not
	 * move at all.
	 * 
	 * @return the {@link Direction} where the {@link Player} should go next
	 */
	public Direction getDirection() {
		return direction;
	}

	/**
	 * Sets the {@link Direction} where the {@link Player} should go next. This can
	 * also be <code>null</code> to indicate that the {@link Player} should not move
	 * at all.
	 * 
	 * @param direction the {@link Direction} where the {@link Player} should go
	 *                  next
	 */
	public void setDirection(Direction direction) {
		this.direction = direction;
	}
}
