package com.everis.alicante.courses.becajava.garage.domain;

public class Plaza {

	private Cliente cliente;

	private Integer id;

	private double precio;

	private int numeroPlaza;

	private int tamanyo;

	private Boolean libre = true;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public double getPrecio() {
		return precio;
	}

	public void setPrecio(double precio) {
		this.precio = precio;
	}

	public int getNumeroPlaza() {
		return numeroPlaza;
	}

	public void setNumeroPlaza(int numeroPlaza) {
		this.numeroPlaza = numeroPlaza;
	}

	public boolean getLibre() {

		if (cliente == null) {
			libre = true;
		} else {
			libre = false;
		}
		return libre;
	}

	public int getTamanyo() {
		return tamanyo;
	}

	public void setTamanyo(int tamanyo) {
		this.tamanyo = tamanyo;
	}

	@Override
	public String toString() {
		return "Plaza [cliente=" + cliente + ", precio=" + precio + ", numeroPlaza=" + numeroPlaza + "]";
	}

}
