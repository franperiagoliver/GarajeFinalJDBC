package com.everis.alicante.courses.becajava.garage.controller;

import java.io.IOException;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.apache.log4j.Logger;

import com.everis.alicante.courses.becajava.garage.domain.Camion;
import com.everis.alicante.courses.becajava.garage.domain.Cliente;
import com.everis.alicante.courses.becajava.garage.domain.Coche;
import com.everis.alicante.courses.becajava.garage.domain.GarajeException;
import com.everis.alicante.courses.becajava.garage.domain.Motocicleta;
import com.everis.alicante.courses.becajava.garage.domain.Plaza;
import com.everis.alicante.courses.becajava.garage.domain.Reserva;
import com.everis.alicante.courses.becajava.garage.domain.Vehiculo;
import com.everis.alicante.courses.becajava.garage.interfaces.Aparcable;
import com.everis.alicante.courses.becajava.garage.interfaces.dao.ClienteDAO;
import com.everis.alicante.courses.becajava.garage.interfaces.dao.PlazaDAO;
import com.everis.alicante.courses.becajava.garage.interfaces.dao.ReservaDAO;
import com.everis.alicante.courses.becajava.garage.interfaces.dao.VehiculoDAO;
import com.everis.alicante.courses.becajava.garage.interfaces.dao.jdbc.ClienteDAOJDBC;
import com.everis.alicante.courses.becajava.garage.interfaces.dao.jdbc.PlazaDAOJDBC;
import com.everis.alicante.courses.becajava.garage.interfaces.dao.jdbc.VehiculoDAOJDBC;
import com.everis.alicante.courses.becajava.garage.interfaces.implementation.files.ClienteDAOFileImpl;
import com.everis.alicante.courses.becajava.garage.interfaces.implementation.files.PlazaDAOFileImp;
import com.everis.alicante.courses.becajava.garage.interfaces.implementation.files.ReservaDAOFileImp;
import com.everis.alicante.courses.becajava.garage.interfaces.implementation.files.VehiculoDAOFileImpl;
import com.everis.alicante.courses.becajava.garage.interfaces.implementation.jdbc.ClienteDAOJDBCImpl;
import com.everis.alicante.courses.becajava.garage.interfaces.implementation.jdbc.PlazaDAOJDBCImpl;
import com.everis.alicante.courses.becajava.garage.interfaces.implementation.jdbc.VehiculoDAOJDBCImpl;
import com.everis.alicante.courses.becajava.garage.utils.ValidadorNIF;

public class ControladorGarajeImpl implements ControladorGaraje {

	static Logger log = Logger.getLogger(ControladorGarajeImpl.class);

	@Override
	public Map<Integer, Plaza> listarPlazasLibres() throws GarajeException {

		Map<Integer, Plaza> plazasTotales = null;

		try {

			PlazaDAO plazaDao = new PlazaDAOFileImp();

			plazasTotales = plazaDao.readPlazas();

			ReservaDAO reservaDAO = new ReservaDAOFileImp();

			Collection<Reserva> reservas = reservaDAO.readReservas().values();

			for (Reserva reserva : reservas) {

				plazasTotales.remove(Integer.parseInt(reserva.getCodigoReserva()));

			}

		} catch (Exception e) {
			GarajeException ex = new GarajeException(e);
			throw ex;

		}

		return plazasTotales;

	}

	@Override
	public void listarPlazasOcupadas() throws GarajeException {

		try {

			ReservaDAO reservaDAO = new ReservaDAOFileImp();

			Collection<Reserva> reservas = reservaDAO.readReservas().values();

			System.out.println("PLAZAS OCUPADAS: ");

			for (Reserva reserva : reservas) {

				System.out.println("La plaza numero: " + reserva.getCodigoReserva() + " esta reservada");

			}

		} catch (Exception e) {
			GarajeException ex = new GarajeException(e);
			throw ex;
		}

	}

