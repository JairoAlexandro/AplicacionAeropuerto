package Clases;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public interface Vuelo {
	public String getCodigo();
	public String getOrigen();
	public String getDestino();
	public LocalDateTime getFechaSalida();
	public void setFechaSalida(LocalDateTime fechaSalida);
	public LocalDateTime getFechaLlegada();
	public void setFechaLlegada(LocalDateTime fechaLlegada);
	public Duration getDuracion();
	public Integer getNumPlazas();
	public Integer getNumPasajeros();
	public Boolean getCompleto();
	public List<Persona> getPasajeros();
	public void nuevoPasajero(Persona p);
	public void eliminaPasajero(Persona p);
	public String toString();
}
