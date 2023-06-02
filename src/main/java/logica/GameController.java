
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package logica;

import common.CartaMemory;
import common.MazoDeCartas;
import common.Partida;
import common.PartidaException;
import common.Utils;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import static logica.App.juegoEJB;
import static logica.App.jugadorApp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.stage.Screen;
import javafx.util.Duration;
import javax.imageio.ImageIO;
import logica.utils.LoadFXML;
import presentacion.PresentationLayer;

/**
 * Controlador del videojuego Es el controlador que gestiona la impresion cartas
 * y demas durante la partida.
 *
 * @author ivan
 */
public class GameController extends PresentationLayer implements Initializable {

    @FXML
    private ResourceBundle resources;

    @FXML
    private GridPane gt_tablero;

    @FXML
    private URL location;

    @FXML
    private Label lb_intento;

    @FXML
    private Label lb_tiempo;

    @FXML
    private Button btn_salirPartida;

    //Objeto LoafFXML utilizado para pasar entre pantallas
    private LoadFXML loadFXML = new LoadFXML();

    //Timeline (temporizador llama cada segundo al servidor)
    private Timeline timeline;

    private MazoDeCartas mazoDeCartas;
    private ArrayList<CartaMemory> listaCartas;

    //Variable que contendra la imagen delantera de la carta
    private Image imagen;

    //Variable que contiene la imagen reversa de la carta
    private Image imagenBack;

    //Indice de la primera carta que se voleta
    private int index_1;

    private int intentos;

    //Hace referencia al image view de la primera carta volteada
    private ImageView backImageView;

    private Partida partida;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            //Añadimos este controlador al manager
            Manager.getInstance().addController(this);

            //Creamos una partida mediante el jugador y la dificultad
            partida = new Partida(jugadorApp, Utils.dificultad);

            //Empezar partida
            juegoEJB.empezarPartida(partida);

            //Ponemos index_1 en -1 (no hace falta es por seguridad)
            index_1 = -1;

            //Obtenemos el mazo de cartas mezclado del juegoEJB.
            mazoDeCartas = juegoEJB.obtenerMazoMezclado();

            //Obtenemos una lista de las cartas del mazo(mezcladas).
            listaCartas = (ArrayList<CartaMemory>) mazoDeCartas.getCartas();

            //Obetnemos del servidor la imagen reversa de las cartas
            imagenBack = setByteToImage(listaCartas.get(0).getBackOfCardImage());

            //Ponemos el numero de intentos(local) a 0
            intentos = 0;

            //Asignamos el valor de intentos a la label.
            lb_intento.setText(String.valueOf(intentos));

            //Configurar el grid, donde se reparten las cartas
            confGrid();

