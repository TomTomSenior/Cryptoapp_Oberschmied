package Infrastruktur;

import Exceptions.RetrieveDataexception;
import Exceptions.SaveDataException;
import com.mycompany.sample.domain.BankAccount;
import com.mycompany.sample.domain.DataStore;
import com.mycompany.sample.domain.WalletList;

import java.io.*;

public class FileDataStore implements DataStore {

    @Override
    public void saveBankAccount(BankAccount bankAccount) throws SaveDataException {
        if (bankAccount !=null) {

            ObjectOutputStream objectOutputStream = null;
            try {
                objectOutputStream = new ObjectOutputStream
                        (new FileOutputStream("account.bin"));
                objectOutputStream.writeObject(bankAccount);
                objectOutputStream.close();
            } catch (IOException ioException) {

                ioException.printStackTrace();
                throw new SaveDataException("Error saving Bankaccount to File" + ioException.getMessage());
            }
        }
    }

    @Override
    public void saveWalletList(WalletList walletList) throws SaveDataException {
        if (walletList !=null) {
            ObjectOutputStream objectOutputStream = null;
            try {
                objectOutputStream = new ObjectOutputStream(new FileOutputStream("walletList.bin"));
                objectOutputStream.writeObject(walletList);
                objectOutputStream.close();
            } catch (IOException ioException) {
                ioException.printStackTrace();
                throw new SaveDataException("Error saving walletList to File"
                        + ioException.getMessage());
            }
        }
    }

    @Override
    public BankAccount retrieveBankAccount() throws RetrieveDataexception {
        ObjectInputStream objectInputStream = null;

        try {
            objectInputStream = new ObjectInputStream(new FileInputStream("account.bin"));
            BankAccount bankAccount = (BankAccount) objectInputStream.readObject();
            objectInputStream.close();
            return bankAccount;
        } catch (IOException | ClassNotFoundException eX) {
            eX.printStackTrace();
            throw new RetrieveDataexception("Error on retrieving Bankaccount Data from File" + eX.getMessage());

        }
    }

    @Override
    public WalletList retrieveWalletList() throws RetrieveDataexception {
        ObjectInputStream objectInputStream = null;

        try {
            objectInputStream = new ObjectInputStream(new FileInputStream("walletList.bin"));
            WalletList walletList =(WalletList) objectInputStream.readObject();
            objectInputStream.close();
            return walletList;
        } catch (IOException |ClassNotFoundException exception) {
            exception.printStackTrace();
            throw new RetrieveDataexception("Error on retrieving walletList Data from File"
                   + exception.getMessage());
        }
    }
}
