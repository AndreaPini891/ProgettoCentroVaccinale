package centrivaccinali.controllers;
/*
 *       AUTORI - COMO:
 *       Samuele Barella - mat.740688
 *       Lorenzo Pengue - mat.740727
 *       Andrea Pini - mat.740675
 */

import centrivaccinali.web.ServerJSONHandler;
import centrivaccinali.web.WebMethods;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
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
     * Text field for entering the name of vthe user.
     */
    @FXML
    public TextField nameText;

    /**
     * Text field for entering the surname of the user.
     */
    @FXML
    public TextField surnameText;


    /**
     * Text field for entering the username of the user.
     */
    @FXML
    public TextField usernameText;

    /**
     * Text field for entering the password of the user.
     */
    @FXML
    public PasswordField passwordText;

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
     * Text field for entering region.
     */
    @FXML
    public TextField regionText;

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

    /**
     * the max amount of characters in province text box
     */
    private final int PROVINCE_LIMIT = 2;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        ObservableList<String> choices = FXCollections.observableArrayList();
        choices.addAll("hub", "ospedaliero","aziendale");

        checkType.setItems(choices);
        checkType.getSelectionModel().selectFirst();

        provinceText.lengthProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.intValue() > oldValue.intValue()) {
                // Check if the new character is greater than LIMIT
                if (provinceText.getText().length() >= PROVINCE_LIMIT) {

                    // if it's 11th character then just setText to previous
                    // one
                    provinceText.setText(provinceText.getText().substring(0, PROVINCE_LIMIT));
                }
            }
        });

        cnameText.addEventFilter(KeyEvent.KEY_TYPED , letter_Validation(25));

    }


    /**
     * Register function that takes user data for the registration
     *
     * @param s = server connector
     * @return the boolean
     */
    public boolean register(ServerJSONHandler s) {

        boolean returnVal = false;

        JSONObject jsonobj = new JSONObject();

        jsonobj.put("nome", nameText.getText());
        jsonobj.put("cognome", surnameText.getText());
        jsonobj.put("userName", usernameText.getText());
        jsonobj.put("pass", passwordText.getText());

        jsonobj.put("nomeCentro", cnameText.getText());

        jsonobj.put("nazione", "ITALIA");
        jsonobj.put("regione", regionText.getText());
        jsonobj.put("provincia", provinceText.getText());
        jsonobj.put("comune", cityText.getText());
        jsonobj.put("via", streetText.getText() + " " + ncText.getText());
        jsonobj.put("tipologiaCentro", checkType.getValue());

        try {
            CompletableFuture<JSONArray> json = s
                    .setMethod(WebMethods.POST)
                    .setEndpoint("rpc/signup_admin_centro")
                    .setData(jsonobj)
                    .makeRequest();

            json.join();

            returnVal = s.getResponseCode() == 200;

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return returnVal;

    }

    /**
     * function containing the filter for a text input
     * @param len the max amount of chars in the text input
     * @return the event handler
     */
    public EventHandler<KeyEvent> letter_Validation(final Integer len) {
        return e -> {
            TextField txt_TextField = (TextField) e.getSource();
            if (txt_TextField.getText().length() >= len) {
                e.consume();
            }
            if(!e.getCharacter().matches("[A-Za-z0-9_]")){
                e.consume();
            }
            if(e.getCharacter().matches("[ ]")){
                e.consume();
                int i = txt_TextField.getCaretPosition();
                txt_TextField.setText(txt_TextField.getText().substring(0, i) + '_' + txt_TextField.getText().substring(i));
                txt_TextField.positionCaret(i+1);
            }
        };
    }
}
