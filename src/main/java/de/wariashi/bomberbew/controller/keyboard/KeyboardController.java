package de.wariashi.bomberbew.controller.keyboard;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

import de.wariashi.bomberbew.Viewport;
import de.wariashi.bomberbew.controller.Controller;
import de.wariashi.bomberbew.controller.ControllerInput;
import de.wariashi.bomberbew.controller.ControllerOutput;
import de.wariashi.bomberbew.model.Direction;

public class KeyboardController implements Controller {
	// movement
	private boolean downPressed = false;
	private boolean leftPressed = false;
	private boolean rightPressed = false;
	private boolean upPressed = false;

	// bombs
	private boolean spacePressed = false;

	// ui
	private BufferedImage image;

	public KeyboardController() {
		var tileSize = Viewport.TILE_SIZE;
		image = new BufferedImage(tileSize, tileSize, BufferedImage.TYPE_INT_ARGB);
		var graphics = image.getGraphics();
		graphics.setColor(new Color(200, 200, 0));
		graphics.fillOval(0, 0, tileSize, tileSize);
	}

	@Override
	public String getName() {
		return "Keyboard";
	}

	@Override
	public BufferedImage getPlayerImage() {
		return image;
	}

	@Override
	public ControllerOutput update(ControllerInput input) {
		var output = new ControllerOutput();
		output.setDirection(getDirection());
		output.setDropBomb(spacePressed);
		return output;
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
		case KeyEvent.VK_SPACE:
			spacePressed = true;
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
		case KeyEvent.VK_SPACE:
			spacePressed = false;
			break;
		case KeyEvent.VK_UP:
			upPressed = false;
			break;
		default:
			break;
		}
	}

	private Direction getDirection() {
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
}
