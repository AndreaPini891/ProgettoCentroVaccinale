/**
 *       @author SEDE COMO
 *       @author Samuele Barella - mat.740688
 *       @author Lorenzo Pengue - mat.740727
 *       @author Andrea Pini - mat.740675
 */

module cittadini {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.json;
    requires java.net.http;
    requires org.joda.time;

    opens centrivaccinali to javafx.fxml;
    opens centrivaccinali.controllers to javafx.fxml;

    exports centrivaccinali;
    exports centrivaccinali.controllers;
    exports centrivaccinali.web;
    exports centrivaccinali.models;
    opens centrivaccinali.models to javafx.fxml;
}
