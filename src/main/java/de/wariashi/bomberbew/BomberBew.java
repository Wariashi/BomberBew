package de.wariashi.bomberbew;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;

import de.wariashi.bomberbew.controller.Controller;
import de.wariashi.bomberbew.controller.ControllerEnum;
import de.wariashi.bomberbew.controller.keyboard.KeyboardController;
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
	private JComboBox<ControllerEnum> controllerSelectorTopLeft;
	private JComboBox<ControllerEnum> controllerSelectorTopRight;
	private JComboBox<ControllerEnum> controllerSelectorBottomLeft;
	private JComboBox<ControllerEnum> controllerSelectorBottomRight;
	private JButton start;
	private JButton stop;

	public static void main(String[] args) {
		new BomberBew();
	}

	public BomberBew() {
		map = new Map(13, 9, 0.75);
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
			controllerSelectorTopLeft.setEnabled(false);
			controllerSelectorTopRight.setEnabled(false);
			controllerSelectorBottomLeft.setEnabled(false);
			controllerSelectorBottomRight.setEnabled(false);
			Clock.start();
			stop.setEnabled(true);
		});

		stop.addActionListener(event -> {
			stop.setEnabled(false);
			Clock.stop();
			controllerSelectorTopLeft.setEnabled(true);
			controllerSelectorTopRight.setEnabled(true);
			controllerSelectorBottomLeft.setEnabled(true);
			controllerSelectorBottomRight.setEnabled(true);
			start.setEnabled(true);
		});

		controllerSelectorTopLeft.addItemListener(event -> {
			var item = (ControllerEnum) controllerSelectorTopLeft.getSelectedItem();
			if (item == ControllerEnum.NONE) {
				players[0] = null;
				controllers[0] = null;
			} else {
				players[0] = new Player(map, 0, 0);
				controllers[0] = item.createController();
			}
			game.updateControllers(players, controllers);
		});

		controllerSelectorTopRight.addItemListener(event -> {
			var item = (ControllerEnum) controllerSelectorTopRight.getSelectedItem();
			if (item == ControllerEnum.NONE) {
				players[1] = null;
				controllers[1] = null;
			} else {
				players[1] = new Player(map, map.getWidth() - 1, 0);
				controllers[1] = item.createController();
			}
			game.updateControllers(players, controllers);
		});

		controllerSelectorBottomLeft.addItemListener(event -> {
			var item = (ControllerEnum) controllerSelectorBottomLeft.getSelectedItem();
			if (item == ControllerEnum.NONE) {
				players[2] = null;
				controllers[2] = null;
			} else {
				players[2] = new Player(map, 0, map.getHeight() - 1);
				controllers[2] = item.createController();
			}
			game.updateControllers(players, controllers);
		});

		controllerSelectorBottomRight.addItemListener(event -> {
			var item = (ControllerEnum) controllerSelectorBottomRight.getSelectedItem();
			if (item == ControllerEnum.NONE) {
				players[3] = null;
				controllers[3] = null;
			} else {
				players[3] = new Player(map, map.getWidth() - 1, map.getHeight() - 1);
				controllers[3] = item.createController();
			}
			game.updateControllers(players, controllers);
		});
	}

	private void setupUi() {
		setTitle("BomberBEW");
		setUndecorated(true);
		setLocation(0, 0);
		setSize(Toolkit.getDefaultToolkit().getScreenSize());

		setLayout(new BorderLayout());

		// toolbar north
		var toolbarNorth = new JPanel();
		toolbarNorth.setBackground(Color.BLACK);
		toolbarNorth.setLayout(new BorderLayout());
		add(toolbarNorth, BorderLayout.NORTH);

		controllerSelectorTopLeft = new JComboBox<>();
		controllerSelectorTopLeft.setFocusable(false);
		toolbarNorth.add(controllerSelectorTopLeft, BorderLayout.WEST);

		controllerSelectorTopRight = new JComboBox<>();
		controllerSelectorTopRight.setFocusable(false);
		toolbarNorth.add(controllerSelectorTopRight, BorderLayout.EAST);

		var controllButtons = new JPanel();
		controllButtons.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 0));
		controllButtons.setBackground(Color.BLACK);
		toolbarNorth.add(controllButtons, BorderLayout.CENTER);

		// start button
		start = new JButton("Start");
		start.setFocusable(false);
		controllButtons.add(start);

		// stop button
		stop = new JButton("Stop");
		stop.setEnabled(false);
		stop.setFocusable(false);
		controllButtons.add(stop);

		// viewport
		add(new Viewport(game), BorderLayout.CENTER);

		// toolbar south
		var toolbarSouth = new JPanel();
		toolbarSouth.setBackground(Color.BLACK);
		toolbarSouth.setLayout(new BorderLayout());
		add(toolbarSouth, BorderLayout.SOUTH);

		controllerSelectorBottomLeft = new JComboBox<>();
		controllerSelectorBottomLeft.setFocusable(false);
		toolbarSouth.add(controllerSelectorBottomLeft, BorderLayout.WEST);

		controllerSelectorBottomRight = new JComboBox<>();
		controllerSelectorBottomRight.setFocusable(false);
		toolbarSouth.add(controllerSelectorBottomRight, BorderLayout.EAST);

		for (var controller : ControllerEnum.values()) {
			controllerSelectorTopLeft.addItem(controller);
			controllerSelectorTopRight.addItem(controller);
			controllerSelectorBottomLeft.addItem(controller);
			controllerSelectorBottomRight.addItem(controller);
		}

		addListeners();
		setVisible(true);
	}
}
