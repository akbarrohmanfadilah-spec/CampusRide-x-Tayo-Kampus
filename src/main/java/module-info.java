module com.mycompany.campusride {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.mycompany.campusride to javafx.fxml;
    exports com.mycompany.campusride;
}
