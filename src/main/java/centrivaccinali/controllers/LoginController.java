package centrivaccinali.controllers;

/*
 * Login controller for the Login view.
 *
 *       @author SEDE COMO
 *       @author Samuele Barella - mat.740688
 *       @author Lorenzo Pengue - mat.740727
 *       @author Andrea Pini - mat.740675
 *
 */

import centrivaccinali.web.ServerJSONHandler;
import centrivaccinali.web.ServerStatusException;
import centrivaccinali.web.WebMethods;
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
 * Controller for the "login" view.
 */
public class LoginController implements Initializable {

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
     * Label to display login errors or successes.
     */
    @FXML
    public Label errorLabel;

    /**
     * Button to log in.
     */
    @FXML
    public Button loginButton;

    /**
     * Button that opens the registration view to register a new user
     */
    @FXML
    public Button registerButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }


    /**
     * Login function that takes username and password for the authentication
     *
     * @param s        the s = server connector
     * @param username the username
     * @param password the password
     * @return the boolean
     */
    public boolean login(ServerJSONHandler s, String username, String password) {

        boolean returnVal = false;

        try {
            JSONObject jsonobj = new JSONObject();

            jsonobj.put("userName", username);
            jsonobj.put("password", password);

            CompletableFuture<JSONArray> json = s
                    .setMethod(WebMethods.POST)
                    .setEndpoint("rpc/login_admin_centro")
                    .setData(jsonobj)
                    .makeRequest();

            String token = (String) json.join().getJSONObject(0).get("token");

            if(token.length() > 0){

                s.setJWT(token);

                returnVal = true;

            }

        } catch (IOException | InterruptedException | ServerStatusException e) {
            e.printStackTrace();
        } finally {
            return returnVal;
        }


    }

}
