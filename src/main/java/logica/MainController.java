/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package logica;

import common.Utils;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import static logica.App.juegoEJB;
import logica.utils.LoadFXML;
import presentacion.PresentationLayer;

/**
 *
 * @author ivan
 */
public class MainController extends PresentationLayer implements Initializable {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button btnHallOfFame;

    @FXML
    private Button btnNewGame;
    
    @FXML
    private Button btnLogout;

    @FXML
    private Button btnExit;

    private LoadFXML loadFXML = new LoadFXML();
    
    // Comprueba si el usuario está logueado. False: Vuelve al login
    @FXML
    void onActionNewGame(ActionEvent event) throws IOException {
        if (!Utils.login) {
            Utils.alertLogin();
            loadFXML.changeScreen("logica/login.fxml", btnNewGame);
        } else {
            loadFXML.changeScreen("logica/dificultad.fxml", btnNewGame);
        }
    }
    
    // Comprueba si el usuario está logueado. False: Vuelve al login
    @FXML
    void onActionHallOfFame(ActionEvent event) throws IOException {
        if (!Utils.login) {
            Utils.alertLogin();
            loadFXML.changeScreen("logica/login.fxml", btnNewGame);
        } else {
            loadFXML.changeScreen("logica/hallOfFame.fxml", btnNewGame);
        }
    }
    
    // Comprueba si el usuario está logueado. False: Vuelve al login
    @FXML
    void onActionLogout(ActionEvent event) {
        if (!Utils.login) {
            Utils.alertLogin();
            loadFXML.changeScreen("logica/login.fxml", btnNewGame);
        } else {
            Utils.login = false;
            juegoEJB.cerrarSesion();
            loadFXML.changeScreen("logica/login.fxml", btnNewGame);
        }
    }
    
    // Comprueba si el usuario está logueado. False: Vuelve al login
    @FXML
    void onActionExit(ActionEvent event) throws IOException {
        if (!Utils.login) {
            Utils.alertLogin();
            loadFXML.changeScreen("logica/login.fxml", btnNewGame);
        } else {
            Utils.alertExit();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Manager.getInstance().addController(this);
    }

    @Override
    public void close() {
    }

}
