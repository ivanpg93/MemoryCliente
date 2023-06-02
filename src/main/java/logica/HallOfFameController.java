/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package logica;

import common.Partida;
import common.Utils;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import static logica.App.juegoEJB;
import logica.utils.LoadFXML;
import presentacion.PresentationLayer;

/**
 *
 * @author ivan
 */
public class HallOfFameController extends PresentationLayer implements Initializable {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private MenuButton btnDificultad;

    @FXML
    private TableView<Partida> tvScores;

    @FXML
    private TableColumn<Partida, String> colJugador;

    @FXML
    private TableColumn<Partida, Integer> colIntentos;

    @FXML
    private TableColumn<Partida, Integer> colTiempo;

    @FXML
    private TableColumn<Partida, Integer> colPuntuacion;

    @FXML
    private Button btnMenuPrincipal;

    @FXML
    private TextArea lvLogger;

    private LoadFXML loadFXML = new LoadFXML();
    
    // Filtro para obtener las puntuaciones del modo fácil
    @FXML
    void onActionPuntuacionFacil(ActionEvent event) throws Exception {
        btnDificultad.setText("Fácil");
        tvScores.getItems().clear();
        tvScores.getItems().addAll(juegoEJB.getHallOfGame(0));
    }

    // Filtro para obtener las puntuaciones del modo normal
    @FXML
    void onActionPuntuacionNormal(ActionEvent event) throws Exception {
        btnDificultad.setText("Normal");
        tvScores.getItems().clear();
        tvScores.getItems().addAll(juegoEJB.getHallOfGame(1));
    }
    
    // Filtro para obtener las puntuaciones del modo difícil
    @FXML
    void onActionPuntuacionDificil(ActionEvent event) throws Exception {
        btnDificultad.setText("Difícil");
        tvScores.getItems().clear();
        tvScores.getItems().addAll(juegoEJB.getHallOfGame(2));
    }
    
    // Comprueba si el usuario está logueado. False: Vuelve al login
    @FXML
    void onActionMenuPrincipal(ActionEvent event) throws IOException {
        if (!Utils.login) {
            Utils.alertLogin();
            loadFXML.changeScreen("logica/login.fxml", btnMenuPrincipal);
        } else {
            loadFXML.changeScreen("logica/main.fxml", btnMenuPrincipal);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Manager.getInstance().addController(this);
        
        // Enlazamos las columnas de la tabla con las propiedades del objeto
        colJugador.setCellValueFactory(cellData ->{
            String nombre = cellData.getValue().getJugador().getNombre();
            return new SimpleStringProperty(nombre);
        });
        colIntentos.setCellValueFactory(new PropertyValueFactory<>("numIntentos"));
        colTiempo.setCellValueFactory(new PropertyValueFactory<>("tiempoRestante"));
        colPuntuacion.setCellValueFactory(new PropertyValueFactory<>("puntos"));

        // Cargamos las puntuaciones del Hall Of Fame e indicamos el momento exacto de la actualización de los resultados
        try {
            tvScores.getItems().clear();
            tvScores.getItems().addAll(juegoEJB.getHallOfGame());
            lvLogger.appendText("Las puntuaciones están actualizadas a " + Utils.getCurrentDateTime() + "\n");
        } catch (Exception ex) {
            lvLogger.appendText("No se han podido cargar las puntuaciones.\n");
        }
    }

    @Override
    public void close() {
    }

}
