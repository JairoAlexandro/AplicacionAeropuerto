package Fondos;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagLayout;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class Fondos {
		public JPanel fondoMenu() {
			@SuppressWarnings("serial")
			JPanel backgroundPanel = new JPanel() {
				@Override
				protected void paintComponent(Graphics imagen) {
					super.paintComponent(imagen);
					ImageIcon imageIcon = new ImageIcon(getClass().getResource("/Imagenes/fondo.png")); // Ruta de tu imagen
					Image image = imageIcon.getImage();
					imagen.drawImage(image, 0, 0, getWidth(), getHeight(), this);
				}
			};
			backgroundPanel.setLayout(new BorderLayout());
			return backgroundPanel;
		}
	//Fondo normal con rectangulo amarillo
		public JPanel fondoNormal() {
			@SuppressWarnings("serial")
			JPanel backgroundPanel = new JPanel() {
				@Override
				protected void paintComponent(Graphics imagen) {
					super.paintComponent(imagen);
					ImageIcon imageIcon = new ImageIcon(getClass().getResource("/Imagenes/fondoNormal.jpg")); // Ruta de tu imagen
					Image image = imageIcon.getImage();
					imagen.drawImage(image, 0, 0, getWidth(), getHeight(), this);
				}
			};
			backgroundPanel.setLayout(new GridBagLayout());
			return backgroundPanel;
		}
		//Fondo rectangulo blanco
		public JPanel fondoBlanco() {
			JPanel formPanel = new JPanel(new GridBagLayout());
			formPanel.setBackground(new Color(255, 255, 255, 225)); // Color blanco con algo de transparencia
			formPanel.setPreferredSize(new Dimension(400, 300));
			return formPanel;
		}
		public JPanel fondoBlancoInsertar() {
			JPanel formPanel = new JPanel(new GridBagLayout());
			formPanel.setBackground(new Color(255, 255, 255, 225)); // Color blanco con algo de transparencia
			formPanel.setPreferredSize(new Dimension(400, 700));
			return formPanel;
		}
		public JPanel fondoBlancoInsertarEscala() {
			JPanel formPanel = new JPanel(new GridBagLayout());
			formPanel.setBackground(new Color(255, 255, 255, 225)); // Color blanco con algo de transparencia
			formPanel.setPreferredSize(new Dimension(400, 500));
			return formPanel;
		}
		public JPanel fondoBlancoInsertarPasajero() {
			JPanel formPanel = new JPanel(new GridBagLayout());
			formPanel.setBackground(new Color(255, 255, 255, 225)); // Color blanco con algo de transparencia
			formPanel.setPreferredSize(new Dimension(500, 600));
			
			return formPanel;
		}
}
