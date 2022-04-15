package de.wariashi.bomberbew;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

@SuppressWarnings("serial")
public class BomberBew extends JFrame {
	public static void main(String[] args) {
		new BomberBew();
	}

	public BomberBew() {
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent event) {
				System.exit(0);
			}
		});

		setSize(800, 600);
		setTitle("BomberBEW");

		// set position to the center of the screen
		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		var positionX = (dimension.width - getSize().width) / 2;
		var positionY = (dimension.height - getSize().height) / 2;
		setLocation(positionX, positionY);

		setVisible(true);
	}
}
