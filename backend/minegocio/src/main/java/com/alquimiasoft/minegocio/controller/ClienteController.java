package com.alquimiasoft.minegocio.controller;

import com.alquimiasoft.minegocio.dto.*;
import com.alquimiasoft.minegocio.dto.cliente.ClienteCreateRequest;
import com.alquimiasoft.minegocio.dto.cliente.ClienteResponse;
import com.alquimiasoft.minegocio.dto.cliente.ClienteUpdateRequest;
import com.alquimiasoft.minegocio.dto.direccion.DireccionCreateRequest;
import com.alquimiasoft.minegocio.dto.direccion.DireccionResponse;
import com.alquimiasoft.minegocio.entity.Cliente;
import com.alquimiasoft.minegocio.entity.Direccion;
import com.alquimiasoft.minegocio.exception.ResourceNotFoundException;
import com.alquimiasoft.minegocio.repository.ClienteRepository;
import com.alquimiasoft.minegocio.service.ClienteService;
import com.alquimiasoft.minegocio.service.DireccionService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controlador REST para la gestión de clientes y direcciones
 */
@RestController
@RequestMapping("/api/clientes")
// @CrossOrigin(origins = "*") para usarlos desde el frontend
public class ClienteController {

    private static final Logger logger = LoggerFactory.getLogger(ClienteController.class);

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private DireccionService direccionService;
    
    @Autowired
    private ClienteRepository clienteRepository;

