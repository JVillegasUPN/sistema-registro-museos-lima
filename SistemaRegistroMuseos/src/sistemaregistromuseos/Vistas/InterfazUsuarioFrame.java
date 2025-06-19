/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package sistemaregistromuseos.Vistas;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.HashMap;
/**
 *
 * @author a-ntt
 */
public class InterfazUsuarioFrame extends javax.swing.JFrame {
    private JPanel mainPanel;
    private JTextArea txtOutput;
    private JTextField txtInput;
    private JButton btnHelp;
    private HashMap<String, String> helpCommands;
    /**
     * Creates new form InterfazUsuarioFrame
     */
    public InterfazUsuarioFrame() {
        setupFrame();
        initHelpSystem();
    }
    
    private void setupFrame() {
        setTitle("Interfaz de Usuario");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        mainPanel = new JPanel(new BorderLayout());
        createComponents();
        setupLayout();
        setupEventHandlers();
        
        getContentPane().add(mainPanel);
        showMainMenu();
    }

    private void initHelpSystem() {
        helpCommands = new HashMap<>();
        helpCommands.put("registro", "Registrar nuevo visitante: registro <dni> <nombre> <tipo>");
        helpCommands.put("buscar", "Buscar visitante: buscar <dni|nombre>");
        helpCommands.put("reporte", "Generar reporte: reporte <diario|mensual>");
        helpCommands.put("ayuda", "Mostrar ayuda: ayuda <comando> o /help");
    }

    private void createComponents() {
        txtOutput = new JTextArea();
        txtOutput.setEditable(false);
        txtOutput.setFont(new Font("Monospaced", Font.PLAIN, 12));
        
        txtInput = new JTextField();
        txtInput.setFont(new Font("Monospaced", Font.PLAIN, 12));
        
        btnHelp = new JButton("Ayuda (/help)");
    }

    private void setupLayout() {
        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.add(new JLabel("Comando:"), BorderLayout.WEST);
        inputPanel.add(txtInput, BorderLayout.CENTER);
        inputPanel.add(btnHelp, BorderLayout.EAST);
        
        mainPanel.add(new JScrollPane(txtOutput), BorderLayout.CENTER);
        mainPanel.add(inputPanel, BorderLayout.SOUTH);
    }

    private void setupEventHandlers() {
        txtInput.addActionListener(e -> processCommand(txtInput.getText()));
        btnHelp.addActionListener(e -> showHelp(null));
        
        // Atajo de teclado para ayuda
        KeyStroke helpKey = KeyStroke.getKeyStroke(KeyEvent.VK_SLASH, InputEvent.CTRL_DOWN_MASK);
        txtInput.getInputMap().put(helpKey, "showHelp");
        txtInput.getActionMap().put("showHelp", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                showHelp(null);
            }
        });
    }

    private void showMainMenu() {
        txtOutput.setText("=== MENÚ PRINCIPAL ===\n\n" +
                         "1. Registro de visitantes\n" +
                         "2. Búsqueda y consultas\n" +
                         "3. Reportes y estadísticas\n" +
                         "4. Gestión de eventos\n" +
                         "5. Configuración del sistema\n\n" +
                         "Ingrese el número de opción o comando directo (/help para ayuda)");
    }

    private void processCommand(String command) {
        txtInput.setText("");
        command = command.trim().toLowerCase();
        
        // Validación de entrada
        if (!validateInput(command)) {
            txtOutput.append("\n\nError: Entrada no válida. Use /help para ver comandos disponibles");
            return;
        }
        
        if (command.equals("/help")) {
            showHelp(null);
            return;
        }
        
        // Procesamiento de comandos
        if (command.matches("[1-5]")) {
            handleMenuSelection(command);
        } else {
            executeCommand(command);
        }
    }

    private boolean validateInput(String input) {
        // Prevención de inyección de código
        if (input.matches(".*[;\\|<>].*")) {
            txtOutput.append("\n\nError: Caracteres no permitidos detectados");
            return false;
        }
        return true;
    }

    private void handleMenuSelection(String option) {
        switch(option) {
            case "1":
                txtOutput.append("\n\n=== MÓDULO DE REGISTRO ===");
                break;
            case "2":
                txtOutput.append("\n\n=== MÓDULO DE BÚSQUEDA ===");
                break;
            case "3":
                txtOutput.append("\n\n=== MÓDULO DE REPORTES ===");
                break;
            case "4":
                txtOutput.append("\n\n=== MÓDULO DE EVENTOS ===");
                break;
            case "5":
                txtOutput.append("\n\n=== CONFIGURACIÓN ===");
                break;
        }
        txtOutput.append("\nIngrese 'volver' para regresar al menú principal");
    }

    private void executeCommand(String command) {
        if (command.equals("volver")) {
            showMainMenu();
        } else {
            txtOutput.append("\n\nComando ejecutado: " + command);
        }
    }

    private void showHelp(String specificCommand) {
        if (specificCommand != null && helpCommands.containsKey(specificCommand)) {
            txtOutput.append("\n\nAyuda para " + specificCommand + ": " + 
                            helpCommands.get(specificCommand));
            return;
        }
        
        StringBuilder helpText = new StringBuilder("\n\n=== SISTEMA DE AYUDA ===\n");
        helpText.append("Comandos disponibles:\n");
        helpCommands.forEach((cmd, desc) -> helpText.append("- ").append(cmd).append(": ").append(desc).append("\n"));
        helpText.append("\nAtajos:\n- Ctrl+/: Mostrar ayuda\n- 'volver': Regresar al menú principal");
        
        txtOutput.append(helpText.toString());
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
            java.util.logging.Logger.getLogger(InterfazUsuarioFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(InterfazUsuarioFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(InterfazUsuarioFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(InterfazUsuarioFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new InterfazUsuarioFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
