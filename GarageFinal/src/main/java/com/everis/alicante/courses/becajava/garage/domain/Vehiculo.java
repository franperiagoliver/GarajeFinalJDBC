package com.everis.alicante.courses.becajava.garage.domain;

public class Vehiculo {
	
	private String matricula;

	private String tipoVehiculo;
	
	private int id;
	
	public String getMatricula() {
		return matricula;
	}


	public void setMatricula(String matricula) {
		this.matricula = matricula;
	}


	public String getTipoVehiculo() {
		return tipoVehiculo;
	}


	public void setTipoVehiculo(String tipoVehiculo) {
		this.tipoVehiculo = tipoVehiculo;
	}
	
	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public String convierteAFormatoTxt(){
		
		String str="";
		
		str=str.concat("Matrícula: ");
		str=str.concat(String.valueOf(this.matricula));
		str=str.concat(". ");
		str=str.concat("Tipo de vehículo: ");
		str=str.concat(String.valueOf(this.tipoVehiculo));	
	
		
		return str;
	}
	

}
