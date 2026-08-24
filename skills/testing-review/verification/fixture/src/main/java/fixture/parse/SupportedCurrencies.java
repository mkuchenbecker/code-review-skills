package fixture.parse;

/**
 * The currency codes the service supports: exactly USD, EUR, and GBP.
 *
 * <p>code must not be null. Passing null is a programmer error, not an
 * unsupported code, and fails immediately.
 */
public interface SupportedCurrencies {
  boolean isSupported(String code);
}
