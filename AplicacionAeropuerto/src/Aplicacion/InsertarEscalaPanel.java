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

import Clases.AeropuertoBBDD;
import Clases.Vuelo;
import Fondos.Fondos;

public class InsertarEscalaPanel {
	private AeropuertoBBDD aero;
    private Fondos fondos;
    private Pantalla pantalla;

    public InsertarEscalaPanel(AeropuertoBBDD aero, Fondos fondos, Pantalla pantalla) {
        this.aero = aero;
        this.fondos = fondos;
        this.pantalla = pantalla;
    }
	public JPanel crearPanel() {
		JPanel backgroundPanel = fondos.fondoNormal();

		JPanel formPanel = fondos.fondoBlancoInsertarEscala();

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(10, 10, 10, 10);
		gbc.gridx = 0;
		gbc.gridy = GridBagConstraints.RELATIVE;
		gbc.anchor = GridBagConstraints.WEST;

		JLabel lblTitulo = new JLabel("INSERTAR ESCALA");
		lblTitulo.setFont(new Font("Arial", Font.BOLD, 16));

		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.anchor = GridBagConstraints.CENTER;
		formPanel.add(lblTitulo, gbc);
		gbc.gridwidth = 1;
		gbc.anchor = GridBagConstraints.WEST;

		JLabel lblOrigen = new JLabel("Introduce la ciudad del Aeropuerto que hará escala: ");
		JComboBox<String> cbOrigen = new JComboBox<>(aero.ciudadesAeropuertos());

		JLabel lblNombreOrigen = new JLabel("Introduce el nombre del Aeropuerto de esa ciudad: ");
		JComboBox<String> cbNombreOrigen = new JComboBox<>(
				aero.nombresAeropuertos((String) cbOrigen.getSelectedItem()));

		JLabel lblVuelos = new JLabel("Selecciona que vuelo quieres que sea la escala: ");
		JComboBox<Vuelo> cbVuelos = new JComboBox<>(
				aero.codigoAeropuerto_Vuelos((String) cbNombreOrigen.getSelectedItem()));

		JButton btnInsertar = new JButton("Insertar Escala");
		JButton btnSalir = new JButton("Salir");

		// Listener para actualizar el comboBox de nombres de aeropuertos de origen
	    cbOrigen.addActionListener(new ActionListener() {
	        @Override
	        public void actionPerformed(ActionEvent e) {
	        	
	        	JComboBox<String> copia = cbOrigen;
	            pantalla.actualizarNombresAeropuertos(copia, cbNombreOrigen);
	        }
	    });

	    cbNombreOrigen.addActionListener(new ActionListener() {
	        @Override
	        public void actionPerformed(ActionEvent e) {
	            pantalla.actualizarVuelos(cbNombreOrigen, cbVuelos);
	        }
	    });

		btnInsertar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				String nombreOrigen = (String) cbNombreOrigen.getSelectedItem();
				Vuelo vuelo = (Vuelo) cbVuelos.getSelectedItem();
				aero.addEscala(vuelo, nombreOrigen);

			}
		});

		btnSalir.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				pantalla.showCard("BackgroundPanel");
			}
		});

		formPanel.add(lblOrigen, gbc);
		formPanel.add(cbOrigen, gbc);
		formPanel.add(lblNombreOrigen, gbc);
		formPanel.add(cbNombreOrigen, gbc);
		formPanel.add(lblVuelos, gbc);
		formPanel.add(cbVuelos, gbc);

		gbc.anchor = GridBagConstraints.CENTER;
		formPanel.add(btnInsertar, gbc);
		formPanel.add(btnSalir, gbc);

		backgroundPanel.add(formPanel);
		return backgroundPanel;
	}
}
