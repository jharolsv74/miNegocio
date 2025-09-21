package com.alquimiasoft.minegocio.dto.direccion;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * DTO para crear una nueva dirección adicional para un cliente
 */
public class DireccionCreateRequest {

    @NotNull(message = "El ID del cliente es obligatorio")
    private Long clienteId;

    @NotBlank(message = "La provincia es obligatoria")
    @Size(max = 100, message = "La provincia no puede exceder 100 caracteres")
    private String provincia;

    @NotBlank(message = "La ciudad es obligatoria")
    @Size(max = 100, message = "La ciudad no puede exceder 100 caracteres")
    private String ciudad;

    @NotBlank(message = "La dirección es obligatoria")
    @Size(max = 500, message = "La dirección no puede exceder 500 caracteres")
    private String direccion;

    // Constructores
    public DireccionCreateRequest() {}

    public DireccionCreateRequest(Long clienteId, String provincia, String ciudad, String direccion) {
        this.clienteId = clienteId;
        this.provincia = provincia;
        this.ciudad = ciudad;
        this.direccion = direccion;
    }

    // Getters y Setters
    public Long getClienteId() {
        return clienteId;
    }

    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
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

    @Override
    public String toString() {
        return "DireccionCreateRequest{" +
                "clienteId=" + clienteId +
                ", provincia='" + provincia + '\'' +
                ", ciudad='" + ciudad + '\'' +
                ", direccion='" + direccion + '\'' +
                '}';
    }
}