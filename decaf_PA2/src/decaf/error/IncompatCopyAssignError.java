package decaf.error;

import decaf.Location;

public class IncompatCopyAssignError extends DecafError {

	private String source;
	private String destination;
	
	public IncompatCopyAssignError(Location location, String source, String destination) {
		super(location);
		this.source = source;
		this.destination = destination;
	}

	@Override
	protected String getErrMsg() {
		return "For copy expr, the source " 
				+ source + " and the destination "
				+ destination + " are not same";
	}

}
