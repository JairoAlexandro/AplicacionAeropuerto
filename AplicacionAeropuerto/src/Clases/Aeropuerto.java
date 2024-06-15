package Clases;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;

public interface Aeropuerto  {
	public String getNombre();
	public String getCiudad();
	public String getCodigo();
	public SortedSet<Vuelo> getVuelos();
	public int compareTo(Object o);
	public String toString();
	public void nuevoVuelo(Vuelo v);
	public void nuevosVuelos(Collection<Vuelo> vuelos);
	public Boolean contieneVuelo(Vuelo v);
	public void eliminaVuelo(Vuelo v);
	public Set<Vuelo> seleccionaVuelosFecha(LocalDate fechaSalida);
	public Vuelo getVueloMasPasajeros();
	public Persona getPasajeroMayor();
	public Vuelo getVueloPlazasLibresDestino(String destino);
	public Integer calculaTotalPasajerosDestino(String destino);
	public Map<String, Integer> getNumeroPasajerosPorDestino();
	public SortedMap<LocalDate,List<Vuelo>> getVuelosPorFecha();
}
