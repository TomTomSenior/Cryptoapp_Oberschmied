package UI;

import Exceptions.InsufficientBalanceException;
import Exceptions.InvalidFeeException;
import com.mycompany.sample.WalletApp;
import com.mycompany.sample.domain.CryptoCurrency;
import com.mycompany.sample.domain.Wallet;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.math.BigDecimal;
import java.util.Optional;

public class MainController extends BaseControllerState {

    @FXML
    private Button btnClose;

    @FXML
    private ComboBox cmbWalletCurrency;

    @FXML
    private Label lblBankaccountBalance;

    @FXML
    private TableView<Wallet> tableView;

    public void initialize() {

        this.cmbWalletCurrency.getItems().addAll(CryptoCurrency.getCodes());

        this.lblBankaccountBalance.textProperty().setValue(getBankAccount().getBalance().toString());

        TableColumn<Wallet, String> symbol = new TableColumn<>("SYMBOL");
        symbol.setCellValueFactory(new PropertyValueFactory<>("cryptoCurrency"));

        TableColumn<Wallet, String> currencyName = new TableColumn<>("CURRENCY NAME");
        currencyName.setCellValueFactory(new PropertyValueFactory<>("currencyName"));

        TableColumn<Wallet, String> name = new TableColumn<>("WALLET NAME");
        name.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Wallet, String> amount = new TableColumn<>("AMOUNT");
        amount.setCellValueFactory(new PropertyValueFactory<>("amount"));

        tableView.getColumns().clear();
        tableView.getColumns().add(name);
        tableView.getColumns().add(symbol);
        tableView.getColumns().add(currencyName);
        tableView.getColumns().add(amount);

        tableView.getItems().setAll(getWalletList().getWalletsAsObservableList());

    }

    public void deposit() {
        TextInputDialog dialog = new TextInputDialog("Insert Amount to deposit....");
        dialog.setTitle("deposit to bankaccount");
        dialog.setHeaderText("How much money do you want to deposit ?");
        dialog.setContentText("Amount: ");
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            try {
                BigDecimal amount = new BigDecimal(result.get());
                this.getBankAccount().deposit(amount);
                this.lblBankaccountBalance.textProperty().set(this.getBankAccount().getBalance().toString());

            } catch (NumberFormatException numberFormatException) {
                WalletApp.showErrorDialog("Please insert a Number !");
            }
        }
    }

    public void withdraw() {
        TextInputDialog dialog = new TextInputDialog("Insert Amount to withdraw....");
        dialog.setTitle("Withdraw from bankaccount");
        dialog.setHeaderText("How much money do you want to withdraw ?");
        dialog.setContentText("Amount: ");
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            try {
                BigDecimal amount = new BigDecimal(result.get());
                this.getBankAccount().withdraw(amount);
                this.lblBankaccountBalance.textProperty().set(this.getBankAccount().getBalance().toString());

            } catch (NumberFormatException numberFormatException) {
                WalletApp.showErrorDialog("Please insert a Number !");
            } catch (InsufficientBalanceException insufficientBalanceException) {
                WalletApp.showErrorDialog(insufficientBalanceException.getMessage());
            }
        }
    }

    public void openWallet() {
        Wallet wallet = this.tableView.getSelectionModel().getSelectedItem();
        if (wallet != null) {
            GlobalContext.getGlobalContext().putStateFor(WalletApp.GLOBAL_SELECTED_WALLET, wallet);
            WalletApp.switchScene("wallet.fxml", "com.mycompany.sample.wallet");
        } else {
            WalletApp.showErrorDialog("You have to select a Wallet first !");
        }
    }

    public void newWallet() throws InvalidFeeException {
        Object selectedItem = this.cmbWalletCurrency.getSelectionModel().getSelectedItem();
        if (selectedItem == null) {
            WalletApp.showErrorDialog("choose currency");
            return;
        }
        CryptoCurrency selectedCryptoCurrency = CryptoCurrency.valueOf(this.cmbWalletCurrency.getSelectionModel().getSelectedItem().toString());
        this.getWalletList().addWallet(new Wallet("My" + selectedCryptoCurrency.currencyName + "Wallet", selectedCryptoCurrency, new BigDecimal("1")));
        tableView.getItems().setAll(this.getWalletList().getWalletsAsObservableList());
    }
}
