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

import Clases.*;
import Fondos.Fondos;

public class InsertarAeropuertoPanel {
	private AeropuertoBBDD aero;
    private Fondos fondos;
    private Pantalla pantalla;

    public InsertarAeropuertoPanel(AeropuertoBBDD aero, Fondos fondos, Pantalla pantalla) {
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

        JLabel lblTitulo = new JLabel("INSERTAR AEROPUERTO");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 16));

        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.anchor = GridBagConstraints.CENTER;
        formPanel.add(lblTitulo, gbc);
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;

        JLabel lblNombre = new JLabel("Nombre: ");
        JTextField txtNombre = new JTextField(20);
        JLabel lblCiudad = new JLabel("Ciudad: ");
        JTextField txtCiudad = new JTextField(20);
        JButton btnInsertar = new JButton("Insertar Aeropuerto");
        // Botón de salir
        JButton btnSalir = new JButton("Salir");

        btnInsertar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nombre = txtNombre.getText();
                String ciudad = txtCiudad.getText();
                Aeropuerto ar1 = new AeropuertoImpl(nombre, ciudad);
                aero.addAeropuerto(ar1);
                pantalla.actualizarComboBoxes(); // Actualizar ComboBoxes en Pantalla
            }
        });

        // Función para volver al inicio
        btnSalir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pantalla.showCard("BackgroundPanel");
            }
        });

        formPanel.add(lblNombre, gbc);
        formPanel.add(txtNombre, gbc);
        formPanel.add(lblCiudad, gbc);
        formPanel.add(txtCiudad, gbc);
        gbc.anchor = GridBagConstraints.CENTER;
        formPanel.add(btnInsertar, gbc);

        gbc.anchor = GridBagConstraints.CENTER;
        formPanel.add(btnSalir, gbc);

        backgroundPanel.add(formPanel);
        return backgroundPanel;
    }
}
