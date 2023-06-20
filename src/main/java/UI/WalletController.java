package UI;
import Exceptions.GetCurrentPriceException;
import com.mycompany.sample.WalletApp;
import com.mycompany.sample.domain.CurrentPriceForCurrency;
import com.mycompany.sample.domain.Transaction;
import com.mycompany.sample.domain.Wallet;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;

import java.math.BigDecimal;
import java.math.RoundingMode;


public class WalletController {

    @FXML
    private Button btnBackToMain;

    @FXML
    private Label lblId, lblName, lblCurrency, lblAmount, lblFee,lblValue,lblTransactions;

    @FXML
    private TableView<Transaction> tblTransactions;


    private Wallet wallet;
    public void initialize()
        {
            this.wallet =(Wallet) GlobalContext.getGlobalContext().getStateFor(WalletApp.GLOBAL_SELECTED_WALLET);

            refreshAllGUIValues();

             btnBackToMain.setOnAction((ActionEvent e ) ->
            {
                WalletApp.switchScene("main.fxml","com.mycompany.sample.main");
            });

        }

        private CurrentPriceForCurrency getCurrentPriceStrategy()
        {
           return (CurrentPriceForCurrency) GlobalContext.getGlobalContext().getStateFor(WalletApp.GLOBAL_CURRENT_CURRENCY_PRICES);

        }

        public void refreshAllGUIValues()
        {
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
        }


        public void buy()
        {
            System.out.println("KAUFEN");
        }

        public  void sell()
        {
            System.out.println("VERKAUFEN");
        }




 }
