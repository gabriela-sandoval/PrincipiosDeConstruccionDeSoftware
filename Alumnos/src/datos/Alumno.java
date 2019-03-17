package datos;

import Daos.AlumnoInterfaceDao;
import Daos.Conexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;

/**
 * Clase Alumno.
 *
 * @author Gabriela Sandoval Cruz
 * @version 1.0
 * @since 2019-02-17
 */
public class Alumno implements AlumnoInterfaceDao {

  private StringProperty matricula;
  private StringProperty nombre;
  private StringProperty apPaterno;
  private StringProperty apMaterno;

  /**
   * Constructor de la clase Alumno.
   *
   * @param matricula es el identificador del alumno
   * @param nombre del alumno
   * @param apPaterno apellido paterno del alumno
   * @param apMaterno apellido materno del alumno
   */
  public Alumno(String matricula, String nombre, String apPaterno, String apMaterno) {
    this.matricula = new SimpleStringProperty(matricula);
    this.nombre = new SimpleStringProperty(nombre);
    this.apPaterno = new SimpleStringProperty(apPaterno);
    this.apMaterno = new SimpleStringProperty(apMaterno);
  }

  /**
   * Constructor de la clase Alumno el cual permite obtener el nombre, el apellido paterno y
   * materno.
   *
   * @param nombre del alumno
   * @param apPaterno apellido paterno del alumno
   * @param apMaterno apellido materno del alumno
   */
  public Alumno(String nombre, String apPaterno, String apMaterno) {
    this.nombre = new SimpleStringProperty(nombre);
    this.apPaterno = new SimpleStringProperty(apPaterno);
    this.apMaterno = new SimpleStringProperty(apMaterno);
  }

  public String getMatricula() {
    return matricula.get();
  }

  public void setMatricula(String matricula) {
    this.matricula = new SimpleStringProperty(matricula);
  }

  public String getNombre() {
    return nombre.get();
  }

  public void setNombre(String nombre) {
    this.nombre = new SimpleStringProperty(nombre);
  }

  public String getApPaterno() {
    return apPaterno.get();
  }

  public void setApPaterno(String apPaterno) {
    this.apPaterno = new SimpleStringProperty(apPaterno);
  }

  public String getApMaterno() {
    return apMaterno.get();
  }

  public void setApMaterno(String apMaterno) {
    this.apMaterno = new SimpleStringProperty(apMaterno);
  }
  
  /**
   * Método para llenar la información de los Alumnos.
   * @param conexion parametro que conecta con la base de datos
   * @param alumnos los alumnos
   * @return 
   */
  
  public static int llenarInformacionAlumnos(Connection conexion, ObservableList<Alumno> alumnos) {
    try {
      Statement statement = conexion.createStatement();
      ResultSet resultado = statement.executeQuery(
              "SELECT matricula, "
              + "nombre, "
              + "apPaterno,"
              + "apMaterno FROM alumno ");
      while (resultado.next()) {
        alumnos.add(
                new Alumno(
                        resultado.getString("matricula"),
                        resultado.getString("nombre"),
                        resultado.getString("apPaterno"),
                        resultado.getString("apMaterno")
                )
        );
      }
      return 1;
    } catch (SQLException e) {
      return 0;
    }
  }

  /**
   * Método para guardar registro del Alumno.
   *
   * @param conexion parametro que conecta con la base de datos
   * @return regresa el número 1, el cual asegura que el registro se guardó correctamente.
   */
  @Override
  public int guardarRegistro(Connection conexion) {
    try {
      PreparedStatement instruccion;
      instruccion = conexion.prepareStatement("INSERT INTO alumno (matricula, nombre, "
              + "apPaterno, apMaterno) "
              + "VALUES (?, ?, ?, ?)");
      instruccion.setString(1, matricula.get());
      instruccion.setString(2, nombre.get());
      instruccion.setString(3, apPaterno.get());
      instruccion.setString(4, apMaterno.get());
      instruccion.executeUpdate();
    } catch (SQLException e) {
      return 0;
    }
    return 1;
  }

  /**
   * Método para actualizar el registro del Alumno.
   *
   * @param conexion parametro que conecta con la base de datos
   * @param matricula id del Alumno
   * @return regresa un número que confirma al actualización
   */
  @Override
  public String actualizarRegistro(Connection conexion, String matri) {
    try {
      PreparedStatement instruccion
              = conexion.prepareStatement(
                      "UPDATE alumno SET  nombre = ?, apPaterno = ?, apMaterno = ? "
                      + "where matricula = ?"
              );
      
      instruccion.setString(4, matri);
      instruccion.setString(1, nombre.get());
      instruccion.setString(2, apPaterno.get());
      instruccion.setString(3, apMaterno.get());
      instruccion.executeUpdate();
    } catch (SQLException e) {
      return "0";
    }
    return "1";
  }

  /**
   * Método para elliminar el registro del Alumno.
   *
   * @param conexion parametro que conecta con la base de datos
   * @param matricula id del Alumno
   * @return regresa un número que confirma la eliminación
   */
  @Override
  public String eliminarRegistro(Connection conexion, String matricula) {
    try {
      PreparedStatement instruccion = conexion.prepareStatement(
              "DELETE FROM alumno where  matricula = ?"
      );
      instruccion.setString(1, matricula);

      instruccion.executeUpdate();

      return "1";

    } catch (SQLException e) {
      return "0";
    }
  }

}
