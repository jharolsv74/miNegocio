package com.alquimiasoft.minegocio.exception;

/**
 * Excepción lanzada cuando hay un conflicto con las reglas de negocio
 */
public class BusinessException extends RuntimeException {

    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }

    public static BusinessException clienteYaExiste(String numeroIdentificacion) {
        return new BusinessException("Ya existe un cliente con el número de identificación: " + numeroIdentificacion);
    }

    public static BusinessException direccionMatrizYaExiste(Long clienteId) {
        return new BusinessException("El cliente ya tiene una dirección matriz asignada");
    }

    public static BusinessException tipoIdentificacionInvalido(String tipo) {
        return new BusinessException("Tipo de identificación no válido: " + tipo);
    }

    public static BusinessException empresaInvalida(Long empresaId) {
        return new BusinessException("La empresa especificada no es válida: " + empresaId);
    }

    public static BusinessException operacionNoPermitida(String operacion) {
        return new BusinessException("Operación no permitida: " + operacion);
    }
}