package alumnos;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * Clase principal de Alumnos.
 *
 * @author Gabriela Sandoval Cruz
 * @version 1.0
 * @since 2019-02-17
 */
public class Alumnos extends Application {

  /**
   * Clase que llama a la interfaz gráfica.
   *
   * @param primaryStage parametro Stage
   * @throws IOException excepción
   */
  @Override
  public void start(Stage primaryStage) throws IOException {
    Parent root = FXMLLoader.load(getClass().getResource("/interfazGrafica/FXMLLista.fxml"));

    Scene scene = new Scene(root);

    primaryStage.setScene(scene);
    Image icono = new Image("/iconos/iconoPrincipal.png");
    primaryStage.getIcons().add(icono);
    primaryStage.setTitle("Lista de alumnos");
    primaryStage.setResizable(false);
    primaryStage.show();
  }

  /**
   * Clase main de Alumnos.
   *
   * @param args the command line arguments
   */
  public static void main(String[] args) {

    launch(args);
  }

}
