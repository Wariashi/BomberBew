package de.wariashi.bomberbew;

import java.awt.Color;
import java.awt.image.BufferedImage;

public class Map {
	private static final int TILE_SIZE = 32;
	private static final int TILES_X = 15;
	private static final int TILES_Y = 11;

	private static final int HEIGHT = TILES_Y * TILE_SIZE;
	private static final int WIDTH = TILES_X * TILE_SIZE;

	public BufferedImage getImage() {
		var image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		var graphics = image.createGraphics();

		graphics.setColor(Color.GRAY);
		for (var tileY = 0; tileY < TILES_Y; tileY++) {
			for (var tileX = 0; tileX < TILES_X; tileX++) {
				var border = tileX == 0 || tileY == 0 || tileX == TILES_X - 1 || tileY == TILES_Y - 1;
				var pillar = tileX % 2 == 0 && tileY % 2 == 0;
				if (border || pillar) {
					var x = tileX * TILE_SIZE + 1;
					var y = tileY * TILE_SIZE + 1;
					var width = TILE_SIZE - 2;
					var height = TILE_SIZE - 2;
					graphics.fillRect(x, y, width, height);
				}
			}
		}
		graphics.dispose();
		return image;
	}
}
