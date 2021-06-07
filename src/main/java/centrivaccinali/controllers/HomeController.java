package centrivaccinali.controllers;

import centrivaccinali.web.ServerJSONHandler;
import centrivaccinali.web.WebMethods;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.concurrent.CompletableFuture;

/**
 * Home controller for the "home" view.
 *
 * @author SEDE COMO
 * @author Samuele Barella - mat.740688
 * @author Lorenzo Pengue - mat.740727
 * @author Andrea Pini - mat.740675
 */
public class HomeController implements Initializable {
    private int centerID;

    private String name;
    private String surname;
    private String cf;

    private JSONObject selectedCentre;

    /**
     * Server connector
     */
    ServerJSONHandler s;

    @FXML
    private ListView<String> vaccineCenterList;

    @FXML
    private Label nameLabel, surnameLabel, cfLabel;

    @FXML
    private Label nameLabel2, typeLabel, addressLabel;

    @FXML
    private Button regButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {


        //dynamic ui elements

        s = new ServerJSONHandler();

        CompletableFuture<JSONArray> centersJson = null;
        try {
            centersJson = s.setEndpoint("CentriVaccinali").setMethod(WebMethods.GET).makeRequest();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        //observable list di stringhe perchè i nomi dei centri vaccinali sono univoci nel database, quindi è uno spreco inserire dei modelli di centro vaccinale

        ObservableList<String> obs = FXCollections.observableArrayList();
        vaccineCenterList.setItems(obs);

        assert centersJson != null;
        JSONArray centersJsonArray = centersJson.join();

        CompletableFuture<JSONArray> addressesJson = null;
        StringBuilder endpointBuilder = new StringBuilder("Indirizzi?or=(");

        for(int i = 0; i < centersJsonArray.length(); i++) {
            endpointBuilder.append("idIndirizzo.eq.").append(centersJsonArray.getJSONObject(i).getInt("idIndirizzo"));
            if(!(i == centersJsonArray.length()-1))
                endpointBuilder.append(",");
        }
        endpointBuilder.append(")");

        String endpoint = endpointBuilder.toString();
        System.out.println(endpoint);
        try {
            addressesJson = s.setEndpoint(endpoint).setMethod(WebMethods.GET).makeRequest();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        fillCentersList(obs, centersJsonArray, "");

        CompletableFuture<JSONArray> citizenJson = null;
        try {
            citizenJson = s.setEndpoint("Cittadini").makeRequest();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        assert citizenJson != null;
        JSONArray citizenJsonArray = citizenJson.join();

        if(citizenJson != null && citizenJsonArray.length() > 0) {
            JSONObject o = citizenJsonArray.getJSONObject(0);

            nameLabel.setText(o.getString("nome"));
            surnameLabel.setText(o.getString("cognome"));
            cfLabel.setText(o.getString("codiceFiscale"));

        }


        //  listener ricerca per nome

        CompletableFuture<JSONArray> finalCentersJson = centersJson;
        nameSearchText.textProperty().addListener((observable, oldValue, newValue) -> {
            cercaCentroVaccinale(obs, finalCentersJson, null, newValue, true);
        });

        // listener ricerca per comune e tipologia

        CompletableFuture<JSONArray> finalAddressesJson = addressesJson;
        citySearchText.textProperty().addListener((observable, oldValue, newValue) -> {
            cercaCentroVaccinale(obs, finalCentersJson, finalAddressesJson, newValue, false);
        });


        //listener su listview

        vaccineCenterList.getSelectionModel().selectedItemProperty()
                .addListener(new ChangeListener<String>() {
                    public void changed(ObservableValue<? extends String> observable,
                                        String oldValue, String newValue) {
                        System.out.println("selection changed " + newValue);
                        JSONArray centreList = finalCentersJson.join();
                        JSONArray addressesList = finalAddressesJson.join();
                        for (int i = 0; i < centreList.length(); i++){
                            if (centreList.getJSONObject(i).getString("nomeCentro").toLowerCase(Locale.ROOT).equals(newValue.toLowerCase(Locale.ROOT))) {
                                selectedCentre = centreList.getJSONObject(i);

                                for (int j = 0; j < addressesList.length(); j++)
                                    if(selectedCentre.getInt("idIndirizzo") == addressesList.getJSONObject(j).getInt("idIndirizzo"))
                                        selectedCentre.put("jsonIndirizzo", addressesList.getJSONObject(j));

                                nameLabel2.setText(selectedCentre.getString("nomeCentro"));
                                typeLabel.setText(selectedCentre.getString("tipologiaCentro"));

                                addressLabel.setText(selectedCentre.getJSONObject("jsonIndirizzo").getString("regione") + ", " +
                                        selectedCentre.getJSONObject("jsonIndirizzo").getString("provincia") + ", " +
                                        selectedCentre.getJSONObject("jsonIndirizzo").getString("comune") + ", " +
                                        selectedCentre.getJSONObject("jsonIndirizzo").getString("via"));
                            }
                        }
                    }
                });
    }

    private void cercaCentroVaccinale(ObservableList<String> obs,
                                      CompletableFuture<JSONArray> futureJson,
                                      CompletableFuture<JSONArray> addressesJson,
                                      String filter,
                                      boolean nameOrCity)
    {
        JSONArray centersJson = futureJson.join();
        if(nameOrCity) {

            vaccineCenterList.getItems().clear();
            fillCentersList(obs, centersJson, filter);
        }
        else
        {

            JSONArray addressesArray = addressesJson.join();

            vaccineCenterList.getItems().clear();
            String tipologia = checkType.getValue();
            JSONArray filteredCentersJson = new JSONArray();
            JSONArray finalCentersJson = new JSONArray();

            //filtro per tipologia
            for (int i = 0; i < centersJson.length(); i++)
                if(centersJson.getJSONObject(i).getString("tipologiaCentro").toLowerCase(Locale.ROOT).equals(tipologia.toLowerCase()))
                    filteredCentersJson.put(centersJson.getJSONObject(i));

            //filtro per comune
            for (int i = 0; i < filteredCentersJson.length(); i++){
                int idIndirizzo = centersJson.getJSONObject(i).getInt("idIndirizzo");
                for (int j = 0; j < addressesArray.length(); j++){
                    if(addressesArray.getJSONObject(j).getInt("idIndirizzo") == idIndirizzo)
                        if(addressesArray.getJSONObject(j).getString("comune")
                                .toLowerCase(Locale.ROOT)
                                .contains(filter.toLowerCase(Locale.ROOT)))
                            finalCentersJson.put(centersJson.getJSONObject(i));
                }
            }

            fillCentersList(obs, finalCentersJson, "");
        }
    }

    private void fillCentersList( ObservableList<String> obs, JSONArray centersJson, String filter)
    {
        obs.removeAll();
        if(centersJson != null && centersJson.length() > 0){
            for(int i = 0; i < centersJson.length(); i++){
                JSONObject tmp = centersJson.getJSONObject(i);
                if(filter.length() == 0)
                    obs.add(tmp.getString("nomeCentro"));
                else {
                    if (tmp.getString("nomeCentro").toLowerCase(Locale.ROOT).contains(filter.toLowerCase(Locale.ROOT)))
                        obs.add(tmp.getString("nomeCentro"));
                }
            }
        }
    }
}
