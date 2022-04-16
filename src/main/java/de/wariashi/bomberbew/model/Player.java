package de.wariashi.bomberbew.model;

import java.awt.Color;
import java.util.Random;

public class Player {
	private Color color;
	private int tileX;
	private int tileY;

	public Player(int tileX, int tileY) {
		var random = new Random();
		color = new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256));
		this.tileX = tileX;
		this.tileY = tileY;
	}

	public Color getColor() {
		return color;
	}

	public int getTileX() {
		return tileX;
	}

	public int getTileY() {
		return tileY;
	}
}
