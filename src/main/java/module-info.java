module com.example.helloworld {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires java.rmi;
    requires com.google.gson;
    requires java.base;

    opens controller to javafx.fxml;
    opens org.shared_classes;
    exports controller;
    exports org.shared_classes;
    exports org.server.gui;
    opens org.server.gui to javafx.fxml;
    exports org.client.gui;
    opens org.client.gui to javafx.fxml;
    exports org.server;
    opens org.server;
}