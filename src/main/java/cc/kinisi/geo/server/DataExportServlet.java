package cc.kinisi.geo.server;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cc.kinisi.geo.data.DeviceLocation;
import cc.kinisi.geo.data.conversion.GeoDataExporter;
import cc.kinisi.geo.data.conversion.GeoDataExporter.ExportFormat;

@WebServlet("/api/export")
public class DataExportServlet extends HttpServlet {

	private ServerController controller;
	
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		ServletContext c = config.getServletContext();
		controller = (ServerController) c.getAttribute(ServerController.CONTROLLER_NAME);
	}

	private static final long serialVersionUID = 1L;

	private static final ExportFormat DEFAULT_FORMAT = ExportFormat.CSV;
	
	private static final String PARAM_FORMAT = "format";
	private static final String PARAM_DEVICE_ID = "device_id";
	
	private static final String ERR_MISSING_DEVICE_ID = "Valid " + PARAM_DEVICE_ID + " parameter must be provided.";
	private static final String ERR_FORMAT = "Format not supported";
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
				
		try {
			
			String deviceId = req.getParameter(PARAM_DEVICE_ID);
			if (deviceId == null) 
				throw new IllegalArgumentException(ERR_MISSING_DEVICE_ID);
			controller.authorizeDeviceIdForRequest(deviceId, req);
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
			List<DeviceLocation> locs = controller.getDeviceLocations(deviceId);
			exporter.writeDeviceLocations(locs, resp.getWriter());
			
		} catch (IllegalArgumentException e) {
			resp.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
		} catch (UnauthorizedException e) {
      resp.sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
		}
		
	}

}
