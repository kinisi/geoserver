
package cc.kinisi.geo.server;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.cayenne.CayenneRuntimeException;
import org.json.JSONException;

import cc.kinisi.geo.data.DeviceConfiguration;
import cc.kinisi.geo.data.DeviceInterface;
import cc.kinisi.geo.data.DeviceLocation;
import cc.kinisi.geo.data.conversion.JsonGeoDataReader;

@WebServlet("/api/import")
public class KinisiDataImportServlet extends KinisiServlet {

	private static final long serialVersionUID = 1L;
			
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		try {
		  
		  ServerController controller = getController();
		  JsonGeoDataReader reader = new JsonGeoDataReader(req.getReader());
		  String deviceId = reader.getDeviceId();
		  controller.authorizeDeviceIdForRequest(deviceId, req);
		  List<DeviceLocation> locs = reader.getDeviceLocations();
      if (locs != null && locs.size() > 0) {
        controller.saveDeviceLocations(locs);
      }
      List<DeviceInterface> ifs = reader.getDeviceInterfaces();
      if (ifs != null && ifs.size() > 0) {
        controller.updateDeviceInterfacesForDeviceId(ifs, deviceId);
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
