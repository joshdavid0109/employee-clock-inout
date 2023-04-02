package org.server;

import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.server.gui.interfaces.AdminLoginInterface;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Objects;

public class ServerMain extends Application{

    private static final String configPath = "src/main/java/org/server/servercfg.txt";
    public int port;
    public String stubName;
    Image image = new Image("SLU_LOGO_REMOVEBG.png");

    @Override
    public void start(Stage primaryStage) {
        try {
            AdminLoginInterface adminLoginInterface = new AdminLoginInterface();
            port = Integer.parseInt(Objects.requireNonNull(getFromConfig("PORT")));
            stubName = getFromConfig("STUB_NAME");
            adminLoginInterface.port = port;
            adminLoginInterface.stubName = stubName;
            adminLoginInterface.image = image;
            adminLoginInterface.start(primaryStage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * It reads a file and returns the value of the key that was passed to it
     *
     * @param anoKukuninKo The name of the parameter you want to get from the config file.
     * @return The value of the key that is passed in.
     */
    private String getFromConfig(String anoKukuninKo) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(configPath));
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(": ");
                if (parts[0].equals(anoKukuninKo)) {
                    return parts[1].replaceAll("\"", "");
                }
            }
            System.err.println("[WARNING]: cannot find "+anoKukuninKo+" in "+configPath+" check mo dun pls");
            br.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
}