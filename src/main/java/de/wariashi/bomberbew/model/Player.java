package de.wariashi.bomberbew.model;

import java.awt.image.BufferedImage;

import de.wariashi.bomberbew.controller.ControllerOutput;

public class Player {
	private Map map;

	private final Object bombLock = new Object();
	private int bombsLeft = 3;

	private boolean alive = true;
	private int tileX;
	private int tileY;
	private int offsetX;
	private int offsetY;
	private Direction velocity;

	private BufferedImage image;

	public Player(Map map, int tileX, int tileY) {
		this.map = map;
		this.tileX = tileX;
		this.tileY = tileY;
	}

	public void addBomb() {
		synchronized (bombLock) {
			bombsLeft++;
		}
	}

	public BufferedImage getImage() {
		return image;
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

	public boolean isAlive() {
		return alive;
	}

	public void setImage(BufferedImage image) {
		this.image = image;
	}

	public void step(ControllerOutput output) {
		if (map.getMaterial(tileX, tileY) == Material.EXPLOSION) {
			alive = false;
		}
		if (!alive) {
			return;
		}

		var direction = output.getDirection();
		if (getOffsetX() != 0 || getOffsetY() != 0) {
			direction = velocity;
		}
		move(direction);

		var dropBomb = output.getDropBomb();
		if (dropBomb) {
			dropBomb();
		}
	}

	private void dropBomb() {
		var tileIsEmpty = map.getMaterial(tileX, tileY) == Material.EMPTY;
		if (tileIsEmpty && bombsLeft > 0) {
			map.dropBomb(this, tileX, tileY);
			synchronized (bombLock) {
				bombsLeft--;
			}
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
		if (getOffsetY() != 0 || map.getMaterial(tileX, tileY + 1) == Material.EMPTY) {
			offsetY++;
			recalculatePosition();
			velocity = Direction.SOUTH;
			return true;
		} else {
			return false;
		}
	}

	private boolean moveLeft() {
		if (getOffsetX() != 0 || map.getMaterial(tileX - 1, tileY) == Material.EMPTY) {
			offsetX--;
			recalculatePosition();
			velocity = Direction.WEST;
			return true;
		} else {
			return false;
		}
	}

	private boolean moveRight() {
		if (getOffsetX() != 0 || map.getMaterial(tileX + 1, tileY) == Material.EMPTY) {
			offsetX++;
			recalculatePosition();
			velocity = Direction.EAST;
			return true;
		} else {
			return false;
		}
	}

	private boolean moveUp() {
		if (getOffsetY() != 0 || map.getMaterial(tileX, tileY - 1) == Material.EMPTY) {
			offsetY--;
			recalculatePosition();
			velocity = Direction.NORTH;
			return true;
		} else {
			return false;
		}
	}

	private void recalculatePosition() {
		int halfTile = Game.STEPS_PER_TILE / 2;
		if (offsetX < -halfTile) {
			tileX--;
			offsetX = halfTile - 1;
		} else if (offsetX > halfTile - 1) {
			tileX++;
			offsetX = -halfTile;
		} else if (offsetY < -halfTile) {
			tileY--;
			offsetY = halfTile - 1;
		} else if (offsetY > halfTile - 1) {
			tileY++;
			offsetY = -halfTile;
		}
	}
}
