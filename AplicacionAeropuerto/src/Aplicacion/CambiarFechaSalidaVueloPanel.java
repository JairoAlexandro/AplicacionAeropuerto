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

public class CambiarFechaSalidaVueloPanel {
	private AeropuertoBBDD aero;
    private Fondos fondos;
    private Pantalla pantalla;

    public CambiarFechaSalidaVueloPanel(AeropuertoBBDD aero, Fondos fondos, Pantalla pantalla) {
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

		JLabel lblTitulo = new JLabel("CAMBIAR FECHA DE SALIDA DEL VUELO");
		lblTitulo.setFont(new Font("Arial", Font.BOLD, 16));

		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.anchor = GridBagConstraints.CENTER;
		formPanel.add(lblTitulo, gbc);
		gbc.gridwidth = 1;
		gbc.anchor = GridBagConstraints.WEST;

		JLabel lblCodigoVuelo = new JLabel("Código del vuelo: ");
		JTextField txtCodigoVuelo = new JTextField(20);

		JLabel lblFechaSalida = new JLabel("Nueva fecha de salida (yyyy-MM-dd HH:mm:ss): ");
		JTextField txtFechaSalida = new JTextField(20);

		JButton btnCambiarFecha = new JButton("Cambiar Fecha de Salida");
		JButton btnSalir = new JButton("Salir");

		btnCambiarFecha.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String codigoVuelo = txtCodigoVuelo.getText();
				String fechaSalida = txtFechaSalida.getText();
				aero.modificarFechaSalida(codigoVuelo, fechaSalida);
			}
		});

		btnSalir.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				pantalla.showCard("BackgroundPanel");
			}
		});

		formPanel.add(lblCodigoVuelo, gbc);
		formPanel.add(txtCodigoVuelo, gbc);
		formPanel.add(lblFechaSalida, gbc);
		formPanel.add(txtFechaSalida, gbc);

		gbc.anchor = GridBagConstraints.CENTER;
		formPanel.add(btnCambiarFecha, gbc);
		formPanel.add(btnSalir, gbc);

		backgroundPanel.add(formPanel);
		return backgroundPanel;
	}
}
