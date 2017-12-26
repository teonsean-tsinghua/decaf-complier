package decaf.error;

import decaf.Location;

public class FieldNotSupportedError extends DecafError {
	
	public FieldNotSupportedError(Location location) {
		super(location);
	}

	@Override
	protected String getErrMsg() {
		return "super.member_var is not supported";
	}

}
