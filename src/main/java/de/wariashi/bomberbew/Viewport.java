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

		var players = game.getPlayers();
		var iterator = players.iterator();
		while (iterator.hasNext()) {
			var player = iterator.next();
			graphics.setColor(player.getColor());
			graphics.fillOval(TILE_SIZE + player.getTileX() * TILE_SIZE, TILE_SIZE + player.getTileY() * TILE_SIZE,
					TILE_SIZE, TILE_SIZE);
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
				case BRICK:
					graphics.setColor(Color.ORANGE);
					break;
				case CONCRETE:
					graphics.setColor(Color.GRAY);
					break;
				case EMPTY:
					graphics.setColor(Color.BLACK);
					break;
				}

				var x = TILE_SIZE + tileX * TILE_SIZE + 1;
				var y = TILE_SIZE + tileY * TILE_SIZE + 1;
				var width = TILE_SIZE - 2;
				var height = TILE_SIZE - 2;
				graphics.fillRect(x, y, width, height);
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
