package Aplicacion;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import Clases.AeropuertoBBDD;
import Fondos.Fondos;

public class EliminarPasajeroPanel {
	private AeropuertoBBDD aero;
    private Fondos fondos;
    private Pantalla pantalla;

    public EliminarPasajeroPanel(AeropuertoBBDD aero, Fondos fondos, Pantalla pantalla) {
        this.aero = aero;
        this.fondos = fondos;
        this.pantalla = pantalla;
    }
	public JPanel crearPanel() {
		JPanel backgroundPanel = fondos.fondoNormal();

		JPanel formPanel = fondos.fondoBlanco();

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(10, 10, 10, 10);
		gbc.gridx = 0;
		gbc.gridy = GridBagConstraints.RELATIVE;
		gbc.anchor = GridBagConstraints.WEST;
		JLabel lblTitulo = new JLabel("ELIMINAR PASAJERO");
		lblTitulo.setFont(new Font("Arial", Font.BOLD, 16));

		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.anchor = GridBagConstraints.CENTER;
		formPanel.add(lblTitulo, gbc);
		gbc.gridwidth = 1;
		gbc.anchor = GridBagConstraints.WEST;
		JLabel lblDNI = new JLabel("Introduce el DNI: ");
		JTextField txtDNI = new JTextField(20);

		JButton btnInsertar = new JButton("Eliminar pasajero");
		// Boton de salir
		JButton btnSalir = new JButton("Salir");

		btnInsertar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String dni = txtDNI.getText();
				aero.eliminarPasajero(dni);

			}
		});
		// Funcion para volver al inicio
		btnSalir.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				pantalla.showCard("BackgroundPanel");
			}
		});

		formPanel.add(lblDNI, gbc);
		formPanel.add(txtDNI, gbc);

		gbc.anchor = GridBagConstraints.CENTER;
		formPanel.add(btnInsertar, gbc);

		gbc.anchor = GridBagConstraints.CENTER;
		formPanel.add(btnSalir, gbc);

		backgroundPanel.add(formPanel);
		return backgroundPanel;
	}
}
