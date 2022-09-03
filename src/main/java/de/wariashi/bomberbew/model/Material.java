package de.wariashi.bomberbew.model;

/**
 * Represents the state of a tile within the {@link Map}.
 */
public enum Material {
	/**
	 * A tile that can not be removed by a {@link Player}, but will remove itself
	 * after a while while also creating an explosion.
	 */
	BOMB(true),

	/**
	 * A tile that can be removed by a {@link Player} by placing a bomb next to it.
	 */
	BRICK(true),

	/**
	 * A tile that can not be removed by a {@link Player}.
	 */
	CONCRETE(true),

	/**
	 * An empty tile that is passable by a {@link Player}.
	 */
	EMPTY(false),

	/**
	 * A tile that kills a {@link Player} standing on it.
	 */
	EXPLOSION(false);

	private final boolean isSolid;

	Material(boolean isSolid) {
		this.isSolid = isSolid;
	}

	/**
	 * Returns <code>true</code> if the material is not passable by a
	 * {@link Player}.
	 * 
	 * @return <code>true</code> if the material is not passable by a {@link Player}
	 */
	public boolean isSolid() {
		return isSolid;
	}
}