	@Override
	public boolean reservarPlaza() throws GarajeException {

		boolean hayplaza = false;

		try {
			// logica de crear cliente

			Cliente cliente = new Cliente();

			ReservaDAO daoReserva = new ReservaDAOFileImp();
			ClienteDAO daoCliente = new ClienteDAOFileImpl();
			VehiculoDAO daoVehiculo = new VehiculoDAOFileImpl();

			// vamos a escribir por pantalla un menu para meter los datos del cliente

			System.out.println("Inserte el nombre completo del Cliente");

			@SuppressWarnings("resource")
			Scanner in = new Scanner(System.in);
			cliente.setNombreCompleto(in.nextLine());

			boolean nifCorrecto = false;
			String nif = "";

			while (!nifCorrecto) {
				System.out.println("Inserte el nif del cliente");
				in = new Scanner(System.in);
				nif = in.nextLine();
				nifCorrecto = ValidadorNIF.validaNif(nif);
				if (nifCorrecto == false) {
					System.out.println("NIF INCORRECTO");
				}
			}
			cliente.setNif(nif);

			Vehiculo vehiculo = null;

			System.out.println("Tipo de vehiculo del propietario:");
			System.out.println("1: Coche:");
			System.out.println("2: Motocicleta");
			System.out.println("3: Camion");

			in = new Scanner(System.in);

			switch (in.nextInt()) {
			case 1:
				vehiculo = new Coche();
				break;
			case 2:
				vehiculo = new Motocicleta();
				break;
			case 3:
				vehiculo = new Camion();
				break;

			default:
				break;
			}

			System.out.println("Inserte la matricula del vehiculo:");
			in = new Scanner(System.in);
			vehiculo.setMatricula(in.nextLine());
			vehiculo.setTipoVehiculo(vehiculo.getClass().getSimpleName());

			cliente.setVehiculo(vehiculo);

			Map<Integer, Plaza> plazas = listarPlazasLibres();

			for (Plaza plaza : plazas.values()) {

				if (vehiculo instanceof Aparcable) {

					hayplaza = true;

					Reserva reserva = new Reserva();
					reserva.setCliente(cliente);
					reserva.setPlaza(plaza);
					reserva.setFechaReserva(Calendar.getInstance().getTime());
					reserva.setCodigoReserva(String.valueOf(plaza.getNumeroPlaza()));

					daoReserva.createReserva(reserva);

					daoCliente.createCliente(cliente);

					daoVehiculo.createVehiculo(vehiculo);

					return hayplaza;
				}
			}

		} catch (Exception e) {
			GarajeException ex = new GarajeException(e);
			throw ex;

		}

		return hayplaza;

	}

	@Override
	public void listarClientes() throws GarajeException {

		try {

			ClienteDAO daoCliente = new ClienteDAOFileImpl();

			Map<String, Cliente> clientes = daoCliente.readClientes();

			Collection<Cliente> collection = clientes.values();

			for (Iterator<Cliente> iterator = collection.iterator(); iterator.hasNext();) {
				Cliente cliente = iterator.next();

				System.out.println(cliente.getNombreCompleto() + ";" + cliente.getNif());

			}

		} catch (Exception e) {
			GarajeException ex = new GarajeException(e);
			throw ex;
		}

	}

	@Override
	public void listarReservas() throws GarajeException {

		try {

			ReservaDAO reservaDao = new ReservaDAOFileImp();

			Map<String, Reserva> reservas = reservaDao.readReservas();

			Collection<Reserva> listaReservas = reservas.values();

			for (Reserva reserva : listaReservas) {

				System.out.println("numero de plaza reservada: " + reserva.getPlaza().getNumeroPlaza());
				System.out.println("cliente: " + reserva.getCliente().getNombreCompleto());
				System.out.println("vehiculo: " + reserva.getCliente().getVehiculo().getMatricula() + " - "
						+ reserva.getCliente().getVehiculo().getTipoVehiculo());

			}

		} catch (Exception e) {
			GarajeException ex = new GarajeException(e);
			throw ex;
		}

	}

	@Override
	public void listarVehiculos() throws GarajeException {

		try {

			VehiculoDAO daoVehiculo = new VehiculoDAOFileImpl();

			Collection<Vehiculo> vehiculos = daoVehiculo.readVehiculos().values();

			for (Vehiculo vehiculo : vehiculos) {

				System.out.println(vehiculo.getMatricula() + "-" + vehiculo.getTipoVehiculo());

			}

		} catch (Exception e) {
			GarajeException ex = new GarajeException(e);
			throw ex;
		}

	}

	@Override
	public void listarReservasByFecha(Date fechaInicio, Date fechaFin) throws GarajeException {

		try {

			ReservaDAO reservaDAO = new ReservaDAOFileImp();

			Map<String, Reserva> reservas = reservaDAO.readReservas();

			for (Reserva reserva : reservas.values()) {

				if (reserva.getFechaReserva().before(fechaFin) &&

						reserva.getFechaReserva().after(fechaInicio)) {

					System.out.println("Reserva: " + reserva);

				}

			}

		} catch (Exception e) {
			GarajeException ex = new GarajeException(e);
			throw ex;
		}

	}

	@Override
	public void insertarVehiculo() throws GarajeException {
		// TODO Auto-generated method stub
		VehiculoDAOJDBC dao = new VehiculoDAOJDBCImpl();
		Vehiculo vehiculo = new Vehiculo();

		try {
			System.out.println("Inserte la matricula del vehiculo:");
			Scanner in = new Scanner(System.in);
			String matricula = in.nextLine();
			System.out.println("Inserte el tipo de vehículo");
			System.out.println("1 - Coche");
			System.out.println("2 - Motocicleta");
			System.out.println("3 - Camion");
			Scanner in2 = new Scanner(System.in);
			int tipo = in2.nextInt();
			switch (tipo) {
			case 1:
				vehiculo = new Coche();
				break;
			case 2:
				vehiculo = new Motocicleta();
				break;
			case 3:
				vehiculo = new Camion();
				break;
			default:
				break;
			}
			vehiculo.setId(tipo);
			vehiculo.setMatricula(matricula);
			dao.create(vehiculo);
		} catch (Exception e) {
			GarajeException ex = new GarajeException(e);
			throw ex;
		}
	}

