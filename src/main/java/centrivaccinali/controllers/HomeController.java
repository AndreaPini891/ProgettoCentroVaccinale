package centrivaccinali.controllers;

import centrivaccinali.App;
import centrivaccinali.models.*;
import centrivaccinali.web.ServerJSONHandler;
import centrivaccinali.web.WebMethods;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Home controller for the "home" view.
 *
 * @author SEDE COMO
 * @author Samuele Barella - mat.740688
 * @author Lorenzo Pengue - mat.740727
 * @author Andrea Pini - mat.740675
 */
public class HomeController implements Initializable {

    /**
     * Server connector
     */
    ServerJSONHandler s;

    /**
     * the list of vaccinated citizens
     */
    @FXML
    private ListView<VaccinazioneModel> vaccinationList;

    /**
     * the list of events regarding this centre
     */
    @FXML
    private ListView<EventoAvversoModel> eventsList;

    /**
     * the list of registered citizens
     */
    @FXML
    private ListView<CittadinoModel> citizensList;

    /**
     * the labels for the centre admin
     */
    @FXML
    private Label nameLabel, surnameLabel, centerLabel;

    /**
     * the labels for the centre informations
     */
    @FXML
    private Label centerNameLabel, typeLabel, addressLabel, eventsLabel, avgEventLabel;

    /**
     * the labels for the current selected citizen
     */
    @FXML
    private Label citizenNameLabel, citizenSurnameLabel, cfLabel, invalidCFLabel;

    /**
     * the button to mark a citizen as vaccinated
     */
    @FXML
    private Button regButton;

    /**
     * the button to select a citizen
     */
    @FXML
    private Button searchCitizenButton;

    /**
     * the textfield to insert data about the queried citizen
     */
    @FXML
    private TextField searchCitizenText;

    AdminCentroModel adminCentroModel;
    CentroVaccinaleModel currentCenter;
    CittadinoModel currentCitizen;

