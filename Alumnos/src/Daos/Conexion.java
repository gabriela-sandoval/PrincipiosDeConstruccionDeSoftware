package Daos;

/**
 * Clase para realizar la conexion con la base de datos y las consultas.
 *
 * @author Gabriela Sandoval Cruz
 * @since 2019-02-17
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class Conexion {

  private Connection connection;
  private String url = "jdbc:mysql://localhost/listaalumnos";
  private String usuario = "root";
  private String contrasena = "123456";

  public Connection getConnection() {
    return connection;
  }

  /**
   * Método setConnection.
   * 
   * @param connection parametro que conecta con la base de datos
   */
  public void setConnection(Connection connection) {
    this.connection = connection;
  }

  /**
   * Método conectar.
   * 
   * @throws SQLException Excepción
   */
  public void conectar() throws SQLException {
    try {
      Class.forName("com.mysql.jdbc.Driver");
      connection = DriverManager.getConnection(url, usuario, contrasena);
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    }
  }

  /**
   * Método cerrar.
   */
  public void cerrar() {
    try {
      connection.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
}
