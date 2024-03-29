package de.wariashi.bomberbew;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import de.wariashi.bomberbew.model.Game;

@SuppressWarnings("serial")
public class Viewport extends JPanel {
	public static final int TILE_SIZE = 128;

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

	public void setGame(Game game) {
		this.game = game;
	}

	private void addPlayers(BufferedImage map) {
		var offsetScaling = TILE_SIZE / Game.STEPS_PER_TILE;

		var graphics = map.createGraphics();

		var mapOffsetX = (int) (1.5 * TILE_SIZE);
		var mapOffsetY = (int) (1.5 * TILE_SIZE);

		var imageOffsetX = -(TILE_SIZE / 2);
		var imageOffsetY = -(TILE_SIZE / 2);

		var players = game.getPlayers();
		for (var player : players) {
			if (player == null || !player.isAlive()) {
				continue;
			}

			var playerX = mapOffsetX + player.getTileX() * TILE_SIZE + offsetScaling * player.getOffsetX()
					+ imageOffsetX;
			var playerY = mapOffsetY + player.getTileY() * TILE_SIZE + offsetScaling * player.getOffsetY()
					+ imageOffsetY;

			graphics.drawImage(player.getImage(), playerX, playerY, TILE_SIZE, TILE_SIZE, null);
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

				var x = TILE_SIZE + tileX * TILE_SIZE;
				var y = TILE_SIZE + tileY * TILE_SIZE;

				switch (map.getMaterial(tileX, tileY)) {
				case BRICK:
					graphics.drawImage(Textures.getBrick(), x, y, TILE_SIZE, TILE_SIZE, null);
					break;
				case CONCRETE:
					graphics.drawImage(Textures.getConcrete(), x, y, TILE_SIZE, TILE_SIZE, null);
					break;
				case EXPLOSION:
					graphics.drawImage(Textures.getExplosion(), x, y, TILE_SIZE, TILE_SIZE, null);
					break;
				default:
					break;
				}

				var bomb = map.getBomb(tileX, tileY);
				if (bomb != null) {
					var bombTimer = bomb.getTimer();
					var percentage = 1 - (bombTimer / (double) map.getIgnitionDuration());
					graphics.drawImage(Textures.getBomb(percentage), x, y, TILE_SIZE, TILE_SIZE, null);
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