	@Override
	public void eliminarVehiculo() throws GarajeException, Exception {
		// TODO Auto-generated method stub
		VehiculoDAOJDBC dao = new VehiculoDAOJDBCImpl();
		Vehiculo vehiculo = new Vehiculo();
		try {
			System.out.println("Dime la matrícula del coche que quieres eliminar");
			Scanner in = new Scanner(System.in);
			vehiculo.setMatricula(in.nextLine());
			dao.delete(vehiculo);

		} catch (Exception e) {
			GarajeException ex = new GarajeException(e);
			throw ex;
		}

	}

	@Override
	public void leerTipoVehiculo() throws GarajeException {
		// TODO Auto-generated method stub
		VehiculoDAOJDBC dao = new VehiculoDAOJDBCImpl();
		System.out.println("Dime la matrícula del coche");
		Scanner in = new Scanner(System.in);
		String matricula = in.nextLine();
		try {
			dao.read(matricula);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void leerTodosTiposVehiculo() throws GarajeException {
		// TODO Auto-generated method stub
		VehiculoDAOJDBC dao = new VehiculoDAOJDBCImpl();
		try {
			List<Vehiculo> vehiculos = dao.readAll();
			Iterator<Vehiculo> it = vehiculos.iterator();
			while (it.hasNext()) {
				Vehiculo vehiculo = it.next();
				System.out.println("Matrícula: " + vehiculo.getMatricula() + ". Tipo de vehículo: "
						+ vehiculo.getTipoVehiculo() + ".");
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void actualizarVehiculo() throws GarajeException {
		// TODO Auto-generated method stub
		VehiculoDAOJDBC dao = new VehiculoDAOJDBCImpl();
		Vehiculo vehiculo = new Vehiculo();

		try {
			System.out.println("Inserte la matricula del vehiculo al que quiere actualizarle la matrícula:");
			Scanner in = new Scanner(System.in);
			String matriculaAnt = in.nextLine();
			System.out.println("Inserte la nueva matrícula:");
			Scanner in2 = new Scanner(System.in);
			String matriculaNueva = in2.nextLine();
			vehiculo.setMatricula(matriculaNueva);
			dao.update(matriculaAnt, vehiculo);

		} catch (Exception e) {
			GarajeException ex = new GarajeException(e);
			throw ex;
		}
	}

	@Override
	public void crearPlaza() throws GarajeException {
		// TODO Auto-generated method stub
		PlazaDAOJDBC plazaDao = new PlazaDAOJDBCImpl();
		ClienteDAOJDBC clienteDao = new ClienteDAOJDBCImpl();
		Plaza plaza = new Plaza();
		Cliente cliente = new Cliente();

		try {
			System.out.println("Inserte el número de plaza:");
			Scanner in = new Scanner(System.in);
			Integer numPlaza = in.nextInt();
			plaza.setNumeroPlaza(numPlaza);
			System.out.println("Inserte el precio de la plaza:");
			Scanner in2 = new Scanner(System.in);
			Integer precio = in2.nextInt();
			plaza.setPrecio(precio);
			System.out.println("Inserte el tamaño de la plaza:");
			Scanner in3 = new Scanner(System.in);
			Integer tamanyo = in3.nextInt();
			plaza.setTamanyo(tamanyo);
			System.out.println("¿La plaza está asociada a algún cliente? S/N");
			Scanner in4 = new Scanner(System.in);
			String libre = in4.nextLine();

			if (libre.toUpperCase().equals("S")) {
				System.out.println("¿A qué cliente está asociada la plaza? Introduce el NIF");
				List<Cliente> clientes = clienteDao.readAll();
				Iterator<Cliente> it = clientes.iterator();
				while (it.hasNext()) {
					Cliente clienteIt = it.next();
					System.out.println("NIF: " + clienteIt.getNif() + ". NOMBRE COMPLETO: "
							+ clienteIt.getNombreCompleto() + ". VEHÍCULO: " + clienteIt.getVehiculo() + ".");
				}
				Scanner in5 = new Scanner(System.in);
				String nif = in5.nextLine();
				cliente.setNif(nif);
				plaza.setCliente(cliente);
				plazaDao.create(plaza);
			}

		} catch (Exception e) {
			GarajeException ex = new GarajeException(e);
			throw ex;
		}
	}

}
