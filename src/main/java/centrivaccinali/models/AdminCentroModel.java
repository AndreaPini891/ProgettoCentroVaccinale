package centrivaccinali.models;

import org.json.JSONObject;

/**
 * @author SEDE COMO
 * @author Samuele Barella - mat.740688
 * @author Lorenzo Pengue - mat.740727
 * @author Andrea Pini - mat.740675
 */
public class AdminCentroModel {

    public int id;
    public String nome;
    public String cognome;
    public String nomeCentro;
    public String userName;

    /**
     * model from json
     * @param json the json object
     */
    public AdminCentroModel(JSONObject json){
        this(
                json.getInt("id"),
                json.getString("nome"),
                json.getString("cognome"),
                json.getString("nomeCentro"),
                json.getString("userName")
        );
    }
    public AdminCentroModel(int id, String nome, String cognome, String nomeCentro, String userName){

        this.id = id;
        this.nome = nome;
        this.cognome = cognome;
        this.nomeCentro = nomeCentro;
        this.userName = userName;
    }

    @Override
    public String toString() {
        return nome + " " + cognome + " " + nomeCentro;
    }
}
