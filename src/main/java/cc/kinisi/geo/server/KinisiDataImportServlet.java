
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

import org.apache.cayenne.CayenneRuntimeException;
import org.json.JSONException;

import cc.kinisi.geo.data.DeviceLocation;
import cc.kinisi.geo.data.conversion.JsonGeoDataImporter;

@WebServlet("/api/import")
public class KinisiDataImportServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
			
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
			controller.authorizeDeviceLocationsForRequest(locs, req);
      if (locs.size() > 0) {
        controller.saveDeviceLocations(locs);
      }

		} catch (UnauthorizedException e) {
      resp.sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
		} catch (JSONException|IOException e) {
			resp.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
		} catch (CayenneRuntimeException e) {
			resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
		}

	}
}
