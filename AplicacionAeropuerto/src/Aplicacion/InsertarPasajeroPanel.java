package Aplicacion;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import Clases.AeropuertoBBDD;
import Clases.Persona;
import Clases.PersonaImpl;
import Clases.Vuelo;
import Fondos.Fondos;

public class InsertarPasajeroPanel {
    private AeropuertoBBDD aero;
    private Fondos fondos;
    private Pantalla pantalla;

    public InsertarPasajeroPanel(AeropuertoBBDD aero, Fondos fondos, Pantalla pantalla) {
	this.aero = aero;
	this.fondos = fondos;
	this.pantalla = pantalla;
    }

    public JPanel crearPanel() {
	JPanel backgroundPanel = fondos.fondoNormal();
	JPanel formPanel = fondos.fondoBlancoInsertarPasajero();
	formPanel.setLayout(new GridBagLayout());
	GridBagConstraints gbc = new GridBagConstraints();
	gbc.insets = new Insets(10, 10, 10, 10);
	gbc.anchor = GridBagConstraints.WEST;

	JLabel lblTitulo = new JLabel("INSERTAR PASAJERO");
	lblTitulo.setFont(new Font("Arial", Font.BOLD, 16));

	gbc.gridx = 0;
	gbc.gridy = 0;
	gbc.gridwidth = 2;
	gbc.anchor = GridBagConstraints.CENTER;
	formPanel.add(lblTitulo, gbc);

	gbc.gridwidth = 1;
	gbc.anchor = GridBagConstraints.WEST;

	JLabel lblNombre = new JLabel("Nombre: ");
	JTextField txtNombre = new JTextField(20);
	JLabel lblEdad = new JLabel("Edad: ");
	JTextField txtEdad = new JTextField(20);
	JLabel lblDNI = new JLabel("DNI: ");
	JTextField txtDNI = new JTextField(20);
	JLabel lblPeso = new JLabel("Peso: ");
	JTextField txtPeso = new JTextField(20);
	JLabel lblAltura = new JLabel("Altura: ");
	JTextField txtAltura = new JTextField(20);
	JLabel lblSexo = new JLabel("Sexo: ");
	JRadioButton hombreButton = new JRadioButton("Hombre");
	JRadioButton mujerButton = new JRadioButton("Mujer");
	ButtonGroup sexoGroup = new ButtonGroup();
	sexoGroup.add(hombreButton);
	sexoGroup.add(mujerButton);
	JPanel sexoPanel = new JPanel();
	sexoPanel.add(hombreButton);
	sexoPanel.add(mujerButton);
	JLabel lblOrigen = new JLabel("Origen del vuelo: ");
	JComboBox<String> cbOrigen = new JComboBox<>(aero.ciudadesAeropuertos());
	JLabel lblDestino = new JLabel("Destino del vuelo: ");
	JComboBox<String> cbDestino = new JComboBox<>(aero.ciudadesAeropuertos());
	JLabel lblVuelos = new JLabel("Elige el vuelo: ");
	JComboBox<Vuelo> cbVuelos = new JComboBox<>();
	JButton btnInsertar = new JButton("Insertar Pasajero");
	JButton btnSalir = new JButton("Salir");

	gbc.gridx = 0;
	gbc.gridy = 1;
	formPanel.add(lblNombre, gbc);
	gbc.gridx = 1;
	formPanel.add(txtNombre, gbc);
	gbc.gridx = 0;
	gbc.gridy = 2;
	formPanel.add(lblEdad, gbc);
	gbc.gridx = 1;
	formPanel.add(txtEdad, gbc);
	gbc.gridx = 0;
	gbc.gridy = 3;
	formPanel.add(lblDNI, gbc);
	gbc.gridx = 1;
	formPanel.add(txtDNI, gbc);
	gbc.gridx = 0;
	gbc.gridy = 4;
	formPanel.add(lblPeso, gbc);
	gbc.gridx = 1;
	formPanel.add(txtPeso, gbc);
	gbc.gridx = 0;
	gbc.gridy = 5;
	formPanel.add(lblAltura, gbc);
	gbc.gridx = 1;
	formPanel.add(txtAltura, gbc);
	gbc.gridx = 0;
	gbc.gridy = 6;
	formPanel.add(lblSexo, gbc);
	gbc.gridx = 1;
	formPanel.add(sexoPanel, gbc);
	gbc.gridx = 0;
	gbc.gridy = 7;
	formPanel.add(lblOrigen, gbc);
	gbc.gridx = 1;
	formPanel.add(cbOrigen, gbc);
	gbc.gridx = 0;
	gbc.gridy = 8;
	formPanel.add(lblDestino, gbc);
	gbc.gridx = 1;
	formPanel.add(cbDestino, gbc);
	gbc.gridx = 0;
	gbc.gridy = 9;
	formPanel.add(lblVuelos, gbc);
	gbc.gridx = 1;
	formPanel.add(cbVuelos, gbc);
	gbc.anchor = GridBagConstraints.CENTER;
	gbc.gridx = 0;
	gbc.gridy = 10;
	gbc.gridwidth = 2;
	formPanel.add(btnInsertar, gbc);

	gbc.gridy = 11;
	formPanel.add(btnSalir, gbc);

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
		cbVuelos.removeAllItems();
	    }
	});
	// Listener para actualizar el comboBox de vuelos cuando se selecciona un
	// destino
	cbDestino.addActionListener(new ActionListener() {
	    @Override
	    public void actionPerformed(ActionEvent e) {
		// Actualizar el comboBox de vuelos
		cbVuelos.removeAllItems();
		if (cbOrigen.getSelectedItem() != null && cbDestino.getSelectedItem() != null) {
		    for (Vuelo vuelo : aero.vuelosOrigenDestino((String) cbOrigen.getSelectedItem(),
			    (String) cbDestino.getSelectedItem())) {
			cbVuelos.addItem(vuelo);
		    }
		}
	    }
	});

	btnInsertar.addActionListener(new ActionListener() {
	    @Override
	    public void actionPerformed(ActionEvent e) {
		String nombre = txtNombre.getText();
		Integer edad = Integer.parseInt(txtEdad.getText());
		String dni = txtDNI.getText();
		Double peso = Double.parseDouble(txtPeso.getText());
		Double altura = Double.parseDouble(txtAltura.getText());
		String sexo = hombreButton.isSelected() ? "Hombre" : "Mujer";
		Character sexoBien = sexo.charAt(0);
		Vuelo vueloSeleccionado = (Vuelo) cbVuelos.getSelectedItem();
		Persona pasajero = new PersonaImpl(nombre, edad, dni, sexoBien, peso, altura);
		aero.addPasajeros(pasajero, vueloSeleccionado);
	    }
	});

	btnSalir.addActionListener(new ActionListener() {
	    @Override
	    public void actionPerformed(ActionEvent e) {
		pantalla.showCard("BackgroundPanel");
	    }
	});

	backgroundPanel.add(formPanel);
	return backgroundPanel;
    }
}
