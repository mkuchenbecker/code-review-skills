package seedtest.core;

/** Error codes shared by all modules so callers can react uniformly. */
public final class ErrorCodes {
  public static final int NOT_FOUND = 1001;
  public static final int CONFLICT = 1002;
  public static final int STORAGE_DOWN = 1003;

  private ErrorCodes() {}
}
