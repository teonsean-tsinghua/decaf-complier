package decaf.error;

import decaf.Location;

public class IncompatCaseExpError extends DecafError {

	private String type;
	
	public IncompatCaseExpError(Location location, String type) {
		super(location);
		this.type = type;
	}

	@Override
	protected String getErrMsg() {
		return "incompatible case expr: " + type + " given, but int expected";
	}

}
