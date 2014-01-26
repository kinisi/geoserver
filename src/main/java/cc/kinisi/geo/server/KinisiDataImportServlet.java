
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

import org.json.JSONException;

import cc.kinisi.geo.data.DeviceLocation;
import cc.kinisi.geo.data.conversion.JsonGeoDataImporter;

@WebServlet("/api/import")
public class KinisiDataImportServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	private static final String ERR_POST_READ  = "Error reading POST data";
	private static final String ERR_JSON_PARSE = "JSON parse error";
	private static final String ERR_CAYENNE_ERR = "Error saving data";
	
	private ServerController controller;

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		ServletContext c = config.getServletContext();
		controller = (ServerController) c.getAttribute(ServerController.CONTROLLER_NAME);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		List<DeviceLocation> locs = null;
		try {
			locs = JsonGeoDataImporter.readDeviceLocations(req.getReader());
		} catch (JSONException e) {
			resp.sendError(HttpServletResponse.SC_BAD_REQUEST, ERR_JSON_PARSE
					+ ": " + e.getMessage());
		} catch (IOException e) {
			resp.sendError(HttpServletResponse.SC_BAD_REQUEST, ERR_POST_READ);
		}

		if (locs != null && locs.size() > 0) {
			try {
				controller.saveDeviceLocations(locs);
			} catch (IOException e) {
				String msg = e.getMessage();
				resp.sendError(HttpServletResponse.SC_BAD_REQUEST,
						ERR_CAYENNE_ERR + ": " + msg);
			}
		}
		
	}
}
