package cc.kinisi.geo.server;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;

public class KinisiServlet extends HttpServlet {

  private static final long serialVersionUID = 1L;

  private static final String PARAM_DEVICE_ID = "device_id";
  private static final String ERR_MISSING_DEVICE_ID = "Valid " + PARAM_DEVICE_ID + " parameter must be provided.";

  private ServerController controller;

  @Override
  public void init(ServletConfig config) throws ServletException {
    super.init(config);
    ServletContext c = config.getServletContext();
    controller = (ServerController) c.getAttribute(ServerController.CONTROLLER_NAME);
  }

  @Override
  public void destroy() {
    controller = null;
  }

  protected ServerController getController() {
    return controller;
  }
  
  public String getAuthorizedDeviceId(HttpServletRequest req) throws UnauthorizedException {
    String deviceId = req.getParameter(PARAM_DEVICE_ID);
    if (deviceId == null) 
      throw new IllegalArgumentException(ERR_MISSING_DEVICE_ID);
    controller.authorizeDeviceIdForRequest(deviceId, req);
    return deviceId;
  }

}
