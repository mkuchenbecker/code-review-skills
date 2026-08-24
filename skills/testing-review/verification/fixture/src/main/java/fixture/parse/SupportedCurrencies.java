package fixture.parse;

/** The currency codes the service supports: exactly USD, EUR, and GBP. */
public interface SupportedCurrencies {
  boolean isSupported(String code);
}
