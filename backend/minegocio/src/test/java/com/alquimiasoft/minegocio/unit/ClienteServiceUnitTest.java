package com.alquimiasoft.minegocio.unit;

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
import com.alquimiasoft.minegocio.service.ClienteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Tests unitarios para ClienteService usando Mockito
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("ClienteService - Tests Unitarios")
class ClienteServiceUnitTest {

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private ClienteMapper clienteMapper;

    @Mock
    private DireccionMapper direccionMapper;

    @InjectMocks
    private ClienteService clienteService;

    private Cliente clienteMock;
    private ClienteCreateRequest createRequestMock;
    private ClienteResponse responseEsperado;

    @BeforeEach
    void setUp() {
        // Configurar mocks base
        clienteMock = new Cliente(
                1L,
                "CEDULA",
                "1234567890",
                "Juan Pérez",
                "juan@test.com",
                "0999888777"
        );

        createRequestMock = new ClienteCreateRequest();
        createRequestMock.setEmpresaId(1L);
        createRequestMock.setTipoIdentificacion("CEDULA");
        createRequestMock.setNumeroIdentificacion("1234567890");
        createRequestMock.setNombres("Juan Pérez");
        createRequestMock.setCorreo("juan@test.com");
        createRequestMock.setCelular("0999888777");

        DireccionRequest direccionRequest = new DireccionRequest();
        direccionRequest.setProvincia("Pichincha");
        direccionRequest.setCiudad("Quito");
        direccionRequest.setDireccion("Av. Principal 123");
        createRequestMock.setDireccionMatriz(direccionRequest);

        responseEsperado = new ClienteResponse(
                1L, 1L,
                "CEDULA",
                "1234567890",
                "Juan Pérez",
                "juan@test.com",
                "0999888777",
                null
        );
    }

    @Test
    @DisplayName("Crear cliente - Caso exitoso")
    void crearCliente_CasoExitoso() {
        // Given
        when(clienteRepository.existsByEmpresaIdAndTipoIdentificacionAndNumeroIdentificacion(
                anyLong(), anyString(), anyString()))
                .thenReturn(false);
        
        when(clienteMapper.toEntity(any(ClienteCreateRequest.class)))
                .thenReturn(clienteMock);
        
        when(direccionMapper.toEntity(any(DireccionRequest.class), eq(true)))
                .thenReturn(new Direccion());
        
        when(clienteRepository.save(any(Cliente.class)))
                .thenReturn(clienteMock);
        
        when(clienteMapper.toResponse(any(Cliente.class)))
                .thenReturn(responseEsperado);

        // When
        ClienteResponse resultado = clienteService.crearCliente(createRequestMock);

        // Then
        assertThat(resultado).isNotNull();
        assertThat(resultado.getNombres()).isEqualTo("Juan Pérez");
        
        verify(clienteRepository).existsByEmpresaIdAndTipoIdentificacionAndNumeroIdentificacion(
                1L, "CEDULA", "1234567890");
        verify(clienteRepository).save(any(Cliente.class));
        verify(clienteMapper).toEntity(createRequestMock);
        verify(clienteMapper).toResponse(clienteMock);
    }

    @Test
    @DisplayName("Crear cliente - Cliente ya existe")
    void crearCliente_ClienteYaExiste() {
        // Given
        when(clienteRepository.existsByEmpresaIdAndTipoIdentificacionAndNumeroIdentificacion(
                anyLong(), anyString(), anyString()))
                .thenReturn(true);

        // When & Then
        assertThatThrownBy(() -> clienteService.crearCliente(createRequestMock))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("Ya existe un cliente");

        verify(clienteRepository, never()).save(any(Cliente.class));
    }

    @Test
    @DisplayName("Obtener cliente por ID - Cliente existe")
    void obtenerClientePorId_ClienteExiste() {
        // Given
        Long clienteId = 1L;
        when(clienteRepository.findByIdWithDirecciones(clienteId))
                .thenReturn(Optional.of(clienteMock));
        when(clienteMapper.toResponse(clienteMock))
                .thenReturn(responseEsperado);

        // When
        ClienteResponse resultado = clienteService.obtenerClientePorId(clienteId);

        // Then
        assertThat(resultado).isNotNull();
        assertThat(resultado.getId()).isEqualTo(clienteId);
        
        verify(clienteRepository).findByIdWithDirecciones(clienteId);
        verify(clienteMapper).toResponse(clienteMock);
    }

