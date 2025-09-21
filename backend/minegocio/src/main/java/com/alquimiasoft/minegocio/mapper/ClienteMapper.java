package com.alquimiasoft.minegocio.mapper;

import com.alquimiasoft.minegocio.dto.cliente.ClienteCreateRequest;
import com.alquimiasoft.minegocio.dto.cliente.ClienteResponse;
import com.alquimiasoft.minegocio.dto.cliente.ClienteUpdateRequest;
import com.alquimiasoft.minegocio.dto.direccion.DireccionResponse;
import com.alquimiasoft.minegocio.entity.Cliente;
import com.alquimiasoft.minegocio.entity.Direccion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper para convertir entre entidades Cliente y DTOs
 */
@Component
public class ClienteMapper {

    @Autowired
    private DireccionMapper direccionMapper;

    /**
     * Convierte una entidad Cliente a ClienteResponse
     */
    public ClienteResponse toResponse(Cliente cliente) {
        if (cliente == null) {
            return null;
        }

        ClienteResponse response = new ClienteResponse(
                cliente.getId(),
                cliente.getEmpresaId(),
                cliente.getTipoIdentificacion(),
                cliente.getNumeroIdentificacion(),
                cliente.getNombres(),
                cliente.getCorreo(),
                cliente.getCelular(),
                cliente.getCreadoEn()
        );

        // Mapear dirección matriz
        Direccion direccionMatriz = cliente.getDireccionMatriz();
        if (direccionMatriz != null) {
            response.setDireccionMatriz(direccionMapper.toResponse(direccionMatriz));
        }

        // Mapear direcciones adicionales
        List<Direccion> direccionesAdicionales = cliente.getDireccionesAdicionales();
        if (direccionesAdicionales != null && !direccionesAdicionales.isEmpty()) {
            List<DireccionResponse> direccionesAdicionalesResponse = 
                direccionMapper.toResponseList(direccionesAdicionales);
            response.setDireccionesAdicionales(direccionesAdicionalesResponse);
        }

        return response;
    }

    /**
     * Convierte una lista de entidades Cliente a lista de ClienteResponse
     */
    public List<ClienteResponse> toResponseList(List<Cliente> clientes) {
        if (clientes == null) {
            return null;
        }

        return clientes.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * Convierte un ClienteCreateRequest a entidad Cliente
     */
    public Cliente toEntity(ClienteCreateRequest request) {
        if (request == null) {
            return null;
        }

        return new Cliente(
                request.getEmpresaId(),
                request.getTipoIdentificacion(),
                request.getNumeroIdentificacion(),
                request.getNombres(),
                request.getCorreo(),
                request.getCelular()
        );
    }

    /**
     * Actualiza una entidad Cliente existente con datos de ClienteUpdateRequest
     */
    public void updateEntity(Cliente cliente, ClienteUpdateRequest request) {
        if (cliente == null || request == null) {
            return;
        }

        cliente.setTipoIdentificacion(request.getTipoIdentificacion());
        cliente.setNumeroIdentificacion(request.getNumeroIdentificacion());
        cliente.setNombres(request.getNombres());
        cliente.setCorreo(request.getCorreo());
        cliente.setCelular(request.getCelular());
    }

    /**
     * Convierte un ClienteResponse a respuesta simplificada (solo con dirección matriz)
     */
    public ClienteResponse toResponseWithMatriz(Cliente cliente) {
        if (cliente == null) {
            return null;
        }

        ClienteResponse response = new ClienteResponse(
                cliente.getId(),
                cliente.getEmpresaId(),
                cliente.getTipoIdentificacion(),
                cliente.getNumeroIdentificacion(),
                cliente.getNombres(),
                cliente.getCorreo(),
                cliente.getCelular(),
                cliente.getCreadoEn()
        );

        // Solo mapear dirección matriz
        Direccion direccionMatriz = cliente.getDireccionMatriz();
        if (direccionMatriz != null) {
            response.setDireccionMatriz(direccionMapper.toResponse(direccionMatriz));
        }

        return response;
    }

    /**
     * Convierte una lista de clientes a respuesta simplificada con solo dirección matriz
     */
    public List<ClienteResponse> toResponseListWithMatriz(List<Cliente> clientes) {
        if (clientes == null) {
            return null;
        }

        return clientes.stream()
                .map(this::toResponseWithMatriz)
                .collect(Collectors.toList());
    }
}