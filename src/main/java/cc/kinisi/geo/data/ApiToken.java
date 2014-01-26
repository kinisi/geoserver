package cc.kinisi.geo.data;

import cc.kinisi.geo.data.auto._ApiToken;

public class ApiToken extends _ApiToken {

	private static final long serialVersionUID = 1L;

	static final int STATUS_DISABLED = 0;
	static final int STATUS_ENABLED = 1;

	public boolean isValid() {
		if (getToken() != null)
			return getStatus() == ApiToken.STATUS_ENABLED;
		return false;
	}
	
	public boolean isEnabled() {
		return getStatus() == 1;
	}
	
	public void enable() {
		setStatus((short) STATUS_ENABLED);
	}
	
	public void disable() {
		setStatus((short) STATUS_DISABLED);
	}

}