            //Iniciamos la timeline que pregunta el tiempo al servidor
            cuentaAtras();
        } catch (IOException ex) {
            Logger.getLogger(GameController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (PartidaException ex) {
            Logger.getLogger(GameController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Configura e imprimer las cartas en el tablero (GridPane)
     */
    private void confGrid() {

        //Obetnemos el numero de cartas del mazo
        int numCartas = mazoDeCartas.getNumeroCartas();

        // Calcular el número de columnas en función del número de cartas
        int numColumnas = calculateNumColumns(numCartas);

        //Calculamos el numero de filas.
        int numFilas = (int) Math.ceil((double) numCartas / numColumnas); // Calcular el número de filas

        int rowIndex = 0; // Índice de fila actual
        int columnIndex = 0; // Índice de columna actual

        //Creamos y añadimos un image view de cada carta a cada cela del tablero
        for (CartaMemory carta : listaCartas) {

            // Crear el ImageView para la carta
            ImageView imageView = new ImageView(imagenBack); //Imagen reversa

            imageView.setFitWidth(100); // Ajusta el ancho según tus necesidades

            imageView.setPreserveRatio(true);

            // Agregar el ImageView al GridPane en la posición correspondiente
            gt_tablero.add(imageView, columnIndex, rowIndex);

            //Añadimos un listener a cada image view para que detecte la pulsacion
            //del raton
            imageView.setOnMouseClicked(event -> { // Cuando pulse en este imageview

                //Obtenemos el indice de la carta seleccionada
                int index = listaCartas.indexOf(carta);

                try {
                    if (juegoEJB.getVoleo()) { // Si es la primera carta volteada

                        //Guardamos el image view de dicha carta
                        backImageView = imageView;
                    }
                    //Giramos la carta
                    flipCard(index, imageView);

                } catch (IOException ex) {
                    Logger.getLogger(GameController.class.getName()).log(Level.SEVERE, null, ex);
                } catch (InterruptedException ex) {
                    Logger.getLogger(GameController.class.getName()).log(Level.SEVERE, null, ex);
                } catch (Exception ex) {
                    Logger.getLogger(GameController.class.getName()).log(Level.SEVERE, null, ex);
                }
            });

            // Actualizar los índices de fila y columna
            columnIndex++;

            if (columnIndex == numColumnas) {//Si el indice de la columna es igual al numero de columnas
                columnIndex = 0; // Reiniciar la columna
                rowIndex++; // Avanzar a la siguiente fila
            }
        }

        // Ajustar las restricciones de columnas y filas del GridPane
        ColumnConstraints columnConstraints = new ColumnConstraints();
        columnConstraints.setHgrow(Priority.ALWAYS);
        columnConstraints.setFillWidth(true);
        gt_tablero.getColumnConstraints().addAll(Collections.nCopies(numColumnas, columnConstraints));

        RowConstraints rowConstraints = new RowConstraints();
        rowConstraints.setVgrow(Priority.ALWAYS);
        rowConstraints.setFillHeight(true);
        gt_tablero.getRowConstraints().addAll(Collections.nCopies(numFilas, rowConstraints));

        // Ajustar los márgenes del GridPane
        Insets gridPaneInsets = new Insets(10);
        GridPane.setMargin(gt_tablero, gridPaneInsets);
    }

    /**
     * Calcula el numero de columnas del tablero segun el numero de cartas
     *
     * @param numCartas
     * @return El numero de columnas
     */
    private int calculateNumColumns(int numCartas) {
        int screenWidth = (int) Screen.getPrimary().getBounds().getWidth();
        int columnWidth = 100; // Ancho de cada carta en píxeles (ajustar según tus necesidades)
        int numColumnas = screenWidth / columnWidth;
        return Math.min(numColumnas, numCartas);
    }

    /**
     * Gira una carta
     *
     * @param index Indice de la carta que se quiere girar
     * @param imageView Image view de la carta que giramos
     * @throws IOException
     * @throws InterruptedException
     * @throws Exception
     */
    private void flipCard(int index, ImageView imageView) throws IOException, InterruptedException, Exception {
        // Obtener la carta correspondiente al índice
        CartaMemory carta = listaCartas.get(index);

        if (!carta.isGirada()) {//Si la carta no esta girada

            if (juegoEJB.getVoleo()) { //Si es la primera carta volteada

                //Obtenemos la imagen frontal de la carta
                imagen = setByteToImage(carta.getImage());

                //Insertamos la imagen frontal en el imageView de la carta
                imageView.setImage(imagen);

                //Marcamos la carta como girada
                carta.setGirada(true);

                //Indicamos que hemos volteado la primera carta
                juegoEJB.voltearCarta();

                //Guardamos el indice de la carta para despues
                index_1 = index;

            } else if (!juegoEJB.getVoleo()) {//Si es la segunda carta volteada

                //Obtenemos la imagen frontal de la carta
                imagen = setByteToImage(carta.getImage());

                //Insertamos la imagen frontal en el image view de la carta
                imageView.setImage(imagen);

                //Marcamos la carta como girada
                carta.setGirada(true);

                //Obtenemos la primera carta voletada a traves del indix_1
                //index_1 guarda la primera carta volteada
                CartaMemory carta_1 = listaCartas.get(index_1);

                //Sumamos los intentos y obtenemos la suma
                intentos = juegoEJB.sumarIntentos();

                //Si las no cartas son iguales
                //La funcion cartasCoenciden() tambien marcara las cartas como emparejadas
                //si son iguales
                if (!juegoEJB.cartasConciden(carta, carta_1, mazoDeCartas.getCartas().indexOf(carta), mazoDeCartas.getCartas().indexOf(carta_1))) {

                    //Para la ejecucion durante un segundo
                    //Para que el jugador pueda ver las dos cartas volteadas
                    Task<Void> task = new Task<Void>() {
                        @Override
                        protected Void call() throws Exception {
                            gt_tablero.getChildren().forEach(child -> child.setDisable(true));
                            Thread.sleep(1000); // Pausa durante 1 segundo
                            return null;
                        }
                    };

                    //Cuando acabe la pausa giramos de vuelta las cartas
                    //Se reinicia el ciclio otra vez
                    task.setOnSucceeded(e -> {
                        carta.setGirada(false);
                        carta_1.setGirada(false);
                        imageView.setImage(imagenBack);
                        backImageView.setImage(imagenBack);
                        gt_tablero.getChildren().forEach(child -> child.setDisable(false));
                    });

                    //Lazamos la pausa
                    new Thread(task).start();

                }

                if (juegoEJB.comprobarVictoria()) {//Si ya estan todas emparejadas

                    //Paramos la time line
                    timeline.stop();

                    //Terminamos la partida
                    Partida p = juegoEJB.terminarPartida();

                    Utils.alertVictoria(p.getPuntos());
                    //Vamos al HallOfFame
                    loadFXML.changeScreen("logica/hallOfFame.fxml", btn_salirPartida);
                }

                //Actualizamos la label de intentos
                lb_intento.setText(String.valueOf(intentos));

                //Volvemos a voltear carta para que se ponga en false
                juegoEJB.voltearCarta();
            }

        }

    }

    /**
     * Inicializar la cuenta atrás
     */
    private void cuentaAtras() {

        //Creamos una time line que preguntara cada segunfo al servidor
        //el tiempo del temporizador
        timeline = new Timeline(
                new KeyFrame(Duration.seconds(1), event -> {
                    int cuentaAtras = juegoEJB.obtenerTiempoPartida();
                    System.out.println(cuentaAtras);
                    lb_tiempo.setText(Utils.formatTime(cuentaAtras));
                })
        );
        timeline.setCycleCount(juegoEJB.getTiempoMaximo());

        //Si la timeline termina se termina la partida
        timeline.setOnFinished(event -> {
            Utils.alertTime();
            try {
                juegoEJB.terminarPartida();
            } catch (Exception ex) {
                Logger.getLogger(GameController.class.getName()).log(Level.SEVERE, null, ex);
            }
            loadFXML.changeScreen("logica/hallOfFame.fxml", btn_salirPartida);

        });

        //Lanzamos la timeline
        timeline.play();

    }

    /**
     * Salir de la partida. Para la timeline y termina la partida, tambien
     * cambia de pantalla
     *
     * @param event
     * @throws Exception
     */
    @FXML
    void onActionSalirPartida(ActionEvent event) throws Exception {
        timeline.stop();
        juegoEJB.terminarPartida();
        loadFXML.changeScreen("logica/main.fxml", btn_salirPartida);
    }

    /**
     * Transforma una array de bytes en un ibjeto Image
     *
     * @param imageBytes
     * @return
     * @throws IOException
     */
    private Image setByteToImage(byte[] imageBytes) throws IOException {

        ByteArrayInputStream bis = new ByteArrayInputStream(imageBytes);

        // Lee la imagen desde el ByteArrayInputStream
        BufferedImage bufferedImage = ImageIO.read(bis);

        // Convierte el BufferedImage a un objeto javafx.scene.image.Image
        Image image = convertToJavaFXImage(bufferedImage);

        return image;

    }

    /**
     * Transforma una array de bytes, en un objeto Image
     *
     * @param bufferedImage
     * @return
     */
    private static Image convertToJavaFXImage(BufferedImage bufferedImage) {
        WritableImage wr = null;
        if (bufferedImage != null) {
            wr = new WritableImage(bufferedImage.getWidth(), bufferedImage.getHeight());
            PixelWriter pw = wr.getPixelWriter();
            for (int x = 0; x < bufferedImage.getWidth(); x++) {
                for (int y = 0; y < bufferedImage.getHeight(); y++) {
                    pw.setArgb(x, y, bufferedImage.getRGB(x, y));
                }
            }
        }
        return wr;
    }

    @Override
    public void close() {

    }
}
