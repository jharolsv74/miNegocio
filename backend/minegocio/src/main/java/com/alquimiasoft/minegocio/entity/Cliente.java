package com.alquimiasoft.minegocio.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Entidad Cliente
 */
@Entity
@Table(name = "cliente", 
       uniqueConstraints = {
           @UniqueConstraint(name = "uq_cliente_empresa_tipo_numero", 
                           columnNames = {"empresa_id", "tipo_identificacion", "numero_identificacion"})
       },
       indexes = {
           @Index(name = "idx_cliente_numero_identificacion", columnList = "numero_identificacion"),
           @Index(name = "idx_cliente_nombres_lower", columnList = "nombres")
       })
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "El ID de la empresa es obligatorio")
    @Column(name = "empresa_id", nullable = false)
    private Long empresaId;

    @NotBlank(message = "El tipo de identificación es obligatorio")
    @Size(max = 10, message = "El tipo de identificación no puede exceder 10 caracteres")
    @Column(name = "tipo_identificacion", nullable = false, length = 10)
    private String tipoIdentificacion;

    @NotBlank(message = "El número de identificación es obligatorio")
    @Size(max = 50, message = "El número de identificación no puede exceder 50 caracteres")
    @Column(name = "numero_identificacion", nullable = false, length = 50)
    private String numeroIdentificacion;

    @NotBlank(message = "Los nombres son obligatorios")
    @Size(max = 255, message = "Los nombres no pueden exceder 255 caracteres")
    @Column(name = "nombres", nullable = false)
    private String nombres;

    @Email(message = "El formato del correo electrónico no es válido")
    @Size(max = 150, message = "El correo no puede exceder 150 caracteres")
    @Column(name = "correo", length = 150)
    private String correo;

    @Size(max = 50, message = "El número de celular no puede exceder 50 caracteres")
    @Column(name = "celular", length = 50)
    private String celular;

    @CreationTimestamp
    @Column(name = "creado_en", nullable = false, updatable = false)
    private LocalDateTime creadoEn;

    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Direccion> direcciones = new ArrayList<>();

    // Constructores
    public Cliente() {}

    public Cliente(Long empresaId, String tipoIdentificacion, String numeroIdentificacion, 
                   String nombres, String correo, String celular) {
        this.empresaId = empresaId;
        this.tipoIdentificacion = tipoIdentificacion;
        this.numeroIdentificacion = numeroIdentificacion;
        this.nombres = nombres;
        this.correo = correo;
        this.celular = celular;
    }

    // Métodos de utilidad
    /**
     * Obtiene la dirección matriz del cliente
     * @return La dirección matriz o null si no existe
     */
    public Direccion getDireccionMatriz() {
        return direcciones.stream()
                .filter(Direccion::isEsMatriz)
                .findFirst()
                .orElse(null);
    }

    /**
     * Obtiene las direcciones adicionales
     * @return Lista de direcciones adicionales
     */
    public List<Direccion> getDireccionesAdicionales() {
        return direcciones.stream()
                .filter(direccion -> !direccion.isEsMatriz())
                .toList();
    }

    /**
     * Agrega una dirección al cliente
     * @param direccion La dirección a agregar
     */
    public void agregarDireccion(Direccion direccion) {
        direcciones.add(direccion);
        direccion.setCliente(this);
    }

    /**
     * Remueve una dirección del cliente
     * @param direccion La dirección a remover
     */
    public void removerDireccion(Direccion direccion) {
        direcciones.remove(direccion);
        direccion.setCliente(null);
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

    public List<Direccion> getDirecciones() {
        return direcciones;
    }

    public void setDirecciones(List<Direccion> direcciones) {
        this.direcciones = direcciones;
    }

    // equals y hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cliente cliente = (Cliente) o;
        return Objects.equals(id, cliente.id) &&
               Objects.equals(empresaId, cliente.empresaId) &&
               Objects.equals(tipoIdentificacion, cliente.tipoIdentificacion) &&
               Objects.equals(numeroIdentificacion, cliente.numeroIdentificacion);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, empresaId, tipoIdentificacion, numeroIdentificacion);
    }

    @Override
    public String toString() {
        return "Cliente{" +
                "id=" + id +
                ", empresaId=" + empresaId +
                ", tipoIdentificacion='" + tipoIdentificacion + '\'' +
                ", numeroIdentificacion='" + numeroIdentificacion + '\'' +
                ", nombres='" + nombres + '\'' +
                ", correo='" + correo + '\'' +
                ", celular='" + celular + '\'' +
                ", creadoEn=" + creadoEn +
                '}';
    }
}