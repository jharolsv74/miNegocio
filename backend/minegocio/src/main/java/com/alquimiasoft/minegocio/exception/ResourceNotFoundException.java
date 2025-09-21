package com.alquimiasoft.minegocio.exception;

/**
 * Excepción lanzada cuando un recurso no es encontrado
 */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public static ResourceNotFoundException cliente(Long id) {
        return new ResourceNotFoundException("Cliente no encontrado con ID: " + id);
    }

    public static ResourceNotFoundException clientePorIdentificacion(String numeroIdentificacion) {
        return new ResourceNotFoundException("Cliente no encontrado con número de identificación: " + numeroIdentificacion);
    }

    public static ResourceNotFoundException direccion(Long id) {
        return new ResourceNotFoundException("Dirección no encontrada con ID: " + id);
    }

    public static ResourceNotFoundException direccionMatriz(Long clienteId) {
        return new ResourceNotFoundException("Dirección matriz no encontrada para el cliente con ID: " + clienteId);
    }
}