package org.SAE.Main;

import java.util.ArrayList;
import java.util.List;

public abstract class Base {

	/**
	 * call the static method getFromDatabase() of all the classes that extends Base
	 */
	public abstract void loadFromDatabase() ;

	protected abstract void delete();


}
