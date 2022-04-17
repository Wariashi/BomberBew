package de.wariashi.bomberbew;

import java.awt.event.KeyEvent;

import de.wariashi.bomberbew.model.Direction;

public class KeyboardController {
	private boolean downPressed = false;
	private boolean leftPressed = false;
	private boolean rightPressed = false;
	private boolean upPressed = false;

	public Direction getDirection() {
		var down = downPressed && !upPressed;
		var left = leftPressed && !rightPressed;
		var right = rightPressed && !leftPressed;
		var up = upPressed && !downPressed;

		if (up) {
			if (left) {
				return Direction.NORTH_WEST;
			} else if (right) {
				return Direction.NORTH_EAST;
			} else {
				return Direction.NORTH;
			}
		} else if (down) {
			if (left) {
				return Direction.SOUTH_WEST;
			} else if (right) {
				return Direction.SOUTH_EAST;
			} else {
				return Direction.SOUTH;
			}
		} else if (left) {
			return Direction.WEST;
		} else if (right) {
			return Direction.EAST;
		} else {
			return null;
		}
	}

	public void onKeyPressed(int keyCode) {
		switch (keyCode) {
		case KeyEvent.VK_DOWN:
			downPressed = true;
			break;
		case KeyEvent.VK_LEFT:
			leftPressed = true;
			break;
		case KeyEvent.VK_RIGHT:
			rightPressed = true;
			break;
		case KeyEvent.VK_UP:
			upPressed = true;
			break;
		default:
			break;
		}
	}

	public void onKeyReleased(int keyCode) {
		switch (keyCode) {
		case KeyEvent.VK_DOWN:
			downPressed = false;
			break;
		case KeyEvent.VK_LEFT:
			leftPressed = false;
			break;
		case KeyEvent.VK_RIGHT:
			rightPressed = false;
			break;
		case KeyEvent.VK_UP:
			upPressed = false;
			break;
		default:
			break;
		}
	}
}
