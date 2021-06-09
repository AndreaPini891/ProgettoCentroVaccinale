package centrivaccinali.models;

import org.json.JSONObject;

/**
 * @author SEDE COMO
 * @author Samuele Barella - mat.740688
 * @author Lorenzo Pengue - mat.740727
 * @author Andrea Pini - mat.740675
 */

public class CittadinoModel {

    public int idCittadino;
    public String nome;
    public String cognome;
    public String codiceFiscale;
    public int idIndirizzo;
    public IndirizzoModel indirizzo;
    public String userName;

    /**
     * model from json
     * @param json the json object
     */
    public CittadinoModel(JSONObject json){
        this(
                json.getInt("idCittadino"),
                json.getString("nome"),
                json.getString("cognome"),
                json.getString("codiceFiscale"),
                json.getInt("idIndirizzo"),
                json.getString("userName")
        );
    }
    public CittadinoModel(int idCittadino, String nome, String cognome, String codiceFiscale, int idIndirizzo, String userName){

        this.idCittadino = idCittadino;
        this.nome = nome;
        this.cognome = cognome;
        this.codiceFiscale = codiceFiscale;
        this.idIndirizzo = idIndirizzo;
        this.userName = userName;
    }

    public void setAddress(IndirizzoModel indirizzo){
        this.indirizzo = indirizzo;
    }

    @Override
    public String toString() {
        return nome + " " + cognome + " - " + codiceFiscale;
    }
}
