package cc.kinisi.geo.data.conversion;

import org.apache.velocity.Template;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;

public enum TemplateExportFormat {

  CSV("csv.vm"), 
  CSV_TORRANCE("csv_torrance.vm"),
  KML("kml.vm");

  private String templateName;

  TemplateExportFormat(String name) {
    templateName = name;
  }

  public Template getTemplate() throws TemplateLoadException {
    Template template = null;
    try {
      template = Velocity.getTemplate(templateName);
    } catch (ResourceNotFoundException | ParseErrorException
        | MethodInvocationException e) {
      String msg = String.format("Error loading template \"%s\"", templateName);
      throw new TemplateLoadException(msg, e);
    }
    return template;
  }

  public String getContentType() {
    switch (this) {
    case CSV:
      return "text/csv";
    case CSV_TORRANCE:
      return "text/csv";
    case KML:
      return "application/vnd.google-earth.kml+xml";
    default:
      return "text/plain";
    }
  }

}
