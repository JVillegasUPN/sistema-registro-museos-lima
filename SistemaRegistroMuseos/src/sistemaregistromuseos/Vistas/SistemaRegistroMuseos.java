/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package sistemaregistromuseos.Vistas;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import sistemaregistromuseos.Modelos.RegistroManager;

/**
 *
 * @author a-ntt
 */
public class SistemaRegistroMuseos extends javax.swing.JFrame {
    private JPanel mainPanel;
    private JButton btnRegistro;
    private JButton btnBusqueda;
    private JButton btnReportes;
    private JButton btnAnulacion;
    private JButton btnEventos;
    private JButton btnSeguridad;
    private JButton btnInterfazUsuario; 
    private JButton btnModoOffline; 
    
    /**
     * Creates new form SistemaRegistroMuseos
     */
    public SistemaRegistroMuseos() {
        initComponents();
        setupCustomUI();
    }
    
    private void setupCustomUI() {
        setTitle("Sistema de Registro del Museo");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().removeAll();
        getContentPane().setLayout(new BorderLayout());
        createComponents();
        setupLayout();
        setupEventHandlers();
        revalidate();
        repaint();
    }
    
    private void createComponents() {
        mainPanel = new JPanel(new GridLayout(4, 1, 10, 10)); // Cambiado a 4 filas
        mainPanel.add(btnReportes);
        mainPanel.add(btnSeguridad);
        mainPanel.add(btnInterfazUsuario);
        mainPanel.add(btnModoOffline);
        btnRegistro = new JButton("Registro de Visitantes");
        btnBusqueda = new JButton("Búsqueda y Consultas");
        btnReportes = new JButton("Generar Reportes");
        btnAnulacion = new JButton("Anulación de Registros"); // Nuevo botón
        btnEventos  = new JButton("Gestión de Eventos");
        btnReportes = new JButton("Reportes y Estadísticas");
        btnSeguridad = new JButton("Seguridad y Backup");
        btnInterfazUsuario = new JButton("Interfaz de Usuario");
        btnModoOffline = new JButton("Modo Offline");
    }

    private void setupLayout() {
        mainPanel.add(btnRegistro);
        mainPanel.add(btnBusqueda);
        mainPanel.add(btnReportes);
        mainPanel.add(btnAnulacion);
        mainPanel.add(btnEventos);
        
        getContentPane().add(mainPanel, BorderLayout.CENTER);
    }

    private void setupEventHandlers() {
        btnRegistro.addActionListener((ActionEvent e) -> {
            abrirRegistroVisitantes();
        });

        btnBusqueda.addActionListener((ActionEvent e) -> {
            abrirBusquedaFrame();
        });

        btnReportes.addActionListener((ActionEvent e) -> {
            abrirReportesFrame();
        });

        btnSeguridad.addActionListener((ActionEvent e) -> {
            abrirSeguridadFrame();
        });

        btnAnulacion.addActionListener((ActionEvent e) -> {
            abrirAnulacionRegistros();
        });

        // Nuevo botón para eventos
        btnEventos.addActionListener((ActionEvent e) -> {
            abrirEventosFrame();
        });
        
        btnInterfazUsuario.addActionListener((ActionEvent e) -> {
            abrirInterfazUsuarioFrame();
        });

        btnModoOffline.addActionListener((ActionEvent e) -> {
            abrirModoOfflineFrame();
        });
    }
    
    private void abrirBusquedaFrame() {
        BusquedaFrame busquedaFrame = new BusquedaFrame();
        busquedaFrame.setVisible(true);
    }

    private void abrirEventosFrame() {
        EventosFrame eventosFrame = new EventosFrame();
        eventosFrame.setVisible(true);
    }

    private void abrirRegistroVisitantes() {
        RegistroVisitantesFrame registroFrame = new RegistroVisitantesFrame();
        registroFrame.setVisible(true);
    }
    
    private void abrirAnulacionRegistros() {
        RegistroManager manager = new RegistroManager();
        AnulacionFrame anulacionFrame = new AnulacionFrame(manager);
        anulacionFrame.setVisible(true);
    }
    
    private void abrirReportesFrame() {
        ReportesFrame reportesFrame = new ReportesFrame();
        reportesFrame.setVisible(true);
    }

    private void abrirSeguridadFrame() {
        SeguridadFrame seguridadFrame = new SeguridadFrame();
        seguridadFrame.setVisible(true);
    }
    
    private void abrirInterfazUsuarioFrame() {
        InterfazUsuarioFrame interfazFrame = new InterfazUsuarioFrame();
        interfazFrame.setVisible(true);
    }

    private void abrirModoOfflineFrame() {
        ModoOfflineFrame offlineFrame = new ModoOfflineFrame();
        offlineFrame.setVisible(true);
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
            java.util.logging.Logger.getLogger(SistemaRegistroMuseos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(SistemaRegistroMuseos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(SistemaRegistroMuseos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(SistemaRegistroMuseos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new SistemaRegistroMuseos().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
