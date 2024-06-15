package Clases;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class VueloImpl implements Vuelo {
    // Atributos

    private String codigo;
    private String origen;
    private String destino;
    private LocalDateTime fechaSalida;
    private LocalDateTime fechaLlegada;
    private Duration duracion;
    private Integer numPlazas;
    private Integer numPasajeros;
    private Boolean completo;
    private List<Persona> pasajeros;

    // Constructores
    public VueloImpl() {

    }

    public VueloImpl(String codigo, String origen, String destino, LocalDateTime fechaSalida,
	    LocalDateTime fechaLlegada, Duration duracion, Integer numPlazas, Boolean completo,
	    List<Persona> pasajeros) {
	this.codigo = codigo;
	this.origen = origen;
	this.destino = destino;
	this.fechaSalida = fechaSalida;
	this.fechaLlegada = fechaLlegada;
	this.duracion = duracion;
	this.numPlazas = numPlazas;
	this.completo = completo;
	this.pasajeros = pasajeros;
    }

    public VueloImpl(String codigo, String origen, String destino, LocalDateTime fechaSalida,
	    LocalDateTime fechaLlegada, Duration duracion, Integer numPlazas, Integer numPasajeros, Boolean completo) {
	this.codigo = codigo;
	this.origen = origen;
	this.destino = destino;
	this.fechaSalida = fechaSalida;
	this.fechaLlegada = fechaLlegada;
	this.duracion = duracion;
	this.numPlazas = numPlazas;
	this.completo = completo;
	this.pasajeros = new ArrayList<>();
	this.numPasajeros = 0;
    }

    public VueloImpl(String codigo, String origen, String destino, LocalDateTime fechaSalida,
	    LocalDateTime fechaLlegada, Integer numPlazas, Integer numPasajeros) {

	this.codigo = codigo;
	this.origen = origen;
	this.destino = destino;
	this.fechaSalida = fechaSalida;
	this.fechaLlegada = fechaLlegada;
	this.duracion = getDuracion();
	this.numPlazas = numPlazas;
	this.completo = getCompleto();
	this.pasajeros = new ArrayList<>();
	this.numPasajeros = 0;
    }

    public VueloImpl(String origen, String destino, LocalDateTime fechaSalida, LocalDateTime fechaLlegada,
	    Integer numPlazas, Integer numPasajeros) {

	this.origen = origen;
	this.destino = destino;
	this.fechaSalida = fechaSalida;
	this.fechaLlegada = fechaLlegada;
	this.duracion = getDuracion();
	this.numPlazas = numPlazas;
	this.completo = getCompleto();
	this.pasajeros = new ArrayList<>();
	this.numPasajeros = 0;
    }

    // Get y set
    public String getCodigo() {
	return codigo;
    }

    public String getOrigen() {
	return origen;
    }

    public String getDestino() {
	return destino;
    }

    public LocalDateTime getFechaSalida() {
	return fechaSalida;
    }

    public void setFechaSalida(LocalDateTime fechaSalida) {
	this.fechaSalida = fechaSalida;
    }

    public LocalDateTime getFechaLlegada() {
	return fechaLlegada;
    }

    public void setFechaLlegada(LocalDateTime fechaLlegada) {
	this.fechaLlegada = fechaLlegada;
    }

    public Duration getDuracion() {
	duracion = Duration.between(getFechaSalida(), getFechaLlegada());
	return duracion;
    }

    public Integer getNumPlazas() {
	return numPlazas;
    }

    public Integer getNumPasajeros() {
	return numPasajeros;
    }

    public Boolean getCompleto() {
	return completo(this.getNumPasajeros(), this.getNumPlazas());
    }

    public List<Persona> getPasajeros() {
	return pasajeros;
    }

    public void nuevoPasajero(Persona p) {
	if (p != null) {
	    if (this.getCompleto().equals(true)) {
		System.out.println("Esta el avion completo");
	    } else {
		pasajeros.add(p);
		this.numPasajeros++;
		this.numPlazas--;
	    }
	}

    }

    public Boolean completo(Integer a, Integer b) {
	boolean res = false;
	if (a == b) {
	    res = true;
	}
	return res;
    }

    public void eliminaPasajero(Persona p) {
	if (this.getNumPasajeros() > 0) {
	    pasajeros.remove(p);
	    this.numPasajeros--;
	    this.numPlazas++;
	} else {
	    System.out.println("Ya no quedan mas pasajeros en el avion");
	}

    }

    public String toString() {
	String fecha = this.getFechaSalida().toString().replace("T", " ");
	return "(" + this.getCodigo() + ") " + this.getOrigen() + " - " + this.getDestino() + " "
		+ fecha;
    }

}
