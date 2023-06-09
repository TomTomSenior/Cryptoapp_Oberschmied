package com.mycompany.sample;

import Exceptions.InsufficientAmountException;
import Exceptions.InsufficientBalanceException;
import Exceptions.InvalidAmountException;
import Exceptions.InvalidFeeException;
import com.gluonhq.attach.display.DisplayService;
import com.gluonhq.attach.util.Platform;
import com.gluonhq.charm.glisten.application.AppManager;
import com.gluonhq.charm.glisten.visual.Swatch;
import com.mycompany.sample.domain.Bankaccount;
import com.mycompany.sample.domain.CryptoCurrency;
import com.mycompany.sample.domain.Transaction;
import com.mycompany.sample.domain.Wallet;
import javafx.application.Application;
import javafx.geometry.Dimension2D;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.math.BigDecimal;

import static com.gluonhq.charm.glisten.application.AppManager.HOME_VIEW;

public class Main extends Application {

    public static final String MAIN_VIEW = HOME_VIEW;

    private final AppManager appManager = AppManager.initialize(this::postInit);

    @Override
    public void init() {
        appManager.addViewFactory(MAIN_VIEW, () -> new MainView().getView());

        DrawerManager.buildDrawer(appManager);
    }

    @Override
    public void start(Stage stage) {
        appManager.start(stage);
    }

    private void postInit(Scene scene) {
        Swatch.LIGHT_GREEN.assignTo(scene);
        scene.getStylesheets().add(Main.class.getResource("styles.css").toExternalForm());
        ((Stage) scene.getWindow()).getIcons().add(new Image(Main.class.getResourceAsStream("/icon.png")));

        if (Platform.isDesktop()) {
            Dimension2D dimension2D = DisplayService.create()
                    .map(DisplayService::getDefaultDimensions)
                    .orElse(new Dimension2D(640, 480));
            scene.getWindow().setWidth(dimension2D.getWidth());
            scene.getWindow().setHeight(dimension2D.getHeight());
        }
    }

    public static void main(String[] args) throws InvalidAmountException, InsufficientAmountException {

        System.out.println("Hallo Welt !");
        Bankaccount ba = new Bankaccount();
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


        launch(args);
    }

}
