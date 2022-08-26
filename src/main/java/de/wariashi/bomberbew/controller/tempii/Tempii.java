package de.wariashi.bomberbew.controller.tempii;

import java.awt.image.BufferedImage;

import de.wariashi.bomberbew.Textures;
import de.wariashi.bomberbew.controller.Controller;
import de.wariashi.bomberbew.controller.ControllerInput;
import de.wariashi.bomberbew.controller.ControllerOutput;

public class Tempii implements Controller {
	@Override
	public String getName() {
		return "Tempii";
	}

	@Override
	public BufferedImage getPlayerImage() {
		return Textures.getTempii();
	}

	@Override
	public ControllerOutput update(ControllerInput input) {
		return new ControllerOutput();
	}
}
