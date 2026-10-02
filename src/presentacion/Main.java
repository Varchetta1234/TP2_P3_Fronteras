package presentacion;

import negocio.SistemaRegiones;
import java.awt.EventQueue;

public class Main {
    public static void main(String[] args) {
    	EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    SistemaRegiones sistema = new SistemaRegiones();
                    
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