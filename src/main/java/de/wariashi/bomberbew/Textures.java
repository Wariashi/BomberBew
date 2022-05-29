package de.wariashi.bomberbew;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

public class Textures {
	private static BufferedImage brick;
	private static BufferedImage concrete;

	private Textures() throws IllegalAccessException {
		throw new IllegalAccessException();
	}

	static {
		loadTextures();
	}

	public static BufferedImage getBrick() {
		return brick;
	}

	public static BufferedImage getConcrete() {
		return concrete;
	}

	private static BufferedImage getTexture(String texturename) {
		var path = "textures" + File.separator + texturename;
		var classLoader = ClassLoader.getSystemClassLoader();
		try {
			return ImageIO.read(classLoader.getResource((path)));
		} catch (Exception exception) {
			exception.printStackTrace();
			return null;
		}
	}

	private static void loadTextures() {
		brick = getTexture("Brick.png");
		concrete = getTexture("Concrete.png");
	}
}
