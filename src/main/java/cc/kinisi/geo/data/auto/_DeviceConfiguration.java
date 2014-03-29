package cc.kinisi.geo.data.auto;

import org.apache.cayenne.CayenneDataObject;

import cc.kinisi.geo.data.ApiToken;

/**
 * Class _DeviceConfiguration was generated by Cayenne.
 * It is probably a good idea to avoid changing this class manually,
 * since it may be overwritten next time code is regenerated.
 * If you need to make any customizations, please use subclass.
 */
public abstract class _DeviceConfiguration extends CayenneDataObject {

    public static final String DEVICE_ID_PROPERTY = "deviceId";
    public static final String VALUE_PROPERTY = "value";
    public static final String API_TOKEN_PROPERTY = "apiToken";

    public static final String ID_PK_COLUMN = "id";

    public void setDeviceId(String deviceId) {
        writeProperty("deviceId", deviceId);
    }
    public String getDeviceId() {
        return (String)readProperty("deviceId");
    }

    public void setValue(String value) {
        writeProperty("value", value);
    }
    public String getValue() {
        return (String)readProperty("value");
    }

    public void setApiToken(ApiToken apiToken) {
        setToOneTarget("apiToken", apiToken, true);
    }

    public ApiToken getApiToken() {
        return (ApiToken)readProperty("apiToken");
    }


}
