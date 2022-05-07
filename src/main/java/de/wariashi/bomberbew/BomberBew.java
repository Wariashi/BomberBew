package de.wariashi.bomberbew;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
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
	// model
	private transient Game game;
	private transient Map map;
	private transient Player[] players = new Player[4];
	private transient Controller[] controllers = new Controller[4];

	// ui
	private JButton start;
	private JButton stop;

	public static void main(String[] args) {
		new BomberBew();
	}

	public BomberBew() {
		map = new Map(13, 9, 0.25);

		// Tempii
		players[0] = new Player(map, 0, 0);
		controllers[0] = new Tempii();

		// Wariashi
		players[1] = new Player(map, map.getWidth() - 1, 0);
		controllers[1] = new Wariashi();

		// Keyboard
		players[2] = new Player(map, 0, map.getHeight() - 1);
		controllers[2] = new KeyboardController();

		game = new Game(map, players, controllers);
		Clock.setGame(game);
		Clock.setTicksPerSecond(200);

		setupUi();
	}

	private void addListeners() {
		addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent key) {
				for (var controller : controllers) {
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
					for (var controller : controllers) {
						if (controller instanceof KeyboardController keyboard) {
							keyboard.onKeyReleased(key.getKeyCode());
						}
					}
				}
			}
		});

		start.addActionListener(event -> {
			start.setEnabled(false);
			Clock.start();
			stop.setEnabled(true);
		});

		stop.addActionListener(event -> {
			stop.setEnabled(false);
			Clock.stop();
			start.setEnabled(true);
		});
	}

	private void setupUi() {
		setTitle("BomberBEW");
		setUndecorated(true);
		setLocation(0, 0);
		setSize(Toolkit.getDefaultToolkit().getScreenSize());

		setLayout(new BorderLayout());

		// toolbar
		var toolbar = new JPanel();
		toolbar.setBackground(Color.BLACK);
		add(toolbar, BorderLayout.NORTH);

		// start button
		start = new JButton("Start");
		start.setFocusable(false);
		toolbar.add(start);

		// stop button
		stop = new JButton("Stop");
		stop.setEnabled(false);
		stop.setFocusable(false);
		toolbar.add(stop);

		// viewport
		add(new Viewport(game), BorderLayout.CENTER);

		addListeners();
		setVisible(true);
	}
}
