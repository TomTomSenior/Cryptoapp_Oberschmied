package com.mycompany.sample;

import Exceptions.*;
import Infrastruktur.CurrentCurrencyPrices;
import Infrastruktur.FileDataStore;
import com.mycompany.sample.domain.*;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.ResourceBundle;

import javafx.fxml.FXMLLoader;


public class WalletApp extends Application {


    // UI Parts
    private static Stage mainStage;

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


    @Override
    public void start(Stage stage) throws IOException {

        mainStage = stage;
        WalletApp.switchScene("main.fxml", "com.mycompany.sample.main");



        /*
        AnchorPane root = FXMLLoader.load(WalletApp.class.getResource("main.fxml"),
        ResourceBundle.getBundle("com.mycompany.sample.main"));

        Scene scene =new Scene(root,640,480);
        stage.setScene(scene);
        stage.show();*/

    }


    public static void main(String[] args) throws InvalidAmountException, InsufficientAmountException {

        System.out.println("Hallo Welt !");
        BankAccount ba = new BankAccount();
        ba.deposit(new BigDecimal(10000));

        System.out.println(ba);

        CryptoCurrency crypto = CryptoCurrency.BTC;
        System.out.println(crypto.getCurrencyName());
        System.out.println(crypto.getCode());
        System.out.println(CryptoCurrency.valueOfCode("BTC").getCurrencyName());

        Transaction transaction = new Transaction(CryptoCurrency.ETH,
                new BigDecimal("1.23"),
                new BigDecimal("1567.8")
        );

        System.out.println(transaction);

        Wallet wallet = null;
        try {
            wallet = new Wallet("My BTC Wallet", CryptoCurrency.BTC, new BigDecimal("1"));
        } catch (InvalidFeeException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();

        }


        try {
            wallet.buy(new BigDecimal("10"), new BigDecimal("5"), ba);
        } catch (InvalidAmountException e) {
            e.printStackTrace();
        } catch (InsufficientBalanceException insufficientBalanceException) {
            insufficientBalanceException.printStackTrace();
        }

        System.out.println(ba);
        System.out.println(wallet);

        try {
            wallet.sell(new BigDecimal(10), new BigDecimal(5), ba);
        } catch (InsufficientAmountException e) {
            e.printStackTrace();
        } catch (InvalidAmountException e) {
            e.printStackTrace();
        }

        System.out.println(ba);
        System.out.println(wallet);

        WalletList walletList = new WalletList();
        walletList.addWallet(wallet);

        System.out.println(walletList);

        CurrentPriceForCurrency currentPrices = new CurrentCurrencyPrices();


        try {
            BigDecimal result = currentPrices.getCurrentprice(CryptoCurrency.ETH);
        } catch (GetCurrentPriceException e) {
            e.printStackTrace();
        }

        DataStore dataStore = new FileDataStore();
        try {
            dataStore.saveBankAccount(ba);
        } catch (SaveDataException e) {
            e.printStackTrace(); // alternativ System.out.println(e.getmessage());
        }

        try {
            BankAccount bankAccount2 = dataStore.retrieveBankAccount();
            System.out.println(bankAccount2);
        } catch (RetrieveDataexception e) {
            e.printStackTrace();// alternativ System.out.println(e.getmessage());
        }

        try {
            dataStore.saveWalletList(walletList);
        } catch (SaveDataException e) {
            e.printStackTrace();
        }

        try {
            WalletList walletList2 = dataStore.retrieveWalletList();
        } catch (RetrieveDataexception e) {
            e.printStackTrace();
        }

        launch(args);
    }

}
