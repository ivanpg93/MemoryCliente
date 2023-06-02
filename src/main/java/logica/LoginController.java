/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package logica;

import common.Jugador;
import common.Lookups;
import common.Utils;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javax.naming.NamingException;
import static logica.App.juegoEJB;
import static logica.App.jugadorApp;
import logica.utils.LoadFXML;
import presentacion.PresentationLayer;

/**
 *
 * @author ivan
 */
public class LoginController extends PresentationLayer implements Initializable {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button btnLogin;

    @FXML
    private TextField txtEmail;

    @FXML
    private Button btnRegistro;

    @FXML
    private TextField txtUsuario;

    @FXML
    private TextArea lvLogger;

    private LoadFXML loadFXML = new LoadFXML();

    @FXML
    void onActionLogin(ActionEvent event) throws IOException {
        try {
            if(!Utils.login) juegoEJB = Lookups.juegoEJBRemoteLookup();
            
            // Instanciamos un nuevo jugador según las credenciales del formulario e intentamos iniciar sesión
            Jugador jugador = new Jugador(txtUsuario.getText(), txtEmail.getText());
            jugador = juegoEJB.getSesion(jugador);
            jugadorApp = jugador;
            // Cambiamos la variable login conforme se el usuario se ha logueado y cambiamos de pantalla
            Utils.login = true;
            loadFXML.changeScreen("logica/main.fxml", btnLogin);
        } catch (Exception ex) {
            lvLogger.appendText("El usuario no ha podido loguearse. Revisa las credenciales.\n");
        }
    }

    @FXML
    void onActionRegistrar(ActionEvent event) throws IOException {
        loadFXML.changeScreen("logica/registro.fxml", btnLogin);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Manager.getInstance().addController(this);

        // Si el usuario no está logueado, se crea una conexión con el servidor
        if (!Utils.login) {
            try {
                juegoEJB = Lookups.juegoEJBRemoteLookup();
            } catch (NamingException ex) {
                Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public void close() {
    }

}
