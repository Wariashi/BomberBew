package de.wariashi.bomberbew.model;

import java.awt.Color;
import java.util.Random;

import de.wariashi.bomberbew.controller.ControllerOutput;

public class Player {
	private Map map;

	private int tileX;
	private int tileY;
	private int offsetX;
	private int offsetY;
	private Direction velocity;

	private Color color;

	public Player(Map map, int tileX, int tileY) {
		this.map = map;
		this.tileX = tileX;
		this.tileY = tileY;

		var random = new Random();
		color = new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256));
	}

	public Color getColor() {
		return color;
	}

	public int getOffsetX() {
		return offsetX;
	}

	public int getOffsetY() {
		return offsetY;
	}

	public int getTileX() {
		return tileX;
	}

	public int getTileY() {
		return tileY;
	}

	public void step(ControllerOutput output) {
		var direction = output.getDirection();
		if (getOffsetX() != 0 || getOffsetY() != 0) {
			direction = velocity;
		}
		move(direction);

		var dropBomb = output.getDropBomb();
		if (dropBomb) {
			map.dropBomb(tileX, tileY);
		}
	}

	private void move(Direction direction) {
		if (direction == null) {
			return;
		}
		switch (direction) {
		case NORTH:
			moveUp();
			break;
		case NORTH_EAST:
			if (!moveUp()) {
				moveRight();
			}
			break;
		case EAST:
			moveRight();
			break;
		case SOUTH_EAST:
			if (!moveRight()) {
				moveDown();
			}
			break;
		case SOUTH:
			moveDown();
			break;
		case SOUTH_WEST:
			if (!moveDown()) {
				moveLeft();
			}
			break;
		case WEST:
			moveLeft();
			break;
		case NORTH_WEST:
			if (!moveLeft()) {
				moveUp();
			}
			break;
		default:
			break;
		}
	}

	private boolean moveDown() {
		if (map.getMaterial(tileX, tileY + 1) == Material.EMPTY || getOffsetY() < 0) {
			offsetY++;
			recalculatePosition();
			velocity = Direction.SOUTH;
			return true;
		} else {
			return false;
		}
	}

	private boolean moveLeft() {
		if (map.getMaterial(tileX - 1, tileY) == Material.EMPTY || getOffsetX() > 0) {
			offsetX--;
			recalculatePosition();
			velocity = Direction.WEST;
			return true;
		} else {
			return false;
		}
	}

	private boolean moveRight() {
		if (map.getMaterial(tileX + 1, tileY) == Material.EMPTY || getOffsetX() < 0) {
			offsetX++;
			recalculatePosition();
			velocity = Direction.EAST;
			return true;
		} else {
			return false;
		}
	}

	private boolean moveUp() {
		if (map.getMaterial(tileX, tileY - 1) == Material.EMPTY || getOffsetY() > 0) {
			offsetY--;
			recalculatePosition();
			velocity = Direction.NORTH;
			return true;
		} else {
			return false;
		}
	}

	private void recalculatePosition() {
		if (offsetX < -16) {
			tileX--;
			offsetX = 15;
		} else if (offsetX > 15) {
			tileX++;
			offsetX = -16;
		} else if (offsetY < -16) {
			tileY--;
			offsetY = 15;
		} else if (offsetY > 15) {
			tileY++;
			offsetY = -16;
		}
	}
}
