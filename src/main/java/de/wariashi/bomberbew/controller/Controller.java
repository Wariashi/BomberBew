package de.wariashi.bomberbew.controller;

import java.awt.image.BufferedImage;

import de.wariashi.bomberbew.model.Game;
import de.wariashi.bomberbew.model.Player;

/**
 * A {@link Controller} can be used to control a {@link Player}.
 */
public interface Controller {
	/**
	 * Returns a human readable name of the {@link Controller} which can be used by
	 * the UI.
	 * 
	 * @return a human readable name of the {@link Controller}
	 */
	public String getName();

	/**
	 * Returns an image to represent the {@link Player} in the UI. This method will
	 * be called several hundred times a second and should therefore be as efficient
	 * as possible e.g. by caching the image.
	 * 
	 * @return an image representing the {@link Player} in the UI
	 */
	public BufferedImage getPlayerImage();

	/**
	 * This method is called periodically to let the {@link Controller} decide what
	 * to do next.
	 * 
	 * @param input a {@link ControllerInput} object containing some information
	 *              about the current state of the {@link Game}. This value can
	 *              never be <code>null</code>.
	 * @return a {@link ControllerOutput} object which contains information about
	 *         what the {@link Player} should do next. This value can never be
	 *         <code>null</code>.
	 */
	public ControllerOutput update(ControllerInput input);
}
