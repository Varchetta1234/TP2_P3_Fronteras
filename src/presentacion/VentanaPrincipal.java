package presentacion;

import negocio.SistemaRegiones;
import negocio.grafos.Provincia;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Arrays;
import java.util.List;

import org.openstreetmap.gui.jmapviewer.JMapViewer;
import org.openstreetmap.gui.jmapviewer.MapMarkerDot;
import org.openstreetmap.gui.jmapviewer.MapPolygonImpl;
import org.openstreetmap.gui.jmapviewer.Coordinate;
import org.openstreetmap.gui.jmapviewer.DefaultMapController;


public class VentanaPrincipal extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField textFieldK;
    private JTextField textPeso;
    private JTable table;
    private SistemaRegiones sistema;
    private JMapViewer mapViewer;

    public VentanaPrincipal(SistemaRegiones sistema) {
        this.sistema = sistema;
        
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch(Exception e) { e.printStackTrace(); }

        setTitle("Diseño de Regiones - TP2");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //tamanio ventana:
        setBounds(50, 50, 1150, 750);
        
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);
        JMenu mnArchivo = new JMenu("Archivo");
        menuBar.add(mnArchivo);
        JMenuItem mntmSalir = new JMenuItem("Salir");
        mntmSalir.addActionListener(e -> System.exit(0));
        mnArchivo.add(mntmSalir);

        contentPane = new JPanel();
        contentPane.setLayout(null);
        setContentPane(contentPane);

        // --- Panel 1: Configuración Algoritmo ---
        JPanel panelAlgoritmo = new JPanel();
        panelAlgoritmo.setBorder(new TitledBorder(null, "Cálculo", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        panelAlgoritmo.setBounds(10, 11, 350, 76);
        panelAlgoritmo.setLayout(null);
        contentPane.add(panelAlgoritmo);

        JLabel lblK = new JLabel("Regiones (K):");
        lblK.setBounds(10, 31, 100, 14);
        panelAlgoritmo.add(lblK);

        textFieldK = new JTextField();
        textFieldK.setBounds(100, 28, 40, 30);
        panelAlgoritmo.add(textFieldK);

        JButton btnCalcular = new JButton("Calcular Regiones");
        btnCalcular.setBounds(160, 27, 160, 23);
        panelAlgoritmo.add(btnCalcular);

        // --- Panel 2: Carga de Datos ---
        JPanel panelDatos = new JPanel();
        panelDatos.setBorder(new TitledBorder(null, "Fuente de Datos", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        panelDatos.setBounds(370, 11, 750, 76);
        panelDatos.setLayout(null);
        contentPane.add(panelDatos);

        JRadioButton rdbtnJson = new JRadioButton("Desde JSON");
        rdbtnJson.setSelected(true);
        rdbtnJson.setBounds(10, 27, 100, 23);
        panelDatos.add(rdbtnJson);

        JRadioButton rdbtnManual = new JRadioButton("Manual");
        rdbtnManual.setBounds(110, 27, 80, 23);
        panelDatos.add(rdbtnManual);

        ButtonGroup groupDatos = new ButtonGroup();
        groupDatos.add(rdbtnJson);
        groupDatos.add(rdbtnManual);

        JComboBox<String> cmbOrigen = new JComboBox<>();
        cmbOrigen.setBounds(200, 28, 140, 22);
        panelDatos.add(cmbOrigen);

        JComboBox<String> cmbDestino = new JComboBox<>();
        cmbDestino.setBounds(350, 28, 140, 22);
        panelDatos.add(cmbDestino);

        textPeso = new JTextField("0");
        textPeso.setBounds(500, 29, 40, 30);
        panelDatos.add(textPeso);

        JButton btnAgregar = new JButton("Agregar Arista");
        btnAgregar.setBounds(560, 27, 150, 23);
        panelDatos.add(btnAgregar);

        // Habilitar/Deshabilitar controles manuales según radio button
        Runnable toggleManual = () -> {
            boolean isManual = rdbtnManual.isSelected();
            cmbOrigen.setEnabled(isManual);
            cmbDestino.setEnabled(isManual);
            textPeso.setEnabled(isManual);
            btnAgregar.setEnabled(isManual);
        };
        toggleManual.run();

        // Llenar Combos
        for (int i = 0; i < 24; i++) {
            String nombre = sistema.obtenerProvincia(i).getNombre();
            cmbOrigen.addItem(nombre);
            cmbDestino.addItem(nombre);
        }

        // Eventos de Radio Buttons
        rdbtnJson.addActionListener(e -> {
            toggleManual.run();
            sistema.recargarDesdeJSON();
            actualizarMapa(null, false);
        });

        rdbtnManual.addActionListener(e -> {
            toggleManual.run();
            sistema.reiniciarAristas();
            actualizarMapa(null, false);
        });

        // Evento Agregar Arista Manual
        btnAgregar.addActionListener(e -> {
            try {
                int origen = cmbOrigen.getSelectedIndex();
                int destino = cmbDestino.getSelectedIndex();
                int peso = Integer.parseInt(textPeso.getText());

                if (origen == destino) {
                    JOptionPane.showMessageDialog(this, "No se permiten bucles: el origen y el destino deben ser distintos.", "Error", JOptionPane.ERROR_MESSAGE);
                    return; // Cortamos la ejecucion aca
                }
                
                if (peso <= 0) {
                    JOptionPane.showMessageDialog(this, "El peso debe ser un número entero mayor a 0.", "Error", JOptionPane.ERROR_MESSAGE);
                    return; // Cortamos la ejecucion aca
                }

                sistema.registrarFrontera(origen, destino, peso);
                actualizarMapa(null, false);
                
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Por favor, ingrese un número entero válido en el campo de peso.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

     // Evento Calcular
        btnCalcular.addActionListener(e -> {
            try {
                // PRIMERA VALIDACIÓN: ¿El grafo es conexo?
                if (!sistema.esGrafoConexo()) {
                    JOptionPane.showMessageDialog(VentanaPrincipal.this, 
                        "El mapa debe estar completamente conectado (grafo conexo) para poder calcular las regiones. Por favor, agregue las fronteras faltantes.", 
                        "Mapa Incompleto", JOptionPane.WARNING_MESSAGE);
                    return; // Cortamos la ejecucion aca
                }

                int k = Integer.parseInt(textFieldK.getText()); 
                
                // SEGUNDA VALIDACIÓN: K debe ser valido
                if (k <= 0 || k > sistema.cantidadProvincias()) {
                    JOptionPane.showMessageDialog(VentanaPrincipal.this, 
                        "Por favor, ingrese un número de regiones válido (entre 1 y " + sistema.cantidadProvincias() + ").", 
                        "K Inválido", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                // Si pasa las validaciones, calculamos normalmente:
                List<List<Integer>> regiones = VentanaPrincipal.this.sistema.generarRegiones(k);
                
                DefaultTableModel tm = (DefaultTableModel) table.getModel();
                tm.setRowCount(0); 
                for (int i = 0; i < regiones.size(); i++) {
                    StringBuilder provs = new StringBuilder();
                    for (Integer id : regiones.get(i)) {
                        provs.append(VentanaPrincipal.this.sistema.obtenerProvincia(id).getNombre()).append(", ");
                    }
                    tm.addRow(new String[] { String.valueOf(i + 1), provs.toString() });
                }
                actualizarMapa(regiones, true);
            } catch(NumberFormatException ex) {
                JOptionPane.showMessageDialog(VentanaPrincipal.this, 
                    "Por favor, ingrese un número válido en el campo K.", "Error", JOptionPane.ERROR_MESSAGE);
            } catch(Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(VentanaPrincipal.this, 
                    "Error al calcular las regiones.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Tablas y Mapa 
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(10, 98, 300, 580);
        contentPane.add(scrollPane);

        table = new JTable();
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Región");
        model.addColumn("Provincias");
        table.setModel(model);
        scrollPane.setViewportView(table);

        mapViewer = new JMapViewer();
        mapViewer.setBounds(320, 98, 800, 580);
        
        new DefaultMapController(mapViewer);
        
        contentPane.add(mapViewer);

        actualizarMapa(null, false);
    }

    private void actualizarMapa(List<List<Integer>> regiones, boolean mostrarRegiones) {
        mapViewer.removeAllMapMarkers();
        mapViewer.removeAllMapPolygons(); 
        
        Color[] colores = {Color.RED, Color.BLUE, Color.GREEN, Color.ORANGE, Color.MAGENTA, Color.CYAN, Color.YELLOW, Color.PINK};
        
     // Dibujar Vértices
        if (regiones == null) {
            for (int i = 0; i < 24; i++) {
                Provincia p = sistema.obtenerProvincia(i);
                if (p != null) {
                    mapViewer.addMapMarker(crearMarcadorProvincia(p, Color.BLACK));
                }
            }
        } else {
            for (int i = 0; i < regiones.size(); i++) {
                Color colorRegion = colores[i % colores.length];
                for (Integer idProvincia : regiones.get(i)) {
                    Provincia p = sistema.obtenerProvincia(idProvincia);
                    if (p != null) {
                        mapViewer.addMapMarker(crearMarcadorProvincia(p, colorRegion));
                    }
                }
            }
        }
        
     // Dibujar Aristas
        int cantProvincias = sistema.cantidadProvincias();         
        for (int i = 0; i < cantProvincias; i++) {
            for (int j = i + 1; j < cantProvincias; j++) {
                
                boolean hayConexion = mostrarRegiones ? 
                                      sistema.existeFronteraResultante(i, j) : 
                                      sistema.existeFrontera(i, j);

                if (hayConexion) {
                    Provincia p1 = sistema.obtenerProvincia(i);
                    Provincia p2 = sistema.obtenerProvincia(j);
                    
                    if (p1 != null && p2 != null) {
                        Coordinate c1 = new Coordinate(p1.getLat(), p1.getLon());
                        Coordinate c2 = new Coordinate(p2.getLat(), p2.getLon());
                        
                        MapPolygonImpl linea = new MapPolygonImpl(Arrays.asList(c1, c2, c1));
                        linea.setColor(regiones == null ? Color.GRAY : Color.BLACK);
                        mapViewer.addMapPolygon(linea);
                    }
                }
            }
        }
        mapViewer.setDisplayPosition(new Coordinate(-43.0, -64.0), 4);
    }
    
    //funcion para engañar a JMapViewer y que dibuje en el tamanio que se nos antoje el texto de Malvinas!
    private MapMarkerDot crearMarcadorProvincia(Provincia p, Color colorFondo) {
        
        String textoReal = p.getNombre().equalsIgnoreCase("Islas Malvinas") ? "Las Malvinas son argentinas" : "";
        
        MapMarkerDot marcador = new MapMarkerDot("", new Coordinate(p.getLat(), p.getLon())) {
            @Override
            public void paint(Graphics g, Point position, int radio) {
                super.paint(g, position, radio);
                
                if (g != null && !textoReal.isEmpty()) {
                    g.setColor(Color.BLACK);
                    
                    g.setFont(new Font("Arial", Font.BOLD, 18)); 
                    
                    g.drawString(textoReal, position.x + 5, position.y + 5); 
                }
            }
        };
        marcador.setBackColor(colorFondo);
        return marcador;
    }
    
}