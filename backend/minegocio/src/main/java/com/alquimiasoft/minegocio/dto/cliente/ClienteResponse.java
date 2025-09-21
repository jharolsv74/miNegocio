package com.alquimiasoft.minegocio.dto.cliente;

import com.alquimiasoft.minegocio.dto.direccion.DireccionResponse;

import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO para la respuesta de cliente
 */
public class ClienteResponse {

    private Long id;
    private Long empresaId;
    private String tipoIdentificacion;
    private String numeroIdentificacion;
    private String nombres;
    private String correo;
    private String celular;
    private LocalDateTime creadoEn;
    private DireccionResponse direccionMatriz;
    private List<DireccionResponse> direccionesAdicionales;

    // Constructores
    public ClienteResponse() {}

    public ClienteResponse(Long id, Long empresaId, String tipoIdentificacion, String numeroIdentificacion,
                          String nombres, String correo, String celular, LocalDateTime creadoEn) {
        this.id = id;
        this.empresaId = empresaId;
        this.tipoIdentificacion = tipoIdentificacion;
        this.numeroIdentificacion = numeroIdentificacion;
        this.nombres = nombres;
        this.correo = correo;
        this.celular = celular;
        this.creadoEn = creadoEn;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getEmpresaId() {
        return empresaId;
    }

    public void setEmpresaId(Long empresaId) {
        this.empresaId = empresaId;
    }

    public String getTipoIdentificacion() {
        return tipoIdentificacion;
    }

    public void setTipoIdentificacion(String tipoIdentificacion) {
        this.tipoIdentificacion = tipoIdentificacion;
    }

    public String getNumeroIdentificacion() {
        return numeroIdentificacion;
    }

    public void setNumeroIdentificacion(String numeroIdentificacion) {
        this.numeroIdentificacion = numeroIdentificacion;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public LocalDateTime getCreadoEn() {
        return creadoEn;
    }

    public void setCreadoEn(LocalDateTime creadoEn) {
        this.creadoEn = creadoEn;
    }

    public DireccionResponse getDireccionMatriz() {
        return direccionMatriz;
    }

    public void setDireccionMatriz(DireccionResponse direccionMatriz) {
        this.direccionMatriz = direccionMatriz;
    }

    public List<DireccionResponse> getDireccionesAdicionales() {
        return direccionesAdicionales;
    }

    public void setDireccionesAdicionales(List<DireccionResponse> direccionesAdicionales) {
        this.direccionesAdicionales = direccionesAdicionales;
    }

    @Override
    public String toString() {
        return "ClienteResponse{" +
                "id=" + id +
                ", empresaId=" + empresaId +
                ", tipoIdentificacion='" + tipoIdentificacion + '\'' +
                ", numeroIdentificacion='" + numeroIdentificacion + '\'' +
                ", nombres='" + nombres + '\'' +
                ", correo='" + correo + '\'' +
                ", celular='" + celular + '\'' +
                ", creadoEn=" + creadoEn +
                ", direccionMatriz=" + direccionMatriz +
                ", direccionesAdicionales=" + direccionesAdicionales +
                '}';
    }
}