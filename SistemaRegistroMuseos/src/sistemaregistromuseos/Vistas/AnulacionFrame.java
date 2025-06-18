/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package sistemaregistromuseos.Vistas;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import sistemaregistromuseos.Modelos.RegistroManager;
import sistemaregistromuseos.Modelos.Visitante;

/**
 *
 * @author a-ntt
 */
public class AnulacionFrame extends javax.swing.JFrame {
    private JPanel mainPanel;
    private JComboBox<String> cmbCodigosVisita;
    private JTextArea txtMotivo;
    private JButton btnAnular;
    private JButton btnActualizar;
    private RegistroManager registroManager;
    /**
     * Creates new form AnulacionFrame
     */
    public AnulacionFrame(RegistroManager registroManager) {
        this.registroManager = registroManager;
        setupFrame();
        cargarCodigosVisita(); // Cargar códigos al iniciar
    }
    
    private void setupFrame() {
        setTitle("Anulación de Registros");
        setSize(500, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        getContentPane().removeAll();
        getContentPane().setLayout(new BorderLayout());
        
        createComponents();
        setupLayout();
        setupEventHandlers();
        
        revalidate();
        repaint();
    }

    private void createComponents() {
        mainPanel = new JPanel(new GridLayout(0, 1, 10, 10));
        
        cmbCodigosVisita = new JComboBox<>();
        txtMotivo = new JTextArea(5, 20);
        txtMotivo.setLineWrap(true);
        txtMotivo.setWrapStyleWord(true);
        
        btnAnular = new JButton("Anular Registro");
        btnActualizar = new JButton("Actualizar Lista");
    }

    private void setupLayout() {
        JPanel formPanel = new JPanel(new GridLayout(0, 1, 5, 5));
        
        formPanel.add(new JLabel("Seleccione Código de Visita:"));
        formPanel.add(cmbCodigosVisita);
        formPanel.add(btnActualizar);
        formPanel.add(new JLabel("Motivo de Anulación (obligatorio):"));
        formPanel.add(new JScrollPane(txtMotivo));
        
        mainPanel.add(formPanel);
        mainPanel.add(btnAnular);
        
        getContentPane().add(mainPanel, BorderLayout.CENTER);
    }

    private void setupEventHandlers() {
        btnAnular.addActionListener(e -> anularRegistro());
        btnActualizar.addActionListener(e -> cargarCodigosVisita());
    }
    
    private void cargarCodigosVisita() {
        cmbCodigosVisita.removeAllItems();
        try {
            // Cambiar esta línea para filtrar solo activos
            List<Visitante> visitantes = registroManager.getVisitantesRegistrados(true);

            if (visitantes.isEmpty()) {
                cmbCodigosVisita.addItem("No hay registros activos disponibles");
                return;
            }

            for (Visitante v : visitantes) {
                if (v.getCodigoVisita() != null && !v.getCodigoVisita().isEmpty()) {
                    cmbCodigosVisita.addItem(v.getCodigoVisita());
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Error al cargar códigos: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void anularRegistro() {
        String codigo = (String) cmbCodigosVisita.getSelectedItem();
        String motivo = txtMotivo.getText().trim();

        // Validaciones
        if (codigo == null || codigo.equals("No hay registros disponibles")) {
            JOptionPane.showMessageDialog(this,
                "Por favor seleccione un código de visita válido",
                "Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (motivo.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Debe ingresar un motivo de anulación",
                "Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            registroManager.anularRegistro(codigo, motivo);

            JOptionPane.showMessageDialog(this,
                "Registro anulado exitosamente:\nCódigo: " + codigo,
                "Éxito",
                JOptionPane.INFORMATION_MESSAGE);

            limpiarFormulario();
            cargarCodigosVisita(); // Actualizar lista
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this,
                "Error: " + e.getMessage() + "\nCódigo: " + codigo,
                "Error de Anulación",
                JOptionPane.ERROR_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this,
                "Error al guardar la anulación: " + e.getMessage(),
                "Error de Sistema",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limpiarFormulario() {
        txtMotivo.setText("");
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
            java.util.logging.Logger.getLogger(AnulacionFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(AnulacionFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(AnulacionFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AnulacionFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                // Crear una instancia de RegistroManager para pasar al frame
                RegistroManager registroManager = new RegistroManager();
                new AnulacionFrame(registroManager).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
