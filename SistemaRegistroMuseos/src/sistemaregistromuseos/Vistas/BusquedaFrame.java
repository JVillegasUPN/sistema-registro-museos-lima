/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package sistemaregistromuseos.Vistas;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import sistemaregistromuseos.Modelos.RegistroManager;
import sistemaregistromuseos.Modelos.Visitante;
/**
 *
 * @author a-ntt
 */
public class BusquedaFrame extends javax.swing.JFrame {
    private JPanel mainPanel;
    private JPanel panelDni;
    private JPanel panelFecha;
    private JPanel panelTipo;
    private JPanel panelBotones;
    private JTextField txtBusquedaDni;
    private JTextField txtFechaBusqueda;
    private JComboBox<Visitante.TipoVisitante> cmbTipoBusqueda;
    private JButton btnBuscarDni;
    private JButton btnBuscarFecha;
    private JButton btnFiltrarTipo;
    private JButton btnGenerarEstadisticas;
    private JButton btnExportarResultados;
    private JTextArea txtResultados;
    private RegistroManager registroManager;

    
    /**
     * Creates new form BusquedaFrame
     */
    public BusquedaFrame() {
        this.registroManager = new RegistroManager();
        setupFrame();
    }
    
    private void setupFrame() {
        setTitle("Búsqueda y Consultas");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        // Inicializar componentes
        createComponents();
        
        // Configurar layout
        setupLayout();
        
        // Configurar manejadores de eventos
        setupEventHandlers();
    }

    private void createComponents() {
        mainPanel = new JPanel(new BorderLayout());
        
        // Panel para formularios
        JPanel formContainer = new JPanel(new GridLayout(4, 1, 5, 5));
        
        // Panel para búsqueda por DNI
        panelDni = new JPanel(new GridLayout(1, 3, 5, 5));
        panelDni.add(new JLabel("Buscar por DNI:"));
        txtBusquedaDni = new JTextField();
        panelDni.add(txtBusquedaDni);
        btnBuscarDni = new JButton("Buscar");
        panelDni.add(btnBuscarDni);
        
        // Panel para búsqueda por fecha
        panelFecha = new JPanel(new GridLayout(1, 3, 5, 5));
        panelFecha.add(new JLabel("Buscar por Fecha (YYYY-MM-DD):"));
        txtFechaBusqueda = new JTextField();
        panelFecha.add(txtFechaBusqueda);
        btnBuscarFecha = new JButton("Buscar");
        panelFecha.add(btnBuscarFecha);
        
        // Panel para filtrar por tipo
        panelTipo = new JPanel(new GridLayout(1, 3, 5, 5));
        panelTipo.add(new JLabel("Filtrar por Tipo:"));
        cmbTipoBusqueda = new JComboBox<>(Visitante.TipoVisitante.values());
        panelTipo.add(cmbTipoBusqueda);
        btnFiltrarTipo = new JButton("Filtrar");
        panelTipo.add(btnFiltrarTipo);
        
        // Panel para botones adicionales
        panelBotones = new JPanel(new GridLayout(1, 2, 5, 5));
        btnGenerarEstadisticas = new JButton("Generar Estadísticas");
        btnExportarResultados = new JButton("Exportar Resultados");
        panelBotones.add(btnGenerarEstadisticas);
        panelBotones.add(btnExportarResultados);
        
        // Área de resultados
        txtResultados = new JTextArea(15, 50);
        txtResultados.setEditable(false);
    }

    private void setupLayout() {
        // Panel contenedor de formularios
        JPanel formContainer = new JPanel(new GridLayout(4, 1, 5, 5));
        formContainer.add(panelDni);
        formContainer.add(panelFecha);
        formContainer.add(panelTipo);
        formContainer.add(panelBotones);
        
        // Agregar componentes al panel principal
        mainPanel.add(formContainer, BorderLayout.NORTH);
        mainPanel.add(new JScrollPane(txtResultados), BorderLayout.CENTER);
        
        // Configurar contenido de la ventana
        getContentPane().add(mainPanel);
    }

    private void setupEventHandlers() {
        btnBuscarDni.addActionListener(e -> buscarPorDni());
        btnBuscarFecha.addActionListener(e -> buscarPorFecha());
        btnFiltrarTipo.addActionListener(e -> filtrarPorTipo());
        btnGenerarEstadisticas.addActionListener(e -> generarEstadisticas());
        btnExportarResultados.addActionListener(e -> exportarResultados());
    }
    
    private void buscarPorDni() {
        String dni = txtBusquedaDni.getText().trim();
        if (dni.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese un DNI para buscar", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try {
            List<Visitante> resultados = registroManager.buscarPorDni(dni);
            if (resultados.isEmpty()) {
                txtResultados.setText("No se encontraron registros para el DNI: " + dni);
            } else {
                mostrarResultados(resultados, "Registros encontrados para DNI " + dni);
            }
        } catch (Exception e) {
            mostrarError("Error al buscar por DNI: " + e.getMessage());
        }
    }
    
    private void buscarPorFecha() {
        String fecha = txtFechaBusqueda.getText().trim();
        if (fecha.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese una fecha para buscar", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try {
            List<Visitante> resultados = registroManager.buscarPorFecha(fecha);
            if (resultados.isEmpty()) {
                txtResultados.setText("No se encontraron registros para la fecha: " + fecha);
            } else {
                mostrarResultados(resultados, "Registros encontrados para fecha " + fecha + " (" + resultados.size() + " visitas)");
            }
        } catch (Exception e) {
            mostrarError("Error al buscar por fecha: " + e.getMessage());
        }
    }
    
    private void filtrarPorTipo() {
        Visitante.TipoVisitante tipo = (Visitante.TipoVisitante) cmbTipoBusqueda.getSelectedItem();
        
        try {
            List<Visitante> resultados = registroManager.filtrarPorTipo(tipo);
            if (resultados.isEmpty()) {
                txtResultados.setText("No se encontraron registros para el tipo: " + tipo);
            } else {
                mostrarResultados(resultados, "Registros encontrados para tipo " + tipo + " (" + resultados.size() + " visitas)");
            }
        } catch (Exception e) {
            mostrarError("Error al filtrar por tipo: " + e.getMessage());
        }
    }
    
    private void generarEstadisticas() {
        try {
            String estadisticas = registroManager.generarEstadisticas();
            txtResultados.setText(estadisticas);
        } catch (Exception e) {
            mostrarError("Error al generar estadísticas: " + e.getMessage());
        }
    }
    
    private void exportarResultados() {
        String resultados = txtResultados.getText();
        if (resultados.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No hay resultados para exportar", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try {
            registroManager.exportarResultados(resultados);
            JOptionPane.showMessageDialog(this, "Resultados exportados exitosamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            mostrarError("Error al exportar resultados: " + e.getMessage());
        }
    }
    
    private void mostrarResultados(List<Visitante> visitantes, String titulo) {
        StringBuilder sb = new StringBuilder();
        sb.append(titulo).append(":\n\n");
        
        for (Visitante v : visitantes) {
            sb.append(formatVisitante(v)).append("\n\n");
        }
        
        txtResultados.setText(sb.toString());
    }
    
    private String formatVisitante(Visitante v) {
        return String.format(
            "DNI: %s\nNombre: %s\nTipo: %s\nCódigo Visita: %s\nFecha Registro: %s\nEstado: %s",
            v.getDni(), v.getNombreCompleto(), v.getTipo(), 
            v.getCodigoVisita(), v.getFechaFormateada(), v.getEstado()
        );
    }
    
    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }


    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(BusquedaFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(BusquedaFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(BusquedaFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(BusquedaFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new BusquedaFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
