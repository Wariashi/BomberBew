package de.wariashi.bomberbew;

import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;

import de.wariashi.bomberbew.controller.Controller;
import de.wariashi.bomberbew.controller.keyboard.KeyboardController;
import de.wariashi.bomberbew.controller.tempii.Tempii;
import de.wariashi.bomberbew.controller.wariashi.Wariashi;
import de.wariashi.bomberbew.model.Game;
import de.wariashi.bomberbew.model.Map;
import de.wariashi.bomberbew.model.Player;

@SuppressWarnings("serial")
public class BomberBew extends JFrame {
	private JPanel viewport;
	private transient Map map;
	private transient List<Player> players;
	private transient List<Controller> controllers;

	public static void main(String[] args) {
		new BomberBew();
	}

	public BomberBew() {
		addKeyListener();

		setTitle("BomberBEW");
		setUndecorated(true);
		setLocation(0, 0);
		setSize(Toolkit.getDefaultToolkit().getScreenSize());

		map = new Map(13, 9, 0.25);

		players = new ArrayList<>();
		controllers = new ArrayList<>();

		// Tempii
		players.add(new Player(map, 0, 0));
		controllers.add(new Tempii());

		// Wariashi
		players.add(new Player(map, map.getWidth() - 1, 0));
		controllers.add(new Wariashi());

		// Keyboard
		players.add(new Player(map, 0, map.getHeight() - 1));
		controllers.add(new KeyboardController());

		var game = new Game(map, players, controllers);

		viewport = new Viewport(game);
		add(viewport);

		Clock.setGame(game);
		Clock.setTicksPerSecond(200);
		Clock.start();

		setVisible(true);
	}

	private void addKeyListener() {
		addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent key) {
				var iterator = controllers.iterator();
				while (iterator.hasNext()) {
					var controller = iterator.next();
					if (controller instanceof KeyboardController keyboard) {
						keyboard.onKeyPressed(key.getKeyCode());
					}
				}
			}

			@Override
			public void keyReleased(KeyEvent key) {
				if (key.getKeyCode() == KeyEvent.VK_ESCAPE) {
					System.exit(0);
				} else {
					var iterator = controllers.iterator();
					while (iterator.hasNext()) {
						var controller = iterator.next();
						if (controller instanceof KeyboardController keyboard) {
							keyboard.onKeyReleased(key.getKeyCode());
						}
					}
				}
			}
		});
	}
}
