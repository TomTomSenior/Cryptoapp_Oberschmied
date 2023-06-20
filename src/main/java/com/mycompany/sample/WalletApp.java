package com.mycompany.sample;

import Exceptions.RetrieveDataexception;
import Exceptions.SaveDataException;
import Infrastruktur.CurrentCurrencyPrices;
import Infrastruktur.FileDataStore;
import UI.GlobalContext;
import com.mycompany.sample.domain.BankAccount;
import com.mycompany.sample.domain.DataStore;
import com.mycompany.sample.domain.WalletList;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ResourceBundle;


public class WalletApp extends Application {


    // UI Parts
    private static Stage mainStage;
    public static final String GLOBAL_WALLET_LIST = "walletlist";
    public static final String GLOBAL_BANK_ACCOUNT = "bankaccount";
    public static final String GLOBAL_CURRENT_CURRENCY_PRICES = "currencyprices";
    public static String GLOBAL_SELECTED_WALLET= "selectedWallet";

    public static void switchScene(String fxmlFile, String resourceBundle) {
        try {
            Parent root = FXMLLoader.load(WalletApp.class.getResource(fxmlFile), ResourceBundle.getBundle(resourceBundle));
            Scene scene = new Scene(root);
            mainStage.setScene(scene);
            mainStage.show();
        } catch (Exception ioException) {
            WalletApp.showErrorDialog("Could not load new scene!");
            ioException.printStackTrace();
        }
    }

    public static void showErrorDialog(String message) {

        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText("An exception occurred: " + message);
        alert.showAndWait();

    }


            //File-Handling -Parts

    private BankAccount loadBankAccountFromFile()throws RetrieveDataexception{
        DataStore dataStore = new FileDataStore();
        BankAccount bankAccount = dataStore.retrieveBankAccount();
        System.out.println("Bankaccount loaded !");
        return bankAccount;
    }

    private  WalletList loadWalletListFromFile()throws RetrieveDataexception{
        DataStore dataStore = new FileDataStore();
        WalletList walletList = dataStore.retrieveWalletList();
        System.out.println("Walletlist loaded !");
        return walletList;
    }

    private void storeBankAccountToFile(BankAccount bankAccount) throws SaveDataException{
    DataStore dataStore = new FileDataStore();
    dataStore.saveBankAccount(bankAccount);
    }

    private void storeWalletListToFile(WalletList walletList)throws SaveDataException{
        DataStore dataStore = new FileDataStore();
        dataStore.saveWalletList(walletList);
    }

    @Override
    public void start(Stage stage) throws IOException {

        mainStage = stage;
        WalletApp.switchScene("main.fxml", "com.mycompany.sample.main");
        BankAccount bankAccount = new BankAccount();
        WalletList walletList = new WalletList();

        try {
            bankAccount = loadBankAccountFromFile();
        } catch (RetrieveDataexception e) {
            WalletApp.showErrorDialog("Error on loading Bankaccount data. Using empty account");
            e.printStackTrace();
        }

        try {
            walletList = loadWalletListFromFile();
        } catch (RetrieveDataexception e) {
            WalletApp.showErrorDialog("Error on loading Walletlist data. Using empty Walletlist");
            e.printStackTrace();
        }

       // Fill GlobalContext
        GlobalContext.getGlobalContext().putStateFor(WalletApp.GLOBAL_WALLET_LIST,walletList);
        GlobalContext.getGlobalContext().putStateFor(WalletApp.GLOBAL_BANK_ACCOUNT,bankAccount);
        GlobalContext.getGlobalContext().putStateFor(GLOBAL_CURRENT_CURRENCY_PRICES,new CurrentCurrencyPrices());

        WalletApp.switchScene("main.fxml", "com.mycompany.sample.main");
    }

    @Override
    public void stop()
    {
        WalletList walletList = (WalletList) GlobalContext.getGlobalContext().getStateFor(WalletApp.GLOBAL_WALLET_LIST);
        BankAccount bankAccount = (BankAccount) GlobalContext.getGlobalContext().getStateFor(WalletApp.GLOBAL_BANK_ACCOUNT);


        try {
            storeBankAccountToFile(bankAccount);
            System.out.println("BankAccount details stored to file !");
            storeWalletListToFile(walletList);
            System.out.println("Walletlist stored to file !");
        } catch (SaveDataException e) {
            WalletApp.showErrorDialog("Could not store bankAccount and / or wallet details !");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

}
