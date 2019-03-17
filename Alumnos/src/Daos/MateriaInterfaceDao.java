package Daos;

import java.sql.Connection;

/**
 *
 * @author Gabriela Sandoval Cruz
 */
public interface MateriaInterfaceDao {
  int guardarMateria(Connection conexion);
  String actualizarMateria(Connection conexion, String id);
  String eliminarMateria(Connection conexion, String id);
}
