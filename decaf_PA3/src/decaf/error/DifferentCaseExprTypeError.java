package decaf.error;

import decaf.Location;

public class DifferentCaseExprTypeError extends DecafError {

	private String given;
	
	private String expected;
	
	public DifferentCaseExprTypeError(Location location, String given, String expected) {
		super(location);
		this.given = given;
		this.expected = expected;
	}

	@Override
	protected String getErrMsg() {
		return "type: " + given + " is different with other expr's type " + expected;
	}

}
