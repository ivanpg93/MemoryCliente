/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package logica.utils;

import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Modality;
import javafx.stage.Stage;
import logica.Manager;
import presentacion.PresentationLayer;

/**
 * Clase encargada de cargar archivos FXML en una aplicación JavaFX.
 * Esta clase ha sido reutilizada y modificada de antiguos proyectos.
 * @author joseb
 */
public class LoadFXML {

    /**
     * Abre una nueva ventana en la aplicación, cargando el contenido del
     * archivo FXML especificado.
     *
     * @param fxml Dirección del archivo FXML a cargar.
     */
    public void openNewWindow(String fxml) {
        try {
            // Creamos el loader para trabajar con la pantalla que abriremos
            FXMLLoader loader = new FXMLLoader(this.getClass().getClassLoader().getResource(fxml));

            // Con root haremos referencia al padre y lo cargaremos
            Parent root = loader.load();

            // Verificamos si el controlador ya ha sido cargado antes
            Object tempController = null;
            var managerController = Manager.getInstance().getController(loader.getController().getClass());

            if (managerController == null) {
                tempController = loader.getController();
            } else {
                tempController = managerController;
            }

            // Asignamos el controlador al loader
            loader.setController(tempController);

            // Creamos una escena y un stage para trabajar con otra pantalla
            Scene scene = new Scene(root);
            Stage stage = new Stage();

            // Hacemos el diálogo modal (significa que cuando se abra, hasta que no se cierre no se podrá interactuar con otras pantallas)
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.showAndWait();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    /**
     * Cierra la ventana actual y abre una nueva ventana en la app con el FXML
     * especificado.
     *
     * @param fxml Dirección del archivo FXML a cargar
     * @param button Botton que utilizamos para recojer la escena actual
     */
    public void changeScreen(String fxml, Button button) {
        try {
            // Creamos el loader para trabajar con la pantalla a la que cambiaremos
            FXMLLoader loader = new FXMLLoader(this.getClass().getClassLoader().getResource(fxml));

            // Con root haremos referencia al padre y lo cargaremos
            Parent root = loader.load();

            // Verificamos si el controlador ya ha sido cargado antes
            Object tempController = null;
            var managerController = Manager.getInstance().getController(loader.getController().getClass());

            if (managerController == null) {
                tempController = loader.getController();
            } else {
                tempController = managerController;
            }

            // Asignamos el controlador al loader
            loader.setController(tempController);
            
            // Obtenemos el stage actual
            Stage currentStage = (Stage) button.getScene().getWindow();
            
            // Creamos una nueva escena con la raíz cargada desde el archivo FXML
            Scene newScene = new Scene(root);
            
            // Establecemos la nueva escena en el stage actual
            currentStage.setScene(newScene);
            currentStage.setResizable(false);
            
            
            currentStage.show();
            
        } catch (IOException ex) {
            ex.printStackTrace();
            System.out.println(ex.getMessage());
        }
    }

}
