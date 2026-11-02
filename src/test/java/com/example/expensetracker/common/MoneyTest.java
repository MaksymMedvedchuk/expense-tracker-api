package com.example.expensetracker.common;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Currency;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MoneyTest {

    @Test
    void of_shouldCreateMoney_whenAmountAndCurrencyCodeAreValid() {
        Money money = Money.of("12.50", "UAH");

        assertThat(money.amount()).isEqualTo(new BigDecimal("12.50"));
        assertThat(money.currency()).isEqualTo(Currency.getInstance("UAH"));
    }

    @Test
    void of_shouldThrowException_whenCurrencyCodeIsUnknown() {
        assertThatThrownBy(() -> Money.of("12.50", "XYZ"))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void of_shouldThrowException_whenAmountIsNotNumber() {
        assertThatThrownBy(() -> Money.of("abc", "UAH"))
                .isInstanceOf(NumberFormatException.class);
    }

    @Test
    void constructor_shouldThrowException_whenAmountIsNull() {
        assertThatThrownBy(() -> new Money(null, Currency.getInstance("UAH")))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("amount must not be null");
    }

    @Test
    void constructor_shouldThrowException_whenCurrencyIsNull() {
        assertThatThrownBy(() -> new Money(BigDecimal.TEN, null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("currency must not be null");
    }

    @Test
    void equals_shouldIgnoreScale_whenAmountsAreNumericallyEqual() {
        Money withOneDigit = Money.of("100.0", "UAH");
        Money withTwoDigits = Money.of("100.00", "UAH");

        assertThat(withOneDigit).isEqualTo(withTwoDigits);
        assertThat(withOneDigit.hashCode()).isEqualTo(withTwoDigits.hashCode());
    }

    @Test
    void equals_shouldReturnFalse_whenCurrenciesDiffer() {
        Money hryvnias = Money.of("10.00", "UAH");
        Money dollars = Money.of("10.00", "USD");

        assertThat(hryvnias).isNotEqualTo(dollars);
    }

    @Test
    void constructor_shouldRoundToCurrencyScale_whenAmountHasExtraDigits() {
        Money money = Money.of("100.005", "UAH");

        assertThat(money.amount()).isEqualTo(new BigDecimal("100.00"));
    }

    @Test
    void constructor_shouldRoundHalfUp_whenPrecedingDigitIsOdd() {
        Money money = Money.of("100.015", "UAH");

        assertThat(money.amount()).isEqualTo(new BigDecimal("100.02"));
    }

    @Test
    void constructor_shouldRoundHalfDown_whenPrecedingDigitIsEven() {
        Money money = Money.of("100.025", "UAH");

        assertThat(money.amount()).isEqualTo(new BigDecimal("100.02"));
    }

    @Test
    void constructor_shouldUseZeroScale_whenCurrencyHasNoMinorUnits() {
        Money money = Money.of("100.4", "JPY");

        assertThat(money.amount()).isEqualTo(new BigDecimal("100"));
    }

    @Test
    void constructor_shouldUseThreeDigitScale_whenCurrencyHasThreeMinorUnits() {
        Money money = Money.of("1.5", "BHD");

        assertThat(money.amount()).isEqualTo(new BigDecimal("1.500"));
    }

    @Test
    void add_shouldReturnSum_whenCurrenciesAreEqual() {
        Money first = Money.of("100.50", "UAH");
        Money second = Money.of("20.25", "UAH");

        Money sum = first.add(second);

        assertThat(sum).isEqualTo(Money.of("120.75", "UAH"));
    }

    @Test
    void add_shouldThrowException_whenCurrenciesDiffer() {
        Money hryvnias = Money.of("100.00", "UAH");
        Money dollars = Money.of("10.00", "USD");

        assertThatThrownBy(() -> hryvnias.add(dollars))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("currencies must match: UAH and USD");
    }

    @Test
    void divide_shouldRoundToCurrencyScale_whenResultHasInfiniteFraction() {
        Money money = Money.of("100.00", "UAH");

        Money result = money.divide(3);

        assertThat(result).isEqualTo(Money.of("33.33", "UAH"));
    }
}
