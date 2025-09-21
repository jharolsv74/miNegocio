package com.alquimiasoft.minegocio.dto.direccion;

import java.time.LocalDateTime;

/**
 * DTO para la respuesta de dirección
 */
public class DireccionResponse {

    private Long id;
    private String provincia;
    private String ciudad;
    private String direccion;
    private boolean esMatriz;
    private LocalDateTime creadoEn;

    // Constructores
    public DireccionResponse() {}

    public DireccionResponse(Long id, String provincia, String ciudad, String direccion, 
                            boolean esMatriz, LocalDateTime creadoEn) {
        this.id = id;
        this.provincia = provincia;
        this.ciudad = ciudad;
        this.direccion = direccion;
        this.esMatriz = esMatriz;
        this.creadoEn = creadoEn;
    }

    // Método de utilidad para obtener la dirección completa
    public String getDireccionCompleta() {
        return String.format("%s, %s, %s", direccion, ciudad, provincia);
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public boolean isEsMatriz() {
        return esMatriz;
    }

    public void setEsMatriz(boolean esMatriz) {
        this.esMatriz = esMatriz;
    }

    public LocalDateTime getCreadoEn() {
        return creadoEn;
    }

    public void setCreadoEn(LocalDateTime creadoEn) {
        this.creadoEn = creadoEn;
    }

    @Override
    public String toString() {
        return "DireccionResponse{" +
                "id=" + id +
                ", provincia='" + provincia + '\'' +
                ", ciudad='" + ciudad + '\'' +
                ", direccion='" + direccion + '\'' +
                ", esMatriz=" + esMatriz +
                ", creadoEn=" + creadoEn +
                '}';
    }
}