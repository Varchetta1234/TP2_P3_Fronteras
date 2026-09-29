package presentacion;

import negocio.SistemaRegiones;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        // Ejecutamos la interfaz gráfica en el hilo de despacho de eventos de Swing
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                    // Instanciamos el sistema sin parámetros
                    SistemaRegiones sistema = new SistemaRegiones();
                    
                    // Creamos y mostramos la ventana principal
                    VentanaPrincipal frame = new VentanaPrincipal(sistema);
                    frame.setLocationRelativeTo(null); // Centra la ventana en la pantalla
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}