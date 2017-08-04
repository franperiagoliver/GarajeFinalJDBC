package com.everis.alicante.courses.becajava.garage.interfaces.dao.jdbc;

import java.io.IOException;
import java.sql.Connection;
import java.util.List;

import com.everis.alicante.courses.becajava.garage.domain.Vehiculo;

public interface VehiculoDAOJDBC {

	public void create(Vehiculo vehiculo) throws IOException;

	public Vehiculo read(String matricula) throws IOException;

	public void delete(Vehiculo vehiculo) throws IOException;

	public Connection getConnection() throws IOException;

	public List<Vehiculo> readAll() throws IOException;

	void update(String matricula, Vehiculo vehiculo) throws IOException;
}
