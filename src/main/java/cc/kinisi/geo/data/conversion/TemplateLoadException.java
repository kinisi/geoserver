package cc.kinisi.geo.data.conversion;

public class TemplateLoadException extends Exception {

  private static final long serialVersionUID = 1L;

  public TemplateLoadException() {
  }

  public TemplateLoadException(String message) {
    super(message);
  }

  public TemplateLoadException(Throwable cause) {
    super(cause);
  }

  public TemplateLoadException(String message, Throwable cause) {
    super(message, cause);
  }

  public TemplateLoadException(String message, Throwable cause,
      boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }

}
