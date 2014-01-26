package cc.kinisi.geo.data.conversion;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

import cc.kinisi.geo.data.DeviceLocation;

public interface GeoDataExporter {
	
	public static enum ExportFormat {
		
		CSV(new CsvGeoDataExporter()),
		KML(new KmlGeoDataExporter());
		
		private final GeoDataExporter exporter;
		
		ExportFormat(GeoDataExporter exp) {
			exporter = exp;
		}
		
		public GeoDataExporter getExporter() {
			return exporter;
		}
	}
	
	public void writeDeviceLocations(List<DeviceLocation> locs, Writer writer) throws IOException;

}
