package de.wariashi.bomberbew;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

public class Textures {
	private static BufferedImage[] bomb;
	private static BufferedImage brick;
	private static BufferedImage concrete;
	private static BufferedImage explosion;

	private Textures() throws IllegalAccessException {
		throw new IllegalAccessException();
	}

	static {
		loadTextures();
	}

	public static BufferedImage getBomb(double progress) {
		var index = (int) (progress * bomb.length) % bomb.length;
		return bomb[index];
	}

	public static BufferedImage getBrick() {
		return brick;
	}

	public static BufferedImage getConcrete() {
		return concrete;
	}

	public static BufferedImage getExplosion() {
		return explosion;
	}

	private static BufferedImage getTexture(String texturename) {
		var path = "textures" + File.separator + texturename;
		var classLoader = ClassLoader.getSystemClassLoader();
		try {
			return ImageIO.read(classLoader.getResource(path));
		} catch (Exception exception) {
			exception.printStackTrace();
			return null;
		}
	}

	private static BufferedImage[] getTexture(String texturename, int parts) {
		var result = new BufferedImage[parts];
		var path = "textures" + File.separator + texturename;
		var classLoader = ClassLoader.getSystemClassLoader();
		try {
			var image = ImageIO.read(classLoader.getResource(path));
			var height = image.getHeight();
			var width = image.getWidth();
			for (int i = 0; i < parts; i++) {
				result[i] = image.getSubimage(i * width / parts, 0, width / parts, height);
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		return result;
	}

	private static void loadTextures() {
		bomb = getTexture("Bomb.png", 8);
		brick = getTexture("Brick.png");
		concrete = getTexture("Concrete.png");
		explosion = getTexture("Explosion.png");
	}
}
