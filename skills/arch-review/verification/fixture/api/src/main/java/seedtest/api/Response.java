package seedtest.api;

/** Minimal HTTP response. */
public final class Response {
  public final int status;
  public final int errorCode;
  public final String body;

  private Response(int status, int errorCode, String body) {
    this.status = status;
    this.errorCode = errorCode;
    this.body = body;
  }

  public static Response ok(String body) {
    return new Response(200, 0, body);
  }

  public static Response error(int status, int errorCode) {
    return new Response(status, errorCode, "");
  }
}
