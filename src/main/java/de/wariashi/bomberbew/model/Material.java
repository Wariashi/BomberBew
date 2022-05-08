package de.wariashi.bomberbew.model;

/**
 * Represents the state of a tile within the {@link Map}.
 */
public enum Material {
	/**
	 * A tile that can not be removed by a {@link Player}, but will remove itself
	 * after a while while also creating an explosion.
	 */
	BOMB,

	/**
	 * A tile that can be removed by a {@link Player} by placing a bomb next to it.
	 */
	BRICK,

	/**
	 * A tile that can not be removed by a {@link Player}.
	 */
	CONCRETE,

	/**
	 * An empty tile that is passable by a {@link Player}.
	 */
	EMPTY;
}