    /**
     * the observable lists for ListViews
     */
    ObservableList<VaccinazioneModel> vaccinationsObservable = FXCollections.observableArrayList();
    ObservableList<EventoAvversoModel> eventsObservable = FXCollections.observableArrayList();
    ObservableList<CittadinoModel> citizensObservable = FXCollections.observableArrayList();


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        setupDynamicElements();

    }

    /**
     * the function to setup dymanic elements that need remote data
     */
    private void setupDynamicElements(){
        //dynamic ui elements

        s = new ServerJSONHandler();

        compileUI();

        addressLabel.setText(currentCenter.indirizzo.toString());
        new Thread(() -> {
            CompletableFuture<JSONArray> jsonStats = null;
            try {
                jsonStats = s
                        .setEndpoint("EventiAvversi?idCentro=eq."+ currentCenter.idCentro)
                        .setMethod(WebMethods.GET)
                        .setData(new JSONObject())
                        .makeRequest();
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }

            assert jsonStats != null;
            JSONArray j = jsonStats.join();

            Platform.runLater(() -> {

                eventsLabel.setText(String.valueOf(j.length()));
                float avg = 0;
                for(int k = 0; k < j.length(); k++)
                    avg += j.getJSONObject(k).getInt("gravita");
                avg = avg / j.length();
                avgEventLabel.setText(String.valueOf(avg));

            });
        }).start();

        searchCitizenButton.setOnAction(event -> searchCitizen());

        regButton.setOnAction(event -> registerVaccination());

        //aggiorno le liste ogni 60 sec
        ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor();
        exec.scheduleAtFixedRate(new Thread(this::fillRegisteredList), 0, 60, TimeUnit.SECONDS);
        exec.scheduleAtFixedRate(new Thread(this::fillEventsList), 0, 60, TimeUnit.SECONDS);

    }

    /**
     * the function used to compile fetched data into the ui
     */
    private void compileUI(){
        citizensList.setItems(citizensObservable);
        vaccinationList.setItems(vaccinationsObservable);
        eventsList.setItems(eventsObservable);

        CompletableFuture<JSONArray> adminJson = null;
        try {
            adminJson = s.setEndpoint("AdminCentro").setMethod(WebMethods.GET).makeRequest();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        assert adminJson != null;
        JSONArray adminJsonArray = adminJson.join();

        if (adminJsonArray.length() > 0) {
            adminCentroModel = new AdminCentroModel(adminJsonArray.getJSONObject(0));

            nameLabel.setText(adminCentroModel.nome);
            surnameLabel.setText(adminCentroModel.cognome);
            centerLabel.setText(adminCentroModel.nomeCentro);

        }

        CompletableFuture<JSONArray> centerJson;
        CompletableFuture<JSONArray> addressJson = null;
        try {
            centerJson = s.setEndpoint("CentriVaccinali?nomeCentro=eq."+adminCentroModel.nomeCentro).setMethod(WebMethods.GET).makeRequest();

            assert centerJson != null;
            if(!centerJson.join().isEmpty())
                currentCenter = new CentroVaccinaleModel(centerJson.join().getJSONObject(0));

            addressJson = s.setEndpoint("Indirizzi?idIndirizzo=eq."+currentCenter.idIndirizzo).setMethod(WebMethods.GET).makeRequest();

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        assert addressJson != null;
        if(!addressJson.join().isEmpty())
            currentCenter.setAddress(new IndirizzoModel(addressJson.join().getJSONObject(0)));

        centerNameLabel.setText(currentCenter.nomeCentro);
        typeLabel.setText(currentCenter.tipologiaCentro);
    }

    /* listeners */
    /**
     * listener to the search button
     */
    private void searchCitizen(){
        String cfString = searchCitizenText.getText();

        new Thread(()-> {

            CompletableFuture<JSONArray> citizenJson = null;
            try {
                citizenJson = s.setEndpoint("Cittadini?codiceFiscale=fts."+cfString).setMethod(WebMethods.GET).makeRequest();
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }

            assert citizenJson != null;
            JSONArray citizen = citizenJson.join();

            if(citizen.length() > 0){
                currentCitizen = new CittadinoModel(citizen.getJSONObject(0));
                invalidCFLabel.setVisible(false);
                //previene problemi di Not on FX application thread
                Platform.runLater(() -> {

                    citizenNameLabel.setText(currentCitizen.nome);
                    citizenSurnameLabel.setText(currentCitizen.cognome);
                    cfLabel.setText(currentCitizen.codiceFiscale);

                    regButton.setVisible(true);
                });

                fillVaccinationsList();

            } else{
                invalidCFLabel.setVisible(true);
                regButton.setVisible(false);
            }


        }).start();

    }

    /**
     * listener to the button to register a user as vaccinated
     */
    private void registerVaccination(){

        Stage dialog = new Stage();
        Parent root = null;
        FXMLLoader loader = new FXMLLoader(App.class.getResource( "dialog.fxml" ));
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        DialogController d = loader.getController();
        d.idCittadino = currentCitizen.idCittadino;
        d.nomeCentro = currentCenter.nomeCentro;
        d.setDescriptionText(currentCitizen.codiceFiscale);

        assert root != null;
        dialog.setScene(new Scene(root));
        dialog.initOwner(regButton.getContextMenu());
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.showAndWait();
        fillVaccinationsList();
    }

    /* utils */

    /**
     * fills the vaccinations for the current citizen
     */
    private void fillVaccinationsList(){
        new Thread(() -> {
            CompletableFuture<JSONArray> j = null;
            try {
                j = s.setMethod(WebMethods.GET).setEndpoint("Vaccinati_"+currentCenter.nomeCentro+"?idCittadino=eq."+currentCitizen.idCittadino).makeRequest();
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
            assert j != null;
            JSONArray jsonArray = j.join();

            //previene problemi di Not on FX application thread
            Platform.runLater(() -> {
                vaccinationsObservable.clear();

                if(jsonArray != null && jsonArray.length() > 0){
                    for(int i = 0; i < jsonArray.length(); i++){
                        JSONObject json = jsonArray.getJSONObject(i);
                        VaccinazioneModel tmp = new VaccinazioneModel(json);
                        vaccinationsObservable.add(tmp);
                    }
                }
            });

        }).start();
    }

    /**
     * fills the registered citizens list
     */
    private void fillRegisteredList(){
        new Thread(() -> {
            CompletableFuture<JSONArray> j = null;
            try {
                j = s.setMethod(WebMethods.GET).setEndpoint("RegistrazioniCentriVaccinali?idCentro=eq."+currentCenter.idCentro).makeRequest();
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
            assert j != null;
            JSONArray jsonArray = j.join();

            StringBuilder endpointBuilder = new StringBuilder();
            endpointBuilder.append("Cittadini?or=(");

            for(int i = 0; i < jsonArray.length(); i++){
                endpointBuilder.append("idCittadino.eq.").append(jsonArray.getJSONObject(i).getInt("idCittadino"));
                if(!(i == jsonArray.length()-1))
                    endpointBuilder.append(",");
            }

            endpointBuilder.append(")");

            CompletableFuture<JSONArray> citizensJson = null;
            try {
                citizensJson = s.setMethod(WebMethods.GET).setEndpoint(endpointBuilder.toString()).makeRequest();
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
            assert citizensJson != null;
            JSONArray citizensArray = citizensJson.join();

            //previene problemi di Not on FX application thread
            Platform.runLater(() -> {
                citizensObservable.clear();

                if(citizensArray != null && citizensArray.length() > 0){
                    for(int i = 0; i < citizensArray.length(); i++){
                        JSONObject json = citizensArray.getJSONObject(i);
                        CittadinoModel tmp = new CittadinoModel(json);
                        citizensObservable.add(tmp);
                    }
                }
            });

        }).start();
    }

    /**
     * fills the events list
     */
    private void fillEventsList(){
        new Thread(() -> {
            CompletableFuture<JSONArray> j = null;
            try {
                j = s.setMethod(WebMethods.GET).setEndpoint("EventiAvversi?idCentro=eq."+currentCenter.idCentro).makeRequest();
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
            assert j != null;
            JSONArray jsonArray = j.join();

            //previene problemi di Not on FX application thread
            Platform.runLater(() -> {
                eventsObservable.clear();

                if (jsonArray != null && jsonArray.length() > 0) {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject json = jsonArray.getJSONObject(i);
                        EventoAvversoModel tmp = new EventoAvversoModel(json);
                        eventsObservable.add(tmp);
                    }
                }

                eventsList.setCellFactory(cell -> new ListCell<>() {

                    final Tooltip tooltip = new Tooltip();

                    @Override
                    protected void updateItem(EventoAvversoModel event, boolean empty) {
                        super.updateItem(event, empty);

                        if (event == null || empty) {
                            setText(null);
                            setTooltip(null);
                        } else {
                            // A book is to be listed in this cell
                            setText(event.toString());

                            // Let's show our Author when the user hovers the mouse cursor over this row
                            tooltip.setText(event.note);
                            setTooltip(tooltip);
                        }
                    }
                });
            });

        }).start();
    }
}
