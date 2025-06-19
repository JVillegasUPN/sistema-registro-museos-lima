/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sistemaregistromuseos.Modelos;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.swing.JOptionPane;

/**
 *
 * @author a-ntt
 */
public class EventoManager {
    private static final String ARCHIVO_EVENTOS = "src/sistemaregistromuseos/BD_TXT/eventos.json";
    private List<Evento> eventos;
    
    // Excepciones personalizadas
    public static class EventoException extends Exception {
        public EventoException(String mensaje) {
            super(mensaje);
        }
    }
    
    public static class EventoNoEncontradoException extends EventoException {
        public EventoNoEncontradoException(String codigo) {
            super("No se encontró el evento con código: " + codigo);
        }
    }
    
    public static class DNIYaInscritoException extends EventoException {
        public DNIYaInscritoException(String dni) {
            super("El DNI " + dni + " ya está inscrito en este evento");
        }
    }
    
    public static class CupoLlenoException extends EventoException {
        public CupoLlenoException() {
            super("No hay cupos disponibles para este evento");
        }
    }
    
    public static class FechaPasadaException extends EventoException {
        public FechaPasadaException() {
            super("La fecha del evento debe ser futura");
        }
    }
    
    public static class CupoInvalidoException extends EventoException {
        public CupoInvalidoException() {
            super("El cupo máximo debe ser mayor a cero");
        }
    }

