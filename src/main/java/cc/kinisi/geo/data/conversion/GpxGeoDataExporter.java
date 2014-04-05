package cc.kinisi.geo.data.conversion;

import java.io.Writer;
import java.util.List;

import cc.kinisi.geo.data.DeviceLocation;

public class GpxGeoDataExporter implements GeoDataExporter {

  public static final String GPX_MIME_TYPE = "text/plain";
  
	@Override
	public void writeDeviceLocations(List<DeviceLocation> locs, Writer writer) {
		// TODO Auto-generated method stub
		
	}

  @Override
  public String getContentType() {
    // TODO Auto-generated method stub
    return GPX_MIME_TYPE;
  }

}
