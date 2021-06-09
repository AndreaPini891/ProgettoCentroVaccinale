package centrivaccinali.controllers;

import centrivaccinali.web.ServerJSONHandler;
import centrivaccinali.web.WebMethods;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class DialogController implements Initializable {

    @FXML
    public TextField descriptionText;

    @FXML
    public ChoiceBox<String> vaccineSelect;

    @FXML
    public Button cancelButton, sendButton;

    public int idCittadino;
    public String nomeCentro;

    /**
     * Server connector
     */
    ServerJSONHandler s;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        s = new ServerJSONHandler();

        ObservableList<String> choices = FXCollections.observableArrayList();
        choices.addAll("Pfizer", "J&J", "AstraZeneca", "Moderna");

        vaccineSelect.setItems(choices);
        vaccineSelect.getSelectionModel().selectFirst();

        cancelButton.setOnAction(event -> {
            Stage stage = (Stage) cancelButton.getScene().getWindow();
            stage.close();
        });

        sendButton.setOnAction(event -> {
            JSONObject json = new JSONObject();

            json.put("idCittadino", idCittadino);
            json.put("tipoVaccinazione", vaccineSelect.getValue());

            try {
                s.setMethod(WebMethods.POST).setEndpoint("Vaccinati_"+nomeCentro).setData(json).makeRequest();
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }

            Stage stage = (Stage) sendButton.getScene().getWindow();
            stage.close();
        });
    }

    public void setDescriptionText(String text){
        descriptionText.setText(text);
    }

}
