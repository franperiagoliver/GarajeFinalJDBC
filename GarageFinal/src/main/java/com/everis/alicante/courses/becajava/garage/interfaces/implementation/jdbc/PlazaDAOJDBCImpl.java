package com.everis.alicante.courses.becajava.garage.interfaces.implementation.jdbc;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.text.ParseException;
import java.util.List;

import com.everis.alicante.courses.becajava.garage.domain.Cliente;
import com.everis.alicante.courses.becajava.garage.domain.Plaza;
import com.everis.alicante.courses.becajava.garage.domain.Reserva;
import com.everis.alicante.courses.becajava.garage.interfaces.dao.jdbc.PlazaDAOJDBC;

public class PlazaDAOJDBCImpl implements PlazaDAOJDBC {

	private final String MYSQL_JDBC_DRIVER = "com.mysql.jdbc.Driver";
	private final String JDBC_CADENA_CONEXION = "jdbc:mysql://localhost:3306/garaje";

	@Override
	public Connection getConnection() throws IOException {
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
	public void create(Plaza plaza) throws IOException {
		Connection cn = null;

		try {

			cn = this.getConnection();
			String sql = "INSERT INTO PLAZAS(NUM_PLAZA, PRECIO, TAMANYO, ESTADO, NIF_CLIENTE) VALUES(?, ?, ?, ?)";
			PreparedStatement pst = null;
			pst = cn.prepareStatement(sql);
			pst.setInt(1, plaza.getNumeroPlaza());
			pst.setDouble(2, plaza.getPrecio());
			pst.setInt(3, plaza.getTamanyo());
			pst.setBoolean(4, plaza.getLibre());
			Cliente cliente = new Cliente();
			pst.setString(5, cliente.getNif());
			pst.execute();

		} catch (Exception e) {
			System.out.println("Error al insertar la plaza " + e.getMessage());

		} finally {
			try {
				cn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	@Override
	public void delete(Plaza plaza) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public List<Plaza> readAll() throws IOException, ParseException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Reserva read(Integer numPlaza) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

}
