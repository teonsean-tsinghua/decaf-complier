package decaf.error;

import decaf.Location;

public class DuplicateCondError extends DecafError {

	public DuplicateCondError(Location location) {
		super(location);
	}

	@Override
	protected String getErrMsg() {
		return "condition is not unique";
	}

}
