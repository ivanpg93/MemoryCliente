/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package logica;

import common.Jugador;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import static logica.App.juegoEJB;
import logica.utils.LoadFXML;
import presentacion.PresentationLayer;

/**
 *
 * @author ivan
 */
public class RegistroController extends PresentationLayer implements Initializable {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField txtEmail;

    @FXML
    private Button btnRegistro;
    
    @FXML
    private Button btnAtras;

    @FXML
    private TextField txtUsuario;

    @FXML
    private TextArea lvLogger;
    
    private LoadFXML loadFXML = new LoadFXML();

    @FXML
    void onActionRegistrar(ActionEvent event) throws IOException, Exception {
        try {
            // Instanciamos un nuevo jugador según las credenciales del formulario e intentamos registrarlo en la BBDD
            Jugador jugador = new Jugador(txtUsuario.getText(), txtEmail.getText());
            juegoEJB.registrarUsuario(jugador);
            lvLogger.appendText("El usuario se ha registrado correctamente.\n");
            loadFXML.changeScreen("logica/login.fxml", btnRegistro);
        } catch (Exception ex){
            ex.printStackTrace();
            lvLogger.appendText("El usuario no se ha podido registrar.\n");
        }
    }
    
    // Vuelve a la página de login
    @FXML
    void onActionBack(ActionEvent event) throws IOException {
         loadFXML.changeScreen("logica/login.fxml", btnRegistro);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Manager.getInstance().addController(this);
    }

    @Override
    public void close() {
    }

}
