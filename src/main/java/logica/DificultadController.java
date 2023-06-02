/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package logica;

import common.Partida;
import common.PartidaException;
import common.Utils;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import static logica.App.juegoEJB;
import static logica.App.jugadorApp;
import logica.utils.LoadFXML;
import presentacion.PresentationLayer;

/**
 *
 * @author ivan
 */
public class DificultadController extends PresentationLayer implements Initializable {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button btnNormal;

    @FXML
    private Button btnDificil;

    @FXML
    private Button btnFacil;
    
    private LoadFXML loadFXML = new LoadFXML();
    
    // Modificamos el valor de la dificultad antes de ir a la pantalla de juego
    @FXML
    void onActionFacil(ActionEvent event) throws PartidaException {
        Utils.dificultad = 0;

        loadFXML.changeScreen("logica/game.fxml", btnFacil);
    }
    
    // Modificamos el valor de la dificultad antes de ir a la pantalla de juego
    @FXML
    void onActionNormal(ActionEvent event) throws PartidaException {
        Utils.dificultad = 1;
        loadFXML.changeScreen("logica/game.fxml", btnNormal);
    }
    
    // Modificamos el valor de la dificultad antes de ir a la pantalla de juego
    @FXML
    void onActionDificil(ActionEvent event) throws PartidaException {
        Utils.dificultad = 2;
        
        loadFXML.changeScreen("logica/game.fxml", btnDificil);
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Manager.getInstance().addController(this);
    }

    @Override
    public void close() {
    }

}
