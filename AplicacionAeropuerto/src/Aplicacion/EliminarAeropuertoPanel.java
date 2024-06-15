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

public class EliminarAeropuertoPanel {
	private AeropuertoBBDD aero;
    private Fondos fondos;
    private Pantalla pantalla;

    public EliminarAeropuertoPanel(AeropuertoBBDD aero, Fondos fondos, Pantalla pantalla) {
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

        JLabel lblTitulo = new JLabel("ELIMINAR AEROPUERTO");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 16));
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.anchor = GridBagConstraints.CENTER;
        formPanel.add(lblTitulo, gbc);

        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;
        JLabel lblNombre = new JLabel("Nombre: ");
        JTextField txtNombre = new JTextField(20);
        formPanel.add(lblNombre, gbc);
        formPanel.add(txtNombre, gbc);

        JButton btnEliminar = new JButton("Eliminar Aeropuerto");
        btnEliminar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nombre = txtNombre.getText();
                aero.eliminarAeropuerto(nombre);
                pantalla.actualizarComboBoxes();
            }
        });

        JButton btnSalir = new JButton("Salir");
        btnSalir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pantalla.showCard("BackgroundPanel");
            }
        });

        gbc.anchor = GridBagConstraints.CENTER;
        formPanel.add(btnEliminar, gbc);
        formPanel.add(btnSalir, gbc);

        backgroundPanel.add(formPanel);
        return backgroundPanel;
    }
}
