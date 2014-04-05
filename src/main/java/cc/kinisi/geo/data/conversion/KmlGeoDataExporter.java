package cc.kinisi.geo.data.conversion;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cc.kinisi.geo.data.DeviceLocation;

public class KmlGeoDataExporter implements GeoDataExporter {
  
  public static final String KML_MIME_TYPE = "application/vnd.google-earth.kml+xml";

	private static Map<String,List<DeviceLocation>> groupByDeviceId(List<DeviceLocation> locs) {
		Map<String,List<DeviceLocation>> locsById = new HashMap<>(locs.size());
		for(DeviceLocation l : locs) {
			String id = l.getDeviceId();
			List<DeviceLocation> idLocs = locsById.get(id);
			if (idLocs == null) {
				idLocs = new ArrayList<DeviceLocation>();
				locsById.put(id, idLocs);
			}
			idLocs.add(l);
		}
 		return locsById;
	}
	
	@Override
	public void writeDeviceLocations(List<DeviceLocation> locs, Writer writer) throws IOException {
		Map<String,List<DeviceLocation>> locsById = groupByDeviceId(locs);
		writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
		writer.write("<kml xmlns=\"http://www.opengis.net/kml/2.2\">\n");
		writer.write("  <Document>\n");
		for(String deviceId : locsById.keySet()) {
			writer.write("    <Placemark>\n");
			writer.write("      <name>" + deviceId + "</name>\n");
			writer.write("      <LineString>\n");
			writer.write("        <extrude>1</extrude>\n");
			writer.write("        <tessellate>1</tessellate>\n");
			writer.write("        <altitudeMode>absolute</altitudeMode>\n");
			writer.write("        <coordinates>\n");
			for(DeviceLocation l : locsById.get(deviceId)) {
			  // replace null with 0.0 so that we at least have something to write;
			  // longer term should handle missing data in a more KML-appropriate way
				Double lon = l.getLongitude();
				if (lon == null)
				  lon = 0.0;
				Double lat = l.getLatitude();
				if (lat == null)
				  lat = 0.0;
				Double alt = l.getAltitude();
				if (alt == null)
				  alt = 0.0;
				writer.write(String.format("          %f,%f,%f\n", lon,lat,alt));
			}
			writer.write("        </coordinates>\n");
			writer.write("      </LineString>\n");
			writer.write("    </Placemark>\n");
		}
		writer.write("  </Document>\n");
		writer.write("</kml>\n");
		writer.flush();

	}

  @Override
  public String getContentType() {
    return KML_MIME_TYPE;
  }

}
