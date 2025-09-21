package com.alquimiasoft.minegocio.service;

import com.alquimiasoft.minegocio.dto.direccion.DireccionCreateRequest;
import com.alquimiasoft.minegocio.dto.direccion.DireccionResponse;
import com.alquimiasoft.minegocio.entity.Cliente;
import com.alquimiasoft.minegocio.entity.Direccion;
import com.alquimiasoft.minegocio.exception.ResourceNotFoundException;
import com.alquimiasoft.minegocio.mapper.DireccionMapper;
import com.alquimiasoft.minegocio.repository.ClienteRepository;
import com.alquimiasoft.minegocio.repository.DireccionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Tests unitarios para DireccionService
 */
@ExtendWith(MockitoExtension.class)
class DireccionServiceTest {

    @Mock
    private DireccionRepository direccionRepository;

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private DireccionMapper direccionMapper;

    @InjectMocks
    private DireccionService direccionService;

    private Cliente cliente;
    private Direccion direccionMatriz;
    private Direccion direccionAdicional;
    private DireccionCreateRequest direccionCreateRequest;
    private DireccionResponse direccionResponse;

    @BeforeEach
    void setUp() {
        cliente = new Cliente(1L, "CEDULA", "0102030405", "Juan Perez", "juan@test.com", "0999999999");
        cliente.setId(1L);

        direccionMatriz = new Direccion("Pichincha", "Quito", "Av. Principal 123", true);
        direccionMatriz.setId(1L);
        direccionMatriz.setCliente(cliente);
        direccionMatriz.setCreadoEn(LocalDateTime.now());

        direccionAdicional = new Direccion("Guayas", "Guayaquil", "Av. Secundaria 456", false);
        direccionAdicional.setId(2L);
        direccionAdicional.setCliente(cliente);
        direccionAdicional.setCreadoEn(LocalDateTime.now());

        direccionCreateRequest = new DireccionCreateRequest(1L, "Azuay", "Cuenca", "Calle Terciaria 789");

        direccionResponse = new DireccionResponse(2L, "Guayas", "Guayaquil", "Av. Secundaria 456", 
                false, LocalDateTime.now());
    }

    @Test
    void crearDireccionAdicional_ConClienteExistente_DeberiaCrearDireccion() {
        // Given
        Direccion nuevaDireccion = new Direccion("Azuay", "Cuenca", "Calle Terciaria 789", false);
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(direccionMapper.toEntity(direccionCreateRequest)).thenReturn(nuevaDireccion);
        when(direccionRepository.save(any(Direccion.class))).thenReturn(nuevaDireccion);
        when(direccionMapper.toResponse(nuevaDireccion)).thenReturn(direccionResponse);

        // When
        DireccionResponse result = direccionService.crearDireccionAdicional(direccionCreateRequest);

        // Then
        assertThat(result).isNotNull();
        verify(direccionRepository).save(any(Direccion.class));
        verify(direccionMapper).toResponse(nuevaDireccion);
    }

    @Test
    void crearDireccionAdicional_ConClienteInexistente_DeberiaLanzarResourceNotFoundException() {
        // Given
        when(clienteRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> direccionService.crearDireccionAdicional(direccionCreateRequest))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Cliente no encontrado");
    }

    @Test
    void obtenerDireccionesPorCliente_ConClienteExistente_DeberiaRetornarDirecciones() {
        // Given
        Long clienteId = 1L;
        List<Direccion> direcciones = Arrays.asList(direccionMatriz, direccionAdicional);
        List<DireccionResponse> direccionesResponse = Arrays.asList(direccionResponse);

        when(clienteRepository.existsById(clienteId)).thenReturn(true);
        when(direccionRepository.findByClienteIdOrderByEsMatrizDescCreadoEnAsc(clienteId))
                .thenReturn(direcciones);
        when(direccionMapper.toResponseList(direcciones)).thenReturn(direccionesResponse);

        // When
        List<DireccionResponse> result = direccionService.obtenerDireccionesPorCliente(clienteId);

        // Then
        assertThat(result).isNotEmpty();
        verify(direccionRepository).findByClienteIdOrderByEsMatrizDescCreadoEnAsc(clienteId);
    }

