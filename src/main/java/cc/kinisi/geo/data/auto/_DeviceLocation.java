package cc.kinisi.geo.data.auto;

import java.util.Date;

import org.apache.cayenne.CayenneDataObject;

/**
 * Class _DeviceLocation was generated by Cayenne.
 * It is probably a good idea to avoid changing this class manually,
 * since it may be overwritten next time code is regenerated.
 * If you need to make any customizations, please use subclass.
 */
public abstract class _DeviceLocation extends CayenneDataObject {

    public static final String ALTITUDE_PROPERTY = "altitude";
    public static final String CLIMB_PROPERTY = "climb";
    public static final String DEVICE_ID_PROPERTY = "deviceId";
    public static final String EPV_PROPERTY = "epv";
    public static final String EPX_PROPERTY = "epx";
    public static final String EPY_PROPERTY = "epy";
    public static final String LATITUDE_PROPERTY = "latitude";
    public static final String LONGITUDE_PROPERTY = "longitude";
    public static final String MEASURE_TIME_PROPERTY = "measureTime";
    public static final String RECEIVE_TIME_PROPERTY = "receiveTime";
    public static final String SPEED_PROPERTY = "speed";
    public static final String TRACK_PROPERTY = "track";

    public static final String ID_PK_COLUMN = "id";

    public void setAltitude(Double altitude) {
        writeProperty("altitude", altitude);
    }
    public Double getAltitude() {
        return (Double)readProperty("altitude");
    }

    public void setClimb(Double climb) {
        writeProperty("climb", climb);
    }
    public Double getClimb() {
        return (Double)readProperty("climb");
    }

    public void setDeviceId(String deviceId) {
        writeProperty("deviceId", deviceId);
    }
    public String getDeviceId() {
        return (String)readProperty("deviceId");
    }

    public void setEpv(Double epv) {
        writeProperty("epv", epv);
    }
    public Double getEpv() {
        return (Double)readProperty("epv");
    }

    public void setEpx(Double epx) {
        writeProperty("epx", epx);
    }
    public Double getEpx() {
        return (Double)readProperty("epx");
    }

    public void setEpy(Double epy) {
        writeProperty("epy", epy);
    }
    public Double getEpy() {
        return (Double)readProperty("epy");
    }

    public void setLatitude(Double latitude) {
        writeProperty("latitude", latitude);
    }
    public Double getLatitude() {
        return (Double)readProperty("latitude");
    }

    public void setLongitude(Double longitude) {
        writeProperty("longitude", longitude);
    }
    public Double getLongitude() {
        return (Double)readProperty("longitude");
    }

    public void setMeasureTime(Date measureTime) {
        writeProperty("measureTime", measureTime);
    }
    public Date getMeasureTime() {
        return (Date)readProperty("measureTime");
    }

    public void setReceiveTime(Date receiveTime) {
        writeProperty("receiveTime", receiveTime);
    }
    public Date getReceiveTime() {
        return (Date)readProperty("receiveTime");
    }

    public void setSpeed(Double speed) {
        writeProperty("speed", speed);
    }
    public Double getSpeed() {
        return (Double)readProperty("speed");
    }

    public void setTrack(Double track) {
        writeProperty("track", track);
    }
    public Double getTrack() {
        return (Double)readProperty("track");
    }

    protected abstract void onPostAdd();

}
