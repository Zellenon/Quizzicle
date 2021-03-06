package gui;

import app.GameManager;
import app.game.QuizClient;
import app.game.QuizServer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Stage;
import net.NetworkManager;
import net.packet.SetUsername;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

/**
 * Highest-level class. Initializes the GUI and holds a static reference to it.
 * Handles (almost) all startup operations.
 *
 * @author gscott4
 * @author klastine
 * @author obergman
 * @version 1.30.0
 * @see MasterUIController The GUI for the application.
 * @see QuizClient Handles all upper-level interaction between client and server.
 * @see GameManager Handles all game data.
 */
public class Launcher extends Application
{
    /**
     * Static reference to UI Controller
     */
    public static MasterUIController GUI;

    /**
     * Starts the app
     *
     * @param args unused.
     */
    public static void main(String[] args)
    {
        Platform.setImplicitExit(true);
        launch(args);
    }

    /**
     * Run by launch(). Handles all startup code for the UI, QuizClient, and GameManager
     *
     * @param stage the Stage object generated by Launch and used for the main UI
     * @throws Exception
     */
    @Override
    public void start(Stage stage) throws Exception
    {
        System.out.println("started");
        // construct scene graph
        FXMLLoader loader = new FXMLLoader(getClass().getResource("MasterUI.fxml"));
        // https://docs.oracle.com/javase/8/javafx/api/javafx/fxml/FXMLLoader.html#getController--
        Parent root = loader.load();
        GUI = loader.getController();

        Scene scene = new Scene(root);
        stage.setTitle("Quizzicle");
        stage.setScene(scene);
        stage.show();

        FXMLLoader popupLoad = new FXMLLoader(getClass().getResource("popup.fxml"));
        Parent popupRoot = popupLoad.load();
        popupController dialogController = popupLoad.getController();
        ArrayList<String> results = new ArrayList<String>();
        dialogController.linkResults(results);
        Scene popup = new Scene(popupRoot);
        Stage popupStage = new Stage();
        popupStage.setScene(popup);
        popupStage.showAndWait();

        if (results.size() == 2)
        {
            QuizClient.setup();
            GameManager.setup();

            if (results.get(0).equals("host"))
            {
                GUI.setSide(true);
                QuizServer.setup();
                QuizServer.InitServer(results.get(1));
            } else
            {
                GUI.setSide(false);
                try
                {
                    String ip = "localhost";
                    TextInputDialog IPPopup = new TextInputDialog(ip);
                    IPPopup.setTitle("Enter host address:");
                    IPPopup.setHeaderText("Enter destination IP");
                    Optional<String> response = IPPopup.showAndWait();
                    if (response.isPresent()) ip = response.get();
                    else exit();
                    NetworkManager.StartClient(ip);
                    NetworkManager.SendToServer(new SetUsername(results.get(1)));
                } catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        } else exit();

        System.out.println("done initializing");
    }

    /**
     * Exits the program.
     */
    private void exit()
    {
        Platform.exit();
        System.exit(0);
        //TODO: Do we need to handle the ThreadExecutor in here?
    }
}