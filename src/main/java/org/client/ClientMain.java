package org.client;

import javafx.application.Application;
import javafx.stage.Stage;
import org.client.gui.LoginInterface;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Objects;

public class ClientMain extends Application {

    private static final String configPath = "src/main/java/org/client/config.txt";
    public String ip_address;
    public int port;
    public String stubName;

    @Override
    public void start(Stage primaryStage) {
        try {
            this.ip_address = getFromConfig("IP_ADDRESS");
            this.port = Integer.parseInt(Objects.requireNonNull(getFromConfig("PORT")));
            this.stubName = getFromConfig("STUB_NAME");

            LoginInterface loginInterface = new LoginInterface();
            loginInterface.ip_address = ip_address;
            loginInterface.remoteReferenceName = stubName;
            loginInterface.port = port;
            loginInterface.start(primaryStage);

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
                    String x = parts[1].replaceAll("\"", "");
                    System.out.println(x);
                    return x;
                }
            }
            br.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
            System.err.println("cannot find "+anoKukuninKo+" in "+configPath+" check mo dun pls");
        }
        return null;
    }
}