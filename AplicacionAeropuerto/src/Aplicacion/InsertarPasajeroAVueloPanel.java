package Aplicacion;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import Clases.AeropuertoBBDD;
import Clases.Vuelo;
import Fondos.Fondos;

public class InsertarPasajeroAVueloPanel {
	private AeropuertoBBDD aero;
    private Fondos fondos;
    private Pantalla pantalla;

    public InsertarPasajeroAVueloPanel(AeropuertoBBDD aero, Fondos fondos, Pantalla pantalla) {
        this.aero = aero;
        this.fondos = fondos;
        this.pantalla = pantalla;
    }
	public JPanel crearPanel() {
		JPanel backgroundPanel = fondos.fondoNormal();

		JPanel formPanel = fondos.fondoBlancoInsertar();

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(10, 10, 10, 10);
		gbc.gridx = 0;
		gbc.gridy = GridBagConstraints.RELATIVE;
		gbc.anchor = GridBagConstraints.WEST;

		JLabel lblTitulo = new JLabel("INSERTAR PASAJERO A VUELO");
		lblTitulo.setFont(new Font("Arial", Font.BOLD, 16));

		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.anchor = GridBagConstraints.CENTER;
		formPanel.add(lblTitulo, gbc);
		gbc.gridwidth = 1;
		gbc.anchor = GridBagConstraints.WEST;

		JLabel lblDNI = new JLabel("DNI: ");
		JTextField txtDNI = new JTextField(20);

		JLabel lblOrigen = new JLabel("Origen del vuelo: ");
		JComboBox<String> cbOrigen = new JComboBox<>(aero.ciudadesAeropuertos());

		JLabel lblDestino = new JLabel("Destino del vuelo: ");
		JComboBox<String> cbDestino = new JComboBox<>(aero.ciudadesAeropuertos());

		JLabel lblVuelos = new JLabel("Elige el vuelo: ");
		JComboBox<Vuelo> cbVuelos = new JComboBox<>();

		JButton btnInsertar = new JButton("Insertar Pasajero");
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
				String dni = txtDNI.getText();
				Vuelo vueloSeleccionado = (Vuelo) cbVuelos.getSelectedItem();
				aero.addPasajeroVuelo(dni, vueloSeleccionado);
			}
		});

		btnSalir.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				pantalla.showCard("BackgroundPanel");
			}
		});


		formPanel.add(lblDNI, gbc);
		formPanel.add(txtDNI, gbc);
		formPanel.add(lblOrigen, gbc);
		formPanel.add(cbOrigen, gbc);
		formPanel.add(lblDestino, gbc);
		formPanel.add(cbDestino, gbc);
		formPanel.add(lblVuelos, gbc);
		formPanel.add(cbVuelos, gbc);

		gbc.anchor = GridBagConstraints.CENTER;
		formPanel.add(btnInsertar, gbc);
		formPanel.add(btnSalir, gbc);

		backgroundPanel.add(formPanel);
		return backgroundPanel;
	}
}
