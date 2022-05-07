package de.wariashi.bomberbew.controller;

import java.lang.reflect.InvocationTargetException;

import de.wariashi.bomberbew.controller.keyboard.KeyboardController;
import de.wariashi.bomberbew.controller.tempii.Tempii;
import de.wariashi.bomberbew.controller.wariashi.Wariashi;

public enum ControllerEnum {
	/**
	 * Used to represent the absence of a {@link Controller}.
	 */
	NONE(null),

	/**
	 * A {@link Controller} that is using an artificial intelligence written by
	 * Tempii.
	 */
	TEMPII(Tempii.class),

	/**
	 * A {@link Controller} that is using keyboard inputs.
	 */
	KEYBOARD(KeyboardController.class),

	/**
	 * A {@link Controller} that is using an artificial intelligence written by
	 * Wariashi.
	 */
	WARIASHI(Wariashi.class);

	private Class<? extends Controller> controllerClass;

	ControllerEnum(Class<? extends Controller> controller) {
		this.controllerClass = controller;
	}

	public Controller createController() {
		if (controllerClass == null) {
			return null;
		}
		try {
			var constructor = controllerClass.getDeclaredConstructor();
			return constructor.newInstance();
		} catch (IllegalAccessException | IllegalArgumentException | InstantiationException | InvocationTargetException
				| NoSuchMethodException | SecurityException exception) {
			return null;
		}
	}

	@Override
	public String toString() {
		var controller = createController();
		if (controller == null) {
			return "";
		} else {
			return controller.getName();
		}
	}
}
