package com.mycompany.sample.domain;

import Exceptions.RetrieveDataexception;
import Exceptions.SaveDataException;

public interface DataStore {
    void saveBankAccount(BankAccount bankAccount) throws SaveDataException;
    void saveWalletList(WalletList walletList) throws SaveDataException;
    BankAccount retrieveBankAccount()throws RetrieveDataexception;
    WalletList retrieveWalletList() throws RetrieveDataexception;

}