    @Test
    void obtenerDireccionesPorCliente_ConClienteInexistente_DeberiaLanzarResourceNotFoundException() {
        // Given
        Long clienteId = 999L;
        when(clienteRepository.existsById(clienteId)).thenReturn(false);

        // When & Then
        assertThatThrownBy(() -> direccionService.obtenerDireccionesPorCliente(clienteId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Cliente no encontrado");
    }

    @Test
    void obtenerDireccionesAdicionales_ConClienteExistente_DeberiaRetornarSoloDireccionesAdicionales() {
        // Given
        Long clienteId = 1L;
        List<Direccion> direccionesAdicionales = Arrays.asList(direccionAdicional);
        List<DireccionResponse> direccionesResponse = Arrays.asList(direccionResponse);

        when(clienteRepository.existsById(clienteId)).thenReturn(true);
        when(direccionRepository.findByClienteIdAndEsMatrizFalse(clienteId))
                .thenReturn(direccionesAdicionales);
        when(direccionMapper.toResponseList(direccionesAdicionales)).thenReturn(direccionesResponse);

        // When
        List<DireccionResponse> result = direccionService.obtenerDireccionesAdicionales(clienteId);

        // Then
        assertThat(result).isNotEmpty();
        verify(direccionRepository).findByClienteIdAndEsMatrizFalse(clienteId);
    }

    @Test
    void obtenerDireccionMatriz_ConClienteExistente_DeberiaRetornarDireccionMatriz() {
        // Given
        Long clienteId = 1L;
        DireccionResponse direccionMatrizResponse = new DireccionResponse(1L, "Pichincha", "Quito", 
                "Av. Principal 123", true, LocalDateTime.now());

        when(clienteRepository.existsById(clienteId)).thenReturn(true);
        when(direccionRepository.findByClienteIdAndEsMatrizTrue(clienteId))
                .thenReturn(Optional.of(direccionMatriz));
        when(direccionMapper.toResponse(direccionMatriz)).thenReturn(direccionMatrizResponse);

        // When
        DireccionResponse result = direccionService.obtenerDireccionMatriz(clienteId);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.isEsMatriz()).isTrue();
    }

    @Test
    void obtenerDireccionMatriz_SinDireccionMatriz_DeberiaLanzarResourceNotFoundException() {
        // Given
        Long clienteId = 1L;
        when(clienteRepository.existsById(clienteId)).thenReturn(true);
        when(direccionRepository.findByClienteIdAndEsMatrizTrue(clienteId))
                .thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> direccionService.obtenerDireccionMatriz(clienteId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Dirección matriz no encontrada");
    }

    @Test
    void obtenerDireccionPorId_ConIdExistente_DeberiaRetornarDireccion() {
        // Given
        Long direccionId = 1L;
        when(direccionRepository.findById(direccionId)).thenReturn(Optional.of(direccionMatriz));
        when(direccionMapper.toResponse(direccionMatriz)).thenReturn(direccionResponse);

        // When
        DireccionResponse result = direccionService.obtenerDireccionPorId(direccionId);

        // Then
        assertThat(result).isNotNull();
    }

    @Test
    void obtenerDireccionPorId_ConIdInexistente_DeberiaLanzarResourceNotFoundException() {
        // Given
        Long direccionId = 999L;
        when(direccionRepository.findById(direccionId)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> direccionService.obtenerDireccionPorId(direccionId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Dirección no encontrada");
    }

    @Test
    void eliminarDireccionAdicional_ConDireccionAdicionalExistente_DeberiaEliminar() {
        // Given
        Long direccionId = 2L;
        when(direccionRepository.findById(direccionId)).thenReturn(Optional.of(direccionAdicional));

        // When
        direccionService.eliminarDireccionAdicional(direccionId);

        // Then
        verify(direccionRepository).delete(direccionAdicional);
    }

    @Test
    void eliminarDireccionAdicional_ConDireccionMatriz_DeberiaLanzarIllegalArgumentException() {
        // Given
        Long direccionId = 1L;
        when(direccionRepository.findById(direccionId)).thenReturn(Optional.of(direccionMatriz));

        // When & Then
        assertThatThrownBy(() -> direccionService.eliminarDireccionAdicional(direccionId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("No se puede eliminar la dirección matriz");
    }

    @Test
    void eliminarDireccionAdicional_ConIdInexistente_DeberiaLanzarResourceNotFoundException() {
        // Given
        Long direccionId = 999L;
        when(direccionRepository.findById(direccionId)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> direccionService.eliminarDireccionAdicional(direccionId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Dirección no encontrada");
    }

    @Test
    void contarDireccionesPorCliente_DeberiaRetornarConteo() {
        // Given
        Long clienteId = 1L;
        long expectedCount = 3L;
        when(direccionRepository.countByClienteId(clienteId)).thenReturn(expectedCount);

        // When
        long result = direccionService.contarDireccionesPorCliente(clienteId);

        // Then
        assertThat(result).isEqualTo(expectedCount);
    }

    @Test
    void tieneDireccionMatriz_ConDireccionMatrizExistente_DeberiaRetornarTrue() {
        // Given
        Long clienteId = 1L;
        when(direccionRepository.existsByClienteIdAndEsMatrizTrue(clienteId)).thenReturn(true);

        // When
        boolean result = direccionService.tieneDireccionMatriz(clienteId);

        // Then
        assertThat(result).isTrue();
    }

    @Test
    void buscarDirecciones_ConCriterioValido_DeberiaRetornarDireccionesEncontradas() {
        // Given
        Long clienteId = 1L;
        String busqueda = "Quito";
        List<Direccion> direcciones = Arrays.asList(direccionMatriz);
        List<DireccionResponse> direccionesResponse = Arrays.asList(direccionResponse);

        when(clienteRepository.existsById(clienteId)).thenReturn(true);
        when(direccionRepository.buscarDireccionesPorClienteYTexto(clienteId, busqueda))
                .thenReturn(direcciones);
        when(direccionMapper.toResponseList(direcciones)).thenReturn(direccionesResponse);

        // When
        List<DireccionResponse> result = direccionService.buscarDirecciones(clienteId, busqueda);

        // Then
        assertThat(result).isNotEmpty();
        verify(direccionRepository).buscarDireccionesPorClienteYTexto(clienteId, busqueda);
    }
}