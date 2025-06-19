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
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import sistemaregistromuseos.Modelos.Evento;
import sistemaregistromuseos.Modelos.EventoManager;

/**
 *
 * @author a-ntt
 */
public class EventosFrame extends javax.swing.JFrame {
    private JPanel mainPanel;
    private JPanel panelCreacion;
    private JPanel panelInscripcion;
    private JTextField txtNombreEvento;
    private JTextField txtFechaEvento;
    private JTextField txtCupoEvento;
    private JButton btnCrearEvento;
    private JButton btnListarEventos;
    private JComboBox<String> cmbEventos;
    private JTextField txtDniParticipante;
    private JButton btnInscribir;
    private JButton btnCancelarEvento;
    private JTextArea txtResultados;
    private EventoManager eventoManager;

    /**
     * Creates new form EventosFrame
     */
    public EventosFrame() {
        this.eventoManager = new EventoManager();
        setupFrame();
    }

    private void setupFrame() {
        setTitle("Gestión de Eventos");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        // Inicializar componentes principales
        mainPanel = new JPanel(new BorderLayout(10, 10));
        
        createComponents();
        setupLayout();
        setupEventHandlers();
        
        getContentPane().add(mainPanel);
    }

    private void createComponents() {
        // Panel de creación de eventos
        panelCreacion = new JPanel(new GridLayout(3, 2, 5, 5));
        panelCreacion.add(new JLabel("Nombre del Evento:"));
        txtNombreEvento = new JTextField();
        panelCreacion.add(txtNombreEvento);
        
        panelCreacion.add(new JLabel("Fecha (YYYY-MM-DD):"));
        txtFechaEvento = new JTextField();
        panelCreacion.add(txtFechaEvento);
        
        panelCreacion.add(new JLabel("Cupo Máximo:"));
        txtCupoEvento = new JTextField();
        panelCreacion.add(txtCupoEvento);
        
        btnCrearEvento = new JButton("Crear Evento");
        btnListarEventos = new JButton("Listar Eventos Activos");
        
        // Panel de inscripción
        panelInscripcion = new JPanel(new GridLayout(1, 3, 5, 5));
        cmbEventos = new JComboBox<>();
        panelInscripcion.add(cmbEventos);
        
        txtDniParticipante = new JTextField();
        panelInscripcion.add(txtDniParticipante);
        
        btnInscribir = new JButton("Inscribir");
        panelInscripcion.add(btnInscribir);
        
        btnCancelarEvento = new JButton("Cancelar Evento Seleccionado");
        
        // Área de resultados
        txtResultados = new JTextArea(15, 50);
        txtResultados.setEditable(false);
    }

