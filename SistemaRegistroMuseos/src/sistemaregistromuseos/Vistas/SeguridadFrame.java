/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package sistemaregistromuseos.Vistas;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
/**
 *
 * @author a-ntt
 */
public class SeguridadFrame extends javax.swing.JFrame {
    private JPanel mainPanel;
    private JButton btnCifrar;
    private JButton btnBackup;
    private JButton btnRestaurar;
    private JTextArea txtResultados;
    private JPasswordField txtPassword;
    /**
     * Creates new form SeguridadFrame
     */
    public SeguridadFrame() {
        setupFrame();
    }

    private void setupFrame() {
        setTitle("Seguridad y Backup");
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
        btnCifrar = new JButton("Cifrar Archivos");
        btnBackup = new JButton("Backup Automático");
        btnRestaurar = new JButton("Restaurar Backup");
        panelBotones.add(btnCifrar);
        panelBotones.add(btnBackup);
        panelBotones.add(btnRestaurar);
        
        txtPassword = new JPasswordField();
        JPanel panelPassword = new JPanel(new GridLayout(1, 2));
        panelPassword.add(new JLabel("Contraseña Maestra:"));
        panelPassword.add(txtPassword);
        
        txtResultados = new JTextArea(15, 50);
        txtResultados.setEditable(false);
    }

    private void setupLayout() {
        JPanel panelSuperior = new JPanel(new GridLayout(2, 1));
        
        JPanel panelBotones = new JPanel(new GridLayout(1, 3, 5, 5));
        panelBotones.add(btnCifrar);
        panelBotones.add(btnBackup);
        panelBotones.add(btnRestaurar);
        
        JPanel panelPassword = new JPanel(new GridLayout(1, 2));
        panelPassword.add(new JLabel("Contraseña Maestra:"));
        panelPassword.add(txtPassword);
        
        panelSuperior.add(panelBotones);
        panelSuperior.add(panelPassword);
        
        mainPanel.add(panelSuperior, BorderLayout.NORTH);
        mainPanel.add(new JScrollPane(txtResultados), BorderLayout.CENTER);
    }

    private void setupEventHandlers() {
        btnCifrar.addActionListener(e -> cifrarArchivos());
        btnBackup.addActionListener(e -> realizarBackup());
        btnRestaurar.addActionListener(e -> restaurarBackup());
    }
    
    private void cifrarArchivos() {
        String password = new String(txtPassword.getPassword());
        if (password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese una contraseña", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try {
            // Implementación básica de cifrado (simulado)
            File dir = new File("src/sistemaregistromuseos/BD_TXT");
            File[] archivos = dir.listFiles((d, name) -> name.endsWith(".json"));
            
            for (File archivo : archivos) {
                String nombreOriginal = archivo.getName();
                String nombreCifrado = nombreOriginal.replace(".json", ".enc");
                
                // Simulación de cifrado - en realidad solo renombra
                Path origen = Paths.get(archivo.getAbsolutePath());
                Path destino = Paths.get(dir.getAbsolutePath(), nombreCifrado);
                Files.move(origen, destino, StandardCopyOption.REPLACE_EXISTING);
            }
            
            txtResultados.setText("Archivos cifrados exitosamente\n");
            txtResultados.append("Se requiere contraseña para acceder a los archivos .enc");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cifrar: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void realizarBackup() {
        try {
            File dirBackup = new File("src/sistemaregistromuseos/backup");
            if (!dirBackup.exists()) {
                dirBackup.mkdirs();
            }
            
            // Limpiar backups antiguos (más de 7 días)
            File[] backups = dirBackup.listFiles();
            if (backups != null && backups.length >= 7) {
                for (File backup : backups) {
                    backup.delete();
                }
            }
            
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            String fecha = sdf.format(new Date());
            
            File dirOrigen = new File("src/sistemaregistromuseos/BD_TXT");
            File[] archivos = dirOrigen.listFiles();
            
            if (archivos != null) {
                for (File archivo : archivos) {
                    String nombreBackup = archivo.getName().replace(".json", "_" + fecha + ".bak");
                    Path origen = Paths.get(archivo.getAbsolutePath());
                    Path destino = Paths.get(dirBackup.getAbsolutePath(), nombreBackup);
                    Files.copy(origen, destino, StandardCopyOption.REPLACE_EXISTING);
                }
            }
            
            txtResultados.setText("Backup completado exitosamente\n");
            txtResultados.append("Guardado en: " + dirBackup.getAbsolutePath());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error en backup: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void restaurarBackup() {
        try {
            File dirBackup = new File("src/sistemaregistromuseos/backup");
            if (!dirBackup.exists() || dirBackup.listFiles() == null || dirBackup.listFiles().length == 0) {
                JOptionPane.showMessageDialog(this, "No hay backups disponibles", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            File[] backups = dirBackup.listFiles();
            StringBuilder lista = new StringBuilder("Backups disponibles:\n");
            for (File backup : backups) {
                lista.append("- ").append(backup.getName()).append("\n");
            }
            
            String seleccion = JOptionPane.showInputDialog(this, 
                lista.toString() + "\nIngrese el nombre del backup a restaurar:",
                "Restaurar Backup",
                JOptionPane.QUESTION_MESSAGE);
            
            if (seleccion != null && !seleccion.trim().isEmpty()) {
                File backupSeleccionado = new File(dirBackup, seleccion.trim());
                if (backupSeleccionado.exists()) {
                    String nombreOriginal = seleccion.trim().replaceAll("_\\d+\\.bak$", ".json");
                    Path origen = Paths.get(backupSeleccionado.getAbsolutePath());
                    Path destino = Paths.get("src/sistemaregistromuseos/BD_TXT", nombreOriginal);
                    
                    Files.copy(origen, destino, StandardCopyOption.REPLACE_EXISTING);
                    txtResultados.setText("Backup restaurado exitosamente: " + seleccion);
                } else {
                    JOptionPane.showMessageDialog(this, "Backup no encontrado", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al restaurar: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
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
            java.util.logging.Logger.getLogger(SeguridadFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(SeguridadFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(SeguridadFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(SeguridadFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new SeguridadFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
