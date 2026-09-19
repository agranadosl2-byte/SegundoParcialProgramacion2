package edu.umg.programacion2.proyecto;

import edu.umg.programacion2.proyecto.ui.VentanaPrincipal;
import javax.swing.SwingUtilities;

public class MainUI {
    public static void main(String[] args) {
        // SwingUtilities asegura que la interfaz gráfica inicie correctamente
        SwingUtilities.invokeLater(() -> {
            VentanaPrincipal ventana = new VentanaPrincipal();
            ventana.setVisible(true);
        });
    }
}