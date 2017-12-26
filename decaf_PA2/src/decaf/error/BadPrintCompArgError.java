package decaf.error;

import decaf.Location;

public class BadPrintCompArgError extends DecafError {

	private String count;

	private String type;

	public BadPrintCompArgError(Location location, String count, String type) {
		super(location);
		this.count = count;
		this.type = type;
	}

	@Override
	protected String getErrMsg() {
		return "incompatible argument " + count + ": " + type
				+ " given, complex expected";
	}
	
}
