package decaf.error;

import decaf.Location;

public class BadDoStmtCondError extends DecafError {

	private String given;
	
	public BadDoStmtCondError(Location location, String given) {
		super(location);
		this.given = given;
	}

	@Override
	protected String getErrMsg() {
		return "The condition of Do Stmt requestd type bool but "
				+ given + " given";
	}

}
