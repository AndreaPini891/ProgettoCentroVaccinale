package cittadini.controllers;
/**
 *       AUTORI - COMO:
 *       Samuele Barella - mat.740688
 *       Lorenzo Pengue - mat.740727
 *       Andrea Pini - mat.740675
 */

import cittadini.web.ServerJSONHandler;
import cittadini.web.WebMethods;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.CompletableFuture;

/**
 * Register controller for the "register" view.
 */
public class RegisterController implements Initializable {


    /**
     * Text field for entering name.
     */
    @FXML
    public TextField nameText;

    /**
     * Text field for entering surname.
     */
    @FXML
    public TextField surnameText;

    /**
     * Text field for entering fiscal code.
     */
    @FXML
    public TextField cfText;

    /**
     * Text field for entering username.
     */
    @FXML
    public TextField usernameText;

    /**
     * Text field for entering password.
     */
    @FXML
    public TextField passwordText;

    /**
     * Text field for entering nation.
     */
    @FXML
    public TextField nationText;

    /**
     * Text field for entering region.
     */
    @FXML
    public TextField regionText;

    /**
     * Text field for entering province.
     */
    @FXML
    public TextField provinceText;

    /**
     * Text field for entering city.
     */
    @FXML
    public TextField cityText;

    /**
     * Text field for entering street.
     */
    @FXML
    public TextField streetText;

    /**
     * Button to register a new user.
     */
    @FXML
    public Button registrationButton;

    /**
     * Button to return to the login view.
     */
    @FXML
    public Button backButton;

    /**
     * Label to display registration errors or successes.
     */
    @FXML
    public Label error;

    private boolean validation;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        validation = false;
        cfText.focusedProperty().addListener((arg0, oldValue, newValue) -> {
            if (!newValue) { //when focus lost
                if(!cfText.getText().matches("(?:[A-Z][AEIOU][AEIOUX]|[B-DF-HJ-NP-TV-Z]{2}[A-Z]){2}(?:[\\dLMNP-V]{2}(?:[A-EHLMPR-T](?:[04LQ][1-9MNP-V]|[15MR][\\dLMNP-V]|[26NS][0-8LMNP-U])|[DHPS][37PT][0L]|[ACELMRT][37PT][01LM]|[AC-EHLMPR-T][26NS][9V])|(?:[02468LNQSU][048LQU]|[13579MPRTV][26NS])B[26NS][9V])(?:[A-MZ][1-9MNP-V][\\dLMNP-V]{2}|[A-M][0L](?:[1-9MNP-V][\\dLMNP-V]|[0L][1-9MNP-V]))[A-Z]$")){
                    cfText.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;");
                    validation = false;
                } else {
                    cfText.setStyle("-fx-border-color: none ; -fx-border-width: none ;");
                    validation = true;
                }
            }

        });
    }


    /**
     * Register function that takes user data for the registration
     *
     * @param s = server connector
     * @return the boolean
     */
    public boolean register(ServerJSONHandler s) {

        boolean returnVal = false;

        if(!validation)
            return false;

        JSONObject jsonobj = new JSONObject();

        jsonobj.put("nome", nameText.getText().toString());
        jsonobj.put("cognome", surnameText.getText().toString());
        jsonobj.put("codiceFiscale", cfText.getText().toString());
        jsonobj.put("userName", usernameText.getText().toString());
        jsonobj.put("pass", passwordText.getText().toString());
        jsonobj.put("nazione", nationText.getText().toString());
        jsonobj.put("regione", regionText.getText().toString());
        jsonobj.put("provincia", provinceText.getText().toString());
        jsonobj.put("comune", cityText.getText().toString());
        jsonobj.put("via", streetText.getText().toString());

        try {
            CompletableFuture<JSONArray> json = s
                    .setMethod(WebMethods.POST)
                    .setEndpoint("rpc/signup_cittadino")
                    .setData(jsonobj)
                    .makeRequest();

            json.join();

            if(s.getResponseCode() == 200)
                returnVal = true;
            else returnVal = false;

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return returnVal;

    }

}
