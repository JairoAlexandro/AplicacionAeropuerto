package Clases;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

public class AeropuertoImpl implements Aeropuerto, Comparable<Object>{
	//Atributos
	private String nombre;
	private String ciudad;
	private String codigo;
	private SortedSet<Vuelo> vuelos;
	//Constructores
	public AeropuertoImpl() {
		
	}
	public AeropuertoImpl(String nombre, String ciudad, SortedSet<Vuelo> vuelos) {
		this.nombre = nombre;
		this.ciudad = ciudad;
		this.vuelos = vuelos;
	}
	public AeropuertoImpl(String nombre, String ciudad, String codigo) {
		this.nombre = nombre;
		this.ciudad = ciudad;
		this.codigo = codigo;
		this.vuelos = new TreeSet<Vuelo>();
	}
	public AeropuertoImpl(String nombre, String ciudad) {
		this.nombre = nombre;
		this.ciudad = ciudad;
		this.vuelos = new TreeSet<Vuelo>();
	}
	
	//Get y set
	public String getNombre() {
		return nombre;
	}
	public String getCiudad() {
		return ciudad;
	}
	public SortedSet<Vuelo> getVuelos(){
		return vuelos;
	}
	public String getCodigo() {
		return codigo;
	}
	
	public int compareTo(Object o) {
		int cmp = 0;
		if(o != null && o instanceof Aeropuerto) {
			Aeropuerto a = (Aeropuerto) o;
			cmp = this.getNombre().compareToIgnoreCase(a.getNombre());
			if(cmp == 0) {
				cmp = this.getCiudad().compareToIgnoreCase(a.getCiudad());
			}	
		}
		return cmp;
	}
	public String toString() {
		return this.getNombre() + "(" + this.getCiudad() + ")";
	}
	
	public void nuevoVuelo(Vuelo v) {
		if(v != null && v instanceof Vuelo) {
			this.vuelos.add(v);
		}
	}
	public void nuevosVuelos(Collection<Vuelo> vuelos) {
		if(vuelos != null && vuelos instanceof Vuelo) {
			this.vuelos.addAll(vuelos);
		}
	}
	public Boolean contieneVuelo(Vuelo v) {
		return this.vuelos.contains(v);
	}
	public void eliminaVuelo(Vuelo v) {
		if(v != null && this.vuelos.contains(v)) {
			this.vuelos.remove(v);
		}
	}
	
	public Set<Vuelo> seleccionaVuelosFecha(LocalDate fechaSalida){
		Set<Vuelo> vuelosEnFecha = new HashSet<Vuelo>();
		
		for(Vuelo v: this.vuelos) {
			if(v.getFechaSalida().toLocalDate().equals(fechaSalida)) {
				vuelosEnFecha.add(v);
			}
		}
		return vuelosEnFecha;
	}
	public Vuelo getVueloMasPasajeros() {
		Vuelo vueloMasPasajeros = new VueloImpl();
		int maxPasajeros = 0;
		for(Vuelo v: this.vuelos) {
			if (v.getNumPasajeros() > maxPasajeros) {
				vueloMasPasajeros = v;
				maxPasajeros = v.getNumPasajeros();
			} 
		}
		return vueloMasPasajeros;
	}
	public Persona getPasajeroMayor() {
		Persona personaMayor = new PersonaImpl();
		int edadMinima = 0;
		for(Vuelo v: this.vuelos) {
			for(Persona p: v.getPasajeros()) {
				if(p.getEdad() > edadMinima) {
					personaMayor = p;
					edadMinima = p.getEdad();
				}
			}
		}
		return personaMayor;
	}
	
	public Vuelo getVueloPlazasLibresDestino(String destino) {
		Vuelo vueloPlazaLibre = new VueloImpl();
		for(Vuelo v: this.vuelos) {
			if(v.getDestino() == destino && v.getCompleto().equals(false)) {
				vueloPlazaLibre = v;
				break;
			}
		}
		return vueloPlazaLibre;
	}
	public Integer calculaTotalPasajerosDestino(String destino) {
		int totalPasajeros = 0;
		for(Vuelo v: this.vuelos) {
			if(v.getDestino() == destino) {
				totalPasajeros = v.getNumPasajeros();
			}
		}
		return totalPasajeros;
		
	}
	
	public Double calculaMediaPasajerosPorDia() {
		Map<LocalDate, Integer> pasajerosDias = new Hashtable<LocalDate, Integer>();
		Set<LocalDate> claves = pasajerosDias.keySet();
		double res = 0;
		String fechaVuelo;
		String fechaAnterior;
		int pasajeros = 0;
		for(Vuelo v: this.vuelos) {
			if(v.getOrigen().equals(this.getCiudad())) {
				fechaVuelo = v.getFechaSalida().toLocalDate().toString();
				for(LocalDate clave: claves) {
					fechaAnterior = clave.toString();
					if(!fechaVuelo.equalsIgnoreCase(fechaAnterior)) {
						pasajerosDias.put(clave, v.getNumPasajeros());
						pasajeros = v.getNumPasajeros();
					}else {
						pasajeros += v.getNumPasajeros();
						pasajerosDias.put(clave, pasajeros);
					}
				}
			}
		}
		int sumaTotal = 0;
		for(int valor: pasajerosDias.values()) {
			sumaTotal += valor;
		}
		res = sumaTotal / pasajerosDias.size();
		return res;
	}
//	Devuelve una aplicación que hace
//	corresponder a cada ciudad de destino la suma del número de pasajeros de todos los
//	vuelos del aeropuerto que tengan ese destino.
	public Map<String, Integer> getNumeroPasajerosPorDestino() {
		Map<String, Integer> numPasajeros = new Hashtable<String, Integer>();
		int sumaPasajeros;
		for (Vuelo vuelos : this.vuelos) {
			String destino = vuelos.getDestino();
			int numPasajerosVuelo = vuelos.getNumPasajeros();
			
			if (numPasajeros.isEmpty() || !numPasajeros.containsKey(destino)) {
				numPasajeros.put(destino, numPasajerosVuelo);
			} else {

				sumaPasajeros = numPasajeros.get(destino) + numPasajerosVuelo;
				numPasajeros.put(destino, sumaPasajeros);

			}
		}
		return numPasajeros;
	}
	
//	Devuelve un SortedMap
//	ordenado que hace corresponder a una fecha una lista con todos los vuelos del aeropuerto
//	que salen esa fecha.
	public SortedMap<LocalDate,List<Vuelo>> getVuelosPorFecha(){
		SortedMap <LocalDate,List<Vuelo>> res = new TreeMap<LocalDate, List<Vuelo>>();
		List<Vuelo> vuelosList = new ArrayList<Vuelo>();
		for(Vuelo vuelos: this.vuelos) {
			LocalDate dateVuelos = vuelos.getFechaSalida().toLocalDate();
			if(!res.containsKey(dateVuelos)) {
				vuelosList = new ArrayList<Vuelo>();
				vuelosList.add(vuelos);
				res.put(dateVuelos, vuelosList);
			}else{
				
				res.get(dateVuelos).add(vuelos);
			}
		}
		return res;
	}
	
	
	
	
	
}