    /**
     * API REST para buscar clientes por número de identificación o nombre
     * GET /api/clientes/buscar?empresaId=1&busqueda=texto
     */
    @GetMapping("/buscar")
    public ResponseEntity<ApiResponse<List<ClienteResponse>>> buscarClientes(
            @RequestParam Long empresaId,
            @RequestParam(required = false) String busqueda) {
        
        logger.info("API: Buscar clientes - Empresa: {}, Búsqueda: {}", empresaId, busqueda);

        try {
            List<ClienteResponse> clientes = clienteService.buscarClientes(empresaId, busqueda);
            
            String mensaje = busqueda != null && !busqueda.trim().isEmpty() 
                ? String.format("Se encontraron %d clientes que coinciden con '%s'", clientes.size(), busqueda)
                : String.format("Se encontraron %d clientes en total", clientes.size());

            return ResponseEntity.ok(ApiResponse.success(mensaje, clientes));
        } catch (Exception e) {
            logger.error("Error al buscar clientes", e);
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.error("Error al buscar clientes: " + e.getMessage()));
        }
    }

    /**
     * API REST para crear un nuevo cliente con dirección matriz
     * POST /api/clientes
     */
    @PostMapping
    public ResponseEntity<ApiResponse<ClienteResponse>> crearCliente(@Valid @RequestBody ClienteCreateRequest request) {
        
        logger.info("API: Crear cliente - {}", request.getNumeroIdentificacion());

        try {
            ClienteResponse cliente = clienteService.crearCliente(request);
            
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success("Cliente creado exitosamente", cliente));
        } catch (Exception e) {
            logger.error("Error al crear cliente", e);
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Error al crear cliente: " + e.getMessage()));
        }
    }

    /**
     * API REST para actualizar los datos de un cliente
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ClienteResponse>> actualizarCliente(
            @PathVariable Long id, 
            @Valid @RequestBody ClienteUpdateRequest request) {
        
        logger.info("API: Actualizar cliente ID: {}", id);

        try {
            ClienteResponse cliente = clienteService.actualizarCliente(id, request);
            
            return ResponseEntity.ok(ApiResponse.success("Cliente actualizado exitosamente", cliente));
        } catch (Exception e) {
            logger.error("Error al actualizar cliente", e);
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Error al actualizar cliente: " + e.getMessage()));
        }
    }

    /**
     * API REST para eliminar un cliente
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> eliminarCliente(@PathVariable Long id) {
        
        logger.info("API: Eliminar cliente ID: {}", id);

        try {
            clienteService.eliminarCliente(id);
            
            return ResponseEntity.ok(ApiResponse.success("Cliente eliminado exitosamente"));
        } catch (Exception e) {
            logger.error("Error al eliminar cliente", e);
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Error al eliminar cliente: " + e.getMessage()));
        }
    }

    /**
     * API REST para obtener un cliente por ID con todas sus direcciones
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ClienteResponse>> obtenerCliente(@PathVariable Long id) {
        
        logger.info("API: Obtener cliente ID: {}", id);

        try {
            ClienteResponse cliente = clienteService.obtenerClientePorId(id);
            
            return ResponseEntity.ok(ApiResponse.success("Cliente obtenido exitosamente", cliente));
        } catch (Exception e) {
            logger.error("Error al obtener cliente", e);
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Error al obtener cliente: " + e.getMessage()));
        }
    }

    /**
     * API REST para debug - obtener cliente por ID con información detallada
     */
    @GetMapping("/{id}/debug")
    public ResponseEntity<Map<String, Object>> debugClientePorId(@PathVariable Long id) {
        
        logger.info("API DEBUG: Obtener cliente por ID - {}", id);

        try {
            // Obtener cliente directamente
            Cliente cliente = clienteRepository.findByIdWithDirecciones(id)
                    .orElseThrow(() -> ResourceNotFoundException.cliente(id));
            
            Map<String, Object> debug = new HashMap<>();
            debug.put("clienteId", cliente.getId());
            debug.put("clienteNombre", cliente.getNombres());
            debug.put("totalDirecciones", cliente.getDirecciones().size());
            debug.put("direccionMatriz", cliente.getDireccionMatriz() != null ? 
                    cliente.getDireccionMatriz().getDireccionCompleta() : null);
            debug.put("cantidadAdicionales", cliente.getDireccionesAdicionales().size());
            
            List<String> direccionesAdicionales = cliente.getDireccionesAdicionales().stream()
                    .map(Direccion::getDireccionCompleta)
                    .toList();
            debug.put("direccionesAdicionales", direccionesAdicionales);
            
            return ResponseEntity.ok(debug);
        } catch (Exception e) {
            logger.error("Error en debug cliente por ID", e);
            Map<String, Object> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.internalServerError().body(error);
        }
    }

    /**
     * API REST para registrar una nueva dirección para un cliente
     */
    @PostMapping("/direcciones")
    public ResponseEntity<ApiResponse<DireccionResponse>> crearDireccionAdicional(
            @Valid @RequestBody DireccionCreateRequest request) {
        
        logger.info("API: Crear dirección adicional para cliente ID: {}", request.getClienteId());

        try {
            DireccionResponse direccion = direccionService.crearDireccionAdicional(request);
            
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success("Dirección adicional creada exitosamente", direccion));
        } catch (Exception e) {
            logger.error("Error al crear dirección adicional", e);
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Error al crear dirección: " + e.getMessage()));
        }
    }

    /**
     * API REST para listar todas las direcciones de un cliente (matriz + adicionales)
     */
    @GetMapping("/{clienteId}/direcciones")
    public ResponseEntity<ApiResponse<List<DireccionResponse>>> listarDireccionesCliente(@PathVariable Long clienteId) {
        
        logger.info("API: Listar direcciones del cliente ID: {}", clienteId);

        try {
            List<DireccionResponse> direcciones = direccionService.obtenerDireccionesPorCliente(clienteId);
            
            String mensaje = String.format("Se encontraron %d direcciones para el cliente", direcciones.size());
            return ResponseEntity.ok(ApiResponse.success(mensaje, direcciones));
        } catch (Exception e) {
            logger.error("Error al listar direcciones del cliente", e);
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Error al obtener direcciones: " + e.getMessage()));
        }
    }

    /**
     * API REST para obtener solo las direcciones adicionales de un cliente
     */
    @GetMapping("/{clienteId}/direcciones/adicionales")
    public ResponseEntity<ApiResponse<List<DireccionResponse>>> listarDireccionesAdicionales(@PathVariable Long clienteId) {
        
        logger.info("API: Listar direcciones adicionales del cliente ID: {}", clienteId);

        try {
            List<DireccionResponse> direcciones = direccionService.obtenerDireccionesAdicionales(clienteId);
            
            String mensaje = String.format("Se encontraron %d direcciones adicionales para el cliente", direcciones.size());
            return ResponseEntity.ok(ApiResponse.success(mensaje, direcciones));
        } catch (Exception e) {
            logger.error("Error al listar direcciones adicionales", e);
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Error al obtener direcciones adicionales: " + e.getMessage()));
        }
    }

    /**
     * API REST para obtener la dirección matriz de un cliente
     */
    @GetMapping("/{clienteId}/direcciones/matriz")
    public ResponseEntity<ApiResponse<DireccionResponse>> obtenerDireccionMatriz(@PathVariable Long clienteId) {
        
        logger.info("API: Obtener dirección matriz del cliente ID: {}", clienteId);

        try {
            DireccionResponse direccion = direccionService.obtenerDireccionMatriz(clienteId);
            
            return ResponseEntity.ok(ApiResponse.success("Dirección matriz obtenida exitosamente", direccion));
        } catch (Exception e) {
            logger.error("Error al obtener dirección matriz", e);
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Error al obtener dirección matriz: " + e.getMessage()));
        }
    }

    /**
     * API REST para eliminar una dirección adicional
     */
    @DeleteMapping("/direcciones/{direccionId}")
    public ResponseEntity<ApiResponse<Void>> eliminarDireccionAdicional(@PathVariable Long direccionId) {
        
        logger.info("API: Eliminar dirección ID: {}", direccionId);

        try {
            direccionService.eliminarDireccionAdicional(direccionId);
            
            return ResponseEntity.ok(ApiResponse.success("Dirección adicional eliminada exitosamente"));
        } catch (Exception e) {
            logger.error("Error al eliminar dirección adicional", e);
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Error al eliminar dirección: " + e.getMessage()));
        }
    }
}
