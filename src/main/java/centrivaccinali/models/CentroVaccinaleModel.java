package centrivaccinali.models;

import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;
import org.json.JSONObject;

/**
 * @author SEDE COMO
 * @author Samuele Barella - mat.740688
 * @author Lorenzo Pengue - mat.740727
 * @author Andrea Pini - mat.740675
 */

public class CentroVaccinaleModel {
    public int idCentro;
    public String nomeCentro;
    public int idIndirizzo;
    public IndirizzoModel indirizzo;
    public String tipologiaCentro;
    public DateTime inseritoIl;

    /**
     * model from json
     * @param json the json object
     */
    public CentroVaccinaleModel(JSONObject json){
        this(
            json.getInt("idCentro"),
            json.getString("nomeCentro"),
            json.getInt("idIndirizzo"),
            json.getString("tipologiaCentro"),
            json.isNull("inseritoIl") ? ISODateTimeFormat.dateTimeParser().parseDateTime("") : ISODateTimeFormat.dateTimeParser().parseDateTime(json.getString("inseritoIl"))
        );
    }
    public CentroVaccinaleModel(int idCentro, String nomeCentro, int idIndirizzo, String tipologiaCentro, DateTime inseritoIl){

        this.idCentro = idCentro;
        this.nomeCentro = nomeCentro;
        this.idIndirizzo = idIndirizzo;
        this.tipologiaCentro = tipologiaCentro;
        this.inseritoIl = inseritoIl;
    }

    public void setAddress(IndirizzoModel indirizzo){
        this.indirizzo = indirizzo;
    }

    @Override
    public String toString() {
        return nomeCentro;
    }
}
