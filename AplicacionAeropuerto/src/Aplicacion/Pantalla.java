package Aplicacion;

import java.awt.CardLayout;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;

import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.time.LocalDateTime;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import Clases.*;
import Fondos.Fondos;

@SuppressWarnings("serial")
public class Pantalla extends JFrame {
	private JPanel mainPanel;
	private CardLayout cardLayout;
	AeropuertoBBDD aero = new AeropuertoBBDD();
	Fondos fondos = new Fondos();
	static JComboBox<String> cbOrigen;
	static JComboBox<String> cbDestino;
	static JComboBox<String> cbOrigenEliminar;
	private String currentCard = "BackgroundPanel";
	public Pantalla() {
		aero.crearBaseDatos();
		setTitle("Aplicación Aeropuerto");
		setBounds(0, 0, 1400, 1000);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

		JFrame frame = new JFrame("Sistema de Gestión de Aeropuertos");
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.setSize(400, 300);
		addWindowListener(new WindowAdapter() {

			public void windowClosing(WindowEvent arg0) {
				int result = JOptionPane.showConfirmDialog(frame, "¿Estás seguro de que deseas salir?", "ALERTA",
						JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
				if (result == JOptionPane.YES_OPTION) {
					System.exit(0);
				}
			}
		});

		JMenuBar menuBar = new JMenuBar();
		// Menu Aeropuerto
		JMenu menuAeropuerto = new JMenu("Aeropuerto");
		JMenuItem itemInsertarAeropuerto = new JMenuItem("Insertar Aeropuerto");
		itemInsertarAeropuerto.addActionListener(e -> showCard("insertarAeroPanel"));
		menuAeropuerto.add(itemInsertarAeropuerto);

		JMenuItem itemEliminarAeropuerto = new JMenuItem("Eliminar Aeropuerto");
		itemEliminarAeropuerto.addActionListener(e -> showCard("eliminarAeroPanel"));
		menuAeropuerto.add(itemEliminarAeropuerto);

		JMenu menuModificarAeropuerto = new JMenu("Modificar Aeropuerto");

		JMenuItem insertarVuelo = new JMenuItem("Insertar Vuelo");
		insertarVuelo.addActionListener(e -> showCard("insertarVueloPanel"));
		menuModificarAeropuerto.add(insertarVuelo);

		JMenu eliminarVuelo = new JMenu("Eliminar Vuelo");

		JMenuItem eliminarVueloCodigo = new JMenuItem("Eliminar mediante código");
		eliminarVueloCodigo.addActionListener(e -> showCard("eliminarVueloCodigoPanel"));
		eliminarVuelo.add(eliminarVueloCodigo);

		JMenuItem eliminarVueloOrigen = new JMenuItem("Eliminar mediante origen");
		eliminarVueloOrigen.addActionListener(e -> showCard("eliminarVueloOrigenPanel"));
		eliminarVuelo.add(eliminarVueloOrigen);

		menuModificarAeropuerto.add(eliminarVuelo);

		menuAeropuerto.add(menuModificarAeropuerto);

		JMenuItem itemBuscarAeropuerto = new JMenuItem("Buscar Aeropuerto");
		itemBuscarAeropuerto.addActionListener(e -> showCard("buscarAeroPanel"));
		menuAeropuerto.add(itemBuscarAeropuerto);

		menuAeropuerto.setAlignmentX(Component.CENTER_ALIGNMENT);
		// Menu Vuelo
		JMenu menuVuelo = new JMenu("Vuelo");
		JMenuItem itemInsertarVuelo = new JMenuItem("Insertar Vuelo");
		itemInsertarVuelo.addActionListener(e -> showCard("insertarVueloPanel"));
		menuVuelo.add(itemInsertarVuelo);

		JMenu itemEliminarVuelo = new JMenu("Eliminar Vuelo");

		JMenuItem eliminarVueloCodigo2 = new JMenuItem("Eliminar mediante código");
		eliminarVueloCodigo2.addActionListener(e -> showCard("eliminarVueloCodigoPanel"));
		itemEliminarVuelo.add(eliminarVueloCodigo2);

		JMenuItem eliminarVueloOrigen2 = new JMenuItem("Eliminar mediante origen");
		eliminarVueloOrigen2.addActionListener(e -> showCard("eliminarVueloOrigenPanel"));
		itemEliminarVuelo.add(eliminarVueloOrigen2);
		menuVuelo.add(itemEliminarVuelo);

		JMenu itemModificarVuelo = new JMenu("Modificar Vuelo");
		JMenu cambiarPasajeros = new JMenu("Cambios en pasajeros");
		JMenuItem itemInsertarPasajero = new JMenuItem("Insertar Pasajero");
		itemInsertarPasajero.addActionListener(e -> showCard("insertarPasajeroVueloPanel"));
		cambiarPasajeros.add(itemInsertarPasajero);

		JMenuItem itemEliminarPasajero = new JMenuItem("Eliminar Pasajero");
		itemEliminarPasajero.addActionListener(e -> showCard("eliminarPasajeroPanel"));
		cambiarPasajeros.add(itemEliminarPasajero);

		JMenuItem itemModificarPasajero = new JMenuItem("Modificar Pasajero");
		itemModificarPasajero.addActionListener(e -> showCard("modificarPasajeroPanel"));
		cambiarPasajeros.add(itemModificarPasajero);

		JMenuItem itemBuscarPasajero = new JMenuItem("Buscar Pasajero");
		itemBuscarPasajero.addActionListener(e -> showCard("buscarPasajeroPanel"));
		cambiarPasajeros.add(itemBuscarPasajero);

		itemModificarVuelo.add(cambiarPasajeros);

		JMenuItem cambioFecha = new JMenuItem("Cambiar fecha de salida");
		cambioFecha.addActionListener(e -> showCard("cambiarFechaPanel"));
		itemModificarVuelo.add(cambioFecha);

		JMenuItem addEscala = new JMenuItem("Añadir una escala");
		addEscala.addActionListener(e -> showCard("insertarEscalaPanel"));
		itemModificarVuelo.add(addEscala);

		menuVuelo.add(itemModificarVuelo);

		JMenu itemBuscarVuelo = new JMenu("Buscar Vuelo");
		JMenuItem buscarVueloCodigo = new JMenuItem("Buscar mediante código");
		buscarVueloCodigo.addActionListener(e -> showCard("buscarVueloCodigoPanel"));
		itemBuscarVuelo.add(buscarVueloCodigo);

		JMenuItem buscarVueloOrigen = new JMenuItem("Buscar mediante origen");
		buscarVueloOrigen.addActionListener(e -> showCard("buscarVueloOrigenPanel"));
		itemBuscarVuelo.add(buscarVueloOrigen);

		menuVuelo.add(itemBuscarVuelo);

		menuVuelo.setAlignmentX(Component.CENTER_ALIGNMENT);
		// Menu Pasajero

		JMenu menuPasajero = new JMenu("Pasajero");
		JMenuItem itemInsertarPasajero2 = new JMenuItem("Insertar Pasajero");
		itemInsertarPasajero2.addActionListener(e -> showCard("insertarPasajeroPanel"));
		menuPasajero.add(itemInsertarPasajero2);

		JMenuItem itemEliminarPasajero2 = new JMenuItem("Eliminar Pasajero");
		itemEliminarPasajero2.addActionListener(e -> showCard("eliminarPasajeroPanel"));
		menuPasajero.add(itemEliminarPasajero2);

		JMenuItem itemModificarPasajero2 = new JMenuItem("Modificar Pasajero");
		itemModificarPasajero2.addActionListener(e -> showCard("modificarPasajeroPanel"));
		menuPasajero.add(itemModificarPasajero2);

		JMenuItem itemBuscarPasajero2 = new JMenuItem("Buscar Pasajero");
		itemBuscarPasajero2.addActionListener(e -> showCard("buscarPasajeroPanel"));
		menuPasajero.add(itemBuscarPasajero2);

		menuPasajero.setAlignmentX(Component.CENTER_ALIGNMENT);

		JMenu opciones = new JMenu("Refrescar");
		JMenuItem actualizarItem = new JMenuItem("Refrescar");
		actualizarItem.addActionListener(e -> reiniciarAplicacion());

		opciones.add(actualizarItem);

		menuBar.add(menuAeropuerto);
		menuBar.add(menuVuelo);
		menuBar.add(menuPasajero);
		menuBar.add(Box.createHorizontalGlue());
		menuBar.add(opciones);

		menuBar.setPreferredSize(new Dimension(menuBar.getWidth(), 30));

		setJMenuBar(menuBar);

		cardLayout = new CardLayout();
		mainPanel = new JPanel(cardLayout);
		// Fondo de la imagen
		JPanel backgroundPanel = fondos.fondoMenu();
		mainPanel.add(backgroundPanel, "BackgroundPanel");
		InsertarAeropuertoPanel insertarAeropuertoPanel = new InsertarAeropuertoPanel(aero, fondos, this);
		mainPanel.add(insertarAeropuertoPanel.crearPanel(), "insertarAeroPanel");
		EliminarAeropuertoPanel eliminarAeropuertoPanel = new EliminarAeropuertoPanel(aero, fondos, this);
		mainPanel.add(eliminarAeropuertoPanel.crearPanel(), "eliminarAeroPanel");
		mainPanel.add(insertarVuelo(), "insertarVueloPanel");
		EliminarVueloCodigoPanel eliminarVueloCodigoPanel = new EliminarVueloCodigoPanel(aero, fondos, this);
		mainPanel.add(eliminarVueloCodigoPanel.crearPanel(), "eliminarVueloCodigoPanel");
		mainPanel.add(eliminarVueloOrigen(), "eliminarVueloOrigenPanel");
		BuscarAeropuertoPanel buscarAeropuertoPanel = new BuscarAeropuertoPanel(aero, fondos, this);
		mainPanel.add(buscarAeropuertoPanel.crearPanel(), "buscarAeroPanel");
		CambiarFechaSalidaVueloPanel cambiarFechaSalidaVueloPanel = new CambiarFechaSalidaVueloPanel(aero, fondos, this);
		mainPanel.add(cambiarFechaSalidaVueloPanel.crearPanel(), "cambiarFechaPanel");
		InsertarEscalaPanel insertarEscalaPanel = new InsertarEscalaPanel(aero, fondos, this);
		mainPanel.add(insertarEscalaPanel.crearPanel(), "insertarEscalaPanel");
		BuscarVueloCodigoPanel buscarVueloCodigoPanel = new BuscarVueloCodigoPanel(aero, fondos, this);
		mainPanel.add(buscarVueloCodigoPanel.crearPanel(), "buscarVueloCodigoPanel");
		BuscarVueloOrigenPanel buscarVueloOrigenPanel = new BuscarVueloOrigenPanel(aero, fondos, this);
		mainPanel.add(buscarVueloOrigenPanel.crearPanel(), "buscarVueloOrigenPanel");
		InsertarPasajeroPanel insertarPasajeroPanel = new InsertarPasajeroPanel(aero, fondos, this);
		mainPanel.add(insertarPasajeroPanel.crearPanel(), "insertarPasajeroPanel");
		InsertarPasajeroAVueloPanel insertarPasajeroAVueloPanel = new InsertarPasajeroAVueloPanel(aero, fondos, this);
		mainPanel.add(insertarPasajeroAVueloPanel.crearPanel(), "insertarPasajeroVueloPanel");
		EliminarPasajeroPanel eliminarPasajeroPanel = new EliminarPasajeroPanel(aero, fondos, this);
		mainPanel.add(eliminarPasajeroPanel.crearPanel(), "eliminarPasajeroPanel");
		ModificarPasajeroPanel modificarPasajeroPanel = new ModificarPasajeroPanel(aero, fondos, this);
		mainPanel.add(modificarPasajeroPanel.crearPanel(), "modificarPasajeroPanel");
		BuscarPasajeroPanel buscarPasajeroPanel = new BuscarPasajeroPanel(aero, fondos, this);
		mainPanel.add(buscarPasajeroPanel.crearPanel(), "buscarPasajeroPanel");

		add(mainPanel);

		showCard("BackgroundPanel");
	}

	public void mostrar() {
		this.setVisible(true);
	}

	private void reiniciarAplicacion() {
		Rectangle bounds = this.getBounds();
		this.dispose(); // Cierra la ventana actual
		Pantalla nuevaPantalla = new Pantalla();
		nuevaPantalla.actualizarComboBoxes();
		nuevaPantalla.setBounds(bounds);
		nuevaPantalla.mostrar(); // Muestra la nueva ventana
		nuevaPantalla.showCard(currentCard);
	}

	public void showCard(String cardName) {
		currentCard = cardName;
		cardLayout.show(mainPanel, cardName);
	}
	public void actualizarComboBoxes() {
        cbOrigen.removeAllItems();
        cbDestino.removeAllItems();
        cbOrigenEliminar.removeAllItems();

        for (String ciudad : aero.ciudadesAeropuertos()) {
            cbOrigen.addItem(ciudad);
            cbDestino.addItem(ciudad);
        }

        for (String origen : aero.origenesVuelos()) {
            cbOrigenEliminar.addItem(origen);
        }
    }
	public void actualizarNombresAeropuertos(JComboBox<String> cbOrigen, JComboBox<String> cbNombreOrigen) {
	    cbNombreOrigen.removeAllItems();
	    String selectedCity = (String) cbOrigen.getSelectedItem();
	    if (selectedCity != null) {
	        for (String nombre : aero.nombresAeropuertos(selectedCity)) {
	            cbNombreOrigen.addItem(nombre);
	        }
	    }
	}

	public void actualizarVuelos(JComboBox<String> cbNombreOrigen, JComboBox<Vuelo> cbVuelos) {
	    cbVuelos.removeAllItems();
	    String selectedAirport = (String) cbNombreOrigen.getSelectedItem();
	    if (selectedAirport != null) {
	        for (Vuelo vuelo : aero.codigoAeropuerto_Vuelos(selectedAirport)) {
	            cbVuelos.addItem(vuelo);
	        }
	    }
	}

	// Modificacion Aeropuerto
	// Insertar Vuelo
	private JPanel insertarVuelo() {
		JPanel backgroundPanel = fondos.fondoNormal();

		JPanel formPanel = fondos.fondoBlancoInsertar();

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(10, 10, 10, 10);
		gbc.gridx = 0;
		gbc.gridy = GridBagConstraints.RELATIVE;
		gbc.anchor = GridBagConstraints.WEST;

		JLabel lblTitulo = new JLabel("INSERTAR VUELO");
		lblTitulo.setFont(new Font("Arial", Font.BOLD, 16));

		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.anchor = GridBagConstraints.CENTER;
		formPanel.add(lblTitulo, gbc);
		gbc.gridwidth = 1;
		gbc.anchor = GridBagConstraints.WEST;

		JLabel lblOrigen = new JLabel("Origen: ");
		cbOrigen = new JComboBox<>(aero.ciudadesAeropuertos());

		JLabel lblNombreOrigen = new JLabel("Nombre del Aeropuerto: ");
		JComboBox<String> cbNombreOrigen = new JComboBox<>(
				aero.nombresAeropuertos((String) cbOrigen.getSelectedItem()));

		JLabel lblDestino = new JLabel("Destino: ");
		cbDestino = new JComboBox<>(aero.ciudadesAeropuertos());

		JLabel lblNombreDestino = new JLabel("Nombre del Aeropuerto: ");
		JComboBox<String> cbNombreDestino = new JComboBox<>(
				aero.nombresAeropuertos((String) cbDestino.getSelectedItem()));

		JLabel lblFechaSalida = new JLabel("Fecha de salida (yyyy-MM-dd HH:mm:ss): ");
		JTextField txtFechaSalida = new JTextField(20);

		JLabel lblFechaLlegada = new JLabel("Fecha de llegada (yyyy-MM-dd HH:mm:ss): ");
		JTextField txtFechaLlegada = new JTextField(20);

		JLabel lblNumPlazas = new JLabel("Número de plazas: ");
		JTextField txtNumPlazas = new JTextField(20);

		JButton btnInsertar = new JButton("Insertar Vuelo");
		JButton btnSalir = new JButton("Salir");

		// Listener para actualizar el comboBox de destino cuando se selecciona un
		// origen
		cbOrigen.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Actializar el comBox de Destino

				cbDestino.removeAllItems();
				for (String ciudad : aero.ciudadesAeropuertos()) {
					if (!ciudad.equals(cbOrigen.getSelectedItem())) {
						cbDestino.addItem(ciudad);
					}
				}

				// Actualizar el comboBox de nombres de aeropuertos de origen
				cbNombreOrigen.removeAllItems();
				String selectedCity = (String) cbOrigen.getSelectedItem();
				if (selectedCity != null) {
					for (String nombre : aero.nombresAeropuertos(selectedCity)) {
						cbNombreOrigen.addItem(nombre);
					}
				}
			}
		});
		// Listener para actualizar el nombre de aeropuerto de destino
		cbDestino.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				cbNombreDestino.removeAllItems();
				String selectedCity = (String) cbDestino.getSelectedItem();
				if (selectedCity != null) {
					for (String nombre : aero.nombresAeropuertos(selectedCity)) {
						cbNombreDestino.addItem(nombre);
					}
				}
			}
		});

		btnInsertar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String origen = (String) cbOrigen.getSelectedItem();
				String nombreOrigen = (String) cbNombreOrigen.getSelectedItem();
				String destino = (String) cbDestino.getSelectedItem();
				String fechaSalida = txtFechaSalida.getText();
				LocalDateTime lFechaSalida = aero.tranformaStringFecha(fechaSalida);
				String fechaLlegada = txtFechaLlegada.getText();
				LocalDateTime lFechaLlegada = aero.tranformaStringFecha(fechaLlegada);
				Integer numPlazas = Integer.parseInt(txtNumPlazas.getText());

				Vuelo vuelo = new VueloImpl(origen, destino, lFechaSalida, lFechaLlegada, numPlazas, 0);
				aero.addVuelo(vuelo, nombreOrigen);
				actualizarComboBoxes();

			}
		});

		btnSalir.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				showCard("BackgroundPanel");
			}
		});

		formPanel.add(lblOrigen, gbc);
		formPanel.add(cbOrigen, gbc);
		formPanel.add(lblNombreOrigen, gbc);
		formPanel.add(cbNombreOrigen, gbc);
		formPanel.add(lblDestino, gbc);
		formPanel.add(cbDestino, gbc);
		formPanel.add(lblNombreDestino, gbc);
		formPanel.add(cbNombreDestino, gbc);
		formPanel.add(lblFechaSalida, gbc);
		formPanel.add(txtFechaSalida, gbc);
		formPanel.add(lblFechaLlegada, gbc);
		formPanel.add(txtFechaLlegada, gbc);
		formPanel.add(lblNumPlazas, gbc);
		formPanel.add(txtNumPlazas, gbc);

		gbc.anchor = GridBagConstraints.CENTER;
		formPanel.add(btnInsertar, gbc);
		formPanel.add(btnSalir, gbc);

		backgroundPanel.add(formPanel);
		return backgroundPanel;
	}

	// Eliminar vuelo
	
	private JPanel eliminarVueloOrigen() {
		JPanel backgroundPanel = fondos.fondoNormal();
		JPanel formPanel = fondos.fondoBlanco();

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(10, 10, 10, 10);
		gbc.gridx = 0;
		gbc.gridy = GridBagConstraints.RELATIVE;
		gbc.anchor = GridBagConstraints.WEST;

		JLabel lblTitulo = new JLabel("ELIMINAR VUELO POR ORIGEN");
		lblTitulo.setFont(new Font("Arial", Font.BOLD, 16));

		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.anchor = GridBagConstraints.CENTER;
		formPanel.add(lblTitulo, gbc);
		gbc.gridwidth = 1;
		gbc.anchor = GridBagConstraints.WEST;

		JLabel lblOrigen = new JLabel("Introduce el origen del vuelo: ");
		cbOrigenEliminar = new JComboBox<>(aero.origenesVuelos());

		JButton btnEliminar = new JButton("Eliminar Vuelos");
		JButton btnSalir = new JButton("Salir");

		btnEliminar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				String origen = (String) cbOrigenEliminar.getSelectedItem();
				Integer[] origenCodigos = aero.buscarVueloOrigen(origen);
				for (Integer codigo : origenCodigos) {
					aero.eliminarVueloCodigo(codigo);
				}
				actualizarComboBoxes();
				
			}
		});

		btnSalir.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				showCard("BackgroundPanel");
			}
		});

		formPanel.add(lblOrigen, gbc);
		formPanel.add(cbOrigenEliminar, gbc);
		gbc.anchor = GridBagConstraints.CENTER;
		formPanel.add(btnEliminar, gbc);
		formPanel.add(btnSalir, gbc);

		backgroundPanel.add(formPanel);
		return backgroundPanel;
	}
	
}
