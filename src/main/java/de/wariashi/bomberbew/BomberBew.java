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
	private static final double BRICK_DENSITY = 0.75;
	private static final int MAP_WIDTH = 13;
	private static final int MAP_HEIGHT = 9;

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
	private JButton reset;
	private Viewport viewport;

	public static void main(String[] args) {
		new BomberBew();
	}

	public BomberBew() {
		map = new Map(MAP_WIDTH, MAP_HEIGHT, BRICK_DENSITY);
		game = new Game(map, players, controllers);
		Clock.setGame(game);
		Clock.setTicksPerSecond(100);
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
			reset.setEnabled(false);
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
			reset.setEnabled(true);
		});

		reset.addActionListener(event -> {
			map = new Map(MAP_WIDTH, MAP_HEIGHT, BRICK_DENSITY);

			var controller0 = (ControllerEnum) controllerSelectorTopLeft.getSelectedItem();
			updateController(0, controller0, 0, 0);
			var controller1 = (ControllerEnum) controllerSelectorTopRight.getSelectedItem();
			updateController(1, controller1, map.getWidth() - 1, 0);
			var controller2 = (ControllerEnum) controllerSelectorBottomLeft.getSelectedItem();
			updateController(2, controller2, 0, map.getHeight() - 1);
			var controller3 = (ControllerEnum) controllerSelectorBottomRight.getSelectedItem();
			updateController(3, controller3, map.getWidth() - 1, map.getHeight() - 1);

			game = new Game(map, players, controllers);
			Clock.setGame(game);
			viewport.setGame(game);
		});

		controllerSelectorTopLeft.addItemListener(event -> {
			var item = (ControllerEnum) controllerSelectorTopLeft.getSelectedItem();
			updateController(0, item, 0, 0);
		});

		controllerSelectorTopRight.addItemListener(event -> {
			var item = (ControllerEnum) controllerSelectorTopRight.getSelectedItem();
			updateController(1, item, map.getWidth() - 1, 0);
		});

		controllerSelectorBottomLeft.addItemListener(event -> {
			var item = (ControllerEnum) controllerSelectorBottomLeft.getSelectedItem();
			updateController(2, item, 0, map.getHeight() - 1);
		});

		controllerSelectorBottomRight.addItemListener(event -> {
			var item = (ControllerEnum) controllerSelectorBottomRight.getSelectedItem();
			updateController(3, item, map.getWidth() - 1, map.getHeight() - 1);
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

		// reset button
		reset = new JButton("Reset");
		reset.setFocusable(false);
		controllButtons.add(reset);

		// viewport
		viewport = new Viewport(game);
		add(viewport, BorderLayout.CENTER);

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

	private void updateController(int index, ControllerEnum controller, int playerX, int playerY) {
		if (controller == ControllerEnum.NONE) {
			players[index] = null;
			controllers[index] = null;
		} else {
			players[index] = new Player(map, playerX, playerY);
			controllers[index] = controller.createController();
		}
		game.updateControllers(players, controllers);
	}
}
