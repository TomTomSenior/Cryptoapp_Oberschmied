package UI;

import Exceptions.GetCurrentPriceException;
import Exceptions.InsufficientAmountException;
import Exceptions.InsufficientBalanceException;
import Exceptions.InvalidAmountException;
import com.mycompany.sample.WalletApp;
import com.mycompany.sample.domain.CurrentPriceForCurrency;
import com.mycompany.sample.domain.Transaction;
import com.mycompany.sample.domain.Wallet;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;


public class WalletController extends BaseControllerState {

    @FXML
    private Button btnBackToMain;

    @FXML
    private Label lblId, lblName, lblCurrency, lblAmount, lblFee, lblValue, lblTransactions;

    @FXML
    private TableView<Transaction> tblTransactions;


    private Wallet wallet;

    public void initialize() {
        this.wallet = (Wallet) GlobalContext.getGlobalContext().getStateFor(WalletApp.GLOBAL_SELECTED_WALLET);

        populateTable();

        refreshAllGUIValues();

        btnBackToMain.setOnAction((ActionEvent e) ->
        {
            WalletApp.switchScene("main.fxml", "com.mycompany.sample.main");
        });

    }

    private CurrentPriceForCurrency getCurrentPriceStrategy() {
        return (CurrentPriceForCurrency) GlobalContext.getGlobalContext().getStateFor(WalletApp.GLOBAL_CURRENT_CURRENCY_PRICES);

    }

    private void refreshAllGUIValues() {
        this.lblId.textProperty().setValue(this.wallet.getId().toString());
        this.lblName.textProperty().setValue(this.wallet.getName());
        this.lblCurrency.textProperty().setValue(wallet.getCryptoCurrency().getCode());
        this.lblAmount.textProperty().setValue(wallet.getAmount().toPlainString());
        this.lblFee.textProperty().setValue(wallet.getFeeInPercent().toString());


        try {
            BigDecimal currentPrice = this.getCurrentPriceStrategy().getCurrentPrice(wallet.getCryptoCurrency());
            BigDecimal currentValue = currentPrice.multiply(wallet.getAmount()).setScale(6, RoundingMode.HALF_UP);
            this.lblValue.textProperty().setValue(currentValue.toString());
        } catch (GetCurrentPriceException e) {
            WalletApp.showErrorDialog(e.getMessage());
            this.lblValue.textProperty().set("CURRENT PRICES UNAVAILABLE !");
            e.printStackTrace();
        }

        tblTransactions.getItems().setAll(wallet.getTransactions());
    }


    private void populateTable() {
        TableColumn<Transaction, String> id = new TableColumn<>("ID");
        id.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Transaction, String> crypto = new TableColumn<>("CRYPTO");
        crypto.setCellValueFactory(new PropertyValueFactory<>("cryptoCurrency"));

        TableColumn<Transaction, String> amount = new TableColumn<>("AMOUNT");
        amount.setCellValueFactory(new PropertyValueFactory<>("amount"));

        TableColumn<Transaction, String> total = new TableColumn<>("TOTAL");
        total.setCellValueFactory(new PropertyValueFactory<>("total"));

        TableColumn<Transaction, String> priceAtTransactionDate = new TableColumn<>("PRICE");
        priceAtTransactionDate.setCellValueFactory(new PropertyValueFactory<>("priceAtTransactionDate"));

        TableColumn<Transaction, String> date = new TableColumn<>("DATE");
        date.setCellValueFactory(new PropertyValueFactory<>("date"));


        tblTransactions.getColumns().clear();
        tblTransactions.getColumns().add(id);
        tblTransactions.getColumns().add(crypto);
        tblTransactions.getColumns().add(amount);
        tblTransactions.getColumns().add(total);
        tblTransactions.getColumns().add(priceAtTransactionDate);
        tblTransactions.getColumns().add(date);
    }

    public void buy() {
        TextInputDialog dialog = new TextInputDialog("Amount of crypto to buy ...");
        dialog.setTitle("Buy Crypto");
        dialog.setHeaderText("How much Crypto do you want to buy?");
        dialog.setContentText("Amount: ");
        Optional<String> result = dialog.showAndWait();


        if (result.isPresent()) {
            try {
                BigDecimal amount = new BigDecimal(result.get());
                this.wallet.buy(amount, this.getCurrentPriceStrategy()
                        .getCurrentPrice(wallet.getCryptoCurrency()), this.getBankAccount());
                this.refreshAllGUIValues();
            } catch (NumberFormatException numberFormatException) {
                WalletApp.showErrorDialog("Invalid Amount. insert a Number !");
            } catch (GetCurrentPriceException getCurrentPriceException) {
                WalletApp.showErrorDialog(getCurrentPriceException.getMessage());
                getCurrentPriceException.printStackTrace();
            } catch (InvalidAmountException invalidAmountException) {
                WalletApp.showErrorDialog(invalidAmountException.getMessage());
                invalidAmountException.printStackTrace();
            } catch (InsufficientBalanceException insufficientBalanceException) {
                WalletApp.showErrorDialog(insufficientBalanceException.getMessage());
                insufficientBalanceException.printStackTrace();
            }

        }
    }

    public void sell() {
        TextInputDialog dialog = new TextInputDialog("Amount of crypto to sell ...");
        dialog.setTitle("sell Crypto");
        dialog.setHeaderText("How much Crypto do you want to sell ?");
        dialog.setContentText("Amount: ");
        Optional<String> result = dialog.showAndWait();

        if (result.isPresent()) {
            try {
                BigDecimal amount = new BigDecimal(result.get());
                this.wallet.sell(amount, this.getCurrentPriceStrategy()
                        .getCurrentPrice(wallet.getCryptoCurrency()), this.getBankAccount());
                this.refreshAllGUIValues();

            } catch (GetCurrentPriceException | InvalidAmountException | InsufficientAmountException exception) {
                WalletApp.showErrorDialog(exception.getMessage());
               exception.printStackTrace();
            }

        }
    }
}