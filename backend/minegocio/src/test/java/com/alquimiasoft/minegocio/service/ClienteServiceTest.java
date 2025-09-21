package com.alquimiasoft.minegocio.service;

import com.alquimiasoft.minegocio.dto.cliente.ClienteCreateRequest;
import com.alquimiasoft.minegocio.dto.cliente.ClienteResponse;
import com.alquimiasoft.minegocio.dto.cliente.ClienteUpdateRequest;
import com.alquimiasoft.minegocio.dto.direccion.DireccionRequest;
import com.alquimiasoft.minegocio.entity.Cliente;
import com.alquimiasoft.minegocio.entity.Direccion;
import com.alquimiasoft.minegocio.exception.BusinessException;
import com.alquimiasoft.minegocio.exception.ResourceNotFoundException;
import com.alquimiasoft.minegocio.mapper.ClienteMapper;
import com.alquimiasoft.minegocio.mapper.DireccionMapper;
import com.alquimiasoft.minegocio.repository.ClienteRepository;
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
 * Tests unitarios para ClienteService
 */
@ExtendWith(MockitoExtension.class)
class ClienteServiceTest {

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private ClienteMapper clienteMapper;

    @Mock
    private DireccionMapper direccionMapper;

    @InjectMocks
    private ClienteService clienteService;

    private Cliente cliente;
    private ClienteCreateRequest clienteCreateRequest;
    private ClienteUpdateRequest clienteUpdateRequest;
    private ClienteResponse clienteResponse;
    private DireccionRequest direccionRequest;
    private Direccion direccion;

    @BeforeEach
    void setUp() {
        // Setup test data
        cliente = new Cliente(1L, "CEDULA", "0102030405", "Juan Perez", "juan@test.com", "0999999999");
        cliente.setId(1L);
        cliente.setCreadoEn(LocalDateTime.now());

        direccion = new Direccion("Pichincha", "Quito", "Av. Principal 123", true);
        direccion.setId(1L);
        cliente.agregarDireccion(direccion);

        direccionRequest = new DireccionRequest("Pichincha", "Quito", "Av. Principal 123");

        clienteCreateRequest = new ClienteCreateRequest(1L, "CEDULA", "0102030405", 
                "Juan Perez", "juan@test.com", "0999999999", direccionRequest);

        clienteUpdateRequest = new ClienteUpdateRequest("RUC", "0999999999001", 
                "Juan Perez Empresa", "juan.empresa@test.com", "0988888888");

        clienteResponse = new ClienteResponse(1L, 1L, "CEDULA", "0102030405", 
                "Juan Perez", "juan@test.com", "0999999999", LocalDateTime.now());
    }

