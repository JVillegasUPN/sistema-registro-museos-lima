/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package sistemaregistromuseos.Vistas;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import sistemaregistromuseos.Modelos.RegistroManager;
import sistemaregistromuseos.Modelos.Visitante;

/**
 *
 * @author a-ntt
 */
public class RegistroVisitantesFrame extends javax.swing.JFrame {
    private JPanel mainPanel;
    private JTextField txtDni;
    private JTextField txtNombre;
    private JLabel lblErrorDni;
    private JLabel lblErrorNombre;
    private JComboBox<Visitante.TipoVisitante> cmbTipo;
    private JButton btnRegistrar;
    private RegistroManager registroManager;

    /**
     * Creates new form RegistroVisitantesFrame
     */
    public RegistroVisitantesFrame() {
        initComponents(); // Mantener esta llamada
        this.registroManager = new RegistroManager();
        setupCustomUI();  // Configurar nuestra interfaz
    }

    private void setupCustomUI() {
        // Configuración básica de la ventana
        setTitle("Registro de Visitantes");
        setSize(500, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        
        // 1. Limpiar el layout existente
        getContentPane().removeAll();
        getContentPane().setLayout(new BorderLayout());
        
        // 2. Crear e inicializar componentes
        createComponents();
        
        // 3. Configurar layout
        setupLayout();
        
        // 4. Configurar eventos
        setupEventHandlers();
        
        // Validar y repintar la interfaz
        revalidate();
        repaint();
    }

    private void createComponents() {
        // Panel principal con GridLayout
        mainPanel = new JPanel(new GridLayout(6, 2, 10, 10));
        
        // Campo DNI
        txtDni = new JTextField();
        txtDni.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                validarDni();
            }
        });
        
        // Etiqueta error DNI
        lblErrorDni = new JLabel(" ");
        lblErrorDni.setForeground(Color.RED);
        
        // Campo Nombre
        txtNombre = new JTextField();
        txtNombre.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                validarNombre();
            }
        });
        
        // Etiqueta error Nombre
        lblErrorNombre = new JLabel(" ");
        lblErrorNombre.setForeground(Color.RED);
        
        // Combo Tipo Visitante
        cmbTipo = new JComboBox<>(Visitante.TipoVisitante.values());
        
        // Botón Registrar
        btnRegistrar = new JButton("Registrar");
    }

    private void setupLayout() {
        // Añadir componentes al panel principal
        mainPanel.add(new JLabel("DNI:"));
        mainPanel.add(txtDni);
        mainPanel.add(lblErrorDni);
        mainPanel.add(new JLabel()); // Espacio vacío
        
        mainPanel.add(new JLabel("Nombre Completo:"));
        mainPanel.add(txtNombre);
        mainPanel.add(lblErrorNombre);
        mainPanel.add(new JLabel()); // Espacio vacío
        
        mainPanel.add(new JLabel("Tipo de Visitante:"));
        mainPanel.add(cmbTipo);
        mainPanel.add(new JLabel()); // Espacio vacío
        mainPanel.add(btnRegistrar);
        
        // Añadir panel al centro del contentPane
        getContentPane().add(mainPanel, BorderLayout.CENTER);
    }

    private void setupEventHandlers() {
        btnRegistrar.addActionListener(e -> registrarVisitante());
    }

    private void validarDni() {
        Visitante visitante = new Visitante();
        try {
            // Primero establecer el tipo para que las validaciones de DNI funcionen correctamente
            visitante.setTipo((Visitante.TipoVisitante)cmbTipo.getSelectedItem());
            visitante.setDni(txtDni.getText());
            lblErrorDni.setText(" ");
        } catch (IllegalArgumentException e) {
            lblErrorDni.setText(e.getMessage());
        }
    }

    private void validarNombre() {
        Visitante visitante = new Visitante();
        try {
            visitante.setNombreCompleto(txtNombre.getText());
            lblErrorNombre.setText(" ");
        } catch (IllegalArgumentException e) {
            lblErrorNombre.setText(e.getMessage());
        }
    }

    private void registrarVisitante() {
        Visitante visitante = new Visitante();
        try {
            // Establecer tipo primero para que las validaciones de DNI funcionen
            visitante.setTipo((Visitante.TipoVisitante)cmbTipo.getSelectedItem());
            visitante.setDni(txtDni.getText());
            visitante.setNombreCompleto(txtNombre.getText());

            registroManager.registrarVisitante(visitante);

            JOptionPane.showMessageDialog(this,
                "Registro exitoso!\n" +
                "Código de visita: " + visitante.getCodigoVisita(),
                "Registro Completado",
                JOptionPane.INFORMATION_MESSAGE);

            limpiarFormulario();
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, 
                e.getMessage(), 
                "Error en Registro", 
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limpiarFormulario() {
        txtDni.setText("");
        txtNombre.setText("");
        cmbTipo.setSelectedIndex(0);
        lblErrorDni.setText(" ");
        lblErrorNombre.setText(" ");
        txtDni.requestFocus();
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
            java.util.logging.Logger.getLogger(RegistroVisitantesFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(RegistroVisitantesFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(RegistroVisitantesFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(RegistroVisitantesFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new RegistroVisitantesFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
