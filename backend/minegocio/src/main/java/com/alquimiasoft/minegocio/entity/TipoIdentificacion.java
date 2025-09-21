package com.alquimiasoft.minegocio.entity;

/**
 * Enumeración para los tipos de identificación de clientes.
 */
public enum TipoIdentificacion {
    CEDULA("CEDULA", "Cédula de Identidad"),
    RUC("RUC", "Registro Único de Contribuyentes"),
    PASAPORTE("PASAPORTE", "Pasaporte");

    private final String codigo;
    private final String descripcion;

    TipoIdentificacion(String codigo, String descripcion) {
        this.codigo = codigo;
        this.descripcion = descripcion;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    /**
     * Busca un tipo de identificación por su código
     * @param codigo El código a buscar
     * @return El tipo de identificación encontrado
     * @throws IllegalArgumentException si no se encuentra el código
     */
    public static TipoIdentificacion fromCodigo(String codigo) {
        for (TipoIdentificacion tipo : values()) {
            if (tipo.codigo.equals(codigo)) {
                return tipo;
            }
        }
        throw new IllegalArgumentException("Tipo de identificación no válido: " + codigo);
    }

    /**
     * Verifica si un código es válido
     * @param codigo El código a verificar
     * @return true si el código es válido
     */
    public static boolean isValid(String codigo) {
        try {
            fromCodigo(codigo);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    @Override
    public String toString() {
        return codigo;
    }
}