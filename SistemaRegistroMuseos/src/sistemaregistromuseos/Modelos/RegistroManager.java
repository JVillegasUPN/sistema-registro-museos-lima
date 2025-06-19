package sistemaregistromuseos.Modelos;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.swing.JOptionPane;

public class RegistroManager {
    private static final String ARCHIVO_REGISTROS = "src/sistemaregistromuseos/BD_TXT/visitantes.json";
    private static final String ARCHIVO_ANULADOS = "src/sistemaregistromuseos/BD_TXT/anulados.json";
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

    private String convertirVisitanteAJson(Visitante visitante) {
        return String.format(
            "{\n" +
            "  \"dni\": \"%s\",\n" +
            "  \"nombre_completo\": \"%s\",\n" +
            "  \"tipo_visitante\": \"%s\",\n" +
            "  \"codigo_visita\": \"%s\",\n" +
            "  \"fecha_registro\": \"%s\",\n" +
            "  \"estado\": \"%s\"\n" +
            "}",
            escapeJson(visitante.getDni()),
            escapeJson(visitante.getNombreCompleto()),
            visitante.getTipo().name(),
            visitante.getCodigoVisita(),
            visitante.getFechaFormateada(),
            visitante.getEstado()
        );
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

    private void guardarJsonEnArchivo(List<String> registros, String archivo) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(archivo))) {
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

    private List<String> leerRegistrosJson(String archivo) throws IOException {
        List<String> registros = new ArrayList<>();
        File file = new File(archivo);
        
        if (!file.exists() || file.length() == 0) {
            return registros;
        }

        String contenido = new String(Files.readAllBytes(file.toPath()));
        if (contenido.trim().isEmpty()) {
            return registros;
        }

        // Eliminar corchetes exteriores y dividir registros
        contenido = contenido.trim().substring(1, contenido.length() - 1).trim();
        String[] registrosArray = contenido.split("(?<=}),\\s*");

        for (String registro : registrosArray) {
            if (!registro.trim().isEmpty()) {
                registros.add(registro.trim());
            }
        }

        return registros;
    }

    public void registrarVisitante(Visitante visitante) {
        visitante.generarCodigoVisita(++ultimaSecuencia);
        visitantes.add(visitante);
        guardarVisitanteEnArchivo(visitante);
    }

    private void guardarVisitanteEnArchivo(Visitante visitante) {
        try {
            List<String> registros = leerRegistrosJson(ARCHIVO_REGISTROS);
            registros.add(convertirVisitanteAJson(visitante));
            guardarJsonEnArchivo(registros, ARCHIVO_REGISTROS);
            
            JOptionPane.showMessageDialog(null,
                "Registro guardado exitosamente",
                "Éxito",
                JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null,
                "Error al guardar: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private int cargarUltimaSecuencia() {
        try {
            List<String> registros = leerRegistrosJson(ARCHIVO_REGISTROS);
            if (registros.isEmpty()) return 0;

            String ultimoRegistro = registros.get(registros.size() - 1);
            int start = ultimoRegistro.indexOf("\"codigo_visita\": \"") + 17;
            int end = ultimoRegistro.indexOf("\"", start);

            if (start >= 17 && end > start) {
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

    public List<Visitante> getVisitantesRegistrados(boolean soloActivos) {
        List<Visitante> visitantes = new ArrayList<>();
        try {
            List<String> registros = leerRegistrosJson(ARCHIVO_REGISTROS);
            for (String registro : registros) {
                Visitante visitante = parsearVisitanteDesdeJson(registro);
                if (visitante != null) {
                    if (!soloActivos || "ACTIVO".equals(visitante.getEstado())) {
                        visitantes.add(visitante);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error al leer visitantes: " + e.getMessage());
        }
        return visitantes;
    }

    private Visitante parsearVisitanteDesdeJson(String json) {
        try {
            Visitante visitante = new Visitante();
            visitante.setDni(extraerCampoJson(json, "dni"));
            visitante.setNombreCompleto(extraerCampoJson(json, "nombre_completo"));
            visitante.setTipo(Visitante.TipoVisitante.valueOf(extraerCampoJson(json, "tipo_visitante")));
            visitante.setCodigoVisita(extraerCampoJson(json, "codigo_visita"));
            
            // Campo opcional estado
            if (json.contains("\"estado\":")) {
                visitante.setEstado(extraerCampoJson(json, "estado"));
            }
            
            return visitante;
        } catch (Exception e) {
            System.err.println("Error al parsear JSON: " + e.getMessage());
            return null;
        }
    }

    private String extraerCampoJson(String json, String campo) {
        String busqueda = "\"" + campo + "\": \"";
        int start = json.indexOf(busqueda) + busqueda.length();
        int end = json.indexOf("\"", start);
        return json.substring(start, end);
    }

    public boolean existeRegistroDuplicado(String dni) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String hoy = sdf.format(new Date());

        try {
            List<String> registros = leerRegistrosJson(ARCHIVO_REGISTROS);
            for (String registro : registros) {
                if (registro.contains("\"dni\": \"" + dni + "\"") && 
                    registro.contains("\"fecha_registro\": \"" + hoy)) {
                    return true;
                }
            }
            return false;
        } catch (IOException e) {
            System.err.println("Error verificando duplicados: " + e.getMessage());
            return false;
        }
    }

    public void anularRegistro(String codigoVisita, String motivo) throws IOException {
        // 1. Leer registros
        List<String> registros = leerRegistrosJson(ARCHIVO_REGISTROS);
        String registroOriginal = null;
        String registroAnulado = null;
        int indice = -1;

        // 2. Buscar y modificar registro
        for (int i = 0; i < registros.size(); i++) {
            if (registros.get(i).contains("\"codigo_visita\": \"" + codigoVisita + "\"")) {
                registroOriginal = registros.get(i);
                indice = i;

                // Eliminar el campo estado existente y cualquier coma residual
                String registroSinEstado = registroOriginal
                    .replaceAll("\"estado\": \"[^\"]*\"", "")  // Elimina el campo estado
                    .replaceAll(", ,", ",")  // Elimina comas dobles
                    .replaceAll(", \\}", "}")  // Elimina comas antes de }
                    .replaceAll("\\{, ", "{");  // Elimina comas después de {

                // Añadir el nuevo estado y motivo
                if (registroSinEstado.trim().endsWith("{")) {
                    // Si no hay otros campos, añadir sin coma
                    registroAnulado = registroSinEstado.replaceFirst(
                        "\\}", 
                        "\"estado\": \"ANULADO\", \"motivo_anulacion\": \"" + escapeJson(motivo) + "\"}"
                    );
                } else {
                    // Si hay otros campos, añadir con coma
                    registroAnulado = registroSinEstado.replaceFirst(
                        "\\}", 
                        ", \"estado\": \"ANULADO\", \"motivo_anulacion\": \"" + escapeJson(motivo) + "\"}"
                    );
                }
                break;
            }
        }

        if (registroOriginal == null) {
            throw new IllegalArgumentException("Código no encontrado: " + codigoVisita);
        }

        // 3. Registrar anulación
        registrarAnulacion(codigoVisita, motivo, registroAnulado);

        // 4. Actualizar registro
        registros.set(indice, registroAnulado);
        guardarJsonEnArchivo(registros, ARCHIVO_REGISTROS);
    }

    private void registrarAnulacion(String codigoVisita, String motivo, String registroAnulado) throws IOException {
        List<String> anulaciones = leerRegistrosJson(ARCHIVO_ANULADOS);
        
        String anulacion = String.format(
            "{\n" +
            "  \"codigo_visita\": \"%s\",\n" +
            "  \"fecha_anulacion\": \"%s\",\n" +
            "  \"motivo\": \"%s\",\n" +
            "  \"registro_original\": %s\n" +
            "}",
            codigoVisita,
            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()),
            escapeJson(motivo),
            registroAnulado
        );

        anulaciones.add(anulacion);
        guardarJsonEnArchivo(anulaciones, ARCHIVO_ANULADOS);
    }
    
    public List<Visitante> buscarPorDni(String dni) throws IOException {
        List<Visitante> resultados = new ArrayList<>();
        List<String> registros = leerRegistrosJson(ARCHIVO_REGISTROS);

        for (String registro : registros) {
            if (registro.contains("\"dni\": \"" + dni + "\"")) {
                Visitante visitante = parsearVisitanteDesdeJson(registro);
                if (visitante != null) {
                    resultados.add(visitante);
                }
            }
        }

        return resultados;
    }

    public List<Visitante> buscarPorFecha(String fecha) throws IOException {
        List<Visitante> resultados = new ArrayList<>();
        List<String> registros = leerRegistrosJson(ARCHIVO_REGISTROS);

        for (String registro : registros) {
            if (registro.contains("\"fecha_registro\": \"" + fecha)) {
                Visitante visitante = parsearVisitanteDesdeJson(registro);
                if (visitante != null) {
                    resultados.add(visitante);
                }
            }
        }

        // Ordenar por hora de registro
        resultados.sort((v1, v2) -> v1.getFechaRegistro().compareTo(v2.getFechaRegistro()));

        return resultados;
    }

    public List<Visitante> filtrarPorTipo(Visitante.TipoVisitante tipo) throws IOException {
        List<Visitante> resultados = new ArrayList<>();
        List<String> registros = leerRegistrosJson(ARCHIVO_REGISTROS);

        for (String registro : registros) {
            if (registro.contains("\"tipo_visitante\": \"" + tipo.name() + "\"")) {
                Visitante visitante = parsearVisitanteDesdeJson(registro);
                if (visitante != null) {
                    resultados.add(visitante);
                }
            }
        }

        return resultados;
    }

    public String generarEstadisticas() throws IOException {
        StringBuilder sb = new StringBuilder();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String hoy = sdf.format(new Date());

        // Estadísticas diarias
        List<Visitante> visitasHoy = buscarPorFecha(hoy);
        sb.append("ESTADÍSTICAS DEL DÍA (").append(hoy).append(")\n");
        sb.append("================================\n");
        sb.append("Total visitantes hoy: ").append(visitasHoy.size()).append("\n");

        // Conteo por tipo
        sb.append("\nDistribución por tipo:\n");
        for (Visitante.TipoVisitante tipo : Visitante.TipoVisitante.values()) {
            long count = visitasHoy.stream().filter(v -> v.getTipo() == tipo).count();
            sb.append("- ").append(tipo).append(": ").append(count).append("\n");
        }

        return sb.toString();
    }

    public void exportarResultados(String contenido) throws IOException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String nombreArchivo = "reporte_busqueda_" + sdf.format(new Date()) + ".txt";
        Path path = Paths.get("src/sistemaregistromuseos/BD_TXT/" + nombreArchivo);

        // Crear directorio si no existe
        Files.createDirectories(path.getParent());

        // Escribir archivo
        Files.write(path, contenido.getBytes(), StandardOpenOption.CREATE);
    }
    
    public String generarReporteDiario() throws IOException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String fechaHoy = sdf.format(new Date());
        String nombreArchivo = "reporte_diario_" + fechaHoy + ".txt";
        Path path = Paths.get("src/sistemaregistromuseos/BD_TXT/" + nombreArchivo);

        String contenido = generarEstadisticas();
        Files.write(path, contenido.getBytes(), StandardOpenOption.CREATE);

        return "Reporte diario generado: " + nombreArchivo + "\n\n" + contenido;
    }

    public List<String> contarVisitantesPorTipo() throws IOException {
        List<String> resultados = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String hoy = sdf.format(new Date());

        List<Visitante> visitasHoy = buscarPorFecha(hoy);

        for (Visitante.TipoVisitante tipo : Visitante.TipoVisitante.values()) {
            long count = visitasHoy.stream().filter(v -> v.getTipo() == tipo).count();
            resultados.add(tipo.toString() + ": " + count);
        }

        return resultados;
    }

    public int contarVisitantesHoy() throws IOException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String hoy = sdf.format(new Date());
        return buscarPorFecha(hoy).size();
    }
}