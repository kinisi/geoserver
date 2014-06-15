package cc.kinisi.geo.data;

import java.util.Date;

import org.apache.cayenne.ObjectContext;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

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

  public String getFormattedMeasureTime(String format) {
    DateTime mdt = new DateTime(getMeasureTime());
    DateTimeFormatter f = DateTimeFormat.forPattern(format);
    return f.print(mdt);
  }

  public String getFormattedReceiveTime(String format) {
    DateTime mdt = new DateTime(getReceiveTime());
    DateTimeFormatter f = DateTimeFormat.forPattern(format);
    return f.print(mdt);
  }

  public String getDirection() {
    Double track = getTrack();
    if ((track > 337.5 && track <= 360) || (track >= 0.0 && track <= 22.5 ))
      return "N";
    else if (track > 22.5 && track <= 67.5)
      return "NE";
    else if (track > 67.5 && track <= 112.5)
      return "E";
    else if (track > 112.5 && track <= 157.5)
      return "SE";
    else if (track > 157.5 && track <= 202.5)
      return "S";
    else if (track > 202.5 && track <= 247.5)
      return "SW";
    else if (track > 247.5 && track <= 292.5)
      return "W";
    else if (track > 292.5 && track <= 337.5)
      return "NW";
    else
      return "??";
  }
  
}
