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

		new Thread() {
			@Override
			public void run() {
				while (true) {
					try {
						Player.this.tileX++;
						if (Player.this.tileX > 12) {
							Player.this.tileX = 0;
						}
						Thread.sleep(500);
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

	public int getTileX() {
		return tileX;
	}

	public int getTileY() {
		return tileY;
	}
}
