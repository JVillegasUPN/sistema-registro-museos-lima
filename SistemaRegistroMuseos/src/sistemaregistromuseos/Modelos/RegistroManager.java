package sistemaregistromuseos.Modelos;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import java.util.List;
import java.util.Optional;
import javax.swing.JOptionPane;

public class RegistroManager {
    private static final String ARCHIVO_REGISTROS = "src/sistemaregistromuseos/BD_TXT/visitantes.txt";
    private List<Visitante> visitantes;
    private int ultimaSecuencia;

    public RegistroManager() {
        crearDirectorioSiNoExiste();
        this.visitantes = new ArrayList<>();
        this.ultimaSecuencia = cargarUltimaSecuencia();
    }
    
    private void crearDirectorioSiNoExiste() {
        File directorio = new File("src/sistemaregistromuseos/BD_TXT");
        if (!directorio.exists()) {
            boolean creado = directorio.mkdirs();
            if (!creado) {
                JOptionPane.showMessageDialog(null,
                    "No se pudo crear el directorio BD_TXT",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private File obtenerArchivoRegistros() {
        return new File(ARCHIVO_REGISTROS);
    }

    public void registrarVisitante(Visitante visitante) {
        visitante.generarCodigoVisita(++ultimaSecuencia);
        visitantes.add(visitante);
        guardarVisitanteEnArchivo(visitante);
    }
    
     private void guardarVisitanteEnArchivo(Visitante visitante) {
        File archivo = obtenerArchivoRegistros();
        
        try {
            // Verificar ruta para depuración
            System.out.println("Intentando guardar en: " + archivo.getAbsolutePath());
            
            if (!archivo.exists()) {
                archivo.createNewFile();
            }

            List<String> registrosExistentes = leerRegistrosExistentes();
            String nuevoRegistro = convertirVisitanteAJson(visitante);
            registrosExistentes.add(nuevoRegistro);
            
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(archivo))) {
                bw.write("[\n");
                for (int i = 0; i < registrosExistentes.size(); i++) {
                    bw.write(registrosExistentes.get(i));
                    if (i < registrosExistentes.size() - 1) {
                        bw.write(",\n");
                    }
                }
                bw.write("\n]");
            }
            
            JOptionPane.showMessageDialog(null,
                "Registro guardado exitosamente en:\n" + 
                archivo.getAbsolutePath(),
                "Éxito",
                JOptionPane.INFORMATION_MESSAGE);
                
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null,
                "Error al guardar en:\n" + 
                archivo.getAbsolutePath() + 
                "\nError: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private List<String> leerRegistrosExistentes() throws IOException {
        List<String> registros = new ArrayList<>();
        File archivo = new File(ARCHIVO_REGISTROS);
        
        if (!archivo.exists() || archivo.length() == 0) {
            return registros;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(ARCHIVO_REGISTROS))) {
            StringBuilder jsonBuilder = new StringBuilder();
            String linea;
            
            while ((linea = br.readLine()) != null) {
                jsonBuilder.append(linea);
            }
            
            String jsonCompleto = jsonBuilder.toString();
            
            // Extraer registros individuales del array JSON
            if (jsonCompleto.startsWith("[") && jsonCompleto.endsWith("]")) {
                String contenido = jsonCompleto.substring(1, jsonCompleto.length() - 1);
                String[] registrosArray = contenido.split("(?<=}),");
                
                for (String registro : registrosArray) {
                    if (!registro.trim().isEmpty()) {
                        registros.add(registro.trim());
                    }
                }
            }
        }
        
        return registros;
    }

    private String convertirVisitanteAJson(Visitante visitante) {
        return String.format(
            "{\n" +
            "  \"dni\": \"%s\",\n" +
            "  \"nombre_completo\": \"%s\",\n" +
            "  \"tipo_visitante\": \"%s\",\n" +
            "  \"codigo_visita\": \"%s\",\n" +
            "  \"fecha_registro\": \"%s\"\n" +
            "}",
            escapeJson(visitante.getDni()),
            escapeJson(visitante.getNombreCompleto()),
            visitante.getTipo().name(),
            visitante.getCodigoVisita(),
            visitante.getFechaFormateada()
        );
    }

    private void escribirTodosLosRegistros(List<String> registros) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ARCHIVO_REGISTROS))) {
            bw.write("[\n");
            
            for (int i = 0; i < registros.size(); i++) {
                bw.write(registros.get(i));
                if (i < registros.size() - 1) {
                    bw.write(",\n");
                }
            }
            
            bw.write("\n]");
        }
    }

    private String escapeJson(String input) {
        if (input == null) return "";
        return input.replace("\\", "\\\\")
                   .replace("\"", "\\\"")
                   .replace("\b", "\\b")
                   .replace("\f", "\\f")
                   .replace("\n", "\\n")
                   .replace("\r", "\\r")
                   .replace("\t", "\\t");
    }

    private int cargarUltimaSecuencia() {
        try {
            List<String> registros = leerRegistrosExistentes();
            if (registros.isEmpty()) return 0;

            String ultimoRegistro = registros.get(registros.size() - 1);
            int start = ultimoRegistro.indexOf("\"codigo_visita\": \"") + 17;
            int end = ultimoRegistro.indexOf("\"", start);

            if (start >= 17 && end > start) {  // Validación más estricta
                String codigo = ultimoRegistro.substring(start, end);
                String[] partes = codigo.split("-");
                if (partes.length >= 2) {
                    try {
                        return Integer.parseInt(partes[1]);
                    } catch (NumberFormatException e) {
                        System.err.println("Formato de secuencia inválido: " + partes[1]);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error al cargar secuencia: " + e.getMessage());
        }
        return 0;
    }

    public List<Visitante> getVisitantesRegistrados() {
        return new ArrayList<>(visitantes);
    }
    
    public boolean existeRegistroDuplicado(String dni) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String hoy = sdf.format(new Date());

        try {
            List<String> registros = leerRegistrosExistentes();
            return registros.stream().anyMatch(registro -> 
                registro.contains("\"dni\":\"" + dni + "\"") &&
                registro.contains("\"fecha_registro\":\"" + hoy)
            );
        } catch (IOException e) {
            System.err.println("Error verificando duplicados: " + e.getMessage());
            return false;
        }
    }
    
    public void anularRegistro(String codigoVisita, String motivo) throws IOException {
        // 1. Buscar registro
        List<String> registros = leerRegistrosExistentes();
        Optional<String> registroOpt = registros.stream()
            .filter(r -> r.contains("\"codigo_visita\":\"" + codigoVisita + "\""))
            .findFirst();

        if (!registroOpt.isPresent()) {
            throw new IllegalArgumentException("Código de visita no encontrado");
        }

        // 2. Registrar anulación en errores.txt
        String anulacion = String.format("[%s] ANULADO - Código: %s - Motivo: %s",
            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()),
            codigoVisita,
            motivo);

        Files.write(Paths.get("src/sistemaregistromuseos/BD_TXT/errores.txt"), 
            (anulacion + System.lineSeparator()).getBytes(),
            StandardOpenOption.CREATE, StandardOpenOption.APPEND);

        // 3. Marcar como anulado en el registro original
        String registroAnulado = registroOpt.get()
            .replaceFirst("\\}$", ", \"anulado\": true, \"motivo\": \"" + escapeJson(motivo) + "\"}");

        registros.remove(registroOpt.get());
        registros.add(registroAnulado);

        // 4. Reescribir archivo
        escribirTodosLosRegistros(registros);
    }
}