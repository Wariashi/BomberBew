package de.wariashi.bomberbew;

import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;

import de.wariashi.bomberbew.model.Map;
import de.wariashi.bomberbew.model.Player;

@SuppressWarnings("serial")
public class BomberBew extends JFrame {
	private JPanel viewport;
	private transient Map map;
	private transient List<Player> players;

	public static void main(String[] args) {
		new BomberBew();
	}

	public BomberBew() {
		addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent key) {
				if (key.getKeyCode() == KeyEvent.VK_ESCAPE) {
					System.exit(0);
				}
			}
		});

		setTitle("BomberBEW");
		setUndecorated(true);
		setLocation(0, 0);
		setSize(Toolkit.getDefaultToolkit().getScreenSize());

		map = new Map(13, 9, 0.75);

		players = new ArrayList<>();
		players.add(new Player(0, 0));
		players.add(new Player(map.getWidth() - 1, 0));
		players.add(new Player(0, map.getHeight() - 1));
		players.add(new Player(map.getWidth() - 1, map.getHeight() - 1));

		viewport = new Viewport(map, players);
		add(viewport);

		setVisible(true);
	}
}
