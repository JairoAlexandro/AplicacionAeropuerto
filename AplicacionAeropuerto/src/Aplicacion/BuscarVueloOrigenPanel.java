package Aplicacion;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import Clases.AeropuertoBBDD;
import Clases.Vuelo;
import Fondos.Fondos;

public class BuscarVueloOrigenPanel {
	private AeropuertoBBDD aero;
    private Fondos fondos;
    private Pantalla pantalla;

    public BuscarVueloOrigenPanel(AeropuertoBBDD aero, Fondos fondos, Pantalla pantalla) {
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
		JLabel lblTitulo = new JLabel("BUSCAR VUELO ORIGEN");
		lblTitulo.setFont(new Font("Arial", Font.BOLD, 16));

		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.anchor = GridBagConstraints.CENTER;
		formPanel.add(lblTitulo, gbc);
		gbc.gridwidth = 1;
		gbc.anchor = GridBagConstraints.WEST;
		JLabel lblOrigen = new JLabel("Origen: ");
		JComboBox<String> cbOrigen = new JComboBox<>(aero.ciudadesAeropuertos());

		JButton btnInsertar = new JButton("Buscar Vuelo");
		// Boton de salir
		JButton btnSalir = new JButton("Salir");

		btnInsertar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String codigo = (String) cbOrigen.getSelectedItem();
				Vuelo[] vuelos = aero.buscarVueloOrigenMenu(codigo);
				if (vuelos != null) {
					String texto = "";
					for (Vuelo vuelo : vuelos) {
						texto = texto + vuelo.toString() + "\n";
					}
					JOptionPane.showMessageDialog(formPanel, texto);
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

		formPanel.add(lblOrigen, gbc);
		formPanel.add(cbOrigen, gbc);

		gbc.anchor = GridBagConstraints.CENTER;
		formPanel.add(btnInsertar, gbc);

		gbc.anchor = GridBagConstraints.CENTER;
		formPanel.add(btnSalir, gbc);

		backgroundPanel.add(formPanel);
		return backgroundPanel;
	}
}
