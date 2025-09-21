package com.alquimiasoft.minegocio.service;

import com.alquimiasoft.minegocio.dto.direccion.DireccionCreateRequest;
import com.alquimiasoft.minegocio.dto.direccion.DireccionResponse;
import com.alquimiasoft.minegocio.entity.Cliente;
import com.alquimiasoft.minegocio.entity.Direccion;
import com.alquimiasoft.minegocio.exception.ResourceNotFoundException;
import com.alquimiasoft.minegocio.mapper.DireccionMapper;
import com.alquimiasoft.minegocio.repository.ClienteRepository;
import com.alquimiasoft.minegocio.repository.DireccionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Servicio para la gestión de direcciones de clientes
 */
@Service
@Transactional
public class DireccionService {

    private static final Logger logger = LoggerFactory.getLogger(DireccionService.class);

    @Autowired
    private DireccionRepository direccionRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private DireccionMapper direccionMapper;

    /**
     * Crea una nueva dirección adicional para un cliente
     */
    public DireccionResponse crearDireccionAdicional(DireccionCreateRequest request) {
        logger.info("Creando nueva dirección adicional para cliente ID: {}", request.getClienteId());

        // Verificar que el cliente existe
        Cliente cliente = clienteRepository.findById(request.getClienteId())
                .orElseThrow(() -> ResourceNotFoundException.cliente(request.getClienteId()));

        // Crear la dirección adicional (no es matriz)
        Direccion nuevaDireccion = direccionMapper.toEntity(request);
        
        // Vincular la dirección al cliente usando el método que mantiene la relación bidireccional
        cliente.agregarDireccion(nuevaDireccion);
        
        // Guardar el cliente (cascada guarda la dirección automáticamente)
        Cliente clienteActualizado = clienteRepository.save(cliente);
        
        // Obtener la dirección guardada desde el cliente actualizado
        Direccion direccionGuardada = clienteActualizado.getDirecciones().stream()
                .filter(dir -> !dir.isEsMatriz())
                .reduce((primera, segunda) -> segunda) // Obtener la última agregada
                .orElse(nuevaDireccion);

        logger.info("Dirección adicional creada exitosamente y vinculada al cliente ID: {}", request.getClienteId());
        return direccionMapper.toResponse(direccionGuardada);
    }

    /**
     * Obtiene todas las direcciones de un cliente (matriz + adicionales)
     */
    @Transactional(readOnly = true)
    public List<DireccionResponse> obtenerDireccionesPorCliente(Long clienteId) {
        logger.info("Obteniendo direcciones para cliente ID: {}", clienteId);

        // Verificar que el cliente existe
        if (!clienteRepository.existsById(clienteId)) {
            throw ResourceNotFoundException.cliente(clienteId);
        }

        List<Direccion> direcciones = direccionRepository.findByClienteIdOrderByEsMatrizDescCreadoEnAsc(clienteId);

        logger.info("Se encontraron {} direcciones para el cliente {}", direcciones.size(), clienteId);
        return direccionMapper.toResponseList(direcciones);
    }

    /**
     * Obtiene solo las direcciones adicionales de un cliente (excluye la matriz)
     */
    @Transactional(readOnly = true)
    public List<DireccionResponse> obtenerDireccionesAdicionales(Long clienteId) {
        logger.info("Obteniendo direcciones adicionales para cliente ID: {}", clienteId);

        // Verificar que el cliente existe
        if (!clienteRepository.existsById(clienteId)) {
            throw ResourceNotFoundException.cliente(clienteId);
        }

        List<Direccion> direccionesAdicionales = direccionRepository.findByClienteIdAndEsMatrizFalse(clienteId);

        logger.info("Se encontraron {} direcciones adicionales para el cliente {}", 
                   direccionesAdicionales.size(), clienteId);
        return direccionMapper.toResponseList(direccionesAdicionales);
    }

    /**
     * Obtiene la dirección matriz de un cliente
     */
    @Transactional(readOnly = true)
    public DireccionResponse obtenerDireccionMatriz(Long clienteId) {
        logger.info("Obteniendo dirección matriz para cliente ID: {}", clienteId);

        // Verificar que el cliente existe
        if (!clienteRepository.existsById(clienteId)) {
            throw ResourceNotFoundException.cliente(clienteId);
        }

        Direccion direccionMatriz = direccionRepository.findByClienteIdAndEsMatrizTrue(clienteId)
                .orElseThrow(() -> ResourceNotFoundException.direccionMatriz(clienteId));

        return direccionMapper.toResponse(direccionMatriz);
    }

    /**
     * Obtiene una dirección por ID
     */
    @Transactional(readOnly = true)
    public DireccionResponse obtenerDireccionPorId(Long direccionId) {
        logger.info("Obteniendo dirección con ID: {}", direccionId);

        Direccion direccion = direccionRepository.findById(direccionId)
                .orElseThrow(() -> ResourceNotFoundException.direccion(direccionId));

        return direccionMapper.toResponse(direccion);
    }

    /**
     * Elimina una dirección adicional (no se puede eliminar la dirección matriz)
     */
    public void eliminarDireccionAdicional(Long direccionId) {
        logger.info("Eliminando dirección con ID: {}", direccionId);

        Direccion direccion = direccionRepository.findById(direccionId)
                .orElseThrow(() -> ResourceNotFoundException.direccion(direccionId));

        // Verificar que no sea dirección matriz
        if (direccion.isEsMatriz()) {
            throw new IllegalArgumentException("No se puede eliminar la dirección matriz del cliente");
        }

        direccionRepository.delete(direccion);

        logger.info("Dirección adicional eliminada exitosamente: {}", direccionId);
    }

    /**
     * Cuenta el número de direcciones de un cliente
     */
    @Transactional(readOnly = true)
    public long contarDireccionesPorCliente(Long clienteId) {
        return direccionRepository.countByClienteId(clienteId);
    }

    /**
     * Cuenta el número de direcciones adicionales de un cliente
     */
    @Transactional(readOnly = true)
    public long contarDireccionesAdicionales(Long clienteId) {
        return direccionRepository.countByClienteIdAndEsMatrizFalse(clienteId);
    }

    /**
     * Verifica si un cliente tiene dirección matriz
     */
    @Transactional(readOnly = true)
    public boolean tieneDireccionMatriz(Long clienteId) {
        return direccionRepository.existsByClienteIdAndEsMatrizTrue(clienteId);
    }

    /**
     * Busca direcciones por criterio de búsqueda
     */
    @Transactional(readOnly = true)
    public List<DireccionResponse> buscarDirecciones(Long clienteId, String busqueda) {
        logger.info("Buscando direcciones para cliente {} con criterio: {}", clienteId, busqueda);

        // Verificar que el cliente existe
        if (!clienteRepository.existsById(clienteId)) {
            throw ResourceNotFoundException.cliente(clienteId);
        }

        List<Direccion> direcciones = direccionRepository.buscarDireccionesPorClienteYTexto(clienteId, busqueda);

        logger.info("Se encontraron {} direcciones", direcciones.size());
        return direccionMapper.toResponseList(direcciones);
    }
}