package com.mycompany.sample.domain;

import Exceptions.GetCurrentPriceException;

import java.math.BigDecimal;

public interface CurrentPriceForCurrency {
    BigDecimal getCurrentPrice(CryptoCurrency cryptoCurrency) throws GetCurrentPriceException;

}
