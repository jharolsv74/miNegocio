package com.alquimiasoft.minegocio.mapper;

import com.alquimiasoft.minegocio.dto.direccion.DireccionCreateRequest;
import com.alquimiasoft.minegocio.dto.direccion.DireccionRequest;
import com.alquimiasoft.minegocio.dto.direccion.DireccionResponse;
import com.alquimiasoft.minegocio.entity.Direccion;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper para convertir entre entidades Direccion y DTOs
 */
@Component
public class DireccionMapper {

    /**
     * Convierte una entidad Direccion a DireccionResponse
     */
    public DireccionResponse toResponse(Direccion direccion) {
        if (direccion == null) {
            return null;
        }

        return new DireccionResponse(
                direccion.getId(),
                direccion.getProvincia(),
                direccion.getCiudad(),
                direccion.getDireccionTexto(),
                direccion.isEsMatriz(),
                direccion.getCreadoEn()
        );
    }

    /**
     * Convierte una lista de entidades Direccion a lista de DireccionResponse
     */
    public List<DireccionResponse> toResponseList(List<Direccion> direcciones) {
        if (direcciones == null) {
            return null;
        }

        return direcciones.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * Convierte un DireccionRequest a entidad Direccion
     */
    public Direccion toEntity(DireccionRequest request, boolean esMatriz) {
        if (request == null) {
            return null;
        }

        return new Direccion(
                request.getProvincia(),
                request.getCiudad(),
                request.getDireccion(),
                esMatriz
        );
    }

    /**
     * Convierte un DireccionCreateRequest a entidad Direccion
     */
    public Direccion toEntity(DireccionCreateRequest request) {
        if (request == null) {
            return null;
        }

        return new Direccion(
                request.getProvincia(),
                request.getCiudad(),
                request.getDireccion(),
                false // Las direcciones adicionales nunca son matriz
        );
    }

    /**
     * Actualiza una entidad Direccion existente con datos de DireccionRequest
     */
    public void updateEntity(Direccion direccion, DireccionRequest request) {
        if (direccion == null || request == null) {
            return;
        }

        direccion.setProvincia(request.getProvincia());
        direccion.setCiudad(request.getCiudad());
        direccion.setDireccionTexto(request.getDireccion());
    }

    /**
     * Actualiza una entidad Direccion existente con datos de DireccionCreateRequest
     */
    public void updateEntity(Direccion direccion, DireccionCreateRequest request) {
        if (direccion == null || request == null) {
            return;
        }

        direccion.setProvincia(request.getProvincia());
        direccion.setCiudad(request.getCiudad());
        direccion.setDireccionTexto(request.getDireccion());
    }
}