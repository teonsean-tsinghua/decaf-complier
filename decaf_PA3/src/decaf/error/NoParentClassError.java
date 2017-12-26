package decaf.error;

import decaf.Location;

public class NoParentClassError extends DecafError {

	private String name;
	
	public NoParentClassError(Location location, String name) {
		super(location);
		this.name = name;
	}

	@Override
	protected String getErrMsg() {
		return "no parent class exist for " + name;
	}

}
