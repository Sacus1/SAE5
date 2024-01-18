package org.SAE.Main;

public abstract class Base {
	/**
	 * call the static method getFromDatabase() of all the classes that extends Base
	 */
	public abstract void loadFromDatabase() ;
	protected abstract void delete();
}