    private void setupLayout() {
        // Panel principal para formularios
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        
        // Agregar componentes con bordes vacíos para espaciado
        panelCreacion.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        formPanel.add(panelCreacion);
        
        btnCrearEvento.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        formPanel.add(btnCrearEvento);
        
        btnListarEventos.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        formPanel.add(btnListarEventos);
        
        panelInscripcion.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        formPanel.add(panelInscripcion);
        
        btnCancelarEvento.setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 10));
        formPanel.add(btnCancelarEvento);
        
        // Agregar al panel principal
        mainPanel.add(formPanel, BorderLayout.NORTH);
        mainPanel.add(new JScrollPane(txtResultados), BorderLayout.CENTER);
    }

    private void setupEventHandlers() {
        btnCrearEvento.addActionListener(e -> crearEvento());
        btnListarEventos.addActionListener(e -> listarEventos());
        btnInscribir.addActionListener(e -> inscribirParticipante());
        btnCancelarEvento.addActionListener(e -> cancelarEvento());
    }
    
    private void crearEvento() {
        try {
            String nombre = txtNombreEvento.getText().trim();
            String fechaStr = txtFechaEvento.getText().trim();
            int cupo = Integer.parseInt(txtCupoEvento.getText().trim());
            
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date fecha = sdf.parse(fechaStr);
            
            eventoManager.crearEvento(nombre, fecha, cupo);
            
            txtResultados.setText("Evento creado exitosamente:\n" +
                                "Nombre: " + nombre + "\n" +
                                "Fecha: " + fechaStr + "\n" +
                                "Cupo: " + cupo);
            
            limpiarCamposCreacion();
            listarEventos();
            
        } catch (EventoManager.FechaPasadaException e) {
            mostrarError("Fecha inválida", e.getMessage());
        } catch (EventoManager.CupoInvalidoException e) {
            mostrarError("Cupo inválido", e.getMessage());
        } catch (NumberFormatException e) {
            mostrarError("Error en cupo", "El cupo debe ser un número válido");
        } catch (Exception e) {
            mostrarError("Error al crear evento", e.getMessage());
        }
    }
    
    private void listarEventos() {
        try {
            List<Evento> eventos = eventoManager.listarEventosActivos();
            cmbEventos.removeAllItems();
            
            if (eventos.isEmpty()) {
                txtResultados.setText("No hay eventos activos disponibles");
                return;
            }
            
            StringBuilder sb = new StringBuilder();
            sb.append("EVENTOS ACTIVOS DISPONIBLES\n");
            sb.append("===========================\n\n");
            
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            for (Evento e : eventos) {
                sb.append(String.format(
                    "Código: %s\nNombre: %s\nFecha: %s\nCupos: %d/%d\n\n",
                    e.getCodigo(), e.getNombre(), sdf.format(e.getFecha()),
                    e.getCuposDisponibles(), e.getCupoMaximo()
                ));
                cmbEventos.addItem(e.getCodigo());
            }
            
            txtResultados.setText(sb.toString());
        } catch (Exception e) {
            mostrarError("Error al listar eventos", e.getMessage());
        }
    }
    
    private void inscribirParticipante() {
        String codigoEvento = (String) cmbEventos.getSelectedItem();
        String dni = txtDniParticipante.getText().trim();

        if (codigoEvento == null || codigoEvento.isEmpty()) {
            mostrarError("Error", "Debe seleccionar un evento de la lista");
            return;
        }

        if (!dni.matches("\\d{8}")) {
            mostrarError("DNI inválido", "Debe ingresar un DNI válido de 8 dígitos numéricos");
            return;
        }

        try {
            eventoManager.inscribirEnEvento(codigoEvento, dni);
            
            txtResultados.setText("Inscripción exitosa:\n" +
                                "Evento: " + codigoEvento + "\n" +
                                "DNI: " + dni);
            
            txtDniParticipante.setText("");
            listarEventos();
            
        } catch (EventoManager.EventoNoEncontradoException e) {
            mostrarError("Evento no encontrado", e.getMessage());
        } catch (EventoManager.DNIYaInscritoException e) {
            mostrarError("DNI duplicado", e.getMessage());
        } catch (EventoManager.CupoLlenoException e) {
            mostrarError("Cupo lleno", e.getMessage());
        } catch (Exception e) {
            mostrarError("Error inesperado", e.getMessage());
        }
    }
    
    private void cancelarEvento() {
        String codigoEvento = (String) cmbEventos.getSelectedItem();

        if (codigoEvento == null || codigoEvento.isEmpty()) {
            mostrarError("Error", "Debe seleccionar un evento para cancelar");
            return;
        }

        String motivo = JOptionPane.showInputDialog(this, 
            "Ingrese el motivo de cancelación para el evento " + codigoEvento + ":",
            "Motivo de Cancelación",
            JOptionPane.QUESTION_MESSAGE);

        if (motivo == null) return; // Usuario canceló
        
        if (motivo.trim().isEmpty()) {
            mostrarError("Error", "Debe ingresar un motivo para cancelar el evento");
            return;
        }

        try {
            eventoManager.cancelarEvento(codigoEvento, motivo.trim());
            
            txtResultados.setText("Evento cancelado exitosamente:\n" +
                                "Código: " + codigoEvento + "\n" +
                                "Motivo: " + motivo.trim());
            
            listarEventos();
            
        } catch (EventoManager.EventoNoEncontradoException e) {
            mostrarError("Error al cancelar", e.getMessage());
        } catch (Exception e) {
            mostrarError("Error inesperado", e.getMessage());
        }
    }
    
    private void limpiarCamposCreacion() {
        txtNombreEvento.setText("");
        txtFechaEvento.setText("");
        txtCupoEvento.setText("");
    }
    
    private void mostrarError(String titulo, String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, titulo, JOptionPane.ERROR_MESSAGE);
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
            java.util.logging.Logger.getLogger(EventosFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(EventosFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(EventosFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(EventosFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new EventosFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
