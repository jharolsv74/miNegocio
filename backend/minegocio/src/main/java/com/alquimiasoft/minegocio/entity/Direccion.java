package com.alquimiasoft.minegocio.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Entidad Direccion que representa una dirección del cliente
 */
@Entity
@Table(name = "direccion",
       indexes = {
           @Index(name = "idx_direccion_cliente", columnList = "cliente_id"),
           @Index(name = "idx_direccion_es_matriz", columnList = "es_matriz")
       })
public class Direccion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false, foreignKey = @ForeignKey(name = "fk_direccion_cliente"))
    @NotNull(message = "El cliente es obligatorio")
    private Cliente cliente;

    @NotBlank(message = "La provincia es obligatoria")
    @Size(max = 100, message = "La provincia no puede exceder 100 caracteres")
    @Column(name = "provincia", nullable = false, length = 100)
    private String provincia;

    @NotBlank(message = "La ciudad es obligatoria")
    @Size(max = 100, message = "La ciudad no puede exceder 100 caracteres")
    @Column(name = "ciudad", nullable = false, length = 100)
    private String ciudad;

    @NotBlank(message = "La dirección es obligatoria")
    @Size(max = 500, message = "La dirección no puede exceder 500 caracteres")
    @Column(name = "direccion_texto", nullable = false, length = 500)
    private String direccionTexto;

    @Column(name = "es_matriz", nullable = false)
    private boolean esMatriz = false;

    @CreationTimestamp
    @Column(name = "creado_en", nullable = false, updatable = false)
    private LocalDateTime creadoEn;

    // Constructores
    public Direccion() {}

    public Direccion(Cliente cliente, String provincia, String ciudad, 
                     String direccionTexto, boolean esMatriz) {
        this.cliente = cliente;
        this.provincia = provincia;
        this.ciudad = ciudad;
        this.direccionTexto = direccionTexto;
        this.esMatriz = esMatriz;
    }

    public Direccion(String provincia, String ciudad, String direccionTexto, boolean esMatriz) {
        this.provincia = provincia;
        this.ciudad = ciudad;
        this.direccionTexto = direccionTexto;
        this.esMatriz = esMatriz;
    }

    // Métodos de utilidad
    /**
     * Obtiene la dirección completa formateada
     * @return String con formato
     */
    public String getDireccionCompleta() {
        return String.format("%s, %s, %s", direccionTexto, ciudad, provincia);
    }

    /**
     * Verifica si esta es la dirección matriz del cliente
     * @return true si es dirección matriz
     */
    public boolean isDireccionMatriz() {
        return esMatriz;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
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

    public String getDireccionTexto() {
        return direccionTexto;
    }

    public void setDireccionTexto(String direccionTexto) {
        this.direccionTexto = direccionTexto;
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

    // equals y hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Direccion direccion = (Direccion) o;
        return Objects.equals(id, direccion.id) &&
               Objects.equals(provincia, direccion.provincia) &&
               Objects.equals(ciudad, direccion.ciudad) &&
               Objects.equals(direccionTexto, direccion.direccionTexto);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, provincia, ciudad, direccionTexto);
    }

    @Override
    public String toString() {
        return "Direccion{" +
                "id=" + id +
                ", provincia='" + provincia + '\'' +
                ", ciudad='" + ciudad + '\'' +
                ", direccionTexto='" + direccionTexto + '\'' +
                ", esMatriz=" + esMatriz +
                ", creadoEn=" + creadoEn +
                '}';
    }
}