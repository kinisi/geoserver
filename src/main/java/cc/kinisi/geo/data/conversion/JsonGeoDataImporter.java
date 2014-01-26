package cc.kinisi.geo.data.conversion;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import cc.kinisi.geo.data.DeviceLocation;

public class JsonGeoDataImporter {
	
	private static final String DEVICE_ID_KEY = "deviceId";
	private static final String DEVICE_RECORDS_KEY = "deviceRecords";
	
	private static final String DEVICE_RECORD_CLIMB_KEY = "climb";
	private static final String DEVICE_RECORD_ALTITUDE_KEY = "alt";
	private static final String DEVICE_RECORD_LATITUDE_KEY = "lat";
	private static final String DEVICE_RECORD_LONGITUDE_KEY = "lon";
	private static final String DEVICE_RECORD_SPEED_KEY = "speed";
	private static final String DEVICE_RECORD_TRACK_KEY = "track";
	private static final String DEVICE_RECORD_TIME_KEY = "time";
	
	private static DeviceLocation deviceLocationFromJson(JSONObject rec)
			throws JSONException {
	
		double climb = rec.getDouble(DEVICE_RECORD_CLIMB_KEY);
		double alt = rec.getDouble(DEVICE_RECORD_ALTITUDE_KEY);
		double lat = rec.getDouble(DEVICE_RECORD_LATITUDE_KEY);
		double lon = rec.getDouble(DEVICE_RECORD_LONGITUDE_KEY);
		double speed = rec.getDouble(DEVICE_RECORD_SPEED_KEY);
		double track = rec.getDouble(DEVICE_RECORD_TRACK_KEY);
		String mtime = rec.getString(DEVICE_RECORD_TIME_KEY);
	
		DeviceLocation dl = new DeviceLocation();
		dl.setAltitude(alt);
		dl.setClimb(climb);
		dl.setLatitude(lat);
		dl.setLongitude(lon);
		dl.setSpeed(speed);
		dl.setTrack(track);
		DateTimeFormatter fmt = ISODateTimeFormat.dateTime();
		DateTime dt = fmt.parseDateTime(mtime);
		dl.setMeasureTime(dt.toDate());
	
		return dl;
	
	}

	public static List<DeviceLocation> readDeviceLocations(Reader reader) throws JSONException,
			IOException {
		JSONObject payload = new JSONObject(new JSONTokener(reader));
		String deviceId = payload.getString(DEVICE_ID_KEY);
		JSONArray objects = payload.getJSONArray(DEVICE_RECORDS_KEY);
		int size = objects.length();
		List<DeviceLocation> dlocs = new ArrayList<>(size);
		for (int i = 0; i < size; i++) {
			JSONObject dlObj = objects.getJSONObject(i);
			DeviceLocation dl = deviceLocationFromJson(dlObj);
			dl.setDeviceId(deviceId);
			dlocs.add(dl);
		}
		return dlocs;
	}

}
