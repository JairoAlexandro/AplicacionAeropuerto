package Clases;

import java.util.Comparator;

public class ComparatorCiudadNombre implements Comparator<Object>{
	public int compare(Object a, Object b) {
		int cmp = 0;
		if(a != null && b != null && a instanceof Aeropuerto && b instanceof Aeropuerto) {
			Aeropuerto a1 = (Aeropuerto)a;
			Aeropuerto a2 = (Aeropuerto)b;
			cmp = a1.getCiudad().compareToIgnoreCase(a2.getCiudad());
			if(cmp == 0) {
				cmp = a1.getNombre().compareToIgnoreCase(a2.getNombre());
			}
		}
		return cmp;
	}
}
