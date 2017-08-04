package com.everis.alicante.courses.becajava.garage.interfaces.implementation.jdbc;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.everis.alicante.courses.becajava.garage.domain.Vehiculo;
import com.everis.alicante.courses.becajava.garage.interfaces.dao.jdbc.VehiculoDAOJDBC;

public class VehiculoDAOJDBCImpl implements VehiculoDAOJDBC {

	// Variables de carga del driver

	private final String MYSQL_JDBC_DRIVER = "com.mysql.jdbc.Driver";
	private final String JDBC_CADENA_CONEXION = "jdbc:mysql://localhost:3306/garaje";

	@Override
	public void create(Vehiculo vehiculo) throws IOException {
		// TODO Auto-generated method stub

		Connection cn = null;

		try {

			cn = this.getConnection();
			String sql = "INSERT INTO VEHICULOS(MATRICULA, TIPO_VEHICULO) VALUES(?, ?)";
			PreparedStatement pst = null;
			pst = cn.prepareStatement(sql);
			pst.setString(1, vehiculo.getMatricula());
			pst.setInt(2, vehiculo.getId());
			pst.execute();

		} catch (Exception e) {
			System.out.println("Error al insertar vehiculo " + e.getMessage());

		} finally {
			try {
				cn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	@Override
	public Vehiculo read(String matricula) throws IOException {

		Connection cn = null;
		PreparedStatement pst = null;
		cn = this.getConnection();
		Vehiculo vehiculo = null;

		try {
			String sql = "SELECT * FROM VEHICULOS WHERE MATRICULA=?";
			pst = cn.prepareStatement(sql);
			pst.setString(1, matricula);
			ResultSet rs = pst.executeQuery();

			while (rs.next()) {
				vehiculo = new Vehiculo();
				vehiculo.setMatricula(rs.getString("MATRICULA"));
				vehiculo.setTipoVehiculo(rs.getString("TIPO_VEHICULO"));
				System.out.println(
						"MATRICULA: " + vehiculo.getMatricula() + " TIPO: " + vehiculo.getTipoVehiculo() + ".");
			}

		} catch (SQLException e) {
			System.out.println("No se ha podido obtener el coche " + e.getMessage());

		} finally {
			try {
				cn.close();
			} catch (SQLException e) {
				System.out.println("Error al cerrar la conexión" + e.getMessage());
			}
		}
		return vehiculo;
	}

	@Override
	public void update(String matricula, Vehiculo vehiculo) throws IOException {
		// TODO Auto-generated method stub
		Connection cn = null;

		try {

			cn = this.getConnection();
			String sql = "UPDATE VEHICULOS SET MATRICULA=? WHERE MATRICULA=?";
			PreparedStatement pst = null;
			pst = cn.prepareStatement(sql);
			pst.setString(1, vehiculo.getMatricula());
			pst.setString(2, matricula);
			pst.execute();

		} catch (Exception e) {
			System.out.println("Error al insertar vehiculo " + e.getMessage());

		} finally {
			try {
				cn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	@Override
	public void delete(Vehiculo vehiculo) throws IOException {

		Connection cn = null;
		PreparedStatement pst = null;

		try {
			cn = this.getConnection();

			String sql = "DELETE FROM VEHICULOS WHERE MATRICULA=?";
			pst = cn.prepareStatement(sql);
			pst.setString(1, vehiculo.getMatricula());
			int result = pst.executeUpdate();

			if (result == 0) {
				System.out.println("No se ha encontrado el vehículo");
			} else {
				System.out.println("Se han borrado " + result + " vehiculos");
			}

		} catch (Exception e) {
			System.out.println("Error al borrar el vehiculo vehiculo " + e.getMessage());

		} finally {
			try {
				cn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	@Override
	public Connection getConnection() throws IOException {
		// TODO Auto-generated method stub

		Connection cn = null;

		try {
			// Carga del driver
			Class.forName(MYSQL_JDBC_DRIVER);

			// Conexión a la BBDD
			cn = DriverManager.getConnection(JDBC_CADENA_CONEXION, "root", "");
		} catch (Exception e) {
			System.out.println("Error al obtener la conexión de la BBDD." + e.getMessage());
		}
		return cn;
	}

	@Override
	public List<Vehiculo> readAll() throws IOException {

		Connection cn = this.getConnection();
		Statement st = null;
		List<Vehiculo> vehiculos = new ArrayList();
		Vehiculo vehiculo = null;

		try {

			st = cn.createStatement();
			String sql = "SELECT * FROM VEHICULOS,TIPOS_VEHICULO WHERE TIPO_VEHICULO=ID_TIPO";
			ResultSet rs = st.executeQuery(sql);

			while (rs.next()) {
				vehiculo = new Vehiculo();
				vehiculo.setMatricula(rs.getString("MATRICULA"));
				vehiculo.setTipoVehiculo(rs.getString("NOMBRE_TIPO"));
				vehiculos.add(vehiculo);
			}

		} catch (SQLException e) {
			e.printStackTrace();

		} finally {
			try {
				cn.close();
			} catch (SQLException e) {
				System.out.println("Error al cerrar la conexión" + e.getMessage());
			}
		}
		return vehiculos;
	}

}
