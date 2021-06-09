package centrivaccinali.models;

import org.json.JSONObject;

/**
 * @author SEDE COMO
 * @author Samuele Barella - mat.740688
 * @author Lorenzo Pengue - mat.740727
 * @author Andrea Pini - mat.740675
 */

public class IndirizzoModel {
    public int idIndirizzo;
    public String nazione;
    public String regione;
    public String provincia;
    public String comune;
    public String via;

    /**
     * model from json
     * @param json the json object
     */
    public IndirizzoModel(JSONObject json)
    {
        this(
                json.getInt("idIndirizzo"),
                json.getString("nazione"),
                json.getString("regione"),
                json.getString("provincia"),
                json.getString("comune"),
                json.getString("via")
        );
    }

    public IndirizzoModel(int idIndirizzo, String nazione, String regione, String provincia, String comune, String via)
    {

        this.idIndirizzo = idIndirizzo;
        this.nazione = nazione;
        this.regione = regione;
        this.provincia = provincia;
        this.comune = comune;
        this.via = via;
    }

    @Override
    public String toString() {
        return regione + ", " + provincia + ", " + comune + ", " + via;
    }
}
