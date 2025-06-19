/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package sistemaregistromuseos.Vistas;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import sistemaregistromuseos.Modelos.RegistroManager;

/**
 *
 * @author a-ntt
 */
public class ReportesFrame extends javax.swing.JFrame {
    private JPanel mainPanel;
    private JButton btnGenerarDiario;
    private JButton btnContarTipos;
    private JButton btnVerAforo;
    private JTextArea txtResultados;
    private RegistroManager registroManager;
    
    /**
     * Creates new form ReportesFrame
     */
    public ReportesFrame() {
        this.registroManager = new RegistroManager();
        setupFrame();
    }

    private void setupFrame() {
        setTitle("Reportes del Museo");
        setSize(600, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        mainPanel = new JPanel(new BorderLayout());
        createComponents();
        setupLayout();
        setupEventHandlers();
        
        getContentPane().add(mainPanel);
    }

    private void createComponents() {
        JPanel panelBotones = new JPanel(new GridLayout(1, 3, 5, 5));
        btnGenerarDiario = new JButton("Generar Reporte Diario");
        btnContarTipos = new JButton("Contar por Tipo");
        btnVerAforo = new JButton("Ver Aforo Actual");
        panelBotones.add(btnGenerarDiario);
        panelBotones.add(btnContarTipos);
        panelBotones.add(btnVerAforo);
        
        txtResultados = new JTextArea(15, 50);
        txtResultados.setEditable(false);
    }

    private void setupLayout() {
        JPanel panelBotones = new JPanel(new GridLayout(1, 3, 5, 5));
        panelBotones.add(btnGenerarDiario);
        panelBotones.add(btnContarTipos);
        panelBotones.add(btnVerAforo);
        
        mainPanel.add(panelBotones, BorderLayout.NORTH);
        mainPanel.add(new JScrollPane(txtResultados), BorderLayout.CENTER);
    }

    private void setupEventHandlers() {
        btnGenerarDiario.addActionListener(e -> generarReporteDiario());
        btnContarTipos.addActionListener(e -> contarPorTipo());
        btnVerAforo.addActionListener(e -> verAforoActual());
    }
    
    private void generarReporteDiario() {
        try {
            String reporte = registroManager.generarReporteDiario();
            txtResultados.setText(reporte);
            JOptionPane.showMessageDialog(this, "Reporte diario generado exitosamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al generar reporte: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void contarPorTipo() {
        try {
            String estadisticas = registroManager.generarEstadisticas();
            txtResultados.setText(estadisticas);
            
            // Generar gráfico ASCII simple
            List<String> tipos = registroManager.contarVisitantesPorTipo();
            StringBuilder grafico = new StringBuilder("\nDistribución Gráfica:\n");
            
            for (String tipo : tipos) {
                String[] partes = tipo.split(":");
                String nombre = partes[0].trim();
                int cantidad = Integer.parseInt(partes[1].trim());
                grafico.append(nombre).append(": ");
                for (int i = 0; i < cantidad; i++) {
                    grafico.append("█");
                }
                grafico.append(" (").append(cantidad).append(")\n");
            }
            
            txtResultados.append(grafico.toString());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al contar tipos: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void verAforoActual() {
        try {
            int capacidad = 500; // Capacidad máxima del museo
            int visitantesHoy = registroManager.contarVisitantesHoy();
            double porcentaje = (double) visitantesHoy / capacidad * 100;
            
            String mensaje = String.format("Aforo Actual: %d/%d (%.2f%%)", 
                visitantesHoy, capacidad, porcentaje);
            
            txtResultados.setText(mensaje);
            
            if (porcentaje >= 80) {
                txtResultados.append("\n¡ALERTA! Aforo al " + (int) porcentaje + "%");
                // Simular alerta sonora (beep)
                java.awt.Toolkit.getDefaultToolkit().beep();
                
                int opcion = JOptionPane.showConfirmDialog(this, 
                    "Aforo al " + (int) porcentaje + "%. ¿Permitir más visitantes?",
                    "Alerta de Aforo", 
                    JOptionPane.YES_NO_OPTION);
                
                if (opcion == JOptionPane.YES_OPTION) {
                    txtResultados.append("\nSe permitirá sobrepasar el aforo");
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al ver aforo: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
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
            java.util.logging.Logger.getLogger(ReportesFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ReportesFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ReportesFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ReportesFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ReportesFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