    @Test
    @DisplayName("Obtener cliente por ID - Cliente no existe")
    void obtenerClientePorId_ClienteNoExiste() {
        // Given
        Long clienteId = 999L;
        when(clienteRepository.findByIdWithDirecciones(clienteId))
                .thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> clienteService.obtenerClientePorId(clienteId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Cliente no encontrado");

        verify(clienteRepository).findByIdWithDirecciones(clienteId);
        verify(clienteMapper, never()).toResponse(any(Cliente.class));
    }

    @Test
    @DisplayName("Buscar clientes - Lista todos los clientes")
    void buscarClientes_ListaTodos() {
        // Given
        Long empresaId = 1L;
        String busqueda = "";
        List<Cliente> clientesEncontrados = Arrays.asList(clienteMock);
        List<ClienteResponse> responsesEsperados = Arrays.asList(responseEsperado);

        when(clienteRepository.findByEmpresaIdWithDirecciones(empresaId))
                .thenReturn(clientesEncontrados);
        when(clienteMapper.toResponseListWithMatriz(clientesEncontrados))
                .thenReturn(responsesEsperados);

        // When
        List<ClienteResponse> resultado = clienteService.buscarClientes(empresaId, busqueda);

        // Then
        assertThat(resultado).isNotNull();
        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getNombres()).isEqualTo("Juan Pérez");

        verify(clienteRepository).findByEmpresaIdWithDirecciones(empresaId);
        verify(clienteMapper).toResponseListWithMatriz(clientesEncontrados);
    }

    @Test
    @DisplayName("Actualizar cliente - Caso exitoso")
    void actualizarCliente_CasoExitoso() {
        // Given
        Long clienteId = 1L;
        ClienteUpdateRequest updateRequest = new ClienteUpdateRequest();
        updateRequest.setNombres("Juan Pérez Actualizado");
        updateRequest.setCorreo("juan.nuevo@test.com");
        updateRequest.setTipoIdentificacion("CEDULA");
        updateRequest.setNumeroIdentificacion("1234567890");

        when(clienteRepository.findById(clienteId))
                .thenReturn(Optional.of(clienteMock));
        when(clienteRepository.existsByEmpresaIdAndTipoIdentificacionAndNumeroIdentificacionAndIdNot(
                anyLong(), anyString(), anyString(), anyLong()))
                .thenReturn(false);
        when(clienteRepository.save(any(Cliente.class)))
                .thenReturn(clienteMock);
        when(clienteMapper.toResponse(any(Cliente.class)))
                .thenReturn(responseEsperado);

        // When
        ClienteResponse resultado = clienteService.actualizarCliente(clienteId, updateRequest);

        // Then
        assertThat(resultado).isNotNull();
        
        verify(clienteRepository).findById(clienteId);
        verify(clienteMapper).updateEntity(clienteMock, updateRequest);
        verify(clienteRepository).save(clienteMock);
        verify(clienteMapper).toResponse(clienteMock);
    }

    @Test
    @DisplayName("Eliminar cliente - Caso exitoso")
    void eliminarCliente_CasoExitoso() {
        // Given
        Long clienteId = 1L;
        when(clienteRepository.findById(clienteId))
                .thenReturn(Optional.of(clienteMock));

        // When
        clienteService.eliminarCliente(clienteId);

        // Then
        verify(clienteRepository).findById(clienteId);
        verify(clienteRepository).delete(clienteMock);
    }

    @Test
    @DisplayName("Eliminar cliente - Cliente no existe")
    void eliminarCliente_ClienteNoExiste() {
        // Given
        Long clienteId = 999L;
        when(clienteRepository.findById(clienteId))
                .thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> clienteService.eliminarCliente(clienteId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Cliente no encontrado");

        verify(clienteRepository).findById(clienteId);
        verify(clienteRepository, never()).delete(any(Cliente.class));
    }
}