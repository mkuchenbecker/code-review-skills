package fixture.parse;

import java.util.Set;

/** The supported codes, as the codes service defines them. */
public final class FixedCurrencies implements SupportedCurrencies {

  private static final Set<String> CODES = Set.of("USD", "EUR", "GBP");

  @Override
  public boolean isSupported(String code) {
    return CODES.contains(code);
  }
}
