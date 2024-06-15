package Aplicacion;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import Clases.AeropuertoBBDD;
import Clases.Vuelo;
import Fondos.Fondos;

public class BuscarVueloCodigoPanel {
	private AeropuertoBBDD aero;
    private Fondos fondos;
    private Pantalla pantalla;

    public BuscarVueloCodigoPanel(AeropuertoBBDD aero, Fondos fondos, Pantalla pantalla) {
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
		JLabel lblTitulo = new JLabel("BUSCAR VUELO CODIGO");
		lblTitulo.setFont(new Font("Arial", Font.BOLD, 16));

		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.anchor = GridBagConstraints.CENTER;
		formPanel.add(lblTitulo, gbc);
		gbc.gridwidth = 1;
		gbc.anchor = GridBagConstraints.WEST;
		JLabel lblCodigo = new JLabel("Codigo: ");
		JTextField txtCodigo = new JTextField(20);

		JButton btnInsertar = new JButton("Buscar Vuelo");
		// Boton de salir
		JButton btnSalir = new JButton("Salir");

		btnInsertar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Integer codigo = Integer.parseInt(txtCodigo.getText());

				Vuelo vuelo = aero.buscarVueloCodigo(codigo);
				if (vuelo != null) {
					JOptionPane.showMessageDialog(formPanel, vuelo.toString());
				} else {
					JOptionPane.showMessageDialog(formPanel, "Ese Vuelo no existe");
				}

			}
		});
		// Funcion para volver al inicio
		btnSalir.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				pantalla.showCard("BackgroundPanel");
			}
		});

		formPanel.add(lblCodigo, gbc);
		formPanel.add(txtCodigo, gbc);

		gbc.anchor = GridBagConstraints.CENTER;
		formPanel.add(btnInsertar, gbc);

		gbc.anchor = GridBagConstraints.CENTER;
		formPanel.add(btnSalir, gbc);

		backgroundPanel.add(formPanel);
		return backgroundPanel;
	}
}
