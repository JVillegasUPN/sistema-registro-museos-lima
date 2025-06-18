/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sistemaregistromuseos.Modelos;

import com.google.gson.annotations.SerializedName;
import java.util.Date;
import java.text.SimpleDateFormat;

/**
 *
 * @author a-ntt
 */
//Codigo de prueba
public class Visitante {
    private String dni;
    private String nombreCompleto;
    private TipoVisitante tipo;
    private String codigoVisita;
    private String fechaRegistro;  // Cambiado de Date a String
    
    public enum TipoVisitante {
        NACIONAL("Visitante Nacional"),
        EXTRANJERO("Visitante Extranjero"),
        ESTUDIANTE("Estudiante");

        private final String descripcion;

        private TipoVisitante(String descripcion) {
            this.descripcion = descripcion;
        }

        @Override
        public String toString() {
            return descripcion;
        }
    }
    
    public Visitante() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        this.fechaRegistro = sdf.format(new Date());
    }
    
    public boolean validarDNI(String dni) {
        if (dni == null || dni.length() != 8) {
            return false;
        }
        
        try {
            Integer.parseInt(dni);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    
    public boolean validarNombre(String nombre) {
        if (nombre == null || nombre.trim().length() < 3) {
            return false;
        }
        
        if (!nombre.matches("^[\\p{L} .'-]+$")) {
            return false;
        }
        
        if (nombre.matches(".*\\d.*")) {
            return false;
        }
        
        return true;
    }
    
    public void generarCodigoVisita(int secuencia) {
        String año = this.fechaRegistro.substring(0, 4);  // Extrae "yyyy" del String

        switch(this.tipo) {
            case EXTRANJERO:
                // Código con letras hasta 20 caracteres
                String letras = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
                StringBuilder codigoExtranjero = new StringBuilder();
                for (int i = 0; i < 8; i++) {  // 8 letras aleatorias
                    int index = (int)(letras.length() * Math.random());
                    codigoExtranjero.append(letras.charAt(index));
                }
                this.codigoVisita = String.format("EXT-%s-%s", codigoExtranjero.toString(), año);
                break;

            case ESTUDIANTE:
                // Código con números y letras (10 caracteres)
                String caracteres = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
                StringBuilder codigoEstudiante = new StringBuilder();
                for (int i = 0; i < 10; i++) {
                    int index = (int)(caracteres.length() * Math.random());
                    codigoEstudiante.append(caracteres.charAt(index));
                }
                this.codigoVisita = String.format("EST-%s-%s", codigoEstudiante.toString(), año);
                break;

            case NACIONAL:
            default:
                // Formato original para nacionales
                this.codigoVisita = String.format("VIS-%03d-%s", secuencia, año);
                break;
        }
    }

    public String getCodigoVisita() {
        return codigoVisita;
    }

    // Getters y Setters
    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        if (this.tipo == TipoVisitante.NACIONAL) {
            if (dni == null || dni.length() != 8) {
                throw new IllegalArgumentException("DNI debe tener 8 dígitos exactos para visitantes nacionales");
            }
            try {
                Integer.parseInt(dni);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("DNI debe contener solo números para visitantes nacionales");
            }
        } else if (this.tipo == TipoVisitante.ESTUDIANTE) {
            if (dni == null || dni.length() != 10) {
                throw new IllegalArgumentException("Código de estudiante debe tener 10 caracteres");
            }
        } else if (this.tipo == TipoVisitante.EXTRANJERO) {
            if (dni == null || dni.length() > 20) {
                throw new IllegalArgumentException("Identificación de extranjero no puede exceder 20 caracteres");
            }
        }
        this.dni = dni;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        if (validarNombre(nombreCompleto)) {
            this.nombreCompleto = nombreCompleto;
        } else {
            throw new IllegalArgumentException("Nombre debe tener mínimo 3 letras sin números");
        }
    }

    public TipoVisitante getTipo() {
        return tipo;
    }

    public void setTipo(TipoVisitante tipo) {
        this.tipo = tipo;
    }
    
    public String getFechaRegistro() {
        return fechaRegistro;
    }

    public String getFechaFormateada() {
        return fechaRegistro;  // Ya está formateada desde el constructor
    }
}
