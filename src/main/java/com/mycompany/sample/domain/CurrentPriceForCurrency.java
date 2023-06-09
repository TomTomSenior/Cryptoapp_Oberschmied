package com.mycompany.sample.domain;

import Exceptions.GetCurrentPriceException;

import java.math.BigDecimal;

public interface CurrentPriceForCurrency {
    BigDecimal getCurrentprice(CryptoCurrency cryptoCurrency)throws GetCurrentPriceException;

}
