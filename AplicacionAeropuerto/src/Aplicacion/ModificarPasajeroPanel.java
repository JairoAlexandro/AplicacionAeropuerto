package Aplicacion;

import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import Clases.AeropuertoBBDD;
import Clases.Persona;
import Clases.PersonaImpl;
import Fondos.Fondos;

public class ModificarPasajeroPanel {
	private AeropuertoBBDD aero;
    private Fondos fondos;
    private Pantalla pantalla;

    public ModificarPasajeroPanel(AeropuertoBBDD aero, Fondos fondos, Pantalla pantalla) {
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

		JLabel lblTitulo = new JLabel("MODIFICAR PASAJERO");
		lblTitulo.setFont(new Font("Arial", Font.BOLD, 16));

		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.anchor = GridBagConstraints.CENTER;
		formPanel.add(lblTitulo, gbc);
		gbc.gridwidth = 1;
		gbc.anchor = GridBagConstraints.WEST;

		JLabel lblDNI = new JLabel("DNI: ");
		JTextField txtDNI = new JTextField(20);

		JLabel lblMensaje = new JLabel("Se debe rellenar todos los campos");

		JLabel lblNombre = new JLabel("Nombre: ");
		JTextField txtNombre = new JTextField(20);

		JLabel lblEdad = new JLabel("Edad: ");
		JTextField txtEdad = new JTextField(20);

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

		JPanel sexoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		sexoPanel.add(hombreButton);
		sexoPanel.add(mujerButton);

		JButton btnInsertar = new JButton("Modificar Pasajero");
		JButton btnSalir = new JButton("Salir");

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
				Persona pasajero = new PersonaImpl(nombre, edad, dni, sexoBien, peso, altura);
				aero.modificarPasajero(pasajero);
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
		formPanel.add(lblMensaje, gbc);
		formPanel.add(lblNombre, gbc);
		formPanel.add(txtNombre, gbc);
		formPanel.add(lblEdad, gbc);
		formPanel.add(txtEdad, gbc);
		formPanel.add(lblPeso, gbc);
		formPanel.add(txtPeso, gbc);
		formPanel.add(lblAltura, gbc);
		formPanel.add(txtAltura, gbc);
		formPanel.add(lblSexo, gbc);
		formPanel.add(sexoPanel, gbc);

		gbc.anchor = GridBagConstraints.CENTER;
		formPanel.add(btnInsertar, gbc);
		formPanel.add(btnSalir, gbc);

		backgroundPanel.add(formPanel);
		return backgroundPanel;
	}
}
