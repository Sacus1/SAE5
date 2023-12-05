package org.SAE.Error;

import org.SAE.Main.Logger;

public class CannotAccessFieldException extends RuntimeException {
			public CannotAccessFieldException(String s) {
				super(s);
				Logger.error(s);
		}
}
