package Daos;

import datos.Alumno;

import java.sql.Connection;
import javafx.collections.ObservableList;

/**
 *
 * @author Gabriela Sandoval Cruz
 */
public interface AlumnoInterfaceDao {

  int guardarRegistro(Connection conexion);
  String actualizarRegistro(Connection conexion, String matricula);
  String eliminarRegistro(Connection conexion, String matricula);
}
