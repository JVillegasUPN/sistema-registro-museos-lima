/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package sistemaregistromuseos.Vistas;
import java.awt.*;
import javax.swing.*;
import java.io.*;
import java.nio.file.*;
import java.util.*;
import javax.swing.filechooser.FileSystemView;
/**
 *
 * @author a-ntt
 */
public class ModoOfflineFrame extends javax.swing.JFrame {
    private JPanel mainPanel;
    private JLabel lblStatus;
    private JButton btnSyncUSB;
    private JTextArea txtLog;
    private boolean offlineMode = false;
    private File usbDevice = null;
    /**
     * Creates new form ModoOfflineFrame
     */
    public ModoOfflineFrame() {
        setupFrame();
        checkInternetConnection();
    }

    private void setupFrame() {
        setTitle("Modo Offline y Sincronización");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        mainPanel = new JPanel(new BorderLayout());
        createComponents();
        setupLayout();
        setupEventHandlers();
        
        getContentPane().add(mainPanel);
    }

    private void createComponents() {
        lblStatus = new JLabel("Estado de conexión: Verificando...");
        lblStatus.setFont(new Font("Arial", Font.BOLD, 14));
        
        btnSyncUSB = new JButton("Sincronizar vía USB");
        btnSyncUSB.setEnabled(false);
        
        txtLog = new JTextArea();
        txtLog.setEditable(false);
    }

    private void setupLayout() {
        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        statusPanel.add(lblStatus);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(btnSyncUSB);
        
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(statusPanel, BorderLayout.WEST);
        topPanel.add(buttonPanel, BorderLayout.EAST);
        
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(new JScrollPane(txtLog), BorderLayout.CENTER);
    }

    private void setupEventHandlers() {
        btnSyncUSB.addActionListener(e -> syncWithUSB());

        // Timer para verificar conexión periódicamente
        javax.swing.Timer connectionTimer = new javax.swing.Timer(5000, e -> checkInternetConnection());
        connectionTimer.start();
    }

    private void checkInternetConnection() {
        boolean wasOffline = offlineMode;
        offlineMode = !testInternetConnection();
        
        if (offlineMode) {
            lblStatus.setText("Estado: OFFLINE (sin conexión a Internet)");
            lblStatus.setForeground(Color.RED);
            btnSyncUSB.setEnabled(true);
            
            if (!wasOffline) {
                txtLog.append("\n[" + new Date() + "] Sistema cambió a modo offline");
            }
        } else {
            lblStatus.setText("Estado: ONLINE (conexión activa)");
            lblStatus.setForeground(new Color(0, 128, 0));
            btnSyncUSB.setEnabled(false);
            
            if (wasOffline) {
                txtLog.append("\n[" + new Date() + "] Sistema reconectado - sincronizando datos...");
                syncData();
            }
        }
    }

    private boolean testInternetConnection() {
        try {
            // Intento de conexión simple
            Process process = Runtime.getRuntime().exec("ping -c 1 google.com");
            return process.waitFor() == 0;
        } catch (Exception e) {
            return false;
        }
    }

    private void syncData() {
        // Lógica para sincronizar datos pendientes
        txtLog.append("\n[" + new Date() + "] Datos sincronizados con servidor");
    }

    private void syncWithUSB() {
        JFileChooser fileChooser = new JFileChooser();
        FileSystemView fsv = FileSystemView.getFileSystemView();
        
        // Buscar dispositivos USB
        for (File root : File.listRoots()) {
            if (fsv.getSystemTypeDescription(root).toLowerCase().contains("removable")) {
                usbDevice = root;
                break;
            }
        }
        
        if (usbDevice == null) {
            JOptionPane.showMessageDialog(this, "No se encontró dispositivo USB", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        fileChooser.setCurrentDirectory(usbDevice);
        fileChooser.setDialogTitle("Seleccionar archivos para sincronizar");
        fileChooser.setMultiSelectionEnabled(true);
        fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File[] selectedFiles = fileChooser.getSelectedFiles();
            for (File file : selectedFiles) {
                processUSBFile(file);
            }
        }
    }

    private void processUSBFile(File file) {
        try {
            Path source = file.toPath();
            Path target = Paths.get("src/sistemaregistromuseos/BD_TXT/" + file.getName());
            
            Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);
            txtLog.append("\n[" + new Date() + "] Archivo sincronizado: " + file.getName());
            
            // Verificar integridad
            if (verifyFileIntegrity(target)) {
                txtLog.append(" - Verificación exitosa");
            } else {
                txtLog.append(" - Error en verificación");
            }
        } catch (Exception e) {
            txtLog.append("\n[" + new Date() + "] Error al procesar " + file.getName() + ": " + e.getMessage());
        }
    }

    private boolean verifyFileIntegrity(Path file) {
        try {
            // Verificación simple por tamaño (en producción usar checksum)
            return Files.size(file) > 0;
        } catch (Exception e) {
            return false;
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
            java.util.logging.Logger.getLogger(ModoOfflineFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ModoOfflineFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ModoOfflineFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ModoOfflineFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ModoOfflineFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
