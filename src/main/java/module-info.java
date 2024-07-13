module com.micompany.todosapp {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.micompany.todosapp to javafx.fxml;
    exports com.micompany.todosapp;
}