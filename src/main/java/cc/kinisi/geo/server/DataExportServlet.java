package cc.kinisi.geo.server;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cc.kinisi.geo.data.DeviceLocation;
import cc.kinisi.geo.data.conversion.GeoDataExporter;
import cc.kinisi.geo.data.conversion.GeoDataExporter.ExportFormat;

@WebServlet("/api/export")
public class DataExportServlet extends KinisiServlet {

	private static final long serialVersionUID = 1L;

	private static final ExportFormat DEFAULT_FORMAT = ExportFormat.CSV;
	
	private static final String PARAM_FORMAT = "format";

	private static final String ERR_FORMAT = "Format not supported";
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
				
		try {

			String deviceId = getAuthorizedDeviceId(req);
			    
			ExportFormat format = DEFAULT_FORMAT;
			String formatParam = req.getParameter(PARAM_FORMAT);
			if (formatParam != null) {
				try {
				format = ExportFormat.valueOf(formatParam.toUpperCase());
				} catch (IllegalArgumentException e) {
					throw new IllegalArgumentException(ERR_FORMAT + ": " + formatParam);
				}
			}
			
			GeoDataExporter exporter = format.getExporter();
			resp.setContentType(exporter.getContentType());
			List<DeviceLocation> locs = getController().getDeviceLocations(deviceId);
			exporter.writeDeviceLocations(locs, resp.getWriter());
			
		} catch (IllegalArgumentException e) {
			resp.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
		} catch (UnauthorizedException e) {
      resp.sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
		}
		
	}

}
