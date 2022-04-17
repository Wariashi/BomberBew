package de.wariashi.bomberbew.model;

import java.awt.Color;
import java.util.Random;

import de.wariashi.bomberbew.KeyboardController;

public class Player {
	private Map map;
	private int tileX;
	private int tileY;
	private KeyboardController controller;
	private Color color;

	public Player(Map map, int tileX, int tileY, KeyboardController controller) {
		this.map = map;
		this.tileX = tileX;
		this.tileY = tileY;
		this.controller = controller;

		var random = new Random();
		color = new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256));

		new Thread() {
			@Override
			public void run() {
				while (true) {
					try {
						move();
						Thread.sleep(100);
					} catch (InterruptedException exception) {
						Thread.currentThread().interrupt();
					}
				}
			}
		}.start();
	}

	public Color getColor() {
		return color;
	}

	public KeyboardController getController() {
		return controller;
	}

	public int getTileX() {
		return tileX;
	}

	public int getTileY() {
		return tileY;
	}

	private void move() {
		var direction = controller.getDirection();
		if (direction == null) {
			return;
		}
		switch (direction) {
		case NORTH:
			if (map.getMaterial(tileX, tileY - 1) == Material.EMPTY) {
				tileY--;
			}
			break;
		case NORTH_EAST:
			if (map.getMaterial(tileX, tileY - 1) == Material.EMPTY) {
				tileY--;
			} else if (map.getMaterial(tileX + 1, tileY) == Material.EMPTY) {
				tileX++;
			}
			break;
		case EAST:
			if (map.getMaterial(tileX + 1, tileY) == Material.EMPTY) {
				tileX++;
			}
			break;
		case SOUTH_EAST:
			if (map.getMaterial(tileX + 1, tileY) == Material.EMPTY) {
				tileX++;
			} else if (map.getMaterial(tileX, tileY + 1) == Material.EMPTY) {
				tileY++;
			}
			break;
		case SOUTH:
			if (map.getMaterial(tileX, tileY + 1) == Material.EMPTY) {
				tileY++;
			}
			break;
		case SOUTH_WEST:
			if (map.getMaterial(tileX, tileY + 1) == Material.EMPTY) {
				tileY++;
			} else if (map.getMaterial(tileX - 1, tileY) == Material.EMPTY) {
				tileX--;
			}
			break;
		case WEST:
			if (map.getMaterial(tileX - 1, tileY) == Material.EMPTY) {
				tileX--;
			}
			break;
		case NORTH_WEST:
			if (map.getMaterial(tileX - 1, tileY) == Material.EMPTY) {
				tileX--;
			} else if (map.getMaterial(tileX, tileY - 1) == Material.EMPTY) {
				tileY--;
			}
			break;
		default:
			break;
		}
	}
}
