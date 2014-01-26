package cc.kinisi.geo.data.conversion;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

import cc.kinisi.geo.data.DeviceLocation;

public class CsvGeoDataExporter implements GeoDataExporter {
	
	private static final String[] fields = {"measureTime", "latitude", "longitude", "speed", "altitude", "climb", "track"};
	
	private static String header;
	
	private static String getHeader() {
		if (header == null) {
			StringBuilder sb = new StringBuilder();
			for(int i = 0; i < fields.length; i++) {
				sb.append(fields[i]);
				if (i+1 < fields.length)
					sb.append(',');
			}
			header = sb.append('\n').toString();
		}
		return header;
	}
	
	private static String rowFor(DeviceLocation loc) {
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < fields.length; i++) {
			Object val = loc.readProperty(fields[i]);
			String strVal = val != null ? val.toString() : null;
			sb.append(strVal);
			if (i+1 < fields.length)
				sb.append(',');
		}
		return sb.append('\n').toString();
	}
	
	@Override
	public void writeDeviceLocations(List<DeviceLocation> locs, Writer writer) throws IOException {
		writer.write(getHeader());
		for (DeviceLocation loc : locs) {
			writer.write(rowFor(loc));
		}
		writer.flush();
	}

}
