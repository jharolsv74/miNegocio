package com.alquimiasoft.minegocio.service;

import com.alquimiasoft.minegocio.dto.cliente.ClienteCreateRequest;
import com.alquimiasoft.minegocio.dto.cliente.ClienteResponse;
import com.alquimiasoft.minegocio.dto.cliente.ClienteUpdateRequest;
import com.alquimiasoft.minegocio.entity.Cliente;
import com.alquimiasoft.minegocio.entity.Direccion;
import com.alquimiasoft.minegocio.entity.TipoIdentificacion;
import com.alquimiasoft.minegocio.exception.BusinessException;
import com.alquimiasoft.minegocio.exception.ResourceNotFoundException;
import com.alquimiasoft.minegocio.mapper.ClienteMapper;
import com.alquimiasoft.minegocio.mapper.DireccionMapper;
import com.alquimiasoft.minegocio.repository.ClienteRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * Servicio para la gestión de clientes
 */
@Service
@Transactional
public class ClienteService {

    private static final Logger logger = LoggerFactory.getLogger(ClienteService.class);

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private ClienteMapper clienteMapper;

    @Autowired
    private DireccionMapper direccionMapper;

    /**
     * Busca clientes por criterio de búsqueda
     */
    @Transactional(readOnly = true)
    public List<ClienteResponse> buscarClientes(Long empresaId, String busqueda) {
        logger.info("Buscando clientes para empresa {} con criterio: {}", empresaId, busqueda);

        List<Cliente> clientes;

        if (!StringUtils.hasText(busqueda)) {
            // Si no hay criterio de búsqueda, devolver todos los clientes de la empresa
            clientes = clienteRepository.findByEmpresaIdWithDirecciones(empresaId);
        } else {
            // Buscar por número de identificación o nombre
            clientes = clienteRepository.buscarClientesPorEmpresaYTexto(empresaId, busqueda.trim());
        }

        logger.info("Se encontraron {} clientes", clientes.size());
        return clienteMapper.toResponseListWithMatriz(clientes);
    }

    /**
     * Crea un nuevo cliente con su dirección matriz
     */
    public ClienteResponse crearCliente(ClienteCreateRequest request) {
        logger.info("Creando nuevo cliente: {}", request.getNumeroIdentificacion());

        // Validar tipo de identificación
        if (!TipoIdentificacion.isValid(request.getTipoIdentificacion())) {
            throw BusinessException.tipoIdentificacionInvalido(request.getTipoIdentificacion());
        }

        // Verificar que no exista otro cliente con la misma identificación
        if (clienteRepository.existsByEmpresaIdAndTipoIdentificacionAndNumeroIdentificacion(
                request.getEmpresaId(), request.getTipoIdentificacion(), request.getNumeroIdentificacion())) {
            throw BusinessException.clienteYaExiste(request.getNumeroIdentificacion());
        }

        // Crear cliente
        Cliente cliente = clienteMapper.toEntity(request);

        // Crear dirección matriz
        Direccion direccionMatriz = direccionMapper.toEntity(request.getDireccionMatriz(), true);
        cliente.agregarDireccion(direccionMatriz);

        // Guardar
        Cliente clienteGuardado = clienteRepository.save(cliente);

        logger.info("Cliente creado exitosamente con ID: {}", clienteGuardado.getId());
        return clienteMapper.toResponse(clienteGuardado);
    }

    /**
     * Actualiza los datos de un cliente existente
     */
    public ClienteResponse actualizarCliente(Long id, ClienteUpdateRequest request) {
        logger.info("Actualizando cliente con ID: {}", id);

        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.cliente(id));

        // Validar tipo de identificación
        if (!TipoIdentificacion.isValid(request.getTipoIdentificacion())) {
            throw BusinessException.tipoIdentificacionInvalido(request.getTipoIdentificacion());
        }

        // Verificar que no exista otro cliente con la misma identificación
        if (clienteRepository.existsByEmpresaIdAndTipoIdentificacionAndNumeroIdentificacionAndIdNot(
                cliente.getEmpresaId(), request.getTipoIdentificacion(), 
                request.getNumeroIdentificacion(), id)) {
            throw BusinessException.clienteYaExiste(request.getNumeroIdentificacion());
        }

        // Actualizar datos
        clienteMapper.updateEntity(cliente, request);

        // Guardar
        Cliente clienteActualizado = clienteRepository.save(cliente);

        logger.info("Cliente actualizado exitosamente: {}", id);
        return clienteMapper.toResponse(clienteActualizado);
    }

    /**
     * Elimina un cliente y todas sus direcciones
     */
    public void eliminarCliente(Long id) {
        logger.info("Eliminando cliente con ID: {}", id);

        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.cliente(id));

        clienteRepository.delete(cliente);

        logger.info("Cliente eliminado exitosamente: {}", id);
    }

    /**
     * Obtiene un cliente por ID con todas sus direcciones
     */
    @Transactional(readOnly = true)
    public ClienteResponse obtenerClientePorId(Long id) {
        logger.info("Obteniendo cliente con ID: {}", id);

        Cliente cliente = clienteRepository.findByIdWithDirecciones(id)
                .orElseThrow(() -> ResourceNotFoundException.cliente(id));

        return clienteMapper.toResponse(cliente);
    }

    /**
     * Obtiene todos los clientes de una empresa con paginación
     */
    @Transactional(readOnly = true)
    public Page<ClienteResponse> obtenerClientesPorEmpresa(Long empresaId, Pageable pageable) {
        logger.info("Obteniendo clientes paginados para empresa: {}", empresaId);

        Page<Cliente> clientes = clienteRepository.findByEmpresaId(empresaId, pageable);

        return clientes.map(clienteMapper::toResponseWithMatriz);
    }

    /**
     * Cuenta el número de clientes de una empresa
     */
    @Transactional(readOnly = true)
    public long contarClientesPorEmpresa(Long empresaId) {
        return clienteRepository.countByEmpresaId(empresaId);
    }

    /**
     * Verifica si un cliente existe
     */
    @Transactional(readOnly = true)
    public boolean existeCliente(Long id) {
        return clienteRepository.existsById(id);
    }

    /**
     * Busca un cliente por número de identificación
     */
    @Transactional(readOnly = true)
    public ClienteResponse buscarClientePorIdentificacion(Long empresaId, String numeroIdentificacion) {
        logger.info("Buscando cliente por identificación: {} en empresa: {}", numeroIdentificacion, empresaId);

        Cliente cliente = clienteRepository.findByEmpresaIdAndNumeroIdentificacion(empresaId, numeroIdentificacion)
                .orElseThrow(() -> ResourceNotFoundException.clientePorIdentificacion(numeroIdentificacion));

        return clienteMapper.toResponse(cliente);
    }
    
}