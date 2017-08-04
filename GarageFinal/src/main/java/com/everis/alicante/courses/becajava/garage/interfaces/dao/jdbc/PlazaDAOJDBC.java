package com.everis.alicante.courses.becajava.garage.interfaces.dao.jdbc;

import java.io.IOException;
import java.sql.Connection;
import java.text.ParseException;
import java.util.List;

import com.everis.alicante.courses.becajava.garage.domain.Plaza;
import com.everis.alicante.courses.becajava.garage.domain.Reserva;

public interface PlazaDAOJDBC {

	List<Plaza> readAll() throws IOException, ParseException;

	void create(Plaza plaza) throws IOException;

	void delete(Plaza plaza) throws IOException;

	Reserva read(Integer numPlaza) throws IOException;

	Connection getConnection() throws IOException;
}
