package centrivaccinali.controllers;
/**
 *       AUTORI - COMO:
 *       Samuele Barella - mat.740688
 *       Lorenzo Pengue - mat.740727
 *       Andrea Pini - mat.740675
 */

import centrivaccinali.web.ServerJSONHandler;
import centrivaccinali.web.WebMethods;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
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
     * Text field for entering the name of vaccination center.
     */
    @FXML
    public TextField nameText;

    /**
     * Text field for entering street.
     */
    @FXML
    public TextField streetText;

    /**
     * Text field for entering the name of vaccination center.
     */
    @FXML
    public TextField cnameText;

    /**
     * Text field for entering civic number.
     */
    @FXML
    public TextField ncText;

    /**
     * Text field for entering city.
     */
    @FXML
    public TextField cityText;

    /**
     * Text field for entering province.
     */
    @FXML
    public TextField provinceText;

    /**
     * Text field for entering cap.
     */
    @FXML
    public TextField capText;

    /**
     * Button to register a new vaccination center.
     */
    @FXML
    public Button registrationButton;

    /**
     * Button to return to the login view.
     */
    @FXML
    public Button backButton;

    /**
     * choice box to select the type of vaccination center
     */
    @FXML
    public ChoiceBox<String> checkType;

    /**
     * Label to display registration errors or successes.
     */
    @FXML
    public Label error;

    private boolean validation;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        ObservableList<String> choices = FXCollections.observableArrayList();
        choices.addAll("hub", "ospedaliero","aziendale");

        checkType.setItems(choices);
        checkType.getSelectionModel().selectFirst();

       /* validation = false;
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

        }); */
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
        jsonobj.put("via", streetText.getText().toString());
        jsonobj.put("nomecentro", cnameText.getText().toString());
        jsonobj.put("numeroCivico", ncText.getText().toString());
        jsonobj.put("comune", cityText.getText().toString());
        jsonobj.put("provincia", provinceText.getText().toString());
        jsonobj.put("cap", capText.getText().toString());
        jsonobj.put("tipologia", checkType.getValue().toString());

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
