package centrivaccinali.models;

import java.util.Date;

/**
 * @author SEDE COMO
 * @author Samuele Barella - mat.740688
 * @author Lorenzo Pengue - mat.740727
 * @author Andrea Pini - mat.740675
 */

public class VaccinazioniModel {

    public int idVaccinazione;

    public int idCittadino;

    public Date dataVaccino;

    public VaccinazioniModel(int idVaccinazione, int idCittadino, String dateString){
        this.idCittadino = idCittadino;
        this.idVaccinazione = idVaccinazione;
        this.dataVaccino = new Date(dateString);
    }

    @Override
    public String toString(){
        return dataVaccino.toString();
    }

}
