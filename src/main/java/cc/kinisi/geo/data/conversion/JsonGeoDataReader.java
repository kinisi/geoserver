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

import cc.kinisi.geo.data.DeviceInterface;
import cc.kinisi.geo.data.DeviceLocation;

public class JsonGeoDataReader {
	
	private static final String DEVICE_ID_KEY = "deviceId";
	private static final String DEVICE_RECORDS_KEY = "deviceRecords";
	private static final String DEVICE_MACS_KEY = "deviceMACs";
	
	private static final String DEVICE_RECORD_CLIMB_KEY = "climb";
	private static final String DEVICE_RECORD_ALTITUDE_KEY = "alt";
	private static final String DEVICE_RECORD_LATITUDE_KEY = "lat";
	private static final String DEVICE_RECORD_LONGITUDE_KEY = "lon";
	private static final String DEVICE_RECORD_SPEED_KEY = "speed";
	private static final String DEVICE_RECORD_TRACK_KEY = "track";
	private static final String DEVICE_RECORD_TIME_KEY = "time";
  private static final String DEVICE_RECORD_EPX_KEY = "epx";
  private static final String DEVICE_RECORD_EPY_KEY = "epy";
  private static final String DEVICE_RECORD_EPV_KEY = "epv";
	
	private static final DateTimeFormatter dtFormatter = ISODateTimeFormat.dateTime();
	
	private static DeviceLocation deviceLocationFromJson(JSONObject rec)
			throws JSONException {
	
		DeviceLocation dl = new DeviceLocation();
		if (rec.has(DEVICE_RECORD_LATITUDE_KEY))
			dl.setLatitude(rec.getDouble(DEVICE_RECORD_LATITUDE_KEY));
		if (rec.has(DEVICE_RECORD_LONGITUDE_KEY))
			dl.setLongitude(rec.getDouble(DEVICE_RECORD_LONGITUDE_KEY));
		if (rec.has(DEVICE_RECORD_ALTITUDE_KEY))
			dl.setAltitude(rec.getDouble(DEVICE_RECORD_ALTITUDE_KEY));
		if (rec.has(DEVICE_RECORD_SPEED_KEY))
			dl.setSpeed(rec.getDouble(DEVICE_RECORD_SPEED_KEY));
		if (rec.has(DEVICE_RECORD_TRACK_KEY))
			dl.setTrack(rec.getDouble(DEVICE_RECORD_TRACK_KEY));
		if (rec.has(DEVICE_RECORD_CLIMB_KEY))
			dl.setClimb(rec.getDouble(DEVICE_RECORD_CLIMB_KEY));
		if (rec.has(DEVICE_RECORD_EPX_KEY))
      dl.setEpx(rec.getDouble(DEVICE_RECORD_EPX_KEY));
    if (rec.has(DEVICE_RECORD_EPY_KEY))
      dl.setEpy(rec.getDouble(DEVICE_RECORD_EPY_KEY));
    if (rec.has(DEVICE_RECORD_EPV_KEY))
      dl.setEpv(rec.getDouble(DEVICE_RECORD_EPV_KEY));
		
		String mtime = rec.getString(DEVICE_RECORD_TIME_KEY);
		DateTime dt = dtFormatter.parseDateTime(mtime);
		dl.setMeasureTime(dt.toDate());
	
		return dl;
	
	}
	
	private final JSONObject body;
	private final String deviceId;
	
	public JsonGeoDataReader(Reader reader) {
	   body = new JSONObject(new JSONTokener(reader));
	   deviceId = body.getString(DEVICE_ID_KEY);
	}
	
	public String getDeviceId() {
	  return deviceId;
	}

  public List<DeviceLocation> getDeviceLocations() throws JSONException, IOException {
    List<DeviceLocation> dlocs = null;
    if (body.has(DEVICE_RECORDS_KEY)) {
      JSONArray objects = body.getJSONArray(DEVICE_RECORDS_KEY);
      int size = objects.length();
      dlocs = new ArrayList<>(size);
      for (int i = 0; i < size; i++) {
        JSONObject dlObj = objects.getJSONObject(i);
        DeviceLocation dl = deviceLocationFromJson(dlObj);
        dl.setDeviceId(deviceId);
        dlocs.add(dl);
      }
    }
    return dlocs;
	}
  
  public List<DeviceInterface> getDeviceInterfaces() {
    List<DeviceInterface> interfaces = null;
    if (body.has(DEVICE_MACS_KEY)) {
      interfaces = new ArrayList<>();
      JSONObject macs = body.getJSONObject(DEVICE_MACS_KEY);
      String[] names = JSONObject.getNames(macs);
      if (names != null) {
        for (String n : names) {
          String addr = macs.getString(n);
          DeviceInterface di = new DeviceInterface();
          di.setDeviceId(deviceId);
          di.setName(n);
          di.setAddress(addr);
          interfaces.add(di);
        }
      }
    }
    return interfaces;
  }

}
