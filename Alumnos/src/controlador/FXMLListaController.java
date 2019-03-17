package controlador;

import datos.Alumno;
import Daos.Conexion;
import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * Controlador de interfaz gráfica.
 *
 * @author Gabriela Sandoval Cruz
 * @version 1.0
 * @since 2019-02-17
 */
public class FXMLListaController implements Initializable {

  @FXML
  private Button buttonAgregar;

  @FXML
  private Button buttonEditar;

  @FXML
  private Button buttonEliminar;

  @FXML
  private Button buttonGuardar;

  @FXML
  private Button buttonLimpiar;

  @FXML
  private Button buttonSalir;

  @FXML
  private TableView<Alumno> tableAlumnos;

  @FXML
  private TableColumn tableColumnApMaterno;

  @FXML
  private TableColumn tableColumnApPaterno;

  @FXML
  private TableColumn tableColumnMatricula;

  @FXML
  private TableColumn tableColumnNombre;

  @FXML
  private TextField textFieldApMaterno;

  @FXML
  private TextField textFieldApPaterno;

  @FXML
  private TextField textFieldMatricula;

  @FXML
  private TextField textFieldNombre;

  @FXML
  ObservableList<Alumno> alumnos;

  private Conexion conexion;

  @Override
  public void initialize(URL url, ResourceBundle rb) {

    conexion = new Conexion();
    try {
      conexion.conectar();
    } catch (SQLException ex) {
      Logger.getLogger(FXMLListaController.class.getName()).log(Level.SEVERE, null, ex);
    }

    //Inicializar lista
    alumnos = FXCollections.observableArrayList();

    //Llenar listas
    Alumno.llenarInformacionAlumnos(conexion.getConnection(), alumnos);

    //Enlazar listas con tableView
    tableAlumnos.setItems(alumnos);

    //Enlazar columnas con atributos
    tableColumnMatricula.setCellValueFactory(new PropertyValueFactory<>("matricula"));
    tableColumnNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
    tableColumnApPaterno.setCellValueFactory(new PropertyValueFactory<>("apPaterno"));
    tableColumnApMaterno.setCellValueFactory(new PropertyValueFactory<>("apMaterno"));
    gestionarEventos();
    conexion.cerrar();
  }

