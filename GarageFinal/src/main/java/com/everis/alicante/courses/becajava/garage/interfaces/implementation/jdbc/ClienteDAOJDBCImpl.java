package com.everis.alicante.courses.becajava.garage.interfaces.implementation.jdbc;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.everis.alicante.courses.becajava.garage.domain.Cliente;
import com.everis.alicante.courses.becajava.garage.domain.Vehiculo;
import com.everis.alicante.courses.becajava.garage.interfaces.dao.jdbc.ClienteDAOJDBC;

public class ClienteDAOJDBCImpl implements ClienteDAOJDBC {

	// Variables de carga del driver

	private final String MYSQL_JDBC_DRIVER = "com.mysql.jdbc.Driver";
	private final String JDBC_CADENA_CONEXION = "jdbc:mysql://localhost:3306/garaje";

	@Override
	public void create(Cliente cliente) throws IOException {
		// TODO Auto-generated method stub
	}

	@Override
	public Cliente read(String nif) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void update(Cliente cliente) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void delete(Cliente cliente) throws IOException {
		// TODO Auto-generated method stub

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
	public List<Cliente> readAll() throws IOException {

		Connection cn = this.getConnection();
		Statement st = null;
		List<Cliente> clientes = new ArrayList();
		Cliente cliente = null;
		Vehiculo vehiculo = null;

		try {

			st = cn.createStatement();
			String sql = "SELECT * FROM CLIENTES";
			ResultSet rs = st.executeQuery(sql);

			while (rs.next()) {
				cliente = new Cliente();
				cliente.setNif(rs.getString("NIF"));
				cliente.setNombreCompleto(rs.getString("NOMBRE_COMPLETO"));
				vehiculo = new Vehiculo();
				vehiculo.setMatricula(rs.getString("MATRICULA"));
				clientes.add(cliente);
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
		return clientes;
	}
}
