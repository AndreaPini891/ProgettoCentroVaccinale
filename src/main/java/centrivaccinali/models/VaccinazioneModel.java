package centrivaccinali.models;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import org.joda.time.DateTime;

/**
 * @author SEDE COMO
 * @author Samuele Barella - mat.740688
 * @author Lorenzo Pengue - mat.740727
 * @author Andrea Pini - mat.740675
 */

public class VaccinazioneModel {

    public int idVaccinazione;
    public int idCittadino;
    public DateTime dataVaccinazione;
    public String tipoVaccinazione;

    /**
     * model from json
     * @param json the json object
     */
    public VaccinazioneModel(JSONObject json){
        this(
                json.getInt("idVaccinazione"),
                json.getInt("idCittadino"),
                json.getString("dataVaccinazione"),
                json.getString("tipoVaccinazione")
        );
    }

    public VaccinazioneModel(int idVaccinazione, int idCittadino, String dateString, String tipoVaccinazione){
        this.idCittadino = idCittadino;
        this.idVaccinazione = idVaccinazione;
        this.dataVaccinazione = new DateTime(dateString);
        this.tipoVaccinazione = tipoVaccinazione;
    }

    @Override
    public String toString(){
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        return sdf.format(dataVaccinazione.toDate()) + " - " + tipoVaccinazione;
    }

}
