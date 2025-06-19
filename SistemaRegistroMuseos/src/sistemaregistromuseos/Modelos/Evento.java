/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sistemaregistromuseos.Modelos;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author a-ntt
 */
public class Evento {
    private String codigo;
    private String nombre;
    private Date fecha;
    private int cupoMaximo;
    private int cuposDisponibles;
    private List<String> participantes;
    private boolean activo;
    private String motivoCancelacion;
    
    public Evento(String nombre, Date fecha, int cupoMaximo) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        this.codigo = generarCodigoEvento(fecha);
        this.nombre = nombre;
        this.fecha = fecha;
        this.cupoMaximo = cupoMaximo;
        this.cuposDisponibles = cupoMaximo;
        this.participantes = new ArrayList<>();
        this.activo = true;
        this.motivoCancelacion = "";
    }
    
    private String generarCodigoEvento(Date fecha) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        return "EVT-" + sdf.format(fecha) + "-" + System.currentTimeMillis();
    }
    
    // Getters y setters
    public String getCodigo() {
        return codigo;
    }
    
    public String getNombre() {
        return nombre;
    }
    
    public Date getFecha() {
        return fecha;
    }
    
    public int getCupoMaximo() {
        return cupoMaximo;
    }
    
    public int getCuposDisponibles() {
        return cuposDisponibles;
    }
    
    public boolean isActivo() {
        return activo;
    }
    
    public List<String> getParticipantes() {
        return new ArrayList<>(participantes); // Devuelve una copia para proteger la lista original
    }
    
    public void cancelar(String motivo) {
        this.activo = false;
        this.motivoCancelacion = motivo;
    }

    public boolean inscribirParticipante(String dni) {
        if (cuposDisponibles > 0 && !participantes.contains(dni)) {
            participantes.add(dni);
            cuposDisponibles--;
            return true;
        }
        return false;
    }
    
    public boolean contieneParticipante(String dni) {
        return participantes.contains(dni);
    }
    
    public String getFechaFormateada() {
        return new SimpleDateFormat("yyyy-MM-dd").format(fecha);
    }
    

    @Override
    public String toString() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return String.format("Evento [%s] %s - Fecha: %s - Cupos: %d/%d",
                codigo, nombre, sdf.format(fecha), cuposDisponibles, cupoMaximo);
    }
    
    protected void setActivo(boolean activo) {
        this.activo = activo;
    }
    
    protected void setCuposDisponibles(int cuposDisponibles) {
        this.cuposDisponibles = cuposDisponibles;
    }
    
    protected void setParticipantes(List<String> participantes) {
        this.participantes = new ArrayList<>(participantes);
    }

    public static Evento fromJson(String json) {
        try {
            String codigo = extraerCampo(json, "codigo");
            String nombre = extraerCampo(json, "nombre");
            
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date fecha = sdf.parse(extraerCampo(json, "fecha"));
            
            int cupoMaximo = Integer.parseInt(extraerCampo(json, "cupo_maximo"));
            int cuposDisponibles = Integer.parseInt(extraerCampo(json, "cupos_disponibles"));
            boolean activo = Boolean.parseBoolean(extraerCampo(json, "activo"));
            
            Evento evento = new Evento(nombre, fecha, cupoMaximo);
            evento.codigo = codigo; // Mantener el código original
            evento.setCuposDisponibles(cuposDisponibles);
            evento.setActivo(activo);
            
            // Manejar motivo de cancelación si existe
            if (json.contains("\"motivo_cancelacion\":")) {
                evento.motivoCancelacion = extraerCampo(json, "motivo_cancelacion");
            }
            
            return evento;
        } catch (Exception e) {
            System.err.println("Error parseando evento: " + e.getMessage());
            return null;
        }
    }
    
    private static String extraerCampo(String json, String campo) {
        String busqueda = "\"" + campo + "\": \"";
        int start = json.indexOf(busqueda) + busqueda.length();
        int end = json.indexOf("\"", start);
        return json.substring(start, end);
    }
}
