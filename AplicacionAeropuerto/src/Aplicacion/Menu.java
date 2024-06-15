package Aplicacion;

import javax.swing.SwingUtilities;

public class Menu {

	public static void main(String[] args) {
		
		SwingUtilities.invokeLater(()->new Pantalla().mostrar());

	}

}
