package de.wariashi.bomberbew;

import java.awt.Graphics;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class Viewport extends JPanel {
	private Map map;

	public Viewport(Map map) {
		this.map = map;
	}

	@Override
	public void paint(Graphics graphics) {
		var mapImage = map.getImage();

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
}
