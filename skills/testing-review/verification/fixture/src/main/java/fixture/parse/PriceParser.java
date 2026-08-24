package fixture.parse;

import java.util.Optional;

/**
 * Parses price strings of the form "CODE:AMOUNT", for example "USD:12.34".
 *
 * Defined behavior:
 * - A valid input returns a Price carrying the currency code and the amount
 *   in cents.
 * - Malformed input is an expected outcome and returns Optional.empty(). The
 *   malformed cases are: wrong shape (no colon, empty parts), an unknown
 *   currency code, and a negative amount.
 * - Supported currency codes are exactly USD, EUR, and GBP, per the codes
 *   service.
 * - Amounts round half-up to whole cents. Rounding is identical for every
 *   supported currency.
 * - Amounts of a trillion units or more are malformed and return
 *   Optional.empty().
 * - raw must not be null. Passing null is a programmer error, not malformed
 *   input, and fails immediately.
 */
public final class PriceParser {

  private static final java.math.BigDecimal MAX_AMOUNT = new java.math.BigDecimal("1000000000000");

  private final SupportedCurrencies currencies;

  public PriceParser(SupportedCurrencies currencies) {
    this.currencies = currencies;
  }

  public Optional<Price> parse(String raw) {
    java.util.Objects.requireNonNull(raw, "raw");
    String[] parts = raw.split(":", -1);
    if (parts.length != 2 || parts[0].isEmpty() || parts[1].isEmpty()) {
      return Optional.empty();
    }
    if (!currencies.isSupported(parts[0])) {
      return Optional.empty();
    }
    java.math.BigDecimal amount;
    try {
      amount = new java.math.BigDecimal(parts[1]);
    } catch (NumberFormatException e) {
      return Optional.empty();
    }
    if (amount.signum() < 0 || amount.compareTo(MAX_AMOUNT) >= 0) {
      return Optional.empty();
    }
    long cents = amount.movePointRight(2)
        .setScale(0, java.math.RoundingMode.HALF_UP)
        .longValueExact();
    return Optional.of(new Price(parts[0], cents));
  }

  public static final class Price {
    public final String currency;
    public final long cents;

    Price(String currency, long cents) {
      this.currency = currency;
      this.cents = cents;
    }
  }
}
