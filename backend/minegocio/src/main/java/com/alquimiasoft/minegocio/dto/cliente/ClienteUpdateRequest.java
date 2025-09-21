package com.alquimiasoft.minegocio.dto.cliente;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO para actualizar los datos de un cliente existente
 */
public class ClienteUpdateRequest {

    @NotBlank(message = "El tipo de identificación es obligatorio")
    @Size(max = 10, message = "El tipo de identificación no puede exceder 10 caracteres")
    private String tipoIdentificacion;

    @NotBlank(message = "El número de identificación es obligatorio")
    @Size(max = 50, message = "El número de identificación no puede exceder 50 caracteres")
    private String numeroIdentificacion;

    @NotBlank(message = "Los nombres son obligatorios")
    @Size(max = 255, message = "Los nombres no pueden exceder 255 caracteres")
    private String nombres;

    @Email(message = "El formato del correo electrónico no es válido")
    @Size(max = 150, message = "El correo no puede exceder 150 caracteres")
    private String correo;

    @Size(max = 50, message = "El número de celular no puede exceder 50 caracteres")
    private String celular;

    // Constructores
    public ClienteUpdateRequest() {}

    public ClienteUpdateRequest(String tipoIdentificacion, String numeroIdentificacion,
                               String nombres, String correo, String celular) {
        this.tipoIdentificacion = tipoIdentificacion;
        this.numeroIdentificacion = numeroIdentificacion;
        this.nombres = nombres;
        this.correo = correo;
        this.celular = celular;
    }

    // Getters y Setters
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

    @Override
    public String toString() {
        return "ClienteUpdateRequest{" +
                "tipoIdentificacion='" + tipoIdentificacion + '\'' +
                ", numeroIdentificacion='" + numeroIdentificacion + '\'' +
                ", nombres='" + nombres + '\'' +
                ", correo='" + correo + '\'' +
                ", celular='" + celular + '\'' +
                '}';
    }
}