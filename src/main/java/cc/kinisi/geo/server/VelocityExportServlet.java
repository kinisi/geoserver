package cc.kinisi.geo.server;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import cc.kinisi.geo.data.DeviceLocation;
import cc.kinisi.geo.data.conversion.TemplateExportFormat;
import cc.kinisi.geo.data.conversion.TemplateLoadException;

@WebServlet("/api/export")
public class VelocityExportServlet extends KinisiServlet {

  private static final long serialVersionUID = 1L;
  
  private static final String VELOCITY_CONFIG = "/velocity.properties";

  private static final TemplateExportFormat DEFAULT_FORMAT = TemplateExportFormat.CSV;
  
  private static final String PARAM_FORMAT = "format";
  private static final String PARAM_DEVICE_ID = "device_id";
  private static final String PARAM_START_TIME = "start_time";
  private static final String PARAM_END_TIME = "end_time";
  
  private static final String ERR_MISSING_DEVICE_ID = "Valid " + PARAM_DEVICE_ID + " parameter must be provided.";
  private static final String ERR_FORMAT = "Format not supported";

  @Override
  public void init(ServletConfig config) throws ServletException {
    super.init(config);
    try {
      InputStream is = getClass().getResourceAsStream(VELOCITY_CONFIG);
      Properties p = new Properties();
      p.load(is);
      Velocity.init(p);
    } catch (IOException e) {
      String msg = String.format("Error loading %s", VELOCITY_CONFIG);
      throw new ServletException(msg, e);
    }
  }
  
  protected static DateTime parseTimeParam(String dateParam) {
    DateTime dt = null;
    if (dateParam != null) {
      DateTimeFormatter f = DateTimeFormat.forPattern("yyyyMMddHHmmss");
      dt = f.parseDateTime(dateParam);
    }
    return dt;
  }
  
  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
        
    try {
            
      ServerController controller = getController();
      
      String deviceId = req.getParameter(PARAM_DEVICE_ID);
      if (deviceId == null) 
        throw new IllegalArgumentException(ERR_MISSING_DEVICE_ID);
      controller.authorizeDeviceIdForRequest(deviceId, req);
      TemplateExportFormat format = DEFAULT_FORMAT;
      String formatParam = req.getParameter(PARAM_FORMAT);
      if (formatParam != null) {
        try {
        format = TemplateExportFormat.valueOf(formatParam.toUpperCase());
        } catch (IllegalArgumentException e) {
          throw new IllegalArgumentException(ERR_FORMAT + ": " + formatParam);
        }
      }

      DateTime startDate = parseTimeParam(req.getParameter(PARAM_START_TIME));
      DateTime endDate = parseTimeParam(req.getParameter(PARAM_END_TIME));
      
      List<DeviceLocation> locs = controller.getDeviceLocations(deviceId, startDate, endDate);
      resp.setContentType(format.getContentType());
      VelocityContext context = new VelocityContext();
      context.put("deviceId", deviceId);
      context.put("deviceLocations", locs);
      format.getTemplate().merge(context, resp.getWriter());
      
    } catch (TemplateLoadException e) {
      resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
      e.printStackTrace();
    } catch (IllegalArgumentException e) {
      resp.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
    } catch (UnauthorizedException e) {
      resp.sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
    }
    
  }
}