  public void gestionarEventos() {
    tableAlumnos.getSelectionModel().selectedItemProperty().addListener(
            new ChangeListener<Alumno>() {
              @Override
              public void changed(ObservableValue<? extends Alumno> arg0,
                      Alumno valorAnterior, Alumno valorSeleccionado) {
                if (valorSeleccionado != null) {
                  textFieldMatricula.setText(String.valueOf(valorSeleccionado.getMatricula()));
                  textFieldNombre.setText(String.valueOf(valorSeleccionado.getNombre()));
                  textFieldApPaterno.setText(String.valueOf(valorSeleccionado.getApPaterno()));
                  textFieldApMaterno.setText(String.valueOf(valorSeleccionado.getApMaterno()));
                }
              }
            });

    //Botón Salir
    buttonSalir.setOnAction((ActionEvent event) -> {
      Stage stage = (Stage) buttonSalir.getScene().getWindow();
      Alert dialogoAlerta = new Alert(AlertType.CONFIRMATION);
      dialogoAlerta.setTitle("Salir");
      dialogoAlerta.setHeaderText(null);
      dialogoAlerta.initStyle(StageStyle.UTILITY);
      dialogoAlerta.setContentText("¿Estás seguro que deseas salir?");
      //Se recibe la respuesta del usuario
      Optional<ButtonType> result = dialogoAlerta.showAndWait();
      if (result.get() == ButtonType.OK) {
        stage.close();
      }
    });

    //Botón Agregar
    buttonAgregar.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        limpiarComponentes();
        textFieldMatricula.requestFocus();
      }
    });

    //Botón Guardar
    buttonGuardar.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        try {
          guardarRegistro();
        } catch (SQLException ex) {
          Logger.getLogger(FXMLListaController.class.getName()).log(Level.SEVERE, null, ex);
        }
        limpiarComponentes();
      }
    });

    //Botón Editar
    buttonEditar.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        try {
          actualizarRegistro();
          limpiarComponentes();
          textFieldMatricula.requestFocus();
          System.out.println("Adiós");
        } catch (SQLException ex) {
          Logger.getLogger(FXMLListaController.class.getName()).log(Level.SEVERE, null, ex);
        }
      }
    });

    //Botón Eliminar
    buttonEliminar.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        try {
          eliminarRegistro();
        } catch (SQLException ex) {
          Logger.getLogger(FXMLListaController.class.getName()).log(Level.SEVERE, null, ex);
        }
      }
    });

    //Botón Limpiar
    buttonLimpiar.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        limpiarComponentes();
      }
    });
  }

  /**
   * Metodo para guardar el registro de la clase Alumno.
   *
   * @throws SQLException
   */
  @FXML
  private void guardarRegistro() throws SQLException {
    Alumno alumno = new Alumno(
            textFieldMatricula.getText(),
            textFieldNombre.getText(),
            textFieldApPaterno.getText(),
            textFieldApMaterno.getText());

    //Llamar al método guardarRegistro de la clase Alumno
    conexion.conectar();

    int resultado = alumno.guardarRegistro(conexion.getConnection());
    conexion.cerrar();

    if (resultado == 1) {
      alumnos.add(alumno);
      Alert mensaje = new Alert(AlertType.INFORMATION);
      mensaje.setTitle("Registro agregado");
      mensaje.setContentText("El registro ha sido agregado exitosamente");
      mensaje.setHeaderText("Resultado:");
      mensaje.show();
    }
  }

  /**
   * Método que actualiza el registro de Alumnos.
   *
   * @throws SQLException Exepción
   */
  @FXML
  public void actualizarRegistro() throws SQLException {
    Alumno alumno = new Alumno(
            textFieldNombre.getText(),
            textFieldApPaterno.getText(),
            textFieldApMaterno.getText());
    conexion.conectar();
    String resultado = alumno.actualizarRegistro(conexion.getConnection(),
            this.textFieldMatricula.getText());
    conexion.cerrar();

    if (resultado == "1") {
      alumnos.set(tableAlumnos.getSelectionModel().getSelectedIndex(), alumno);

      Alert mensaje = new Alert(AlertType.INFORMATION);
      mensaje.setTitle("Registro actualizado");
      mensaje.setContentText("El registro ha sido actualizado exitosamente");
      mensaje.setHeaderText("Resultado:");
      mensaje.show();
    } else {
      Alert mensaje = new Alert(AlertType.INFORMATION);
      mensaje.setTitle("Registro no actualizado");
      mensaje.setContentText("El registro no se actualizó");
      mensaje.show();
    }
  }

  /**
   * Método que elimina el registro.
   *
   * @throws SQLException Excepción
   */
  @FXML
  public void eliminarRegistro() throws SQLException {
    conexion.conectar();
    String resultado = tableAlumnos.getSelectionModel().getSelectedItem().eliminarRegistro(
            conexion.getConnection(), this.textFieldMatricula.getText());
    conexion.cerrar();

    if (resultado == "1") {
      alumnos.remove(tableAlumnos.getSelectionModel().getSelectedIndex());

      Alert mensaje = new Alert(AlertType.INFORMATION);
      mensaje.setTitle("Registro eliminado");
      mensaje.setContentText("El registro ha sido eliminado exitosamente");
      mensaje.setHeaderText("Resultado:");
      mensaje.show();
    }
  }

  /**
   * Método que limpia los componentes.
   */
  @FXML
  public void limpiarComponentes() {
    textFieldMatricula.setText(null);
    textFieldNombre.setText(null);
    textFieldApPaterno.setText(null);
    textFieldApMaterno.setText(null);
  }

}
