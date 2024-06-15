package Clases;

import java.awt.GridBagLayout;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.sql.Statement;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class AeropuertoBBDD {
	static String nombrePuerto = "";

	public static Connection getConnection() throws SQLException {
		String url = "jdbc:mysql://localhost/" + nombrePuerto;
		String user = "root";
		String password = "";
		return DriverManager.getConnection(url, user, password);
	}

	public void crearBaseDatos() {
		try {
			Connection cnx = getConnection();
			Statement stm = cnx.createStatement();
			ResultSet rs = stm.executeQuery("SHOW DATABASES LIKE 'aeropuertos'");
			if (rs.next()) {
				nombrePuerto = "aeropuertos";
			} else {
				stm.addBatch("CREATE DATABASE aeropuertos;");
				stm.addBatch("USE aeropuertos");
				stm.executeBatch();
				stm.close();
				nombrePuerto = "aeropuertos";
				creatTablas();

			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public void creatTablas() {
		try {
			Connection cnx = getConnection();
			Statement stm = cnx.createStatement();
			stm.addBatch(
					"CREATE TABLE aeropuertos (nombre VARCHAR(100) NOT NULL, ciudad VARCHAR(100) NOT NULL, cod_Aero INT AUTO_INCREMENT NOT NULL PRIMARY KEY);");
			stm.addBatch(
					"CREATE TABLE vuelos (codigo INT AUTO_INCREMENT PRIMARY KEY, origen VARCHAR(255) NOT NULL, destino VARCHAR(255) NOT NULL, num_plazas INT NOT NULL, num_pasajeros INT NOT NULL, completo TINYINT(1) NOT NULL, fecha_salida VARCHAR(255) NOT NULL, fecha_llegada VARCHAR(255) NOT NULL, duracion VARCHAR(255) NOT NULL);");
			stm.addBatch(
					"CREATE TABLE aeropuertos_vuelos (id INT AUTO_INCREMENT PRIMARY KEY, cod_Aero INT NOT NULL, codigo_vuelo INT NOT NULL, FOREIGN KEY (cod_Aero) REFERENCES aeropuertos(cod_Aero), FOREIGN KEY (codigo_vuelo) REFERENCES vuelos(codigo));");
			stm.addBatch(
					"CREATE TABLE escalas (id INT AUTO_INCREMENT PRIMARY KEY, vuelo_codigo INT NOT NULL, aeropuerto_codigo INT NOT NULL, fecha_llegada VARCHAR(255) NOT NULL, fecha_salida VARCHAR(255) NOT NULL, num_pasajeros INT NOT NULL, completo TINYINT(1) NOT NULL, FOREIGN KEY (vuelo_codigo) REFERENCES vuelos(codigo), FOREIGN KEY (aeropuerto_codigo) REFERENCES aeropuertos(cod_Aero));");
			stm.addBatch(
					"CREATE TABLE pasajeros (dni VARCHAR(20) NOT NULL PRIMARY KEY, nombre VARCHAR(100) NOT NULL, edad INT NOT NULL, sexo CHAR(1) NOT NULL, peso FLOAT NOT NULL, altura FLOAT NOT NULL);");
			stm.addBatch(
					"CREATE TABLE pasajero_vuelos (id INT AUTO_INCREMENT PRIMARY KEY, dni VARCHAR(20) NOT NULL, codigo_vuelo INT NOT NULL, FOREIGN KEY (dni) REFERENCES pasajeros(dni), FOREIGN KEY (codigo_vuelo) REFERENCES vuelos(codigo));");
			stm.executeBatch();
			stm.close();

		} catch (SQLException e) {

			e.printStackTrace();
		}
	}

	// Añadir Aeropuerto
	public void addAeropuerto(Aeropuerto aeropuerto) {
		JPanel formPanel = new JPanel(new GridBagLayout());
		try {

			Connection cnx = getConnection();
			PreparedStatement psAeropuerto = cnx
					.prepareStatement("INSERT INTO aeropuertos (nombre, ciudad) VALUES (?, ?)");
			psAeropuerto.setString(1, aeropuerto.getNombre());
			psAeropuerto.setString(2, aeropuerto.getCiudad());
			psAeropuerto.executeUpdate();
			JOptionPane.showMessageDialog(formPanel, "Aeropuerto insertado correctamente.");
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(formPanel, "Error en insertar Aeropuerto");
			e.printStackTrace();
		}
	}

	// Eliminar Aeropuertos
	public void eliminarAeropuerto(String nombre) {
		JPanel formPanel = new JPanel(new GridBagLayout());
		Savepoint punto = null;
		Connection cnx = null;
		try {
			cnx = getConnection();
			cnx.setAutoCommit(false);

			// Obtenemos el código del aeropuerto
			PreparedStatement psAero = cnx.prepareStatement("SELECT cod_Aero FROM aeropuertos WHERE nombre = ?");
			psAero.setString(1, nombre);
			ResultSet rs = psAero.executeQuery();
			Integer codigoAeropuerto = 0;
			if (rs.next()) {
				codigoAeropuerto = rs.getInt("cod_Aero");
			} else {
				JOptionPane.showMessageDialog(formPanel, "No existe ese Aeropuerto");
				return;
			}

			// Obtenemos los códigos de vuelo relacionados con el código del aeropuerto
			PreparedStatement codigosVuelos = cnx
					.prepareStatement("SELECT codigo_vuelo FROM aeropuertos_vuelos WHERE cod_Aero = ?");
			codigosVuelos.setInt(1, codigoAeropuerto);
			ResultSet rsCodigos = codigosVuelos.executeQuery();
			List<Integer> codigosVuelosList = new ArrayList<>();
			while (rsCodigos.next()) {
				codigosVuelosList.add(rsCodigos.getInt("codigo_vuelo"));
			}

			Integer[] codigosVuelosArray = codigosVuelosList.toArray(new Integer[0]);

			// Obtenemos los dni de los pasajeros mediante el código del aeropuerto
			PreparedStatement dniPasajeros = cnx.prepareStatement(
					"SELECT DISTINCT p.dni FROM pasajeros p JOIN pasajero_vuelos pv ON p.dni = pv.dni "
							+ "JOIN vuelos v ON pv.codigo_vuelo = v.codigo JOIN aeropuertos_vuelos av ON v.codigo = av.codigo_vuelo "
							+ "WHERE av.cod_Aero = ?");
			dniPasajeros.setInt(1, codigoAeropuerto);
			ResultSet rsDNI = dniPasajeros.executeQuery();
			List<String> dniPasajerosList = new ArrayList<>();
			while (rsDNI.next()) {
				dniPasajerosList.add(rsDNI.getString("dni"));
			}
			punto = cnx.setSavepoint("muyayo");

			String[] dniPasajerosArray = dniPasajerosList.toArray(new String[0]);

			// Eliminamos las tablas relacionales
			PreparedStatement psEliminarPasajeroVuelo = cnx.prepareStatement(
					"DELETE FROM pasajero_vuelos WHERE codigo_vuelo IN (SELECT codigo_vuelo FROM aeropuertos_vuelos WHERE cod_Aero = ?)");
			psEliminarPasajeroVuelo.setInt(1, codigoAeropuerto);
			psEliminarPasajeroVuelo.addBatch();

			// Eliminamos pasajeros mediante los DNI obtenidos
			PreparedStatement psEliminarPasajeros = cnx.prepareStatement("DELETE FROM pasajeros WHERE dni = ?");
			for (String dni : dniPasajerosArray) {
				psEliminarPasajeros.setString(1, dni);
				psEliminarPasajeros.addBatch();
			}

			// Eliminamos escalas
			PreparedStatement psEliminarEscalas = cnx.prepareStatement(
					"DELETE FROM escalas WHERE vuelo_codigo IN (SELECT codigo_vuelo FROM aeropuertos_vuelos WHERE cod_Aero = ?) OR aeropuerto_codigo = ?");
			psEliminarEscalas.setInt(1, codigoAeropuerto);
			psEliminarEscalas.setInt(2, codigoAeropuerto);
			psEliminarEscalas.addBatch();

			// Eliminamos la relación aeropuertos_vuelos
			PreparedStatement psEliminarAeropuertoVuelos = cnx
					.prepareStatement("DELETE FROM aeropuertos_vuelos WHERE cod_Aero = ?");
			psEliminarAeropuertoVuelos.setInt(1, codigoAeropuerto);
			psEliminarAeropuertoVuelos.addBatch();

			// Eliminamos los vuelos relacionados con el código del aeropuerto
			PreparedStatement psEliminarVuelos = cnx.prepareStatement("DELETE FROM vuelos WHERE codigo = ?");
			for (Integer codigoVuelo : codigosVuelosArray) {
				psEliminarVuelos.setInt(1, codigoVuelo);
				psEliminarVuelos.addBatch();
			}

			// Finalmente, eliminamos el aeropuerto
			PreparedStatement psEliminarAeropuertos = cnx
					.prepareStatement("DELETE FROM aeropuertos WHERE cod_Aero = ?");
			psEliminarAeropuertos.setInt(1, codigoAeropuerto);
			psEliminarAeropuertos.addBatch();

			psEliminarPasajeroVuelo.executeBatch();
			psEliminarPasajeros.executeBatch();
			psEliminarEscalas.executeBatch();
			psEliminarAeropuertoVuelos.executeBatch();
			psEliminarVuelos.executeBatch();
			psEliminarAeropuertos.executeBatch();

			psAero.close();
			codigosVuelos.close();
			dniPasajeros.close();
			rs.close();
			rsCodigos.close();
			rsDNI.close();
			psEliminarPasajeroVuelo.close();
			psEliminarPasajeros.close();
			psEliminarEscalas.close();
			psEliminarAeropuertoVuelos.close();
			psEliminarVuelos.close();
			psEliminarAeropuertos.close();
			cnx.commit();

			JOptionPane.showMessageDialog(formPanel, "Aeropuerto eliminado");

		} catch (SQLException e) {
			if(cnx != null) {
				try {
					if(punto != null) {
						cnx.rollback(punto);
						cnx.commit();
					}else {
						cnx.rollback(); 
					}
				} catch (Exception e2) {
					
				}
			}
			e.printStackTrace();
			JOptionPane.showMessageDialog(formPanel, "Error eliminando el Aeropuerto");
		}
	}

	// Buscar Aeropuerto
	public Aeropuerto buscaAeropuerto(String nombre) {
		Aeropuerto aer = null;
		try {
			Connection cnx = getConnection();
			PreparedStatement psAeropuerto = cnx.prepareStatement("SELECT * from aeropuertos WHERE nombre = ?");
			psAeropuerto.setString(1, nombre);
			ResultSet rs = psAeropuerto.executeQuery();
			if (rs.next()) {
				String nomAer = rs.getString("nombre");
				String ciuAer = rs.getString("ciudad");
				String codAer = rs.getString("cod_Aero");
				aer = new AeropuertoImpl(nomAer, ciuAer, codAer);
			}
			rs.close();
			psAeropuerto.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return aer;
	}

	// Buscar Aeropuerto por ciudad
	public Boolean existeAeropuerto(String ciudad) {
		Boolean existe = false;
		try {
			Connection cnx = getConnection();
			PreparedStatement psAeropuerto = cnx.prepareStatement("SELECT * FROM aeropuertos WHERE ciudad = ?");
			psAeropuerto.setString(1, ciudad);
			ResultSet rs = psAeropuerto.executeQuery();
			if (rs.next()) {
				existe = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return existe;
	}

	// Añadir aeropuerto_vuelos
	public void addAeropuertoVuelos(Integer codigoVuelo, Integer codigoAero) {
		try {
			Connection cnx = getConnection();
			PreparedStatement psAeroVuelo = cnx
					.prepareStatement("INSERT INTO aeropuertos_vuelos (cod_Aero, codigo_vuelo) VALUES (?,?)");
			psAeroVuelo.setInt(1, codigoAero);
			psAeroVuelo.setInt(2, codigoVuelo);
			psAeroVuelo.executeUpdate();
			psAeroVuelo.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// Buscar codigo Aeropuerto mediante ciudad
	public Integer codigoAeropuerto(String ciudad, String nombre) {

		Integer codigo = null;
		try {
			Connection cnx = getConnection();
			PreparedStatement psAeropuerto = cnx.prepareStatement("SELECT cod_Aero FROM aeropuertos WHERE ciudad = ?");
			psAeropuerto.setString(1, ciudad);
			ResultSet rs = psAeropuerto.executeQuery();
			while (rs.next()) {

				PreparedStatement psAeropuerto2 = cnx
						.prepareStatement("SELECT * FROM aeropuertos WHERE ciudad = ? AND nombre = ?");
				psAeropuerto2.setString(1, ciudad);
				psAeropuerto2.setString(2, nombre);
				ResultSet rs2 = psAeropuerto2.executeQuery();
				if (rs2.next()) {
					codigo = rs2.getInt("cod_Aero");
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return codigo;
	}

	// Añadir vuelo
	public void addVuelo(Vuelo vuelo, String nombre) {
		JPanel formPanel = new JPanel(new GridBagLayout());
		try {
			Connection cnx = getConnection();
			cnx.setAutoCommit(false);
			PreparedStatement psVuelo = cnx.prepareStatement("INSERT INTO vuelos"
					+ " (origen, destino,num_plazas, num_pasajeros, completo, fecha_salida, fecha_llegada, duracion) VALUES (?,?,?,?,?,?,?,?)",
					Statement.RETURN_GENERATED_KEYS);

			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
			if (existeAeropuerto(vuelo.getOrigen()) == false || existeAeropuerto(vuelo.getDestino()) == false) {
				System.out.println("Origen o Destino no existen como Aeropuertos");
				cnx.rollback();
				return;
			}
			psVuelo.setString(1, vuelo.getOrigen());
			psVuelo.setString(2, vuelo.getDestino());
			psVuelo.setInt(3, vuelo.getNumPlazas());
			psVuelo.setInt(4, vuelo.getNumPasajeros());
			psVuelo.setBoolean(5, vuelo.getCompleto());
			psVuelo.setString(6, vuelo.getFechaSalida().format(formatter));
			psVuelo.setString(7, vuelo.getFechaLlegada().format(formatter));
			psVuelo.setString(8, vuelo.getDuracion().toString());
			psVuelo.executeUpdate();

			ResultSet codigoGenerado = psVuelo.getGeneratedKeys();
			Integer codigoVuelo = null;
			Integer codigoAero = null;
			if (codigoGenerado.next()) {
				codigoVuelo = codigoGenerado.getInt(1);
				codigoAero = codigoAeropuerto(vuelo.getOrigen(), nombre);

			}
			cnx.commit();
			JOptionPane.showMessageDialog(formPanel, "Vuelo añadido correctamente.");
			addAeropuertoVuelos(codigoVuelo, codigoAero);

		} catch (SQLException e) {
			JOptionPane.showMessageDialog(formPanel, "Error insertando Vuelo.");
			e.printStackTrace();
		}
	}

	// Eliminar Vuelo
	public void eliminarVueloCodigo(Integer codigo) {
		JPanel formPanel = new JPanel(new GridBagLayout());
		try {
			Connection cnx = getConnection();
			cnx.setAutoCommit(false);

			// Cogemos los dni de los pasajeros mediante el codigo de Aeropuerto
			PreparedStatement dniPasajeros = cnx.prepareStatement(
					"SELECT DISTINCT p.dni FROM pasajeros p JOIN pasajero_vuelos pv ON p.dni = pv.dni WHERE pv.codigo_vuelo = ?");
			dniPasajeros.setInt(1, codigo);
			ResultSet rsDNI = dniPasajeros.executeQuery();
			List<String> dniPasajerosList = new ArrayList<>();
			while (rsDNI.next()) {
				dniPasajerosList.add(rsDNI.getString("dni"));
			}

			String[] dniPasajerosArray = dniPasajerosList.toArray(new String[0]);

			// Eliminamos tablas relacionales
			PreparedStatement psEliminarPasajeroVuelo = cnx
					.prepareStatement("DELETE FROM pasajero_vuelos WHERE codigo_vuelo = ?");
			psEliminarPasajeroVuelo.setInt(1, codigo);
			psEliminarPasajeroVuelo.addBatch();

			PreparedStatement psEliminarPasajeros = cnx.prepareStatement("DELETE FROM pasajeros WHERE dni = ?");
			for (String dni : dniPasajerosArray) {
				psEliminarPasajeros.setString(1, dni);
				psEliminarPasajeros.addBatch();
			}

			PreparedStatement psEliminarEscalas = cnx.prepareStatement("DELETE FROM escalas WHERE vuelo_codigo = ?");
			psEliminarEscalas.setInt(1, codigo);
			psEliminarEscalas.addBatch();

			PreparedStatement psEliminarAeropuertoVuelos = cnx
					.prepareStatement("DELETE FROM aeropuertos_vuelos WHERE codigo_vuelo = ?");
			psEliminarAeropuertoVuelos.setInt(1, codigo);
			psEliminarAeropuertoVuelos.addBatch();

			// Eliminamos los vuelos que tenian la relacionm con el codigo Aeropuerto
			PreparedStatement psEliminarVuelos = cnx.prepareStatement("DELETE FROM vuelos WHERE codigo = ?");
			psEliminarVuelos.setInt(1, codigo);
			psEliminarVuelos.addBatch();

			psEliminarPasajeroVuelo.executeBatch();
			psEliminarPasajeros.executeBatch();
			psEliminarEscalas.executeBatch();
			psEliminarAeropuertoVuelos.executeBatch();
			psEliminarVuelos.executeBatch();

			dniPasajeros.close();
			psEliminarPasajeroVuelo.close();
			psEliminarPasajeros.close();
			psEliminarEscalas.close();
			psEliminarAeropuertoVuelos.close();
			psEliminarVuelos.close();

			cnx.commit();
			JOptionPane.showMessageDialog(formPanel, "Vuelo eliminado");

		} catch (SQLException e) {
			JOptionPane.showMessageDialog(formPanel, "Error eliminando vuelo");
			e.printStackTrace();
		}

	}

	// Eliminar vuelo
	// Tranformador de fecha String a fecha de verdad
	public LocalDateTime tranformaStringFecha(String fecha) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		LocalDateTime ldt = LocalDateTime.parse(fecha, formatter);
		return ldt;
	}

	// Modificaciones Vuelo
	public void modificarFechaSalida(String codigoVue, String fechaSa) {
		JPanel formPanel = new JPanel(new GridBagLayout());
		
		try {
			Connection cnx = getConnection();
			PreparedStatement psVuelo = cnx.prepareStatement("UPDATE vuelos SET fecha_salida = ? WHERE codigo = ?");
			psVuelo.setString(1, fechaSa);
			psVuelo.setString(2, codigoVue);
			psVuelo.executeUpdate();
			JOptionPane.showMessageDialog(formPanel, "Fecha de salida del vuelo actualizada");
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(formPanel, "Error actualizando la Fecha de salida");
			e.printStackTrace();
		}
	}

	// Buscar Vuelo por codigo
	public ResultSet seleccionarVueloCodigo(Integer codigo) {
		ResultSet rs = null;
		try {
			Connection cnx = getConnection();
			PreparedStatement psVuelo = cnx.prepareStatement("SELECT * FROM vuelos WHERE codigo = ?");
			psVuelo.setInt(1, codigo);
			rs = psVuelo.executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rs;
	}

	public Vuelo buscarVueloCodigo(Integer codigo) {
		Vuelo vuelo = null;
		try {
			ResultSet rs = seleccionarVueloCodigo(codigo);
			if (rs.next()) {
				String origen = rs.getString("origen");
				String destino = rs.getString("destino");
				Integer numPlazas = rs.getInt("num_plazas");
				Integer numPasajeros = rs.getInt("num_pasajeros");
				Boolean completo = rs.getBoolean("completo");
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
				LocalDateTime fechaSalida = LocalDateTime.parse(rs.getString("fecha_salida"), formatter);
				LocalDateTime fechaLlegada = LocalDateTime.parse(rs.getString("fecha_llegada"), formatter);
				Duration duracion = Duration.parse(rs.getString("duracion"));

				vuelo = new VueloImpl(codigo.toString(), origen, destino, fechaSalida, fechaLlegada, duracion,
						numPlazas, numPasajeros, completo);

			} else {

				return vuelo;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return vuelo;
	}

	// Buscar Vuelo por codigo
	public ResultSet seleccionarVueloOrigen(String origen) {
		ResultSet rs = null;
		try {
			Connection cnx = getConnection();
			PreparedStatement psVuelo = cnx.prepareStatement("SELECT * FROM vuelos WHERE origen = ?");
			psVuelo.setString(1, origen);
			rs = psVuelo.executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rs;
	}

	public Integer[] buscarVueloOrigen(String origen) {
		Integer[] codigos = null;
		try {
			ResultSet rs = seleccionarVueloOrigen(origen);
			List<Integer> codigosVuelos = new ArrayList<Integer>();
			boolean hay = false;
			while (rs.next()) {
				Integer codigoVuelto = rs.getInt("codigo");
				codigosVuelos.add(codigoVuelto);
				hay = true;
			}
			if (hay != true) {
				System.out.println("No hay vuelos con ese origen");
				return codigos;
			}
			codigos = codigosVuelos.toArray(new Integer[0]);

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return codigos;
	}

	public Vuelo[] buscarVueloOrigenMenu(String origen) {
		JPanel formPanel = new JPanel(new GridBagLayout());
		Vuelo vuelo = null;
		Vuelo[] vuelosArray = null;
		try {
			ResultSet rs = seleccionarVueloOrigen(origen);
			List<Vuelo> vuelos = new ArrayList<Vuelo>();
			while (rs.next()) {
				Integer codigo = rs.getInt("codigo");
				String destino = rs.getString("destino");
				Integer numPlazas = rs.getInt("num_plazas");
				Integer numPasajeros = rs.getInt("num_pasajeros");
				Boolean completo = rs.getBoolean("completo");
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
				LocalDateTime fechaSalida = LocalDateTime.parse(rs.getString("fecha_salida"), formatter);
				LocalDateTime fechaLlegada = LocalDateTime.parse(rs.getString("fecha_llegada"), formatter);
				Duration duracion = Duration.parse(rs.getString("duracion"));

				vuelo = new VueloImpl(String.valueOf(codigo), origen, destino, fechaSalida, fechaLlegada, duracion,
						numPlazas, numPasajeros, completo);
				vuelos.add(vuelo);
				vuelosArray = vuelos.toArray(new Vuelo[0]);
			}

		} catch (SQLException e) {
			JOptionPane.showMessageDialog(formPanel, "Error buscando el vuelo");
			e.printStackTrace();
		}
		if (vuelo == null) {
			JOptionPane.showMessageDialog(formPanel, "No existe ningun vuelo con ese Origen");
		}
		return vuelosArray;
	}

	// Añadir Pasajeros
	public void addPasajeros(Persona pasajeros, Vuelo vuelo) {
		JPanel formPanel = new JPanel(new GridBagLayout());
		try {
			Connection cnx = getConnection();
			cnx.setAutoCommit(false);
			PreparedStatement psPersonas = cnx.prepareStatement(
					"INSERT INTO pasajeros " + "(dNI, nombre, edad, sexo, peso, altura) VALUES (?,?,?,?,?,?)");
			psPersonas.setString(1, pasajeros.getDNI());
			psPersonas.setString(2, pasajeros.getNombre());
			psPersonas.setInt(3, pasajeros.getEdad());
			psPersonas.setString(4, pasajeros.getSexo().toString());
			psPersonas.setDouble(5, pasajeros.getPeso());
			psPersonas.setDouble(6, pasajeros.getAltura());
			psPersonas.executeUpdate();

			Integer codigoVuelo = Integer.valueOf(vuelo.getCodigo());
			PreparedStatement psVuelo_Pasajero = cnx
					.prepareStatement("INSERT INTO pasajero_vuelos (dni, codigo_vuelo) VALUES (?,?)");
			psVuelo_Pasajero.setString(1, pasajeros.getDNI());
			psVuelo_Pasajero.setInt(2, codigoVuelo);
			psVuelo_Pasajero.executeUpdate();
			PreparedStatement psSumaResta = cnx.prepareStatement(
					"UPDATE vuelos SET num_pasajeros = num_pasajeros + 1, num_plazas = num_plazas - 1 WHERE codigo = ?");
			psSumaResta.setInt(1, codigoVuelo);
			psSumaResta.executeUpdate();
			PreparedStatement psSumaRestaInter = cnx.prepareStatement(
					"UPDATE escalas SET num_pasajeros = num_pasajeros + 1 WHERE vuelo_codigo = ?");
			psSumaRestaInter.setInt(1, codigoVuelo);
			psSumaRestaInter.executeUpdate();
			cnx.commit();
			JOptionPane.showMessageDialog(formPanel, "Pasajero insertado correctamente");
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(formPanel, "Error insertando pasajero");
			e.printStackTrace();
		}

	}
	// Añadir Pasajeros a vuelo
		public void addPasajeroVuelo(String DNI, Vuelo vuelo) {
			JPanel formPanel = new JPanel(new GridBagLayout());
			try {
				Connection cnx = getConnection();
				cnx.setAutoCommit(false);
				Integer codigoVuelo = Integer.valueOf(vuelo.getCodigo());
				PreparedStatement psVuelo_Pasajero = cnx
						.prepareStatement("INSERT INTO pasajero_vuelos (dni, codigo_vuelo) VALUES (?,?)");
				psVuelo_Pasajero.setString(1, DNI);
				psVuelo_Pasajero.setInt(2, codigoVuelo);
				psVuelo_Pasajero.executeUpdate();
				PreparedStatement psSumaResta = cnx.prepareStatement(
						"UPDATE vuelos SET num_pasajeros = num_pasajeros + 1, num_plazas = num_plazas - 1 WHERE codigo = ?");
				psSumaResta.setInt(1, codigoVuelo);
				psSumaResta.executeUpdate();
				PreparedStatement psSumaRestaInter = cnx.prepareStatement(
						"UPDATE escalas SET num_pasajeros = num_pasajeros + 1 WHERE vuelo_codigo = ?");
				psSumaRestaInter.setInt(1, codigoVuelo);
				psSumaRestaInter.executeUpdate();
				cnx.commit();
				JOptionPane.showMessageDialog(formPanel, "Pasajero insertado correctamente");
			} catch (SQLException e) {
				JOptionPane.showMessageDialog(formPanel, "Error insertando pasajero");
				e.printStackTrace();
			}

		}

	// Eliminar Pasajero
	public void eliminarPasajero(String dni) {
		JPanel formPanel = new JPanel(new GridBagLayout());
		try {
			Connection cnx = getConnection();
			cnx.setAutoCommit(false);
			PreparedStatement psVuelo = cnx.prepareStatement("SELECT codigo_vuelo FROM pasajero_vuelos WHERE dni = ?");
			psVuelo.setString(1, dni);
			ResultSet rs = psVuelo.executeQuery();
			List<Integer> codigos = new ArrayList<Integer>();
			while (rs.next()) {
				Integer codigoVuelo = rs.getInt("codigo_vuelo");
				codigos.add(codigoVuelo);
			}

			Integer[] codigosArrays = codigos.toArray(new Integer[0]);
			PreparedStatement psRestaSuma = cnx.prepareStatement(
					"UPDATE vuelos SET num_pasajeros = num_pasajeros - 1, num_plazas = num_plazas + 1 WHERE codigo = ?");
			PreparedStatement psSumaRestaInter = cnx.prepareStatement(
					"UPDATE escalas SET num_pasajeros = num_pasajeros - 1 WHERE vuelo_codigo = ?");
			
			for (Integer codigo : codigosArrays) {
				psRestaSuma.setInt(1, codigo);
				psSumaRestaInter.setInt(1, codigo);
				psRestaSuma.addBatch();
				psSumaRestaInter.addBatch();
			}
			psRestaSuma.executeBatch();
			psSumaRestaInter.executeBatch();
			PreparedStatement psPersona_Vuelos = cnx.prepareStatement("DELETE FROM pasajero_vuelos WHERE dni = ?");
			psPersona_Vuelos.setString(1, dni);
			psPersona_Vuelos.executeUpdate();
			psPersona_Vuelos.close();
			PreparedStatement psPasajero = cnx.prepareStatement("DELETE FROM pasajeros WHERE dni = ?");
			psPasajero.setString(1, dni);
			psPasajero.executeUpdate();
			psPasajero.close();
			cnx.commit();
			JOptionPane.showMessageDialog(formPanel, "Pasajero eliminado correctamente");
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(formPanel, "Error eliminando pasajero");
			e.printStackTrace();
		}
	}
	// Modificar Pasajero

	public void modificarPasajero(Persona pasajero) {
		JPanel formPanel = new JPanel(new GridBagLayout());
		try {
			Connection cnx = getConnection();
			PreparedStatement psPasajero = cnx.prepareStatement(
					"UPDATE pasajeros SET nombre = ?, edad = ?, sexo = ?, peso = ?, altura = ? WHERE dni = ?");
			psPasajero.setString(1, pasajero.getNombre());
			psPasajero.setInt(2, pasajero.getEdad());
			psPasajero.setString(3, pasajero.getSexo().toString());
			psPasajero.setDouble(4, pasajero.getPeso());
			psPasajero.setDouble(5, pasajero.getAltura());
			psPasajero.setString(6, pasajero.getDNI());
			psPasajero.executeUpdate();
			psPasajero.close();
			JOptionPane.showMessageDialog(formPanel, "Pasajero modificado correctamente");
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(formPanel, "Error modificando pasajero");
			e.printStackTrace();
		}
	}

	// Buscar pasajero
	public Persona buscarPasajeroMenu(String dni) {
		Persona pasajero = null;
		try {
			Connection cnx = getConnection();
			PreparedStatement psPasa = cnx.prepareStatement("SELECT * FROM pasajeros WHERE dni = ?");
			psPasa.setString(1, dni);
			ResultSet rs = psPasa.executeQuery();
			if (rs.next()) {
				String nombre = rs.getString("nombre");
				Integer edad = rs.getInt("edad");
				String sexoS = rs.getString("sexo");
				char sexo = sexoS.charAt(0);
				Double peso = rs.getDouble("peso");
				Double altura = rs.getDouble("altura");
				pasajero = new PersonaImpl(nombre, edad, dni, sexo, peso, altura);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return pasajero;
	}

	// Añadir Escala
	public void addEscala(Vuelo vuelo, String nombre) {
		JPanel formPanel = new JPanel(new GridBagLayout());
		Integer vueloCodigo = 0;
		Integer cod_Aero = 0;
		String fechaLlegada = "";
		String fechaSalida = "";
		Integer numeroPasajeros = 0;
		Boolean completo = false;
		try {

			Connection cnx = getConnection();
			cnx.setAutoCommit(false);
			PreparedStatement psAeroCodigo = cnx.prepareStatement("SELECT cod_Aero FROM aeropuertos WHERE nombre = ?");
			psAeroCodigo.setString(1, nombre);
			ResultSet rs = psAeroCodigo.executeQuery();
			if (rs.next()) {
				cod_Aero = rs.getInt("cod_Aero");
			}
			vueloCodigo = Integer.valueOf(vuelo.getCodigo());
			fechaLlegada = vuelo.getFechaLlegada().toString().replace("T", " ");
			fechaSalida = vuelo.getFechaSalida().toString().replace("T", " ");
			completo = vuelo.getCompleto();
			numeroPasajeros = vuelo.getNumPasajeros();
			PreparedStatement psEscala = cnx.prepareStatement(
					"INSERT INTO escalas (vuelo_codigo, aeropuerto_codigo, fecha_llegada, fecha_salida, num_pasajeros, completo) VALUES (?,?,?,?,?,?)");
			psEscala.setInt(1, vueloCodigo);
			psEscala.setInt(2, cod_Aero);
			psEscala.setString(3, fechaLlegada);
			psEscala.setString(4, fechaSalida);
			psEscala.setInt(5, numeroPasajeros);
			psEscala.setBoolean(6, completo);
			psEscala.executeUpdate();
			cnx.commit();
			JOptionPane.showMessageDialog(formPanel, "Escala insertada");

		} catch (SQLException e) {
			JOptionPane.showMessageDialog(formPanel, "Error insertando la Escala");
			e.printStackTrace();
		}

	}

	// SELECCIONAR CODIGO AEROPUERTO MEDIANTE NOMBRE
	public Vuelo[] codigoAeropuerto_Vuelos(String nombre) {
		Vuelo[] vuelo = null;
		Integer cod_Aero = null;
		try {
			List<Vuelo> vuelos = new ArrayList<Vuelo>();
			Connection cnx = getConnection();
			PreparedStatement psAeropuerto = cnx.prepareStatement("SELECT * FROM aeropuertos WHERE nombre = ?");
			psAeropuerto.setString(1, nombre);
			ResultSet rs = psAeropuerto.executeQuery();
			if (rs.next()) {
				cod_Aero = rs.getInt("cod_Aero");
				Integer[] codigo_vuelos = codigosVuelos_Pasajeros(cod_Aero);
				for (Integer codigos : codigo_vuelos) {
					vuelos.add(vuelosElegir(codigos));
				}
			}
			vuelo = vuelos.toArray(new Vuelo[0]);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return vuelo;

	}

	// Eliminar Escala
	public void eliminarEscala(Integer cod_Vuelo, Integer cod_Aero) {
		try {
			Connection cnx = getConnection();
			PreparedStatement psEscala = cnx
					.prepareStatement("DELETE FROM escalas WHERE vuelo_codigo = ? AND aeropuerto_codigo = ?");
			psEscala.setInt(2, cod_Vuelo);
			psEscala.setInt(3, cod_Aero);
			psEscala.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	// Lista de vuelos

	public Vuelo vuelosElegir(Integer codigo) {
		Vuelo vuelo = null;
		try {
			Connection cnx = getConnection();
			PreparedStatement psVuelos = cnx.prepareStatement("SELECT * FROM vuelos WHERE codigo = ?");
			psVuelos.setInt(1, codigo);
			ResultSet rs = psVuelos.executeQuery();
			if (rs.next()) {
				String origen = rs.getString("origen");
				String destino = rs.getString("destino");
				String fechaSalida = rs.getString("fecha_salida");
				LocalDateTime lFechaSalida = tranformaStringFecha(fechaSalida);
				String fechaLlegada = rs.getString("fecha_llegada");
				LocalDateTime lFechaLlegada = tranformaStringFecha(fechaLlegada);
				Integer numPlazas = rs.getInt("num_plazas");
				Integer numPasajeros = rs.getInt("num_pasajeros");
				vuelo = new VueloImpl(String.valueOf(codigo), origen, destino, lFechaSalida, lFechaLlegada, numPlazas,
						numPasajeros);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return vuelo;
	}

	// Lista vuelos por origen y destino
	public Vuelo[] vuelosOrigenDestino(String origen, String destino) {
		Vuelo vuelo = null;
		List<Vuelo> listVuelos = new ArrayList<Vuelo>();
		try {

			Connection cnx = getConnection();
			PreparedStatement psVuelos = cnx.prepareStatement("SELECT * FROM vuelos WHERE origen = ? AND destino = ?");
			psVuelos.setString(1, origen);
			psVuelos.setString(2, destino);
			ResultSet rs = psVuelos.executeQuery();
			while (rs.next()) {
				String codigo = rs.getString("codigo");
				String fechaSalida = rs.getString("fecha_salida");
				LocalDateTime lFechaSalida = tranformaStringFecha(fechaSalida);
				String fechaLlegada = rs.getString("fecha_llegada");
				LocalDateTime lFechaLlegada = tranformaStringFecha(fechaLlegada);
				Integer numPlazas = rs.getInt("num_plazas");
				Integer numPasajeros = rs.getInt("num_pasajeros");
				vuelo = new VueloImpl(String.valueOf(codigo), origen, destino, lFechaSalida, lFechaLlegada, numPlazas,
						numPasajeros);
				listVuelos.add(vuelo);

			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return listVuelos.toArray(new Vuelo[0]);
	}

	// Lista nombres mediante ciudades
	public String[] nombresAeropuertos(String ciudad) {
		String[] nombresAero = null;
		try {
			ArrayList<String> nombres = new ArrayList<>();
			Connection cnx = getConnection();
			PreparedStatement psAeropuerto = cnx.prepareStatement("SELECT nombre FROM aeropuertos WHERE ciudad = ?");
			psAeropuerto.setString(1, ciudad);
			ResultSet rs = psAeropuerto.executeQuery();
			while (rs.next()) {
				String nombre = rs.getString("nombre");
				if (!nombres.contains(nombre)) {
					nombres.add(nombre);
				}
			}
			nombresAero = nombres.toArray(new String[0]);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return nombresAero;
	}

	// Lista ciudades mediante nombres
	public String[] ciudadesNombreAeropuertos(String nombre) {
		String[] nombresAero = null;
		try {
			ArrayList<String> nombres = new ArrayList<>();
			Connection cnx = getConnection();
			PreparedStatement psAeropuerto = cnx.prepareStatement("SELECT ciudad FROM aeropuertos WHERE nombre = ?");
			psAeropuerto.setString(1, nombre);
			ResultSet rs = psAeropuerto.executeQuery();
			while (rs.next()) {
				String ciudad = rs.getString("ciudad");
				if (!nombres.contains(ciudad)) {
					nombres.add(ciudad);
				}
			}
			nombresAero = nombres.toArray(new String[0]);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return nombresAero;
	}

	// Lista ciudades
	public String[] ciudadesAeropuertos() {
		String[] ciudadesAero = null;
		try {
			ArrayList<String> ciudades = new ArrayList<>();
			Connection cnx = getConnection();
			PreparedStatement psAeropuerto = cnx.prepareStatement("SELECT ciudad FROM aeropuertos");
			ResultSet rs = psAeropuerto.executeQuery();
			while (rs.next()) {
				String ciudad = rs.getString("ciudad");
				if (!ciudades.contains(ciudad)) {
					ciudades.add(ciudad);
				}
			}
			ciudadesAero = ciudades.toArray(new String[0]);
			rs.close();
			psAeropuerto.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return ciudadesAero;
	}
	// Lista origenes de Vuelos

	public String[] origenesVuelos() {
		String[] origenesVue = null;
		try {
			ArrayList<String> origenes = new ArrayList<>();
			Connection cnx = getConnection();
			PreparedStatement psVuelo = cnx.prepareStatement("SELECT origen FROM vuelos");
			ResultSet rs = psVuelo.executeQuery();
			while (rs.next()) {
				String origen = rs.getString("origen");
				if (!origenes.contains(origen)) {
					origenes.add(origen);
				}
			}
			origenesVue = origenes.toArray(new String[0]);
			rs.close();
			psVuelo.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return origenesVue;
	}

	// Lista origenes por codigo

	public String origenCodigoVuelos(Integer codigo) {
		String origen = null;
		try {
			Connection cnx = getConnection();
			PreparedStatement psVuelo = cnx.prepareStatement("SELECT origen FROM vuelos where codigo = ?");
			psVuelo.setInt(1, codigo);
			ResultSet rs = psVuelo.executeQuery();
			if (rs.next()) {
				origen = rs.getString("origen");

			}
			rs.close();
			psVuelo.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return origen;
	}

	// Lista de codigo de vuelos
	public Integer[] codigosVuelos() {
		Integer[] codigosVuelos = null;
		try {
			ArrayList<Integer> codigos = new ArrayList<>();
			Connection cnx = getConnection();
			PreparedStatement psVuelos = cnx.prepareStatement("SELECT codigo FROM vuelos");
			ResultSet rs = psVuelos.executeQuery();
			while (rs.next()) {
				Integer codigo = rs.getInt("codigo");
				if (!codigos.contains(codigo)) {
					codigos.add(codigo);
				}
			}
			codigosVuelos = codigos.toArray(new Integer[0]);
			rs.close();
			psVuelos.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return codigosVuelos;
	}

	// Lista de codigo de vuelos con codigo de Aeropuerto
	public Integer[] codigosVuelos_Pasajeros(Integer codAero) {
		Integer[] codigosVuelos = null;
		try {
			ArrayList<Integer> codigos = new ArrayList<>();
			Connection cnx = getConnection();
			PreparedStatement psVuelos = cnx
					.prepareStatement("SELECT codigo_vuelo FROM aeropuertos_vuelos WHERE cod_Aero = ?");
			psVuelos.setInt(1, codAero);
			ResultSet rs = psVuelos.executeQuery();
			while (rs.next()) {
				Integer codigo = rs.getInt("codigo_vuelo");
				if (!codigos.contains(codigo)) {
					codigos.add(codigo);
				}
			}
			codigosVuelos = codigos.toArray(new Integer[0]);
			rs.close();
			psVuelos.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return codigosVuelos;
	}

}
