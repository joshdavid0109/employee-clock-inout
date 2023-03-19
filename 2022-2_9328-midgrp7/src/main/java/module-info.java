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
    requires java.base;

    opens com.example.helloworld to javafx.fxml;
    opens org.server to com.google.gson;
    opens org.shared_classes;
    exports com.example.helloworld;
    exports org.shared_classes;
}