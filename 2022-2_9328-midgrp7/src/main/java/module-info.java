module com.example.helloworld {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires java.rmi;
    requires com.google.gson;

    opens com.example.helloworld to javafx.fxml;
    exports com.example.helloworld;
}