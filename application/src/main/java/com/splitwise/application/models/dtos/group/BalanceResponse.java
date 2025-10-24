package com.splitwise.application.models.dtos.group;

import java.math.BigDecimal;

public interface BalanceResponse {
    Long getUserId();

    BigDecimal getTotalCredit();

    BigDecimal getTotalDebit();

    BigDecimal getNetBalance();
}
