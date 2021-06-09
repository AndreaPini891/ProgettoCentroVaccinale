package centrivaccinali.models;

import org.joda.time.DateTime;
import org.json.JSONObject;

import java.text.SimpleDateFormat;

/**
 * @author SEDE COMO
 * @author Samuele Barella - mat.740688
 * @author Lorenzo Pengue - mat.740727
 * @author Andrea Pini - mat.740675
 */

public class EventoAvversoModel {
    public int idEvento;
    public String descrizione;
    public int gravita;
    public String note;
    public int idVaccinazione;
    public int idCentro;
    public DateTime inseritoIl;

    /**
     * model from json
     * @param json the json object
     */
    public EventoAvversoModel(JSONObject json){
        this(
                json.getInt("idEvento"),
                json.getString("descrizione"),
                json.getInt("gravita"),
                json.getString("note"),
                json.getInt("idVaccinazione"),
                json.getInt("idCentro"),
                json.getString("inseritoIl")
        );
    }

    public EventoAvversoModel(int idEvento, String descrizione, int gravita, String note, int idVaccinazione, int idCentro, String inseritoIl){

        this.idEvento = idEvento;
        this.descrizione = descrizione;
        this.gravita = gravita;
        this.note = note;
        this.idVaccinazione = idVaccinazione;
        this.idCentro = idCentro;
        this.inseritoIl = new DateTime(inseritoIl);

    }

    @Override
    public String toString() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        return sdf.format(inseritoIl.toDate()) + "; gravitá: "+ gravita +"; descrizione: " + descrizione;
    }
}