    @Test
    void buscarClientes_ConBusqueda_DeberiaRetornarClientesEncontrados() {
        // Given
        Long empresaId = 1L;
        String busqueda = "Juan";
        List<Cliente> clientes = Arrays.asList(cliente);
        List<ClienteResponse> clientesResponse = Arrays.asList(clienteResponse);

        when(clienteRepository.buscarClientesPorEmpresaYTexto(empresaId, busqueda))
                .thenReturn(clientes);
        when(clienteMapper.toResponseListWithMatriz(clientes))
                .thenReturn(clientesResponse);

        // When
        List<ClienteResponse> result = clienteService.buscarClientes(empresaId, busqueda);

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getNombres()).isEqualTo("Juan Perez");
        verify(clienteRepository).buscarClientesPorEmpresaYTexto(empresaId, busqueda);
    }

    @Test
    void buscarClientes_SinBusqueda_DeberiaRetornarTodosLosClientes() {
        // Given
        Long empresaId = 1L;
        List<Cliente> clientes = Arrays.asList(cliente);
        List<ClienteResponse> clientesResponse = Arrays.asList(clienteResponse);

        when(clienteRepository.findByEmpresaIdWithDirecciones(empresaId))
                .thenReturn(clientes);
        when(clienteMapper.toResponseListWithMatriz(clientes))
                .thenReturn(clientesResponse);

        // When
        List<ClienteResponse> result = clienteService.buscarClientes(empresaId, null);

        // Then
        assertThat(result).hasSize(1);
        verify(clienteRepository).findByEmpresaIdWithDirecciones(empresaId);
    }

    @Test
    void crearCliente_ConDatosValidos_DeberiaCrearCliente() {
        // Given
        when(clienteRepository.existsByEmpresaIdAndTipoIdentificacionAndNumeroIdentificacion(
                anyLong(), anyString(), anyString())).thenReturn(false);
        when(clienteMapper.toEntity(clienteCreateRequest)).thenReturn(cliente);
        when(direccionMapper.toEntity(direccionRequest, true)).thenReturn(direccion);
        when(clienteRepository.save(any(Cliente.class))).thenReturn(cliente);
        when(clienteMapper.toResponse(cliente)).thenReturn(clienteResponse);

        // When
        ClienteResponse result = clienteService.crearCliente(clienteCreateRequest);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getNombres()).isEqualTo("Juan Perez");
        verify(clienteRepository).save(any(Cliente.class));
    }

    @Test
    void crearCliente_ConIdentificacionExistente_DeberiaLanzarBusinessException() {
        // Given
        when(clienteRepository.existsByEmpresaIdAndTipoIdentificacionAndNumeroIdentificacion(
                anyLong(), anyString(), anyString())).thenReturn(true);

        // When & Then
        assertThatThrownBy(() -> clienteService.crearCliente(clienteCreateRequest))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("Ya existe un cliente");
    }

    @Test
    void crearCliente_ConTipoIdentificacionInvalido_DeberiaLanzarBusinessException() {
        // Given
        clienteCreateRequest.setTipoIdentificacion("INVALIDO");

        // When & Then
        assertThatThrownBy(() -> clienteService.crearCliente(clienteCreateRequest))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("Tipo de identificación no válido");
    }

    @Test
    void actualizarCliente_ConIdExistente_DeberiaActualizarCliente() {
        // Given
        Long id = 1L;
        when(clienteRepository.findById(id)).thenReturn(Optional.of(cliente));
        when(clienteRepository.existsByEmpresaIdAndTipoIdentificacionAndNumeroIdentificacionAndIdNot(
                anyLong(), anyString(), anyString(), anyLong())).thenReturn(false);
        when(clienteRepository.save(any(Cliente.class))).thenReturn(cliente);
        when(clienteMapper.toResponse(cliente)).thenReturn(clienteResponse);

        // When
        ClienteResponse result = clienteService.actualizarCliente(id, clienteUpdateRequest);

        // Then
        assertThat(result).isNotNull();
        verify(clienteMapper).updateEntity(cliente, clienteUpdateRequest);
        verify(clienteRepository).save(cliente);
    }

    @Test
    void actualizarCliente_ConIdInexistente_DeberiaLanzarResourceNotFoundException() {
        // Given
        Long id = 999L;
        when(clienteRepository.findById(id)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> clienteService.actualizarCliente(id, clienteUpdateRequest))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Cliente no encontrado");
    }

    @Test
    void eliminarCliente_ConIdExistente_DeberiaEliminarCliente() {
        // Given
        Long id = 1L;
        when(clienteRepository.findById(id)).thenReturn(Optional.of(cliente));

        // When
        clienteService.eliminarCliente(id);

        // Then
        verify(clienteRepository).delete(cliente);
    }

    @Test
    void eliminarCliente_ConIdInexistente_DeberiaLanzarResourceNotFoundException() {
        // Given
        Long id = 999L;
        when(clienteRepository.findById(id)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> clienteService.eliminarCliente(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Cliente no encontrado");
    }

    @Test
    void obtenerClientePorId_ConIdExistente_DeberiaRetornarCliente() {
        // Given
        Long id = 1L;
        when(clienteRepository.findByIdWithDirecciones(id)).thenReturn(Optional.of(cliente));
        when(clienteMapper.toResponse(cliente)).thenReturn(clienteResponse);

        // When
        ClienteResponse result = clienteService.obtenerClientePorId(id);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(id);
    }

    @Test
    void obtenerClientePorId_ConIdInexistente_DeberiaLanzarResourceNotFoundException() {
        // Given
        Long id = 999L;
        when(clienteRepository.findByIdWithDirecciones(id)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> clienteService.obtenerClientePorId(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Cliente no encontrado");
    }

    @Test
    void existeCliente_ConIdExistente_DeberiaRetornarTrue() {
        // Given
        Long id = 1L;
        when(clienteRepository.existsById(id)).thenReturn(true);

        // When
        boolean result = clienteService.existeCliente(id);

        // Then
        assertThat(result).isTrue();
    }

    @Test
    void existeCliente_ConIdInexistente_DeberiaRetornarFalse() {
        // Given
        Long id = 999L;
        when(clienteRepository.existsById(id)).thenReturn(false);

        // When
        boolean result = clienteService.existeCliente(id);

        // Then
        assertThat(result).isFalse();
    }

    @Test
    void contarClientesPorEmpresa_DeberiaRetornarConteo() {
        // Given
        Long empresaId = 1L;
        long expectedCount = 5L;
        when(clienteRepository.countByEmpresaId(empresaId)).thenReturn(expectedCount);

        // When
        long result = clienteService.contarClientesPorEmpresa(empresaId);

        // Then
        assertThat(result).isEqualTo(expectedCount);
    }
}