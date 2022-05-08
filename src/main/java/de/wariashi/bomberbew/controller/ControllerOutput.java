package de.wariashi.bomberbew.controller;

import de.wariashi.bomberbew.model.Direction;
import de.wariashi.bomberbew.model.Game;
import de.wariashi.bomberbew.model.Material;
import de.wariashi.bomberbew.model.Player;

/**
 * The {@link ControllerOutput} can be used by a {@link Controller} to tell the
 * {@link Game} what the {@link Player} should do next.
 */
public class ControllerOutput {
	private Direction direction = null;
	private boolean dropBomb = false;

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
	 * Returns whether the {@link Controller} wants the {@link Player} to drop a
	 * {@link Material#BOMB bomb} at their current location.
	 * 
	 * @param dropBomb <code>true</code> if the {@link Player} is supposed to drop a
	 *                 {@link Material#BOMB bomb}, <code>false</code> otherwise
	 */
	public boolean getDropBomb() {
		return dropBomb;
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

	/**
	 * Sets whether the {@link Controller} wants the {@link Player} to drop a
	 * {@link Material#BOMB bomb} at their current location.
	 * 
	 * @param dropBomb <code>true</code> if the {@link Player} is supposed to drop a
	 *                 {@link Material#BOMB bomb}, <code>false</code> otherwise
	 */
	public void setDropBomb(boolean dropBomb) {
		this.dropBomb = dropBomb;
	}
}