    public EventoManager() {
        crearDirectorioSiNoExiste();
        this.eventos = new ArrayList<>();
        cargarEventos();
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
    
    private void cargarEventos() {
        try {
            List<String> registros = leerRegistrosJson();
            for (String registro : registros) {
                Evento evento = parsearEventoDesdeJson(registro);
                if (evento != null) {
                    eventos.add(evento);
                }
            }
        } catch (Exception e) {
            System.err.println("Error cargando eventos: " + e.getMessage());
        }
    }
    
    public void crearEvento(String nombre, Date fecha, int cupoMaximo) throws FechaPasadaException, CupoInvalidoException {
        if (fecha.before(new Date())) {
            throw new FechaPasadaException();
        }
        if (cupoMaximo <= 0) {
            throw new CupoInvalidoException();
        }
        
        Evento nuevoEvento = new Evento(nombre, fecha, cupoMaximo);
        eventos.add(nuevoEvento);
        guardarEvento(nuevoEvento);
    }
    
    private void guardarEvento(Evento evento) {
        try {
            List<String> registros = leerRegistrosJson();
            registros.add(convertirEventoAJson(evento));
            guardarJsonEnArchivo(registros);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                "Error al guardar evento: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public List<Evento> listarEventosActivos() {
        List<Evento> activos = new ArrayList<>();
        Date ahora = new Date();
        
        for (Evento e : eventos) {
            if (e.isActivo() && e.getFecha().after(ahora) && e.getCuposDisponibles() > 0) {
                activos.add(e);
            }
        }
        
        activos.sort((e1, e2) -> e1.getFecha().compareTo(e2.getFecha()));
        return activos;
    }
    
    public void inscribirEnEvento(String codigoEvento, String dni) 
            throws EventoNoEncontradoException, DNIYaInscritoException, CupoLlenoException {
        
        Evento evento = buscarEventoPorCodigo(codigoEvento);
        if (evento == null) {
            throw new EventoNoEncontradoException(codigoEvento);
        }
        
        if (evento.getParticipantes().contains(dni)) {
            throw new DNIYaInscritoException(dni);
        }
        
        if (evento.getCuposDisponibles() <= 0) {
            throw new CupoLlenoException();
        }
        
        evento.inscribirParticipante(dni);
        actualizarEventoEnArchivo(evento);
    }
    
    private Evento buscarEventoPorCodigo(String codigo) {
        return eventos.stream()
                     .filter(e -> e.getCodigo().equals(codigo))
                     .findFirst()
                     .orElse(null);
    }
    
    public void cancelarEvento(String codigoEvento, String motivo) throws EventoNoEncontradoException {
        Evento evento = buscarEventoPorCodigo(codigoEvento);
        if (evento == null) {
            throw new EventoNoEncontradoException(codigoEvento);
        }
        
        evento.cancelar(motivo);
        actualizarEventoEnArchivo(evento);
    }
    
    private void actualizarEventoEnArchivo(Evento evento) {
        try {
            List<String> registros = leerRegistrosJson();
            List<String> nuevosRegistros = new ArrayList<>();
            
            for (String registro : registros) {
                if (registro.contains("\"codigo\": \"" + evento.getCodigo() + "\"")) {
                    nuevosRegistros.add(convertirEventoAJson(evento));
                } else {
                    nuevosRegistros.add(registro);
                }
            }
            
            guardarJsonEnArchivo(nuevosRegistros);
        } catch (Exception e) {
            System.err.println("Error actualizando evento: " + e.getMessage());
        }
    }
    
    private List<String> leerRegistrosJson() {
        List<String> registros = new ArrayList<>();
        File file = new File(ARCHIVO_EVENTOS);
        
        if (!file.exists() || file.length() == 0) {
            return registros;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            StringBuilder contenido = new StringBuilder();
            String linea;
            while ((linea = reader.readLine()) != null) {
                contenido.append(linea);
            }
            
            if (contenido.length() == 0) {
                return registros;
            }

            String json = contenido.toString().trim();
            if (json.startsWith("[") && json.endsWith("]")) {
                json = json.substring(1, json.length() - 1).trim();
                String[] registrosArray = json.split("(?<=}),\\s*");
                
                for (String registro : registrosArray) {
                    if (!registro.trim().isEmpty()) {
                        registros.add(registro.trim());
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error leyendo archivo JSON: " + e.getMessage());
        }
        
        return registros;
    }
    
    private void guardarJsonEnArchivo(List<String> registros) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ARCHIVO_EVENTOS))) {
            writer.write("[\n");
            for (int i = 0; i < registros.size(); i++) {
                writer.write(registros.get(i));
                if (i < registros.size() - 1) {
                    writer.write(",\n");
                }
            }
            writer.write("\n]");
        } catch (Exception e) {
            System.err.println("Error guardando archivo JSON: " + e.getMessage());
        }
    }
    
    private String convertirEventoAJson(Evento evento) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return String.format(
            "{\n" +
            "  \"codigo\": \"%s\",\n" +
            "  \"nombre\": \"%s\",\n" +
            "  \"fecha\": \"%s\",\n" +
            "  \"cupo_maximo\": %d,\n" +
            "  \"cupos_disponibles\": %d,\n" +
            "  \"activo\": %b\n" +
            "}",
            evento.getCodigo(),
            escapeJson(evento.getNombre()),
            sdf.format(evento.getFecha()),
            evento.getCupoMaximo(),
            evento.getCuposDisponibles(),
            evento.isActivo()
        );
    }
    
    private Evento parsearEventoDesdeJson(String json) {
        try {
            String codigo = extraerCampoJson(json, "codigo");
            String nombre = extraerCampoJson(json, "nombre");
            
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date fecha = sdf.parse(extraerCampoJson(json, "fecha"));
            
            int cupoMaximo = Integer.parseInt(extraerCampoJson(json, "cupo_maximo"));
            int cuposDisponibles = Integer.parseInt(extraerCampoJson(json, "cupos_disponibles"));
            boolean activo = Boolean.parseBoolean(extraerCampoJson(json, "activo"));
            
            Evento evento = new Evento(nombre, fecha, cupoMaximo);
            // Actualizar estado interno
            for (int i = 0; i < (cupoMaximo - cuposDisponibles); i++) {
                evento.inscribirParticipante(""); // DNI vacío para ajustar cupos
            }
            if (!activo) {
                evento.cancelar("Cargado desde archivo");
            }
            
            return evento;
        } catch (Exception e) {
            System.err.println("Error parseando evento: " + e.getMessage());
            return null;
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
    
    private String extraerCampoJson(String json, String campo) {
        String busqueda = "\"" + campo + "\": \"";
        int start = json.indexOf(busqueda) + busqueda.length();
        int end = json.indexOf("\"", start);
        return json.substring(start, end);
    }
}