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

    opens org.shared_classes;
    exports org.shared_classes;
    exports org.client;
    exports org.server;
    opens org.server;
    exports org.server.gui.interfaces;
    opens org.server.gui.interfaces to javafx.fxml;
    exports org.server.gui.controllers;
    opens org.server.gui.controllers to javafx.fxml;
    exports org.client.gui.interfaces;
    opens org.client.gui.interfaces to javafx.fxml;
    exports org.client.gui.controllers;
    opens org.client.gui.controllers to javafx.fxml;
    exports org.server.resources;
    opens org.server.resources;
}