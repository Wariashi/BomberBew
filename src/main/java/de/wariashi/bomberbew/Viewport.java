package de.wariashi.bomberbew;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import de.wariashi.bomberbew.model.Game;

@SuppressWarnings("serial")
public class Viewport extends JPanel {
	private static final int TILE_SIZE = 32;

	private transient Game game;

	public Viewport(Game game) {
		this.game = game;
		startGraphicsThread();
	}

	@Override
	public void paint(Graphics graphics) {
		var mapImage = createMap();
		addPlayers(mapImage);

		var mapWidth = mapImage.getWidth();
		var mapHeight = mapImage.getHeight();
		var mapRatio = (float) mapWidth / (float) mapHeight;

		var panelWidth = getWidth();
		var panelHeight = getHeight();
		var panelRatio = (float) panelWidth / (float) panelHeight;

		// background
		graphics.setColor(Color.BLACK);
		graphics.fillRect(0, 0, panelWidth, panelHeight);

		// map
		if (mapRatio < panelRatio) {
			// pillarbox
			var scaling = (float) panelHeight / (float) mapHeight;
			var height = (int) (scaling * mapHeight);
			var width = (int) (scaling * mapWidth);
			var offset = (panelWidth - width) / 2;
			graphics.drawImage(mapImage, offset, 0, offset + width, height, 0, 0, mapWidth, mapHeight, null);
		} else {
			// letterbox
			var scaling = (float) panelWidth / (float) mapWidth;
			var height = (int) (scaling * mapHeight);
			var width = (int) (scaling * mapWidth);
			var offset = (panelHeight - height) / 2;
			graphics.drawImage(mapImage, 0, offset, width, offset + height, 0, 0, mapWidth, mapHeight, null);
		}
	}

	private void addPlayers(BufferedImage map) {
		var graphics = map.createGraphics();

		var mapOffsetX = (int) (1.5 * TILE_SIZE);
		var mapOffsetY = (int) (1.5 * TILE_SIZE);

		var imageOffsetX = -(TILE_SIZE / 2);
		var imageOffsetY = -(TILE_SIZE / 2);

		var players = game.getPlayers();
		for (var player : players) {
			if (player == null) {
				continue;
			}

			graphics.setColor(player.getColor());

			var playerX = mapOffsetX + player.getTileX() * TILE_SIZE + player.getOffsetX();
			var playerY = mapOffsetY + player.getTileY() * TILE_SIZE + player.getOffsetY();

			graphics.fillOval(imageOffsetX + playerX + 2, imageOffsetY + playerY + 2, TILE_SIZE - 4, TILE_SIZE - 4);
		}
		graphics.dispose();
	}

	private BufferedImage createMap() {
		var map = game.getMap();
		var imageWidth = (map.getWidth() + 2) * TILE_SIZE;
		var imageHeight = (map.getHeight() + 2) * TILE_SIZE;

		var image = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_RGB);
		var graphics = image.createGraphics();

		for (var tileY = -1; tileY <= map.getHeight(); tileY++) {
			for (var tileX = -1; tileX <= map.getWidth(); tileX++) {
				switch (map.getMaterial(tileX, tileY)) {
				case BOMB:
					graphics.setColor(Color.BLACK);
					break;
				case BRICK:
					graphics.setColor(Color.ORANGE);
					break;
				case CONCRETE:
					graphics.setColor(Color.GRAY);
					break;
				case EMPTY:
					graphics.setColor(Color.BLACK);
					break;
				case EXPLOSION:
					graphics.setColor(Color.RED);
					break;
				}

				var x = TILE_SIZE + tileX * TILE_SIZE + 1;
				var y = TILE_SIZE + tileY * TILE_SIZE + 1;
				var width = TILE_SIZE - 2;
				var height = TILE_SIZE - 2;
				graphics.fillRect(x, y, width, height);

				var bomb = map.getBomb(tileX, tileY);
				if (bomb != null) {
					var bombTimer = bomb.getTimer();
					var percentage = 1 - (bombTimer / (double) map.getIgnitionDuration());
					var color = new Color(128 + (int) (127 * percentage), 128 - (int) (127 * percentage),
							128 - (int) (127 * percentage));
					graphics.setColor(color);
					graphics.fillOval(x, y, width, height);
				}
			}
		}
		graphics.dispose();
		return image;
	}

	/**
	 * Starts a new thread that renders the current view in an endless loop.
	 */
	private void startGraphicsThread() {
		var graphicsThread = new Thread() {
			@Override
			public void run() {
				while (true) {
					try {
						repaint();
						Thread.sleep(1);
					} catch (InterruptedException exception) {
						Thread.currentThread().interrupt();
					}
				}
			}
		};
		graphicsThread.setName("Graphics");
		graphicsThread.start();
	}
}
