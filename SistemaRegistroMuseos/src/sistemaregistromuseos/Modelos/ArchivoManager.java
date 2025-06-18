/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sistemaregistromuseos.Modelos;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author a-nttBD_TXT
 */
public class ArchivoManager {
    public static void crearArchivoSiNoExiste(String nombreArchivo) throws IOException {
        File archivo = new File(nombreArchivo);
        if (!archivo.exists()) {
            archivo.createNewFile();
        }
    }

    public static List<String> leerLineas(String nombreArchivo) throws IOException {
        List<String> lineas = new ArrayList<>();
        crearArchivoSiNoExiste(nombreArchivo);
        
        try (BufferedReader reader = new BufferedReader(new FileReader(nombreArchivo))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                if (!linea.trim().isEmpty()) {
                    lineas.add(linea);
                }
            }
        }
        
        return lineas;
    }
}
