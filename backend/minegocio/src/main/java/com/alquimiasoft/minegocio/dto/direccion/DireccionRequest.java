package com.alquimiasoft.minegocio.dto.direccion;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO para crear/actualizar una dirección
 */
public class DireccionRequest {

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
    public DireccionRequest() {}

    public DireccionRequest(String provincia, String ciudad, String direccion) {
        this.provincia = provincia;
        this.ciudad = ciudad;
        this.direccion = direccion;
    }

    // Getters y Setters
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
        return "DireccionRequest{" +
                "provincia='" + provincia + '\'' +
                ", ciudad='" + ciudad + '\'' +
                ", direccion='" + direccion + '\'' +
                '}';
    }
}