package cc.kinisi.geo.data;

import java.util.Date;

import org.apache.cayenne.ObjectContext;

import cc.kinisi.geo.data.auto._DeviceLocation;

public class DeviceLocation extends _DeviceLocation {

	private static final long serialVersionUID = 1L;
	
	/**
	 * Callback called immediately after object is added to an
	 * {@link ObjectContext}. Use it to set 'now' as the default receiveTime for
	 * DeviceLocations
	 */
	protected void onPostAdd() {
		if (getReceiveTime() == null)
			setReceiveTime(new Date());
	}

}
